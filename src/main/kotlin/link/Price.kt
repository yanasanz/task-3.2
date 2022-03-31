package link

data class Price(
    val amount: Int = 0,
    val currency: Currency = Currency(),
    val text: String = ""
)