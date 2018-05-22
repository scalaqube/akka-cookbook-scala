package com.cffex

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.RandomPool

class RandomPoolActor extends Actor {
  override def receive = {
    case msg: String => println(s" I am ${self.path.name}")
    case _ => println(s" I don't understand the message")
  }
}

object RandomPoolApp extends App {
  val actorSystem = ActorSystem("Hello-Akka")
  val router = actorSystem.actorOf(RandomPool(5).props(Props[RandomPoolActor]))
  for (i <- 1 to 5) {
    router ! s"Hello $i"
  }
}

