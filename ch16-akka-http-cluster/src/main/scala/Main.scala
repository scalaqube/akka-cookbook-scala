package cep.cluster.poc

import java.util.UUID

import akka.actor.{ActorSystem, PoisonPill}
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.pattern._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._
import scala.io.StdIn
import scala.util.{Failure, Success}

/**
  * Created by WH1409013 on 2017/6/6.
  */
object Main {

  def main(args: Array[String]): Unit = {
//    val port = args(0).toInt
    val port = 8080
    startALEInstance(port)
    startWebServer(port + 10)
  }

  def startALEInstance(port: Int): Unit = {
    val conf = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).
      withFallback(ConfigFactory.load())
    val system = ActorSystem("automation-akka-http", conf)
    system.actorOf(
      ClusterSingletonManager.props(
        ClusterNodeRegistry.props(workTimeout),
        PoisonPill,
        ClusterSingletonManagerSettings(system)
      ),
      "registry")

    val registryProxy = system.actorOf(
      ClusterSingletonProxy.props(
        settings = ClusterSingletonProxySettings(system),
        singletonManagerPath = "/user/registry"
      ),
      name = "registryProxy")
    system.actorOf(ClusterNode.props(registryProxy), UUID.randomUUID().toString)
  }

  def workTimeout = 10.seconds

  def startWebServer(port: Int): Unit = {
    implicit val system = ActorSystem("automation-akka-http")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val registryProxy = system.actorOf(
      ClusterSingletonProxy.props(
        settings = ClusterSingletonProxySettings(system),
        singletonManagerPath = "/user/registry"
      ),
      name = "registryProxy")

    val route =
      get {
        path("send") {
          implicit val timeout = Timeout(5.seconds)
          onComplete(registryProxy ? Work) {
            case Success(res) => complete(StatusCodes.Accepted, s"workID:$res")
            case Failure(t) => complete(StatusCodes.InternalServerError, t.getMessage)
          }
        }
      } ~
        get {
          pathPrefix("query" / JavaUUID) { workId =>
            implicit val timeout = Timeout(5.seconds)
            onComplete(registryProxy ? WorkStatus(workId.toString, None)) {
              case Success(res) => complete(StatusCodes.Accepted, s"workID:$res")
              case Failure(t) => complete(StatusCodes.InternalServerError, t.getMessage)
            }
          }
        } ~
        get {
          pathPrefix("runner" / JavaUUID) { nodeId =>
            implicit val timeout = Timeout(5.seconds)
            onComplete(registryProxy ? RunnerStatus(nodeId.toString)) {
              case Success(res) => complete(StatusCodes.Accepted, s"workID:$res")
              case Failure(t) => complete(StatusCodes.InternalServerError, t.getMessage)
            }
          }
        }

    val bindingFuture = Http().bindAndHandle(route, "localhost", port)

    println(s"Server online at http://localhost:$port/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
