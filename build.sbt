version := "0.1"

lazy val scala2_13 = "2.13.6"

lazy val catsVersion       = "2.6.1"
lazy val catsEffectVersion = "2.5.2"
lazy val scalatestVersion  = "3.2.9"

lazy val `examples` = (project in file("examples")).settings(
  name := "examples",
  scalaVersion := scala2_13,
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core"   % catsVersion,
    "org.typelevel" %% "cats-effect" % catsEffectVersion,
    "org.scalatest" %% "scalatest"   % scalatestVersion
  )
)

lazy val `app` = (project in file("app")).settings(
  name := "app",
  scalaVersion := scala2_13
).dependsOn( `examples` )

lazy val root = (project in file(".")).settings(
  scalaVersion := scala2_13
).aggregate( `examples`, `app` )
