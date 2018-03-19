package com.example.hello.impl

import com.example.hello.api.HellolagomService
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.ExecutionContext

/**
  * Implementation of the HellolagomService.
  */
class HellolagomServiceImpl(persistentEntityRegistry: PersistentEntityRegistry)(implicit ec: ExecutionContext) extends HellolagomService {

  override def updateSettings() = { request =>
    val ref = persistentEntityRegistry.refFor[HellolagomEntity]("hello")
    ref.ask(UpdateMapCmd(request)).map(_.updated)
  }
}
