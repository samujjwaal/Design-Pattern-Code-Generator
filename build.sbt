name := "hw1"

version := "0.1"

scalaVersion := "2.13.1"

mainClass in(Compile, run) := Some("com.DePaCoG")
mainClass in(Compile, packageBin) := Some("com.DePaCoG")

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.last
}
assemblyOption in assembly := (assemblyOption in assembly).value.copy(cacheOutput = false)

////// https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

libraryDependencies += "org.junit.jupiter" % "junit-jupiter-api" % "5.6.0" % Test

// https://mvnrepository.com/artifact/com.squareup/javapoet
libraryDependencies += "com.squareup" % "javapoet" % "1.12.1"

// https://mvnrepository.com/artifact/com.typesafe/config
libraryDependencies += "com.typesafe" % "config" % "1.4.0"


