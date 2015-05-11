package com.example

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._

class ReportServiceActor extends Actor with ReportService {

  def actorRefFactory = context

  def receive = runRoute(reportRoute)
}

trait ReportService extends HttpService {
  def reportRoute : Route = ???
}