package models

import com.mongodb.casbah.Imports.MongoConnection
import com.mongodb.casbah.Imports.MongoDBObject
import com.mongodb.casbah.Imports.ObjectId
import com.mongodb.casbah.Imports.wrapDBObj
import com.novus.salat.global.ctx
import com.novus.salat.grater
import com.novus.salat.annotations.raw.Key
import mongoContext._;

case class User(@Key("_id") id: ObjectId = new ObjectId, username: String, password: String, confirm: String, realName: String)

object Users {
  val users = MongoConnection()("sampleapp")("users")

  def all = users.map(grater[User].asObject(_)).toList
  
  def create(user: User) {
    users += grater[User].asDBObject(user)
  }
  
  def check(username : String, password : String) = {
    users.findOne(MongoDBObject("username" -> ("(?i)"+username).r, "password" -> password)).isDefined
  }
  def getUserByName(username : String) = {
    grater[User].asObject(users.findOne(MongoDBObject("username" -> ("(?i)"+username).r)).get)
  }
  
  def remove(registration: User) { 
    users -= grater[User].asDBObject(registration)
  }
  
  def getUser(id: String) = users.findOne(MongoDBObject("_id" -> new ObjectId(id))).get
  
  def remove(id: String){
    users -= Users.this.getUser(id)
  }
  
  def removeAll = users.dropCollection
}
