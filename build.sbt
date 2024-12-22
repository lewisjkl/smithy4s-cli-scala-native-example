scalaVersion := "3.3.3" // A Long Term Support version.

enablePlugins(ScalaNativePlugin)
enablePlugins(Smithy4sCodegenPlugin)

// set to Debug for compilation details (Info is default)
logLevel := Level.Info

// import to add Scala Native options
import scala.scalanative.build._

libraryDependencies ++= Seq(
  "com.armanbilge" %%% "epollcat" % "0.1.6",
  "com.disneystreaming.smithy4s" %%% "smithy4s-decline" % smithy4sVersion.value,
  "com.disneystreaming.smithy4s" %%% "smithy4s-http4s" % smithy4sVersion.value,
  "org.http4s" %%% "http4s-ember-client" % "0.23.30",
  "com.monovore" %%% "decline-effect" % "2.4.1"
)

val isLinux = Option(System.getProperty("os.name"))
  .exists(_.toLowerCase().contains("linux"))
val isMacOs =
  Option(System.getProperty("os.name")).exists(_.toLowerCase().contains("mac"))
val isArm = Option(System.getProperty("os.arch"))
  .exists(_.toLowerCase().contains("aarch64"))

// Copied from Chris Davenport's examples using Ember with Scala Native
nativeConfig ~= { c =>
  if (isLinux) { // brew-installed s2n
    c.withLinkingOptions(c.linkingOptions :+ "-L/home/linuxbrew/.linuxbrew/lib")
  } else if (isMacOs) // brew-installed OpenSSL
    if (isArm)
      c.withLinkingOptions(
        c.linkingOptions :+ "-L/opt/homebrew/opt/openssl@3/lib"
      )
    else
      c.withLinkingOptions(c.linkingOptions :+ "-L/usr/local/opt/openssl@3/lib")
  else c
}
envVars ++= {
  val ldLibPath =
    if (isLinux)
      Map("LD_LIBRARY_PATH" -> "/home/linuxbrew/.linuxbrew/lib")
    else Map("LD_LIBRARY_PATH" -> "/usr/local/opt/openssl@1.1/lib")
  Map("S2N_DONT_MLOCK" -> "1") ++ ldLibPath
}
