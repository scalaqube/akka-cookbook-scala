package com.cffex

import akka.persistence.PersistentActor
import com.cffex.RestartableActor.{RestartActor, RestartActorException}


trait RestartableActor extends PersistentActor {
	
	abstract override def receiveCommand = super.receiveCommand orElse {
		case RestartActor => throw RestartActorException
	}
}

object RestartableActor {
	case object RestartActor
	
	private object RestartActorException extends Exception
}
