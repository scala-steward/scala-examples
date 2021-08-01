package examples.eitherT

object EitherTExample1 {

  import cats.data.EitherT
  import scala.concurrent.{ExecutionContext, Future}
  import scala.util.{Failure, Success}

  sealed trait Error
  case class NotFound(msg: String) extends Error

  implicit val ec: ExecutionContext = ExecutionContext.Implicits.global

  def f1: EitherT[Future, Error, Int] = EitherT.right(Future.successful(1))
  def f2: EitherT[Future, Error, Int] = EitherT.right(Future.successful(2))

  val res: EitherT[Future, Error, Int] =
    for {
      v1 <- f1
      v2 <- f2
    } yield v1 + v2

  def execute(): Unit =
    res.value.onComplete{
      case Success(v) => println(v) // 3
      case Failure(t) => println("Error: " + t.getMessage)
    }

}