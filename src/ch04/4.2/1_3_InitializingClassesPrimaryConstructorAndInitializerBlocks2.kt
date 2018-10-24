package ch04.ex2_1_3_InitializingClassesPrimaryConstructorAndInitializerBlocks2

// 주 생성자 앞에 별다른 애노테이션이나 가시성 변경자가 없다면 constructor 생략 가능
class User(_nickname: String) {
    val nickname = _nickname
}