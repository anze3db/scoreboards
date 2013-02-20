package models

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.ValidBSONType.BasicDBList
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._
import com.novus.salat.annotations._

import com.novus.salat.{ TypeHintFrequency, StringTypeHintStrategy, Context }
import play.api.Play
import play.api.Play.current

/*
  Adding a custom Salat context to work with Play's Classloader
  Using example from:
  https://github.com/leon/play-salat/blob/master/sample/app/models/mongoContext.scala
*/
package object mongoContext {
  implicit val context = {
    val context = new Context {
      val name = "global"
      override val typeHintStrategy = StringTypeHintStrategy(
          when = TypeHintFrequency.WhenNecessary, typeHint = "_t")
    }
    context.registerGlobalKeyOverride(remapThis = "id", toThisInstead = "_id")
    context.registerClassLoader(Play.classloader)
    context
  }
}
import mongoContext._

case class User(
  @Key("_id") id: ObjectId = new ObjectId,
  username: String,
  password: String,
  confirm: String,
  realName: String)

object Users {
  val users = MongoConnection()("sampleapp")("registrations")

  def all = users.map(grater[User].asObject(_)).toList

  def create(registration: User) {
    users += grater[User].asDBObject(registration)
  }

  def check(username: String, password: String) = {
    users.findOne(MongoDBObject(
      "username" -> ("(?i)" + username).r,
      "password" -> password)).isDefined
  }

  def checkUsername(username: String) = {
    !users.findOne(MongoDBObject("username" -> ("(?i)" + username).r)).isDefined
  }

  def getUser(id: String) = users.findOne(MongoDBObject("_id" -> new ObjectId(id))).get

  def getUserByName(username: String) = {
    grater[User].asObject(users.findOne(MongoDBObject("username" -> ("(?i)" + username).r)).get)
  }
  
  def getUserById(id: String) = {
    //grater[User].asObject(users.findOne(MongoDBObject("_id" -> new ObjectId(id))).get)
    users.findOne(MongoDBObject("_id" -> new ObjectId(id))).map(grater[User].asObject(_))
  }

  def remove(registration: User) {
    users -= grater[User].asDBObject(registration)
  }

  def remove(id: String) {
    users -= Users.this.getUser(id)
  }

  def removeAll = users.dropCollection

}
