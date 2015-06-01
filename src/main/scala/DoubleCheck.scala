import java.util.Calendar
import java.net.URLEncoder._
import scala.collection.mutable
import scala.util.Random

object DoubleCheck extends App {

  val currentYear = Calendar.getInstance().get(Calendar.YEAR)
  val tokens = CookieAndPostData.allTokens
  val random = new Random()

  DBManager.allNotHaveEntryYearUsers().take(25).par.foreach(old => {
    DBManager.fixUserEntryYear(new User(old.id, getUserEntryYear(old)))
    println(old)
  })

  def getUserEntryYear(user: User, limitYear: Int = currentYear): Int = {
    val test = tokens(random.nextInt(tokens.length))
    val testCookie = test(0)
    val testData = test(1)

    val idRegex = "(.*id=)(\\d+)(.*)".r

    def caculOffset(nodes: xml.Node): Int = {
      val resultNumRegex1 = """.*id="resultNum" value="(\d+).*""".r
      val resultNumRegex2 = """.*value="(\d+)" id="resultNum".*""".r
      val resultNum = (nodes \\ "input").toString() match {
        case resultNumRegex1(resultNum) => resultNum.toInt
        case resultNumRegex2(resultNum) => resultNum.toInt
      }
      if (resultNum > 490) 490
      else if(resultNum == 0) 0
      else (resultNum / 10 - (if (resultNum % 10 == 0) 1 else 0)) * 10
    }

    def checkThisYear(year: Int): Int = {
      if (year > limitYear) -1
      else {
        val firstPage = post(user.nickName, user.birth, year, 0, testCookie, testData)
        val maxOff = caculOffset(firstPage)
        if (isUserOnThisPage(firstPage)) return year
        else if (isUserOnThosePages(10, maxOff, year)) return year
        else checkThisYear(year + 1)
      }
    }

    def isUserOnThosePages(offset: Int, maxOff: Int, year: Int): Boolean = {
      if (offset > maxOff) false
      else {
        val nodes = post(user.nickName,user.birth,year,offset,testCookie,testData)
        if (isUserOnThisPage(nodes)) true
        else isUserOnThosePages(offset + 10, maxOff, year)
      }
    }

    def isUserOnThisPage(nodes: xml.Node): Boolean = {
      val idList = for (one <- nodes \\ "li" \\ "strong") yield {
        (one \\ "@href").text match {
          case idRegex(_, id, _) => id.toLong
        }
      }
      idList.exists(_ == user.uid)
    }

    def post(name: String, birth: Int, entryYear: Int, offset: Int,
             cookie: mutable.Map[String,String], data: mutable.Map[String,String]): xml.Node = {
      val postData = "[{\"t\":\"univ\",\"year\":\"%d\"},{\"t\":\"birt\",\"year\":\"%d\"}]".format(entryYear,birth)
      data("p") = postData
      data("q") = name
      data("offset") = offset.toString
      val url = """http://browse.renren.com/sAjax.do?ref_search=searchResult_People_Tab&ajax=1&q=%s&p=%s&s=0&u=%s&act=search&offset=%d&sort=0"""
        .format(encode(name,"utf-8"),
          encode("[{\"t\":\"univ\",\"year\":\"%d\"},{\"t\":\"birt\",\"year\":\"%d\"}]"
            .format(entryYear,birth), "UTF-8"), data("u"), offset)
      val doc = HtmlGetter.getHtmlByPost(url, cookie, data)
//      println(url)
      try {
        xml.XML.loadString(doc.toString)
      } catch {
        case _: org.xml.sax.SAXParseException =>
//          println(doc.toString.
//            replaceAll("""target="_blank">.*</a></strong>""", """target="_blank">"""+ name + "</a></strong>")
//            .replaceAll("""data.name=[\w&#;]+""", "data.name=\"%s\"".format(name)))
          xml.XML.loadString(doc.toString.
            replaceAll("""target="_blank">.*</a></strong>""", """target="_blank">"""+ name + "</a></strong>")
           .replaceAll("""name=.*" """, "name=\"%s\" ".format(name)))
      }
    }

    checkThisYear(user.birth)

  }
}
