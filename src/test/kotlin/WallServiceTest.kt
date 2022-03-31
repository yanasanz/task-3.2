import audio.Audio
import exceptions.PostNotFoundException
import file.File
import link.Link
import org.junit.Test
import org.junit.Assert.*
import photo.Photo
import posts.Post
import video.Video

class WallServiceTest {

    @Test
    fun is_attachment_added_with_right_type() {
        val service = WallService()
        val attachment1: Attachment = AudioAttachment(audio = Audio(12, 5, "колыбельная"))
        val attachment2: Attachment = PhotoAttachment(photo = Photo(99, 48, 16))
        val attachment3: Attachment = VideoAttachment(video = Video(53, 14, "о котиках"))
        val attachment4: Attachment = FileAttachment(file = File(55, 98, "расписание"))
        val attachment5: Attachment = LinkAttachment(link = Link("www", "сайт", "подпись"))
        service.addAttachment(attachment1)
        service.addAttachment(attachment2)
        service.addAttachment(attachment3)
        service.addAttachment(attachment4)
        service.addAttachment(attachment5)
        val result1 = service.attachments[0].type
        val result2 = service.attachments[1].type
        val result3 = service.attachments[2].type
        val result4 = service.attachments[3].type
        val result5 = service.attachments[4].type
        assertEquals("audio", result1)
        assertEquals("photo", result2)
        assertEquals("video", result3)
        assertEquals("file", result4)
        assertEquals("link", result5)
    }

    @Test
    fun id_notNull_after_adding() {
        val aboutDogs = Post(40, 12, 34, 18, text = "Статья про собак")
        val service = WallService()
        service.add(aboutDogs)
        val result = aboutDogs.id
        assertNotEquals(0, result)
    }

    @Test
    fun update_if_id_exists() {
        val aboutCats = Post(1, 12, 34, 18, text = "Статья про кошек")
        val notesFromIndia = Post(8, 48, 458, 7987, text = "Записки из Индии")
        val news = Post(1, 76, 24, 45, text = "Новости")
        val service = WallService()
        service.add(aboutCats)
        service.add(notesFromIndia)
        service.add(news)
        val update = Post(2, 733, 52, 4785, text = "Обновленные записки")
        val result = service.update(update)
        assertTrue(result)
    }

    @Test
    fun update_if_id_does_not_exist() {
        val aboutCats = Post(1, 12, 34, 18, text = "Статья про кошек")
        val notesFromIndia = Post(8, 48, 458, 7987, text = "Записки из Индии")
        val news = Post(1, 76, 24, 45, text = "Новости")
        val service = WallService()
        service.add(aboutCats)
        service.add(notesFromIndia)
        service.add(news)
        val update = Post(9, 733, 52, 4785, text = "Обновленные записки")
        val result = service.update(update)
        assertFalse(result)
    }

    @Test(expected = PostNotFoundException::class)
    fun shouldThrow() {
            val service = WallService()
            val post = Post(1, 5, 15, 9, text = "Новый пост")
            service.add(post)
            val wallComment = WallComment(25, 12, 3, text = "Комментарий к посту")
            service.createWallComment(wallComment)
    }

    @Test
    fun shouldNotThrow() {
        val service = WallService()
        val post = Post(1, 5, 15, 9, text = "Новый пост")
        service.add(post)
        val post2 = Post(2, 8, 7, 12, text = "Еще один пост")
        service.add(post2)
        val wallComment = WallComment(25, 12, 2, text = "Комментарий к посту")
        service.createWallComment(wallComment)
        assertTrue(service.wallComments.contains(wallComment))
    }
}