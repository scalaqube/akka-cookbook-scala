package com.cffex

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.HttpApp
import akka.http.scaladsl.settings.ServerSettings
import com.typesafe.config.ConfigFactory
import scala.util.Random._

object OrderCalculatorJsonServer extends HttpApp with OrderJsonSupport {
  val routes = path("calculateGrandTotal" ~ Slash.?) {
    post {
      entity(as[Order]) { order =>
        complete {
          calculateGrandTotal(order)
        }
      }
    }
  } ~ path("randomOrder") {
    get {
      complete {
        generateRandomOrder()
      }
    }
  }

  private def calculateGrandTotal(o: Order) = {
    val amount = o.items.map(i =>i.percentageDiscount.getOrElse(1.0d) * i.unitPrice * i.quantity).sum + o.deliveryPrice
    GrandTotal(o.id, amount)
  }

  private def generateRandomOrder(): Order = {
    val items = (0 to nextInt(5)).map(i => {
      Item(i, nextInt(100), 100 * nextDouble(), if (nextBoolean()) Some(nextDouble()) else None)
    }).toList
    Order(nextString(4), System.currentTimeMillis(), items, 100 * nextDouble(), Map("notes" -> "random"))
  }
}

object OrderCalculatorJsonServerApplication extends App {
  OrderCalculatorJsonServer.startServer("0.0.0.0", 8088, ServerSettings(ConfigFactory.load))
}

