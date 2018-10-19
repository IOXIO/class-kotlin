//코드를 왜이렇게 꼬는지 잘 모르겟음

package chapter2.cast
interface Expr

class Num(val value :Int) : Expr
class Sum(val left: Expr, val right : Expr) : Expr
// sum의 left와 right는 결과적으로 Num, Sum대한 타입을 다 받을수있다

fun eval(e:Expr):Int{
    if(e is Num){
        val n = e as Num
        return  n.value
    }
    if(e is Sum){
        return  eval(e.right)+ eval(e.left)
    }
    throw  IllegalAccessException("Unknown expression")
}

fun main(args : Array<String>){
    println( eval( Sum( Sum( Num(1), Num(2) ),Num(4) ) ) )
}