package com.cffex

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Callback extends App {
  val future = Future(1 + 2).mapTo[Int]
  future onComplete {
    case Success(result) => println(s"result is $result")
    case Failure(fail) => fail.printStackTrace()
  }
  println("Executed before callback")
}
