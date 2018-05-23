package com.cffex

import akka.actor.ActorSystem
import akka.persistence.{Recovery, SnapshotSelectionCriteria}

object FriendRecoveryEventsReplay extends App {
  val system = ActorSystem("test")
  val recovery = Recovery(fromSnapshot = SnapshotSelectionCriteria.None, replayMax = 3)
  val hector = system.actorOf(FriendActor.props("Hector", recovery))
  Thread.sleep(2000)
  system.terminate()
}
