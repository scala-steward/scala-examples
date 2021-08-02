package examples.eitherT

import cats.data.EitherT
import cats.Id
import org.scalatest.flatspec.AnyFlatSpec

class EitherTExample2Test extends AnyFlatSpec {

  val user1: User = User( UserId(1000), Name("Foo") )
  val user2: User = User( UserId(2000), Name("Bar") )
  val user3: User = User( UserId(3000), Name("Hoge") )
  val user4: User = User( UserId(4000), Name("John") )
  val user5: User = User( UserId(5000), Name("Mike") )

  object MockIdService {
    implicit def userRepositoryOfId: UserRepository[Id] = new UserRepository[Id]() {
      def followers(id: UserId): EitherT[Id, Error, List[User]] = id match {
        case UserId(1000) => EitherT.rightT( List(user2) )
        case UserId(2000) => EitherT.rightT( List(user1) )

        case UserId(3000) => EitherT.rightT( Nil )
        case UserId(4000) => EitherT.rightT( List(user3) )

        case userId@UserId(_) => EitherT.leftT( NotFound(s"UserId ${userId.value} Not Found") )
      }
    }
    val useCase = new UseCase
  }

  "EitherT" should "works as expected to follow each other " in {
    val mockRes1  = MockIdService.useCase.execute( user1.id, user2.id ).value
    val expected1 = Right( true )
    assert( mockRes1 == expected1 )
  }

  it should "work as expected not to follow each other" in {
    val mockRes2  = MockIdService.useCase.execute( user3.id, user4.id ).value
    val expected2 = Right( false )
    assert( mockRes2 == expected2 )
  }

  it should "work as expected to be not found." in {
    val mockRes3  = MockIdService.useCase.execute( user4.id, user5.id ).value
    val expected3 = Left( NotFound("UserId 5000 Not Found") )
    assert( mockRes3 == expected3 )
  }

}