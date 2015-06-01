import scala.collection.mutable

/**
 * Created by li-wei on 2015/4/15.
 */
object UserGetter extends App {
  import collection.mutable.Map

  for(year <- 1981 to 2005)
    for (token <- CookieAndPostData.allTokens) {}


  def getUserByBirth(birth: Int, limit: Int, cookie: Map[String, String], tempData: Map[String, String], where: String): List[User] = {
    import java.net.URLEncoder._
    0.to(limit, 10).flatMap {
      x => {
        val url = "http://browse.renren.com/sAjax.do?ref_search=searchResult_People_Tab&ajax=1&q=&p=%s&s=0&u=%s&act=search&offset=%d&sort=0"
          .format(encode("[{\"t\":\"birt\",\"year\":\"%s\"}]".format(birth), "UTF-8"), cookie("id"), x)
        tempData("offset") = x.toString;
        tempData("p") = "[{\"t\":\"birt\",\"year\":\"%d\"}]".format(birth)
        getUserIdFromPage(url, cookie, tempData)
      }
    }
      .filter(x => x._4 != null).map(x => new User(0,x._1, birth, where, 0, x._2, x._3, x._4, x._5)).toList
  }

  def getUserIdFromPage(url: String, cookie: Map[String, String], data: Map[String, String]): mutable.Buffer[(String, String, String, String, Long)] = {
    import collection.JavaConversions._
    HtmlGetter.getHtmlByPost(url, cookie, data).
      select("ol#active_2012_module.fl.search_log").select("div.info").select("strong").map(
        x => {
          val pattern = "(.*)(id=)([0-9]+)(.*)".r
          val id = x.child(0).attr("href") match { case pattern(_, _, id, _) => id }
          val album = "http://photo.renren.com/photo/%s/albumlist/v7#".format(id)
          val avatarId = getAvatarIdByParse(album,cookie)
          val avatar = if(avatarId.isEmpty) null else "http://photo.renren.com/photo/%s/album-%s/v7".format(id,avatarId.get)
          (x.text, "http://www.renren.com/%s/profile".format(id), album, avatar, id.toLong)
        })
  }

  def getAvatarIdByParse(url: String, cookie: Map[String,String]): Option[String] = {
    val doc = HtmlGetter.getHtmlByGet(url,cookie)
    if(doc.title() != "人人网 - 抱歉，出错了。") {
      val page = doc.toString
      val pattern = """'albumList':\s(\S+)""".r
      val idPattern = """("albumId":")([0-9]+)""".r
      val namePattern = """("albumName":")([^"]+)""".r
      val albumList = pattern.findFirstIn(page)
      try {
        Some(albumList.get.split("}").map(x => {
          var id = "";
          var name = ""
          for (idPattern(_, idP) <- idPattern.findFirstIn(x)) {id = idP}
          for (namePattern(_, nameP) <- namePattern.findFirstIn(x)) {name = nameP}
          (id, name)
        }).filter(x => x._2 == "头像相册" || x._2 == "\\u5934\\u50cf\\u76f8\\u518c").toList.head._1)
      } catch {
        case _ : Exception =>  None
      }
    } else None
  }



}
