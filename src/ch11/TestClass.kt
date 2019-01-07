package ch11

fun main(args: Array<String>) {
    hello {
        print(this)
    }
}

fun hello(block: String.() -> Unit) {
    "Hello1".block()
    block("Hello2")
}