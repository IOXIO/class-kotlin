package ch04.ex1_5_1_SealedClassesDefiningRestrictedClassHierarchies

/*
리스트 4.12
인터페이스 구현을 통해 식 표현하기
*/

interface Expr
class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) : Expr

// 코틀린 컴파일러는 when을 사용해 Expr 타입의 값을 검사할 때,
// 꼭 디폴트 분기인 else 분기를 덧붙이게 강제한다.
fun eval(e: Expr): Int =
        when(e){
            is Num -> e.value
            is Sum -> eval(e.right) + eval(e.left)
            else ->
                    throw IllegalArgumentException("Unknown expression")
        }