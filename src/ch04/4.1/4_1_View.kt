package ch04.View
import java.io.Serializable

/*
리스트 4.9
직렬화할 수 있는 상태가 있는 뷰 선언
*/

// View의 요소를 하나 만든다고 해보자. 그 View의 상태를 직렬화해야 한다.
interface State : Serializable

// View의 인터페이스 안에는 뷰의 상태를 가져와 저장할 때 사용할 메소드가 정의되어 있다.
interface View {
    fun getCurrentState(): State
    fun restoreState(state: State) {}
}

