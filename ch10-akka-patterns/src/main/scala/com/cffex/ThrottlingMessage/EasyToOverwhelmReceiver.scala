package com.cffex.ThrottlingMessage

import akka.actor.{Actor, ActorLogging}

class EasyToOverwhelmReceiver extends Actor with ActorLogging {

  def receive = {
    case x => log.info(s"Received $x")
  }
}
