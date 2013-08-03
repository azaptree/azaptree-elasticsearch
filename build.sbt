organization := "com.azaptree"

name := "azaptree-elasticsearch"

version := "0.0.1-SNAPSHOT"

scalaVersion in ThisBuild := "2.10.2"

autoCompilerPlugins := true

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.azaptree" %% "azaptree-commons" % "0.0.1-SNAPSHOT"

libraryDependencies +=  "org.elasticsearch" % "elasticsearch" % "0.90.2"

scalacOptions ++= Seq("-P:continuations:enable",
					  "-optimise",
					  "-feature",
					  "-language:postfixOps",
					  "-language:higherKinds",
					  "-deprecation")

scalariformSettings

net.virtualvoid.sbt.graph.Plugin.graphSettings


