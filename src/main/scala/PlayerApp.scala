import akka.actor._

object PlayerApp {

  val system = ActorSystem()

  def main(args: Array[String]): Unit = run()

  def run(): Unit = {
    val player = system.actorOf(Props[Player], "PlayerFSM")

    player ! PlayPause
    player ! PlayPause
    player ! PlayPause
    player ! SkipForward
    player ! SkipForward
    player ! Stop
    player ! Stop
    player ! PlayPause

    //system.shutdown()
  }

}
