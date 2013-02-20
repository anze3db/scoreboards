package controllers

import com.mongodb.casbah.Imports.ObjectId
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
    )((user, pass, pass2, real) => User(new ObjectId, user, pass, pass2, real))
     ((registration: User) => Some((registration.username, registration.password, registration.confirm, registration.realName)))
      verifying("Username already exsists", fields => fields match {
        case User(_, username, _, _, _) => Users.checkUsername(username)
      })
      verifying("Passwords must match", fields => fields match {
        case User(_, _, password, confirmation, _) => (password == confirmation)
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
}
