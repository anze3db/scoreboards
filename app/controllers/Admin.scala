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
      verifying ("Invalid username or password", result => result match {
        case Article(_,text, _, _) => (text.length > MIN_ARTICLE_LENGTH)
    })
  )

  def userList = Action { implicit request =>
    Ok(views.html.userList(Users.all))
  }
  
  def articleList = Action { implicit request =>
    Ok(views.html.userList(Corpus.all))
  }
  
  def insertNewArticle = Action { implicit request =>
    articleForm.bindFromRequest.fold(
      form => BadRequest(views.html.newArticle(form)),
      article => {
        Corpus.create(article)
        Redirect(routes.Application.index()).flashing("message" -> "User Registered!")
      }
    )
  }
  
  def newArticle = Action { implicit request =>
    Ok(views.html.newArticle(articleForm))
  }

  def removeUser(id: String) = Action { implicit request =>

  	val flash = if (user.isDefined){
  		Users.remove(id)
  		("message" -> "User Deleted!")
  	}else{
  		("error" -> "You can't do that! You're a bad perosn")
  	}
    Redirect(routes.Admin.userList()).flashing(flash)
  }
  
}
