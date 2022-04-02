import exceptions.*
import org.junit.Assert.*
import org.junit.Test

class NoteServiceTest {

    @Test
    fun is_note_added_to_notes_list_with_the_right_id() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.add("вторая заметка", "note", 0, 0)
        service.add("третья заметка", "note", 0, 0)
        val result1 = service.notes[0].noteId
        val result2 = service.notes[1].noteId
        val result3 = service.notes[2].noteId
        assertEquals(1, result1)
        assertEquals(2, result2)
        assertEquals(3, result3)
    }

    @Test
    fun note_privacy_and_note_comment_privacy_out_of_range() {
        val service = NoteService()
        service.add("первая заметка", "note", 5, 0)
        service.add("вторая заметка", "note", 0, 8)
        service.add("третья заметка", "note", 11, 15)
        assertTrue(service.notes.isEmpty())
    }

    @Test
    fun comment_is_created() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.add("вторая заметка", "note", 0, 0)
        service.add("третья заметка", "note", 0, 0)
        service.createComment(1, null, "коммент к первой заметке")
        service.createComment(2, null, "коммент ко второй заметке")
        service.createComment(2, null, "еще коммент ко второй заметке")
        service.createComment(3, null, "коммент к третьей заметке")
        service.createComment(3, null, "еще коммент к третьей заметке")
        service.createComment(3, null, "и еще один коммент к третьей заметке")
        val result1 = service.notes[0].comments.size
        val result2 = service.notes[1].comments.size
        val result3 = service.notes[2].comments.size
        assertEquals(result1, 1)
        assertEquals(result2, 2)
        assertEquals(result3, 3)
    }

    @Test
    fun note_with_comments_is_deleted() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.add("вторая заметка", "note", 0, 0)
        service.add("третья заметка", "note", 0, 0)
        service.createComment(1, null, "коммент к первой заметке")
        service.createComment(2, null, "коммент ко второй заметке")
        service.createComment(2, null, "еще коммент ко второй заметке")
        service.createComment(3, null, "коммент к третьей заметке")
        service.createComment(3, null, "еще коммент к третьей заметке")
        service.createComment(3, null, "и еще один коммент к третьей заметке")
        service.delete(3)
        assertTrue(service.notes[2].isDeleted)
        assertTrue(service.notes[2].comments[0].isDeleted)
        assertTrue(service.notes[2].comments[1].isDeleted)
        assertTrue(service.notes[2].comments[2].isDeleted)
    }

    @Test
    fun comment_is_deleted() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.add("вторая заметка", "note", 0, 0)
        service.add("третья заметка", "note", 0, 0)
        service.createComment(1, null, "коммент к первой заметке")
        service.createComment(2, null, "коммент ко второй заметке")
        service.createComment(2, null, "еще коммент ко второй заметке")
        service.createComment(3, null, "коммент к третьей заметке")
        service.createComment(3, null, "еще коммент к третьей заметке")
        service.createComment(3, null, "и еще один коммент к третьей заметке")
        service.deleteComment(2)
        service.deleteComment(5)
        assertTrue(service.notes[1].comments[0].isDeleted)
        assertTrue(service.notes[2].comments[1].isDeleted)
    }

    @Test
    fun note_successfully_edited() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.add("вторая заметка", "note", 0, 0)
        service.add("третья заметка", "note", 0, 0)
        val noteTitle = service.notes[1].title
        val noteText = service.notes[1].text
        service.edit(2, "заметка номер 2", "о погоде", 0, 0)
        assertNotEquals(noteTitle, service.notes[1].title)
        assertNotEquals(noteText, service.notes[1].text)
    }

    @Test
    fun comment_successfully_edited() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.add("вторая заметка", "note", 0, 0)
        service.add("третья заметка", "note", 0, 0)
        service.createComment(1, null, "коммент к первой заметке")
        service.createComment(2, null, "коммент ко второй заметке")
        service.createComment(2, null, "еще коммент ко второй заметке")
        service.createComment(3, null, "коммент к третьей заметке")
        service.createComment(3, null, "еще коммент к третьей заметке")
        service.createComment(3, null, "и еще один коммент к третьей заметке")
        val commentFourMessage = service.notes[2].comments[0].message
        service.editComment(4, "новый коммент")
        assertNotEquals(commentFourMessage, service.notes[2].comments[0].message)
    }

    @Test
    fun get_selected_notes_list_in_proper_opder() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.add("вторая заметка", "note", 0, 0)
        service.add("третья заметка", "note", 0, 0)
        val notesInDirectOrder = service.get("1 2 3",1,2,1)
        val notesInReverseOrder = service.get("2 3",0,2,0)
        assertEquals(notesInDirectOrder[1].title, "третья заметка")
        assertEquals(notesInReverseOrder[1].title,"вторая заметка")
    }

    @Test
    fun wrong_sort_parameter_in_fun_get() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.add("вторая заметка", "note", 0, 0)
        service.add("третья заметка", "note", 0, 0)
        val notes = service.get("1 2 3",0,3,5)
        assertTrue(notes.isEmpty())
    }

    @Test
    fun get_selected_comments_list_in_proper_opder() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.add("вторая заметка", "note", 0, 0)
        service.add("третья заметка", "note", 0, 0)
        service.createComment(1, null, "коммент к первой заметке")
        service.createComment(2, null, "коммент ко второй заметке")
        service.createComment(2, null, "еще коммент ко второй заметке")
        service.createComment(3, null, "коммент к третьей заметке")
        service.createComment(3, null, "еще коммент к третьей заметке")
        service.createComment(3, null, "и еще один коммент к третьей заметке")
        val commentsInDirectOrder = service.getComments(2,0,2,1)
        val commentsInReverseOrder = service.getComments(3,1,2,0)
        assertEquals(commentsInDirectOrder[0].message, "коммент ко второй заметке")
        assertEquals(commentsInReverseOrder[1].message,"еще коммент к третьей заметке")
    }

    @Test
    fun wrong_sort_parameter_in_fun_get_comments() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.add("вторая заметка", "note", 0, 0)
        service.add("третья заметка", "note", 0, 0)
        service.createComment(1, null, "коммент к первой заметке")
        service.createComment(2, null, "коммент ко второй заметке")
        service.createComment(2, null, "еще коммент ко второй заметке")
        service.createComment(3, null, "коммент к третьей заметке")
        service.createComment(3, null, "еще коммент к третьей заметке")
        service.createComment(3, null, "и еще один коммент к третьей заметке")
        val comments = service.getComments(3,2,1,8)
        assertTrue(comments.isEmpty())
    }

    @Test
    fun comment_is_restored() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.add("вторая заметка", "note", 0, 0)
        service.add("третья заметка", "note", 0, 0)
        service.createComment(1, null, "коммент к первой заметке")
        service.createComment(2, null, "коммент ко второй заметке")
        service.createComment(2, null, "еще коммент ко второй заметке")
        service.createComment(3, null, "коммент к третьей заметке")
        service.createComment(3, null, "еще коммент к третьей заметке")
        service.createComment(3, null, "и еще один коммент к третьей заметке")
        service.deleteComment(5)
        assertTrue(service.notes[2].comments[1].isDeleted)
        service.restoreComment(5)
        assertFalse(service.notes[2].comments[1].isDeleted)
    }

    @Test(expected = NoteNotFoundException::class)
    fun should_throw_note_not_found_exception_in_fun_create_comment() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.createComment(2, null, "коммент к первой заметке")
    }

    @Test(expected = NoteNotFoundException::class)
    fun should_throw_note_not_found_exception_in_fun_delete() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.delete(2)
    }

    @Test(expected = NoteNotFoundException::class)
    fun should_throw_note_not_found_exception_in_fun_edit() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.edit(5,"новая заметка","новый текст", 0,0)
    }

    @Test(expected = NoteNotFoundException::class)
    fun should_throw_note_not_found_exception_in_fun_get() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.add("вторая заметка", "note", 0, 0)
        service.get("1 3", 0, 2,1)
    }

    @Test(expected = NoteNotFoundException::class)
    fun should_throw_note_not_found_exception_in_fun_get_by_id() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.getById(7)
    }

    @Test(expected = NoteNotFoundException::class)
    fun should_throw_note_not_found_exception_in_fun_get_comments() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.getComments(2, 1, 3,0)
    }

    @Test(expected = NoteAccessDeniedException::class)
    fun should_throw_note_access_denied_exception_in_fun_create_comment() {
        val service = NoteService()
        service.add("первая заметка", "note", 1, 0)
        service.createComment(1, null, "коммент к первой заметке")
    }

    @Test(expected = NoteAccessDeniedException::class)
    fun should_throw_note_access_denied_exception_in_fun_delete_comment() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.createComment(1, null, "коммент к первой заметке")
        service.edit(1,"первая заметка", "изменена приватность заметки", 1, 0)
        service.deleteComment(1)
    }

    @Test(expected = NoteAccessDeniedException::class)
    fun should_throw_note_access_denied_exception_in_fun_edit() {
        val service = NoteService()
        service.add("первая заметка", "note", 1, 0)
        service.edit(1,"заметка номер 1", "note", 0, 0)
    }

    @Test(expected = CantCommentNoteException::class)
    fun should_throw_cant_comment_note_exception_in_fun_create_comment() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 2)
        service.createComment(1, null, "коммент к первой заметке")
    }

    @Test(expected = CommentAccessDeniedException::class)
    fun should_throw_comment_access_denied_exception_in_fun_delete_comment() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.createComment(1, null, "коммент к первой заметке")
        service.edit(1,"первая заметка", "изменена приватность комментария", 0, 2)
        service.deleteComment(1)
    }

    @Test(expected = CommentAccessDeniedException::class)
    fun should_throw_comment_access_denied_exception_in_fun_edit_comment() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.createComment(1, null, "коммент к первой заметке")
        service.edit(1,"первая заметка", "изменена приватность комментария", 0, 2)
        service.editComment(1,"новый комментарий")
    }

    @Test(expected = CommentNotFoundException::class)
    fun should_throw_comment_not_found_exception_in_fun_delete_comment() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.createComment(1, null, "коммент к первой заметке")
        service.deleteComment(2)
    }

    @Test(expected = CommentNotFoundException::class)
    fun should_throw_comment_not_found_exception_in_fun_edit_comment() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.createComment(1, null, "коммент к первой заметке")
        service.editComment(5,"новый комментарий")
    }

    @Test(expected = CommentNotFoundException::class)
    fun should_throw_comment_not_found_exception_in_fun_restore_comment() {
        val service = NoteService()
        service.add("первая заметка", "note", 0, 0)
        service.createComment(1, null, "коммент к первой заметке")
        service.deleteComment(1)
        service.restoreComment(3)
    }
}