name := "renren"

version := "1.0"

scalaVersion := "2.10.4"

//resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

//resolvers += "Maven China" at "http://maven.oschina.net/content/groups/public/"

libraryDependencies += "org.jsoup" % "jsoup" % "1.7.2"

libraryDependencies += "org.mybatis" % "mybatis" % "3.2.3"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.13"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.3.11"

libraryDependencies += "net.sourceforge.htmlunit" % "htmlunit" % "2.15"

//libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "3.5" % "test")
//
//resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
//
//scalacOptions in Test ++= Seq("-Yrangepos")
//libraryDependencies := {
//  CrossVersion.partialVersion(scalaVersion.value) match {
//    // if scala 2.11+ is used, add dependency on scala-xml module
//    case Some((2, scalaMajor)) if scalaMajor >= 11 =>
//      libraryDependencies.value ++ Seq(
//        "org.scala-lang.modules" %% "scala-xml" % "1.0.3",
//        "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3",
//        "org.scala-lang.modules" %% "scala-swing" % "1.0.1")
//    case _ =>
//      // or just libraryDependencies.value if you don't depend on scala-swing
//      libraryDependencies.value :+ "org.scala-lang" % "scala-swing" % scalaVersion.value
//  }
//}