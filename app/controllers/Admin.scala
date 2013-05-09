package controllers

import com.mongodb.casbah.Imports.ObjectId
import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._


object Admin extends Controller with UserTrait {
  
  def userList = Action { implicit request =>
    user match {
      case Some(u) => if (u.admin) {
	        Ok(views.html.userList(Users.all))
	      } else { 
	          Redirect(routes.Application.index())
	            .flashing("message" -> "You don't have the power!")
	      }
      case None => 
        Redirect(routes.Auth.login())
          .flashing("message" -> "PLEASE LOGIN!")
    }
  }

  def toggle(id: String) = Action { implicit request =>
  	Users.toggle(Users.getM(id).get)
    Redirect(routes.Admin.userList())
  }

  def removeUser(id: String) = Action { implicit request =>

    val flash = user map { user => 
      Users.remove(id)
      ("message" -> "User Deleted!")
    }getOrElse{
  	  ("error" -> "You can't do that! You're a bad perosn")
  	}
    Redirect(routes.Admin.userList()).flashing(flash)
  }
}
