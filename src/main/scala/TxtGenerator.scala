import java.io.{PrintWriter, File}
import javax.imageio.ImageIO

object TxtGenerator {
  def generateTxt(age: Int): Unit = {
    val out = new PrintWriter("D:/work/photos-true/total/%d.txt".format(age))
    var del = collection.mutable.ArrayBuffer[String]()
    face.Util.subFiles(new File("D:/work/photos-true/total/%d".format(age))).map(file => {
      val absolutePath = file.getAbsolutePath
      val image = ImageIO.read(new File(absolutePath))
      val size = image.getWidth + "x" + image.getHeight
      (file.getName, size)
    }).map(
        jpg => (jpg._1.substring(0, jpg._1.lastIndexOf(".")), jpg._1.substring(0, jpg._1.lastIndexOf("-")).toInt, jpg._2)
      ).toList.sortWith((a,b) => a._2 < b._2).map(tp => (tp._1, DBManager.getPhotoById(tp._2), tp._3)).foreach( tp => {
      //println(tp._1)
      tp._2 match {
        case null => del += tp._1
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
  }
  def main(args: Array[String]): Unit = {
    (37 to 40).foreach(generateTxt)
  }
}
