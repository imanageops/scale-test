package com.imanage.stratus

import com.imanage.stratus.MyJsonProtocol.LoginResponseJsonFormat
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

class LoginResponse(val access_token: String)

object MyJsonProtocol extends DefaultJsonProtocol {

  implicit object LoginResponseJsonFormat extends RootJsonFormat[LoginResponse] {
    def write(r: LoginResponse) = JsObject(
      "access_token" -> JsString(r.access_token),
    )

    def read(value: JsValue) = {
      value.asJsObject.getFields("access_token") match {
        case Seq(JsString(access_token)) =>
          new LoginResponse(access_token = access_token)
        case _ => throw new DeserializationException("access_token expected")
      }
    }
  }

}

object authToken {

  def authenticate: (String) => String = (apiBaseUri: String) => log_in(apiBaseUri)

  def log_in: (String) => String = (apiBaseUri: String) => {
    val email = "scaleadmin1@scaletest1.com"
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
    val loginRequest = post(apiBaseUri + "/auth/oauth2/token")
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

}
