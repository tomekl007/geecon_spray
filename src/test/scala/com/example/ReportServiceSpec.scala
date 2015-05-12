package com.example

import java.util.concurrent.TimeUnit

import akka.testkit.TestProbe
import org.specs2.mutable.Specification
import spray.caching.{Cache, LruCache}
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._

import scala.concurrent.duration.Duration

class ReportServiceSpec extends Specification with Specs2RouteTest with ReportService {
  def actorRefFactory = system
  
  "ReportService" should {

    "" in {
      Get() ~> reportRoute ~> check {

      }
    }
  }
}
