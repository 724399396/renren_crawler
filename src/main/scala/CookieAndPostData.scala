import java.io.File
import scala.io.Source

/**
 * Created by li-wei on 2015/4/15.
 */
object CookieAndPostData extends App {
  readFromFile("cookie&data.properties").foreach(_.foreach(println _))
  def readFromFile(fileName: String): Array[Array[Map[String,String]]] = {
    val source = Source.fromFile(new File(fileName))
    source.getLines().map(line => {
      line.split(";").map(stringToMap _)
    }).toArray
  }
  private def stringToMap(str: String): Map[String,String] = {
    val map = collection.mutable.Map[String,String]()
    for(one <- str.split(",")) {
      val tmp = one.split("=")
      map(tmp(0).trim) = if (tmp.size == 1) "" else tmp(1).trim
    }
    map.toMap
  }
  val cookie = Map("anonymid" -> "i8jk02a3-3ufzej", "depovince" -> "SXI", "jebecookies" -> "88d8c297-b62c-459f-aa72-02cce95876d9|||||", "_r01_" -> "1", "JSESSIONID" -> "abcMbKv5WoTGB5rT8eaZu", "_de" -> "FB2C42362CC007BA04CA0335B14B0CDA6DEBB8C2103DE356",
    "p" -> "d578dc2072fab946e47d55a4660d76901", "first_login_flag" -> "1", "ln_uact" -> "223164199@163.com", "ln_hurl" -> "http://hdn.xnimg.cn/photos/hdn521/20140512/2315/h_main_xxnU_27e90000c0dc1986.jpg",
    "t" -> "8cdb22943114d4e21a6623e853efdb661", "societyguester" -> "8cdb22943114d4e21a6623e853efdb661", "id" -> "816946441", "xnsid" -> "3fec2f3a", "ver" -> "7.0", "loginfrom" -> "null")

  val data = Map("ajax" -> "1",
    "q" -> "", "p" -> "[{\"t\":\"birt\",\"year\":\"1992\"}]",
    "s" -> "0", "act" -> "search", "offset" -> "0", "ss" -> "false", "requestToken" -> "992259598",
  "_rtk" -> "6e5ac68", "u" -> "816946441")
  val takon1 = ()
}
