package controllers

import play.api._
import play.api.mvc._

import com.mongodb.casbah.Imports._

object Application extends Controller {
  
  def index = Action {
    val mongoClient =  MongoClient()
    val mongoDB = mongoClient("testing")("scores")
    for { x <- mongoDB} println(x)
    Ok(views.html.index("Mongo"))
  }
  
}