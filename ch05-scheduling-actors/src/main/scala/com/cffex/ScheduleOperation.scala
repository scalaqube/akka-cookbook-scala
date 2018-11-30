package com.cffex

import akka.actor.ActorSystem
import scala.concurrent.duration._

object ScheduleOperation extends App {
  val system = ActorSystem("Hello-Akka")

  import system.dispatcher

  system.scheduler.scheduleOnce(10 seconds) {
    println(s"Sum of (1 + 2) is ${1 + 2}")
  }
  system.scheduler.schedule(11 seconds, 2 seconds) {
    println(s"Hello, Sorry for disturbing you every 2 seconds")
  }
}