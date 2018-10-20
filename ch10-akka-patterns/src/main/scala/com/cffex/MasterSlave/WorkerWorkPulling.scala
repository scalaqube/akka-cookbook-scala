package com.cffex.MasterSlave

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.cffex.MasterSlave.MasterWorkPulling.{DeregisterWorker, JoinWorker}

import scala.util.Random

object WorkerWorkPulling {
  trait WorkerState
  case object Idle extends WorkerState
  case object Working extends WorkerState

  case object PullWork
  case class Work(workId: Int, originalSender: ActorRef)
  case class WorkDone(work: Work)
  case class RejectWork(work: Work)
}

class WorkerWorkPulling(master: ActorRef) extends Actor with ActorLogging {
  import WorkerWorkPulling._

  override def preStart() = master ! JoinWorker
  override def postStop() = master ! DeregisterWorker

  def receive = {
    case work : Work =>
      val millis = Random.nextInt(5) * 1000
      log.info(s"Sleeping for [$millis] millis to simulate work.")
      Thread.sleep(millis)
      work.originalSender ! WorkDone(work)
      master ! PullWork
  }
}
