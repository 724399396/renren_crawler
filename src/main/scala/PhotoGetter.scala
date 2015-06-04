import java.util.{Calendar, Date}

import org.jsoup.Jsoup

import scala.collection.mutable.Map
import scala.util.Random

/**
 * Created by li-wei on 2015/4/17.
 */
object PhotoGetter {
  val rand = new Random()
  val tokens = CookieAndPostData.allTokens

//  DBManager.allUsers().foreach(x => {
//    getAvatarPhotoUrl(x.avatarAlbum).foreach(y => {
//      DBManager.savePhoto(new Photo(x.nickName, 2015 - x.birth, y.replaceAll("\\\\","")))
//    })
//    DBManager.changeUserIsFetch(x)
//  })
  def getAvatarPhotoUrl(url: String):Iterator[(Int,String)] = {
    val cookie = tokens(rand.nextInt(tokens.size))(0)
    val doc = HtmlGetter.getHtmlByGet(url, cookie)
    if(doc.title() != "人人网 - 抱歉，出错了。") {
      val page = doc.toString
      val photoListPattern = """'photoList':(\S+)""".r
      val urlPattern = """("url":")([^"]+)""".r
      val timePattern = """(\\"createTime\\":\\")([0-9]+)""".r
      val photoList = try {
         photoListPattern.findFirstIn(page).get
      } catch {
        case ex: Exception => println("异常"); println(url); println(page); println(cookie); ex.printStackTrace(); System.exit(-1); ""
      }
      val times = for (timePattern(_,time) <- timePattern.findAllIn(photoList)) yield time
      val urls = for (urlPattern(_, url) <- urlPattern.findAllIn(photoList)) yield url
      times.map(t => { val cal = Calendar.getInstance(); cal.setTime(new Date(t.toLong)); cal.getWeekYear}).zipAll(urls,2015, "")
    } else {
      Iterator()
    }
  }
}
