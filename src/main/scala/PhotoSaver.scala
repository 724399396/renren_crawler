import java.io.{File, ByteArrayOutputStream, InputStream, FileOutputStream}
import java.net.{HttpURLConnection, URL}
import collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * Created by li-wei on 2015/4/17.
 */
object PhotoSaver {
  var map:mutable.Map[String,ArrayBuffer[Photo]] = mutable.Map()
  for(age <- 10 to 35)
    for(photo <- DBManager.photosByAge(age))
      map(photo.nickName) = map.getOrElse(photo.nickName, ArrayBuffer[Photo]()) += photo



  def saveUserImage(photos: ArrayBuffer[Photo]): Unit = {
    for(i <- 0 until photos.length) {
      saveUrlImage(photos(i), i + 1)
      DBManager.changePhotoIsFetch(photos(i))
    }
  }

  def saveUrlImage(photo: Photo, id: Int) {
    val imageDirectory = new File("photos/%s/%s".format(photo.age, photo.nickName.replaceAll("[^\\u4e00-\\u9fa5]+", "").trim))
    if (!imageDirectory.exists()) {
      imageDirectory.mkdirs();
    }
    val imageFile = new File("photos/%s/%s/%d.jpg".format(photo.age, photo.nickName.replaceAll("[^\\u4e00-\\u9fa5]+", "").trim, id))
    if(!imageFile.exists()) {
      println(id + " : " + photo)
      val url: URL = new URL(photo.getAvatarUrl)
      val conn: HttpURLConnection = url.openConnection().asInstanceOf[HttpURLConnection]
      conn.setRequestMethod("GET");
      val inStream =
        try {
          conn.getInputStream()
        } catch {
          case _: Exception => ()
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
