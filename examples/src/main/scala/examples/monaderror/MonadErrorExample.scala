package examples.monaderror

import cats.MonadError
import cats.implicits._

case class CreateUserInputData(name: String, email: String, password: String)
case class CreateUserOutputData(user: User)

trait AppError
case class AlreadyExists(message: String) extends AppError
case class SomeError(message: String) extends AppError

/**
 * MonadError Example 1:
 * if user exists, throw Exception
 * else, save user's data
 */
class CreateUserUseCase[F[_]](implicit userRepository: UserRepository[F]) {
  def execute(input: CreateUserInputData)(implicit ME: MonadError[F, AppError]): F[CreateUserOutputData] =
    for {
      userOpt <- userRepository.findByEmail( input.email )
      res <- userOpt
        .map(user => ME.raiseError(AlreadyExists(s"Already Exists $user")))
        .getOrElse(userRepository.create(input.name, input.email, input.password))
    } yield CreateUserOutputData( res )
}
