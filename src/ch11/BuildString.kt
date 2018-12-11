package ch11

class BuildString {
    fun withHigherOrderFunction(
        builderAction: (StringBuilder) -> Unit
    ): String {
        val sb = StringBuilder()
        builderAction(sb)
        return sb.toString()
    }

    fun withLambdaReceiver(
        builderAction: StringBuilder.() -> Unit
    ): String {
        val sb = StringBuilder()
        sb.builderAction()
        return sb.toString()
    }

    fun assignmentVariable() {
        // Excl: Exclude / Exclusive 와 같은 단어를 쓸 때의 관례 인 것 같음.
        val appendExcl: StringBuilder.() -> Unit = { append("!") }

        val stringBuilder = StringBuilder("Hi")
        stringBuilder.appendExcl() // 확장 함수로써의 호출

        println(stringBuilder)

        println(withLambdaReceiver(appendExcl)) // 확장 함수 타입의 수신 객체 지정 람다로써의 사용
    }
}