package com.example

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.caching.{Cache, LruCache}
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

object Boot extends App {


  val cache: Cache[String] =
    LruCache(
      maxCapacity = 10,
      initialCapacity = 5,
      timeToLive = Duration(2, TimeUnit.DAYS),
      timeToIdle = Duration(1, TimeUnit.DAYS)
    )


  implicit val system = ActorSystem("on-spray-can")
  val service = system.actorOf(Props(new ReportServiceActor()), "report-service")

  implicit val timeout = Timeout(5.seconds)
  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)
}
