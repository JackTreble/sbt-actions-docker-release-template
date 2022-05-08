import com.typesafe.sbt.SbtNativePackager.autoImport.packageName
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.dockerUsername
import ReleaseTransformations._

lazy val root = (project in file("."))
  .settings(
    name := "sbt-actions-docker-release-template",
    organization := "dev.treble",
    ThisBuild / scalaVersion := "2.13.8"
  )
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(actionsSettings, dockerSettings, releaseSettings)

lazy val actionsSettings = Seq(
  ThisBuild / githubWorkflowPublishPreamble := Seq(
    WorkflowStep.Use(
      name = Some("Set Git User"),
      ref =
        UseRef.Public(owner = "fregante", repo = "setup-git-user", ref = "v1")
    ),
    WorkflowStep.Use(
      name = Some("GitHub Slug Action"),
      ref = UseRef.Public(
        owner = "rlespinasse",
        repo = "github-slug-action",
        ref = "4.2.4"
      )
    ),
    WorkflowStep.Use(
      name = Some("Login to GitHub Container Registry"),
      ref = UseRef.Public(
        owner = "docker",
        repo = "login-action",
        ref = "v2"
      ),
      params = Map(
        "registry" -> "ghcr.io",
        "username" -> "${{ github.actor }}",
        "password" -> "${{ secrets.GITHUB_TOKEN }}"
      )
    )
  ),
  ThisBuild / githubWorkflowPublish := Seq(
    WorkflowStep.Sbt(List("release with-defaults"))
  )
)

lazy val dockerSettings =
  Seq(
    dockerRepository := Some("ghcr.io"),
    dockerUpdateLatest := true,
    dockerUsername := sys.env.get("GITHUB_REPOSITORY_OWNER_PART_SLUG"),
    packageName := sys.env
      .getOrElse("GITHUB_REPOSITORY_NAME_PART_SLUG", moduleName.value)
  )

lazy val releaseSettings = Seq(
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    releaseStepTask(Docker / publish),
    setNextVersion,
    commitNextVersion,
    pushChanges
  )
)
