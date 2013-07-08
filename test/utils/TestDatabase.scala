package utils

import play.api.test.FakeApplication
import models.User
import org.bson.types.ObjectId
import models.Users
import com.mongodb.casbah.MongoConnection
import play.api.Play
import play.api.test.WithApplication
import org.specs2.execute.Result

trait TestDatabase {
  
  val name = "scoreboardstest"

  val fakeApplication = FakeApplication(additionalConfiguration = Map("mongo" -> name))

  def resetDb {
    val db = MongoConnection()(name)
    db.dropDatabase()
    
    Users.create(User(new ObjectId, "admin", "pass", "pass", "test", true))
    Users.create(User(new ObjectId, "user", "pass", "pass", "test", false))
  }

}
