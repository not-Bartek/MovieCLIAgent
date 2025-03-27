# Movie CLI Agent
## Overview
This program provides command-line interface application that simulates AI Agent.
App does not use an LLM as a backbone but instead uses TMDB API.
## Requirements
- JDK 11 or newer
- Gradle 6.0 or newer
- TMDB account and API key

*Optional:*
- IntelliJ IDEA installed
## Usage
To run the application, follow these steps:
1. Clone the repository: `git clone <URL>`
2. Navigate to the project directory: `cd <project_name>`
3. Configure the bearer TMDB API key in the `/.env` file,
 *.env* file should look like this:
`API_KEY=[Your api key]`
4. Build the application: `./gradlew build`
5. Run the application: `./gradlew run`
### Prompt format
The application accepts queries in the following format:
`Recommend me {n} {genre} movies from {year} year with minimum rating of {rating}`
Where:
- `n` - maximum number of movies
- `genre` - movie genre
- `year` - year of production
- `rating` - minimum rating on TMDB (0.0-10.0)<br>
Example:
- `Recommend me 2 Action movies from 2015 year with minimum rating of 7`
- `Recommend me 4 any movies from any year with minimum rating of 5`
- `Recommend me Western movie from 2012 with minimum rating of 6`
### Possible commands
- `exit` - terminate the application
- `help` - display help
## Implementation
### General
The application is written in Kotlin and uses Gradle as the build system.
### Movie API
The application uses the TMDB API to fetch movie information and recommendations.

### Regex
The application uses regular expressions to parse user queries.

## Author
Bartosz Dźwigała