import org.jsoup.nodes.Document

import scala.collection.mutable

/**
 * Created by li-wei on 2015/4/15.
 */
object main extends App {
    import org.jsoup.Jsoup

    import collection.JavaConversions._
//    val cookie = Map(//"anonymid" -> "i5sv19j0-appf4s",
//      //"depovince" -> "SXI",
//      //"_r01_" -> "1",
//      //"alxn" -> "bd629c867b3e48a85c34ed1fd539813db5d80914c017cacf",
//      //"mt" -> "WQ-jWd1qmB3HLyCpbU30n0",
//      //"cp_config" -> "2",
//      //"wp" -> "0",
//      //"WebOnLineNotice_816946441" -> "1",
//      "jebecookies" -> "53ec0313-eb71-4d1b-ba3c-ff98921b51e7|||||", // different
//      //"JSESSIONID" -> "abcxb05wn1lotbbHwY7Yu",  // different
//      //"ick_login" -> "4818baa5-8370-42ef-a9b3-e27f57347ed1",
//      //"_de" -> "FB2C42362CC007BA04CA0335B14B0CDA6DEBB8C2103DE356",
//      //"p" -> "695997588645e04dd4d851a4297eb6f21", // different
//      //"first_login_flag" -> "1",
//      //"ln_uact" -> "223164199@163.com",
//      //"ln_hurl" -> "http://hdn.xnimg.cn/photos/hdn521/20140512/2315/h_main_xxnU_27e90000c0dc1986.jpg",
//      "t" -> "93fb2204e2d47462ea367517344cdb431" // different
//      //"societyguester" -> "f193a074cfd44e43f0c626920f22fb131", // different
//      //"id" -> "816946441",
//      //"xnsid" -> "44368c6e", // different
//      //"ver" -> "7.0",
//      //"loginfrom" -> "null",
//      //"XNESSESSIONID" -> "f974277424e2",
//      //"wp_fold" -> "0")
//    )
//
//    val data = Map("ajax" -> "1",
//      "q" -> "", "p" -> "[{\"t\":\"birt\",\"year\":\"1992\"}]",
//      "s" -> "0", "u" -> "816946441", "act" -> "search", "offset" -> "0", "ss" -> "false", "requestToken" -> "-58711377"
//      ,"_rtk" -> "c8412109")

  val all = CookieAndPostData.readFromFile("cookie&data.properties")(0)
  val cookie = all(0)
  val data = all(1)
  println(getUserByBirth(1992, 0))

  def getUserByBirth(birth: Int, limit: Int): List[User] = {
    import java.net.URLEncoder._
    0.to(limit,10).flatMap{
      x => {
        val url = "http://browse.renren.com/sAjax.do?ref_search=searchResult_People_Tab&ajax=1&q=&p=%s&s=0&u=816946441&act=search&offset=%d&sort=0"
          .format(encode("[{\"t\":\"birt\",\"year\":\"%s\"}]".format(birth), "UTF-8"), x)
        getUserFromPage(url,data)
      }}
    .map(x => new User(x._1, birth, x._2)).toList
  }

  def getUserFromPage(url: String, data: Map[String,String]):mutable.Buffer[(String,String)] = {
    println(url)
   getHtmlByPost(url, data).
     select("ol#active_2012_module.fl.search_log").select("div.info").select("strong").map(
       x => { val pattern = "(.*)(id=)([0-9]+)(.*)".r
              (x.text -> ("http://www.renren.com/%s/profile".format(x.child(0).attr("href") match {case pattern(_,_,id,_) => id })))})
  }

  def getHtmlByPost(url: String, data: Map[String,String]): Document = {
    Jsoup.connect(url).header("Accept","*/*")
      .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36")
      .cookies(cookie).data(data).post
  }
}
