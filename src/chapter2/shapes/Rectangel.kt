package chapter2.shapes

import java.util.*

class Rectangel(val height: Int, val width : Int ){
    val isSquare : Boolean
        get() = height == width
}

fun crateRandomRectangle() : Rectangel{
    val random = Random();
    return Rectangel(random.nextInt(), random.nextInt())
}

fun main(args:Array<String>){
    val shape = crateRandomRectangle();

    println(shape.isSquare)
    println(shape.height)
    println(shape.width)
}