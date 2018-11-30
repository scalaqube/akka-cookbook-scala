package com.cffex.testprobe

import akka.actor.Status.Success
import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActors, TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._

class TestProbeSpec extends TestKit(ActorSystem("TestProbeSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {


  "MyDoubleEcho actor" must {

    "send back messages to the setting probes" in {
      val probe1 = TestProbe()
      val probe2 = TestProbe()
      val actor = system.actorOf(Props[MyDoubleEcho])
      actor ! ((probe1.ref, probe2.ref))
      actor ! "hello"
      probe1.expectMsg(500 millis, "hello")
      probe2.expectMsg(500 millis, "hello")

      val worker = TestProbe("worker")
      val aggregator = TestProbe("aggregator")

      worker.ref.path.name should startWith("worker")
      aggregator.ref.path.name should startWith("aggregator")


//      implicit val timeout = Timeout(10 seconds)
//
//      val probe = TestProbe()
//      val future = probe.ref ? "hello"
//      probe.expectMsg(10 millis, "hello") // TestActor runs on CallingThreadDispatcher
//      probe.reply("world")
//      assert(future.isCompleted && future.value.contains(Success("world")))
    }

  }
}
