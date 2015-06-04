import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import scala.collection.JavaConversions.mapAsJavaMap
import scala.collection.mutable.Map

/**
 * Created by li-wei on 2015/4/17.
 */
object HtmlGetter {
  def getHtmlByPost(url: String, cookie: Map[String, String], data: Map[String, String]): Document = {
    var doc:Document = null
    var done: Boolean = false
    while(!done) {
      try {
        doc = Jsoup.connect(url).header("Accept", "*/*")
          .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36")
          .data(data).cookies(cookie).timeout(10000).post()
        if(doc.toString.contains("验证码"))  throw new RuntimeException("验证码")
        else done = true
      }
      catch {
        case re: RuntimeException =>
          Console.err.println("验证码")
          System.exit(-1)
        case ex: Exception => println("post " + url + " 异常")
      }
    }
    doc
  }
  def getHtmlByGet(url: String, cookie: Map[String, String]): Document = {
    var doc:Document = null
    var done: Boolean = false
    while(!done) {
      try {
        doc = Jsoup.connect(url).header("Accept", "*/*")
          .userAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36")
          .cookies(cookie).timeout(10000).get()
        if(doc.toString.contains("验证码")) throw new RuntimeException("验证码")
        else if (doc.toString.size < 60)  throw new RuntimeException("空网页")
        else done = true
      }
      catch {
        case re: RuntimeException =>
          Console.err.println("验证码")
          System.exit(-1)
        case ex: Exception => println("get " + url + " 异常")
      }
    }
    doc
  }
}
