package com.cffex

import akka.actor.ActorSystem
import akka.persistence.{Recovery, SnapshotSelectionCriteria}

object FriendApp extends App {
  val system = ActorSystem("test")
  val hector = system.actorOf(FriendActor.props("Hector", Recovery()))
  hector ! AddFriend(Friend("Laura"))
  hector ! AddFriend(Friend("Nancy"))
  hector ! AddFriend(Friend("Oliver"))
  hector ! AddFriend(Friend("Steve"))
  hector ! "snap"
  hector ! RemoveFriend(Friend("Oliver"))
  hector ! "print"
  Thread.sleep(2000)
  system.terminate()
}
