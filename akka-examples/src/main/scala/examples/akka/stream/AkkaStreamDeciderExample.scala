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

  val source: Seq[Divide] => Source[Divide, NotUsed] = input => Source( input )
  val flow: Flow[Divide, Int, NotUsed] = Flow[Divide].map {
    case Divide(a, b) => a / b
  }.withAttributes(ActorAttributes.supervisionStrategy(decider))
  val sink: Sink[Int, Future[Int]] = Sink.fold(0)(_ + _)

  def runnable(input: Seq[Divide]): RunnableGraph[Future[Int]] =
    source(input).via(flow).toMat(sink)(Keep.right)

  def execute(input: Seq[Divide]): Unit = {
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

// execute( Seq(Divide(2, 1), Divide(1, 0), Divide(6, 2), Divide(2, 0)) ) // 5
