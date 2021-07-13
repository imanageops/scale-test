package com.imanage.stratus

import io.gatling.core.Predef._
import io.gatling.http.Predef._ // used for specifying duration unit, eg "5 second"

object Accusoft {

  def getPageRendering(page: Int) = http("Get Page " + page + " Rendering")
    .get(StringBody("/work/web/pas/Page/q/" + page + "?DocumentID=u${viewingSessionId}&Scale=1&ContentType=svgb"))
    .check(status.is(200))

  private val pageRendering = http("Get Page ${pageNum} Rendering")
    .get(StringBody("/work/web/pas/Page/q/${pageNum}?DocumentID=u${viewingSessionId}&Scale=1&ContentType=svgb"))
    .check(status.is(200))

  //  def getFirstPagesRenderings() = http("Get Page ${pageNum} Rendering")
  //    .get(StringBody("/work/web/pas/Page/q/${pageNum}?DocumentID=u${viewingSessionId}&Scale=1&ContentType=svgb"))
  //    .check(status.is(200))
  //    .resources()
  def getFirstPagesRenderings() = exec(session => {
    repeat(Math.min(session.attributes.get("pageCount").asInstanceOf[Int], 3), "pageNum") {
      exec(pageRendering)
    }
    session
  })

  def gotoPage(page: Int) =
    exec(
      http("Page " + page)
        .get("/computers?p=" + page)
    ).pause(1)

  val browse = exec(gotoPage(0), gotoPage(1), gotoPage(2), gotoPage(3), gotoPage(4))

}

object Search {

  val search = exec(
    http("Home") // let's give proper names, as they are displayed in the reports
      .get("/")
  ).pause(7)
    .exec(
      http("Search")
        .get("/computers?f=macbook")
    )
    .pause(2)
    .exec(
      http("Select")
        .get("/computers/6")
    )
    .pause(3)
}
