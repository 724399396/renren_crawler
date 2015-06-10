import java.io.{File, PrintWriter}
import scala.io.Source

/**
 * Created by li-wei on 2015/5/21.
 */
object TxtFillFromFile extends App {
  val age = 17
  val lines = Source.fromFile("D:/work/photos-true/message-generator/txt-not-complete/%d.txt".format(age)).getLines()
  //val map = lines.map(_.split(" ")).map(arr => (arr(0) -> (arr(1) + " " + arr(2) + " " + arr(3) + " "))).toMap
  val nonMessage = lines.map(_.span(_ != ' ')).toMap
  val files = face.Util.subFiles(new File("D:/work/photos-true/total/%d".format(age))).toList
  val out = new PrintWriter("D:/work/photos-true/message-generator/txt-complete/%d.txt".format(age))
  files.map(file => (file.getName.takeWhile(_ != '.'), file.getParentFile.getName.toList.mkString(" ")))
  .map{ case (id, message) => id + nonMessage(id) + " " + message }
  .foreach(out.println)
  out.close
  println(nonMessage.size)
  nonMessage.keySet.diff(files.map(_.getName.takeWhile(_ != '.')).toSet).foreach(println)
}
