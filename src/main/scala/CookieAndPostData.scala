import java.io.File
import scala.io.Source

/**
 * Created by li-wei on 2015/4/15.
 */
object CookieAndPostData {
  val allTokens = readFromFile("cookie&data.properties")
  private def readFromFile(fileName: String): Array[Array[collection.mutable.Map[String,String]]] = {
    val source = Source.fromFile(new File(fileName))
    source.getLines().map(line => {
      line.split(";").map(stringToMap _)
    }).toArray
  }
  private def stringToMap(str: String): collection.mutable.Map[String,String] = {
    val map = collection.mutable.Map[String,String]()
    for(one <- str.split(",")) {
      val tmp = one.split("=")
      map(tmp(0).trim) = if (tmp.size == 1) "" else tmp(1).trim
    }
    map
  }
}
