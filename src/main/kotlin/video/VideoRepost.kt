package video

data class VideoRepost(
    val count: Int = 0,
    val wall_count: Int = 0,
    val mail_count: Int = 0,
    val user_reposted: Boolean = false
)