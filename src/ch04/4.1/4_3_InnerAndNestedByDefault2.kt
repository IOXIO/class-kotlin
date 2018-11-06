package ch04.ex1_4_3_InnerAndNestedClassesNestedByDefault2

class Outer {
    inner class Inner{
        // 내부 클래스 Inner에서 바깥쪽 클래스 Outer의 참조에 접근하려면 this@Outer라고 써야한다.
        fun getOuterReference(): Outer = this@Outer
    }
}