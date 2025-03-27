import io.github.cdimascio.dotenv.dotenv
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException
import kotlinx.coroutines.*
import java.util.Scanner

@Serializable
data class MovieRecommendation(
    val page: Int,
    val results: List<Results>,
    val total_pages: Int,
    val total_results: Int
)

@Serializable
data class Results(
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

@Serializable
data class Genres(
    val genres: List<Genre>
)

@Serializable
data class Genre(
    val id: Int,
    val name: String
)


class Main {
    private val genreMap = mapOf("Action" to 28,
        "Adventure" to 12,
        "Animation" to 16,
        "Comedy" to 35,
        "Crime" to 80,
        "Documentary" to 99,
        "Drama" to 18,
        "Family" to 10751,
        "Fantasy" to 14,
        "History" to 36,
        "Horror" to 27,
        "Music" to 10402,
        "Mystery" to 9648,
        "Romance" to 10749,
        "Science Fiction" to 878,
        "TV Movie" to 10770,
        "Thriller" to 53,
        "War" to 10752,
        "Western" to 37)

    private fun loadApiKey(): String {
        val dotenv = dotenv()
        return dotenv["API_KEY"] ?: throw IllegalStateException("API_KEY not found in .env file")
    }
    private fun getGenres(client: OkHttpClient, API_KEY: String) {
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/genre/movie/list?language=en")
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer ${API_KEY}")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val json = response.body?.string() ?: throw IOException("Empty response body")
            val genres = Json.decodeFromString<Genres>(json)
            genres.genres.forEach() {
                if(it.id == 37) {
                    println(it.name)
                } else if(it.name=="Horror"){
                    println("${it.name},")
                } else{
                    print("${it.name}, ")
                }
            }
        }
    }

    private fun <K, V> keyOf(map: Map<K, V>, value: V): K? {
        return map.entries.firstOrNull { it.value == value }?.key
    }

    private fun getRecommendations(printer: ConsolePrinter, client: OkHttpClient, API_KEY: String, genre: Int?, year: Int?, minRating: Double=0.0, maxResults: Int=3) {

        val baseUrl = "https://api.themoviedb.org/3/discover/movie?vote_average.gte=$minRating&sort_by=popularity.desc&language=en-US&page=1"
        val yearUrl = if (year != null) "$baseUrl&primary_release_year=$year" else baseUrl
        val url = if (genre != null) "$yearUrl$&with_genres=$genre" else yearUrl
        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer $API_KEY")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful)  println("Unexpected code $response")

            val jsonString = response.body?.string()
            if (jsonString == null) {
                printer.type("Cannot find any movie meeting your requirements")
            } else {
                val json = Json {
                    coerceInputValues=true
                }
                val recommendations = json.decodeFromString<MovieRecommendation>(jsonString)
                if(recommendations.results.isEmpty()) {

                    printer.type("Cannot find any movie meeting your requirements")
                    println()

                } else {
                    val genreString = keyOf(genreMap, genre) ?: "any"
                    val yearString = year ?: "any"
                    val these = if(maxResults == 1) "this" else "these"
                    val film = if(maxResults == 1) "movie" else "movies"
                    printer.type("Okay, I recommend you $these $maxResults $genreString $film from $yearString year: ")
                    println()
                    recommendations.results.take(maxResults).forEach { movie ->
                        printer.type("${movie.title} from ${movie.release_date} with rating ${movie.vote_average}")
                        println()
                    }
                }
            }
        }
    }

    private fun getGenre(prompt: String): String? {
        val regex = Regex("Recommend me \\d*\\s?((?:\\w+\\s?){1,2}) movies?")
        val genre = regex.find(prompt)?.groups?.get(1)?.value
        return if (genre == "any") null else genre
    }

    private fun getYear(prompt: String): Int? {
        val regex = Regex("from (\\d{4}|any)")
        val year = regex.find(prompt)?.groups?.get(1)?.value
        return if (year == "any") null else year?.toIntOrNull()
    }

    private fun getMaxResults(prompt: String): Int {
        val regex = Regex("Recommend me (\\d+)?\\s?\\w+(?:\\s\\w+)? movies?")
        val maxResults = regex.find(prompt)?.groups?.get(1)?.value?.toIntOrNull()
        return maxResults ?: 1
    }

    private fun getRating(prompt: String): Double {
        val regex = Regex("with minimum rating of (\\d+(\\.\\d+)?|any)")
        val rating = regex.find(prompt)?.groups?.get(1)?.value
        if (rating != null) {
            return if (rating == "any") 0.0 else rating.toDouble()
        } else return 0.0

    }
    private fun checkCorrectness(prompt: String): Boolean {
        val regex = Regex("""(?i)Recommend me (\d+)?\s?((?:\w+\s?){1,2})\s?movies? from (\d{4}|any)( year)? with minimum rating of (\d+(\.\d+)?|any)(\n)?""")
        return regex.matches(prompt)
    }

    fun run() = runBlocking {
        val client = OkHttpClient()
        val printer = ConsolePrinter()
        val API_KEY = loadApiKey()
        val scanner = Scanner(System.`in`)

        var start = 1
        var helping = 0
        var responded = 0
        var invalid = 0
        var count = 0


        printer.printStart()
        while(true) {
            if(start == 1) {
                print("Movie Agent: ")
                printer.type("Hi! I'm Your movie recomendadion agent. Ask me for a recomendation, type 'help' to see prompt syntax or type 'exit' to quit")
                start = 0
            } else if (helping == 1) {
                print("Movie Agent: ")
                printer.type("Okay, Now how can I help You?")
                helping = 0
            } else if(invalid == 1) {
                print("Movie Agent: ")
                printer.type("Don't worry, You can type 'help' if you want or try again")
                invalid = 0
            } else if(responded == 1) {
                print("Movie Agent: ")
                printer.type("If you want something else just ask me again or if you want to quit type 'exit'")
                responded = 0
            } else {
                print("Movie Agent: ")
                printer.type("What can I do for You?")
            }
            print("\nUser: ")
            val prompt = if(scanner.hasNextLine()) scanner.nextLine() else ""
            print("Movie Agent: ")
            if(prompt == "exit") {
                printer.type("Goodbye! [end credits]\n")
                printer.endCredits()
                break;
            } else if(prompt == "help") {
                printer.type("Printing help for You")
                println()
                println()
                delay(300)
                printer.printHelp()
                getGenres(client, API_KEY)
                println()
                helping = 1
            } else {
                if (count < 39) {
                    try {
                        if (prompt == "") throw Exception("Empty prompt")
                        val genre_string = getGenre(prompt)
                        val year = getYear(prompt)
                        val minRating = getRating(prompt)
                        val maxResults = getMaxResults(prompt)



                        if (!checkCorrectness(prompt)) {
                            //print("not working")
                            throw Exception("Invalid prompt")
                        }
                        val genre = genreMap[genre_string]
                        //println("$genre, $year, $minRating, $maxResults")
                        getRecommendations(printer, client, API_KEY, genre, year, minRating, maxResults)
                        count++
                        responded = 1
                    } catch (e: Exception) {
                        //println(e)
                        invalid = 1
                        printer.type("Invalid prompt")
                        println()
                    }
                }
            }
        }
    }
}

fun main() {
    val agent = Main()
    agent.run()
}
