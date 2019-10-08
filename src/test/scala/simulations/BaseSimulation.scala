package simulations

import config.Config
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt
import io.gatling.commons.validation.Success
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}


import scala.util.Random

class BaseSimulation extends Simulation{

  val httpProtocol = http.header("user-agent", "test")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:46.0) Gecko/20100101 Firefox/46.0")

  def defineIfUndefined(sessionKey: String, defaultVal: String): ChainBuilder = {
    // Put a default value in session if none exists.
    // side-note:  It's dirty/wordy.  Maybe there's a way to make this cleaner within Gatling's DSL, but who knows.
    doIfOrElse(_ (sessionKey).validate[String].isInstanceOf[Success[String]]) {
      exec()
    } {
      exec { session =>
        session.set(sessionKey, defaultVal)
      }
    }
  }

  def feedIfUndefined(sessionKey: String, feeder: Iterator[Map[String,String]]): ChainBuilder = {
    // Put a default value from a feeder into session if none exists
    doIfOrElse(_ (sessionKey).validate[String].isInstanceOf[Success[String]]) {
      exec()
    } {
      feed(feeder)
    }
  }

  val ranStringFeeder = Iterator.continually(Map(
      "username" -> Random.alphanumeric.take(24).mkString
      )
  )

  def initSessionDefaults() = {
        exec(defineIfUndefined("password", "password"))
        .exec(feedIfUndefined("username", ranStringFeeder))
    }

  def prerequisites() = initSessionDefaults()
  def activeScenario:ScenarioBuilder = scenario("browser").exec()
  def fullScenario:ScenarioBuilder = scenario("browser").exec(prerequisites()).exec(activeScenario)

}

