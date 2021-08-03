package examples.validated

import org.scalatest.flatspec.AnyFlatSpec
import cats.Id
import cats.data.NonEmptyList

class ValidatedExampleTest extends AnyFlatSpec {

  val signInValidation: SignInValidation[Id] = new SignInValidation[Id]() {}

  "ValidatedNel" should "works as expected to be successful when it has correct data." in {
    val signInRequestData: SignInRequestData = SignInRequestData("test@example.com", "Passw0r$1234")
    val validatedRes = signInValidation.validate( signInRequestData ).toEither
    val expected     = Right( SignInInputData(Email("test@example.com"), Password("Passw0r$1234")) )
    assert( validatedRes == expected )
  }

  it should "works as expected to be failed when it has incorrect Email." in {
    val signInRequestData: SignInRequestData = SignInRequestData("incorrect", "Passw0r$1234")
    val validatedRes3 = signInValidation.validate( signInRequestData ).toEither
    val expected3     = Left(NonEmptyList(EmailValidation, List()))
    assert( validatedRes3 == expected3 )
  }

  it should "works as expected to be failed when it has incorrect Password." in {
    val signInRequestData: SignInRequestData = SignInRequestData("test@example.com", "incorrect")
    val validatedRes2 = signInValidation.validate( signInRequestData ).toEither
    val expected2     = Left(NonEmptyList(PasswordValidation, List()))
    assert( validatedRes2 == expected2 )
  }

  it should "works as expected to be failed when it has incorrect Email and Password." in {
    val signInRequestData: SignInRequestData = SignInRequestData("incorrect", "incorrect")
    val validatedRes3 = signInValidation.validate( signInRequestData ).toEither
    val expected3     = Left(NonEmptyList(EmailValidation, List(PasswordValidation)))
    assert( validatedRes3 == expected3 )
  }

}