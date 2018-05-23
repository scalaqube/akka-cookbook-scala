package com.cffex

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._

class ComputationActor extends Actor {
  def receive = {
    case (a: Int, b: Int) => sender ! (a + b)
  }
}

object FutureWithScala extends App {
  implicit val timeout = Timeout(10 seconds)
  val actorSystem = ActorSystem("Hello-Akka")
  val computationActor = actorSystem.actorOf(Props[ComputationActor])
  val future = (computationActor ? (2, 3)).mapTo[Int]
  val sum = Await.result(future, 10 seconds)
  println (s"Future Result $sum")
  actorSystem.terminate()
}
