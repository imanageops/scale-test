package com.imanage.stratus.elasticsearch.performance.query

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import org.slf4j.LoggerFactory

/*
* Test scenario :
* Wildcard Single term ES query, 40 users, 250 queries each
* */
class WildCardTrailingSingleTerm2Letters_40_250 extends Simulation {
  private val logger = LoggerFactory.getLogger(getClass)
  val esBaseUrl = System.getProperty("ES_BASE_URL", "https://internal-atldev3.imanagelabs.com:9953")
  val esUser = System.getProperty("ES_USER", "healthcheck")
  val esSecret = System.getProperty("ES_SECRET", "healthchek")
  val podName = System.getProperty("POD_NAME", "dev3pod2")
  val searchPath = "/dm." + podName + ".av.r/_search"
  val custId = System.getProperty("CUSTID", "516")
  val libId = System.getProperty("LIBID", "888")
  val virtualUsers = 40
  val scenarioRepeatCount = 250
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(esBaseUrl)
    .basicAuth(esUser, esSecret)
    .acceptHeader("application/json, text/plain, */*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .doNotTrackHeader("1")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:74.0) Gecko/20100101 Firefox/74.0")
  val headers = Map(
    "Content-Type" -> "application/json;charset=utf-8",
    "TE" -> "Trailers")
  val custIdFeeder = Array(Map("custId" -> custId)).circular
  val libIdFeeder = Array(Map("libId" -> libId)).circular
  val feeder1 = csv("com/imanage/stratus/elasticsearch/feeder/letterplus2.csv").random
  logger.info("ES Base URL: " + esBaseUrl)
  logger.info("ES user: " + esUser)
  logger.info("ES pod name: " + podName)
  logger.info("Cust id: " + custId)
  logger.info("ES Lib Id: " + libId)
  logger.info("Virtual users: " + virtualUsers)
  logger.info("Test scenario per user : " + scenarioRepeatCount)
  val scn = scenario("ElasticQueryDirect")
    .repeat(scenarioRepeatCount) {
      exec().feed(feeder1)
        .feed(custIdFeeder)
        .feed(libIdFeeder)
        .exec(http("single-term-trailing-wildcard-search")
          .post(searchPath)
          .headers(headers)
          .body(ElFileBody("com/imanage/stratus/elasticsearch/query/SingleTermTrailingWildCardEsQuery.json")))
    }
  setUp(scn.inject(atOnceUsers(virtualUsers))).protocols(httpProtocol)
}