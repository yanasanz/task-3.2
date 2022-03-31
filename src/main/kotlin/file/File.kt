package file

data class File(
    val id: Int = 0,
    val owner_id: Int = 0,
    val title: String = "",
    val size: Int = 0,
    val ext: String = "",
    val url: String = "",
    val date: Long = System.currentTimeMillis(),
    val type: Int = 0,
    val preview: Preview? = Preview()
)