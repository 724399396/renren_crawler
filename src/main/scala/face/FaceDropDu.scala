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
    (11 to 40).foreach(age => deleteRepeatPhoto("D:/work/photos-true/faces/%d".format(age)))
  }

  private def deleteRepeatPhoto(source: String) {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

    val repeatPhotos: collection.mutable.HashSet[String] = new collection.mutable.HashSet[String]()
    val allPhotoFiles = Util.subFiles(new File(source)).toList
    val allPhotos = allPhotoFiles.map(_.getAbsolutePath).filter(_.endsWith(".jpg"))
    val allHashs = allPhotos.map(photo => (photo -> Util.pHash(photo))).toMap
//    val out = new ObjectOutputStream(new FileOutputStream("face-hash.back"))
//    out.writeObject(allHashs)
//    val in = new ObjectInputStream(new FileInputStream("face-hash.back"))
//    val allHashs = in.readObject().asInstanceOf[Map[String,IndexedSeq[Int]]]
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

    val copy2DiStr = "D:/work/photos-true/faces-repeat/%s".format(new File(source).getName)
    val copy2Directory = new File(copy2DiStr)
    if(!copy2Directory.exists()) copy2Directory.mkdirs()
    println(copy2Directory.getAbsolutePath)

    println(repeatPhotos.size)
    repeatPhotos.foreach(photo =>
      new File(photo).renameTo(new File(copy2DiStr + "/%s".format(new File(photo).getName))))
  }

  private def jpgIndex2Number(photo: String): Int = {
    val full = new File(photo).getName
    full.substring(0, full.lastIndexOf("-")).toInt
  }
}
