package examples.partialfunction

// PartialFunction[-A, +B] extends (A) => B
// Aの値に応じて、Bの値を返す

case class TheBulletTrainBaseInfo(trainArea: List[String], maxSpeed: Int)
sealed trait BulletTrainType
case object E5 extends BulletTrainType {
  def baseInfo: TheBulletTrainBaseInfo = TheBulletTrainBaseInfo( List("Tohoku", "Hokkaido"), 320 )
}
case object E7 extends BulletTrainType {
  def baseInfo: TheBulletTrainBaseInfo = TheBulletTrainBaseInfo( List("Hokuriku", "Joetsu"), 260 )
}
class TrainInformation {
  def getInformation: PartialFunction[BulletTrainType, TheBulletTrainBaseInfo] = {
    case E5 => E5.baseInfo
    case E7 => E7.baseInfo
  }
}
