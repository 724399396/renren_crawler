import akka.actor.{ActorSystem, Props, Actor}
import akka.routing.RoundRobinRouter

import scala.collection.mutable.ArrayBuffer

/**
 * Created by li-wei on 2015/4/18.
 */
object Main extends App {
  val tokens = CookieAndPostData.allTokens
  //getUser(1980 to 2000, 0)
  //getPhoto()
  savePhoto()

  import collection.mutable.Map
  sealed class Message
  case class QueryCondition(birth: Int, limit: Int, cookie: Map[String, String], tempData: Map[String, String]) extends Message
  case class UserMessage(user: User) extends Message
  case class BirthAndLimit(birthList: Range, limit: Int) extends Message

  case class WillFetchUser() extends Message
  case class UserCondition(user: User) extends Message
  case class PhotoMessage(photo: Photo) extends Message

  case class SavePhoto() extends Message
  case class PhotoCondition(photos: ArrayBuffer[Photo]) extends Message

  class Crawler extends Actor {
    def receive() = {
      case QueryCondition(birth,limit,cookie,tempData) =>
        UserGetter.getUserByBirth(birth,limit,cookie,tempData).foreach( user => sender ! UserMessage(user))
      case UserCondition(user: User) =>
        PhotoGetter.getAvatarPhotoUrl(user.avatarAlbum).foreach(y => {
          sender ! PhotoMessage(new Photo(user.nickName, y._1 - user.birth, y._2.replaceAll("\\\\","")))
        })
        DBManager.changeUserIsFetch(user)
      case PhotoCondition(photos) =>
        PhotoSaver.saveUserImage(photos)
    }
  }

  class Master(nrWorker: Int) extends Actor {
    val worker = context.actorOf(Props[Crawler].withRouter(RoundRobinRouter(nrWorker)), "crawler")
    def receive() = {
      case BirthAndLimit(birthList, limit) => birthList.foreach(birth =>
        tokens.foreach(token => worker ! QueryCondition(birth, limit, token(0), token(1)))
      )
      case UserMessage(user) => DBManager.saveUser(user); println(user)
      case WillFetchUser() => DBManager.allUsers().foreach(user =>
        worker ! UserCondition(user)
      )
      case PhotoMessage(photo) => DBManager.savePhoto(photo); println(photo)
      case SavePhoto() => PhotoSaver.map.foreach(token => worker ! PhotoCondition(token._2))
    }
  }

  def getUser(birthList: Range, limit: Int) = {
    val system = ActorSystem("ren-ren-actor")
    val master = system.actorOf(Props(new Master(8)), name="master")
    master ! BirthAndLimit(birthList, limit)
  }

  def getPhoto() = {
    val system = ActorSystem("ren-ren-actor")
    val master = system.actorOf(Props(new Master(8)), name="master")
    master ! WillFetchUser()
  }

  def savePhoto() = {
    val system = ActorSystem("ren-ren-actor")
    val master = system.actorOf(Props(new Master(30)), name="master")
    master ! SavePhoto()
  }
}
