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
    while(doc == null) {
      try {
        doc = Jsoup.connect(url).header("Accept", "*/*")
          .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36")
          .data(data).cookies(cookie).post()
      }
      catch {
        case _: Exception => ()
      }
    }
    doc
  }
  def getHtmlByGet(url: String, cookie: Map[String, String]): Document = {
    var doc:Document = null
    while(doc == null) {
      try {
        doc = Jsoup.connect(url).header("Accept", "*/*")
          .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36")
          .cookies(cookie).get()
      }
      catch {
        case _: Exception => ()
      }
    }
    doc
  }
}
