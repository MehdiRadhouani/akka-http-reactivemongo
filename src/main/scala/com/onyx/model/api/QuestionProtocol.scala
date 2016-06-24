package com.onyx.model.api

import com.onyx.model.persistence.QuizEntity
import org.json4s.DefaultFormats

object QuestionProtocol {

  case class Question(id: String, question: String)

  case class Answer(answer: String)
  
  /* messages */
  case object QuestionNotFound

  case object AnswerNotFound

  /* implicit conversions */
  
  implicit def toQuestion(quizEntity: QuizEntity): Question = Question(id = quizEntity.id.stringify, question = quizEntity.question)

  implicit def toAnswer(quizEntity: QuizEntity): Answer = Answer(answer = quizEntity.correctAnswer)
}
