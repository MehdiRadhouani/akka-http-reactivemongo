package com.onyx.services

import com.onyx.dao.QuizDao
import com.onyx.model.api.QuestionProtocol.{QuestionNotFound, _}
import com.onyx.model.api.QuizProtocol.{QuizCreated, QuizDeleted, QuizUpdated}
import com.onyx.model.persistence.QuizEntity
import com.typesafe.scalalogging.LazyLogging
import reactivemongo.bson.BSONObjectID

import scala.concurrent.{ExecutionContext, Future}

class QuestionService(implicit val executionContext: ExecutionContext) extends QuizDao with LazyLogging {

  def createQuiz(quizEntity: QuizEntity): Future[Option[QuizCreated]] = {
    logger.info(s"[POST] createQuiz by quizEntity $quizEntity")
    save(quizEntity)
  }

  def getQuestion(maybeId: Option[String] = None) = {
    val someId = maybeId.getOrElse(None)
    logger.info(s"[GET] getQuestion by id $someId")
    def extractQuestion(maybeQuiz: Option[QuizEntity]) = maybeQuiz match {
      case Some(quizEntity) => toQuestion(quizEntity)
      case _ => QuestionNotFound
    }
    tryGetQuiz(maybeId).map(extractQuestion)
  }

  def getAnswer(maybeId: Option[String] = None) = {
    val someId = maybeId.getOrElse(None)
    logger.info(s"[GET] getAnswer by id $someId")
    def extractAnswer(maybeQuiz: Option[QuizEntity]) = maybeQuiz match {
      case Some(quizEntity) => toAnswer(quizEntity)
      case _ => QuestionNotFound
    }
    tryGetQuiz(maybeId).map(extractAnswer)
  }

  def updateQuiz(id: String, quizEntity: QuizEntity): Future[Option[QuizUpdated]] = {
    logger.info(s"[PUT] updateQuiz by id $id and quizEntity $quizEntity")
    tryGetQuiz(Some(id)).flatMap {
      case None => Future {
        None
      } // No question found, nothing to update
      case Some(question) => update(id, quizEntity.copy(id = BSONObjectID(id)))
    }
  }

  def deleteQuiz(id: String): Future[QuizDeleted.type] = {
    logger.info(s"[DELETE] deleteQuiz by id $id")
    deleteById(id)
  }

  private def tryGetQuiz(maybeId: Option[String]): Future[Option[QuizEntity]] = maybeId match {
    case Some(id) => findById(id)
    case _ => findOne
  }
}

