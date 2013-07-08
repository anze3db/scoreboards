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
  admin: Boolean) extends Model {
  def md5(s : String) = Users.md5(s)
  def hashPassword = User(new ObjectId, username, md5(password), "", realName, admin)
  def getSecret = id.toString
}

object Users extends Models[User] {

  override val table = db("users")
  
  def create(u: User) {
    super.create(u.hashPassword)
  }
  
  def md5(s : String) = MessageDigest.getInstance("MD5").digest(s.getBytes).map("%02x".format(_)).mkString
  
  def check(username: String, password: String) = {
    table.findOne(MongoDBObject(
      "username" -> ("(?i)" + username).r,
      "password" -> md5(password))).isDefined
  }

  def checkUsername(username: String) = {
    !table.findOne(MongoDBObject("username" -> ("(?i)" + username).r)).isDefined
  }

  def getByName(username: String) = {
    grater[User].asObject(table.findOne(MongoDBObject("username" -> ("(?i)" + username).r)).get)
  }
  
  def toggle(user: User) {
    val u = grater[User].asDBObject(user)
    table.update(u, $set(Seq("admin" -> !user.admin)))
  }
  
}

