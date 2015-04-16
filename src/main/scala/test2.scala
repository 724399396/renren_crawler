import org.jsoup.Jsoup

/**
 * Created by li-wei on 2015/4/15.
 */
object test2 extends App {
  import scala.collection.JavaConversions._
  val str = Jsoup.connect("http://browse.renren.com/s/all?q=&p=%5B%7B%22t%22%3A%22birt%22%2C%22year%22%3A%221992%22%7D%5D&s=0&ref=sg_findpeople_search#qt=/tindex=2/curpage=3")
    .header("Accept","*/*").userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36")
    .cookies(CookieAndPostData.cookie).get.head().select("script").toString
  val pattern = """(.*)(requestToken : ')([-0-9]+)(.*)""".r
  val pattern2 = """(_rtk : ')([0-9a-z]+)""".r
  str match {
    case pattern(_,_,requestToken,_) => println(requestToken)
    case pattern2(r, _rtk) => println(_rtk)
    case _ =>
  }
//  for(pattern(_, i) <- pattern.findAllIn(str))
//    println(i)
//  val pattern2 = """(_rtk : ')([0-9a-z]+)""".r
//  for(pattern2(_, i) <- pattern2.findAllIn(str))
//    println(i)
}
