package com.onyx.dao

import com.onyx.model.api.QuizProtocol.{QuizCreated, QuizDeleted, QuizUpdated}
import com.onyx.model.persistence.QuizEntity
import reactivemongo.api.QueryOpts
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.core.commands.Count

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

trait QuizDao extends MongoDao {
  
  val collection = db[BSONCollection]("quizzes")

  def save(quizEntity: QuizEntity): Future[Option[QuizCreated]] = collection.save(quizEntity)
    .map(_ => Some(QuizCreated(quizEntity.id.stringify)))

  def update(id: String, quizEntity: QuizEntity): Future[Option[QuizUpdated]] = collection.update(queryById(id), quizEntity)
    .map(_ => Some(QuizUpdated(id)))
  
  def findById(id: String) =
    collection.find(queryById(id)).one[QuizEntity]
  
  def findOne = {
    val futureCount = db.command(Count(collection.name))
    futureCount.flatMap { count =>
      val skip = Random.nextInt(count)
      collection.find(emptyQuery).options(QueryOpts(skipN = skip)).one[QuizEntity]
    }
  }
  
  def deleteById(id: String) = collection.remove(queryById(id)).map(_ => QuizDeleted)

  private def queryById(id: String) = BSONDocument("_id" -> BSONObjectID(id))

  private def emptyQuery = BSONDocument()
}
