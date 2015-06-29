import java.io.{FileInputStream, PrintWriter, File}
import javax.imageio.ImageIO

import com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi

object TxtGenerator {
  def generateTxt(age: Int): Unit = {
    val out = new PrintWriter("D:/work/photos-true/message-generator/txt-not-complete/%d.txt".format(age))
    var del = collection.mutable.ArrayBuffer[File]()
    val imgs = face.Util.subFiles(new File("D:/work/photos-true/first/faces/%d".format(age))).toList
    val readers = ImageIO.getImageReadersByFormatName("jpg")
    val reader =  readers.next()
    val total = imgs.size
    var count: Long = 0
    imgs.filter(_.getName.endsWith(".jpg")).map(file => {
      count += 1
      printf("%d : %d/%d %f%%%n", age, count, total, count.toDouble/total.toDouble*100)
      val absolutePath = file.getAbsolutePath
      val iis = ImageIO.createImageInputStream(new FileInputStream(absolutePath))
      reader.setInput(iis, true)
      val size = reader.getWidth(0) + "x" + reader.getHeight(0)
      (file.getName, size, file)
    }).map(
        jpg => (jpg._1.substring(0, jpg._1.lastIndexOf(".")), jpg._1.substring(0, jpg._1.lastIndexOf("-")).toInt, jpg._2, jpg._3)
      ).sortWith((a,b) => a._2 < b._2).map(tp => (tp._1, DBManager.getPhotoById(tp._2), tp._3, tp._4)).foreach( tp => {
      tp._2 match {
        case null => del += tp._4
        case _ =>
          out.print (tp._1 + " ")
          out.print (tp._2.age + " ")
          out.print (tp._3 + " ")
          out.println (if (tp._2.whereFrom == "") "无" else tp._2.whereFrom)
      }
    })
    out.close()
    println(age + " finish")
    println(del)
    //del.foreach(_.delete())
  }
  def main(args: Array[String]): Unit = {
    (36 to 75).foreach(generateTxt)
  }
}
