package com.cffex.SamplePersistence

import akka.actor.{ActorSystem, Props}
import com.cffex.{Add, Remove, UserUpdate}

object SamplePersistenceApp extends App {
  val system = ActorSystem("example")
  val persistentActor1 = system.actorOf(Props[SamplePersistenceActor])
  persistentActor1 ! UserUpdate("foo", Add)
  persistentActor1 ! UserUpdate("baz", Add)
  persistentActor1 ! "snap"
  persistentActor1 ! "print"
  persistentActor1 ! UserUpdate("baz", Remove)
  persistentActor1 ! "print"
  Thread.sleep(2000)
  system.stop(persistentActor1)
  val persistentActor2 = system.actorOf(Props[SamplePersistenceActor])
  persistentActor2 ! "print"
  Thread.sleep(2000)
  system.terminate()
}
