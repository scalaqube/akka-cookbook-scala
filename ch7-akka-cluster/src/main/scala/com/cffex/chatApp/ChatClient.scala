package com.cffex.chatApp

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.cffex.chatApp.ChatServer.{Connect, Disconnect, Disconnected, Message}

import scala.concurrent.duration._

object ChatClient {
  def props(chatServer: ActorRef) = Props(new ChatClient(chatServer))
}

class ChatClient(chatServer: ActorRef) extends Actor {
  import context.dispatcher

  implicit val timeout = Timeout(5 seconds)

  override def preStart = {
    chatServer ! Connect
  }

  def receive = {
    case Disconnect =>
      (chatServer ? Disconnect).pipeTo(self)
    case Disconnected =>
      context.stop(self)
    case body : String =>
      chatServer ! Message(self, body)
    case msg : Message =>
      println(s"Message from [${msg.author}] at [${msg.creationTimestamp}]: ${msg.body}")
  }
}
