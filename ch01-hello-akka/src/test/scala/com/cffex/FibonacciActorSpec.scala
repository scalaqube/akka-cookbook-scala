package com.cffex

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class FibonacciActorSpec extends TestKit(ActorSystem("FibonacciActorSpec"))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll
  with Matchers {

  private val fibonacciActor: ActorRef=system.actorOf(Props[FibonacciActor])

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "FibonacciActor" must {

    "send back 1 at 1" in {

      fibonacciActor ! 1
      expectMsg(1)

    }

    "send back 3 at 2" in {

      fibonacciActor ! 2
      expectMsg(1)

    }

    "send back 55 at 10" in {

      fibonacciActor ! 10
      expectMsg(55)

    }

  }

//  override def beforeAll: Unit ={
//    fibonacciActor = system.actorOf(Props[FibonacciActor])
//  }

}
