package com.example.hello.impl

import com.example.hello.api.HellolagomService
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

/**
  * Implementation of the HellolagomService.
  */
class HellolagomServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends HellolagomService {

  override def updateSettings() = { request =>
    val ref = persistentEntityRegistry.refFor[HellolagomEntity]("hello")
    ref.ask(UpdateMapCmd(request))
  }
}
