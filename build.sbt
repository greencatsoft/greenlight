name := "greenlight"

description := "Simple BDD style testing framework for Scala and Scala.js."

organization := "com.greencatsoft"

version := "0.1-SNAPSHOT"

homepage := Some(url("http://github.com/greencatsoft/greenlight"))

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if(isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

pomIncludeRepository := { _ => false }

pomExtra := (
  <licenses>
    <license>
      <name>Apache License 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:greencatsoft/greenlight.git</url>
    <connection>scm:git:git@github.com:greencatsoft/greenlight.git</connection>
  </scm>
  <developers>
    <developer>
      <id>mysticfall</id>
      <name>Xavier Cho</name>
      <url>http://github.com/mysticfall</url>
    </developer>
  </developers>
)

val sharedSettings = Seq(
    name := "greenlight",
    scalaVersion := "2.11.5",
    scalacOptions ++= Seq("-feature","-deprecation"),
    unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil,
    unmanagedSourceDirectories in Compile <+= baseDirectory(_ /  "shared" / "main" / "scala"),
    unmanagedSourceDirectories in Test := (scalaSource in Test).value :: Nil,
    unmanagedSourceDirectories in Test <+= baseDirectory(_ /  "shared" / "test" / "scala"),
    EclipseKeys.useProjectId := true
  )

lazy val root = project.in(file("."))
  .aggregate(`greenlight-jvm`, `greenlight-js`)
  .settings(
    name := "root",
    publishArtifact := false,
    publishArtifact in (Compile, packageDoc) := false,
    publishArtifact in (Compile, packageSrc) := false,
    publishArtifact in (Compile, packageBin) := false
  )

lazy val `greenlight-jvm` = project.in(file("jvm"))
  .settings(sharedSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-sbt" % "test-interface" % "1.0",
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    ),
    testFrameworks := Seq(new TestFramework("com.greencatsoft.greenlight.jvm.Greenlight"))
  )

lazy val `greenlight-js` = project.in(file("js"))
  .settings(sharedSettings: _*)
  .settings(
    libraryDependencies += "org.scala-js" %% "scalajs-test-interface" % scalaJSVersion,
    testFrameworks := Seq(new TestFramework("com.greencatsoft.greenlight.js.Greenlight"))
  )
  .enablePlugins(ScalaJSPlugin)
