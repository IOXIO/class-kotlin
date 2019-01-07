package ch11

import java.lang.StringBuilder

fun main(args: Array<String>) {
    val bsInstance = BuildString()
    val s1 = bsInstance.withHigherOrderFunction(::testFun)
    val s3 = bsInstance.withHigherOrderFunction {

    }

    val s2 = bsInstance.withLambdaReceiver {
        this.append("Hello, ")
        append("World")
    }

    bsInstance.assignmentVariable()

    val createTable = createHTML().table {
        tr {
            td {

            }
        }
    }

    val createDropdown = createHTML().dropdown {
        dropdownButton { /*...*/ }
        dropdownMenu {
            item("#", "Action")
            item("#", "Another Action")
            divider()
            dropdownHeader("Header")
            item("#", "Separated link")
        }
    }

    val invokeClassInstance = InvokeClass()
    // invokeClassInstance.invoke("Cro")
    invokeClassInstance("Cro")

    val sb = StringBuilder()
}

fun testFun(sb: StringBuilder) {
    sb.append("Hello")
}