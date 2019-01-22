package com.cffex

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.ClusterDomainEvent
import akka.management.AkkaManagement
import akka.management.cluster.bootstrap.ClusterBootstrap

object ClusterWatcher {
	def props = Props(new ClusterWatcher)
}

class ClusterWatcher extends Actor with ActorLogging {
	
	val cluster = Cluster(context.system)
	
	override def preStart(): Unit = {
		cluster.subscribe(self, classOf[ClusterDomainEvent])
	}
	
	override def receive: Receive = {
		case msg => log.info(s"${cluster.selfAddress} receive >>> $msg")
	}
}

object ClusterExec {
	
	def main(args: Array[String]): Unit = {
		implicit val system = ActorSystem("cluster-bootstrap-ex-system")
		AkkaManagement(system).start()
		ClusterBootstrap(system).start()
		
		system.actorOf(ClusterWatcher.props)
	}
	
}
