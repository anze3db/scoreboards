package controllers

import play.api._
import play.api.mvc._
import models._
import com.mongodb.casbah.Imports._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import views.html.defaultpages.badRequest

object Application extends Controller {

  def loginForm = Form(
    tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    ) verifying ("Invalid email or password", result => result match {
      case (username,password) => check(username, password)
    })
  )
  
  def check(username : String, password : String) = {
    Registrations.check(username, password)
  }
  
  def index = Action { implicit request =>

      val user = request.session.get(Security.username);
      user match {
        case Some(u) => Ok(views.html.index(Registrations.all))
        case None => Redirect(routes.Application.login())
                      .flashing("message" -> "PLEASE LOGIN!")
      }
      
      //Registrations.create(r)
      
      //Registrations.remove(Registration("Ime", "pass", "pass", "Name"))
  }
  
  def login = Action { implicit request => 
    Ok(views.html.login(loginForm))
  }
  def log = Action { implicit request => 
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      user =>  {
            Registrations.getUserByName(user._1)
            println(user._1);
            Redirect(routes.Application.index())
                  .flashing("message" -> "Locked in!")
                  .withSession(Security.username -> Registrations.getUserByName(user._1).id.toString)
      }
    )
  }
}
