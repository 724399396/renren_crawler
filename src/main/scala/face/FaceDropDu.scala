package face

import java.io.File

import org.opencv.core.{Core, Mat, MatOfFloat, MatOfInt}
import org.opencv.highgui.Highgui
import org.opencv.imgproc.Imgproc

/**
 * Created by li-wei on 2015/4/30.
 */
object FaceDropDu extends App {
  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  val photoDirectory = "D:/work/photos-true/no-repeate-photos"
  val allPhotos = Util.subFiles(new File(photoDirectory)).map(_.getAbsolutePath).filter(_.endsWith(".jpg"))
  //allPhotos.foreach(photo => Util.copyFile(photo, "D:/work/photos-true/tmp/%s".format(new File(photo).getName)))
  for (mainPhoto <- allPhotos) {
    val source = new File(mainPhoto)
    val parent = source.getParentFile
    val image: Mat = Highgui.imread("D:/work/photos-true/tmp/%s".format(source.getName))
    val channel = new MatOfInt(0)
    val mMaskMat = new Mat()
    val mHistMat = new Mat()
    val histSize = new MatOfInt(256)
    val range = new MatOfFloat(0f, 256f)
    Imgproc.calcHist(java.util.Arrays.asList(image), channel, mMaskMat, mHistMat, histSize, range)
    Core.normalize(mHistMat, mHistMat, 0, mHistMat.rows, Core.NORM_MINMAX)
    for(photo <- Util.subFiles(parent).map(_.getAbsolutePath).filter(_ != mainPhoto)) {
        val source2 = new File(photo)
        val image2: Mat = Highgui.imread("D:/work/photos-true/tmp/%s".format(source2.getName))
        val mHistMat2 = new Mat()
        Imgproc.calcHist(java.util.Arrays.asList(image2), channel, mMaskMat, mHistMat2, histSize, range)
        Core.normalize(mHistMat2, mHistMat2, 0, mHistMat2.rows, Core.NORM_MINMAX)
        val distance = Imgproc.compareHist(mHistMat, mHistMat2, Imgproc.CV_COMP_CHISQR)
      println(mainPhoto +  "->"  + photo + " : " + distance)
        if (distance == 0)
          new File(photo).deleteOnExit()
    }
  }
}
