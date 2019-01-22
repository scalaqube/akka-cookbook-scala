package cep.cluster.poc

import java.util.UUID

import akka.actor.{Actor, ActorIdentity, ActorLogging, ActorRef, Props}
import akka.cluster.{Cluster, Member}
import akka.cluster.pubsub.{DistributedPubSub, DistributedPubSubMediator}


import scala.collection.immutable.SortedSet
import scala.concurrent.duration.{Deadline, FiniteDuration}
import scala.util.Success

/**
  * Created by WH1409013 on 2017/6/6.
  */
object ClusterNodeRegistry {
  val WorkStatusTopic = "WorkStatus"

  def props(workTimeout: FiniteDuration): Props =
    Props(classOf[ClusterNodeRegistry], workTimeout)

  private sealed trait WorkerStatus

  private case class Busy(workId: String, deadline: Deadline) extends WorkerStatus

  private case class WorkerState(ref: ActorRef, status: WorkerStatus)

  private case object Idle extends WorkerStatus

}

class ClusterNodeRegistry(workTimeout: FiniteDuration) extends Actor with ActorLogging {

  import ClusterNodeRegistry._
  import LookUpActor._

  val mediator = DistributedPubSub(context.system).mediator
  private var nodes = Map[String, WorkerState]()
  private var workLoads = Map[String, Int]()

  override def receive: Receive = {
    case RegisterNodeProtocol.RegisterNode(nodeId) =>
      if (nodes.contains(nodeId)) {
        nodes += (nodeId -> nodes(nodeId).copy(ref = sender()))
      } else {
        nodes += (nodeId -> WorkerState(sender(), status = Idle))
        workLoads += (nodeId -> 0)
        log.info("Worker registered: {}", nodeId)
      }
    case Work =>
      val minWorkLoad = workLoads.minBy(_._2)
      val workLst = List.fill(10)(UUID.randomUUID().toString)
      for (workId <- workLst) {
        nodes(minWorkLoad._1).ref ! Work(workId)
      }

      workLoads += minWorkLoad.copy(_2 = (minWorkLoad._2 + workLst.length))
      sender() ! Success(workLst.toString())
    case WorkStatus(workId, s) =>
      mediator ! DistributedPubSubMediator.Publish(WorkStatusTopic, WorkStatus(workId, Some(sender())))
    case WorkComplete(nodeId, workId) =>
      val load = workLoads(nodeId)
      workLoads += (nodeId -> (load - 1))
    case RunnerStatus(nodeId) =>
      val cluster = Cluster(context.system);
      val members = cluster.state.members.toList;
      val looker = context.system.actorOf(LookUpActor.prop(nodeId, members, sender()))
      looker ! LookMeUp()
  }
}
