package face

import java.io.{File, FileInputStream, FileOutputStream}

import org.opencv.core._
import org.opencv.highgui.Highgui
import org.opencv.objdetect.CascadeClassifier

/**
 * vm args: -Djava.library.path=F:/opencv/build/java/x64;F:/opencv/build/x64/vc12/bin -Xmx1000M
 */
object FaceDetect {
  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  //val faceDetector: CascadeClassifier = new CascadeClassifier("F:\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_default.xml")
  val faceDetector: CascadeClassifier = new CascadeClassifier("F:\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml")
  //val faceDetector: CascadeClassifier = new CascadeClassifier("F:\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt2.xml")
  //val faceDetector: CascadeClassifier = new CascadeClassifier("F:\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt_tree.xml")

  def main(args: Array[String]):Unit = {
    val t = System.currentTimeMillis()
    (21 to 40).flatMap(age => Util.subFiles(new File("D:/work/photos-true/photos/%d".format(age)))).map(_.getAbsolutePath)
      .filter(_.endsWith("jpg")).foreach(detectFaceAndWrite _)
    println("time: " + (System.currentTimeMillis - t) + "ms")
    //detectFaceAndWrite("D:\\work\\photos\\photos-face\\20\\陈冰\\4.jpg")
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
//          Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
//            new Scalar(0, 255, 0))
          val filename = "D:/work/photos-true/faces/%s/%s-%d.jpg".format(sourceFile.getParentFile.getName,
            sourceFile.getName.substring(0, sourceFile.getName.lastIndexOf(".")), i)
          i += 1
          val rio = new Rect(rect.x,rect.y ,rect.width, rect.height)
          val parent = new File(filename).getParentFile
          if(!parent.exists()) parent.mkdirs()
          Highgui.imwrite(filename,image.submat(rio))
        }
      } else {
        val removeFile = new File("D:/work/photos-true/non-faces/%s/%s".format(sourceFile.getParentFile.getName,
          sourceFile.getName))
        if (!removeFile.getParentFile.exists()) removeFile.getParentFile.mkdirs()
        Highgui.imwrite(removeFile.getAbsolutePath,image)
      }
      image.release()
      faceDetections.release()
    } catch {
      case _: AnyRef =>
        println("error: " + source )
    }
  }

  //        val faceDetections2: MatOfRect = new MatOfRect()
  //        faceDetector2.detectMultiScale(image, faceDetections2)
  //        if(faceDetections2.toArray.length > 0) {
  //          val rects = faceDetections.toArray
  //          for (i <- 0 until faceDetections.toArray.length)
  //            for (rect2 <- faceDetections2.toArray) {
  //              val rect = rects(i)
  //              val avgWidth = average(rect.width, rect2.width)
  //              val avgHeight = average(rect.height, rect2.height)
  //              if (Math.abs(rect2.x - rect.x) < (avgWidth * 0.1)  && Math.abs(rect2.y - rect.y) < (avgHeight * 0.1)) {
  ////                Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
  ////                  new Scalar(0, 255, 0))
  //                val avgX = average(rect.x, rect2.x)
  //                val avgY = average(rect.y, rect.y)
  //                val filename = "faces/%s/%s-%d.jpg".format(file.getParentFile.getParentFile.getName,
  //                  file.getName.substring(0,file.getName.lastIndexOf(".")), i)
  //                val parent = new File(filename).getParentFile
  //                if(!parent.exists()) parent.mkdirs()
  //                val rio = new Rect(avgX - (0.0*avgWidth).toInt,
  //                  avgY - (0.0*avgHeight).toInt , (1.0*avgWidth).toInt, (1.0*avgHeight).toInt)
  //                new File(file.getName).deleteOnExit()
  //                Highgui.imwrite(filename,image.submat(rio))
  //                //copyFile("out-temp.jpg", filename)
  //              }
  //            }
  //        }
  //      }
  //      val removeFile = new File("2-non-faces/%s/%s.jpg".format(file.getParentFile.getParentFile.getName,
  //        file.getName.substring(0,file.getName.lastIndexOf("."))))
  //      if(!removeFile.getParentFile.exists()) removeFile.getParentFile.mkdirs()
  //      new File(file.getName).renameTo(removeFile)
}