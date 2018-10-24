package ch04.ex2_1_5_InitializingClassesPrimaryConstructorAndInitializerBlocks3

class User(val nickname: String, val isSubscribed: Boolean = true)

fun main(args: Array<String>){
    val alice = User("Alice")
    println(alice.isSubscribed)

    val bob = User("Bob", false)
    println(bob.isSubscribed)

    val carol = User("Carol", false)
    println(carol.isSubscribed)
}
