package face

import java.io._

import org.opencv.core._
import org.opencv.highgui.Highgui
import org.opencv.imgproc.Imgproc

/**
 * vm args: -Djava.library.path=F:/opencv/build/java/x64;F:/opencv/build/x64/vc12/bin
 */
object PhotoDropDu {
  def main(args: Array[String]): Unit = {
    (41 to 75).foreach(age => deleteRepeatPhoto("D:/work/photos-true/photos/%d".format(age)))
  }

  private def copyPhoto2Tmp(directory: String) {
    val photoFiles = Util.subFiles(new File(directory)).toList
    val allPhotos = photoFiles.map(_.getAbsolutePath).filter(_.endsWith(".jpg")).toArray
    val size = allPhotos.size
    for (i <- 0 until size) {
      val srcPhoto = allPhotos(i)
      val desPhoto = Util.convertReal2Tmp(new File(srcPhoto))
      printf("%d/%d\r\n", i + 1, size)
      Util.copyFile(srcPhoto, desPhoto)
    }
  }

  private def deleteRepeatPhoto(source: String) {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

    val repeatPhotos: collection.mutable.HashSet[String] = new collection.mutable.HashSet[String]()
    val allPhotoFiles = Util.subFiles(new File(source)).toList
    val allPhotos = allPhotoFiles.map(_.getAbsolutePath).filter(_.endsWith(".jpg"))
    val allHashs = allPhotos.map(photo => (photo -> Util.pHash(photo))).toMap
    allPhotos.foreach(one => {
      allPhotos.filter(other => jpgIndex2Number(other) > jpgIndex2Number(one)).foreach(
        other => {
          val hash1 = allHashs.get(one).get
          val hash2 = allHashs.get(other).get
          val diff = hash1.zip(hash2).map({ case (x, y) => if (x != y) 1 else 0 }).sum
          if (diff < 5)
            repeatPhotos.add(other)
        }
      )
    })

    if (repeatPhotos.size > 0) {
      val copy2DiStr = "D:/work/photos-true/photos-repeat/%s".format(new File(source).getName)
      val copy2Directory = new File(copy2DiStr)
      if (!copy2Directory.exists()) copy2Directory.mkdirs()
      println(copy2Directory.getAbsolutePath)

      println(repeatPhotos.size)
      repeatPhotos.foreach(photo =>
        new File(photo).renameTo(new File(copy2DiStr + "/%s".format(new File(photo).getName))))
    }
  }



  def jpgIndex2Number(photo: String): Int = {
    val full = new File(photo).getName
    full.substring(0, full.lastIndexOf(".jpg")).toInt
  }
}