package com.me.java2plantuml


/**
 *
 */
class JavaParser {
  private val packagePattern = """package ([\w\.]+);""".r
  private val classPattern = """public\s+(?:abstract\s+)?class (\w+).*""".r
  private val fieldPattern = """\s*private\s+(?:static\s+)?(?:final\s+)?(?:transient\s+)?(\S+)\s+(\w+).*;""".r
  private val mapFieldPattern = """\s*private\s+(?:static\s+)?(?:final\s+)?(?:transient\s+)?(\w+)<(\w+)\s*,\s*(\w+)>\s+(\w+).*;""".r
  private val compositeTypePattern = """(\w+)<(\w+)>""".r

  def parse(src: String): ClassDefinition = {
    src.split("\r?\n").foldLeft(ClassDefinition.empty) {
      (classDefinition, line) =>
        line match {
          case packagePattern(packageName) =>
            classDefinition.copy(packageName = packageName)

          case classPattern(className) =>
            classDefinition.copy(className = className)

          case mapFieldPattern(compositionType, keyTypeName, objectTypeName, fieldName) =>
            classDefinition.copy(fields = classDefinition.fields :+ FieldDefinition(fieldName, MapFieldType(compositionType, keyTypeName, objectTypeName)))

          case fieldPattern(fieldType, fieldName) =>
            //println(s"use field $fieldName of type $fieldType")
            classDefinition.copy(fields = classDefinition.fields :+ FieldDefinition(fieldName, parseFieldType(fieldType)))

          case _ =>
            //println(s"ignore: $line")
            classDefinition // ignore the rest
        }
    }
  }

  def parseFieldType(str: String): FieldType = {
    str match {
      case "Long" => SimpleFieldType.Long
      case "long" => SimpleFieldType.Long
      case "String" => SimpleFieldType.String
      case "Boolean" => SimpleFieldType.Boolean
      case "boolean" => SimpleFieldType.Boolean
      case "Date" => SimpleFieldType.Date
      case "Integer" => SimpleFieldType.Integer
      case "int" => SimpleFieldType.Integer
      case compositeTypePattern(compositionType, objectType) => CompositeFieldType(compositionType, objectType)
      case any => ObjectFieldType(any)
    }
  }
}
