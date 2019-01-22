package sample.cluster.stats

import akka.actor.{Actor, ActorLogging}
import akka.cluster.Cluster

//#worker
class StatsWorker extends Actor with ActorLogging{
  
  val cluster = Cluster(context.system)
  
  var cache = Map.empty[String, Int]
  def receive = {
    case word: String =>
      log.info(s"I have been created at ${cluster.selfUniqueAddress}")
      val length = cache.get(word) match {
        case Some(x) => x
        case None =>
          val x = word.length
          cache += (word -> x)
          x
      }

      sender() ! length
  }
}
//#worker