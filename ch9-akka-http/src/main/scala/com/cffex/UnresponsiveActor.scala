package com.cffex

import akka.actor.Actor

class UnresponsiveActor extends Actor {
  def receive = Actor.ignoringBehavior
}

