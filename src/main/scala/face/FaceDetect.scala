package face

import java.io.{File, FileInputStream, FileOutputStream}

import org.opencv.core._
import org.opencv.highgui.Highgui
import org.opencv.objdetect.CascadeClassifier

/**
 * vm args: -Djava.library.path=F:/opencv/build/java/x64;F:/opencv/build/x64/vc12/bin -Xms100M -Xmx300M -XX:MaxPermSize=50M -XX:MaxDirectMemorySize=50M
 */
object FaceDetect {
  System.loadLibrary(Core.NATIVE_LIBRARY_NAME) /* load opencv library */
  /* choose one classifier */
  val faceDetector: CascadeClassifier = new CascadeClassifier("F:\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml")
  // the photo base dir
  val baseDir = "D:/work/photos-true"

  def main(args: Array[String]):Unit = {
    val t = System.currentTimeMillis()  // start time
    (61 to 75).flatMap(age =>  // process every age
      Util.subFiles(new File("%s/photos/%d".format(baseDir,age)))) // get every img file
      .map(_.getAbsolutePath) // get file absolute path
      .filter(_.endsWith("jpg")) // only process jpg file
      .foreach(detectFaceAndWrite _) // detect face and save
    println("time: " + (System.currentTimeMillis - t) + "ms")   // print spend time
  }

  def detectFaceAndWrite(source: String): Unit = {
    try {
      println(source) // print for watch
      val sourceFile = new File(source)
      val image: Mat = Highgui.imread(source) // read image
      val faceDetections: MatOfRect = new MatOfRect() // create rect images to save face that detect
      faceDetector.detectMultiScale(image, faceDetections) // detect face
      if(faceDetections.toArray.length > 0) {   // if detected
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
