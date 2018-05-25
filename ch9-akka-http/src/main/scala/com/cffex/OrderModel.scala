package com.cffex

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

case class Item(id: Int, quantity: Int, unitPrice: Double, percentageDiscount: Option[Double])

case class Order(id: String, timestamp: Long, items: List[Item], deliveryPrice: Double, metadata: Map[String, String])

case class GrandTotal(id: String, amount: Double)

trait OrderJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val itemFormat = jsonFormat4(Item)
  implicit val orderFormat = jsonFormat5(Order)
  implicit val grandTotalFormat = jsonFormat2(GrandTotal)
}

