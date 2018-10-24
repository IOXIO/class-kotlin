package ch04.ex2_1_2_InitializingClassesPrimaryConstructorAndInitializerBlocks1

// constructor 키워드는 주 생성자나 부 생성자 정의를 시작할 때 사용
class User constructor(_nickname: String){
    val nickname: String

    // 초기화 블록은 주 생성자와 함께 사용된다.
    init {
        nickname = _nickname
    }
}