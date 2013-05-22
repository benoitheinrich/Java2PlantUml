package com.me.java2plantuml

/**
 *
 */
class PlantUmlWriter {
  type Relation = (String, String)

  def buildPlantUml(classDefinitions: Seq[ClassDefinition]): String = {
    val safeClassDefinitions = classDefinitions.filterNot(_.className.isEmpty)

    val classesToDefinitions = safeClassDefinitions.map(c => (c.className, c)).toMap
    val classesNames = safeClassDefinitions.map(c => s"class ${c.fullName}")
    def getRelations(f: FieldDefinition): Seq[Relation] = {
      f.fieldType match {
        case ObjectFieldType(t) => Seq(("1", t))
        case CompositeFieldType(_, t) => Seq(("many", t))
        case MapFieldType(_, k, v) => Seq(("many", k), ("many", v))
        case SimpleFieldType(_) => Seq.empty
      }
    }

    val relations = for {
      c <- safeClassDefinitions
      f <- c.fields
      (arity, objectType) <- getRelations(f)
    } yield {
      classesToDefinitions.get(objectType) match {
        case None =>
          println(s"Error: missing linked object type $objectType when analysing field ${c.className}.${f.fieldName}")
          ""
        case Some(n) =>
          s"""${c.fullName} "1" --* "$arity" ${n.fullName} : ${f.fieldName}"""
      }
    }

    s"""
      |@startuml
      |${classesNames.mkString("\n")}
      |${relations.filterNot(_.isEmpty).mkString("\n")}
      |@enduml
    """.stripMargin.trim
  }
}
