package controllers

import com.mongodb.casbah.Imports._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import models._

// STEP 2:
object Register extends Controller {
  def registrationForm = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText,
      "confirm" -> nonEmptyText,
      "realName" -> text
    )((user, pass, pass2, real) => Registration(new ObjectId, user, pass, pass2, real))
     ((registration: Registration) => Some((registration.username, registration.password, registration.confirm, registration.realName)))
      verifying("Passwords must match", fields => fields match {
        case Registration(_, _, password, confirmation, _) => password.equals(confirmation)
      })
  )

  def index = Action { implicit request =>
    Ok(views.html.register(registrationForm))
  }

  def register = Action { implicit request =>
    registrationForm.bindFromRequest.fold(
      form => BadRequest(views.html.register(form)),
      registration => {
        Registrations.create(registration)
        Redirect(routes.Application.index()).flashing("message" -> "User Registered!")
      }
    )
  }
  
  def remove(id : String) = Action { implicit request => 
      Registrations.remove(id)
      Redirect(routes.Application.index()).flashing("message" -> "User Deleted!")
  }
}
