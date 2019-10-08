import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object Engine extends App {


  val props = new GatlingPropertiesBuilder()
    .resourcesDirectory(IDEPathHelper.resourcesDirectory.toString)
    .resultsDirectory(IDEPathHelper.resultsDirectory.toString)
    .binariesDirectory(IDEPathHelper.mavenBinariesDirectory.toString)
    .simulationsDirectory(IDEPathHelper.simulationsDirectory.toString)

  // to run from a JVM arg
  //    .simulationClass(System.getenv("SIMULATION"))

  Gatling.fromMap(props.build)
}
