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
import play.api.data.Forms.tuple
import play.api.mvc.Security

// STEP 2:
object UserController extends Controller {
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
        case User(_, _, password, confirmation, _) => password.equals(confirmation)
      })
  )
  
  def loginForm = Form(
    tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText) verifying ("Invalid email or password", result => result match {
        case (username, password) => Users.check(username, password)
    })
  )


  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }
  def _login = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      user => {
        Users.getUserByName(user._1)
        println(user._1);
        Redirect(routes.Application.index())
          .flashing("message" -> "Locked in!")
          .withSession(Security.username -> Users.getUserByName(user._1).id.toString)
      })
  }

  def register = Action { implicit request =>
    Ok(views.html.register(registrationForm))
  }
  
  def _register = Action { implicit request =>
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
