package com.example

import akka.actor.{ActorRef, Actor}
import com.example.actors.TriggerGenerating
import spray.caching.Cache
import spray.http.HttpHeaders.Location
import spray.routing._
import spray.http._
import MediaTypes._
import spray.http.StatusCodes._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ReportServiceActor() extends Actor with ReportService {

  def actorRefFactory = context

  def receive = runRoute(reportRoute)
}

trait ReportService extends HttpService {
  def reportRoute : Route = ???
}