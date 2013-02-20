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

case class Article(
  @Key("_id") id: ObjectId = new ObjectId,
  text: String,
  author: String,
  source: String)

object Corpus {

  def md5(s: String) = MessageDigest.getInstance("MD5").digest(s.getBytes)

  val articles = MongoConnection()("sampleapp")("registrations")

  def all = articles.map(grater[User].asObject(_)).toList

  def create(article: Article) {
    articles += grater[Article].asDBObject(article)
  }

  def check(username: String, password: String) = {
    articles.findOne(MongoDBObject(
      "username" -> ("(?i)" + username).r,
      "password" -> password)).isDefined
  }

  def checkUsername(username: String) = {
    !articles.findOne(MongoDBObject("username" -> ("(?i)" + username).r)).isDefined
  }

  def getUser(id: String) = articles.findOne(MongoDBObject("_id" -> new ObjectId(id))).get

  def getUserByName(username: String) = {
    grater[User].asObject(articles.findOne(MongoDBObject("username" -> ("(?i)" + username).r)).get)
  }

  def getUserById(id: String) = {
    //grater[User].asObject(users.findOne(MongoDBObject("_id" -> new ObjectId(id))).get)
    articles.findOne(MongoDBObject("_id" -> new ObjectId(id))).map(grater[User].asObject(_))
  }

  def remove(registration: User) {
    articles -= grater[User].asDBObject(registration)
  }

  def remove(id: String) {
    articles -= Corpus.this.getUser(id)
  }

  def removeAll = articles.dropCollection

}
