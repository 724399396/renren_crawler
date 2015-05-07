import akka.actor.{ActorSystem, Props, Actor}
import akka.routing.{RoundRobinRouter,SmallestMailboxRouter}

/**
 * Created by li-wei on 2015/4/18.
 */
object Main extends App {
  val tokens = CookieAndPostData.allTokens
  //getUser(1975 to 2002, 490)
  getPhoto()
  //savePhoto(6,40)

  import collection.mutable.Map
  sealed class Message
  case class QueryCondition(birth: Int, limit: Int, cookie: Map[String, String], tempData: Map[String, String], where: String) extends Message
  case class UserMessage(user: User) extends Message
  case class BirthAndLimit(birthList: Range, limit: Int) extends Message

  case class WillFetchUser() extends Message
  case class UserCondition(user: User) extends Message
  case class PhotoMessage(photo: Photo) extends Message
  case class UserFinish(user: User) extends Message

  case class SavePhoto(start: Int, end: Int) extends Message
  case class PhotoCondition(photo: Photo) extends Message
  case class PhotoFinish(photo: Photo) extends Message

  class Crawler extends Actor {
    def receive() = {
      case QueryCondition(birth,limit,cookie,tempData, where) =>
        UserGetter.getUserByBirth(birth,limit,cookie,tempData, where).foreach(
        { user => DBManager.saveUser(user);sender ! UserMessage(user) })
      case UserCondition(user: User) =>
        PhotoGetter.getAvatarPhotoUrl(user.avatarAlbum).foreach(y => {
          val photo = new Photo(0,user.nickName, y._1 - user.birth, user.whereFrom, y._2.replaceAll("\\\\",""))
          DBManager.savePhoto(photo)
          sender ! PhotoMessage(photo)
        })
        DBManager.changeUserIsFetch(user)
        sender ! UserFinish(user)
      case PhotoCondition(photo) =>
        PhotoSaver.saveUrlImage(photo)
        DBManager.changePhotoIsFetch(photo)
        sender ! PhotoFinish(photo)
    }
  }

  class Master(nrWorker: Int) extends Actor {
    val worker = context.actorOf(Props[Crawler].withRouter(RoundRobinRouter(nrWorker)), "crawler")
    def receive() = {
      case BirthAndLimit(birthList, limit) => birthList.foreach(birth =>
        tokens.foreach(token => worker ! QueryCondition(birth, limit, token(0), token(1), token(2).keys.head))
      )
      case UserMessage(user) => println(user)

      case WillFetchUser() => DBManager.allUsers().foreach(user =>
        worker ! UserCondition(user)
      )
      case PhotoMessage(photo) => println(photo)
      case UserFinish(user) => println(user.nickName + " is Finish")

      case SavePhoto(start, end) => (start to end).flatMap(DBManager.photosByAge _).foreach(photo => worker ! PhotoCondition(photo))
      case PhotoFinish(photo) => println(photo)
    }
  }

  def getUser(birthList: Range, limit: Int) = {
    val system = ActorSystem("ren-ren-actor")
    val master = system.actorOf(Props(new Master(8)), name="master")
    master ! BirthAndLimit(birthList, limit)
  }

  def getPhoto() = {
    val system = ActorSystem("ren-ren-actor")
    val master = system.actorOf(Props(new Master(10)), name="master")
    master ! WillFetchUser()
  }

  def savePhoto(start: Int, end: Int) = {
    val system = ActorSystem("ren-ren-actor")
    val master = system.actorOf(Props(new Master(10)), name="master")
    master ! SavePhoto(start, end)
  }
}
