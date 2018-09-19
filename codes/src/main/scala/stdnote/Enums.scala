package stdnote

/**
  * Created by liam on 24/01/2017.
  */

import scala.collection._
trait TypeValue[A] {
  def code: A
  def name: Option[String]
  def description: Option[String]
}


class TypeValueEnum[A] {
  private val vmap: mutable.Map[A, TypeValue[A]] = new mutable.HashMap

  def Enum(typeValue : TypeValue[A]): TypeValue[A] = {
    vmap(typeValue.code) = typeValue
    typeValue
  }

  def values: Iterable[TypeValue[A]] = vmap.values
  def withCode(code : A): TypeValue[A] = withCodeOption(code).get
  def withName(name : String): TypeValue[A] = withNameOption(name).get
  def withCodeOption(code : A): Option[TypeValue[A]] = vmap.get(code)
  def withNameOption(name : String): Option[TypeValue[A]] = vmap.find(_._2.name == name).map(_._2)
}


trait TypeValueWithMember {
  type A
  def code: A
  def name: Option[String]
  def description: Option[String]
}

object TypeValueWithMember {
  type Aux[A0] = TypeValueWithMember { type A = A0 }
}

trait TypeValueEnumWithMember {
  type Code
  type EnumVal <: TypeValueWithMember.Aux[Code]

  def Enum(typeValue : EnumVal): EnumVal = {
    vmap(typeValue.code) = typeValue
    typeValue
  }

  private val vmap: mutable.Map[Code, TypeValueWithMember.Aux[Code]] = new mutable.HashMap()


  def values: Iterable[TypeValueWithMember] = vmap.values
  def withCode(code : Code): TypeValueWithMember = withCodeOption(code).get
  def withName(name : String): TypeValueWithMember = withNameOption(name).get
  def withCodeOption(code : Code): Option[TypeValueWithMember] = vmap.get(code)
  def withNameOption(name : String): Option[TypeValueWithMember] = vmap.find(_._2.name == name).map(_._2)
}

object TypeValueEnumWithMember {
  type Aux[Code0] = TypeValueEnumWithMember {
    type Code = Code0
  }
}
