package helpers

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import io.gatling.core.structure.ChainBuilder
import io.gatling.http.request.builder.HttpRequestBuilder
import config.Config
import data.NaughtyStrings

import scala.collection.mutable.ListBuffer


sealed trait ApiData {

  val name: String

  // standard REST API params
  val method: String
  val path: String
  val query: String
  val body: String
  val expRspCode: Int

  val version: Int

  def fullPath: String = path + query
}

case class API_V1(
                   method: String = "",
                   path: String = "",
                   query: String = "",
                   body: String = "",
                   name: String = "",
                   expRspCode: Int = 200,
                   version: Int = 1
                 ) extends ApiData


object Requester {

  def request(api: ApiData): HttpRequestBuilder = {
    http(api.name)
      .httpRequest(api.method, api.fullPath)
      .body(StringBody(api.body))
      .asJson
      .check(status.in(api.expRspCode))
  }

  def requestWithAuth(api: ApiData, signIn: Boolean = Config.auth): ChainBuilder = {
    exec {
      session => session.set("auth_api", signIn)
    }
    .doIfEquals("${auth_api}", true) {
      exec(request(Config.auth_api))
    }
    .exec(request(api))
  }

}

object FeederUtil {
  // Input: an array of strings and a <newKey>
  // Output: an array of maps consisting of all the input-array's values mapped to <newKey>.
  // Note: `queue` is the default feeder strategy.  It can be overridden with appending `.circular`, `.shuffle` , or `.random`
  // see https://gatling.io/docs/current/session/feeder/#strategies
  def mapArray(in: Array[String],
               newKey: String = "body"): Array[Map[String, Any]] = {
    val allcases = ListBuffer[Map[String, Any]]()
    in.foreach(s => allcases += Map(newKey -> s))
    allcases.toArray
  }
}

object NaughtyStringFeeder {
  val naughtyKey = "naughtyString"
  val feeder: Array[Map[String, Any]] = FeederUtil.mapArray(NaughtyStrings.all, naughtyKey)

  def modSessionKey(key: String): ChainBuilder = {
    exec(session => session.set(key, session(naughtyKey).as[String]))
  }
}


