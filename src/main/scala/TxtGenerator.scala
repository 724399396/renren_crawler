import java.io.{PrintWriter, File}
import javax.imageio.ImageIO

object TxtGenerator {
  def generateTxt(age: Int): Unit = {
    val out = new PrintWriter("D:/work/photos-true/message-generator/txt-not-complete/%d.txt".format(age))
    var del = collection.mutable.ArrayBuffer[File]()
    face.Util.subFiles(new File("D:/work/photos-true/total/%d".format(age))).toList
      .filter(_.getName.endsWith(".jpg")).map(file => {
      val absolutePath = file.getAbsolutePath
      val image = ImageIO.read(new File(absolutePath))
      val size = image.getWidth + "x" + image.getHeight
      (file.getName, size, file)
    }).map(
        jpg => (jpg._1.substring(0, jpg._1.lastIndexOf(".")), jpg._1.substring(0, jpg._1.lastIndexOf("-")).toInt, jpg._2, jpg._3)
      ).sortWith((a,b) => a._2 < b._2).map(tp => (tp._1, DBManager.getPhotoById(tp._2), tp._3, tp._4)).foreach( tp => {
      //println(tp._1)
      tp._2 match {
        case null => del += tp._4
        case _ =>
          out.print (tp._1 + " ")
          out.print (tp._2.age + " ")
          out.print (tp._3 + " ")
          out.println (if (tp._2.whereFrom == "") "无" else tp._2.whereFrom)
      }
    })
    out.close
    println(age + " finish")
    println(del)
    del.foreach(_.delete())
  }
  def main(args: Array[String]): Unit = {
    (15 to 75).foreach(generateTxt)
  }
}
