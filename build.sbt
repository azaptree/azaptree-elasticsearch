organization := "com.azaptree"

name := "azaptree-elasticsearch"

version := "0.0.1-SNAPSHOT"

scalaVersion in ThisBuild := "2.10.2"

autoCompilerPlugins := true

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.azaptree" %% "azaptree-application" % "0.0.1-SNAPSHOT"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"

libraryDependencies +=  "org.elasticsearch" % "elasticsearch" % "0.90.2"

libraryDependencies += compilerPlugin("org.scala-lang.plugins" % "continuations" % "2.10.2")

scalacOptions ++= Seq("-P:continuations:enable",
					  "-optimise",
					  "-feature",
					  "-language:postfixOps",
					  "-language:higherKinds",
					  "-deprecation")

scalariformSettings

net.virtualvoid.sbt.graph.Plugin.graphSettings


