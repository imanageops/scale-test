package com.imanage.stratus.elasticserch.query;

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
/*
* Test scenario :
* Single term ES query, 40 users, 25 queries each
* */
class SingleTerm_40_25 extends Simulation {

  val httpProtocol:HttpProtocolBuilder = http
    .baseUrl("https://internal.atldev1.imanagelabs.com:9952")
    .acceptHeader("application/json, text/plain, */*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .doNotTrackHeader("1")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:74.0) Gecko/20100101 Firefox/74.0")
  val headers_1 = Map(
    "Content-Type" -> "application/json;charset=utf-8",
    "TE" -> "Trailers")
  val feeder1 = csv("com/imanage/stratus/elasticsearch/performance/query/SingleTerm_40_25.scala").random
  val scn = scenario("ElasticQueryDirect")
    .repeat(2) {
   // .repeat(25) {
      exec().feed(feeder1)
        .exec(http("request_1")
          .post("/dm.dev1pod1.av.045/_search")
          .headers(headers_1)
          .body(ElFileBody("com/imanage/stratus/elasticsearch/query/es_singleterm1.json")))
    }
  //setUp(scn.inject(atOnceUsers(40))).protocols(httpProtocol)
  setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}