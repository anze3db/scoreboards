package controllers

import play.api._
import play.api.mvc._
import models._

object Application extends Controller {
  
  implicit def user(implicit request : RequestHeader) : Option[User] = {  
    session.get(Security.username).flatMap(Users.getUserById(_))
  }
  
  def index = Action { implicit request =>
    Ok(views.html.index(Users.all))
  }
  
  def me = Action { implicit request =>
    Ok(views.html.index(Users.all))
    
    val user = request.session.get(Security.username);
    user match {
      case Some(u) => Ok(views.html.index(Users.all))
      case None => Redirect(routes.Auth.login())
        .flashing("message" -> "PLEASE LOGIN!")
    }
  }

  def remove(id: String) = Action { implicit request =>

  	val flash = if (user.isDefined){
  		Users.remove(id)
  		("message" -> "User Deleted!")
  	}else{
  		("error" -> "You can't do that! You're a bad perosn")
  	}
    Redirect(routes.Application.index()).flashing(flash)

  }
  
}
