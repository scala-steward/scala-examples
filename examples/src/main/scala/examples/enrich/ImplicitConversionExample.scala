package examples.enrich
object ImplicitConversionExample {
  lazy val list: List[Int] = List(1, 2, 3).eachAdd(3)
}