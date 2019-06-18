name := "ec2-ri-optimizer"

version := "0.1"

scalaVersion := "2.12.0"

libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.11.568"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "org.json4s" %% "json4s-native" % "3.6.6"
//libraryDependencies += "com.sumologic.shellbase" % "shellbase-slack" % "1.5.1"