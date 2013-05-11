// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository 
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("play" % "sbt-plugin" % "2.1.1")

// Need juint and scalatest to make unit testing possible in eclipse
libraryDependencies += "junit" % "junit" % "4.10" % "test"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"