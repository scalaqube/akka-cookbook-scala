package com.cffex.testprobe


import akka.actor._
class MyDoubleEcho extends Actor {
  var dest1: ActorRef = _
  var dest2: ActorRef = _
  def receive = {
    case (d1: ActorRef, d2: ActorRef) ⇒
      dest1 = d1
      dest2 = d2
    case x ⇒
      dest1 ! x
      dest2 ! x
  }
}
