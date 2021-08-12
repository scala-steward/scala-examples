package examples.enrich

import org.scalatest.flatspec.AnyFlatSpec

class ImplicitConversionExampleTest extends AnyFlatSpec {
  "Implicit Conversion" should "work as expected for List.eachAdd" in {
    val res: List[Int] = List(1, 2, 3).eachAdd(2)
    val expected: List[Int] = List(3, 4, 5)
    assert( res == expected )
  }
}