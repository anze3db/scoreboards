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

  val articles = MongoConnection()("sampleapp")("coprus")

  def all = articles.map(grater[Article].asObject(_)).toList

  def create(article: Article) {
    articles += grater[Article].asDBObject(article)
  }

  def getArticle(id: String) = articles.findOne(MongoDBObject("_id" -> new ObjectId(id))).get

  def getRandomArticle() = {
    grater[Article].asObject(articles.findOne(MongoDBObject("text" -> "aa")).get)
  }

  def getArticleById(id: String) = {
    //grater[User].asObject(users.findOne(MongoDBObject("_id" -> new ObjectId(id))).get)
    articles.findOne(MongoDBObject("_id" -> new ObjectId(id))).map(grater[Article].asObject(_))
  }

  def remove(article: Article) {
    articles -= grater[Article].asDBObject(article)
  }

  def remove(id: String) {
    articles -= Corpus.this.getArticle(id)
  }

  def removeAll = articles.dropCollection

}
