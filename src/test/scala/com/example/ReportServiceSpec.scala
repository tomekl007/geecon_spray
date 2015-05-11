package com.example

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._

class ReportServiceSpec extends Specification with Specs2RouteTest with ReportService {
  def actorRefFactory = system
  
  "ReportService" should {

    "" in {
      Get() ~> reportRoute ~> check {

      }
    }
  }
}
