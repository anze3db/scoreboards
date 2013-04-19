package controllers

import play.api._
import play.api.mvc._
import models._
import org.codehaus.jackson.node.ObjectNode
import play.api.libs.json.Json
import views.html.defaultpages.badRequest

object API extends Controller with UserTrait {

  def test = Action { implicit request =>
    Ok(Json.obj("status" -> "OK"))
  }
  
  def add = Action { implicit request => 
    
    request.body.asJson match {
      case Some(json) => {
        BadRequest("Could not parse JSON");
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
