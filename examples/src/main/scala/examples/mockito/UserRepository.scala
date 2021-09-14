package examples.mockito

case class User(name: String, age: Int)

trait UserRepository[F[_]] {
  def findBy(id: Long): F[Option[User]]
}