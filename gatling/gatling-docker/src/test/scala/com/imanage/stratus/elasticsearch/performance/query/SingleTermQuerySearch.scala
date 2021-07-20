package com.imanage.stratus.elasticsearch.performance.query

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import org.slf4j.LoggerFactory

/*
* Test scenario :
* Single term ES query, 40 users, 25 queries each
* */
class SingleTermQuerySearch extends Simulation {
  private val logger = LoggerFactory.getLogger(getClass)
  val esBaseUrl = System.getenv().getOrDefault("ES_BASE_URL", "https://internal-atldev3.imanagelabs.com:9953")
  val esUser = System.getenv().getOrDefault("ES_USER", "healthcheck")
  val esSecret = System.getenv().getOrDefault("ES_SECRET", "healthchek")
  val podName = System.getenv().getOrDefault("POD_NAME", "dev3pod2")
  val searchPath = "/dm." + podName + ".av.r/_search"
  val custId = System.getenv().getOrDefault("CUSTOMER_ID", "516")
  val libId = System.getenv().getOrDefault("LIBRARY_ID", "888")
  val virtualUsers = Integer.parseInt(System.getenv().getOrDefault("VIRTUAL_USERS", "1"))
  val scenarioRepeatCount = Integer.parseInt(System.getenv().getOrDefault("SCENARIO_REPEAT_COUNT", "1"))
  val dataFile = Integer.parseInt(System.getenv().getOrDefault("SCENARIO_REPEAT_COUNT", "1"))
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
  val feeder1 = csv("com/imanage/stratus/elasticsearch/feeder/dict1.csv").random
  logger.info("ES Base URL: " + esBaseUrl)
  logger.info("ES user: " + esUser)
  logger.info("ES pod name: " + podName)
  logger.info("Cust id: " + custId)
  logger.info("Lib Id: " + libId)
  logger.info("Virtual users: " + virtualUsers)
  logger.info("Test scenario per user : " + scenarioRepeatCount)
  val scn = scenario("ElasticQueryDirect")
    .repeat(scenarioRepeatCount) {
      exec().feed(feeder1)
        .feed(custIdFeeder)
        .feed(libIdFeeder)
        .exec(http("single-term-search")
          .post(searchPath)
          .headers(headers)
          .body(ElFileBody("com/imanage/stratus/elasticsearch/query/SingleTermEsQuery.json")))
    }
  setUp(scn.inject(atOnceUsers(virtualUsers))).protocols(httpProtocol)
}