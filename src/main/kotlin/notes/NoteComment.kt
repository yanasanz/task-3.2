package notes

import notes.Note

class NoteComment(
    val commentId: Int = 0,
    var message: String = "",
    val replyTo: Int? = 0,
    date: Long = System.currentTimeMillis(),
    noteId: Int,
    isDeleted: Boolean = false,
) : Note()