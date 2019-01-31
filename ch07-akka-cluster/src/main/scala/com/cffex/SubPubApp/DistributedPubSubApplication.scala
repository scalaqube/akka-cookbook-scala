package com.cffex.SubPubApp

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._
import scala.util.Random

object DistributedPubSubApplication extends App {
  val actorSystem = ActorSystem("ClusterSystem",ConfigFactory.load("application-cluster-1.conf"))
  val cluster = Cluster(actorSystem)

  val notificationSubscriber = actorSystem.actorOf(Props[NotificationSubscriber])
  val notificationPublisher = actorSystem.actorOf(Props[NotificationPublisher])

  val clusterAddress = cluster.selfUniqueAddress
  val notification = Notification(s"Sent from $clusterAddress", "Test!")

  import actorSystem.dispatcher
  actorSystem.scheduler.schedule(Random.nextInt(5) seconds, 5 seconds, notificationPublisher, notification)
}
