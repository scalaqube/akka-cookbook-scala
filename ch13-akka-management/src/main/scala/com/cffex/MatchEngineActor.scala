package com.cffex

import akka.actor.{Actor, ActorLogging}
import akka.cluster.Cluster

class MatchEngineActor extends Actor with ActorLogging{
	val cluster = Cluster(context.system)
	
	override def receive: Receive = {
		case message: String =>
			log.info(s"I have been created at ${cluster.selfUniqueAddress}")
	}
	
}
