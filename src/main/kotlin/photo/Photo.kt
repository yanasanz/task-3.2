package photo

data class Photo(
    val id: Int = 0,
    val owner_id: Int = 0,
    val album_id: Int = 0,
    val user_id: Int = 0,
    val text: String = "",
    val date: Long = System.currentTimeMillis(),
    val sizes: Array<PhotoSize>? = arrayOf(),
    val width: Int = 0,
    val height: Int = 0
)