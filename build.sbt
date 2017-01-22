name := """multipart-test"""

version := "1.0"

scalaVersion := "2.11.8"

// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"

// https://mvnrepository.com/artifact/com.amazonaws/aws-lambda-java-core
libraryDependencies += "com.amazonaws" % "aws-lambda-java-core" % "1.1.0"

// https://mvnrepository.com/artifact/io.spray/spray-json_2.11
libraryDependencies += "io.spray" % "spray-json_2.11" % "1.3.3"

// https://mvnrepository.com/artifact/org.specs2/specs2-core_2.11
libraryDependencies += "org.specs2" % "specs2-core_2.11" % "3.8.7" % Test
scalacOptions in Test ++= Seq("-Yrangepos")
