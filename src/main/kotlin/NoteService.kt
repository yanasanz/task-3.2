import exceptions.CommentAccessDeniedException
import exceptions.NoteAccessDeniedException
import exceptions.NoteNotFoundException
import notes.ItemAndList
import notes.Note
import notes.NoteComment
import notes.User
import java.text.DateFormat

class NoteService {
    var users = listOf<ItemAndList<User, List<ItemAndList<Note, List<NoteComment>>>>>()
    var noteIds = mutableListOf<Int>()
    var commentIds = mutableListOf<Int>()

    fun add(title: String, text: String, privacy: Int, commentPrivacy: Int, userId: Int): Int {
        val note =
            Note(title, text, privacy, commentPrivacy, noteId = noteIds.size + 1, userId = userId)
        noteIds.add(note.noteId)
        val noteWithComments: ItemAndList<Note, List<NoteComment>> = ItemAndList(note, listOf())
        val currentUser = User(userId)
        val userWithNotes: ItemAndList<User, List<ItemAndList<Note, List<NoteComment>>>> =
            ItemAndList(currentUser, listOf())
        if (users.isEmpty()) {
            users = users + userWithNotes
        }
        for (user in users){
            when {
                user.main.userId != userId -> {
                    users = users + userWithNotes
                    user.list += noteWithComments
                    break
                }
                user.main.userId.equals(userId) -> user.list += noteWithComments
            }
        }
        return note.noteId
    }


    fun createComment(noteId: Int, replyTo: Int?, message: String): Int {
        var commentId: Int = 0
        for (user in users) {
            for (note in user.list) {
                if (note.main.noteId == noteId) {
                    if (note.main.privacy == 0 && note.main.commentPrivacy == 0) {
                        val comment = NoteComment(noteId = note.main.noteId,
                            replyTo = replyTo,
                            message = message,
                            commentId = if (commentIds.isEmpty()) 1 else commentIds.last() + 1)
                        note.list += comment
                        commentIds.add(comment.commentId)
                        commentId = comment.commentId
                    } else {
                        println("This note has invalid privacy parameters")
                    }
                } else {
                    println("There is no such note")
                }
            }
        }
        return commentId
    }


    fun delete(noteId: Int): Int {
        if (!(noteId in 1..noteIds.size)) throw NoteNotFoundException("180 notes.Note not found")
        for (user in users) {
            for (note in user.list) {
                when {
                    note.main.noteId == noteId && !note.main.isDeleted -> note.main.isDeleted = true
                    note.main.noteId == noteId && note.main.isDeleted -> throw NoteNotFoundException("180 notes.Note not found")
                }
            }
        }
        return 1
    }

    fun deleteComment(commentId: Int): Int {
        if (!(commentId in 1..commentIds.size)) println("There is no such comment")
        for (user in users) {
            for (note in user.list) {
                for (comment in note.list) {
                    if (comment.commentId == commentId && !comment.isDeleted) {
                        when {
                            note.main.privacy in 1..3 -> throw NoteAccessDeniedException("181 Access to note denied")
                            note.main.commentPrivacy in 1..3 -> throw CommentAccessDeniedException("183 Access to comment denied")
                            note.main.privacy == 0 && note.main.commentPrivacy == 0 -> comment.isDeleted =
                                true
                        }
                    }

                }
            }
        }
        return 1
    }

    fun edit(
        noteId: Int, title: String, text: String, privacy: Int, commentPrivacy: Int,
    ): Int {
        if (!(noteId in 1..noteIds.size)) throw NoteNotFoundException("180 notes.Note not found")
        for (user in users) {
            for (note in user.list) {
                if (note.main.noteId == noteId && !note.main.isDeleted) {
                    note.main.title = title
                    note.main.text = text
                    note.main.privacy = privacy
                    note.main.commentPrivacy = commentPrivacy
                }
            }
        }
        return 1
    }

    fun editComment(commentId: Int, message: String): Int {
        if (!(commentId in 1..commentIds.size)) println("There is no such comment")
        for (user in users) {
            for (note in user.list) {
                for (comment in note.list) {
                    if (comment.commentId == commentId && !comment.isDeleted) {
                        when {
                            note.main.commentPrivacy in 1..3 -> throw CommentAccessDeniedException("183 Access to comment denied")
                            note.main.commentPrivacy == 0 -> comment.message = message
                        }
                    }

                }
            }
        }
        return 1
    }

    fun get(userId: Int, offset: Int, count: Int, sort: Int): List<Note> {
        var notes: List<Note> = listOf()
        for (user in users) {
            if (user.main.userId == userId) {
                for (note in user.list) {
                    notes = notes + note.main
                }
            } else {
                println("notes.User not found")
            }
        }
        notes = notes.subList(offset, offset + count)
        when (sort) {
            0 -> notes.sortedBy { it.date }
            1 -> notes.sortedByDescending { it.date }
            else -> println("Please enter sort type as 0 or 1")
        }
        return notes
    }

    fun getById(noteId: Int): Unit {
        if (!(noteId in 1..noteIds.size)) throw NoteNotFoundException("180 notes.Note not found")
        for (user in users) {
            for (note in user.list) {
                when {
                    note.main.noteId == noteId && !note.main.isDeleted -> {
                        println("Заметка №${note.main.noteId} \n" +
                                "Название: ${note.main.title} \n" +
                                "Текст: ${note.main.text} \n" +
                                "Уровень доступа к заметке: ${note.main.privacy} - ${note.main.privacyView} \n" +
                                "Уровень доступа к комментированию заметки: ${note.main.commentPrivacy} - ${note.main.privacyComment} \n" +
                                "Пользователь: ${note.main.userId} \n" +
                                "Дата создания: ${DateFormat.getInstance().format(note.main.date)}")
                    }
                    note.main.noteId == noteId && note.main.isDeleted -> throw NoteNotFoundException("180 notes.Note not found")
                }
            }
        }
    }

    fun getComments(noteId: Int, offset: Int, count: Int, sort: Int): List<NoteComment> {
        var noteComments: List<NoteComment> = listOf()
        for (user in users) {
            for (note in user.list) {
                if (note.main.noteId == noteId) {
                    for (comment in note.list) {
                        noteComments = noteComments + comment
                    }
                } else {
                    println("notes.Note not found")
                }
            }
        }
        noteComments = noteComments.slice(offset..offset + count)
        when (sort) {
            0 -> noteComments.sortedBy { it.date }
            1 -> noteComments.sortedByDescending { it.date }
            else -> println("Please enter sort type as 0 or 1")
        }
        return noteComments
    }

    fun restoreComment(commentId: Int): Int {
        if (!(commentId in 1..commentIds.size)) println("There is no such comment")
        for (user in users) {
            for (note in user.list) {
                for (comment in note.list) {
                    if (comment.commentId == commentId && comment.isDeleted) {
                        when {
                            note.main.commentPrivacy in 1..3 -> throw CommentAccessDeniedException("183 Access to comment denied")
                            note.main.privacy == 0 && note.main.commentPrivacy == 0 -> comment.isDeleted =
                                false
                        }
                    }

                }
            }
        }
        return 1
    }
}