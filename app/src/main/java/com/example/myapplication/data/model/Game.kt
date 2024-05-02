package com.example.myapplication.data.model

import Player
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.UUID

@JsonClass(generateAdapter = true)
data class Game(
    @Json(name = "_id") val id: Map<String, String>,
    @Json(name = "match_name") var matchName: String,
    @Json(name = "game_name") val gameName: String,
    @Json(name = "template_id") val templateId: String,
    @Json(name = "is_finished") var finished: Boolean,
    @Json(name = "duration") val duration: Int,
    @Json(name = "scores_dict") var scores: MutableMap<String, Int>,
    @Json(name = "players") val players: List<CreatedPlayer>,
    @Json(name = "winner") var winner: CreatedPlayer
){

    companion object {
        // Function to create a new game
        fun createGame(
            matchName: String,
            gameName: String,
            templateId: String,
            finished: Boolean,
            duration: Int,
            scores: MutableMap<String, Int>,
            players: List<CreatedPlayer>,
            winner: CreatedPlayer
        ): Game {
            // You can generate a unique ID for the game using UUID.randomUUID()
            val gameId = UUID.randomUUID().toString()

            // Return a new instance of the Game class with the provided parameters
            return Game(
                id = mapOf("_id" to gameId), // Set the game ID
                matchName = matchName,
                gameName = gameName,
                templateId = templateId,
                finished = finished,
                duration = duration,
                scores = scores,
                players = players,
                winner = winner
            )
        }
    }

    fun updateTitle(newTitle: String) {
        // You can update other fields similarly
        // For example, if you want to update matchName:
        // matchName = newMatchName
        matchName = newTitle
    }

    // Function to add a player to the game
    fun addPlayer(player: Player) {
        // Add the player to the list of players
        // players.add(player)


    }

    // Function to remove a player from the game
    fun removePlayer(player: Player) {
        // Remove the player from the list of players
        // players.remove(player)
    }

    // Function to update the scores for a player
    fun updateScores(playerId: String, newScore: Int) {
        // Update the score for the specified player
        // scores[playerId] = newScore
        scores[playerId] = newScore
    }

    // Function to check if the game is finished
    fun isGameFinished(): Boolean {
        // Return whether the game is finished
        // return finished
        return finished
    }

    fun updateFinished(){
        finished = !finished
    }

    // Function to determine the winner of the game
    fun determineWinner(): Player? {
        // Implement logic to determine the winner
        // For example, find the player with the highest score
        // and return that player. If there's a tie, you might
        // need to define additional rules to break the tie.
        // return winner
        var winner: Player
        var score = 0

        return null
    }
}


@JsonClass(generateAdapter = true)
data class BoardGameData(
    @Json(name= "boardgame_data") val data: List<Game>
)




