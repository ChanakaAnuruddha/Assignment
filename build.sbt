name := "BookStore"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies ++= Seq(
  "com.google.code.gson" % "gson" % "2.8.2",
  "com.newmotion" %% "akka-rabbitmq" % "5.0.4-beta"
)