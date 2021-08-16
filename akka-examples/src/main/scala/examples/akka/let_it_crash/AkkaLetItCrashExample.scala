package examples.akka.let_it_crash

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

sealed trait CalculationType
case class Multi(a: Int, b: Int) extends CalculationType
case class Divide(a: Int, b: Int) extends CalculationType

class CalculationActor extends Actor with ActorLogging {

  def mul(a: Int, b: Int): Int = a * b
  def div(a: Int, b: Int): Int = a / b

  def receive: Receive = {
    case Multi(a, b)  => sender() ! mul(a, b)
    case Divide(a, b) => sender() ! div(a, b)
    case _            => sender() ! 0
  }
}

/**
 * Let it Crash
 * ex) Divide(_, 0) の時、ArithmeticExceptionになるが、Let it crash により次処理に移る。
 */
object AkkaLetItCrashExample {
  val system: ActorSystem = ActorSystem("actor-system")
  val actor: ActorRef     = system.actorOf(Props[CalculationActor]())
  implicit val timeout: Timeout = Timeout(100 milliseconds)
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  def calculation(calcTypeList: List[CalculationType]): Unit = {
    try {
      calcTypeList.foreach{ calcType =>
        (actor ? calcType).mapTo[Int].onComplete{
          case Success(value) => println(value)
          case Failure(t) => println("Failure : " + t.getMessage)
        }
      }
    } finally system.terminate()
  }
}

//>> val calcTypeList: List[CalcTypeList] = List( Multi(1, 2), Divide(2, 0), Divide(2, 1), Divide(1, 0), Multi(2, 3) )
//>> AkkaLetItCrashExample.calculation( calcTypeList )