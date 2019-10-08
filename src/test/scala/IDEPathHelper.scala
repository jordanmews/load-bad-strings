import java.nio.file.Path

import io.gatling.commons.util.PathHelper._

object IDEPathHelper {

  val gatlingConfUrl: Path = getClass.getClassLoader.getResource("gatling.conf")
  val projectRootDir = gatlingConfUrl.ancestor(3)

  val mavenSourcesDirectory = projectRootDir / "src" / "main" / "scala"
  val mavenResourcesDirectory = projectRootDir / "src" / "main" / "resources"
  val mavenSimulationsDirectory = projectRootDir / "src" / "main" / "scala"
  val mavenTargetDirectory = projectRootDir / "target"
  val mavenBinariesDirectory = mavenTargetDirectory / "test-classes"

  val resourcesDirectory = mavenResourcesDirectory
  val simulationsDirectory = mavenSimulationsDirectory
  val recorderSimulationsDirectory = mavenSourcesDirectory
  val resultsDirectory = mavenTargetDirectory / "gatling"
  val recorderConfigFile = mavenResourcesDirectory / "recorder.conf"
}
