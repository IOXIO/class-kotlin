package ch11

class InvokeClass {
    operator fun invoke(name: String) {
        println("InvokeClass::invoke::$name")
    }
}