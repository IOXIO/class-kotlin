fun printProblmCounts(responses: Collection<String>) {
  var clientErrors = 0
  var setverErrors = 0
  responses.forEach {
    if (it.startsWith("4")) {
      clientErrors++
    } else if (it.startsWith("5")) {
      setverErrors++
    }
  }
  println("$clientErrors client rrors, $setverErrors server errors")
}

fun main(args: Array<String>) {
  val responses = listOf("200 OK", "418 I'm a teapot", "500 Internal Server Error")
  printProblmCounts(responses)
  // 출력: 1 client rrors, 1 server errors
}