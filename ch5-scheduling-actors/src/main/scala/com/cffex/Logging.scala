package com.cffex

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

class LoggingActor extends Actor with ActorLogging {
  def receive = {
    case (a: Int, b: Int) => {
      log.info(s"sum of $a and $b is ${a + b}")
    }
    case msg => log.warning(s"i don't know what are you talking about : msg")
  }
}

object Logging extends App {
  val system = ActorSystem("hello-Akka")
  val actor = system.actorOf(Props[LoggingActor], "SumActor")
  actor ! (10, 12)
  actor !"Hello !!"
  system.terminate()
}


