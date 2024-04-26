package com.example.myapplication

import java.util.UUID

data class Player(
    val playerId: UUID,
    val playerName: String,
    var wins: Int,
    var gamesPlayed: Int,
    var stats: MutableMap<String, winLoss> = mutableMapOf()
)

data class winLoss(
    var wins: Int =0 ,
    var losses: Int = 0
)



object PlayerList {
    private val players: MutableList<Player> = mutableListOf()

    init{
        players.add(
            Player(
            UUID.randomUUID(),
            "Stephen",
            0,
            0,

            )
        )

        players.add(
            Player(
            UUID.randomUUID(),
            "Trent",
            0,
            0,

            )
        )

        players.add(
            Player(
            UUID.randomUUID(),
            "Ellie",
            0,
            0,

            )
        )

        players.add(
            Player(
            UUID.randomUUID(),
            "Ronin",
            0,
            0,

            )
        )
    }


    fun getPlayers(): List<Player>{
        return players.toList()
    }


    fun addPlayer(player: Player){
        players.add(player)
    }

    fun getPlayerNames(): List<String> {
        return players.map{ it.playerName}
    }

    fun getPlayer(name: String): Player? {
        return players.find{it.playerName == name}
    }



}