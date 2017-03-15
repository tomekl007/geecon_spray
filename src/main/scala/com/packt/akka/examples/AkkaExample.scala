package com.packt.akka.examples

import akka.actor.{Actor, ActorSystem, Props}

case class Greeting(msg: String, name: String)

case class SayGoodbye(msg: String)

class HelloActor extends Actor {
  def receive = {
    case g: Greeting => println(s"hello ${g.name}")
    case s: SayGoodbye => println(s"Bye you too")
  }
}

object Main  {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("HelloSystem")

    val helloActor = system.actorOf(Props[HelloActor], name = "helloactor")
    helloActor ! Greeting("Welcome", "Tom")
    helloActor ! SayGoodbye("Bye :-(")
  }
}
