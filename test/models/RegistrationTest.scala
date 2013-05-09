package models

import org.specs2.mutable.Specification
import play.api.test.WithApplication
import org.bson.types.ObjectId
import java.security.MessageDigest
import java.util.NoSuchElementException

class UserTest extends Specification {
  
  
  
  "Users model" should {
    "have an admin user" in new WithApplication{
      
      val user = Users.getUserByName("admin")
      user.username.toLowerCase() must equalTo("admin")
      user.admin must equalTo(true)
    }
    "be able to create and delete a user" in new WithApplication{
      val user = User(new ObjectId, "test-user", "pass", "pass", "test", false)
      Users.create(user)
      
      val savedUser = Users.getUserByName("test-user")
      savedUser.confirm must equalTo("")
      savedUser.password must equalTo(savedUser.md5("pass"))
      savedUser.admin must equalTo(false)
      
      Users.remove(savedUser)
      
      Users.getUserByName("test-user") must throwA[NoSuchElementException]
    }
  }
  
}