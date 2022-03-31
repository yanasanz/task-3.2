import audio.Audio
import file.File
import link.Link
import photo.Photo
import video.Video

sealed class Attachment(val type: String)
    data class VideoAttachment(val video: Video) : Attachment("video")
    data class AudioAttachment(val audio: Audio) : Attachment("audio")
    data class PhotoAttachment(val photo: Photo) : Attachment("photo")
    data class LinkAttachment(val link: Link) : Attachment("link")
    data class FileAttachment(val file: File) : Attachment("file")