package com.github.mnogu.gatling.kafka.action

import com.github.mnogu.gatling.kafka.Predef._
import com.github.mnogu.gatling.kafka.request.builder.KafkaAttributes
import io.gatling.commons.stats.{KO, OK}
import io.gatling.commons.util.ClockSingleton.nowMillis
import io.gatling.commons.validation.Validation
import io.gatling.core.action.{Action, ChainableAction}
import io.gatling.core.session._
import io.gatling.core.stats.StatsEngine
import io.gatling.core.stats.message.ResponseTimings
import org.apache.kafka.clients.producer._

class KafkaRequestAction[K,V](
  val producer: KafkaProducer[K,V],
  val kafkaAttributes: KafkaAttributes[K,V],
  val next: Action,
  val statsEngine: StatsEngine)
  extends ChainableAction {

  override def execute(session: Session): Unit = {
    kafkaAttributes.requestName(session).flatMap { resolvedRequestName =>
      val payload = kafkaAttributes.payload

      val outcome = kafkaAttributes.key match {
        case Some(k) => k(session).flatMap { resolvedKey =>
          sendRequest(
            resolvedRequestName,
            producer,
            Some(resolvedKey),
            payload,
            session)
        }
        case None => sendRequest(
          resolvedRequestName,
          producer,
          None,
          payload,
          session)
      }
      
      outcome.onFailure(
        errorMessage => statsEngine.reportUnbuildableRequest(
          session, resolvedRequestName, errorMessage))
      outcome
    }
  }
  private def sendRequest(
      requestName: String,
      producer: Producer[K,V],
      key: Option[K],
      payload: Expression[V],
      session: Session): Validation[Unit] = {

    payload(session).map { resolvedPayload =>
      val record = key match {
        case Some(k) => new ProducerRecord[K,V](kafka.topic, k, resolvedPayload)
        case None => new ProducerRecord[K,V](kafka.topic, resolvedPayload)
      }

      val requestStartDate = nowMillis
      val requestEndDate = nowMillis

      // send the request
      producer.send(record, new Callback() {
        override def onCompletion(m: RecordMetadata, e: Exception): Unit = {
          val responseStartDate = nowMillis
          val responseEndDate = nowMillis

          // log the outcome
          statsEngine.logResponse(
            session,
            requestName,
            ResponseTimings(requestStartDate, responseEndDate),
            if (e == null) OK else KO,
            if (e == null) None else Some(e.getMessage),
            None
          )
        }
      })

      // calling the next action in the chain
      next ! session
    }
  }

  override def name: String = {
    getClass.getName
  }
}
