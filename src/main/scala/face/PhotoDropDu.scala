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
    (6 to 40).foreach(age => deleteRepeatPhoto("D:/work/photos-true/photos/%d".format(age)))
    //deleteRepeatPhoto("D:/work/photos-true/photos/20")
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
//    val out = new ObjectOutputStream(new FileOutputStream("hash.back"))
//    out.writeObject(allHashs)
//    val in = new ObjectInputStream(new FileInputStream("hash.back"))
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


//try {
//val source = new File(mainPhoto)
//val parent = source.getParentFile
//
//val channel = new MatOfInt(2)
//val mMaskMat = new Mat()
//val mHistMat = new Mat()
//val histSize = new MatOfInt(256)
//val range = new MatOfFloat(0f, 256f)
//
//val image: Mat = Highgui.imread(Util.convertReal2Tmp(source))
//Imgproc.calcHist(java.util.Arrays.asList(image), channel, mMaskMat, mHistMat, histSize, range)
//Core.normalize(mHistMat, mHistMat, image.height / 2, 0, Core.NORM_INF)
//
//for (photo <- Util.subFiles(parent).filter(_.getName.compareTo(source.getName) > 0)
//.map(_.getAbsolutePath)) {
//try {
//val image2: Mat = Highgui.imread(Util.convertReal2Tmp(new File(photo)))
//val mHistMat2 = new Mat()
//val mMaskMat2 = new Mat()
//
//Imgproc.calcHist(java.util.Arrays.asList(image2), channel, mMaskMat2, mHistMat2, histSize, range)
//Core.normalize(mHistMat2, mHistMat2, image2.height / 2, 0, Core.NORM_INF)
//val distance = Imgproc.compareHist(mHistMat, mHistMat2, Imgproc.CV_COMP_BHATTACHARYYA)
////13,062
//if (distance == 0.0) {
//println(mainPhoto + "->" + photo + " : " + distance)
//// 按顺序，删除的是外层文件，就能避免多次删除和比较问题
//// 最好在这里加入 java 里的break
//println(mainPhoto)
//repeatPhotos.add(mainPhoto)
//}
//image2.release()
//mHistMat2.release()
//} catch {
//case _ =>  println("error occur:" + photo); new File(photo).delete()
//}
//}
//count += 1
//println("%d/%d".format(count, allPhotos.size))
//image.release()
//mMaskMat.release()
//mHistMat.release()
//} catch {
//case _ => println("error occur:" + mainPhoto); new File(mainPhoto).delete()
//}
