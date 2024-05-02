package com.example.myapplication


import android.util.Log
import androidx.room.PrimaryKey
import com.example.myapplication.data.api.BookMatesApi
import com.example.myapplication.data.model.BoardGameData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import com.example.myapplication.data.model.Game
import com.example.myapplication.data.model.CreatedPlayer

import java.util.UUID
data class Game(
    @PrimaryKey val gameId: UUID = UUID.randomUUID(),
    val templateId: UUID,
    var title: String,
    val players: List<CreatedPlayer>,
    val scores: MutableMap<UUID, MutableList<Int>> = mutableMapOf(),
    var finished: Boolean = false
){
    constructor(templateID: UUID, title: String, players: List<CreatedPlayer>,scores: MutableMap<UUID, MutableList<Int>>) : this(UUID.randomUUID(), templateID,title, players, scores)
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




object Games {

    private val gamesList: MutableList<Game> = mutableListOf()
    private lateinit var api: BookMatesApi
    init{
        val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

        val retrofit: Retrofit = Retrofit.Builder()

            .baseUrl("https://bookmate.discovery.cs.vt.edu/")

            .addConverterFactory(MoshiConverterFactory.create(moshi))

            .build()

        api = retrofit.create()


        var responseGame: List<com.example.myapplication.data.model.Game> = listOf<Game>()

        api.getBoardGameListById(UserStorage.user.userId).enqueue(

            object : Callback<BoardGameData> {

                override fun onFailure(call: Call<BoardGameData>, t: Throwable) {

                    Log.e("The failure Response", t.message.toString())



                }

                override fun onResponse(call: Call<BoardGameData>, response: Response<BoardGameData>) {

                    Log.e("The success Response", response.toString())

                    responseGame = response.body()?.data!!

                }

            })
        responseGame.let {
            responseGame.forEach { game ->
                gamesList.add(game)
            }
        }
    }

    object Games {

        private val gamesList: MutableList<Game> = mutableListOf()

        // Function to create a new game
        fun createGame(matchName: String, gameName: String, templateId: String,
                       finished: Boolean, duration: Int, scores: MutableMap<String, Int>,
                       players: List<CreatedPlayer>, winner: CreatedPlayer) {
            val game = Game(id = emptyMap(), matchName = matchName, gameName = gameName,
                templateId = templateId, finished = finished, duration = duration,
                scores = scores, players = players, winner = winner)
            gamesList.add(game)
        }

        // Function to get all games
        fun getGames(): List<Game> {
            return gamesList
        }

        // Function to delete a game
        fun deleteGame(game: Game) {
            gamesList.remove(game)
        }

        // Function to get a game by its ID
        fun getGame(id: Map<String, String>): Game? {
            return gamesList.find { it.id == id }
        }

        // Function to add a game
        fun addGame(game: Game) {
            gamesList.add(game)
        }
    }


//    fun createGames(template: Template, title: String,  players: List<Player>, scores: MutableMap<UUID, MutableList<Int>>){
//        //val game = Game(template.templateId, title, players, scores)
//        val game = Game(template.templateId, title, players, scores)
//        players.forEach{
//                player ->
//            game.updateScore(player, mutableListOf(0))
//        }
//        gamesList.add(game)
//
//
//    }
//
//
//    fun getGames(): List<Game>{
//        return gamesList
//    }
//
//    fun deleteGame(game: Game){
//        gamesList.remove(game)
//    }
//
//    fun getGame(id: UUID): Game? {
//
//        return gamesList.find{it.gameId == id}
//
//    }
//
//    fun addGame(game: Game): UUID{
//        gamesList.add(game)
//        return gamesList.find { it.gameId == game.gameId }?.gameId ?: game.gameId
//    }
//



}

