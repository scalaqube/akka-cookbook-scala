package com.cffex.SimpleStream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

object SimpleStreamsApplication extends App {
  implicit val actorSystem = ActorSystem("SimpleStream")
  implicit val actorMaterializer = ActorMaterializer()
  val fileList = List(
    "src/main/resources/textfile1.txt",
    "src/main/resources/textfile2.txt",
    "src/main/resources/textfile3.txt"
  )
  val stream = Source(fileList)
    .map(new java.io.File(_))
    .filter(_.exists())
    .filter(_.length() != 0)
    .to(Sink.foreach(f => println(s"Absolute path: ${f.getAbsolutePath}")))
  stream.run()
}

