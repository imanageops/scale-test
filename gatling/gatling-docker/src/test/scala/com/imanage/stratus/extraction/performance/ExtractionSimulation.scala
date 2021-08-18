package com.imanage.stratus.extraction.performance

import com.imanage.stratus.authToken.{log_in, system_user_log_in}
import io.gatling.core.Predef._
import io.gatling.http.Predef.{http, _}
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.request.builder.HttpRequestBuilder.toActionBuilder

class ExtractionSimulation extends Simulation {

  private val numAtOnceUsers = Integer.getInteger("atOnceUsers")
  private val numConstantConcurrentUsers = Integer.getInteger("constantConcurrentUsers", 1)
  private val testDurationSeconds = Integer.getInteger("testDurationSeconds", 60)

  private val apiBaseUri: String = System.getProperty("apiBaseUri", "https://extractionhttps.service.imanagecloud.com")
  private val systemUserLoginUri = System.getProperty("loginUri", "https://frontend.service.imanagecloud.com/config/api/v2/system-login")
  //  private val apiBaseUri = System.getProperty("apiBaseUri", "https://localhost:18443")
  //  private val systemUserLoginUri = System.getProperty("loginUri", "https://localhost:28443/api/v2/session/system-user-login")
  private val user = System.getProperty("user", "scaleadmin1@scaletest1.com")
  private val password = System.getProperty("password", "password")
  private val systemUser = System.getProperty("systemUser", "work-pipeline-proc@operations.com")
  private val systemUserPassword = System.getProperty("systemUserPassword", "go1547rjpj06rm5bp75upb1drib9fn0")

  val xAuthToken: String = log_in(apiBaseUri, user, password)
  val systemUserXAuthToken: String = system_user_log_in(systemUserLoginUri, systemUser, systemUserPassword)

  if (systemUserXAuthToken == null) {
    printf("Failed to log in to %s%n", systemUserLoginUri)
    System.exit(1)
  }

  printf("Using token %s for system user%n", systemUserXAuthToken)

  val headers = Map(
    "X-Auth-Token" -> s"${systemUserXAuthToken}",
  )

  private val feeder = Array(
    Map("customerId" -> "516", "libraryName" -> "ATLDEV4POD1", "documentId" -> "ATLDEV4POD1!728.1"),
    Map("customerId" -> "516", "libraryName" -> "ATLDEV4POD1", "documentId" -> "ATLDEV4POD1!730.1"),
    Map("customerId" -> "516", "libraryName" -> "ATLDEV4POD1", "documentId" -> "ATLDEV4POD1!732.1"),
  ).random

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(apiBaseUri)
    .headers(headers)
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36")

  val scn = scenario("Extract documents")
    .feed(feeder)
    .group("Extract document") {
      exec(http("Extract")
        .get(s"/api/v2/customers/$${customerId}/libraries/$${libraryName}/documents/$${documentId}/extract")
        .queryParam("extractionType", "TEXT_AND_METADATA")
        .header("Accept", "application/json")
        .check(status.is(200))
      )
    }

  if (numAtOnceUsers != null)
    setUp(scn.inject(atOnceUsers(10))).protocols(httpProtocol)
  else {
    setUp(scn.inject(constantConcurrentUsers(numConstantConcurrentUsers).during(testDurationSeconds.seconds))).protocols(httpProtocol)
  }

}
