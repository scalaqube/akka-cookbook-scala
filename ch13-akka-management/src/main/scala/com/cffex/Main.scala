package com.cffex

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.cluster.Cluster
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings}
import akka.management.AkkaManagement
import akka.management.cluster.bootstrap.ClusterBootstrap
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._

object Node1 extends App {
	new Main(1).postMsg("hello from node2")

}

object Node2 extends App {
	new Main(2).postMsg("hello from node2")
}

object Node3 extends App {
	new Main(3).postMsg("what's up!")
}

class Main(nr: Int) {
	
	val config = ConfigFactory.parseString(s"""
      akka.remote.artery.canonical.hostname = "127.0.0.$nr"
      akka.management.http.hostname = "127.0.0.$nr"
    """).withFallback(ConfigFactory.load("application.conf"))
	val system = ActorSystem("local-cluster", config)
	
	AkkaManagement(system).start()
	
	ClusterBootstrap(system).start()
	
	Cluster(system).registerOnMemberUp({
		system.log.info("Cluster is up!")
	})
	
	val clusterSingletonSettings = ClusterSingletonManagerSettings(system)
	val clusterSingletonManager = ClusterSingletonManager.props(Props[MatchEngineActor], PoisonPill, clusterSingletonSettings)
	
	system.actorOf(clusterSingletonManager, "matchEngineMgr")
	val matchEngineMgr = system.actorOf(ClusterSingletonProxy.props(
		singletonManagerPath = "/user/matchEngineMgr",
		settings = ClusterSingletonProxySettings(system)),
		name = "matchEngineMgrProxy")
	
	
	
	import system.dispatcher
	system.scheduler.schedule(10 seconds, 5 seconds, matchEngineMgr, "TEST")
	def postMsg(msg:String)={
		matchEngineMgr! msg
	}
}
