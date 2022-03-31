package link

import link.Button
import link.Product
import photo.Photo

data class Link(
    val url: String = "",
    val title: String = "",
    val caption: String? = "",
    val description: String = "",
    val photo: Photo? = Photo(),
    val product: Product = Product(),
    val button: Button = Button(),
    val preview_page: String? = "",
    val preview_url: String = ""
)