package posts

data class Geo(
    val type: String = "",
    val coordinates: String = "",
    val place: Place? = Place()
)