package com.imanage.stratus

import com.imanage.stratus.Document.uploadedDocnums
import io.gatling.core.Predef.{StringBody, jsonPath, _}
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef.{http, status, _}

import scala.concurrent.duration.DurationInt

object Elasticsearch {

  val queryElasticsearchForDocnum: (Int, String, String, String) => ChainBuilder = (latestDocNum: Int, elasticsearchBasePath: String, elasticsearchUsername: String, elasticsearchPassword: String) => exec(session => {
    session.set("latestDocNum", latestDocNum)
  }).asInstanceOf[ChainBuilder]
    .exec(http("Query Elasticsearch")
      .post(elasticsearchBasePath + "/_search")
      .body(StringBody(s"""{"size":1,"query":{"match":{"docNum":{"query":"$${latestDocNum}"}}},"_source":{"includes":["docNum"]}}"""))
      .header("content-type", "application/json")
      .basicAuth(elasticsearchUsername, elasticsearchPassword)
      .check(status.is(200))
      .check(jsonPath("$.hits.hits[*]._source.docNum").findAll.saveAs("foundDocNums"))
    )
    .exec(session => {
      val latestDocNum = session.attributes.get("latestDocNum").asInstanceOf[Some[Int]].value
      println("uploaded new docnum: " + latestDocNum)
      uploadedDocnums = uploadedDocnums :+ (latestDocNum)
      println("uploadedDocnums: " + uploadedDocnums)
      session
    })

  val queryElasticsearch: (String, String, String) => ChainBuilder = (elasticsearchBasePath: String, elasticsearchUsername: String, elasticsearchPassword: String) =>
    foreach(uploadedDocnums, "docnum") {
      //try 3 times because not all nodes with the shards containing this document necessarily have the document
      tryMax(3) {
        exec(
          http("Query Elasticsearch for all uploaded documents")
            .post(elasticsearchBasePath + "/_search")
            .body(StringBody(s"""{"size":1,"query":{"match":{"docNum":{"query":"$${docnum}"}}},"_source":{"includes":["docNum"]}}"""))
            .header("content-type", "application/json")
            .basicAuth(elasticsearchUsername, elasticsearchPassword)
            .check(status.is(200))
            .check(jsonPath("$.hits.hits[*]._source.docNum").findAll.optional.saveAs("foundDocNums"))).asInstanceOf[ChainBuilder]
      }
    }.asInstanceOf[ChainBuilder]
      .pause(10.seconds)
      .exec(session => {
        if (uploadedDocnums.nonEmpty) {
          println("uploadedDocnums: " + uploadedDocnums)
        }
        session
      })
}
