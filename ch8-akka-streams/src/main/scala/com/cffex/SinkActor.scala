package com.cffex

import akka.actor.Actor
import com.cffex.SinkActor.{AckSinkActor, InitSinkActor}

object SinkActor {
  case object CompletedSinkActor
  case  object AckSinkActor
  case  object InitSinkActor
}

class SinkActor extends Actor {
  def receive = {
    case InitSinkActor =>
      println("SinkActor initialized")
      sender !AckSinkActor
    case something =>
      println(s"Received [$something] in SinkActor")
      sender !AckSinkActor
  }
}

