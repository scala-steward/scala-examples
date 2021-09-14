package examples.mockito

import org.scalatest.flatspec.AnyFlatSpec
import org.mockito.Mockito._
import cats.Id
import org.mockito.Mock

class UserRepositorySpec extends AnyFlatSpec {

  @Mock
  val userRepository: UserRepository[Id] = mock(classOf[UserRepository[Id]])

  when(userRepository.findBy(1L)).thenReturn(Some(User("Foo", 20)))
  when(userRepository.findBy(2L)).thenReturn(None)

  "UserRepository" should "be some" in {
    assert(userRepository.findBy(1L) == Some(User("Foo", 20)))
  }

  it should "be none" in {
    assert(userRepository.findBy(2L) == None)
  }
}