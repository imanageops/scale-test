package com.imanage.stratus

import com.imanage.stratus.MyJsonProtocol.LoginResponseJsonFormat
import io.gatling.core.Predef._
import io.gatling.http.Predef.{http, _}
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.request.builder.HttpRequestBuilder.toActionBuilder
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity
import org.apache.hc.client5.http.impl.classic.{CloseableHttpClient, HttpClients}
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder.post
import org.apache.hc.core5.http.message.BasicNameValuePair
import org.apache.hc.core5.http.{HttpStatus, NameValuePair}
import spray.json._

import java.nio.charset.StandardCharsets
import java.util
import scala.util.{Success, Try, Using}


class AccusoftPreviewSimulation extends Simulation {
  private def log_in(): String = {
    val email = "jim@ajubalaw.com"
    val password = "password"

    val httpclient: CloseableHttpClient = HttpClients.createDefault

    val loginFormParams = new util.ArrayList[NameValuePair]
    loginFormParams.add(new BasicNameValuePair("username", s"${email}"))
    loginFormParams.add(new BasicNameValuePair("email", s"${email}"))
    loginFormParams.add(new BasicNameValuePair("password", s"${password}"))
    loginFormParams.add(new BasicNameValuePair("grant_type", "password"))
    loginFormParams.add(new BasicNameValuePair("client_secret", "49e6d46f-f811-49ab-9437-0553806102b2"))
    loginFormParams.add(new BasicNameValuePair("client_id", "web"))
    loginFormParams.add(new BasicNameValuePair("scope", "user"))

    val loginEntity = new UrlEncodedFormEntity(loginFormParams, StandardCharsets.UTF_8)
    val loginRequest = post(baseUri + "/auth/oauth2/token")
      .setEntity(loginEntity)
      .build()

    val xAuthToken: Try[String] = Using(httpclient.execute(loginRequest)) { response =>
      //    EntityUtils toString(response.getEntity.getContent, StandardCharsets.UTF_8)
      if (response.getCode == HttpStatus.SC_OK) {
        val str = EntityUtils.toString(response.getEntity)
        str.parseJson.convertTo[LoginResponse].access_token
      } else {
        null
      }
    }

    if (xAuthToken.isSuccess)
      xAuthToken.asInstanceOf[Success[String]].value
    else {
      null
    }
  }

  //    val baseUri = "https://frontend.service.imanagecloud.com"
  val baseUri = "https://ajubalaw.atldev1.imanagelabs.com"
  //  val baseUri = "https://atldev1.imanagelabs.com"

  // https://atldev1.imanagelabs.com/work/api

  val xAuthToken: String = log_in()

  if (xAuthToken == null) {
    printf("Failed to log in to %s%n", baseUri)
    System.exit(1)
  }

  printf("Using token %s%n", xAuthToken)

  val headers = Map(
    "X-Auth-Token" -> s"${xAuthToken}",
  )


  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(baseUri)
    // entry for atldev4
    //    .hostNameAliases(Map("atldev1.imanagelabs.com" -> List("20.84.176.25")))
    .headers(headers)
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36")


  //  # NT-29572-156231_FW Follow up on Mobility Issues
  //  # 100004220418  11.0 MB
  //
  //  # NT-26080-72043_DST Issue at Paul Hastings
  //  # 100004220419  6.1 MB
  //
  //  # NT-25055-71188_RE___REG_110070179939777__Prem_Outlook_2007_Sporadic_slowness_or_pausing__when_the_automatic__spellcheck_on_the_message_send
  //  # 100004220420  1.0 MB
  //
  //  # NT-21432-68095_Support for large files (double nested attachments)
  //  # 100004220421  121.24 KB
  //
  //  # NT-17279-64214_RE Action Items from yesterday's Conference Call
  //  # 100004220422.1 1.7 MB
  private val local_msg = "100004220422"

  private val docnum_11MB_msg_14_16MB_eml = "100004220418"
  private val docnum_6_1MB_msg_2_88KB_eml = "100004220419"
  private val docnum_1MB_msg = "100004220420"
  private val docnum_121_24KB_msg_120_85KB_eml = "100004220421"
  private val docnum_1_7MB_msg_2_17MB_eml = "100004220422"

  private val inline_png_18_4MB_23_74MB_eml = "100004255911"
  private val inline_png_24MB_1_05KB_eml = "100004255912"
  private val inline_png_5_7MB_39_09KB_eml = "100004255913"

  private val docnum_5MB_msg_6_88MB_eml = "100004255902"
  private val docnum_10MB_msg_13_72MB_eml = "100004255899"
  private val docnum_15MB_msg_20_56MB_eml = "100004255901"
  private val docnum_20MB_msg_27_41MB_eml = "100004255904"
  private val docnum_25MB_msg = "100004255905"
  private val docnum_30MB_msg = "100004255903"
  private val docnum_35MB_msg = "100004255900"

  //  private val documentId = "ACTIVE!100004220421.1"
  //  private val documentName = "Draft Proposed Findings of Fact.msg"
  //  private val split: Array[String] = documentName.split(".+\\.", 2)
  //  private val fileExtension = if (split.length == 2) split.apply(1) else split.apply(0)

  private val feeder = Array(
    Map("documentName" -> "LargeFile", "documentId" -> "ACTIVE!100012343266.1", "pageCount" -> 379, "fileExtension" -> "pdf"),
    Map("documentName" -> "48_Laws_of_Power_300dpi", "documentId" -> "ACTIVE!100012343261.1", "pageCount" -> 10, "fileExtension" -> "pdf"),
    Map("documentName" -> "word.ebola.portuguese", "documentId" -> "ACTIVE!100012343268.1", "pageCount" -> 3, "fileExtension" -> "doc"),
  ).random

  private val requestBody = StringBody(
    s"""{
           "source": {
             "type": "document",
             "fileName": "$${documentId}",
             "displayName": "$${documentName}",
             "fileExtension": "$${fileExtension}"
           },
           "countOfInitialPages": 3
         }""".stripMargin)

  //  val page = 0
  private val pageRendering = http("Get Page ${pageNum} Rendering")
    .get(StringBody("/work/web/pas/Page/q/${pageNum}?DocumentID=u${viewingSessionId}&Scale=1&ContentType=svgb"))
    .check(status.is(200))
  private val pageText = http("Get Page ${pageNum} Text")
    .get(StringBody("/work/web/pas/Document/q/${pageNum}-${pageNum}/Text?DocumentID=u${viewingSessionId}"))
    .check(status.is(200))
  private val documentAttachments = http("Get Attachments")
    .get(StringBody("/work/web/pas/ViewingSession/u${viewingSessionId}/Attachments"))
    .check(status.is(200))
    .check(jsonPath("$..status").saveAs("attachmentsStatus"))
  private val imageStampList = http("ImageStampList")
    .get("/work/web/pas/ImageStampList")
    .check(status.is(200))

  val scn = scenario("Preview with Accusoft")
    .feed(feeder)
    .group("Preview first 3 pages") {
      exec(http("Create ViewingSession")
        .post("/work/web/pas/ViewingSession")
        .header("Content-Type", "application/json")
        .body(requestBody)
        .check(status.is(200))
        .check(jsonPath("$..viewingSessionId").saveAs("viewingSessionId"))
      )
        .exec(_.set("pageNum", "0"))
        .exec(
          Accusoft.getPageRendering(0)
            .resources(
              // yes, we request the rendering a second time
              Accusoft.getPageRendering(0),
              http("Get Document Attributes")
                .get(StringBody("/work/web/pas/Document/q/Attributes?DocumentID=u${viewingSessionId}&DesiredPageCountConfidence=50"))
                .check(status.is(200))
                .check(jsonPath("$..pageCount").saveAs("pageCount")),
              // TODO request pageRendering pages 1-min(3, pageCount)
              http("Get License")
                .get("/work/web/pas/License/ClientViewer?v=8.4&iv=69D6434F-224A434B-17C99D47-32CB27B5&p=24")
                .check(status.is(200)),
              documentAttachments,
              imageStampList,
              pageText, // TODO request pageText pages 1-min(3, pageCount)
            )
        )
    }.exec(http("Delete Viewing Session").delete("/work/web/pas/ViewingSession/u${viewingSessionId}"))

  //  .exec(http("Create ViewingSession")
  //      .post("/work/web/pas/ViewingSession")
  //      .header("Content-Type", "application/json")
  //      .body(requestBody)
  //      .check(status.is(200))
  //      .check(jsonPath("$..viewingSessionId").saveAs("viewingSessionId"))
  //    )
  //    .exec(_.set("pageNum", "0"))
  //    .exec(
  //      Accusoft.getPageRendering(0)
  //        .resources(
  //          // yes, we request the rendering a second time
  //          Accusoft.getPageRendering(0),
  //          http("Get Document Attributes")
  //            .get(StringBody("/work/web/pas/Document/q/Attributes?DocumentID=u${viewingSessionId}&DesiredPageCountConfidence=50"))
  //            .check(status.is(200))
  //            .check(jsonPath("$..pageCount").saveAs("pageCount")),
  //          // TODO request pageRendering pages 1-min(3, pageCount)
  //          http("Get License")
  //            .get("/work/web/pas/License/ClientViewer?v=8.4&iv=69D6434F-224A434B-17C99D47-32CB27B5&p=24")
  //            .check(status.is(200)),
  //          documentAttachments,
  //          imageStampList,
  //          pageText, // TODO request pageText pages 1-min(3, pageCount)
  //        )
  //    ).exec(http("Delete Viewing Session").delete("/work/web/pas/ViewingSession/u${viewingSessionId}"))

  //        .resources(
  //          repeat(session => session("pageCount").as[Int], "pageNum") {
  //            pageRendering
  //          }
  ////          documentAttachments,
  ////          imageStampList,
  //        )  //    .exec(http("Delete ViewingSession")
  //      .delete("/work/web/pas/ViewingSession/u${viewingSessionId}")
  //    )


  setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)

  //    setUp(scn.inject(constantConcurrentUsers(5)
  //      during (30))).protocols(httpProtocol)
}
