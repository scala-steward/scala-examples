package examples.typeclass

trait Monoid[A] {
  def zero: A
  def append(a1: A, a2: A): A
}
object Monoid {
  implicit def intMonoid: Monoid[Int] = new Monoid[Int] {
    def zero: Int = 0
    def append(a1: Int, a2: Int): Int = a1 + a2
  }

  implicit def strMonoid: Monoid[String] = new Monoid[String] {
    def zero: String = ""
    def append(a1: String, a2: String): String = a1 + a2
  }

  implicit def valueIntMonoid(implicit intM: Monoid[Int]): Monoid[Value[Int]] = new Monoid[Value[Int]] {
    def zero: Value[Int] = Value( intM.zero )
    def append(a1: Value[Int], a2: Value[Int]): Value[Int] = Value( intM.append(a1.v, a2.v) )
  }

  implicit def valueStrMonoid(implicit strM: Monoid[String]): Monoid[Value[String]] = new Monoid[Value[String]] {
    override def zero: Value[String] = Value( strM.zero )
    override def append(a1: Value[String], a2: Value[String]): Value[String] = Value( strM.append(a1.v, a2.v) )
  }
}

case class Value[A](v: A)

class Calculation {
  def sum[A](list: List[A])(implicit m: Monoid[A]): A = list.foldLeft(m.zero)(m.append)
}
