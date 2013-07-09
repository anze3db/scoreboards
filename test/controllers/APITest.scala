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
  
  sequential
  
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
    contentAsString(noJsonResult) must equalTo("Could not parse JSON")
    
    val garbledJsonResult = controllers.API.add()(FakeRequest().withTextBody("{{"))
    status(garbledJsonResult) must equalTo(400)
    contentAsString(garbledJsonResult) must equalTo("Could not parse JSON")
    
  }
  
  "check if add Action adds the score" in new WithApplication(fakeApplication){
    resetDb
    implicit val user = Option(Users.getByName("user"))
    
    val json = Json.parse("""{"username": "player", "score": 1000, "secret":""""+user.get.getSecret+""""}""")
    val result = controllers.API.add()(FakeRequest().withJsonBody(json))
    status(result) must equalTo(200)
    
    (Json.parse(contentAsString(result)) \ "status").as[String] must equalTo("saved")
    val allScores = Scores.allFromUser()
    
    
    
    allScores.length must equalTo(1)
    
    for{
      score <- allScores
    } score.score must equalTo(1000)
    
  }
    
  "check if add Action fails on missing parameters" in {
    resetDb
    val jsons = List(
        Json.parse("""{"username": "player", "score": 1000}"""),
        Json.parse("""{"secret": "a seckret key", "score": 1000}"""),
        Json.parse("""{"secret": "a seckret key", "username": "player"}""")
    )
    jsons.foreach(json => {
    	val result = controllers.API.add()(FakeRequest().withJsonBody(json))
    	status(result) must equalTo(400) 
    	contentAsString(result) must equalTo("Missing required fields")
    }) 
  }
  
  "check if get Action returns list" in {
    
    resetDb
    
    implicit val user = Option(Users.getByName("user"))

    val name = "test"
    
    // Add some scores
    for (i <- 1 to 20){
    	Scores.newScore(user.get.getSecret, name, i*100)
    } 
    val json = Json.parse("""{"secret": """"+user.get.getSecret+""""}""")
    val result = controllers.API.get()(FakeRequest().withJsonBody(json))
    
    val resultList = Json.parse(contentAsString(result)).as[List[JsObject]]
    
    resultList.length must equalTo(10) // limit top 10 results
    (resultList.head \ "score").as[Int] must equalTo(2000)
    
    for(score <- resultList){
      (score \ "username").as[String] must equalTo(name)
    }    
    
  }
  
}