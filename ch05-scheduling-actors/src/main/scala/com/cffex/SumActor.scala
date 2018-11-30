package com.cffex

import akka.actor.Actor

class SumActor extends Actor {
  def receive = {
    case (a: Int, b: Int) => sender ! (a + b)
  }
}