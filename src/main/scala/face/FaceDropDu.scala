package face

import java.io._

import org.opencv.core._
import org.opencv.highgui.Highgui
import org.opencv.imgproc.Imgproc

/**
 * vm args: -Djava.library.path=F:/opencv/build/java/x64;F:/opencv/build/x64/vc12/bin
 */
object FaceDropDu {
  def main(args: Array[String]): Unit = {
    (args(0).toInt to args(1).toInt)   // age range
      .foreach(age => deleteRepeatPhoto("%s/faces/%d".format(Main.baseDir, age))) // check repeat
  }

  private def deleteRepeatPhoto(source: String) {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME) // load lib
    val repeatPhotos: collection.mutable.HashSet[String]
              = new collection.mutable.HashSet[String]() // record repeat file
    val allPhotoFiles = Util.subFiles(new File(source)).toList // get all sub file
    val allPhotos = allPhotoFiles.map(_.getAbsolutePath).filter(_.endsWith(".jpg")) // only process the file end with jpg
    val allHashes = allPhotos.map(photo => (photo -> Util.pHash(photo))).toMap // get all img hash value
    allPhotos.foreach(one => {
      allPhotos.filter(other => jpgIndex2Number(other) > jpgIndex2Number(one)).foreach( // avoid twice check
        other => {
          val hash1 = allHashes.get(one).get
          val hash2 = allHashes.get(other).get
          val diff = hash1.zip(hash2).map({ case (x, y) => if (x != y) 1 else 0 }).sum
          if (diff < 5)   // value that think same
            repeatPhotos.add(other)
        }
      )
    })

    if (repeatPhotos.size > 0) {
      val copy2DiStr = "%s/faces-repeat/%s".format(Main.baseDir, new File(source).getName)
      val copy2Directory = new File(copy2DiStr)
      if (!copy2Directory.exists()) copy2Directory.mkdirs()
      println(copy2Directory.getAbsolutePath)

      println(repeatPhotos.size)
      repeatPhotos.foreach(photo =>
        new File(photo).renameTo(new File(copy2DiStr + "/%s".format(new File(photo).getName))))
    }
  }

  private def jpgIndex2Number(photo: String): Int = {
    val full = new File(photo).getName
    full.substring(0, full.lastIndexOf("-")).toInt
  }
}
