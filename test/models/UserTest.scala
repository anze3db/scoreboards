package models

import org.specs2.mutable.Specification
import play.api.test.WithApplication
import org.bson.types.ObjectId
import java.security.MessageDigest
import java.util.NoSuchElementException
import org.specs2.execute.{AsResult,Result}
import com.mongodb.casbah.MongoConnection
import models.{Users => HowCanThisBeOverride}
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import play.api.Play
import play.api.test.FakeApplication
import utils.TestDatabase

@RunWith(classOf[JUnitRunner])
class UserTest extends Specification with TestDatabase {
  
  
  "Test db" should {
    "not be the default one" in new WithApplication(fakeApplication){
      Play.current.configuration.getString("mongo").get must equalTo(name)
    }
  }
  
  "Users model" should {
    "have an admin user" in new WithApplication(fakeApplication){
      resetDb
      val user = Users.getByName("admin")
      user.username.toLowerCase() must equalTo("admin")
      user.admin must equalTo(true)
    }
    "be able to create and delete a user" in new WithApplication(fakeApplication){
      val user = User(new ObjectId, "test-user", "pass", "pass", "test", false)
      Users.create(user)
      
      val savedUser = Users.getByName("test-user")
      
      savedUser.confirm must equalTo("")
      savedUser.password must equalTo(savedUser.md5("pass"))
      savedUser.admin must equalTo(false)
      
      
      val user2 = User(new ObjectId, "test-user2", "pass", "pass", "test", false)
      Users.create(user2)
      
      val savedUser2 = Users.getByName("test-user2")
      savedUser.id.toString() must not equalTo(savedUser2.id.toString())
      
      Users.remove(savedUser)
      Users.remove(savedUser2)
      
      Users.getByName("test-user") must throwA[NoSuchElementException]
    }
  }
  
}