package com.cffex

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.RoundRobinPool

class RoundRobinPoolActor extends Actor {
  override def receive = {
    case msg: String => println(s" I am ${self.path.name}")
    case _ => println(s" I don't understand the message")
  }
}

object RoundRobinPoolApp extends App {
  val actorSystem = ActorSystem("Hello-Akka")
  val router = actorSystem.actorOf(RoundRobinPool(5).props(Props[RoundRobinPoolActor]))
  for (i <- 1 to 5) {
    router ! s"Hello $i"
  }
}