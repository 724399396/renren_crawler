import akka.actor.{Props, ActorSystem, Actor}

object Manager extends Actor {
  def receive = {
    case "first" => println("1")
    case "second" => println("2")
  }
}

object test2 extends App {

  val system = ActorSystem("ren-ren-actor")
  val manager = system.actorOf(Props(Manager), name = "manager")
  manager ! "first"
}