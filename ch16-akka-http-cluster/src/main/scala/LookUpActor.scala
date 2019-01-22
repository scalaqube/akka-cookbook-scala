package cep.cluster.poc
import akka.actor.{Actor, ActorIdentity, ActorLogging, ActorRef, Identify, Props, RootActorPath}
import akka.cluster.Member

import scala.util.{Failure, Success}

/**
  * Created by WH1409013 on 2017/6/14.
  */
object LookUpActor {
  def prop(nodeId: String, members: List[Member], webRef:ActorRef): Props = Props(new LookUpActor(nodeId, members, webRef))
  case class LookMeUp()
}

class LookUpActor(nodeId: String, members: List[Member], webRef:ActorRef) extends Actor with ActorLogging{
  import LookUpActor._
  var failures = 0
  def receive: Receive = {
    case ActorIdentity(`nodeId`, Some(ref)) =>
      webRef ! Success(ref.path)
      context.stop(self)
    case ActorIdentity(`nodeId`, None) =>
      failures = failures + 1
      if (failures >= members.size) {
        webRef ! Failure
        context.stop(self)
      }
    case LookMeUp() =>
      members.foreach((m:Member)=>
        context.actorSelection(RootActorPath(m.address)+"/user/"+nodeId) ! Identify(nodeId))
  }
}
