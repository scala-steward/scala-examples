package examples.either

import org.scalatest.flatspec.AnyFlatSpec
import cats.Id

class EitherExampleTest extends AnyFlatSpec {

  object MockIdService {
    val user1: User = User( UserId(1000), Name("Hoge") )
    val user2: User = User( UserId(2000), Name("Bar") )
    val user3: User = User( UserId(3000), Name("Foo") )

    implicit def userRepository: UserRepository[Id] = new UserRepository[Id]() {
      override def followers(userId: UserId): Either[Error, List[User]] = {
        userId match {
          case UserId(1000)       => Right( List(user2) )
          case UserId(2000)       => Right( List(user1) )

          case UserId(3000)       => Right( Nil )
          case UserId(4000)       => Right( List(user3) )

          case userId@UserId(_) => Left( UserNotFound(s"UserId ${userId.value} Not Found") )
        }
      }
    }
    val useCase = new UseCase
  }

  "2 users" should "follow each other" in {
    val mockRes1  = MockIdService.useCase.execute( UserId(1000), UserId(2000) )
    val expected1 = Right( true )
    assert( mockRes1 == expected1 )
  }

  "2 users" should "not follow each other" in {
    val mockRes2  = MockIdService.useCase.execute( UserId(3000), UserId(4000) )
    val expected2 = Right( false )
    assert( mockRes2 == expected2 )
  }

  "User" should "be not found." in {
    val mockRes3  = MockIdService.useCase.execute( UserId(4000), UserId(5000) )
    val expected3 = Left( UserNotFound("UserId 5000 Not Found") )
    assert( mockRes3 == expected3 )
  }
}