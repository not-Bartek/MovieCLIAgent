import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class ConsolePrinter {
    fun printStart() {
        println()
        println(" _____ ______   ________  ___      ___ ___  _______           ________  ________  _______   ________   _________   \n" +
                "|\\   _ \\  _   \\|\\   __  \\|\\  \\    /  /|\\  \\|\\  ___ \\         |\\   __  \\|\\   ____\\|\\  ___ \\ |\\   ___  \\|\\___   ___\\ \n" +
                "\\ \\  \\\\\\__\\ \\  \\ \\  \\|\\  \\ \\  \\  /  / | \\  \\ \\   __/|        \\ \\  \\|\\  \\ \\  \\___|\\ \\   __/|\\ \\  \\\\ \\  \\|___ \\  \\_| \n" +
                " \\ \\  \\\\|__| \\  \\ \\  \\\\\\  \\ \\  \\/  / / \\ \\  \\ \\  \\_|/__       \\ \\   __  \\ \\  \\  __\\ \\  \\_|/_\\ \\  \\\\ \\  \\   \\ \\  \\  \n" +
                "  \\ \\  \\    \\ \\  \\ \\  \\\\\\  \\ \\    / /   \\ \\  \\ \\  \\_|\\ \\       \\ \\  \\ \\  \\ \\  \\|\\  \\ \\  \\_|\\ \\ \\  \\\\ \\  \\   \\ \\  \\ \n" +
                "   \\ \\__\\    \\ \\__\\ \\_______\\ \\__/ /     \\ \\__\\ \\_______\\       \\ \\__\\ \\__\\ \\_______\\ \\_______\\ \\__\\\\ \\__\\   \\ \\__\\\n" +
                "    \\|__|     \\|__|\\|_______|\\|__|/       \\|__|\\|_______|        \\|__|\\|__|\\|_______|\\|_______|\\|__| \\|__|    \\|__|")
        println()
    }

    fun printEnd() {
        println(" _________  ___  ___  _______           _______   ________   ________     \n" +
                "|\\___   ___\\\\  \\|\\  \\|\\  ___ \\         |\\  ___ \\ |\\   ___  \\|\\   ___ \\    \n" +
                "\\|___ \\  \\_\\ \\  \\\\\\  \\ \\   __/|        \\ \\   __/|\\ \\  \\\\ \\  \\ \\  \\_|\\ \\   \n" +
                "     \\ \\  \\ \\ \\   __  \\ \\  \\_|/__       \\ \\  \\_|/_\\ \\  \\\\ \\  \\ \\  \\ \\\\ \\  \n" +
                "      \\ \\  \\ \\ \\  \\ \\  \\ \\  \\_|\\ \\       \\ \\  \\_|\\ \\ \\  \\\\ \\  \\ \\  \\_\\\\ \\ \n" +
                "       \\ \\__\\ \\ \\__\\ \\__\\ \\_______\\       \\ \\_______\\ \\__\\\\ \\__\\ \\_______\\\n" +
                "        \\|__|  \\|__|\\|__|\\|_______|        \\|_______|\\|__| \\|__|\\|_______|")
    }

    fun printHelp() {
        println("=-=-=-=-=-=-=-=-= Prompt Syntax =-=-=-=-=-=-=-=-=")
        println()
        println("\"Recommend me {n} {genre} movies from {year} year with minimum rating of {rating}\"")
        println("Where n - number of movies, genre - genre (check below), year - integer year of production, rating - rating on TMDB (0.0-10.0)")
        println("Example:")
        println("Recommend me 2 Action movies from 2015 year with minimum rating of 7.5")
        println("If You don't want to specify genre, year or rating you can type 'any' instead of actual value")
        println("For example:")
        println("Recommend me 4 any movies from any year with minimum rating of 5")
        println()
        println("If you want one movie you can type instead:")
        println("\"Recommend me {genre} movie from {year} year with minimum rating of {rating}\"")
        println()
        println("=-=-=-=-=-=-=-=-= Error Handling =-=-=-=-=-=-=-=-=")
        println()
        println("If your prompt is invalid, You will have to type values manually")
        println()
        println("=-=-=-=-=-= List of all possible genres =-=-=-=-=-=")
        println()
    }

    fun type(prompt: String) = runBlocking {
        prompt.forEach() {
            print(it)
            delay(30)
        }
    }

    fun endCredits() = runBlocking {
        type("Written and Directed by: ")
        delay(500)
        type("Bartosz Dźwigała")
        println()
        delay(500)

        type("Produced by: ")
        delay(500)
        type("Bartosz Dźwigała")
        println()
        delay(500)

        type("CAST")
        println()
        delay(500)

        type("Programmer: ")
        delay(500)
        type("Bartosz Dźwigała")
        println()
        delay(500)
        printEnd()
    }
}