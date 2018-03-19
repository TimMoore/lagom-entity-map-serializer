package com.example.hello.impl

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import play.api.libs.json.{Format, Json}

import scala.collection.immutable.Seq


class HellolagomEntity extends PersistentEntity {

  override type Command = HellolagomCommand
  override type Event = HellolagomEvent
  override type State = HellolagomState

  override def initialState: HellolagomState = HellolagomState(
    Map[String, Seq[String]]("test" -> Seq("A", "B", "C"))
  )

  override def behavior: Behavior = {
    Actions()
      .onCommand[UpdateMapCmd, Map[String, Seq[String]]] {
        case (UpdateMapCmd(map), ctx, state) =>
          ctx.thenPersist(MapUpdatedEvt(map))(_ => ctx.reply(map))
      }.onEvent {
        case (MapUpdatedEvt(map), state) => HellolagomState(map)
      }
  }
}

case class HellolagomState(map: Map[String, Seq[String]])

object HellolagomState {
  implicit val format: Format[HellolagomState] = Json.format
}

sealed trait HellolagomCommand

final case class UpdateMapCmd(settings: Map[String, Seq[String]]) extends
  HellolagomCommand with ReplyType[Map[String, Seq[String]]]

sealed trait HellolagomEvent extends AggregateEvent[HellolagomEvent] {
  def aggregateTag = HellolagomEvent.Tag
}

object HellolagomEvent {
  val Tag = AggregateEventTag[HellolagomEvent]
}

final case class MapUpdatedEvt(settings: Map[String, Seq[String]]) extends HellolagomEvent

object HellolagomSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer(Json.format[UpdateMapCmd]),
    JsonSerializer(Json.format[MapUpdatedEvt]),
    JsonSerializer(Json.format[HellolagomState])
  )
}
