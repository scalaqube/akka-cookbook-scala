package com.cffex.hello.app

import akka.actor.{ActorSystem, Props}
import com.cffex.hello.actor.HelloWorldActor
import com.cffex.hello.model.HelloWorld

object HelloWorldActorApp extends App{

       val actorSystem = ActorSystem("helloworld")

       val actor = actorSystem.actorOf(Props[HelloWorldActor], "HelloWorldActor")

       actor ! HelloWorld

       actorSystem.terminate()
}
