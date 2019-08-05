package com.ccousseran.gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://computer-database.gatling.io") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn = scenario("Basic Simulation") // A scenario is a chain of requests and pauses
    .exec(http("Get Computers").get("/computers"))
    .pause(5)
    .repeat(5, "n") {
      exec(http("Page ${n}")
        .get("/computers?p=${n}"))
        .pause(1)
    }
    .group("Add Computer") {
      exec(http("Add Computer").post("/computers")
        .formParam("name", "Beautiful Computer")
        .formParam("introduced", "2012-05-30")
        .formParam("discontinued", "")
        .formParam("company", "37"))
    }

  setUp(scn.inject(rampUsers(2000) during 70).protocols(httpProtocol))
    .assertions(global.responseTime.max.lt(1000))
}
