package com.cffex

import akka.persistence.journal.WriteEventAdapter
import akka.persistence.journal.Tagged

class MyTaggingEventAdapter(tags: Set[String]) extends WriteEventAdapter {
  override def toJournal(event: Any): Any = Tagged(event, tags)
  override def manifest(event: Any): String = ""
}