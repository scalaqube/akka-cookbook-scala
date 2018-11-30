package com.cffex.Cassandra

case class ValueUpdate(newValue: Double)

case class StockValue(value: Double, timestamp: Long = System.currentTimeMillis())

case class ValueAppended(stockValue: StockValue)

case class StockHistory(values: Vector[StockValue] = Vector.empty[StockValue]) {
  def update(evt: ValueAppended) = copy(values :+ evt.stockValue)

  override def toString = s"$values"
}