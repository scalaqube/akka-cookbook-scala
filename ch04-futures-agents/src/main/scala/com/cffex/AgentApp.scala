package com.cffex

import akka.agent.Agent
import akka.util.Timeout
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object AgentApp extends App {
  val timeout = Timeout(10 seconds)
  val agent = Agent(5)
  val result = agent.get
  println (s"Result is now $result")
  val f1: Future[Int]  = agent alter 7
  println (s"Result after sending a value ${Await.result(f1, 10 seconds)}")
  val f2: Future[Int]  = agent alter (_ + 3)
  println (s"Result after sending a function ${Await.result(f2, 10 seconds)}")
}

