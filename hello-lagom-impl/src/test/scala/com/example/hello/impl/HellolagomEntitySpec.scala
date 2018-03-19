package com.example.hello.impl

import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.lightbend.lagom.scaladsl.testkit.PersistentEntityTestDriver
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}
import scala.collection.immutable._

class HellolagomEntitySpec extends WordSpec with Matchers with BeforeAndAfterAll {

  private val system = ActorSystem("HellolagomEntitySpec",
    JsonSerializerRegistry.actorSystemSetupFor(HellolagomSerializerRegistry))

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  private def withTestDriver(block: PersistentEntityTestDriver[HellolagomCommand, HellolagomEvent,
    HellolagomState] => Unit): Unit = {
    val driver = new PersistentEntityTestDriver(system, new HellolagomEntity, "hello-lagom-1")
    block(driver)
    driver.getAllIssues should have size 0
  }

  val map = Map[String, Seq[String]]("test2" -> Seq("E", "F", "D"))

  "hello-lagom entity" should {

    "allow updating the map" in withTestDriver { driver =>
      val outcome = driver.run(UpdateMapCmd(map))
      outcome.state.map should === (map)
    }
  }
}
