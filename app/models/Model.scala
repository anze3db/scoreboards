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

class Model() { 
  
  
} //Foo trait for Models

abstract class Models[A <: Model : Manifest] {
  
  val db = MongoConnection()("scoreboards")
  val table : MongoCollection
  
  def all = table.map(grater[A].asObject(_)).toList
  
  def get(id: String) = table.findOne(MongoDBObject("_id" -> new ObjectId(id)))
  
  def get(id: ObjectId) = table.findOne(MongoDBObject("_id" -> id))
  
  def create(m : Model) {
    table += grater[Model].asDBObject(m)
  }
  
  def remove(m: Model) {
    table -= grater[Model].asDBObject(m)
  }

  def remove(id: String) {
    get(id) match {
      case Some(m) => table -= m
      case None => {}
    } 
  }

  def removeAll = table.dropCollection

}
