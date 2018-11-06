package ch04.ex1_1_1_InterfacesInKotlin
/*
리스트 4.1
코틀린 인터페이스
*/

interface Clickable {
    fun click()
}

// 자바에서는 extends와 implements 키워드를 사용
// 코틀린에서는 클래스 이름 뒤에 콜론(:)을 붙이고 인터페이스와 클래스 이름을 적는 것으로 클래스 확장과
// 인터페이스 구현을 모두 처리
class Button : Clickable {
    override fun click() = println("I was clicked")
}

fun main(args: Array<String>){
    Button().click()
    // 출력: I was clicked
}

