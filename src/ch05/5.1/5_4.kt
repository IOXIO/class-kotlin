data class Person(val name: String, val age: Int)

fun main(args: Array<String>){
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    println(people.maxBy { it.age })
    println(people.maxBy { it.name })
    println(people.minBy { it.name })
    // 출력: Person(name=Bob, age=31)
    // 출력: Person(name=Bob, age=31)
    // 출력: Person(name=Alice, age=29)
}