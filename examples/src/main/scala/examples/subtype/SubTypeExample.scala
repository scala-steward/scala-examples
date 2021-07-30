package examples.subtype

import cats.effect.IO

case class Amount(v: Int)
sealed trait Logo { def amount: Amount }
case object Nike extends Logo { def amount: Amount = Amount(50000) }
case object Adidas extends Logo { def amount: Amount = Amount(30000) }

sealed trait Color { def amount: Amount }
case object Red extends Color{ def amount: Amount = Amount(1000) }
case object Green extends Color{ def amount: Amount = Amount(3000) }

trait UniformService[F[_]] {
  def chooseUniformLogo(logo: Logo): F[Amount]
}

trait PantsService[F[_]] {
  def chooseColor(color: Color): F[Amount]
}

trait ShoesService[F[_]] {
  def chooseShoesLogo(logo: Logo): F[Amount]
}

// 例) ユニフォームにつけるオプションの金額を求める
object Service {

  implicit val service: UniformService[IO] with PantsService[IO] with ShoesService[IO] =
    new UniformService[IO] with PantsService[IO] with ShoesService[IO] {
      def chooseUniformLogo(uniLogo: Logo): IO[Amount]   = IO{ uniLogo.amount }
      def chooseColor(color: Color): IO[Amount] = IO{ color.amount }
      def chooseShoesLogo(shoLogo: Logo): IO[Amount] = IO{ shoLogo.amount }
    }

  def calcOptionAmount[Service <: UniformService[IO] with PantsService[IO] with ShoesService[IO]](uniLogo: Logo, color: Color, shoLogo: Logo)(implicit service: Service): IO[Amount] =
    for {
      uniLogoAmount    <- service.chooseUniformLogo( uniLogo )
      pantsColorAmount <- service.chooseColor( color )
      shoLogoAmount    <- service.chooseShoesLogo( shoLogo )
    } yield Amount( uniLogoAmount.v + pantsColorAmount.v + shoLogoAmount.v )

}
