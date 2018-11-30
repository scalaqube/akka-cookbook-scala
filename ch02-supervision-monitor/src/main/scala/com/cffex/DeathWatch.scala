package com.cffex

import akka.actor.{Actor, ActorSystem, Props, Terminated}

case object Service

case object Kill

class ServiceActor extends Actor {
  def receive = {
    case Service => println("I provide a special service")
  }
}

class DeathWatchActor extends Actor {
  val child = context.actorOf(Props[ServiceActor], "serviceActor")
  context.watch(child)

  def receive = {
    case Service => child ! Service
    case Kill => context.stop(child)
    case Terminated(`child`) => println("The service actor has terminated and no longer available")
  }
}

object DeathWatchApp extends App {
  val actorSystem = ActorSystem("Supervision")
  val deathWatchActor = actorSystem.actorOf(Props[DeathWatchActor])
  deathWatchActor ! Service
  deathWatchActor ! Service
  Thread.sleep(1000)
  deathWatchActor ! Kill
  deathWatchActor ! Service
}

