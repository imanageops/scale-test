package com.imanage.stratus

import com.imanage.stratus.Document.uploadedDocnums
import com.imanage.stratus.authToken.log_in
import io.gatling.core.Predef._
import io.gatling.http.Predef.{RawFileBodyPart, _}
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.request.BodyPart
import org.apache.commons.io.FilenameUtils

import java.util.concurrent.TimeUnit

class DocumentUploadSimulation extends Simulation {
  private val constant_concurrent_users = Integer.getInteger("constantConcurrentUsers", 1)
  private val test_duration_seconds = Integer.getInteger("testDurationSeconds", 600)

  //  val baseUri = "https://frontend.service.imanagecloud.com"
  private val apiBaseUri = System.getProperty("apiBaseUri", "https://atldev1.imanagelabs.com")
  private val elasticsearchBasePath = System.getProperty("elasticsearchBasePath", "https://es7-atldev4pod2-elasticsearch.service.imanagecloud.com:9950/dm.atldev4pod2.av.046")
  private val elasticsearchUsername = System.getProperty("elasticsearchUsername", "elastic")
  private val elasticsearchPassword = System.getProperty("elasticsearchPassword", "oLgVTL4VfeZKCIXi5Ag3aLVfEFGRx5vt")
  private val sleepTimeUntilElasticsearchQuery = Integer.getInteger("sleepTimeUntilElasticsearchQuery", 30)

  val xAuthToken: String = log_in(apiBaseUri)

  if (xAuthToken == null) {
    printf("Failed to log in to %s%n", apiBaseUri)
    System.exit(1)
  }

  printf("Using token %s%n", xAuthToken)

  val headers = Map(
    "X-Auth-Token" -> s"${xAuthToken}",
  )


  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(apiBaseUri)
    .inferHtmlResources()
    .headers(headers)
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36")

  private val fileName = "word.ebola.portuguese.doc"

  private val profileBodyPart: BodyPart = StringBodyPart(
    s"""{"doc_profile":{"name":"${FilenameUtils.getBaseName(fileName)}","extension":"${FilenameUtils.getExtension(fileName)}","file_edit_date":"2021-04-26T11:10:32-05:00","file_create_date":"2021-04-26T11:10:32-05:00","size":38400},"warnings_for_required_and_disabled_fields":true}
                                                   """.stripMargin)
  private val fileBodyPart: BodyPart = RawFileBodyPart("file", fileName)
    .contentType("application/msword")
    .fileName(fileName)

  val documentUpload = scenario("Upload document and query Elasticsearch")
    .exec(http("Upload document")
      .post("/work/web/api/v2/customers/516/libraries/ATLDEV4POD2/folders/ATLDEV4POD2!5/documents")
      .headers(headers)
      .bodyParts(profileBodyPart, fileBodyPart).asMultipartForm
      .check(status.is(201))
      .check(jsonPath("$.data.document_number").ofType[Int].saveAs("latestDocNum"))
    ).pause(s"${sleepTimeUntilElasticsearchQuery}", TimeUnit.SECONDS)
    .exec(http("Query Elasticsearch")
      .post(elasticsearchBasePath + "/_search")
      .body(StringBody(s"""{"size":1,"query":{"match":{"docNum":{"query":"$${latestDocNum}"}}},"_source":{"includes":["docNum"]}}"""))
      .header("content-type", "application/json")
      .basicAuth(elasticsearchUsername, elasticsearchPassword)
      .check(status.is(200))
      .check(jsonPath("$.hits.hits[*]._source.docNum").findAll.saveAs("foundDocNums"))
    ).exec(session => {
    val latestDocNum = session.attributes.get("latestDocNum").asInstanceOf[Some[Int]].value
    println("uploaded new docnum: " + latestDocNum)
    uploadedDocnums = uploadedDocnums :+ (latestDocNum)
    println("uploadedDocnums: " + uploadedDocnums)
    session
  })

  Document.authenticate(apiBaseUri)

  private val documentUploads = scenario("Upload Documents").exec(Document.upload(elasticsearchBasePath, elasticsearchUsername, elasticsearchPassword, sleepTimeUntilElasticsearchQuery))
  private val elasticsearchQuery = scenario("Query Elasticsearch").exec(Elasticsearch.queryElasticsearch(elasticsearchBasePath, elasticsearchUsername, elasticsearchPassword))

  setUp(documentUploads.inject(constantConcurrentUsers(constant_concurrent_users).during(test_duration_seconds.seconds)).protocols(httpProtocol),
    elasticsearchQuery.inject(constantConcurrentUsers(constant_concurrent_users).during(test_duration_seconds.seconds)
    ).protocols(httpProtocol))
}
