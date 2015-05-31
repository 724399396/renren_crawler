package face


import org.opencv.core._
import org.opencv.highgui.Highgui
import org.opencv.imgproc.Imgproc

/**
 * vm args: -Djava.library.path=F:/opencv/build/java/x64;F:/opencv/build/x64/vc12/bin
 */
object Test extends App {
  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  val img1 = Highgui.imread("1.jpg")
  val img2 = Highgui.imread("D:\\work\\photos-true\\other\\Images_ori\\009055_0M54.JPG")
  println(img1.dump().takeRight(1000))
  println(img2.dump().takeRight(1000))
  println(img1.dump().length)
  println(img2.dump().length)
  println(img1.copyTo(img2))
  println(img2.dump.length)
  println(img1.dump.length)
//  val hash = pHash("D:\\work\\photos-true\\faces\\20\\100047-0.jpg")
//  val hash2 = pHash("D:\\work\\photos-true\\faces\\20\\100047-1.jpg")
//  println(hash.zip(hash2).map({ case (x, y) => if (x != y) 1 else 0 }).sum)
  private def pHash(photo: String): IndexedSeq[Int] = {
    val size = 32
    val mainPhoto = photo //117200 109436 939
    val source: Mat = Highgui.imread(mainPhoto)
    val dSize = new Size(size, size)
    val image = new Mat()
    Imgproc.cvtColor(source, image, Imgproc.COLOR_RGB2GRAY)
    Imgproc.resize(image, image, dSize)
    image.assignTo(image, CvType.CV_64FC1)
    Core.dct(image, image)
    val pixels = (0 until 8).flatMap(row => (0 until 8).map(col => (row, col)))
      .map { case (row, col) => image.get(row, col) }.flatten
    val mean = pixels.sum / pixels.size
    val hash = pixels.map(value => if (value >= mean) 1 else 0)
    image.release()
    source.release()
    hash.foreach(print _)
    println()
    hash
  }

  private def perceptualHash(photo: String): IndexedSeq[Int] = {
    try {
      val one = Highgui.imread(photo)
      val dSize = new Size(8, 8)
      val image = new Mat(dSize, CvType.CV_16S)
      Imgproc.resize(one, image, dSize)
      Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY)
      val points = (0 until 8).flatMap(row => (0 until 8).map(col => (row, col)))
      val pixels = points.map { case (row, col) => image.get(row, col) }.flatten.map(_ / 6)
      val mean = pixels.sum / pixels.size
      val hash = pixels.map(value => if (value >= mean) 1 else 0)
      one.release()
      image.release()
      hash.foreach(print _)
      println()
      hash
      //hash.zip(hash2).map({ case (x, y) => if (x != y) 1 else 0 }).sum
    } catch {
      case _: Throwable => (0 until 64).map(_ => 0)
    }
  }
}
//
//  val mainPhoto2 = "D:/work/photos-true/tmp/20/26464.jpg" //117200 109436 939
//  val image2: Mat = Highgui.imread(mainPhoto2)
//  Imgproc.resize(image2,image2,dSize)
//  Imgproc.cvtColor(image2,image2,Imgproc.COLOR_RGB2GRAY)
//  val pixels2 = (0 until 8).flatMap(row => (0 until 8).map(col => (row, col)))
//    .map{case (row,col) => image2.get(row,col) }.flatten
//  val mean2 = pixels2.sum / pixels2.size
//  val hash2 = pixels2.map(value => if (value >= mean2) 1 else 0)
//
//  println(hash.zip(hash2).map({case (x,y) => if(x != y) 1 else 0}).sum)
//  val channel = new MatOfInt(2)+
//  val mMaskMat = new Mat()
//  val mHistMat = new Mat()
//  val histSize = new MatOfInt(256)
//  val range = new MatOfFloat(0f, 256f)

//  Imgproc.calcHist(java.util.Arrays.asList(image), channel, mMaskMat, mHistMat, histSize, range)
//  Core.normalize(mHistMat, mHistMat, image.height/2, 0, Core.NORM_INF)
//  Util.subFiles(new File("D:/work/photos-true/tmp/20/")).map(_.getAbsolutePath).map( photo => {
//    //val photo = "D:/work/photos-true/tmp/20/27163.jpg"
//    val image2: Mat = Highgui.imread(photo)
//    val mHistMat2 = new Mat()
//    Imgproc.calcHist(java.util.Arrays.asList(image2), channel, mMaskMat, mHistMat2, histSize, range)
//    Core.normalize(mHistMat2, mHistMat2, image2.height / 2, 0, Core.NORM_INF)
//    val distance = Imgproc.compareHist(mHistMat, mHistMat2, Imgproc.CV_COMP_BHATTACHARYYA)
//    println(distance + photo)
//    image2.release()
//    mHistMat2.release()
//    distance
//  }).toList.sorted.foreach(println _)

