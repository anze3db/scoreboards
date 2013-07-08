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
import play.api.libs.json.Json
import play.api.libs.json.JsResult
import play.api.libs.json.JsSuccess
import play.api.libs.json.Writes
import play.api.libs.json.JsValue
import play.api.libs.json.JsString
import play.api.libs.json.JsObject
import play.api.libs.json.JsNumber

case class Score(
  @Key("_id") id: ObjectId = new ObjectId,
  user: ObjectId,
  username: String,
  score: Int
) extends Model

object Score{
  implicit def toJson = new Writes[Score]{
    def writes(s : Score) : JsValue = {
      JsObject(Seq(("username", JsString(s.username)), ("score", JsNumber(s.score))))
    }
  }
}

object Scores extends Models[Score] {
  
  override val table = db("scores")
  
  def newScore(id: String, username: String, score: Int) = 
    Scores.create(new Score(new ObjectId, new ObjectId(id), username, score))
  
  def allFromUser()(implicit user: Option[User]) = user match {
    case Some(u) => table.find(MongoDBObject("user" -> u.id)).map(grater[Score].asObject(_)).toList.sortBy(s => s.score).reverse
    case None => List[Score]()
  }
  def allFromUser(id : String) = table.find(MongoDBObject("user" -> new ObjectId(id))).map(grater[Score].asObject(_)).toList.sortBy(s => s.score).reverse.take(10)
}
