package examples.eitherT

import cats.Monad
import cats.data.EitherT
import cats.Id

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

sealed trait Error
case class NotFound(name: String) extends Error

case class UserId(value: Long)
case class Name(value: String)
case class User(id: UserId, name: Name)

abstract class UserRepository[F[_]](implicit F: Monad[F]) {
  def followers(id: UserId): EitherT[F, Error, List[User]]

  def isFriends(u1Id: UserId, u2Id: UserId): EitherT[F, Error, Boolean] =
    for {
      u1followers <- followers( u1Id )
      u2followers <- followers( u2Id )
    } yield  u1followers.exists(_.id == u2Id) && u2followers.exists(_.id == u1Id)
}

class UseCase[F[_]](implicit userRepository: UserRepository[F], F: Monad[F]) {
  def execute(u1Id: UserId, u2Id: UserId): EitherT[F, Error, Boolean] =
    userRepository.isFriends( u1Id, u2Id )
}

object EitherTExample2 {
  val user1: User = User( UserId(1000), Name("Foo") )
  val user2: User = User( UserId(2000), Name("Bar") )

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
  implicit val userRepositoryFuture: UserRepository[Future] = new UserRepository[Future] {
    def followers(id: UserId): EitherT[Future, Error, List[User]] = EitherT.right( Future.successful( List(user1, user2) ) )
  }
  val useCaseWithFuture = new UseCase
}
