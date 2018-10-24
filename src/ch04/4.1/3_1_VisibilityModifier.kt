package ch04.ex1_3_1_VisibilityModifier
import ch04.main.Focusable

internal open class TalkativeButton : Focusable {
    private fun yell() = println("Hey!")
    protected fun whisper() = println("Let's talk!")
}

//fun TalkativeButton.giveSpeech(){ // 오류 public 멤버가 자신의 internal 수신 타입인 TalkativeButton을 노출함
//    yell() // yell에 접근 불가능. yell은 TalkativeButton의 private 멤버임
//    whisper() // whisper에 접근 불가능. whisper는 Talkative의 protected 멤버임
//}

// 컴파일 오류를 없애기 위해서는?
// 1.giveSpeech 확장 함수의 가시성을 internal로 바꿔준다.
// 2.TalkativeButton 클래스의 가시성을 public으로 바꿔준다.
