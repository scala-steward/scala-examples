package examples.typeclass

import org.scalatest.flatspec.AnyFlatSpec

class TypeClassExampleTest extends AnyFlatSpec {

  // genericなクラスに対して、制約をかけたい。

  val calculation = new Calculation

  "Type Class Value[Int]" should "be provided sufficient constraints on generic classes" in {
    val viList: List[Value[Int]] = List( Value(1), Value(2), Value(3) )
    val res: Value[Int]          = calculation.sum(viList)
    val expected: Value[Int]     = Value(6)
    assert( res == expected)
  }

  "Type Class Value[String]" should "be provided sufficient constraints on generic classes" in {
    val vsList: List[Value[String]] = List( Value("Hello "), Value("World "), Value("!") )
    val res: Value[String]          = calculation.sum(vsList)
    val expected: Value[String]     = Value("Hello World !")
    assert( res == expected)
  }

}
