package com.packt.akka.examples

import akka.actor._
import spray.http.DateTime

import scala.language.postfixOps

case object AskNameMessage

class TestActor extends Actor {
  def receive = {
    case AskNameMessage => {
      println(s"received AskNameMessage at: {${DateTime.now}}")
      Thread.sleep(200)
      sender ! "Fred"
    }
    case _ => println("that was unexpected")
  }
}

