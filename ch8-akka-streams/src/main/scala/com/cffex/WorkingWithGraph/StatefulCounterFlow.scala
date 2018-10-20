package com.cffex.WorkingWithGraph

import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import com.cffex.WorkingWithGraph.WorkingWithGraphsApplication.GenericMsg

class StatefulCounterFlow extends  GraphStage[FlowShape[Seq[GenericMsg], Int]] {
  val in: Inlet[Seq[GenericMsg]] = Inlet("IncomingGenericMsg")
  val out: Outlet[Int] = Outlet("OutgoingCount")
  override val shape: FlowShape[Seq[GenericMsg], Int] = FlowShape(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = new GraphStageLogic(shape) {
    var count = 0
    setHandler(in, new InHandler {
      override def onPush() = {
        val elem = grab(in)
        count += elem.size
        push(out, count)
      }
    })
    setHandler(out, new OutHandler {
      override def onPull() = {
        pull(in)
      }
    })
  }
}

