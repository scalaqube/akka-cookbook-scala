package com.cffex

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.ScatterGatherFirstCompletedPool
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.duration._

class ScatterGatherFirstCompletedPoolActor extends Actor {
  override def receive = {
    case msg: String => sender ! "I say hello back to you"
    case _ => println(s" I don't understand the message")
  }
}

object ScatterGatherFirstCompletedPoolApp extends App {
  implicit val timeout = Timeout(10 seconds)
  val actorSystem = ActorSystem("Hello-Akka")
  val router = actorSystem.actorOf(ScatterGatherFirstCompletedPool(5, within = 10.seconds).props(Props[ScatterGatherFirstCompletedPoolActor]))
  println (Await.result((router ? "hello").mapTo[String], 10 seconds))
  actorSystem.terminate()
}
