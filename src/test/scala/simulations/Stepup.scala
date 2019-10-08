package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt
import config.Config._

class Stepup extends BaseSimulation {

  override def activeScenario = scenario("base").exec()

  val steps = 5
  val totalTime = (steps * holdDuration) + (steps * rampDuration)

  // How Gatling's injection logic works below:
  // The test will run for the shorter of total duration defined in either the inject or throttle function.
  // So ensure target test duration by making inject function equal the total time of ramps & holds.
  // To reach the reachRPS-rate, the number of concurrentUsers must be high-enough to generate that much RPS.
  setUp(activeScenario
    .inject(
      incrementConcurrentUsers(100)
        .times(5)
        .eachLevelLasting(totalTime)
    )
  )
    .throttle(
      reachRps(throttleRPS) in (rampDuration seconds), holdFor(holdDuration seconds),
      reachRps(throttleRPS * 2) in (rampDuration seconds), holdFor(holdDuration seconds),
      reachRps(throttleRPS * 3) in (rampDuration seconds), holdFor(holdDuration seconds),
      reachRps(throttleRPS * 4) in (rampDuration seconds), holdFor(holdDuration seconds),
      reachRps(throttleRPS * 5) in (rampDuration seconds), holdFor(holdDuration seconds)
    )
}
