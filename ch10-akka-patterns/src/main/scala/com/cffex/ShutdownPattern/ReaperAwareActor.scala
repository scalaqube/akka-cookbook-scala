package com.cffex.ShutdownPattern

import akka.actor.Actor
import com.cffex.ShutdownPattern.Reaper.WatchMe

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
