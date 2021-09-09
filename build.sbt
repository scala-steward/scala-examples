version := "0.1"

lazy val scala2_13 = "2.13.6"

Global / onChangedBuildSource := ReloadOnSourceChanges

// cats
lazy val catsVersion       = "2.6.1"
lazy val catsEffectVersion = "2.5.2"
lazy val scalatestVersion  = "3.2.9"
// akka
lazy val akkaVersion = "2.6.15"
// slack-api-client
lazy val slackVersion = "1.12.1"

lazy val `examples` = (project in file("examples")).settings(
  name := "examples",
  scalaVersion := scala2_13,
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core"   % catsVersion,
    "org.typelevel" %% "cats-effect" % catsEffectVersion,
    "org.scalatest" %% "scalatest"   % scalatestVersion,

    "com.slack.api" % "slack-api-client" % slackVersion
  )
)

lazy val `akka-examples` = (project in file("akka-examples")).settings(
  name := "akka-examples",
  scalaVersion := scala2_13,
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor"  % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j"  % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion
  ),
  scalacOptions ++= Seq(
    "-deprecation",
    "-language:postfixOps"
  )
)

lazy val `app` = (project in file("app")).settings(
  name := "app",
  scalaVersion := scala2_13
).dependsOn( `examples` )

lazy val root = (project in file(".")).settings(
  scalaVersion := scala2_13
).aggregate( `examples`, `app` )
