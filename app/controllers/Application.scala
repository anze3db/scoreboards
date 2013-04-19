package controllers

import play.api._
import com.mongodb.casbah.Imports._
import play.api.mvc._
import models._

object Application extends Controller with UserTrait {

  def index = Action { implicit request =>
  	Ok(views.html.index())
  }
  
  def scores = Action { implicit request => 
    Ok(views.html.scores(Scores.allFromUser()))
  }
  
  def removeScore(id: String) = Action { implicit request => 
    Scores.remove(id)
    Redirect(routes.Application.scores()).flashing(flash)
  }

  def me = Action { implicit request =>
    
    user match {
      case Some(u) => Ok(views.html.userStats())
      case None => Redirect(routes.Auth.login())
        .flashing("message" -> "PLEASE LOGIN!")
    }
  }

}
