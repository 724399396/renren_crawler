package face

import java.io.{File, FileInputStream, FileOutputStream}

import org.opencv.core._
import org.opencv.highgui.Highgui
import org.opencv.objdetect.CascadeClassifier

/**
 * vm args: -Djava.library.path=F:/opencv/build/java/x64;F:/opencv/build/x64/vc12/bin -Xms100M -Xmx300M -XX:MaxPermSize=50M -XX:MaxDirectMemorySize=50M
 */
object FaceDetect {
  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  //val faceDetector: CascadeClassifier = new CascadeClassifier("F:\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_default.xml")
  val faceDetector: CascadeClassifier = new CascadeClassifier("F:\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml")
  //val faceDetector: CascadeClassifier = new CascadeClassifier("F:\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt2.xml")
  //val faceDetector: CascadeClassifier = new CascadeClassifier("F:\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt_tree.xml")

  def main(args: Array[String]):Unit = {
    val t = System.currentTimeMillis()
    (15 to 40).flatMap(age => Util.subFiles(new File("D:/work/photos-true/photos/%d".format(age)))).map(_.getAbsolutePath)
      .filter(_.endsWith("jpg"))foreach(detectFaceAndWrite _)
    println("time: " + (System.currentTimeMillis - t) + "ms")
//    Util.subFiles(new File("D:\\work\\photos-true\\other\\Images_ori")).toList.map(_.getAbsolutePath)
//    .filter(_.toLowerCase().endsWith("jpg")).foreach(detectFaceAndWrite _)
    //detectFaceAndWrite("D:\\work\\photos-true\\other\\Images_ori\\009055_0M54.JPG")
  }

  def detectFaceAndWrite(source: String): Unit = {
    try {
      println(source)
      val sourceFile = new File(source)
      val image: Mat = Highgui.imread(source)
      val faceDetections: MatOfRect = new MatOfRect()
      faceDetector.detectMultiScale(image, faceDetections)
      if(faceDetections.toArray.length > 0) {
        var i = 0
        for (rect <- faceDetections.toArray) {
          val filename = "D:/work/photos-true/faces/%s/%s-%d.jpg".format(sourceFile.getParentFile.getName,
            sourceFile.getName.substring(0, sourceFile.getName.lastIndexOf(".")), i)
          i += 1
          val rio = new Rect(rect.x,rect.y,rect.width, rect.height)
          val parent = new File(filename).getParentFile
          if(!parent.exists()) parent.mkdirs()
          Highgui.imwrite(filename,image.submat(rio))
        }
      } else {
        val removeFile = new File("D:/work/photos-true/non-faces/%s/%s".format(sourceFile.getParentFile.getName,
          sourceFile.getName))
        if (!removeFile.getParentFile.exists()) removeFile.getParentFile.mkdirs()
        Util.copyFile(source, removeFile.getAbsolutePath)
      }
      image.release()
      faceDetections.release()
      System.gc
    } catch {
      case _: AnyRef =>
        println("error: " + source )
    }
  }
}
