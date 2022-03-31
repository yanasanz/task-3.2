data class WallComment(
    val id: Int = 0,
    val fromId: Int = 0,
    val postId: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val text: String = "",
    val replyToUser: Int = 0,
    val replyToComment: Int = 0,
    val attachments: Array<Attachment> = emptyArray<Attachment>(),
    val thread: Thread = Thread()
)