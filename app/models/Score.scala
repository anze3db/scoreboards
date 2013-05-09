package models

import java.security.MessageDigest

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.ValidBSONType.BasicDBList
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._
import com.novus.salat.annotations._

import com.novus.salat.{ TypeHintFrequency, StringTypeHintStrategy, Context }
import play.api.Play
import play.api.Play.current

import mongoContext._
import scala.reflect._


case class Score(
  @Key("_id") id : ObjectId,
  user: ObjectId,
  username: String,
  score: Int
) extends Model

object Scores extends Models[Score] {
  
  override val table = db("scores")
  
  def newScore(id: String, username: String, score: Int) = 
    Scores.create(new Score(new ObjectId, new ObjectId(id), username, score))
  
  def allFromUser()(implicit user: Option[User]) = user match {
    case Some(u) => table.find(MongoDBObject("user" -> u.id)).map(grater[Score].asObject(_)).toList.sortBy(s => s.score).reverse
    case None => List[Score]()
  }
}
