package com.me.java2plantuml

trait FieldType {
  def isRelation: Boolean
}

case class SimpleFieldType(typeName: String) extends FieldType {
  val isRelation = false
}

object SimpleFieldType {
  val String = SimpleFieldType("String")
  val Date = SimpleFieldType("Date")
  val Integer = SimpleFieldType("Integer")
  val Long = SimpleFieldType("Long")
  val Boolean = SimpleFieldType("Boolean")
}

case class ObjectFieldType(objectTypeName: String) extends FieldType {
  val isRelation = true
}

case class CompositeFieldType(compositionType: String, objectTypeName: String) extends FieldType {
  val isRelation = true
}

case class MapFieldType(compositionType: String, keyTypeName: String, objectTypeName: String) extends FieldType {
  val isRelation = true
}

case class ClassDefinition(className: String, packageName: String, fields: List[FieldDefinition]) {
  lazy val fullName = packageName + "." + className
}

case class FieldDefinition(fieldName: String, fieldType: FieldType)

object ClassDefinition {
  val empty = ClassDefinition("", "", List.empty)
}