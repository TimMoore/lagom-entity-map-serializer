package com.example.hello.api

import com.lightbend.lagom.scaladsl.api.transport.Method.PUT
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import scala.collection.immutable._

trait HellolagomService extends Service {

  def updateSettings(): ServiceCall[Map[String, Seq[String]], Map[String, Seq[String]]]

  override final def descriptor = {
    import Service._

    named("hello-lagom")
      .withCalls(
        restCall(PUT, "/settings", updateSettings())
      )
      .withAutoAcl(true)
  }
}
