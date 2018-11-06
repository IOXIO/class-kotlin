package ch04.Button1
import java.io.Serializable

/*
리스트 4.11
중첩 클래스를 사용해 코틀린에서 View 구현하기
*/

interface State: Serializable

interface View {
    fun getCurrentState(): State
    fun restoreState(state: State) {}
}

class Button : View {
    override fun getCurrentState(): State = Button.ButtonState()
    override fun restoreState(state: State) {}

    class ButtonState : State {} // 코틀린 중첩 클래스에 아무런 변경자가 붙지 않으면 자바 static 중첩 클래스와 같다.
}


/*
Java Code

public class Button implements View {
    @Override
    public State getCurrentState(){
        return new ButtonState()
    }

    @Override
    public void restoreState(State state) {}
    public class ButtonState implements State {}



*/