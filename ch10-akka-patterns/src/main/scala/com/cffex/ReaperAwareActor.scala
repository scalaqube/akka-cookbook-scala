package com.cffex

import akka.actor.Actor
import com.cffex.Reaper.WatchMe

trait ReaperAwareActor extends Actor {
  override final def preStart() = {
    registerReaper()
    preStartPostRegistration()
  }

  private def registerReaper() = {
    context.actorSelection("/user/Reaper") ! WatchMe(self)
  }

  def preStartPostRegistration() : Unit = ()
}
