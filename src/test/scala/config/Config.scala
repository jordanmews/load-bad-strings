package config

import helpers.{API_V1, NaughtyStringFeeder, Requester}
import io.gatling.core.Predef.{feed, heavisideUsers, scenario, _}
import simulations.BaseSimulation

import scala.concurrent.duration.DurationInt

object Config {
  // Override properties thru CLI by setting JVM env vars.
  // ..i.e. -Dpath="http://localhost:3001/user" -Dmethod=GET -Dquery="?name=${custom}" -Dbody="\"{\"username\":\"${custom}\", \"password\":\"${custom}\"}" -Dtarget=custom

  // ***** Test Request  *****
  // Required
  def path: String = getProperty("path", "http://localhost:3001/user")
  def method: String = getProperty("method", "POST")

  // Either body or query is required to have the 'target' encapsulated in ${}
  def body: String = getProperty("body", "")
  def query: String = getProperty("query", "")
  def target: String = getProperty("target", "target")
  def expRspCode: Int = getProperty("expRspCode", "200").toInt
  // ***************************************************************************

  // ***** Optional Authentication Request  *****
  //  If an authentication cookie is required before your test request,
  //  set `-Dauth=true` along with the necessary API details to make the auth request (method, path, body)

  def auth: Boolean = getProperty("auth", "false").toBoolean
  def auth_path: String = getProperty("auth_path", "http://localhost:3001/sign-in")
  def auth_method: String = getProperty("auth_method", "POST")
  def auth_body: String = getProperty("auth_body", "")
  def auth_query: String = getProperty("auth_query", "")
  def auth_expRspCode: Int = getProperty("auth_expRspCode", "200").toInt
  def auth_api = API_V1(name="sign-in", method = auth_method, path = auth_path, body = auth_body, query = auth_query, expRspCode = auth_expRspCode)
  // ***************************************************************************

  // ***** Load-profile *****
  def threads: Int = getProperty("threads", "100").toInt
  def maxDuration: Int = getProperty("maxDuration", "300").toInt
  def rampDuration: Int = getProperty("rampDuration", "0").toInt
  // below currently un-used
  def holdDuration: Int = getProperty("holdDuration", "100").toInt
  def throttleRPS: Int = getProperty("throttleRPS", "100").toInt
  // ***************************************************************************

  def api = API_V1(name="test", method = method, path = path, body = body, query = query, expRspCode = expRspCode)

  def getProperty(propertyName: String, defaultValue: String): String = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }
}

class testSim extends BaseSimulation {
  // Run the test faster/concurrently.
  // Ensure all strings will be used at least once dividing the string-list among threads, adding 1 to catch any modulus-remainder and also using a circular feeder strategy.
  val iterations = (NaughtyStringFeeder.feeder.length / Config.threads) + 1
  val feeder = NaughtyStringFeeder.feeder.circular

  override def activeScenario = scenario("naughty strings")
    .repeat(iterations) {
      feed(feeder)
        .exec(NaughtyStringFeeder.modSessionKey(Config.target))
        .exec(Requester.requestWithAuth(Config.api))
    }

  setUp(fullScenario.inject(heavisideUsers(Config.threads) during (Config.rampDuration seconds))).maxDuration(Config.maxDuration seconds).protocols(httpProtocol)
}
