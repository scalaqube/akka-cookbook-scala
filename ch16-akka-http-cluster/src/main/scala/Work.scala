package cep.cluster.poc

import akka.actor.ActorRef


/**
  * Created by WH1409013 on 2017/6/6.
  */

case class Work(workId:String)
case class WorkStatus(workId:String, ref: Option[ActorRef])
case class RunnerStatus(NodeId:String)
case class WorkComplete(NodeId:String, workId:String)
