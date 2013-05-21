package com.me.java2plantuml

import org.scalatest.FunSpec
import org.scalatest.matchers.MustMatchers

/**
 *
 */
class PlantUmlWriterTest extends FunSpec with MustMatchers {
  describe("The Plant UML writer works if") {
    it("generates a UML for just one class") {
      val classes = Seq(
        ClassDefinition("MyClass", "com.my.pack", List(
          FieldDefinition("field1", SimpleFieldType.String)
        ))
      )
      val uml = new PlantUmlWriter().buildPlantUml(classes)
      assert(uml ===
        """
          |@startuml
          |class com.my.pack.MyClass
          |
          |@enduml
        """.stripMargin.trim)
    }

    it("generates a UML for just two classes with an object link") {
      val classes = Seq(
        ClassDefinition("MyClass", "com.my.pack", List(
          FieldDefinition("field1", ObjectFieldType("MyOther"))
        )),
        ClassDefinition("MyOther", "com.my.pack", List(
          FieldDefinition("field1", SimpleFieldType.String)
        ))
      )
      val uml = new PlantUmlWriter().buildPlantUml(classes)
      assert(uml ===
        """
          |@startuml
          |class com.my.pack.MyClass
          |class com.my.pack.MyOther
          |com.my.pack.MyClass "1" *-- "1" com.my.pack.MyOther : field1
          |@enduml
        """.stripMargin.trim)
    }

    it("generates a UML for just two classes with a composite link") {
      val classes = Seq(
        ClassDefinition("MyClass", "com.my.pack", List(
          FieldDefinition("field1", CompositeFieldType("Set", "MyOther"))
        )),
        ClassDefinition("MyOther", "com.my.pack", List(
          FieldDefinition("field1", SimpleFieldType.String)
        ))
      )
      val uml = new PlantUmlWriter().buildPlantUml(classes)
      assert(uml ===
        """
          |@startuml
          |class com.my.pack.MyClass
          |class com.my.pack.MyOther
          |com.my.pack.MyClass "1" *-- "many" com.my.pack.MyOther : field1
          |@enduml
        """.stripMargin.trim)
    }

    it("generates a UML for just two classes with a map link (value)") {
      val classes = Seq(
        ClassDefinition("MyClass", "com.my.pack", List(
          FieldDefinition("field1", MapFieldType("Map", "String", "MyOther"))
        )),
        ClassDefinition("MyOther", "com.my.pack", List(
          FieldDefinition("field1", SimpleFieldType.String)
        ))
      )
      val uml = new PlantUmlWriter().buildPlantUml(classes)
      assert(uml ===
        """
          |@startuml
          |class com.my.pack.MyClass
          |class com.my.pack.MyOther
          |com.my.pack.MyClass "1" *-- "many" com.my.pack.MyOther : field1
          |@enduml
        """.stripMargin.trim)
    }

    it("generates a UML for just two classes with a map link (key)") {
      val classes = Seq(
        ClassDefinition("MyClass", "com.my.pack", List(
          FieldDefinition("field1", MapFieldType("Map", "MyOther", "String"))
        )),
        ClassDefinition("MyOther", "com.my.pack", List(
          FieldDefinition("field1", SimpleFieldType.String)
        ))
      )
      val uml = new PlantUmlWriter().buildPlantUml(classes)
      assert(uml ===
        """
          |@startuml
          |class com.my.pack.MyClass
          |class com.my.pack.MyOther
          |com.my.pack.MyClass "1" *-- "many" com.my.pack.MyOther : field1
          |@enduml
        """.stripMargin.trim)
    }

    it("Ignores unknown classes in links") {
      val classes = Seq(
        ClassDefinition("MyClass", "com.my.pack", List(
          FieldDefinition("field1", CompositeFieldType("Set", "MyOther"))
        ))
      )
      val uml = new PlantUmlWriter().buildPlantUml(classes)
      assert(uml ===
        """
          |@startuml
          |class com.my.pack.MyClass
          |
          |@enduml
        """.stripMargin.trim)
    }
  }
}
