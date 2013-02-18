package controllers

import com.mongodb.casbah.Imports.ObjectId

import models.User
import models.Users
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.nonEmptyText
import play.api.data.Forms.text
import play.api.mvc.Action
import play.api.mvc.Controller

// STEP 2:
object Register extends Controller {
  def registrationForm = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText,
      "confirm" -> nonEmptyText,
      "realName" -> text
    )((user, pass, pass2, real) => User(new ObjectId, user, pass, pass2, real))
     ((registration: User) => Some((registration.username, registration.password, registration.confirm, registration.realName)))
      verifying("Passwords must match", fields => fields match {
        case User(_, _, password, confirmation, _) => password.equals(confirmation)
      })
  )

  def index = Action { implicit request =>
    Ok(views.html.register(registrationForm))
  }

  def register = Action { implicit request =>
    registrationForm.bindFromRequest.fold(
      form => BadRequest(views.html.register(form)),
      registration => {
        Users.create(registration)
        Redirect(routes.Application.index()).flashing("message" -> "User Registered!")
      }
    )
  }
  
  def remove(id : String) = Action { implicit request => 
      Users.remove(id)
      Redirect(routes.Application.index()).flashing("message" -> "User Deleted!")
  }
}
