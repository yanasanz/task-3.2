package file

data class AudioMessage(
    val duration: Int = 0,
    val waveform: Array<Int> = arrayOf(),
    val link_ogg: String = "",
    val link_mp3: String = ""
)