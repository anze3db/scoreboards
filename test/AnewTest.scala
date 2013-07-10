import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import models.Score
import com.mongodb.casbah.Imports._
import models.Scores
import org.specs2.mutable.Specification
import play.api.test.WithApplication
import org.bson.types.ObjectId
import java.security.MessageDigest
import java.util.NoSuchElementException
import org.specs2.execute.{AsResult,Result}
import org.specs2._
import com.mongodb.casbah.MongoConnection
import models.{Users => HowCanThisBeOverride}
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import play.api.Play
import play.api.test.FakeApplication
import utils.TestDatabase
import utils.MongoTests

@RunWith(classOf[JUnitRunner])
class AnewTest extends Specification with MongoTests {
  "Score" should {
    "create item" in {
      withApp{
        val score = Score(new ObjectId, new ObjectId, "me", 1000)
        Scores.save(score)
        
        val all = Scores.find(ref = MongoDBObject())
        all.length must equalTo(1)
      }
      
    }
    
    "test item" in {
      withApp{
        
        val all = Scores.find(ref = MongoDBObject())
        all.length must equalTo(0)
      }
    }
    
  }
}