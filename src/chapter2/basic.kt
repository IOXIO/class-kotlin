
import java.io.BufferedReader
import java.util.*

/**
 * hello kotlin
 * */
/**
 * 코틀린은 함수를 선언할때 fun키워드를 사용
 * 파라미터 이름 뒤에 그 파라미터의 타입을 작성
 * 함수를 최상위로 작성할수있다(꼭 클래스 안에 함수를 넣어야 할 필요가 없다.
 * System.out.pringln 대신 print를 쓴다 (어찌보면 당연한 내용이다 클래스 없이 바로 글로벌 함수를 작성 할수 있으므로 println함수를 실행할수 있는것이다)
 * 세미 콜론을 붙이지 않아도 된다
 *
 * */
//fun main(args:Array<String>){
//    println("Hello, world")
//}



/**
 * 함수
 * */
/**
 * tools -> kotlin -> kotlin REPL
 *
 * if가 다른 언어와 달리 문이 아니라 식으로 쓰인다
 * */
fun max(a:Int, b:Int) :Int{
    return if (a>b) a else b
}

//println(max(1,2));
/***
 * 더욱더 간략하게 표현하면 아래와 같다
 *
 * 모든 함수에는 리턴 타입을 선언 해주어야 한다 그러나 식이 본인인 함수에 굳이 타입을 선언 하지 않아도 컴파일러가 함수 본문의 식을 분석해 타입을을 반환해야 하는 함수에 타입을 정해준다
 * 이렇게 컴퍼일러가 타입을 분석해 개발자 대신 태입을 정해주는 기능을 타입추론이라고 한다
 * 여기서 주의해야할점은 식이 본문인 함수만 타입 생략이 가능하다는것을 잊지 말자
 *
 */
//fun max(a : Int, b : Int) = if (a >b) a else b



/**
 * 변수
 * */
/**
 * 자바에서 변수를 선언할떄 타입이 맨 앞에 온다
 * 코틀린은 타입 지정 생략이 가능하다 만약 타입으로 코틀린이 변수 선언을 시작하면 타입을 생략할경우 식과 변수 선언을 구별할 방법이 없어진다 그런이유로 코틀린에선 키워드로 변수 선언을 시작한다
 *
 * */
var question = "삶 우주 그리고 모든것에 대한 궁극적인 질문"
var answer = 42
/**
 * 초기화 식을 사용하지 않고 변수를 선언하려면 변수 타입을 반드시 명시해야된다. 초기화 식이 없다면 변수에 저장될 값에 아무 정보가 없기때문에 컴파일러가 타입을 추론할수 없다 따라서 그런 경우 타입을 반드시 지정해야된다
 * */
//var answer2 : Int
//answer2 = 42
/**
 * 변수 선언시 사용하는 키워드는 다음과 같은 2가지가 있다
 * 1.val - 변경이 불가능한 참조를 저장하는 변수 like final변수
 * 2.var - 변경이 가능한 참조
 *
 * val이 참조 자체가 불변일지라도 참조가 가리키는 객체의 내부 값은 변경될 수 있다는 사실을 알아둬라.
 *
 * ex) val languages = arrayListOf("java") //불변변수
 * languages.add("Kotlin")// 참조가 가리키는 객체 내부 변경
 *
 * */

/**
 *문자열 템플릿
 * 한글을 쓸때 {}를 감싸라고 하는데 저는 잘됩니다.
 * ${ if(args.size > 0) args[0] else "someone"}
 * */
//fun main(args:Array<String>){
//    val name = if(args.size > 0) args[0] else "반가워요"
//    println("hello, $name")
//}

/**
 * 클래스와 프로퍼티
 *
 * ex)java
 *
 * public class Person {
 *  private final String name;
 *
 *  public Person(String name){
 *      this.name = name;
 *  }
 *
 *  public String getName(){
 *      return name;
 *  }
 * }
 *
 * 만약 필드가 늘어난다면 대입문도 더 늘어 난다
 *
 * 아래에 코드는 코틀린으로 변환한 Person클래스이다
 * */
 //class Person(val name: String)


/**
 * 프로퍼티
 *
 * val로 선언 하면 외부에서 get은 할수 잇지만 set을 할수 없음
 * */

class Person(
        val name : String,
        var isMarried : Boolean
)
var person = Person("Bob", true)

//fun main(args:Array<String>) {
//    println(person.name)
//    println(person.isMarried)
//}


/**
 *커스텀 접근자
 * */

class Rectangle(val height : Int, val width : Int){
    val isSquare : Boolean
        get(){
            return height === width
        }
}
val rectangle = Rectangle(41,43)

//fun main(args:Array<String>){
//    println(rectangle.isSquare)
//}


/**
 * 디렉터리와 패키지
 * shapes 폴더에 가서 확인
 * */



/**
 * enum 클래스
 * 자바와 마찬가지로 eum은 단순히 값만 열거하는 존재가 아님
 * enum 클래스 안에도 프로퍼티나 메소드를 정의 할수 있다
 * 아래의 예제를 확인해보자
 * */
enum class Color(val r : Int, val g: Int, val b: Int){
    RED(255,0,0),
    ORANGE(255,165,0),
    YELLOW(0,255,0),
    GRREN(0,255,0),
    BLUE(0,0,255),
    INDIGO(75,0,130);

    fun rgb() = (r*256+g) * 256 + b
}

//fun main(args:Array<String>){
//    println(Color.BLUE.rgb())
//    println(Color.GRREN.rgb())
//}


/**
 * when으로 enum클래스 다루기
 * 기본적으로 java의 switch문과 비슷하다 다만 case별로 break를 넣지 않아도 된다는 차이가 있다
 * */
fun getMnemonic(color: Color) =
    when(color){
        Color.RED -> "Richard"
        Color.ORANGE -> "Of"
        Color.YELLOW -> "York"
        Color.GRREN -> "Gave"
        Color.BLUE -> "Battle"
        Color.INDIGO -> "In"
    }

fun getWarmth(color : Color) =
        when(color){
            Color.RED, Color.ORANGE, Color.YELLOW -> "Warm"
            Color.GRREN -> "neutral"
            Color.BLUE, Color.INDIGO -> "cold"
        }


//fun main(args:Array<String>){
//    println(getMnemonic(Color.BLUE))
//    println("//////")
//    println(getWarmth(Color.ORANGE))
//}
/**
* enum 상수 값을 임포트해서 enum 클래스 수식자 없이 사용가능하다
* ex) RED -> "Warm"
*
 * 매치 조건이 없을 경우
 * else -> throw Exception("Diry color") //조건이 없으면 이 문장이 실행 된다*
* */


/**
 * 인자 없는 when사용
 * 단 아무 인자를 받지 않으려면 각 부기의 조건이 블리언 결과를 계산해야되는 식이어야 한다
 * */

fun mixOptimiazed(c1: Color, c2: Color) =
   when {
       (c1 === Color.RED && c2 === Color.YELLOW || c1 === Color.YELLOW && c2 === Color.RED) -> "ORANGE"
       (c1 === Color.YELLOW && c2 === Color.BLUE || c1 === Color.BLUE && c2 === Color.YELLOW) -> "GREEN"
       (c1 === Color.BLUE && c2 === Color.YELLOW || c1 === Color.YELLOW && c2 === Color.RED) -> "ORANGE"
       else -> throw Exception("Dirty color")
   }

//fun main(args:Array<String>){
//    println(mixOptimiazed(Color.BLUE,Color.YELLOW));
//}

/**
 * 스마트 캐스트
 * cast 패키지 참조 sum.kt참조 할것
 *
 * 리펙토링 if를 when으로 변경 및 if와 분기에서 블록 사용하기
 * cast 패키지 change_if_to_when.kt참조
 *
 * if와 when의 분기에서 블록 사용
 * cast 패키지 change_if_to_when.kt참조
 *
 * do while은 생략
 * */


/**
 * 수에 대한 이터레이션
 * */
fun fizzBuzz(i : Int) = when {
    i % 15 == 0 -> "FizzBuzz"
    i % 3 == 0 -> "Fizz"
    i % 5 == 0 -> "Buzz"
    else -> "$i"
}

//fun main(args : Array<String>){
//    for(i in 1..100){
//        print(fizzBuzz(i))
//    }
//}

/**
 * 맵에 대한 이터레이션
 * */

val binaryReps = TreeMap<Char, String>()

//fun main(args : Array<String>){
//
//    for(c in 'A'..'F'){ // A부터 F까지 범위를 사용해 이터레이션한다
//        val binary = Integer.toBinaryString(c.toInt()) //아스키 코드를 2진 표현으로 바꾼다.
//         binaryReps[c] = binary // c를 키로  c의 2진 표현을 맵에 넣는다.
//    }
//
//    for((letter, binary) in binaryReps){ // 맵에 대해 이터레이션한다 맵의 키와 값을 두변수에 각각 대입한다
//        println("$letter = $binary")
//    }
//}


/**
 * in으로 컬렉션이나 범위의 원소 검사
 * */

fun isLetter(c : Char) = c in 'a'..'z' || c in 'A'..'z'
fun isNotDigit(c : Char) = c !in '0'..'9'

//fun main(args : Array<String>){
//    println(isLetter('g'))
//    println(isNotDigit('x'))
//}


/**
 * when에서 in사용하기
 * */

fun recognize(c:Char) =
        when(c){
            in '0'..'9' -> "it's a digit"
            in 'a'..'z', in 'A'..'Z' -> "it's a letter"
            else -> "I don't know"
        }

//fun main(args : Array<String>){
//    println(recognize('A'))
//}

/**
 * 코틀린 예외 처리
 *
 * try catch finally
 * */


fun readNumber(reader: BufferedReader) : Int?{ //함수가 던질수 있는 예외를 명시 할 필요가 없다
    try {
        val line = reader.readLine()
        return Integer.parseInt(line) //예외가 발생한다면 이값을
    }
    catch (e:NumberFormatException){
        return null //발생하면null씀
    }
    finally {
        reader.close()
    }
}

//fun main(args : Array<String>){
//    val reader = BufferedReader(StringReader("239"))
//    println(readNumber(reader))
//}

