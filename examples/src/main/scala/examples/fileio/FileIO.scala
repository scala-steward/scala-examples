package examples.fileio

import java.io.{File, FileWriter, IOException}
import scala.io.Source

object FileIO extends App {

  def write(msg: String, path: String): Unit =
    try {
      val file = new File(path)
      val fileWriter = new FileWriter(file, true)
      fileWriter.write(s"$msg\n")
      fileWriter.close()
    } catch {
      case e: IOException => println(e.getMessage)
    }

  def read(path: String): Either[Exception, Iterator[String]] =
    try {
      val lines = Source.fromFile(path).getLines()
      Right(lines)
    } catch {
      case e: IOException => Left(new Exception(e.getMessage))
    }

  read("/Users/kodai/scala/scala-examples/examples/src/main/scala/examples/fileio/data/data1.txt") match {
    case Right(a) => a.foreach( v => write(v, "/Users/kodai/scala/scala-examples/examples/src/main/scala/examples/fileio/data/data2.txt"))
    case Left(e)  => println(e)
  }

}

//read("/Users/kodai/scala/scala-examples/akka-examples/src/main/scala/examples/akka/stream/data.txt") match {
//  case Right(a) => a.foreach( v => write(v, "/Users/kodai/scala/scala-examples/akka-examples/src/main/scala/examples/akka/stream/data2.txt"))
//  case Left(e)  => println(e)
//}