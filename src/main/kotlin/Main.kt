import io.github.cdimascio.dotenv.dotenv
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException
import kotlinx.coroutines.*

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
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

fun loadApiKey(): String {
    val dotenv = dotenv()
    return dotenv["API_KEY"] ?: throw IllegalStateException("API_KEY not found in .env file")
}



fun getRecommendations(client: OkHttpClient, API_KEY: String, genre: Int?, year: Int?, minRating: Double=0.0, maxResults: Int=3) {
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
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        val json = response.body?.string() ?: throw IOException("Empty response body")
        val recommendations = Json.decodeFromString<MovieRecommendation>(json)

        recommendations.results.take(maxResults).forEach { movie ->
            println("Title: ${movie.title}, Release Date: ${movie.release_date}, Rating: ${movie.vote_average}")
        }
    }
}

fun main() = runBlocking {
    val client = OkHttpClient()
    val printer = ConsolePrinter()
    val API_KEY = loadApiKey()


    var start = 1
    var helping = 0
    var responded = 0
    var invalid = 0
    var count = 0
    printer.printStart()
    while(true) {
        if(start == 1) {
            print("Movie Agent: ")
            printer.type("Hi! I'm your private movie recomendadion agent. Ask me for a recomendation, type 'help' to see possible commands or type 'exit' to quit")
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
        val prompt = readLine()
        print("Movie Agent: ")
        if(prompt == "exit") {
            printer.type("Goodbye! [end credits]\n")
            printer.endCredits()
            break;
        } else if(prompt == "help") {
            printer.type("Printing help for You")
            println()
            delay(300)
            printer.printHelp()
            helping = 1
        } else {
            if(count < 39)
            try {
                print("Enter genre ID: ")
                val genre = readLine()?.toIntOrNull()// default to Action genre
                print("Enter release year: ")
                val year = readLine()?.toIntOrNull() // default to current year
                print("Enter minimum rating: ")
                val minRating = readLine()?.toDoubleOrNull() ?: 0.0 // default to 0.0
                print("Enter number of results: ")
                val maxResults = readLine()?.toIntOrNull() ?: 3 // default to 3 results

                getRecommendations(client, API_KEY, genre, year, minRating, maxResults)
                count++
                responded = 1
            } catch (e: Exception) {
                invalid = 1
                print("Movie Agent: ")
                printer.type("Invalid prompt")
            }
        }
    }



}