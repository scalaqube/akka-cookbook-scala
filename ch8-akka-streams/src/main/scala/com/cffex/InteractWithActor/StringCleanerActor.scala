package com.cffex.InteractWithActor

import akka.actor.Actor

class StringCleanerActor extends Actor {
  def receive = {
    case s: String =>
      println(s"Cleaning [$s] in StringCleaner")
      sender ! s.replaceAll("""[!|#|$|?|{|}|*]""", "")
        .replaceAll(System.lineSeparator(), "")
  }
}

