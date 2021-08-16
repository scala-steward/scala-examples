package examples.akka.stream

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

// usage of Source, Flow, Sink
object AkkaStreamEasyExample {
  implicit val system: ActorSystem = ActorSystem("akka-stream-easy-example")
  implicit val materializer: Materializer.type = Materializer

  val source:   Source[Int, NotUsed]       = Source(1 to 10)
  val flow:     Flow[Int, Int, NotUsed]    = Flow[Int].map(x => x * 2)
  val sink:     Sink[Int, Future[Int]]     = Sink.fold(0)(_ + _)
  val runnable: RunnableGraph[Future[Int]] = source.via(flow).toMat(sink)(Keep.right)

  def execute(): Unit = {
    try {
      val sum: Future[Int] = runnable.run()
      implicit val executionContext: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
      sum onComplete {
        case Success(value) => println(value)
        case Failure(exception) => println(exception.getMessage)
      }
    } finally {
      system.terminate()
    }
  }
}

// example of execution
//execute // 110