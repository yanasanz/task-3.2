package notes

import exceptions.CantCommentNoteException
import exceptions.NoteAccessDeniedException

open class Note(
    var title: String = "",
    var text: String = "",
    var privacy: Int = 0,
    var commentPrivacy: Int = 0,
    var privacyView: String = "",
    var privacyComment: String = "",
    val noteId: Int = 0,
    var isDeleted: Boolean = false,
    var userId: Int = 0,
    val date: Long = System.currentTimeMillis()
) {
    init {
        when (privacy) {
            0 -> privacyView = "все пользователи"
            1 -> privacyView = "только друзья"
            2 -> privacyView = "друзья и друзья друзей"
            3 -> privacyView = "только пользователь"
        }
        when (commentPrivacy) {
            0 -> privacyComment = "все пользователи"
            1 -> privacyComment = "только друзья"
            2 -> privacyComment = "друзья и друзья друзей"
            3 -> privacyComment = "только пользователь"
        }
        when {
            privacy in 1..3 -> throw NoteAccessDeniedException("181 Access to note denied")
            commentPrivacy in 1..3 -> throw CantCommentNoteException("182 You can't comment this note")
        }
    }
}