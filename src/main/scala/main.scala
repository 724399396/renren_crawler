import akka.actor.{ActorSystem, Props, Actor}
import akka.routing.{RoundRobinRouter,SmallestMailboxRouter}

/**
 * Created by li-wei on 2015/4/18.
 */
object Main extends App {
  val tokens = CookieAndPostData.allTokens
  //getUser(1973 to 2000, 0)
  //getUser(1970 to 1972, 490)
  //getUser(1999 to 2000, 490)
  //getPhoto(List(1973,1974,1975,1997,1998,1999,2000))
  getPhoto(1970 to 2000 toList)
  //savePhoto(15 to 40,10000)

  import collection.mutable.Map
  sealed class Message
  case class QueryCondition(birth: Int, limit: Int, cookie: Map[String, String], tempData: Map[String, String], where: String) extends Message
  case class UserMessage(user: User) extends Message
  case class BirthAndLimit(birthList: Range, limit: Int) extends Message
  case class QueryFinish(birth:Int, limit:Int) extends Message

  case class WillFetchUser(userList: List[Int]) extends Message
  case class UserCondition(user: User) extends Message
  case class PhotoMessage(photo: Photo) extends Message
  case class UserFinish(user: User) extends Message

  case class SavePhoto(ageList: Range, photoLimit: Int) extends Message
  case class PhotoCondition(photo: Photo) extends Message
  case class PhotoFinish(photo: Photo) extends Message

  class Crawler extends Actor {
    def receive() = {
      case QueryCondition(birth,limit,cookie,tempData, where) =>
        UserGetter.getUserByBirth(birth,limit,cookie,tempData, where).foreach(
          { user => DBManager.saveUser(user);sender ! UserMessage(user) })
        sender ! QueryFinish(birth, limit)
      case UserCondition(user: User) =>
        PhotoGetter.getAvatarPhotoUrl(user.avatarAlbum).foreach(y => {
          val photo = new Photo(0,user.nickName, y._1 - user.birth, user.whereFrom, y._2.replaceAll("\\\\",""), y._2.replaceAll("\\\\","").hashCode)
          if (photo.getAvatarUrl.trim != null) {
            DBManager.savePhoto(photo)
            sender ! PhotoMessage(photo)
          }
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
    var taskSend, taskReceive = 0
    def receive() = {
      case BirthAndLimit(birthList, limit) => birthList.foreach(birth =>
        tokens.foreach(token => {
          taskSend += 1
          worker ! QueryCondition(birth, limit, token(0), token(1), token(2).keys.head)}))
      case UserMessage(user) =>
        println(user)
      case QueryFinish(birth, limit) =>
        taskReceive += 1
        printf("%s finish, %d / %d %n", birth, taskReceive, taskSend)
        if (taskReceive == taskSend) { println("user get done!"); System.exit(0) }

      case WillFetchUser(userList) => userList.flatMap(DBManager.notFetchUsersByBirth)
        .foreach(user => {
          worker ! UserCondition(user)
          taskSend += 1
        }
      )
      case PhotoMessage(photo) => println(photo)
      case UserFinish(user) =>
        taskReceive += 1
        printf("%s is Finish, %d / %d %n", user.nickName, taskReceive, taskSend)
        if (taskReceive == taskSend) { println("user fetch done!"); System.exit(0) }

      case SavePhoto(ageList, photoLimit) => ageList.flatMap(age =>
        DBManager.notSavePhotosByAge(age).take(photoLimit-DBManager.hasSavedPhotoNumByAge(age)))
        .foreach(photo => { worker ! PhotoCondition(photo); taskSend += 1})
      case PhotoFinish(photo) =>
        taskReceive += 1
        printf("%s is Finish, %d / %d %n", photo, taskReceive, taskSend)
        if (taskReceive == taskSend) { println("photo fetch done!"); System.exit(0) }
    }
  }

  def getUser(birthList: Range, limit: Int) = {
    val system = ActorSystem("ren-ren-actor")
    val master = system.actorOf(Props(new Master(3)), name="master")
    master ! BirthAndLimit(birthList, limit)
  }

  def getPhoto(userList: List[Int]) = {
    val system = ActorSystem("ren-ren-actor")
    val master = system.actorOf(Props(new Master(6)), name="master")
    master ! WillFetchUser(userList)
  }

  def savePhoto(ageList: Range, photoLimit: Int) = {
    val system = ActorSystem("ren-ren-actor")
    val master = system.actorOf(Props(new Master(50)), name="master")
    master ! SavePhoto(ageList, photoLimit)
  }

}
