import java.net.{URLDecoder, URLEncoder}

import org.jsoup.Jsoup

/**
 * Created by li-wei on 2015/4/15.
 */
object test2 extends App {
  val str = "完颜尚文|๑¯ω¯๑1.jpg"
  println(str.replaceAll("[^\\u4e00-\\u9fa5]+", ""))
}
