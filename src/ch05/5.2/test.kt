data class Person(val name: String, val age: Int)

fun main(arge: Array<String>) {
  val people = listOf(Person("Alice", 29), Person("Bob", 31))
  var maxAge = people.maxBy(Person::age)!!.age
  println(people.filter { it.age == maxAge })
}