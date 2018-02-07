package monoclenote

/**
  * Created by Liam.M on 2018. 02. 07..
  */
object Motivation extends App {

  case class Street(number: Int, name: String)
  case class Address(city: String, street: Street)
  case class Company(name: String, address: Address)
  case class Employee(name: String, company: Company)


  val employee = Employee("john", Company("awesome inc", Address("london", Street(23, "high street"))))

  employee.copy(
    company = employee.company.copy(
      address = employee.company.address.copy(
        street = employee.company.address.street.copy(
          name = employee.company.address.street.name.capitalize // luckily capitalize exists
        )
      )
    )
  )

  import monocle.Lens
  import monocle.macros.GenLens

  val company   : Lens[Employee, Company] = GenLens[Employee](_.company)
  val address   : Lens[Company , Address] = GenLens[Company](_.address)
  val street    : Lens[Address , Street]  = GenLens[Address](_.street)
  val streetName: Lens[Street  , String]  = GenLens[Street](_.name)

  val opic = company composeLens address composeLens street composeLens streetName

  // 그닥 편해보이진 않는다.
  println((company composeLens address composeLens street composeLens streetName).modify(_.capitalize)(employee))


  import monocle.function.Cons.headOption // to use headOption (an optic from Cons typeclass)

  // headOption => ? // String => Char
  val e2 = (company composeLens address
    composeLens street
    composeLens streetName
    composeOptional headOption)
    .modify(_.toUpper)

  println(e2)

  import monocle.macros.syntax.lens._

  // lens syntax는 그나마 좀 낫다.
  val e3 = employee.lens(_.company.address.street.name).composeOptional(headOption).modify(_.toUpper)
  println(e3)






}

