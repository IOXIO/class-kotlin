package ch04.main
/*
리스트 4.2~6
간단한 인터페이스 선언하기
*/

class Button : Clickable, Focusable {
    override  fun click() = println("I was clicked")

    override fun showOff() {
        super<Clickable>.showOff() // <-> Clickable.super.showOff()
        super<Focusable>.showOff()
    }

    // if 만약, 한 줄이라면
    // override fun showOff() = super<Clickable>.showOff()
}

// 인터페이스 안에 본문이 있는 메소드 정의하기
interface Clickable{
    fun click()
    fun showOff() = println("I'm clickable")
}

// 동일한 메소드를 구현하는 다른 인터페이스 정의
interface Focusable{
    fun setFocus(b: Boolean) =
            println("I ${if (b) "got" else "lost"} focus.")
    fun showOff() = println("I'm focusable");
}

fun main(args: Array<String>) {
    val button = Button()
    button.showOff()
    button.setFocus(true)
    button.click()
}

// I'm clickable
// I'm focusable
// I got focus
// I was clicked