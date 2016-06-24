package com.onyx.resources

import akka.http.scaladsl.server.Route
import com.onyx.model.persistence.QuizEntity
import com.onyx.routing.MyResource
import com.onyx.services.QuestionService

trait QuestionResource extends MyResource {

  val questionService: QuestionService

  def questionRoutes: Route = pathPrefix("questions") {
    pathEnd {
      post {
        entity(as[QuizEntity]) { quizEntity =>
          completeWithLocationHeader(
            resourceId = questionService.createQuiz(quizEntity),
            ifDefinedStatus = 201, ifEmptyStatus = 409)
        }
      }
    } ~
      path(Segment) { id =>
        get {
          complete(questionService.getQuestion(Some(id)))
        } ~
          put {
            entity(as[QuizEntity]) { update =>
              complete(questionService.updateQuiz(id, update))
            }
          } ~
          delete {
            complete(questionService.deleteQuiz(id))
          }
      }
  } ~ pathPrefix("answers") {
    path(Segment) { id =>
      get {
        complete(questionService.getAnswer(Some(id)))
      }
    }
  }
}

