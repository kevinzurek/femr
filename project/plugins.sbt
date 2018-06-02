// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "jBCrypt Repository" at "http://repo1.maven.org/maven2/org/"

resolvers += "Fluentium-Core Repository" at "https://mvnrepository.com/artifact/org.fluentlenium/fluentlenium-core"

resolvers += "Fluentium-FluentTest junit adapter" at "https://mvnrepository.com/artifact/org.fluentlenium/fluentlenium-junit"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.12")

// Ebean ORM
addSbtPlugin("com.typesafe.sbt" % "sbt-play-ebean" % "4.1.0")

//versioning and compression
addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.3")

addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.2")

