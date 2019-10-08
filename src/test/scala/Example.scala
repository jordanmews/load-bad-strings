import helpers.API_V1
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.Predef.{http, status}
import io.gatling.http.request.builder.HttpRequestBuilder

object Example {
  val GET_SOME = API_V1("GET", "/some", query = """?something=${something}""")
  val POST_SOME = API_V1("POST", "/some", body = """{"something":${something}""")
  val POST_SIGNIN = API_V1("POST", "/sign-in", body = "{\"username\":${username}, \"password\":${password}")

  def signIn(expRspCode: Seq[Int] = Seq(200)): HttpRequestBuilder = {
    http(POST_SIGNIN.name)
      .httpRequest(POST_SIGNIN.method, POST_SIGNIN.fullPath)
      .body(StringBody(POST_SIGNIN.body))
      .asJson
      .check(status.in(expRspCode))
  }
}