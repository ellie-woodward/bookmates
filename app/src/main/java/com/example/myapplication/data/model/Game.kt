package com.example.myapplication.data.model

import com.example.myapplication.Player

data class Game(
    val id: String,
    val matchName: String,
    val gameName: String,
    val templateId: String,
    val finished: Boolean,
    val duration: Int,
    val scores: LinkedHashMap<String, Int>,
    val players: List<Player>
) {
}