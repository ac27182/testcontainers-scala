package com.dimafeng.testcontainers

import org.testcontainers.containers.{MariaDBContainer => JavaMariaDBContainer}

case class MariaDBContainer(
  dockerImageName: String = MariaDBContainer.defaultDockerImageName,
  dbName: String = MariaDBContainer.defaultDatabaseName,
  dbUsername: String = MariaDBContainer.defaultUsername,
  dbPassword: String = MariaDBContainer.defaultPassword,
  configurationOverride: Option[String] = None,
  urlParams: Map[String, String] = Map.empty,
  commonJdbcParams: JdbcDatabaseContainer.CommonParams = JdbcDatabaseContainer.CommonParams()
) extends SingleContainer[JavaMariaDBContainer[_]] with JdbcDatabaseContainer {

  override val container: JavaMariaDBContainer[_] = {
    val c = new JavaMariaDBContainer(dockerImageName)

    c.withDatabaseName(dbName)
    c.withUsername(dbUsername)
    c.withPassword(dbPassword)
    configurationOverride.foreach(c.withConfigurationOverride)
    urlParams.foreach { case (key, value) =>
      c.withUrlParam(key, value)
    }
    commonJdbcParams.applyTo(c)

    c
  }

  def testQueryString: String = container.getTestQueryString

}

object MariaDBContainer {

  val defaultDockerImageName = s"${JavaMariaDBContainer.IMAGE}:${JavaMariaDBContainer.DEFAULT_TAG}"
  val defaultDatabaseName = "test"
  val defaultUsername = "test"
  val defaultPassword = "test"

  case class Def(
    dockerImageName: String = MariaDBContainer.defaultDockerImageName,
    dbName: String = MariaDBContainer.defaultDatabaseName,
    dbUsername: String = MariaDBContainer.defaultUsername,
    dbPassword: String = MariaDBContainer.defaultPassword,
    configurationOverride: Option[String] = None,
    urlParams: Map[String, String] = Map.empty,
    commonJdbcParams: JdbcDatabaseContainer.CommonParams = JdbcDatabaseContainer.CommonParams()
  ) extends ContainerDef {

    override type Container = MariaDBContainer

    override def createContainer(): MariaDBContainer = {
      new MariaDBContainer(
        dockerImageName,
        dbName,
        dbUsername,
        dbPassword,
        configurationOverride,
        urlParams,
        commonJdbcParams
      )
    }
  }

}
