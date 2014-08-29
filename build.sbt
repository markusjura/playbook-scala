import com.typesafe.sbteclipse.core.EclipsePlugin.EclipseKeys

name := """playbook"""

version := "1.0"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(EclipseKeys.skipParents in ThisBuild := false)

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  // Server side dependencies
  cache,
  ws,
  "com.typesafe.play" %% "play-slick" % "0.7.0",
  "joda-time" % "joda-time" % "2.3",
  "org.joda" % "joda-convert" % "1.5",
  "com.github.tototoshi" %% "slick-joda-mapper" % "1.1.0",
  "org.scalatestplus" %% "play" % "1.1.1" % "test",
  // Client side dependencies
  "org.webjars" % "jquery" % "1.11.1",
  "org.webjars" % "bootstrap" % "3.2.0" exclude("org.webjars", "jquery"),
  "org.webjars" % "font-awesome" % "4.1.0",
  "org.webjars" % "react" % "0.11.1"
)

// Configure the steps of the asset pipeline (used in stage and dist tasks)
// digest = Adds hash to filename
// gzip = Zips all assets, Asset controller serves them automatically when client accepts them
pipelineStages := Seq(digest, gzip)

// Minify LESS files
LessKeys.compress in Assets := true