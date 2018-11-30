package com.cffex.hello.actor

import akka.actor.{Actor, ActorLogging}
import com.cffex.hello.model.HelloWorld

class HelloWorldActor extends Actor with ActorLogging{

  override def receive: PartialFunction[Any, Unit] = {
    case HelloWorld => log.info("Hello World")
    case _          => log.info("Unknown World")
  }

}
