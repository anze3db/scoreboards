package utils

import com.mongodb.casbah.MongoClient
import play.api.test._
import play.api.test.Helpers._
import com.mongodb.casbah.Implicits._
import models.Users
import models.User
import org.bson.types.ObjectId
import com.mongodb.casbah.MongoConnection

import play.api.test.FakeApplication
import models.User
import org.bson.types.ObjectId
import models.Users
import com.mongodb.casbah.MongoConnection
import play.api.Play
import play.api.test.WithApplication
import org.specs2.execute.Result

trait MongoTests {
  
  def withApp[T](block : => T) = {
    val host = "localhost"
    val name = "scoreboardstest"
      
    val db = MongoConnection()(name)
    db.dropDatabase()
    
    Users.create(User(new ObjectId, "admin", "pass", "pass", "test", true))
    Users.create(User(new ObjectId, "user", "pass", "pass", "test", false))

    
    val app = FakeApplication(
        
      additionalConfiguration = Map("mongo" -> name) 
    )
  }

}