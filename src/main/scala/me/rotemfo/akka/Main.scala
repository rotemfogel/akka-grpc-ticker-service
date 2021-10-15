package me.rotemfo.akka

import akka.Done
import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.http.scaladsl.Http
import akka.stream.SystemMaterializer
import ticker.{DefaultTickerServiceClient, TickerService, TickerServiceHandler, TickerSymbol}

import scala.concurrent.Future

object Main extends App {
  // Boot akka
  implicit val system: ActorSystem = ActorSystem("grpc-ticker")
  implicit val mat = SystemMaterializer(system).materializer

  // setup server
  Http()
    .newServerAt("0.0.0.0", 8080)
    .bind(TickerServiceHandler(TickerServiceImpl))

  // Configure the client by code:
  val clientSettings = GrpcClientSettings.connectToServiceAt("127.0.0.1", 8080).withTls(false)

  // Create a client-side stub for the service
  val client: TickerService = DefaultTickerServiceClient(clientSettings)

  val responseStream = client.monitorSymbol(TickerSymbol("AAA"))
  val done: Future[Done] =
    responseStream.runForeach(reply => println(s"${reply.name}:${reply.value}"))

}
