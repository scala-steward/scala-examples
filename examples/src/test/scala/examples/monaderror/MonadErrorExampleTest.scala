package examples.monaderror

import cats.{MonadError, StackSafeMonad}
import cats.Id
import org.scalatest.flatspec.AnyFlatSpec

class MonadErrorExampleTest extends AnyFlatSpec {

  implicit val idMonadError: MonadError[Id, AppError] = new MonadError[Id, AppError] with StackSafeMonad[Id] {
    override def pure[A](x: A): Id[A]                                       = x
    override def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B]             = f(fa)
    override def raiseError[A](e: AppError): Id[A]                          = throw new Exception(e.toString)
    override def handleErrorWith[A](fa: Id[A])(f: AppError => Id[A]): Id[A] = ???
  }

  implicit val userRepository: UserRepository[Id] = new UserRepository[Id] {
    override def findByEmail(email: String): Id[Option[User]] = email match {
      case "test@example.com" => Some(User("Bar", "test@example.com"))
      case _                  => None
    }
    override def create(name: String, email: String, password: String): Id[User] = User(name, email)
  }

  val createUserUseCase = new CreateUserUseCase[Id]

  "createUserUseCase" should "throw Exception with message AlreadyExists" in {
    assert{
      intercept[java.lang.Exception] {
        createUserUseCase.execute(CreateUserInputData("Foo", "test@example.com", "pass"))
      }.getMessage == "AlreadyExists(Already Exists User(Bar,test@example.com))"
    }
  }

  it should "create user's data as expected" in {
    val user = User("Taro", "sample@example.com")
    val name     = user.name
    val email    = user.email
    val password = "testPassword"
    assert( createUserUseCase.execute(CreateUserInputData(name, email, password)) == CreateUserOutputData(user) )
  }

}