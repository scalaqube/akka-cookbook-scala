package com.cffex

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}


class MyActor extends Actor {
  def receive = {
    case msg: String => println(msg)
  }
}

object ActorWithConfig extends App {
  val config: Config = ConfigFactory.load("akka.conf")
  val actorsystem = ActorSystem(config.getString("myactor.actorsystem"))
  val actorName = config.getString("myactor.actorname")
  val actor = actorsystem.actorOf(Props[MyActor], actorName)
  println (actor.path)
}

