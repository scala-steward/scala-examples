package examples.subtype

import org.scalatest.flatspec.AnyFlatSpec

class SubTypeExampleTest extends AnyFlatSpec {
  // サブタイプについて
  import Service._
  "SubType" should "work as expected" in {
    assert( calcOptionAmount( Nike, Red, Adidas).unsafeRunSync() == Amount(81000) )
  }
}