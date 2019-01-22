package cep.cluster.poc
/**
  * Created by WH1409013 on 2017/6/6.
  */
object RegisterNodeProtocol {
  // Messages from Nodes
  case class RegisterNode(NodeId: String)
}
