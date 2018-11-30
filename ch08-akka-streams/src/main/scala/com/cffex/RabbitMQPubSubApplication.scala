package com.cffex

import akka.stream.alpakka.amqp.{AmqpLocalConnectionProvider, AmqpSinkSettings, ExchangeDeclaration, TemporaryQueueSourceSettings}
import akka.stream.alpakka.amqp.scaladsl.{AmqpSink, AmqpSource}
import akka.stream.scaladsl.Source

object RabbitMQPubSubApplication extends App {
  val exchangeName = "amqp-conn-it-spec-pub-sub-" + System.currentTimeMillis()
  val exchangeDeclaration = ExchangeDeclaration(exchangeName, "fanout")
  val amqpSink = AmqpSink.simple(
    AmqpSinkSettings(AmqpLocalConnectionProvider.getInstance())
      .withExchange(exchangeName)
      .withDeclarations(exchangeDeclaration)
  )
  val fanoutSize = 4

  val mergedSources = (0 until fanoutSize).foldLeft(Source.empty[(Int, String)]) {
    case (source, fanoutBranch) =>
      source.merge(
        AmqpSource
          .atMostOnceSource(
            TemporaryQueueSourceSettings(
              AmqpLocalConnectionProvider.getInstance(),
              exchangeName
            ).withDeclarations(exchangeDeclaration),
            bufferSize = 1
          )
          .map(msg => (fanoutBranch, msg.bytes.utf8String))
      )
  }
}
