package models

import com.novus.salat.Context
import com.novus.salat.StringTypeHintStrategy
import com.novus.salat.TypeHintFrequency

import play.api.Play
import play.api.Play.current

package object mongoContext {
  implicit val context = {
    val context = new Context {
      val name = "global"
      override val typeHintStrategy = StringTypeHintStrategy(when = TypeHintFrequency.WhenNecessary, typeHint = "_t")
    }
    context.registerGlobalKeyOverride(remapThis = "id", toThisInstead = "_id")
    context.registerClassLoader(Play.classloader)
    context
  }
}
