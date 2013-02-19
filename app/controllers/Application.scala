package controllers

import models.Users
import play.api.data.Form
import play.api.data.Forms.nonEmptyText
import play.api.data.Forms.tuple
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.Security

object Application extends Controller {

  def index = Action { implicit request =>

    val user = request.session.get(Security.username);
    user match {
      case Some(u) => Ok(views.html.index(Users.all))
      case None => Redirect(routes.UserController.login())
        .flashing("message" -> "PLEASE LOGIN!")
    }
  }
}
