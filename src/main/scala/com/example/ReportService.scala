package com.example

import akka.actor.{Actor, ActorRef}
import com.example.actors.messages.TriggerGenerating
import spray.caching.Cache
import spray.http.HttpHeaders.Location
import spray.http.HttpResponse
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.routing._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}


class ReportServiceActor(cache: Cache[String], reportGeneratorActor: ActorRef)
  extends Actor with ReportService {

  def actorRefFactory = context
  def receive = runRoute(reportRoute(cache, reportGeneratorActor))
}

trait ReportService extends HttpService {

  def reportRoute(reportCache: Cache[String],
                  reportGenerateActor: ActorRef): Route =
    (get & pathPrefix("report" / Segment )) {
      case (accountId) =>{
        println("handle")
        parameters("type") { case (reportType) =>
          reportGenerateActor ! TriggerGenerating(accountId, reportType)
          complete {
            new HttpResponse(Accepted, s"get $reportType for $accountId triggered")
              .withHeaders(Location(accountId + reportType))
          }
        }
      }
    } ~ 
      (get & pathPrefix("report" / Segment / Segment)) {
        case(accountId, key) =>{
          getFromCache(reportCache, key)
        }
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