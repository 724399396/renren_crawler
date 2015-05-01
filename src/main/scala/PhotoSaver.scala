import java.io.{File, ByteArrayOutputStream, InputStream, FileOutputStream}
import java.net.{HttpURLConnection, URL}
import collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * Created by li-wei on 2015/4/17.
 */
object PhotoSaver extends App {
  for(age <- 0 to 6)
    for(photo <- DBManager.photosByAge(age)) {
      saveUrlImage(photo)
      DBManager.changePhotoIsFetch(photo)
    }

  def saveUrlImage(photo: Photo) {
    val imageDirectory = new File("D:/work/photos-true/photos/%s/%s".format(photo.age, photo.nickName.replaceAll("[^\\u4e00-\\u9fa5]+", "").trim))
    if (!imageDirectory.exists()) {
      imageDirectory.mkdirs();
    }
    val imageFile = new File("D:/work/photos-true/photos/%s/%s/%d.jpg".format(photo.age, photo.nickName.replaceAll("[^\\u4e00-\\u9fa5]+", "").trim, photo.id))
    if(!imageFile.exists()) {
      val url: URL = new URL(photo.getAvatarUrl)
      val conn: HttpURLConnection = url.openConnection().asInstanceOf[HttpURLConnection]
      conn.setRequestMethod("GET");
      val inStream =
        try {
          conn.getInputStream()
        } catch {
          case _: Exception => println(photo + " is failure")
        }
      if (inStream ==()) ()
      else {
        val data: Array[Byte] = readInputStream(inStream.asInstanceOf[InputStream])
        val outStream = new FileOutputStream(imageFile)
        outStream.write(data)
        outStream.close()
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
