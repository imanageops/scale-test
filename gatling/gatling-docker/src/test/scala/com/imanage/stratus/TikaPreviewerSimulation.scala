package com.imanage.stratus

import com.imanage.stratus.MyJsonProtocol.LoginResponseJsonFormat
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
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

class TikaPreviewSimulation extends Simulation {
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

  //  val baseUri = "https://frontend.service.imanagecloud.com"
  val baseUri = "https://atldev1.imanagelabs.com"

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
    .inferHtmlResources()
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
  val scn = scenario("Preview MSG")
    .exec(http("Preview docnum_121_24KB_msg_120_85KB_eml MSG")
      .get("/work/web/api/v2/customers/121/libraries/ACTIVE/email/ACTIVE!" + docnum_121_24KB_msg_120_85KB_eml + ".1/preview")
      .headers(headers)
      .check(status.is(200))
      .check(bodyLength.gte(10_000))
    )

  setUp(scn.inject(constantConcurrentUsers(10)
    during (180))).protocols(httpProtocol)
}
