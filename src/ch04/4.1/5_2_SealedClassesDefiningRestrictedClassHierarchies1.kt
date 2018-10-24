package ch04.ex1_5_2_SealedClassesDefiningRestrictedClassHierarchies1

sealed class Expr { // 기반 클래스를 sealed로 봉인하다.
    class Num(val value: Int) : Expr() // 기반 클래스의 모든 하위 클래스를 중첩 클래스로 나열한다.
    class Sum(val left: Expr, val right: Expr) : Expr()
}

fun eval(e: Expr): Int =
        when(e) { // when 식이 모든 하위 클래스를 검사하므로 별도의 else 분기가 없어도 된다.
            is Expr.Num -> e.value
            is Expr.Sum -> eval(e.right) + eval(e.left)
        }
