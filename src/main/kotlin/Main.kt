import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException

@Serializable
data class MovieRecomendation(
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

fun main() {
    val client = OkHttpClient()

    val API_KEY = loadApiKey()
    println(API_KEY)

    val request = Request.Builder()
        .url("https://api.themoviedb.org/3/discover/movie?with_genres=28&sort_by=popularity.desc&language=en-US&page=1")
        .get()
        .addHeader("accept", "application/json")
        .addHeader("Authorization", "Bearer $API_KEY")
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        val json = response.body?.string() ?: throw IOException("Empty response body")
        val recommendations = Json.decodeFromString<MovieRecomendation>(json)

        recommendations.results.take(10).forEach { movie ->
            println("Title: ${movie.title}, Release Date: ${movie.release_date}, Rating: ${movie.vote_average}")
        }
    }



}