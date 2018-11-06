data class Person(val name: String, val age: Int)

fun <T> Collection<T>.joinToString(
  separator: String = ", ",
  prefix: String = "",
  postfix: String = ""
):String {
  var result = StringBuilder(prefix)
  for ((index, element) in this.withIndex()) {
    if (index > 0) result.append(separator)
    result.append(element)
  }
  result.append(postfix)
  return result.toString()
}

fun main(arge: Array<String>) {
  val people = listOf(Person("이몽룡", 29), Person("성춘향", 31))
  val names = people.joinToString(" ") { p: Person -> p.name }
  println(names)
}