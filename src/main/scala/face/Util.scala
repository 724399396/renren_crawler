package face

import java.io.{FileOutputStream, File, FileInputStream}

import org.opencv.core.{Core, CvType, Size, Mat}
import org.opencv.highgui.Highgui
import org.opencv.imgproc.Imgproc

/**
 * Created by li-wei on 2015/5/1.
 */
object Util {
  def copyFile(source: String, desc: String): Unit = {
    val in = new FileInputStream(source)
    val outDirectory = new File(desc)
    outDirectory.getParent match {
      case null =>
      case _ =>
        if (! outDirectory.getParentFile.exists () ) {
          outDirectory.getParentFile.mkdirs ()
        }
    }
    val out = new FileOutputStream(desc)
    val buffer = new Array[Byte](1024)
    var len = 0
    while ( {
      len = in.read(buffer)
      len != -1
    }) {
      out.write(buffer, 0, len)
    }
    in.close()
    out.close()
  }

  def subFiles(dir: File) : Iterator[File] = {
    val all = dir.listFiles()
    val files = all.filter(_.isFile)
    val dirs = all.filter(_.isDirectory)
    files.toIterator ++ dirs.toIterator.flatMap(subFiles _)
  }

  def convertReal2Tmp(source: File): String = {
    "D:/work/photos-true/tmp/%s/%s"
      .format(source.getParentFile.getParentFile.getName,source.getName)
  }

  def pHash(photo: String): IndexedSeq[Int] = {
    try {
      val size = 32
      val source: Mat = Highgui.imread(photo)
      val dSize = new Size(size, size)
      val image = new Mat()
      Imgproc.cvtColor(source, image, Imgproc.COLOR_BGR2GRAY)
      Imgproc.resize(image, image, dSize)
      image.assignTo(image, CvType.CV_64FC1)
      Core.dct(image, image)
      val pixels = (0 until 8).flatMap(row => (0 until 8).map(col => (row, col)))
        .map { case (row, col) => image.get(row, col) }.flatten
      val mean = pixels.sum / pixels.size
      val hash = pixels.map(value => if (value >= mean) 1 else 0)
      image.release()
      source.release()
      hash
    } catch {
      case _: Throwable => println("失效图片: " + photo ); new File(photo).delete();  (0 until 64).map(_ => 2)
    }
  }

  def perceptualHash(photo: String): IndexedSeq[Int] = {
    try {
      val one = Highgui.imread(photo)
      val dSize = new Size(8, 8)
      val image = new Mat(dSize, CvType.CV_16S)
      Imgproc.resize(one, image, dSize)
      Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY)
      val points = (0 until 8).flatMap(row => (0 until 8).map(col => (row, col)))
      val pixels = points.map { case (row, col) => image.get(row, col) }.flatten.map(_ / 4)
      val mean = pixels.sum / pixels.size
      val hash = pixels.map(value => if (value >= mean) 1 else 0)
      one.release()
      image.release()
      hash
      //hash.zip(hash2).map({ case (x, y) => if (x != y) 1 else 0 }).sum
    } catch {
      case _: Throwable => println("失效图片: " + photo ); new File(photo).delete(); (0 until 64).map(_ => 2)
    }
  }
}
