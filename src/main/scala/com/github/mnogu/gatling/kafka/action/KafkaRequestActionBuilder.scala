package com.github.mnogu.gatling.kafka.action

import com.github.mnogu.gatling.kafka.Predef._
import com.github.mnogu.gatling.kafka.request.builder.KafkaAttributes
import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.structure.ScenarioContext
import org.apache.kafka.clients.producer.KafkaProducer
import io.gatling.core.protocol.{Protocol}

import scala.collection.JavaConverters._

class KafkaRequestActionBuilder[K,V](kafkaAttributes: KafkaAttributes[K,V])
  extends ActionBuilder {

  override def build(ctx: ScenarioContext, next: Action): Action = {
    val kafkaProtocol = ctx.protocolComponentsRegistry.components().getOrElse[Protocol](
            throw new UnsupportedOperationException("Kafka Protocol wasn't registered"))
    new KafkaRequestAction(
      new KafkaProducer[K,V](kafka.properties.asJava),
      kafkaAttributes,
      next,
      ctx.coreComponents.statsEngine
    )
  }
}