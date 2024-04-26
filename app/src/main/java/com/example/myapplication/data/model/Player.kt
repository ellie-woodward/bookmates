import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Player(
    val id: String,
    val name: String,
    val wins: Int,
    val totalGames: Int,
    val type: String
) {
}
