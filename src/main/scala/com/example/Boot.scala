package com.example

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import com.example.actors.ReportGeneratorActor
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
  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("on-spray-can")

  // create and start our service actor
  val reportGeneratorActor = system.actorOf(Props (new ReportGeneratorActor(cache)), "report-generator-actor")
  val service = system.actorOf(Props(new ReportServiceActor(cache, reportGeneratorActor)), "report-service")

  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)
}
