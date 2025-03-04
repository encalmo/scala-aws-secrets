package org.encalmo.aws

import scala.io.AnsiColor
import scala.jdk.CollectionConverters.*

class LambdaSecretsSpec extends munit.FunSuite {

  val environment = System.getenv().asScala
    ++ Map(
      "ENVIRONMENT_SECRETS" -> "arn:aws:secretsmanager:eu-central-1:047719648492:secret:test-secret-rJbTad"
    )

  def debug(s: String): Unit = println(s)
  def error(s: String): Unit = println(
    s"${AnsiColor.WHITE}${AnsiColor.RED_B}$s${AnsiColor.RESET}"
  )

  test("retrieve test secrets") {
    val secrets =
      LambdaSecrets.retrieveSecrets(environment.get)
    assert(
      secrets.size > 0,
      "Retrieved secrets size should be greater than zero"
    )
  }

}
