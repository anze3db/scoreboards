object caseclasstest {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(44); 
  val c = Test("AAA");System.out.println("""c  : Test = """ + $show(c ));$skip(10); val res$0 = 
  c.field;System.out.println("""res0: String = """ + $show(res$0))}
  
}

case class Test(
  field : String = "AAA"
)
