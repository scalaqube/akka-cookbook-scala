import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}
import scala.concurrent.duration._

/**
  * Created by WH1409013 on 2017/6/12.
  */

object DistributedALESpec {
  val clusterConfig = ConfigFactory.parseString("""
    |akka {
    |  actor.provider = "akka.cluster.ClusterActorRefProvider"
    |  remote.netty.tcp.port=0
    |  cluster.metrics.enabled=off
    |}
  """.stripMargin)
}
class DistributedALESpec (_system: ActorSystem)
  extends TestKit(_system)
    with Matchers
    with FlatSpecLike
    with BeforeAndAfterAll
    with ImplicitSender {
  import DistributedALESpec._
  val workTimeout = 3.seconds

  def this() = this(ActorSystem("DistributedALESpec", DistributedALESpec.clusterConfig))

  val backendSystem: ActorSystem = {
    val config = ConfigFactory.parseString("akka.cluster.roles=[backend]").withFallback(clusterConfig)
    ActorSystem("DistributedALESpec", config)
  }
  "Distributed ALEs" should "perform work and publish results" in {

  }
}
