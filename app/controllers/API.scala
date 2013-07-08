package controllers

import play.api._
import play.api.mvc._
import models._
import org.codehaus.jackson.node.ObjectNode
import play.api.libs.json.Json
import views.html.defaultpages.badRequest
import play.api.libs.json.JsResultException
import play.api.libs.json.JsResultException

object API extends Controller with UserTrait {

  def test = Action { implicit request =>
    Ok(Json.obj("status" -> "OK"))
  }
  
  def add = Action { implicit request => 
    request.body.asJson match {
      case Some(json) => {
        try{
        	Scores.newScore((json \ "secret").as[String], (json \ "username").as[String], (json \ "score").as[Int])
        	Ok(Json.obj("status" -> "saved"))
        }
        catch{
          case _:JsResultException => BadRequest("Missing required fields")
        }
        
      }
      case None => BadRequest("Could not parse JSON");
    }
  }
  
  def get = Action { implicit request => 
  
    request.body.asJson match{
      case Some(json) => {
        try{
        	val id = (json \ "secret").as[String]
        	val scores = Scores.allFromUser(id)
        	
        	Ok(Json.toJson(scores))
        }
        catch{
          case _:JsResultException => BadRequest("Missing required fields")
        }
        
      }
      case None => BadRequest("Could not parse JSON");
    }  
  }

  def me = Action { implicit request =>
    
    user match {
      case Some(u) => Ok(views.html.userStats())
      case None => Redirect(routes.Auth.login())
        .flashing("message" -> "PLEASE LOGIN!")
    }
  }
}
