package com.cffex

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}

object Fib {
  def fib(n: Int): Int = {
    def fib_tail(n: Int, a: Int, b: Int): Int = n match {
      case 0 => a
      case _ => fib_tail(n - 1, b, a + b)
    }

    fib_tail(n, 0, 1)
  }
}

object Parallel extends App {

  import Fib._

  val t1 = System.currentTimeMillis
  val sum = fib(1000) + fib(1000) + fib(1000)
  println(s"sum is $sum time taken in sequential computation  ${(System.currentTimeMillis - t1) / 1000.0}")
  val t2 = System.currentTimeMillis
  val future1 = Future(fib(1000))
  val future2 = Future(fib(1000))
  val future3 = Future(fib(1000))
  val future = for {
    x <- future1
    y <- future2
    z <- future3
  } yield (x + y + z)
  future onSuccess {
    case sum =>
      val endTime = ((System.currentTimeMillis - t2) / 1000.0)
      println(s"sum is $sum time taken in parallel computation $endTime seconds")
  }
  Thread.sleep(5000)
}
