package examples
package object enrich {
  implicit class CalcList(val list: List[Int]) extends AnyVal {
    def eachAdd(a: Int): List[Int] = list.map(_ + a)
  }
}