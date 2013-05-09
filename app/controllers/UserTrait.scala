package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._

trait UserTrait extends Controller{
  
  implicit def user(implicit request : RequestHeader) : Option[User] = {  
    session.get(Security.username).flatMap(Users.getM(_))
  }
}