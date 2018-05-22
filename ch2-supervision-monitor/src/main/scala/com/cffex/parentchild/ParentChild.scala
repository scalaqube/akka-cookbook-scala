package com.cffex.parentchild

import akka.actor.{Actor, ActorSystem, Props}

case object CreateChild

case class Greet(msg: String)

class ChildActor extends Actor {
  def receive = {
    case Greet(msg) => println(s"My parent[${self.path.parent}] greeted to me [${self.path}] $msg")
  }
}

class ParentActor extends Actor {
  def receive = {
    case CreateChild =>
      val child = context.actorOf(Props[ChildActor], "child")
      child ! Greet("Hello Child")
  }
}


object ParentChild extends App {
  val actorSystem = ActorSystem("Supervision")
  val parent = actorSystem.actorOf(Props[ParentActor], "parent")
  parent ! CreateChild
}

