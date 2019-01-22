package cep.cluster.poc
import akka.actor.{Actor, ActorLogging, ActorRef, LoggingFSM, Props}
import akka.cluster.pubsub.{DistributedPubSub, DistributedPubSubMediator}
import AutomationRunner.AutomationStates.AutomationRunnerState
import AutomationRunner.AutomationRunData
import akka.cluster.pubsub.DistributedPubSubMediator.SubscribeAck

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

/**
  * Created by WH1409013 on 2017/6/6.
  */

object AutomationRunner {
  def prop : Props = Props(new AutomationRunner)

  case class RunAutomation(workId: String)

  object AutomationStates {
    sealed trait AutomationRunnerState
    case object Idling extends AutomationRunnerState
    case object Running extends AutomationRunnerState
  }
  case class AutomationRunData(workId: String, currentStep: Int, nodeRef: Option[ActorRef])
  case class Processing()
  case class WorkResult(workId: String, result: Any)
}

class AutomationRunner extends Actor with LoggingFSM[AutomationRunnerState, Option[AutomationRunData]]{
  import AutomationRunner._
  import DistributedPubSubMediator.{ Subscribe, SubscribeAck }
  val mediator = DistributedPubSub(context.system).mediator
  mediator ! DistributedPubSubMediator.Subscribe(ClusterNodeRegistry.WorkStatusTopic, self)
  startWith(AutomationStates.Idling, Some(AutomationRunData(workId = "", currentStep = 0, None)))

  override def preStart() : Unit = {
    log.info("AutomationRunner path:" + self.path)
  }
  when (AutomationStates.Idling){
    case Event(r:RunAutomation, Some(d)) =>
      goto(AutomationStates.Running) using(Some(AutomationRunData(workId = r.workId, currentStep = 1, nodeRef = Some(sender()))))
  }

  when (AutomationStates.Running) {
    case Event(Processing, Some(d)) =>
      log.info("AutomationRunner {} is working on : {} step {}", self.path.name, d.workId, d.currentStep)
      Thread.sleep(1000)
      if (d.currentStep == 30) {
        goto(AutomationStates.Idling) using (Some(d))
      } else {
        self ! Processing
        stay using Some(d.copy(currentStep = d.currentStep + 1))
      }
  }

  whenUnhandled {
    case Event(WorkStatus(workId, ref), Some(d)) => {
      if (workId == d.workId) {
        ref.get ! Success("Step " + d.currentStep)
      }
      stay()
    }

    case Event( ack : SubscribeAck, s) => {
      log.info(s"Subscribed to topic $ack")
      stay()
    }
    case Event(WorkComplete(w, workId), Some(d)) =>
      goto(AutomationStates.Idling) using(Some(d))
  }

  onTransition {
    case AutomationStates.Idling -> AutomationStates.Running =>
      self ! Processing
    case AutomationStates.Running -> AutomationStates.Idling =>
      stateData match {
        case Some(AutomationRunData(workId, currentStep, nodeRef)) =>
          nodeRef.get ! WorkComplete("", workId)
        case _ =>
      }
  }
}