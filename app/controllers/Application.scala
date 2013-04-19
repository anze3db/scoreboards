package controllers

import play.api._
import play.api.mvc._
import models._

object Application extends Controller with UserTrait {

  def index = Action { implicit request =>
  	Ok(views.html.index())
  }

  def me = Action { implicit request =>
    
    user match {
      case Some(u) => Ok(views.html.userStats())
      case None => Redirect(routes.Auth.login())
        .flashing("message" -> "PLEASE LOGIN!")
    }
  }

}
