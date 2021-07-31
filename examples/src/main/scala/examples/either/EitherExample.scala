package examples.either

import cats.Monad
import cats.implicits._

sealed trait Error
case class UserNotFound(errorMsg: String) extends Error

case class UserId(value: Long)
case class Name(value: String)
case class User(id: UserId, name: Name)

abstract class UserRepository[F[_]](implicit F: Monad[F]) {
  def followers(userId: UserId): F[Either[Error, List[User]]]

  // 内側のモナドにアクセスするのが面倒
  def isFriends(userId1: UserId, userId2: UserId): F[Either[Error, Boolean]] =
    for {
      user1 <- followers( userId1 )
      user2 <- followers( userId2 )
    } yield for {
      followers1 <- user1
      followers2 <- user2
    } yield followers1.exists(_.id == userId2) && followers2.exists(_.id == userId1)
}

class UseCase[F[_]](implicit userRepository: UserRepository[F], F: Monad[F]) {
  def execute( userId1: UserId, userId2: UserId): F[Either[Error, Boolean]] =
    userRepository.isFriends( userId1, userId2 )
}
