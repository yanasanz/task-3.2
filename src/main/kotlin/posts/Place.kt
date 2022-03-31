package posts

data class Place(
    val id: Int = 0,
    val title: String = "",
    val latitude: Int = 0,
    val longitude: Int = 0,
    val created: Long = System.currentTimeMillis(),
    val icon: String = "",
    val chekins: Int = 0,
    val updated: Long = System.currentTimeMillis(),
    val type:Int = 0,
    val country: Int = 0,
    val city: Int = 0,
    val address: String = ""
)