package models

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.ValidBSONType.BasicDBList
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._
import com.novus.salat.annotations._

import com.novus.salat.{TypeHintFrequency, StringTypeHintStrategy, Context}
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
      override val typeHintStrategy = StringTypeHintStrategy(when = TypeHintFrequency.WhenNecessary, typeHint = "_t")
    }
    context.registerGlobalKeyOverride(remapThis = "id", toThisInstead = "_id")
    context.registerClassLoader(Play.classloader)
    context
  }
}
import mongoContext._

case class Registration(@Key("_id") id: ObjectId = new ObjectId, username: String, password: String, confirm: String, realName: String)

object Registrations {
  val registrations = MongoConnection()("sampleapp")("registrations")

  def all = registrations.map(grater[Registration].asObject(_)).toList
  
  def create(registration: Registration) {
    registrations += grater[Registration].asDBObject(registration)
  }
  
  def check(username : String, password : String) = {
    registrations.findOne(MongoDBObject("username" -> username, "password" -> password)).isDefined
  }
  def getUserByName(username : String) = {
    grater[Registration].asObject(registrations.findOne(MongoDBObject("username" -> username)).get)
  }
  
  def remove(registration: Registration) { 
    registrations -= grater[Registration].asDBObject(registration)
  }
  
  def getUser(id: String) = registrations.findOne(MongoDBObject("_id" -> new ObjectId(id))).get
  
  def remove(id: String){
    registrations -= this.getUser(id)
  }
  
  def removeAll = registrations.dropCollection
}
