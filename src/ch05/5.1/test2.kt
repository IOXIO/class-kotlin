data class Person(val name: String, val age: Int)

fun main(args: Array<String>) {
  val createPerson = ::Person
  val p = createPerson("Alice", 29)
  println(p)
}