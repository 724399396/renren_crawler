import java.io.{File, ByteArrayOutputStream, InputStream, FileOutputStream}
import java.net.{HttpURLConnection, URL}

/**
 * Created by li-wei on 2015/4/17.
 */
object PhotoSaver extends App {
  var id = 0
  for(age <- 15 to 35)
    for(photo <- DBManager.photosByAge(age)) {
      println(photo)
      saveUrlImage(photo)
      id += 1
    }

  def saveUrlImage(photo: Photo) {
    val imageDirectory = new File("photos/%s/%s".format(photo.age, photo.nickName.replaceAll("[^\\u4e00-\\u9fa5]+", "").trim))
    if (!imageDirectory.exists()) {
      imageDirectory.mkdirs(); id = 1
    }
    val imageFile = new File("photos/%s/%s/%d.jpg".format(photo.age, photo.nickName.replaceAll("[^\\u4e00-\\u9fa5]+", "").trim, id))
    if(!imageFile.exists()) {
      val url: URL = new URL(photo.getAvatarUrl)
      val conn: HttpURLConnection = url.openConnection().asInstanceOf[HttpURLConnection]
      conn.setRequestMethod("GET");
      conn.setConnectTimeout(5 * 1000);
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
