package com.onyx.model.api

object QuizProtocol {

  case class Quiz(question: String, correctAnswer: String)
  
  /* messages */

  case class QuizCreated(id: String)

  case class QuizUpdated(id: String)

  case object QuizDeleted

}
