package org.encalmo.aws

import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest
import upickle.default.*

import scala.util.control.NonFatal

object LambdaSecrets {

  inline def retrieveSecrets(
      maybeGetProperty: String => Option[String]
  ): Map[String, String] =
    retrieveSecrets(maybeGetProperty, println, println)

  def retrieveSecrets(
      maybeGetProperty: String => Option[String],
      logDebug: String => Unit,
      logError: String => Unit
  ): Map[String, String] =

    val region: Region =
      Region.of(maybeGetProperty("AWS_DEFAULT_REGION").getOrElse("us-east-1"))

    val credentialsProvider = new AwsCredentialsProvider {
      override def resolveCredentials(): AwsCredentials =
        AwsSessionCredentials.create(
          maybeGetProperty("AWS_ACCESS_KEY_ID").getOrElse(
            throw new Exception(
              "Missing AWS_ACCESS_KEY_ID environment property"
            )
          ),
          maybeGetProperty("AWS_SECRET_ACCESS_KEY").getOrElse(
            throw new Exception(
              "Missing AWS_SECRET_ACCESS_KEY environment property"
            )
          ),
          maybeGetProperty("AWS_SESSION_TOKEN").getOrElse(
            throw new Exception(
              "Missing AWS_SESSION_TOKEN environment property"
            )
          )
        )
    }
    maybeGetProperty("ENVIRONMENT_SECRETS")
      .map { secretArns =>
        val client = SecretsManagerClient
          .builder()
          .httpClientBuilder(UrlConnectionHttpClient.builder())
          .credentialsProvider(credentialsProvider)
          .region(region)
          .build()
        try {
          secretArns
            .split(",")
            .map(_.trim())
            .map(secretArn =>
              logDebug(s"Looking for secrets in $secretArn ...")
              getSecretValueString(
                secretArn,
                client,
                logError
              )
                .map { secret =>
                  val secrets = secret.readAs[Map[String, String]]
                  logDebug(
                    s"Retrieved ${secrets.size} secrets from $secretArn: ${secrets.keys.toSeq.sorted
                        .mkString(", ")}"
                  )
                  secrets
                }
                .getOrElse {
                  logError(
                    s"Didn't retrieve any secrets from $secretArn"
                  )
                  Map.empty[String, String]
                }
            )
            .reduce(_ ++ _)
        } finally {
          client.close()
        }
      }
      .getOrElse(Map.empty[String, String])

  private def getSecretValueString(
      secretId: String,
      client: SecretsManagerClient,
      logError: String => Unit
  ): Option[String] =
    try {
      val secret = client
        .getSecretValue(
          GetSecretValueRequest.builder().secretId(secretId).build()
        )
        .secretString()
      Some(secret)
    } catch {
      case NonFatal(e) =>
        logError(e.toString())
        None
    }

  extension (string: String) {
    inline def readAs[T: ReadWriter]: T =
      try (read(string))
      catch {
        case NonFatal(e) =>
          throw new Exception(e.getMessage)
      }
  }
}
