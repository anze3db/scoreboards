package controllers

import com.mongodb.casbah.Imports.ObjectId
import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._


object Admin extends Controller with UserTrait {
  
  val MIN_ARTICLE_LENGTH = 250;
  
  def articleForm = Form(
    mapping(
      "text" -> nonEmptyText,
      "author" -> nonEmptyText,
      "source" -> nonEmptyText)
      ((text, author, source) => Article(new ObjectId, text, author, source))
     ((article: Article) => Some((
         article.text, 
         article.author, 
         article.source)))
      verifying ("Sorry, this text is too short for this app.", result => result match {
        case Article(_,text, _, _) => (true)
    })
  )

  def userList = Action { implicit request =>
    user match {
      case Some(u) => Ok(views.html.userList(Users.all)) /* u.admin match {
        case true  => Ok(views.html.userList(Users.all))
        case false => 
          Redirect(routes.Auth.login())
            .flashing("message" -> "You don't have the power!")
      } */
      case None => 
        Redirect(routes.Auth.login())
          .flashing("message" -> "PLEASE LOGIN!")
    }
  }
 
  def articleList = Action { implicit request =>
    Ok(views.html.articleList(Corpus.all))
  }
  
  def toggle = Action { implicit request =>
  	Users.toggle(user.get)
    Redirect(routes.Admin.userList())
  }
  
  def insertNewArticle = Action { implicit request =>
    articleForm.bindFromRequest.fold(
      form => BadRequest(views.html.newArticle(form)),
      article => {
        Corpus.create(article)
        Redirect(routes.Application.index()).flashing("message" -> "Article added!")
      }
    )
  }
  
  def newArticle = Action { implicit request =>
    Ok(views.html.newArticle(articleForm))
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
 

  def disableArticle(id: String) = Action { implicit request =>
  	//TODO: dissable article
    Redirect(routes.Admin.articleList()).flashing(flash)
  }

}
