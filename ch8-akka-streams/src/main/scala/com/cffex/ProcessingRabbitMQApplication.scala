package com.cffex

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.amqp._
import akka.stream.alpakka.amqp.scaladsl.{AmqpSink, AmqpSource}
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString
import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.{Failure, Success}

object ProcessingRabbitMQApplication extends App {
  implicit val actorSystem = ActorSystem("SimpleStream")
  implicit val actorMaterializer = ActorMaterializer()

  val queueName = "amqp-conn-it-spec-simple-queue-" + System.currentTimeMillis()
  val queueDeclaration = QueueDeclaration(queueName)
  val amqpSink = AmqpSink.simple(
    AmqpSinkSettings(AmqpLocalConnectionProvider.getInstance())
      .withRoutingKey(queueName)
      .withDeclarations(queueDeclaration)
  )
  val input = Vector("one", "two", "three", "four", "five")
  Source(input).map(s => ByteString(s)).runWith(amqpSink)

  val amqpSource = AmqpSource.atMostOnceSource(
    NamedQueueSourceSettings(AmqpLocalConnectionProvider.getInstance(), queueName).withDeclarations(queueDeclaration),
    bufferSize = 10
  )

  val result = amqpSource.take(input.size).runWith(Sink.seq)
  result.onComplete{
    case Success(data)=>println (s"$data")
    case Failure(fail)=> println(s"$fail")
  }


//  val consumerQueueName = "akka_streams_consumer_queue"
//  val consumerQueueDeclaration = QueueDeclaration(consumerQueueName)
//  val sourceDeclarations = Seq(consumerQueueDeclaration)
//  val exchangeName = "akka_streams_exchange"
//  val exchangeDeclaration = ExchangeDeclaration(exchangeName, "direct")
//  val destinationQueueName = "akka_streams_destination_queue"
//  val destinationQueueDeclaration = QueueDeclaration(destinationQueueName)
//  val bindingDeclaration = BindingDeclaration(destinationQueueName, exchangeName)
//  val sinkDeclarations = Seq(exchangeDeclaration, destinationQueueDeclaration, bindingDeclaration)
//  val credentials = AmqpCredentials("guest", "guest")
//  val connectionSetting = AmqpConnectionDetails("127.0.0.1", 5672, Some(credentials))
//  val amqpSourceConfig = NamedQueueSourceSettings(connectionSetting, consumerQueueName, sourceDeclarations)
//  val rabbitMQSource = AmqpSource(amqpSourceConfig, 1000)
//  val amqpSinkConfig = AmqpSinkSettings(connectionSetting, Some(exchangeName), None, sinkDeclarations)
//  val rabbitMQSink = AmqpSink(amqpSinkConfig)
//  val stream = rabbitMQSource.map(incomingMessage => {
//    val upperCased = incomingMessage.bytes.utf8String.toUpperCase
//    OutgoingMessage(bytes = ByteString(upperCased), immediate = false, mandatory = false, props = None)
//  }).to(rabbitMQSink)
//  stream.run()
}


