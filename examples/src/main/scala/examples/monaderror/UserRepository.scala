package examples.monaderror

trait UserRepository[F[_]] {
  def findByEmail(email: String): F[Option[User]]
  def create(name: String, email: String, password: String): F[User]
}
