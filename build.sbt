name := """consulting-services-inc"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaCore,
  cache,
  jdbc,
  ws,
  evolutions,
  filters
)

libraryDependencies +=  "org.webjars" % "webjars-play_2.11" % "2.5.0-3"
libraryDependencies += "org.webjars" % "bootstrap" % "3.3.7-1"
libraryDependencies += "com.adrianhurt" %% "play-bootstrap" % "1.1-P25-B3"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases/"
resolvers += "Apache" at "https://repo1.maven.org/maven2/"

// Play provides two styles of routers, one expects its actions to be injected
// The other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator