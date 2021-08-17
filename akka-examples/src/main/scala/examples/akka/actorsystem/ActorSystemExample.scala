package examples.akka.actorsystem

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

class SquareActor extends Actor {
  private def square(v: Int): Int = v
  def receive: Receive = {
    case v: Int => sender() ! square(v)
  }
}
object Test {
  val system: ActorSystem = ActorSystem("test")
  implicit val timeout: Timeout = Timeout(100 milliseconds)
  val squareActor: ActorRef = system.actorOf(Props[SquareActor]())
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  def execute(list: Seq[Int]): Unit =
    try {
      // actorによって非同期に処理されることの確認
      list.foreach(v => (squareActor ? v).mapTo[Int].onComplete{
        case Success(value) => print(s"$value ")
        case Failure(exception) => println(exception.getMessage)
      })
    } finally {
      system.terminate()
    }
}

// execute(1 to 100)
// 1 2 4 3 5 6 7 9 10 8 11 12 13 14 15 16 18 19 20 21 22 23 25 17 27 28 24 34 35 36 33 32 31 37 30 29 26 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60 61 62 64 66 67 63 68 69 71 72 65 73 74 75 77 70 78 81 82 83 76 85 86 87 84 92 93 94 95 97 98 99 100 80 79 96 90 91 89 88
