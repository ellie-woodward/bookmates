package com.example.myapplication.data.model

import android.content.Intent
import java.time.Duration

data class Template(
    val id: String,
    val name: String,
    val gameType: String,
    val scoreType: String,
    val duration: Int,
    val numberPlayers: Int,
    val winner: Int,
    val template: List<Int>,
    val type: String
) {
}