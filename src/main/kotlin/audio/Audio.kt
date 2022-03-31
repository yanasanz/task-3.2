package audio

data class Audio(
    val id: Int = 0,
    val owner_id: Int = 0,
    val artist: String = "",
    val title: String = "",
    val duration: Int = 0,
    val url: String = "",
    val lyrics_id: Int = 0,
    val album_id: Int = 0,
    val genre_id: Int = 0,
    val date: Int = 0,
    val no_search: Boolean = false,
    val is_hq: Boolean = false
)