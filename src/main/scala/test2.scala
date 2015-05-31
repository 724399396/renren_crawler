import org.jsoup.Jsoup

/**
 * Created by li-wei on 2015/5/30.
 */
object test2 extends App {
  import collection.JavaConversions.mapAsJavaMap
  val test = CookieAndPostData.allTokens.last
  val testCookie = test(0)
  val url = "http://photo.renren.com/photo/35078/album-230521667/v7"
  Jsoup.connect(url).header("Accept", "*/*")
    .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36")
    .cookies(testCookie).get()
  Jsoup.connect(url).header("Accept", "*/*")
    .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36")
    .cookies(testCookie).get()
  Jsoup.connect(url).header("Accept", "*/*")
    .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36")
    .cookies(testCookie).get()
  Jsoup.connect(url).header("Accept", "*/*")
    .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36")
    .cookies(testCookie).get()
  Jsoup.connect(url).header("Accept", "*/*")
    .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36")
    .cookies(testCookie).get()
  Jsoup.connect(url).header("Accept", "*/*")
    .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36")
    .cookies(testCookie).get()
  Jsoup.connect(url).header("Accept", "*/*")
    .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36")
    .cookies(testCookie).get()
  Jsoup.connect(url).header("Accept", "*/*")
    .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36")
    .cookies(testCookie).get()
  Jsoup.connect(url).header("Accept", "*/*")
    .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36")
    .cookies(testCookie).get()
  Jsoup.connect(url).header("Accept", "*/*")
    .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36")
    .cookies(testCookie).get()
  println("ok")
  while(true) {}
}
