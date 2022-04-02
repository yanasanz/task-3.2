import exceptions.CommentAccessDeniedException
import exceptions.CommentNotFoundException
import exceptions.NoteAccessDeniedException
import exceptions.NoteNotFoundException
import notes.Note
import notes.NoteComment
import java.text.DateFormat

class NoteService {
    val notes: MutableList<Note> = mutableListOf()
    private val commentIds: MutableList<Int> = mutableListOf()

    fun add(title: String, text: String, privacy: Int, commentPrivacy: Int): Int {
        val note =
            Note(title, text, privacy, commentPrivacy, noteId = notes.size + 1)
        when {
            !(privacy in 0..4) -> {
                println("Privacy should be from 0 to 1")
                return -1
            }
            !(commentPrivacy in 0..4) -> {
                println("Comment privacy should be from 0 to 1")
                return -2
            }
            else -> notes.add(note)
        }
        return note.noteId
    }

    fun createComment(noteId: Int, replyTo: Int?, message: String): Int {
        if (!(noteId in 1..notes.size)) throw NoteNotFoundException("180 Note not found")
        var commentId = 0
        for (note in notes) {
            if (note.noteId == noteId) {
                val comment = NoteComment(noteId = note.noteId,
                    replyTo = replyTo,
                    message = message,
                    commentId = if (commentIds.isEmpty()) 1 else commentIds.last() + 1)
                note.comments.add(comment)
                commentIds.add(comment.commentId)
                commentId = comment.commentId
            }
        }
        return commentId
    }

    fun delete(noteId: Int): Int {
        if (!(noteId in 1..notes.size)) throw NoteNotFoundException("180 Note not found")
        for (note in notes) {
            when {
                note.noteId == noteId && !note.isDeleted -> note.isDeleted = true
                note.noteId == noteId && note.isDeleted -> throw NoteNotFoundException("180 Note not found")
            }
            for (comment in note.comments) {
                if (note.isDeleted) {
                    comment.isDeleted = true
                }
            }

        }
        return 1
    }

    fun deleteComment(commentId: Int): Int {
        if (!(commentId in 1..commentIds.size)) throw CommentNotFoundException("184 Comment not found")
        for (note in notes) {
            for (comment in note.comments) {
                if (comment.commentId == commentId && !comment.isDeleted) {
                    when {
                        note.privacy in 1..3 -> throw NoteAccessDeniedException("181 Access to note denied")
                        note.commentPrivacy in 1..3 -> throw CommentAccessDeniedException("183 Access to comment denied")
                        note.privacy == 0 && note.commentPrivacy == 0 -> comment.isDeleted = true
                    }
                }
            }
        }
        return 1
    }

    fun edit(noteId: Int, title: String, text: String, privacy: Int, commentPrivacy: Int): Int {
        if (!(noteId in 1..notes.size)) throw NoteNotFoundException("180 Note not found")
        for (note in notes) {
            when {
                privacy in 1..3 -> throw NoteAccessDeniedException("181 Access to note denied")
                note.noteId == noteId && !note.isDeleted -> {
                    note.title = title
                    note.text = text
                    note.privacy = privacy
                    note.commentPrivacy = commentPrivacy
                }
            }
        }
        return 1
    }

    fun editComment(commentId: Int, message: String): Int {
        if (!(commentId in 1..commentIds.size)) throw CommentNotFoundException("184 Comment not found")
        for (note in notes) {
            for (comment in note.comments) {
                if (comment.commentId == commentId && !comment.isDeleted) {
                    when {
                        note.commentPrivacy in 1..3 -> throw CommentAccessDeniedException("183 Access to comment denied")
                        note.commentPrivacy == 0 -> comment.message = message
                    }
                }
            }
        }
        return 1
    }

    fun get(noteIds: String, offset: Int, count: Int, sort: Int): List<Note> {
        val input = noteIds.split(" ").toTypedArray()
        val ids = input.map { it.toInt() }
        var notesList: List<Note> = listOf()
        for (id in ids) {
            if (id in 1..notes.size) {
                notesList = notesList + notes[id - 1]
            } else {
                throw NoteNotFoundException("180 Note with ID $id is not found")
            }
        }
        val selectedNotesList = notesList.subList(offset, offset + count)
        var sortedList: List<Note> = listOf()
        when (sort) {
            1 -> sortedList = selectedNotesList.sortedWith(compareBy { it.date })
            0 -> sortedList = selectedNotesList.sortedWith(compareBy { it.date }).reversed()
            else -> println("Please enter sort type as 0 or 1")
        }
        return sortedList
    }

    fun getById(noteId: Int): Note {
        if (!(noteId in 1..notes.size)) throw NoteNotFoundException("180 Note not found")
        val note: Note = notes[noteId - 1]
        if (!note.isDeleted) {
            println("Заметка №${note.noteId} \n" +
                    "Название: ${note.title} \n" +
                    "Текст: ${note.text} \n" +
                    "Уровень доступа к заметке: ${note.privacy} - ${note.privacyView} \n" +
                    "Уровень доступа к комментированию заметки: ${note.commentPrivacy} - ${note.privacyComment} \n" +
                    "Дата создания: ${DateFormat.getInstance().format(note.date)}")
        } else {
            throw NoteNotFoundException("180 Note not found")
        }
        return note
    }

    fun getComments(noteId: Int, offset: Int, count: Int, sort: Int): List<NoteComment> {
        if (!(noteId in 1..notes.size)) throw NoteNotFoundException("180 Note not found")
        var noteComments: List<NoteComment> = listOf()
        for (note in notes) {
            if (note.noteId == noteId) {
                noteComments = note.comments
            }
        }
        val selectedCommentsList = noteComments.subList(offset, offset + count)
        var sortedCommentsList: List<NoteComment> = listOf()
        when (sort) {
            1 -> sortedCommentsList = selectedCommentsList.sortedWith(compareBy { it.date })
            0 -> sortedCommentsList = selectedCommentsList.sortedWith(compareBy { it.date }).reversed()
            else -> println("Please enter sort type as 0 or 1")
        }
        return sortedCommentsList
    }

    fun restoreComment(commentId: Int): Int {
        if (!(commentId in 1..commentIds.size)) throw CommentNotFoundException("184 Comment not found")
        for (note in notes) {
            for (comment in note.comments) {
                if (comment.commentId == commentId && comment.isDeleted) {
                       comment.isDeleted = false
                }
            }
        }
        return 1
    }
}