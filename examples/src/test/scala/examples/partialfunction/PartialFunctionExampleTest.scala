package examples.partialfunction

import org.scalatest.flatspec.AnyFlatSpec

class PartialFunctionExampleTest extends AnyFlatSpec {

  "partial function" should "work as expected for Bullet Train E5 type." in {
    val ti: TrainInformation = new TrainInformation
    val res: TheBulletTrainBaseInfo = ti.getInformation(E5)
    val expected: TheBulletTrainBaseInfo = TheBulletTrainBaseInfo( List("Tohoku", "Hokkaido"), 320 )
    assert( res == expected )

  }
}