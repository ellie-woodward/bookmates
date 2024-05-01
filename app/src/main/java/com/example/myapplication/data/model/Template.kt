package com.example.myapplication.data.model

import android.content.Intent
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Duration

@JsonClass(generateAdapter = true)
data class Template(
    @Json(name = "_id") val id: Map<String, String>,
    @Json(name = "game_name") val name: String,
    @Json(name = "game_type") val gameType: String,
    @Json(name = "score_type") val scoreType: String,
    @Json(name = "avg_duration")val duration: Int,
    @Json(name = "num_players")val numberPlayers: Int,
    @Json(name = "winner") val winner: Int,
    @Json(name = "game_template") val template: List<Int>,
    @Json(name = "type") val type: String
) {
}