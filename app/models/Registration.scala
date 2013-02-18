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
  
  def remove(registration: Registration) { 
    registrations -= grater[Registration].asDBObject(registration)
  }
  def remove(id: String){
    val _id = new ObjectId(id)
    val m = MongoDBObject("_id" -> _id)
    registrations -= registrations.findOne(m).get
  }
  
  def removeAll = registrations.dropCollection
}
