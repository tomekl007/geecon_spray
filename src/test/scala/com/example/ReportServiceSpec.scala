package com.example

import java.util.concurrent.TimeUnit

import akka.testkit.TestProbe
import com.example.actors.messages.TriggerGenerating
import org.specs2.mutable.Specification
import spray.caching.{Cache, LruCache}
import spray.testkit.Specs2RouteTest

import scala.concurrent.duration.Duration

class ReportServiceSpec extends Specification with Specs2RouteTest with ReportService {
  def actorRefFactory = system
  val probe = TestProbe()
  val cache: Cache[String] =
  LruCache(
    maxCapacity = 10,
    initialCapacity = 5,
    timeToLive = Duration(2, TimeUnit.DAYS),
    timeToIdle = Duration(1, TimeUnit.DAYS)
  )
  
  "ReportService" should {
    val accountId = "accountId"
    val reportType = "simpleReport"
    "return Accepted response with Location header" in {
      Get(s"/report/$accountId?type=$reportType") ~> reportRoute(cache, probe.ref) ~> check {
        println("--->" + response)
        probe.expectMsg(TriggerGenerating(accountId, reportType))
        response.status.intValue shouldEqual 202
        response.headers(0).value shouldEqual s"$accountId$reportType"
      }
    }
    val reportKey = "key"
    val reportContent = "content"
    cache(reportKey) { reportContent }
    "return reportContent when data for key is in cache" in {
      Get(s"/report/$accountId/$reportKey") ~> reportRoute(cache, probe.ref) ~> check {
        println("--->" + response)
        response.status.intValue shouldEqual 200
        response.entity.asString shouldEqual reportContent
      }
    }
  }
}
