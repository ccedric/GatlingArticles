package com.ccousseran.gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BasicSimulation extends Simulation {

  val httpConf = http
    .baseURL("http://computer-database.gatling.io") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers

  val scn = scenario("BasicSimulation")
    .exec(http("Get Computers").get("/computers"))
    .pause(5)
    .exec(http("Add Computer").post("/computers")
      .formParam("name", "Beautiful Computer")
      .formParam("introduced", "2012-05-30")
      .formParam("discontinued", "")
      .formParam("company", "37"))

  setUp(scn.inject(atOnceUsers(100)).protocols(httpConf))
    .assertions(global.responseTime.max.lt(2000))
}