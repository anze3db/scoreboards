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
  @Key("_id") id: ObjectId = new ObjectId,
  user: ObjectId,
  username: String,
  score: Int
) extends Models

object Scores extends Model[Score] {
  def getModel() = MongoConnection()("scoreboards")("scores")
  
  def newScore(id: String, username: String, score: Int) = 
    Scores.create(new Score(new ObjectId(), new ObjectId(id), username, score))
  
  def getScore(id : String) =
    getModel.findOne(MongoDBObject("_id" -> new ObjectId(id))).get

}
