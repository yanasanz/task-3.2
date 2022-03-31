import file.File
import video.Video
import java.text.DateFormat

fun main() {
val service = NoteService()
    service.add("first","note",0,0,1)
    service.add("second","note",0,0,2)
    service.add("third","note",0,0,2)
    service.add("forth","note",0,0,3)
    service.add("fifth","note",0,0,4)
    val users = service.users.map { it.main }
    for (user in users){
            println(user.userId)
    }
}

fun printMsg(attachment: Attachment) =
    when (attachment) {
        is VideoAttachment -> println("Attachment type is ${attachment.type}")
        is AudioAttachment -> println("Attachment type is ${attachment.type}")
        is PhotoAttachment -> println("Attachment type is ${attachment.type}")
        is FileAttachment -> println("Attachment type is ${attachment.type}")
        is LinkAttachment -> println("Attachment type is ${attachment.type}")
    }