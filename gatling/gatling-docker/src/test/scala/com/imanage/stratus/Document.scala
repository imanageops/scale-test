package com.imanage.stratus

import com.imanage.stratus.Elasticsearch.queryElasticsearchForDocnum
import io.gatling.core.Predef.{jsonPath, _}
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef.{RawFileBodyPart, http, status, _}
import io.gatling.http.request.BodyPart
import org.apache.commons.io.FilenameUtils

import java.util.concurrent.TimeUnit
import scala.collection.immutable.Range
import scala.concurrent.duration.DurationInt

object Document {

  var uploadedDocnums: Seq[Int] = Range(1091, 1181) concat Range(1411, 2021)

  var xauthToken: Option[String] = None

  def authenticate: (String, String, String) => Unit = (apiBaseUri: String, userEmail: String, userPassword: String) => {
    xauthToken = Option.apply(authToken.log_in(apiBaseUri, userEmail, userPassword))
    headers += ("X-Auth-Token" -> s"${xauthToken.get}")
    xauthToken.get
  }

  private var headers = Map(
    "X-Auth-Token" -> s"${xauthToken}",
  )

  private val fileName = "word.ebola.portuguese.doc"

  private val profileBodyPart: BodyPart = StringBodyPart(
    s"""{"doc_profile":{"name":"${FilenameUtils.getBaseName(fileName)}","extension":"${FilenameUtils.getExtension(fileName)}","file_edit_date":"2021-04-26T11:10:32-05:00","file_create_date":"2021-04-26T11:10:32-05:00","size":38400},"warnings_for_required_and_disabled_fields":true}
                                                   """.stripMargin)
  private val fileBodyPart: BodyPart = RawFileBodyPart("file", fileName)
    .contentType("application/msword")
    .fileName(fileName)

  val upload: (String, String, String, Int) => ChainBuilder = (elasticsearchBasePath: String, elasticsearchUsername: String, elasticsearchPassword: String, sleepTimeUntilElasticsearchQuery: Int) => exec(http("Upload document")
    .post("/work/web/api/v2/customers/516/libraries/ATLDEV4POD2/folders/ATLDEV4POD2!5/documents")
    .headers(headers)
    .bodyParts(profileBodyPart, fileBodyPart).asMultipartForm
    .check(status.is(201))
    .check(jsonPath("$.data.document_number").ofType[Int].saveAs("latestDocNum"))
  ).asInstanceOf[ChainBuilder]
    .pause(s"${sleepTimeUntilElasticsearchQuery}", TimeUnit.SECONDS)
    .exec(session => {
      queryElasticsearchForDocnum(session.attributes("latestDocnum").asInstanceOf[Some[Int]].value, elasticsearchBasePath, elasticsearchUsername, elasticsearchPassword)
      session
    })
    .pause(5.seconds)
    .exec(session => {
      val latestDocNum = session.attributes.get("latestDocNum").asInstanceOf[Some[Int]].value
      println("uploaded new docnum: " + latestDocNum)
      uploadedDocnums = uploadedDocnums :+ (latestDocNum)
      println("uploadedDocnums: " + uploadedDocnums)
      session
    })
}
