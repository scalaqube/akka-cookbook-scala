package com.cffex

import akka.actor.{Cancellable, Props, ActorSystem, Actor}
import scala.concurrent.duration._

class CancelOperationActor extends Actor {
  var i = 10

  def receive = {
    case "tick" => {
      println(s"Hi, Do you know i do the same task again and again")
      i = i - 1
      if (i == 0) CancelOperationApp.cancellable.cancel()
    }
  }
}

object CancelOperationApp extends App {
  val system = ActorSystem("Hello-Akka")
  import system.dispatcher
  val actor = system.actorOf(Props[CancelOperationActor])
  val cancellable: Cancellable  = system.scheduler.schedule(0 seconds, 2 seconds, actor, "tick")
}