package file

data class Preview(
    val photo: FilePhoto? = FilePhoto(),
    val graffiti: Graffiti? = Graffiti(),
    val audio_message: AudioMessage? = AudioMessage()
)