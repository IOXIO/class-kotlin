package chapter2.cast

//위에 if를 아래에 when으로 리펙토링

//fun eval1(e:Expr): Int =
//        if(e is Num){
//            e.value
//        }else if(e is Sum){
//            eval1(e.right)+eval1(e.left)
//        }else{
//            throw IllegalAccessException("Unknow expression")
//        }


fun eval1(e: Expr):Int =
        when(e){
            is Num -> e.value
            is Sum -> eval1(e.right) + eval1(e.left)
            else-> throw IllegalAccessException("Unknown expresssion")
        }

//fun main(args : Array<String>){
//    println(eval1(Sum(Num(1),Num(2))))
//}

fun evalWithLogging(e:Expr): Int =
        when(e){
            is Num->{
                println("num : ${e.value}")
                e.value
            }
            is Sum ->{
                val left = evalWithLogging(e.left)
                val right = evalWithLogging(e.right)
                println("Sum : $left + $right")
                left + right
            }

            else -> throw  IllegalAccessException("Unknown espression")
        }

fun main(args : Array<String>){
    println(eval1(Sum(Num(1),Num(2))))
}