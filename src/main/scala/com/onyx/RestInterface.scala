package com.onyx

import akka.http.scaladsl.server.Route
import com.onyx.resources.QuestionResource
import com.onyx.services.QuestionService

import scala.concurrent.ExecutionContext

trait RestInterface extends Resources {

  implicit def executionContext: ExecutionContext

  lazy val questionService = new QuestionService

  val routes: Route = questionRoutes

}

trait Resources extends QuestionResource

