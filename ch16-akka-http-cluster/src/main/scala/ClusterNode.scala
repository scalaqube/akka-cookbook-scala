package cep.cluster.poc

import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.cluster.client.ClusterClient.SendToAll
import cep.cluster.poc.AutomationRunner.RunAutomation
import cep.cluster.poc.RegisterNodeProtocol.RegisterNode
import com.google.common.util.concurrent.AbstractScheduledService.Scheduler
import scala.concurrent.duration._
/**
  * Created by WH1409013 on 2017/6/9.
  */

object ClusterNode {

  def props(registryProxy: ActorRef, registerInterval: FiniteDuration = 10.seconds): Props =
    Props(classOf[ClusterNode], registryProxy, registerInterval)

  case class WorkComplete(result: Any)
}
class ClusterNode(registryProxy: ActorRef, registerInterval: FiniteDuration) extends Actor with ActorLogging {
  import context.dispatcher
  val registerTask = context.system.scheduler.schedule(0.seconds, registerInterval, registryProxy,
    RegisterNode(self.path.name))

  override def preStart() : Unit = {
    log.info("ClusterNode path:" + self.path)
  }
  override def receive: Receive = {
    case Work(workId) =>
      val runerId = scala.util.Random.alphanumeric.take(8).mkString
      val aRunner = context.system.actorOf(AutomationRunner.prop, runerId)
      aRunner ! RunAutomation(workId)
    case WorkComplete(_, workId) =>
      registryProxy ! WorkComplete(self.path.name, workId)
      log.info("work {} is complete on Node {}", workId, self.path.name)
  }
}
