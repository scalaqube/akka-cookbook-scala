package com.cffex

import akka.actor.Actor

class SimpleActor extends Actor {

  def receive = {
    case _ =>
      println(s"I have been created at ${self.path.address.hostPort}")
  }
}
