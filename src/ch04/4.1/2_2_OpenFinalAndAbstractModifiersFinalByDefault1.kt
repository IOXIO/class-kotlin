package ch04.ex1_2_2_OpenFinalAndAbstractModifiersFinalByDefault1

/*
리스트 4.7
오버라이드 금지하기
*/

interface Clickable {
    fun click()
    fun showOff() = println("I'm clickable!")
}

open class RichButton : Clickable {
    final override fun click() {}
}
