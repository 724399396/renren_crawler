import java.io._
import java.net.{HttpURLConnection, URL}
import collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * Created by li-wei on 2015/4/17.
 */
object PhotoSaver extends App {
  for (age <- 39 to 40)
    for (photo <- DBManager.photosByAge(age).take(1)) {
      easySaveUrlImage(photo)
      DBManager.changePhotoIsFetch(photo)
    }

  def easySaveUrlImage(photo: Photo): Unit = {
    val imageDirectory = new File("D:/work/photos-true/photos/%s".format(photo.age))
    if (!imageDirectory.exists())
      imageDirectory.mkdirs()
    try {
      val url = new URL(photo.avatarUrl)
      val is = new BufferedInputStream(url.openStream())
      val os = new FileOutputStream("D:/work/photos-true/photos/%s/%d.jpg".format(photo.age, photo.id))
      val b = new Array[Byte](1024)
      var length = -1
      while ( {length = is.read(b); length != -1 }) {
        os.write(b, 0, length)
      }
      is.close()
      os.close()
    } catch {
      case _: Exception => println(photo + " is failure")
    }
  }

  def saveUrlImage(photo: Photo) {
//    val personName: String = photo.nickName.replaceAll("[^\\u4e00-\\u9fa5]+", "").trim match {
//      case "" => "全英文名或其他"
//      case name: String => name
//    }
    val imageDirectory = new File("D:/work/photos-true/photos/%s".format(photo.age))
    if (!imageDirectory.exists()) {
      imageDirectory.mkdirs()
    }
    val imageFile = new File("D:/work/photos-true/photos/%s/%d.jpg".format(photo.age, photo.id))
    if (!imageFile.exists()) {
      try {
        val url: URL = new URL(photo.getAvatarUrl)
        val conn: HttpURLConnection = url.openConnection().asInstanceOf[HttpURLConnection]
        conn.setRequestMethod("GET");
        val inStream = conn.getInputStream()
        if (inStream ==()) ()
        else {
          val data: Array[Byte] = readInputStream(inStream.asInstanceOf[InputStream])
          val outStream = new FileOutputStream(imageFile)
          outStream.write(data)
          outStream.close()
        }
      } catch {
        case _: Exception => println(photo + " is failure")
      }
    }
  }

  def readInputStream(inStream: InputStream): Array[Byte] = {
    val outStream = new ByteArrayOutputStream()
    val buffer = new Array[Byte](1024)
    var len = 0
    while ( {
      len = inStream.read(buffer);
      len != -1
    }) {
      outStream.write(buffer, 0, len);
    }
    inStream.close()
    outStream.toByteArray()
  }

}
