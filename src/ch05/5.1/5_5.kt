data class Person(val name: String, val age: Int)

fun main(args: Array<String>){
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    println(people.maxBy(Person::age))
    // 출력: Person(name=Bob, age=31)
}