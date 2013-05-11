package controllers;

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._
import views.html.defaultpages.badRequest
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import utils.TestDatabase
import models.Users
import models.Scores

@RunWith(classOf[JUnitRunner])
class APITest extends Specification with TestDatabase {
  
  "check if test Action returns OK" in {
    val result = controllers.API.test()(FakeRequest())
  
    status(result) must equalTo(OK)
    contentType(result) must beSome("application/json")
    charset(result) must beSome("utf-8")
    (Json.parse(contentAsString(result)) \ "status").as[String] must equalTo("OK")
  }
  
  "check if add Action fails gracefully" in {
    
    val noJsonResult = controllers.API.add()(FakeRequest())
    status(noJsonResult) must equalTo(400)
    contentAsString(noJsonResult ) must equalTo("Could not parse JSON")
    
    val garbledJsonResult = controllers.API.add()(FakeRequest().withTextBody("{{"))
    status(garbledJsonResult) must equalTo(400)
    contentAsString(garbledJsonResult) must equalTo("Could not parse JSON")
    
  }
  
  "check if add Action adds the score" in new WithApplication(fakeApplication){
    
    implicit val user = Option(Users.getByName("user"))
    val json = Json.parse("""{"username": "player", "score": 1000, "secret":""""+user.get.id.toString+""""}""")
    val result = controllers.API.add()(FakeRequest().withJsonBody(json))
    status(result) must equalTo(200)
    
    (Json.parse(contentAsString(result)) \ "status").as[String] must equalTo("saved")
    val allScores = Scores.allFromUser()
    allScores.length must equalTo(1)
    
    for{
      score <- allScores
    } score.score must equalTo(1000)
    
  }
    
//  "check if add Action fails on missing parameters" in {
//    val jsons = List(
//        Json.parse("""{"username": "player", "score": 1000}"""),
//        Json.parse("""{"secret": "a seckret key", "score": 1000}"""),
//        Json.parse("""{"secret": "a seckret key", "username": "player"}""")
//    )
//    jsons.foreach(json => {
//    	val result = controllers.API.add()(FakeRequest().withJsonBody(json))
//    	status(result) must equalTo(400) 
//    	contentAsString(result) must equalTo("Missing required fields")
//    }) 
//  }
  
}