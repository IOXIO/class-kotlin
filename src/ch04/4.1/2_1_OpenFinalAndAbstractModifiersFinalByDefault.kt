package ch04.ex1_2_1_OpenFinalAndAbstractModifiersFinalByDefault

/*
리스트 4.6
열린 메소드를 포함하는 열린 클래스 정의
*/

interface Clickable{
    fun click()
    fun showOff() = println("I'm clickable!")
}

open class RichButton : Clickable { // 이 클래스는 열려있다. 다른 클래스가 이 클래스를 상속할 수 있다.
    fun disable() {} // 이 함수는 파이널이다. 하위 클래스가 이 메소드를 오버라이드 할 수 없다.
    open fun animate() {} // 이 함수는 열려있다. 하위 클래스에서 이 메소드를 오버라이드해도 된다.
    override fun click() {} // 이 함수는 (상위 클래스에서 선언된) 열려있는 메소드를 오버라이드한다. 오버라이드한 메소드는 기본적으로 열려있다.
}


open class RichButton1 : Clickable {
    final override fun click() {} // 오버라이드하지 못하게 금지하려면 함수 앞에 final을 명시해야 한다.
}
