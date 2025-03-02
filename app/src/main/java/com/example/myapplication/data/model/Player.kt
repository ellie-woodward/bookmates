import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Player(
    @Json(name = "_id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "wins") val wins: Int,
    @Json(name = "games_total") val totalGames: Int,
    @Json(name = "type") val type: String
) {
}
