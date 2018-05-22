package com.cffex

import akka.actor.{Actor, ActorSystem, Props}


class SummingActor extends Actor {
  // state inside the actor
  var sum = 0
  // behaviour which is applied on the state
  override def receive: Receive = {
    // receives message an integer
    case x: Int => sum = sum + x
      println(s"my state as sum is $sum")
      // receives default message
      case _ => println("I don't know what are you talking about")
  }
}

class SummingActorWithConstructor(intitalSum: Int) extends Actor {
  // state inside the actor
  var sum = 0
  // behaviour which is applied on the state
  override def receive: Receive = {
    // receives message an integer
    case x: Int => sum = intitalSum + sum + x
      println(s"my state as sum is $sum")
    // receives default message
    case _ => println("I don't know what are you talking about")
  }
}

object BehaviourAndState extends App {
  val actorSystem = ActorSystem("HelloAkka")

  // creating an actor inside the actor system
  val actor = actorSystem.actorOf(Props[SummingActor], "summingactor")
  val actor1 = actorSystem.actorOf(Props(classOf[SummingActorWithConstructor], 10), "summingactor1")


  while (true)
  {
    Thread.sleep(3000)
    actor ! 1
    actor1 ! 1
  }
}
