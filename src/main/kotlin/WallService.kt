import exceptions.PostNotFoundException
import posts.Post

class WallService {
    var posts = emptyArray<Post>()
    var uniqueIds = emptyArray<Int>()
    var attachments = emptyArray<Attachment>()
    var wallComments = emptyArray<WallComment>()

    fun createWallComment(wallComment: WallComment) {
        var hasSuchComment: Boolean = false
        for (post in posts) {
            if (post.id == wallComment.postId) {
                wallComments += wallComment
                hasSuchComment = true
            }
        }
        if (!hasSuchComment) {
            throw PostNotFoundException("Не удалось добавить комментарий - нет такого поста")
        }
    }

    fun addAttachment(attachment: Attachment): Attachment {
        attachments += attachment
        return attachments.last()
    }

    fun add(post: Post): Post {
        val newId = if (uniqueIds.isEmpty()) 1 else uniqueIds.last() + 1
        uniqueIds += newId
        posts += post.copy(id = newId)
        return posts.last()
    }

    fun update(post: Post): Boolean {
        if (!uniqueIds.contains(post.id)) {
            return false
        } else {
            for ((index, item) in posts.withIndex()) {
                if (item.id == post.id) {
                    val newCopy = post.copy(ownerId = item.ownerId, date = item.date)
                    posts[index] = newCopy
                }
            }
            return true
        }
    }
}