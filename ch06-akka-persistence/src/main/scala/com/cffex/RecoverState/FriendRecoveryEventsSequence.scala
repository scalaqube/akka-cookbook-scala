package com.cffex.RecoverState

import akka.actor.ActorSystem
import akka.persistence.{Recovery, SnapshotSelectionCriteria}

object FriendRecoveryEventsSequence extends App {
  val system = ActorSystem("test")
  val recovery = Recovery(fromSnapshot = SnapshotSelectionCriteria.None, toSequenceNr = 2)
  val hector = system.actorOf(FriendActor.props("Hector", recovery))
  Thread.sleep(2000)
  system.terminate()
}
