import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.RoundRobinRouter
import collection.mutable.Map

object AllInOne extends App {
  val tokens = CookieAndPostData.allTokens
  autoDoneAll(1970 to 1973, 490, (1970 to 1973).toList, 15 to 40, 10000)

  case class AllMessage(birthList: Range, userNumLimit: Int,
                        userList: List[Int], ageList: Range, photoLimit: Int)

  sealed class Message
  case object FirstStep extends Message
  case class QueryCondition(birth: Int, limit: Int, cookie: Map[String, String], tempData: Map[String, String], where: String) extends Message
  case class UserMessage(user: User) extends Message
  case class QueryFinish(birth:Int, limit:Int, where: String) extends Message

  case object SecondStep extends Message
  case class UserCondition(user: User) extends Message
  case class PhotoMessage(photo: Photo) extends Message
  case class UserFinish(user: User) extends Message

  case object ThirdStep extends Message
  case class PhotoCondition(photo: Photo) extends Message
  case class PhotoFinish(photo: Photo) extends Message

  class Crawler extends Actor {
    def receive() = {
      case QueryCondition(birth,limit,cookie,tempData, where) =>
        UserGetter.getUserByBirth(birth,limit,cookie,tempData, where).foreach(
          { user => DBManager.saveUser(user);sender ! UserMessage(user) })
        sender ! QueryFinish(birth, limit, where)
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

  class Master(nrWorker: Int, message: AllMessage) extends Actor {
    val AllMessage(birthList, userNumLimit, userList, ageList, photoLimit) = message
    val worker = context.actorOf(Props[Crawler].withRouter(RoundRobinRouter(nrWorker)), "crawler")
    var taskSend, taskReceive = 0
    def receive() = {
      case FirstStep =>
        birthList.foreach(birth =>
        tokens.foreach(token => {
          taskSend += 1
          worker ! QueryCondition(birth, userNumLimit, token(0), token(1), token(2).keys.head)}))
      case UserMessage(user) =>
        println(user)
      case QueryFinish(birth, limit, where) =>
        taskReceive += 1
        printf("%s(%s) finish, %d / %d %n", birth, where, taskReceive, taskSend)
        if (taskReceive == taskSend) { println("user get done!"); taskSend = 0; taskReceive = 0; self ! SecondStep }

      case SecondStep => userList.flatMap(DBManager.notFetchUsersByBirth)
        .foreach(user => {
          worker ! UserCondition(user)
          taskSend += 1
        }
      )
      case PhotoMessage(photo) => println(photo)
      case UserFinish(user) =>
        taskReceive += 1
        printf("%s is Finish, %d / %d %n", user.nickName, taskReceive, taskSend)
        if (taskReceive == taskSend) { println("user fetch done!"); taskSend = 0; taskReceive = 0; self ! ThirdStep }

      case ThirdStep => ageList.flatMap(age =>
        DBManager.notSavePhotosByAge(age).take(photoLimit-DBManager.hasSavedPhotoNumByAge(age)))
        .foreach(photo => { worker ! PhotoCondition(photo); taskSend += 1})
      case PhotoFinish(photo) =>
        taskReceive += 1
        printf("%s is Finish, %d / %d %n", photo, taskReceive, taskSend)
        if (taskReceive == taskSend) { println("photo fetch done!"); System.exit(0) }
    }
  }



  def autoDoneAll(birthList: Range, userNumLimit: Int,
                  userList: List[Int], ageList: Range, photoLimit: Int): Unit = {
    val system = ActorSystem("ren-ren-actor")
    val master = system.actorOf(Props(new Master(10,
      AllMessage(birthList, userNumLimit, userList, ageList, photoLimit))), name="master")
    master ! FirstStep
  }



}
