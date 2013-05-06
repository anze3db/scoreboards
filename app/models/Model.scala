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

trait Models { } //Foo trait for Models

abstract class Model[A <: Models : Manifest] {
  
  def getModel : MongoCollection;
  
  // How to add the correct type to the grater?
  def all = getModel.map(grater[A].asObject(_)).toList//.sortBy(s => s.score).reverse

  def allFromUser()(implicit user: Option[User]) = user match {
    case Some(u) => getModel.find(MongoDBObject("user" -> u.id)).map(grater[A].asObject(_)).toList//.sortBy(s => s.score).reverse
    case None => List[A]()
  }


  def remove(id: String) {
    "a" + id
    //model -= Scores.this.getScore(id)
  }
  

  def create(s : Score) = getModel += grater[Score].asDBObject(s)
  

}
