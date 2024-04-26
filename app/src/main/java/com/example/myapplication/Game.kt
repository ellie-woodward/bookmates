package com.example.myapplication


import androidx.room.PrimaryKey

import java.util.UUID
data class Game(
    @PrimaryKey val gameId: UUID = UUID.randomUUID(),
    val templateId: UUID,
    var title: String,
    val players: List<Player>,
    val scores: MutableMap<UUID, MutableList<Int>> = mutableMapOf(),
    var finished: Boolean = false
){
    constructor(templateID: UUID, title: String, players: List<Player>,scores: MutableMap<UUID, MutableList<Int>>) : this(UUID.randomUUID(), templateID,title, players, scores)
    fun updateScore(player: Player, score: MutableList<Int>){
        scores[player.playerId] = score
    }

    fun updateFinished(){
        finished = !finished
    }

    fun updateTitle(text: String){
        title = text
    }
}




object Games{

    private val gamesList: MutableList<Game> = mutableListOf()


    fun createGames(template: Template, title: String,  players: List<Player>, scores: MutableMap<UUID, MutableList<Int>>){
        val game = Game(template.templateId, title, players, scores)
        players.forEach{
                player ->
            game.updateScore(player, mutableListOf(0))
        }
        gamesList.add(game)


    }


    fun getGames(): List<Game>{
        return gamesList
    }

    fun deleteGame(game: Game){
        gamesList.remove(game)
    }

    fun getGame(id: UUID): Game? {

        return gamesList.find{it.gameId == id}

    }

    fun addGame(game: Game): UUID{
        gamesList.add(game)
        return gamesList.find { it.gameId == game.gameId }?.gameId ?: game.gameId
    }




}