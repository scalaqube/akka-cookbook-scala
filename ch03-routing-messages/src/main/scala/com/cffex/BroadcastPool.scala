package com.cffex

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.BroadcastPool

class BroadcastPoolActor extends Actor {
  override def receive = {
    case msg: String => println(s" $msg, I am ${self.path.name}")
    case _ => println(s" I don't understand the message")
  }
}

object BroadcastPoolApp extends App {
  val actorSystem = ActorSystem("Hello-Akka")
  val router = actorSystem.actorOf(BroadcastPool(5).props(Props[BroadcastPoolActor]))
  router !"hello"
  actorSystem.terminate()
}

