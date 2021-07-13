package com.imanage.stratus

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RealUserPreviewSimulation extends Simulation {

	val httpProtocol = http
		.baseUrl("https://atldev1.imanagelabs.com")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US")
		.doNotTrackHeader("1")
		.userAgentHeader("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:86.0) Gecko/20100101 Firefox/86.0")

	val headers_0 = Map(
		"Accept" -> "application/json, text/plain, */*",
		"TE" -> "Trailers",
		"X-XSRF-TOKEN" -> "7875e2180845fd2158f79ca8e90cb9dc8ae3f03f")

	val headers_7 = Map("X-Requested-With" -> "XMLHttpRequest")

	val headers_17 = Map(
		"Content-Type" -> "application/json",
		"Origin" -> "https://atldev1.imanagelabs.com",
		"TE" -> "Trailers",
		"X-Requested-With" -> "XMLHttpRequest",
		"X-XSRF-TOKEN" -> "7875e2180845fd2158f79ca8e90cb9dc8ae3f03f")

	val headers_18 = Map(
		"TE" -> "Trailers",
		"X-XSRF-TOKEN" -> "7875e2180845fd2158f79ca8e90cb9dc8ae3f03f")

	val headers_19 = Map(
		"If-None-Match" -> "uaTVnMiiBgeAbEIakD2lrHr3xbQYYO12lKrQpLkKab5gIlgxC8jGtdclocr1HbV-JcqcUNuvwPU3wx2bC-hNL7w-pagerender-0-1.00-svgb",
		"TE" -> "Trailers",
		"X-XSRF-TOKEN" -> "7875e2180845fd2158f79ca8e90cb9dc8ae3f03f")

	val headers_20 = Map(
		"Content-Type" -> "application/json",
		"TE" -> "Trailers",
		"X-XSRF-TOKEN" -> "7875e2180845fd2158f79ca8e90cb9dc8ae3f03f")

	val headers_25 = Map(
		"Accept" -> "application/json",
		"TE" -> "Trailers",
		"X-XSRF-TOKEN" -> "7875e2180845fd2158f79ca8e90cb9dc8ae3f03f")



	val scn = scenario("RealUserPreviewSimulation")
		.exec(http("request_0")
			.get("/work/web/api/v2/customers/121/libraries/ACTIVE/documents/ACTIVE!100004256020.1")
			.headers(headers_0)
			.resources(http("request_1")
			.get("/work/web/api/v2/customers/121/libraries/ACTIVE/documents/ACTIVE!100004256020.1/path")
			.headers(headers_0),
            http("request_2")
			.get("/work/web/api/v2/customers/121/libraries/ACTIVE/documents/ACTIVE!100004256020.1/versions?limit=25&offset=0&total=true")
			.headers(headers_0),
            http("request_3")
			.get("/work/web/api/v2/customers/121/libraries/ACTIVE/documents/ACTIVE!100004256020.1/operations")
			.headers(headers_0),
            http("request_4")
			.get("/work/web/api/v2/customers/121/libraries/ACTIVE/documents/ACTIVE!100004256020.1/name-value-pairs")
			.headers(headers_0),
            http("request_5")
			.get("/work/web/api/v2/customers/121/libraries/ACTIVE/documents/ACTIVE!100004256020.1?include_operations=true&is_latest=true&profile_check=true")
			.headers(headers_0),
            http("request_6")
			.get("/work/web/api/v2/customers/121/libraries/ACTIVE/workspaces/ACTIVE!314640")
			.headers(headers_0),
            http("request_7")
			.get("/work/web/preview/config.json?rel=1614712866293")
			.headers(headers_7),
            http("request_8")
			.get("/work/web/preview/viewer-assets/templates/commentTemplate.html?rel=1614712866293")
			.headers(headers_7),
            http("request_9")
			.get("/work/web/preview/viewer-assets/templates/contextMenuTemplate.html?rel=1614712866293")
			.headers(headers_7),
            http("request_10")
			.get("/work/web/preview/viewer-assets/templates/copyOverlayTemplate.html?rel=1614712866293")
			.headers(headers_7),
            http("request_11")
			.get("/work/web/preview/viewer-assets/templates/hyperlinkMenuTemplate.html?rel=1614712866293")
			.headers(headers_7),
            http("request_12")
			.get("/work/web/preview/viewer-assets/templates/redactionReasonTemplate.html?rel=1614712866293")
			.headers(headers_7),
            http("request_13")
			.get("/work/web/preview/viewer-assets/templates/viewerTemplate.html?rel=1614712866293")
			.headers(headers_7),
            http("request_14")
			.get("/work/web/preview/viewer-assets/languages/en.json?rel=1614712866293")
			.headers(headers_7),
            http("request_15")
			.get("/work/web/preview/redactionReason.en.json?rel=1614712866293")
			.headers(headers_7),
            http("request_16")
			.get("/work/web/preview/predefinedSearch.en.json?rel=1614712866293")
			.headers(headers_7),
            http("request_17")
			.post("/work/web/pas/ViewingSession")
			.headers(headers_17)
			.body(RawFileBody("com/imanage/stratus/realuserpreviewsimulation/0017_request.json")),
            http("request_18")
			.get("/work/web/pas/Page/q/0?DocumentID=uaTVnMiiBgeAbEIakD2lrHr3xbQYYO12lKrQpLkKab5gIlgxC8jGtdclocr1HbV-JcqcUNuvwPU3wx2bC-hNL7w&Scale=1&ContentType=svgb")
			.headers(headers_18),
            http("request_19")
			.get("/work/web/pas/Page/q/0?DocumentID=uaTVnMiiBgeAbEIakD2lrHr3xbQYYO12lKrQpLkKab5gIlgxC8jGtdclocr1HbV-JcqcUNuvwPU3wx2bC-hNL7w&Scale=1&ContentType=svgb")
			.headers(headers_19),
            http("request_20")
			.get("/work/web/pas/License/ClientViewer?v=8.4&iv=B998B55C-BDA401A8-37CE713D-D2060DA2&p=24")
			.headers(headers_20),
            http("request_21")
			.get("/work/web/pas/Document/q/Attributes?DocumentID=uaTVnMiiBgeAbEIakD2lrHr3xbQYYO12lKrQpLkKab5gIlgxC8jGtdclocr1HbV-JcqcUNuvwPU3wx2bC-hNL7w&DesiredPageCountConfidence=50")
			.headers(headers_20),
            http("request_22")
			.get("/work/web/pas/Page/q/1?DocumentID=uaTVnMiiBgeAbEIakD2lrHr3xbQYYO12lKrQpLkKab5gIlgxC8jGtdclocr1HbV-JcqcUNuvwPU3wx2bC-hNL7w&Scale=1&ContentType=svgb")
			.headers(headers_18),
            http("request_23")
			.get("/work/web/pas/ImageStampList")
			.headers(headers_18),
            http("request_24")
			.get("/work/web/pas/ViewingSession/uaTVnMiiBgeAbEIakD2lrHr3xbQYYO12lKrQpLkKab5gIlgxC8jGtdclocr1HbV-JcqcUNuvwPU3wx2bC-hNL7w/Attachments")
			.headers(headers_18),
            http("request_25")
			.get("/work/web/pas/Document/q/0-0/Text?DocumentID=uaTVnMiiBgeAbEIakD2lrHr3xbQYYO12lKrQpLkKab5gIlgxC8jGtdclocr1HbV-JcqcUNuvwPU3wx2bC-hNL7w")
			.headers(headers_25),
            http("request_26")
			.get("/work/web/pas/Document/q/1-1/Text?DocumentID=uaTVnMiiBgeAbEIakD2lrHr3xbQYYO12lKrQpLkKab5gIlgxC8jGtdclocr1HbV-JcqcUNuvwPU3wx2bC-hNL7w")
			.headers(headers_25)))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}