package com.example.actors

import akka.actor.Actor
import com.example.actors.messages.TriggerGenerating
import spray.caching.Cache
import spray.http.DateTime

class ReportGeneratorActor(cache: Cache[String]) extends Actor {
  implicit val ec = context.dispatcher

  override def receive: Receive = {
    case TriggerGenerating(accountId, reportType) => cache(accountId + reportType) {
      s"reportContent that was generated ${DateTime.now}"
    }
  }
}
