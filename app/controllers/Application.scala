package controllers

import play.api._
import play.api.mvc._
import models._
import com.mongodb.casbah.Imports._

object Application extends Controller {

  def index = Action { implicit request => {
      val r = Registration(new ObjectId, "Ime", "pass", "pass", "Name")
      //Registrations.create(r)
      
      //Registrations.remove(Registration("Ime", "pass", "pass", "Name"))
      Ok(views.html.index(Registrations.all))
    }
  }
}
