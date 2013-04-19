object caseclasstest {
  val c = Test("AAA")                             //> c  : Test = Test(AAA)
  c.field                                         //> res0: String = AAA
  
}

case class Test(
  field : String = "AAA"
)