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


case class User(
  @Key("_id") id: ObjectId = new ObjectId,
  username: String,
  password: String,
  confirm: String,
  realName: String,
  admin: Boolean){
  
  def md5(s : String) = MessageDigest.getInstance("MD5").digest(s.getBytes).map("%02x".format(_)).mkString
  def hashPassword = User(id, username, md5(password), "", realName, admin)
}

object Users {

  val users = MongoConnection()("scoreboards")("registrations")

  def all = users.map(grater[User].asObject(_)).toList

  def create(registration: User) {
    val user = registration.hashPassword
    users += grater[User].asDBObject(user)
  }
  def md5(s : String) = MessageDigest.getInstance("MD5").digest(s.getBytes).map("%02x".format(_)).mkString
  def check(username: String, password: String) = {
    users.findOne(MongoDBObject(
      "username" -> ("(?i)" + username).r,
      "password" -> md5(password))).isDefined
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
  
  def toggle(user: User) {
    val obj = grater[User].asDBObject(user)
    users.update(obj, $set(Seq("admin" -> !user.admin)))
  }

  def remove(u: User) {
    users -= grater[User].asDBObject(u)
  }

  def remove(id: String) {
  
    users -= Users.this.getUser(id)
  }

  def removeAll = users.dropCollection

}
