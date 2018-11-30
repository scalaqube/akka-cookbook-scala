package com.cffex.RecoverState

import akka.actor.ActorSystem
import akka.persistence.Recovery

object FriendRecoveryDefault extends App {
  val system = ActorSystem("test")
  val hector = system.actorOf(FriendActor.props("Hector",
    Recovery()))
  hector ! "print"
  Thread.sleep(2000)
  system.terminate()
}