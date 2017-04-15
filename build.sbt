name := "scala-simple-app"

version := "1.0"

scalaVersion := "2.12.1"

enablePlugins(sbtdocker.DockerPlugin)

lazy val akka_version = "2.4.17"
lazy val typesafe_config_version = "1.3.0"
lazy val scala_version = "2.11.7"
lazy val scalatest_version = "3.0.1"
lazy val scalacheck_version ="7.2.0"
lazy val scalaz_version = "7.2.0"
lazy val json4s_jackson_version = "3.5.1"
lazy val json4s_native_version = "3.5.1"
lazy val scalalogging_version ="3.5.0"
lazy val slf4j_version="1.7.12"
lazy val commons_codec_version= "1.10"
lazy val h2_version = "1.4.190"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-log4j12" % slf4j_version,
  "com.typesafe.scala-logging" %% "scala-logging" % scalalogging_version,
  "com.typesafe" % "config" % typesafe_config_version,
  "org.json4s" %% "json4s-native" % json4s_native_version,
  "org.json4s" %% "json4s-jackson" % json4s_jackson_version,
  "com.typesafe.akka" %% "akka-actor" % akka_version,
  "com.h2database" % "h2" % h2_version,
  "org.scalatest" %% "scalatest" % scalatest_version % "test",
  "org.mockito" % "mockito-all" % "1.9.5" % "test"
)

dockerfile in docker := {
  val jarFile = sbt.Keys.`package`.in(Compile, packageBin).value
  val classpath = (managedClasspath in Compile).value
  val mainclass = mainClass.in(Compile, packageBin).value.getOrElse(sys.error("Expected exactly one main class"))
  val jarTarget = s"/app/${jarFile.getName}"
  // Make a colon separated classpath with the JAR file
  val classpathString = classpath.files.map("/app/" + _.getName).mkString(":") + ":" + jarTarget
  new Dockerfile {
    // Base image
    from("java")
    // Add all files on the classpath
    add(classpath.files, "/app/")
    // Add the JAR file
    add(jarFile, jarTarget)
    // On launch run Java with the classpath and the main class
    entryPoint("java", "-cp", classpathString, mainclass)
  }
}

// Set names for the image
imageNames in docker := Seq(
//  ImageName("sbtdocker/basic:stable"),
  ImageName(namespace = Some(organization.value),
    repository = name.value,
    tag = Some("v" + version.value))
)


    