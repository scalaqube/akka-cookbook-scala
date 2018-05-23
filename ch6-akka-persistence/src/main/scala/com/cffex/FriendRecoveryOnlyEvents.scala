package com.cffex

import akka.actor.ActorSystem
import akka.persistence.{Recovery, SnapshotSelectionCriteria}

object FriendRecoveryOnlyEvents extends App {
  val system = ActorSystem("test")
  val recovery = Recovery(fromSnapshot = SnapshotSelectionCriteria.None)
  val hector = system.actorOf(FriendActor.props("Hector", recovery))
  hector ! "print"
  Thread.sleep(2000)
  system.terminate()
}
