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

case class Score(
  @Key("_id") id: ObjectId = new ObjectId,
  user: ObjectId,
  username: String,
  score: Int
)

object Scores{
  
  val scores = MongoConnection()("scoreboards")("scores")
  def all = scores.map(grater[Score].asObject(_)).toList.sortBy(s => s.score).reverse
  def allFromUser()(implicit user: Option[User]) = user match {
    case Some(u) => scores.find(MongoDBObject("user" -> u.id)).map(grater[Score].asObject(_)).toList.sortBy(s => s.score).reverse
    case None => List[Score]()
  }

  def remove(id: String) {
    scores -= Scores.this.getScore(id)
  }
  
  def getScore(id : String) = scores.findOne(MongoDBObject("_id" -> new ObjectId(id))).get

  def create(s : Score) = scores += grater[Score].asDBObject(s)
  
  def newScore(id: String, username: String, score: Int) = 
    Scores.create(new Score(new ObjectId(), new ObjectId(id), username, score))
}