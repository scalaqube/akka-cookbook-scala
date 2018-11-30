package com.cffex.InteractWithActor

import akka.actor.Actor
import com.cffex.InteractWithActor.SinkActor.{AckSinkActor, InitSinkActor}

object SinkActor {

  case object CompletedSinkActor

  case object AckSinkActor

  case object InitSinkActor

}

class SinkActor extends Actor {
  def receive = {
    case InitSinkActor =>
      println("SinkActor initialized")
      sender ! AckSinkActor
    case something =>
      println(s"Received [$something] in SinkActor")
      sender ! AckSinkActor
  }
}

