import java.io.{PrintWriter, File}

import org.opencv.core.Core
import org.opencv.highgui.Highgui

/**
 * vm args: -Djava.library.path=F:/opencv/build/java/x64;F:/opencv/build/x64/vc12/bin -Xms100M -Xmx300M -XX:MaxPermSize=50M -XX:MaxDirectMemorySize=50M
 */
object TxtGenerator {
  def generateTxt(age: Int): Unit = {
    val out = new PrintWriter("D:/work/photos-true/first/faces/%d.txt".format(age))
    face.Util.subFiles(new File("D:/work/photos-true/first/faces/%d".format(age))).map(file => {
      val absolutePath = file.getAbsolutePath
      val image = Highgui.imread(absolutePath)
      val size = image.size().toString
      image.release
      System.gc
      (file.getName, size)
    }).map(
        jpg => (jpg._1.substring(0, jpg._1.lastIndexOf(".")), jpg._1.substring(0, jpg._1.lastIndexOf("-")).toInt, jpg._2)
      ).toList.sortWith((a,b) => a._2 < b._2).map(tp => (tp._1, DBManager.getPhotoById(tp._2), tp._3)).foreach( tp => {
      out.print(tp._1 + " ")
      out.print(tp._2.age + " ")
      out.print(tp._3 + " ")
      out.println(if (tp._2.whereFrom == "") "无" else tp._2.whereFrom)
    })
    out.close
    println(age + " finish")
  }
  def main(args: Array[String]): Unit = {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    (14 to 40).foreach(generateTxt)
  }
}
