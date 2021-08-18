package com.imanage.stratus

import com.imanage.stratus.MyJsonProtocol.LoginResponseJsonFormat
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity
import org.apache.hc.client5.http.impl.classic.{CloseableHttpClient, HttpClients}
import org.apache.hc.core5.http.io.entity.{EntityUtils, StringEntity}
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder.post
import org.apache.hc.core5.http.message.BasicNameValuePair
import org.apache.hc.core5.http.{ContentType, HttpStatus, NameValuePair}
import spray.json._

import java.net.HttpCookie
import java.nio.charset.StandardCharsets
import java.security.cert.X509Certificate
import java.util
import javax.net.ssl.{HostnameVerifier, SSLSession}
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

  def authenticate: (String, String, String) => String = (apiBaseUri: String, userEmail: String, userPassword: String) => log_in(apiBaseUri, userEmail, userPassword)

  def log_in: (String, String, String) => String = (apiBaseUri: String, email: String, password: String) => {
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


  def system_user_log_in: (String, String, String) => String = (loginUri: String, email: String, password: String) => {
    import org.apache.hc.core5.ssl.{SSLContexts, TrustStrategy}
    val sslcontext = SSLContexts.custom.loadTrustMaterial(new TrustStrategy() {
      override def isTrusted(chain: Array[X509Certificate], authType: String): Boolean = true
    }).build

    val hnv: HostnameVerifier = new HostnameVerifier() {
      override def verify(hostname: String, session: SSLSession): Boolean = true
    }

    import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder
    val sslSocketFactory = SSLConnectionSocketFactoryBuilder.create.setSslContext(sslcontext).setHostnameVerifier(hnv).build
    import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
    val cm = PoolingHttpClientConnectionManagerBuilder.create.setSSLSocketFactory(sslSocketFactory).build

    val httpclient: CloseableHttpClient = HttpClients.custom().setConnectionManager(cm).build

    val loginEntity = new StringEntity(s"""{"email":"${email}","password":"${password}"}""", ContentType.APPLICATION_JSON)
    val requestBuilder = post(loginUri)
      .setEntity(loginEntity)

    if (loginUri.contains("://localhost")) {
      // most likely using port forwarding, something like
      // kubectl port-forward svc/atldev4-frontend 28443:443
      requestBuilder
        .setHeader("Host", "frontend.service.imanagecloud.com")
    }

    val loginRequest = requestBuilder
      .build()

    val xAuthToken: Try[String] = Using(httpclient.execute(loginRequest)) { response =>
      //    EntityUtils toString(response.getEntity.getContent, StandardCharsets.UTF_8)
      if (response.getCode == HttpStatus.SC_OK) {
        EntityUtils.toString(response.getEntity)
        HttpCookie.parse(response.getHeader("Set-Cookie").getValue).get(0).getValue
      } else {
        println("got response " + response.getCode + " from " + loginRequest.getMethod + " " + loginUri + " / body:")
        response.getEntity.writeTo(System.out)
        throw new Exception(response.getReasonPhrase)
      }
    }

    if (xAuthToken.isSuccess)
      xAuthToken.asInstanceOf[Success[String]].value
    else {
      xAuthToken.failed.get.printStackTrace
      null
    }
  }

}
