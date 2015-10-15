import java.io.{File, PrintWriter}

import com.gargoylesoftware.htmlunit.html._
import com.gargoylesoftware.htmlunit.{BrowserVersion, WebClient}

import scala.collection.mutable.ArrayBuffer
import scala.io.Source


/**
 * Created by li-wei on 2015/4/15.
 */
object Login extends App {
  val resourcePath = Login.getClass().getResource("/").getPath
  val source = Source.fromFile(new File(resourcePath + "id&pwd.properties"))
  val out = new PrintWriter(new File(resourcePath + "cookie&data.properties"))
  val cookies = source.getLines().map(line => {
    val np = line.split("----")
    write2File(loginAndGetCookie(np(0), np(1)), np(2))
  }).toArray
  out.close()

  private def write2File(cookie: Array[String], where: String): Unit = {
      val data = getData(cookie.map(x => {
        val y = x.split("=");
        (y(0) -> y(1))
      }).toMap)
      out.print(cookie.mkString(","))
      out.print(";")
      out.print(data.mkString(","))
      out.print(";")
      out.println(where)
  }


  def loginAndGetCookie(id: String,pwd: String):Array[String] =  {
    val webClient: WebClient = new WebClient(BrowserVersion.CHROME)
    webClient.getOptions().setCssEnabled(false)
    webClient.getOptions().setJavaScriptEnabled(false)
    val prePage: HtmlPage = webClient.getPage("http://www.renren.com/")
    val form = prePage.getForms().get(0)
    val userName: HtmlTextInput = form.getInputByName("email")
    val password: HtmlPasswordInput = form.getInputByName("password")
    val submit: HtmlSubmitInput = form.getInputByValue("登录")
    userName.setValueAttribute(id)
    password.setValueAttribute(pwd)
    submit.click()
    val cookieManager = webClient.getCookieManager()
    var isRealT = true
    cookieManager.getCookies().toString.substring(1).trim.split(";").flatMap(_.split(",")).filter(x => {
      !x.startsWith("domain") && !x.startsWith("path") && !x.startsWith("expires")
    }).filterNot(x => {(if(x.startsWith(" t")) {isRealT = !isRealT; isRealT} else false)})
  }

  def getData(cookie: Map[String,String]): Array[String] = {
    import org.jsoup.Jsoup
    import scala.collection.JavaConversions._
    var result: ArrayBuffer[String] = ArrayBuffer("ajax=1",
      "q=", "s=0", "act=search", "ss=false")
    val str = Jsoup.connect("http://browse.renren.com/s/all?q=&p=%5B%7B%22t%22%3A%22birt%22%2C%22year%22%3A%221992%22%7D%5D&s=0&ref=sg_findpeople_search#qt=/tindex=2/curpage=3")
      .header("Accept","*/*").userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36")
      .cookies(cookie).get.head().select("script").toString
    val pattern = """(requestToken : ')([-0-9]+)""".r
    for(pattern(_, i) <- pattern.findFirstIn(str))
      result += ("requestToken="+i)
    val pattern2 = """(_rtk : ')([0-9a-z]+)""".r
    for(pattern2(_, i) <- pattern2.findFirstIn(str))
      result += ("_rtk="+i)
    val pattern3 = """(ruid:")([0-9]+)""".r
    for(pattern3(_, i) <- pattern3.findFirstIn(str))
      result += ("u="+i)
    result.toArray
  }



}
