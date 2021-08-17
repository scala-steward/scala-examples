package examples.akka.stream

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorAttributes, Materializer, Supervision}
import akka.stream.Supervision.Decider
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

case class Divide(a: Int, b: Int)

object AkkaStreamDeciderExample {

  implicit val system: ActorSystem = ActorSystem("akka-stream-decider-example")
  implicit val materializer: Materializer.type = Materializer

  val decider: Decider = {
    case _: ArithmeticException => Supervision.Resume
    case _                      => Supervision.Stop
  }

  val source: Seq[Int] => Source[Int, NotUsed] = input => Source( input )
  val mkDivFlow: Flow[Int, Divide, NotUsed] = Flow[Int].map{ v =>
      if(v % 2 == 0) Divide(v, 0)
      else Divide(v, 1)
  }
  val divFlow: Flow[Divide, Int, NotUsed] = Flow[Divide].map {
    case Divide(a, b) => println(a); a / b
  }.withAttributes(ActorAttributes.supervisionStrategy(decider))
  val sink: Sink[Int, Future[Int]] = Sink.fold(0)(_ + _)

  def runnable(input: Seq[Int]): RunnableGraph[Future[Int]] =
    source(input).via(mkDivFlow).via(divFlow).toMat(sink)(Keep.right)

  def execute(input: Seq[Int]): Unit = {
    try {
      val sum: Future[Int] = runnable( input ).run()
      implicit val executionContext: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
      sum onComplete {
        case Success(value) => println(value)
        case Failure(e)     => println(e.getMessage)
      }
    } finally {
      system.terminate()
    }
  }
}
