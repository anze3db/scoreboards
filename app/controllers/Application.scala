package controllers

import models.Users
import play.api.data.Form
import play.api.data.Forms.nonEmptyText
import play.api.data.Forms.tuple
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.Security

object Application extends Controller {

  def loginForm = Form(
    tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    ) verifying ("Invalid email or password", result => result match {
      case (username,password) => Users.check(username, password)
    })
  )
  
  def index = Action { implicit request =>

      val user = request.session.get(Security.username);
      user match {
        case Some(u) => Ok(views.html.index(Users.all))
        case None => Redirect(routes.Application.login())
                      .flashing("message" -> "PLEASE LOGIN!")
      }
  }
  
  def login = Action { implicit request => 
    Ok(views.html.login(loginForm))
  }
  def log = Action { implicit request => 
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      user =>  {
            Users.getUserByName(user._1)
            println(user._1);
            Redirect(routes.Application.index())
                  .flashing("message" -> "Locked in!")
                  .withSession(Security.username -> Users.getUserByName(user._1).id.toString)
      }
    )
  }
}
