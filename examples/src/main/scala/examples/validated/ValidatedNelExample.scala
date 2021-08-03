package examples.validated

import cats.Monad
import cats.data.ValidatedNel
import cats.implicits._

case class Password(value: String)
case class Email(value: String)
case class UserAccount(password: Password, email: Email)

sealed trait DomainValidation {
  def errorMessage: String
}
case object EmailValidation extends DomainValidation {
  def errorMessage: String = "Email must have a mark [@]."
}
case object PasswordValidation extends DomainValidation {
  def errorMessage: String = "Password must be at least 10 characters long, including an uppercase and a lowercase letter, one number and one special character."
}

case class SignInRequestData(email: String, password: String)
case class SignInInputData(email: Email, password: Password)

abstract class SignInValidation[F[_]](implicit F: Monad[F]) {
  type ValidationResult[A] = ValidatedNel[DomainValidation, A]

  def validateEmail(email: String): F[ValidationResult[Email]] =
    if(email.matches("[^@]+@[^@]+")) F.pure( Email(email).validNel )
    else F.pure( EmailValidation.invalidNel )

  def validatePassword(password: String): F[ValidationResult[Password]] =
    if(password.matches("(?=^.{10,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")) F.pure( Password(password).validNel )
    else F.pure( PasswordValidation.invalidNel )

  def validate(signInRequestData: SignInRequestData): F[ValidationResult[SignInInputData]] =
    for {
      validatedEmail    <- validateEmail( signInRequestData.email )
      validatedPassword <- validatePassword( signInRequestData.password )
    } yield ( validatedEmail, validatedPassword )
      .mapN( SignInInputData )
}
