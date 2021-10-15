package me.rotemfo.akka

import akka.NotUsed
import akka.stream.scaladsl.Source
import ticker.{StockValue, TickerService, TickerSymbol}

import scala.util.Random

object TickerServiceImpl extends TickerService {
  /**
   * Monitor a symbol
   */
  override def monitorSymbol(in: TickerSymbol): Source[StockValue, NotUsed] = {
    Source.fromIterator(() => new Iterator[StockValue] {
      override def hasNext: Boolean = true
      override def next(): StockValue = StockValue(in.name, Random.nextInt(800))
    })
  }
}
