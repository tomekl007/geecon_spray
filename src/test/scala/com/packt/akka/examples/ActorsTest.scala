package com.packt.akka.examples


import java.util.concurrent.TimeUnit

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import org.specs2.mutable.Specification
import spray.http.DateTime

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps


class ActorsTest extends Specification {
  "actor" should {
    "receive events" in {
      //given
      val system = ActorSystem("AskTestSystem")
      val myActor = system.actorOf(Props[TestActor], name = "myActor")

      //when
      implicit val timeout = Timeout(Duration(5, TimeUnit.SECONDS))
      val future = myActor ? AskNameMessage
      val result = Await.result(future, timeout.duration).asInstanceOf[String]
      println(s"first result: $result at: {${DateTime.now}}")

      //then
      result shouldEqual "Fred"

      //and
      val future2: Future[String] = ask(myActor, AskNameMessage).mapTo[String]
      val result2 = Await.result(future2, Duration(3, TimeUnit.SECONDS))
      println(s"second result: $result2 at: {${DateTime.now}}")

      //then
      result2 shouldEqual "Fred"

    }
  }
}
