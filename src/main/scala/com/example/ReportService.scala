package com.example

import akka.actor.{ActorRef, Actor}
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

  def receive = runRoute(reportRoute())
}

trait ReportService extends HttpService {
  def reportRoute() : Route =
    (get & pathPrefix("report" / Segment )) {
      case(accountId) =>
          complete(OK)
        
    } ~
      (get & pathPrefix("report" / Segment / Segment)) {
        case(accountId, key) =>
          complete(OK)
    }


  
  
  
  
  
  
  
  
  
  
  
  
  def getFromCache(cache: Cache[String], key: String) = {
    respondWithMediaType(`application/json`) {
      cache.get(key).map { x =>
        val rez = Await.result(x, Duration.Inf)
        complete(OK, rez)
      }.getOrElse {
        respondWithHeader(Location(key)) {
          complete(Accepted, "{}")
        }
      }
    }
  }
}