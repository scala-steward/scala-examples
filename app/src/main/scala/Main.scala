object Main {

  object EitherTExampleSample {
    import examples.eitherT.EitherTExample2._
    import scala.util.{Success, Failure}
    def executeMain(): Unit =
      examples.eitherT.EitherTExample2.useCaseWithFuture.execute( user1.id, user2.id ).value.onComplete {
        case Success(v) => println(v)
        case Failure(t) => println(s"Error: ${t.getMessage}")
      }
  }

  def main(args: Array[String]): Unit = {
    EitherTExampleSample.executeMain()
  }
}