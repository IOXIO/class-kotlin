# 7장 연산자 오버로딩과 기타 관례

> 관례 : 여러 연산을 지원하기 위해 특별한 이름이 붙은 메소드

다루는 내용

- 산술 연산자
- 비교 연산자
- 컬렉션과 범위
- 구조 분해 선언과 component
- 위임 프로퍼티




## 7.1 산술 연산자 오버로딩

자바에서는 원시 타입(+```String```까지)에서만 산술 연산자 사용 가능. 

그래서 ```BigInteger``` 같은 클래스를 더하거나 컬렉션에 원소를 추가하는 경우에도 ```+``` 를 사용할 수 없음.

**하지만 코틀린은 가능!!!**



### 7.1.1 이항 산술 연산 오버로딩

#### 리스트7.1 `plus` 함수를 이용한 ```+``` 연산자 구현

```kotlin
fun main(args: Array<String>) {
    val p1 = Point(10, 20)
    val p2 = Point(30, 40)
    println(p1 + p2) // p1 + p2 == p1.plus(p2)
    /*** output ***/
    //Point(x=40, y=60)
}

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}
```

```java
// Decompile
public static final void main(@NotNull String[] args) {
    Point p1 = new Point(10, 20);
    Point p2 = new Point(30, 40);
    Point var2 = p1.plus(p2);
    System.out.println(var2);
}

public final class Point {
   private final int x;
   private final int y;

   @NotNull
   public final Point plus(@NotNull Point other) {
      return new Point(this.x + other.x, this.y + other.y);
   }
   ...
}
```



```plus``` 함수 앞에 ```operator``` 키워드 필수.

![](./images/no_operator.png)



#### 리스트7.2 연산자를 멤버 함수로 만드는 대신 확장 함수로 정의도 가능

```kotlin
fun main(args: Array<String>) {
    operator fun Point.plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
    val p1 = Point(10, 20)
    val p2 = Point(30, 40)
    println(p1 + p2)
    //println(p1.plus(p2))
    /*** output ***/
    //Point(x=40, y=60)
}

data class Point(val x: Int, val y: Int)
```



**직접 작성한 클래스에 대해 관례를 따르는 확장 함수를 만들어도 잘 작동함**

그러나 직접 연산자를 만들어 사용할 수 없고 언어에서 미리 정해둔 연산자만 오버로딩 가능.



오버로딩 가능한 이항 산술 연산자

| 식    | 함수 이름        |
| ----- | ---------------- |
| a * b | times            |
| a / b | div              |
| a % b | mod(1.1부터 rem) |
| a + b | plus             |
| a - b | minus            |



연산자 우선순위는 표준 숫자 타입에 대한 연산자 우선순위와 동일.

```kotlin
fun main(args: Array<String>) {
    operator fun Point.plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
    operator fun Point.times(other: Point): Point {
        return Point(x * other.x, y * other.y)
    }
    val p1 = Point(10, 20)
    val p2 = Point(30, 40)
    val p3 = Point(2, 2)
    println(p1 + p2 * p3)
    /*** output ***/
    //Point(x=70, y=100)

    println((p1 + p2) * p3)
    /*** output ***/
    //Point(x=80, y=120)
}

data class Point(val x: Int, val y: Int)
```



> 코틀린 연산자를 자바에서 호출하기는 쉬우며, 오버로딩한 연산자는 함수로 정의됨.
>
> 자바를 코틀린에서 호출하는 경우 함수 이름이 코틀린 관례에 맞아 떨어지기만 하면 연산자 식을 사용해서 그 함수 호출할 수 있음.
>
> 자바에서는 operator 변경자를 메서드에 적용할 수 없어 이름과 파라미터 개수만 맞추면 됨.

```java
public class Ch07 {
    public int x = 0;

    public Ch07(int x) {
        this.x = x;
    }

    public static void main(String[] args) {
        Point p1 = new Point(10, 20);
        Point p2 = new Point(30, 30);
        System.out.println("java : " + p1.plus(p2));
        /*** output ***/
        //java : Point(x=40, y=50)
    }
}

```

```kotlin
fun main(args: Array<String>) {
    operator fun Ch07.plus(other: Ch07): Ch07 {
        return Ch07(x + other.x)
    }
    val test1 = Ch07(10)
    val test2 = Ch07(20)
    println("kotlin : " + (test1 + test2).x)
    /*** output ***/
    //kotlin : 30
}

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
    operator fun times(other: Point): Point {
        return Point(x * other.x, y * other.y)
    }
}
```



#### 리스트7.3 두 피연산자의 타입이 다른 연산자 정의

```kotlin
fun main(args: Array<String>) {
    data class Point(val x: Int, val y: Int)
    operator fun Point.times(scale: Double): Point {
        return Point((x * scale).toInt(), (y * scale).toInt())
    }
    val p = Point(10, 20)
    println(p * 1.5)
    /*** output ***/
    //Point(x=15, y=30)
}

data class Point(val x: Int, val y: Int)
```

코틀린 연산자가 자동으로 교환 법칙(a op b == b op a인 성질)을 지원하지 않음. 

p * 1.5 외에 1.5 * p를 사용하려면 Double의 times를 정의하여 사용해야 함.



#### 리스트7.4 결과 타입이 피연산자 타입과 다른 연산자 정의

```kotlin
fun main(args: Array<String>) {
    operator fun Char.times(count: Int): String {
        return toString().repeat(count)
    }
    println('a' * 3)
    /*** output ***/
    //aaa
    operator fun Char.times(count: Char): String {
        return toString() + count
    }
    println('a' * '3')
    /*** output ***/
    //a3
}
```

Char를 좌항으로 Int를 우항으로 받아서 String을 돌려 줌.

operator 함수도 오버로딩 가능하여 이름은 같지만 파라미터 타입이 서로 다른 연산자 함수를 만들 수 있음.



> 비트 연산자에 대해 특별한 연산자 함수를 사용하지 않는다.
>
> 코틀린은 표준 숫자 타입에 대해 비트 연산자를 정의하지 않고 중위 연산자 표기법을 지원하는 일반 함수를 사용해 비트 연산을 수행함
>
> | 코틀린  | 설명(자바)                          |
> | ----- | ---------------- |
> | shl  | 왼쪽 시프트(<<)                          |
> | shr  | 오른쪽 시프트(부호 비트 유지, >>)        |
> | ushr | 오른쪽 시프트(0으로 부호 비트 설정, >>>) |
> | and  | 비트 곱 (&)                              |
> | or   | 비트 합 (\|)                             |
> | xor  | 비트 배타 합 (^)                         |
> | inv  | 비트반전 (~)                             |
>



### 7.1.2 복합 대입 연산자 오버로딩

`plus` 같은 연산자를 오버로딩하면 `+` 뿐만 아니라 `+=`도 자동으로 지원함.

`+=`, `-=` 등의 연산자는 복합 대입 연산자라 함.

```kotlin
fun main(args: Array<String>) {
    var point = Point(1, 2)
    point += Point(3, 4)
    //Decompile : point = point.plus(new Point(3, 4));
    println(point)
    /*** output ***/
    //Point(x=4, y=6)
}
data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}
```

컬렉션에 원소를 추가하는  경우 등 원래 객체의 내부 상태를 변경하게 만들고 싶은 경우가 있음.

이 경우 반환 타입이 `Unit`인 `plusAssign` 함수를 정의하면 `+=` 연산자에 그 함수를 사용함.

`minusAssign`, `timesAssign` 등의 다른 복합 대인 연산자 함수도 사용함.

```kotlin
fun main(args: Array<String>) {
    val numbers = ArrayList<Int>()
    numbers += 42
    //Decompile : var2.add(var3);
    println(numbers)
    /*** output ***/
    //[42]
}
```

_Collection.kt

```kotlin
/**
 * Returns a list containing all elements of the original collection and then the given [element].
 */
public operator fun <T> Collection<T>.plus(element: T): List<T> {
    val result = ArrayList<T>(size + 1)
    result.addAll(this)
    result.add(element)
    return result
}
```

MutableCollection.kt

```kotlin
/**
 * Adds the specified [element] to this mutable collection.
 */
@kotlin.internal.InlineOnly
public inline operator fun <T> MutableCollection<in T>.plusAssign(element: T) {
    this.add(element)
}
```



어떤 클래스가 `plus`와 `plusAssign` 두 함수를 모두 정의하고 둘 다 +=에 사용 가능한 경우 컴파일러는 오류를 보고함.

이를 해결하기 위해서는 일반 연산자를 사용하거나 var를 val로 바꿔서 plusAssign 적용이 불가능하게 할 수도 있음.

plus와 plusAssign 연산을 동시에 정의하지 말 것.

코틀린 표준 라이브러리는 컬렉션에 대해 두 가지 접근 방법을 제공함

* `+`나 `-`는 항상 새로운 컬렉션 반환
* `+=`나 `-=`는 항상 변경 가능한 컬렉션에 작용해 메모리에 있는 객체 상태를 변화시킴.
  읽기 전용 컬렉션에서 `+=`나 `-=`는 변경을 적용한 복사본을 반환함.

```kotlin
fun main(args: Array<String>) {
    val list = arrayListOf(1, 2)
    list += 3
    val newList = list + listOf(4, 5)
    println(list)
    /*** output ***/
    //[1, 2, 3]
    println(newList)
    /*** output ***/
    //[1, 2, 3, 4, 5]
}
```



### 7.1.3 단항 연산자 오버로딩

단항 연산자를 오버로딩하기 위해 사용하는 함수는 인자를 취하지 않음.

####  리스트7.5 단항 연산자 정의

```kotlin
fun main(args: Array<String>) {
    operator fun Point.unaryMinus(): Point {
        return Point(-x, -y)
    }
    val p = Point(10, 20)
    println(-p)
    /*** output ***/
    //Point(x=-10, y=-20)
}

data class Point(val x: Int, val y: Int)
```

오버로딩할 수 있는 단항 연산자

| 식       | 함수이름   |
| -------- | ---------- |
| +a       | unaryPlus  |
| -a       | unaryMinus |
| !a       | not        |
| ++a, a++ | inc        |
| --a, a-- | dec        |

#### 리스트7.6 증가 연산자 정의

```kotlin
import java.math.BigDecimal

fun main(args: Array<String>) {
    operator fun BigDecimal.inc() = this + BigDecimal.ONE
    var bd = BigDecimal.ZERO
    println(bd++)
    /*** output ***/
    //0
    println(++bd)
    /*** output ***/
    //2
}
```



## 7.2 비교 연산자 오버로딩

자바에서는 객체 간 비교를 위해 `equals`나 `compareTo`를 사용하나 코틀린에서는 `==`비교 연산자를 직접 사용할 수 있음.

### 7.2.1 동등성 연산자: `equals`

> 4.3.1 모든 클래스가 정의해야 하는 메소드
>
> 문자열 표현 : `toString()`
>
> 객체의 동등성 : `equals()` => 코틀린에서`==`는 내부적으로 `equals`를 호출함.
>
> 해시 컨테이너 : `hashCod()`

`!=` 연산자를 사용하는 식도 `equals` 호출로 컴파일됨.

`==`와 `!=`는 내부에서 인자가 널인지 검사함.

`data` 클래스의 경우 컴파일러가 자동으로 `equals`를 생성함.

#### 리스트7.7 `equals` 메소드 구현

```kotlin
fun main(args: Array<String>) {
    println(Point(10, 20) == Point(10, 20))
    /*** output ***/
    //true
    println(Point(10, 20) == Point(5, 5))
    /*** output ***/
    //false
    println(null == Point(1, 2))
    /*** output ***/
    //false
}

class Point(val x: Int, val y: Int) {
    override fun equals(obj: Any?): Boolean {
        if(obj === this) return true
        if(obj !is Point) return false
        return obj.x == x && obj.y == y
    }
}
```

식별자 비교 연산자 `===`를 사용해 `equals`의 파라미터가 수신 객체와 같은지 확인.(자바 `==` 연산자와 동일)

`equals` 함수에는 `override`가 붙음. 

다른 연산자 오버로딩 관례와 달리 `equals`는 `Any`에 정의된 메소드이므로 `override`가 필요.

```kotlin
package kotlin
public open class Any {
    public open operator fun equals(other: Any?): Boolean
    public open fun hashCode(): Int
    public open fun toString(): String
}
```

```kotlin
public class Int private constructor() : Number(), Comparable<Int> {
    ...
    public operator fun plus(other: Int): Int
    ...
    public operator fun minus(other: Int): Int
    ...
}
```



`Any`의 `equals`에는 `opertor`가 붙어있지만 오버라이드하는 메소드 앞에는 `opertor`를 붙이지 않아도 지정이 적용됨.

> `Any`에서 상속받은 `equals`가 확장 함수보다 우선순위가 높기 때문에 `equals`를 확장 함수로 정의할 수는 없음



### 7.2.2 순서 연산자: `compareTo`

자바에서 비교해야 하는 알고리즘에 사용할 클래스는 `Comparable` 인터페이스를 구현해야 함.

`Comparable`에 들어있는 `compareTo` 메소드는 객체 간의 크기를 비교해 정수로 나타냄.

```java
    public static void main(String[] args) {
        Ch07 ch07 = new Ch07(0);
        //ch07.operatorAndJava();        // 연산자 함수와 자바
        ch07.ch0708();
    }

    public void ch0708() {
        Peaple p1 = new Peaple("Alice", "Smith");
        Peaple p2 = new Peaple("Bob", "Johnson");
        System.out.println(p1.compareTo(p2));
        /*** output ***/
        //-1
        System.out.println(p2.compareTo(p1));
        /*** output ***/
        //1
    }

    class Peaple implements Comparable<Peaple> {
        private String lastName;
        private String firstName;
        public Peaple(String lastName, String firstName) {
            this.lastName = lastName;
            this.firstName = firstName;
        }
        @Override
        public int compareTo(@NotNull Peaple other) {
            return lastName.compareTo(other.lastName);
        }
    }
```

#### 리스트7.8 compareTo 메소드 구현

```kotlin
fun main(args: Array<String>) {
    val p1 = Person("Alice", "Smith")
    val p2 = Person("Bob", "Johnson")
    println(p1 < p2)
    /*** output ***/
    //false
}

class Person(val firstName: String, val lastName: String)
: Comparable<Person> {
    override fun compareTo(other: Person): Int {
        return compareValuesBy(this, other, Person::lastName, Person::firstName)
    }
}
```

 `Person` 객체의 `Comparable` 인터페이스를 자바 쪽에 컬렉션 정렬 메소드 등에도 사용 가능함.

```java
        Person pe1 = new Person("Alice", "Smith");
        Person pe2 = new Person("Bob", "Johnson");
        System.out.println(p1.compareTo(p2));
        /*** output ***/
        //-1
        System.out.println(p2.compareTo(p1));
        /*** output ***/
        //1
```

`equals`와 마찬가지로 `Comparable`의 `compareTo`에도 `operator` 변경자가 붙어있으므로 오버라이딩 함수에 `operator`를 붙일 필요 없음.



## 7.3 컬렉션과 범위에 대해 쓸 수 있는 관례

컬렉션에서 a[b] 식과 같이 인덱스 연산자를 사용해 원소를 설정하거나 가져올 수 있음.

`in` 연산자는 원소가 컬렉션이나 범위에 속하는지 검사하거나 컬렉션에 있는 원소를 이터레이션할 때 사용.

사용자 지정 클래스에 이런 연산 추가 가능.

### 7.3.1 인덱스로 원소에 접근: `get`과 `set`

코틀린에서 맵의 원소에 접근하거나 자바에서 배열 원소에 접근시

```kotlin
val value = map[key]
mutableMap[key] = newValue
```

코틀린에서 인덱스 연산자 관례

- `get` : 인덱스 연산자를 사용해 원소를 읽는 연산자 메소드
- `set` : 원소를 쓰는 연산자 메소드

Map과 MutableMap 인터페이스에 들어 있음.

#### 리스트7.9 `get` 관례 구현

```kotlin
fun main(args: Array<String>) {
    val p = Point(10, 20)
    println("p = ${p[0]}, ${p[1]}")
    /*** output ***/
    //p = 10, 20
}

data class Point(val x: Int, val y: Int)
operator fun Point.get(index: Int): Int {
    return when(index) {
        0 -> x
        1 -> y
        else ->
        throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}
```

`get` 메소드를 만들고 `operator` 변경자를 붙이면 됨.

맵 인덱스 연산의 경우 `get`의 파라미터 타입이 맵의 키 타입과 같은 임의의 타입이 될 수 있음.

```kotlin
public interface List<out E> : Collection<E> {
    ...
    public operator fun get(index: Int): E
}
...
public interface Map<K, out V> {
    ...
    public operator fun get(key: K): V?
}
```

2차원 배열을 표현하는 클래스 등에는 `get`의 파라미터가 여러 개 정의할 수 있음.

#### 리스트7.10 `set` 관례 구현

```kotlin
fun main(args: Array<String>) {
    val p = MutablePoint(10, 20)
    p[1] = 42
    println(p)
    /*** output ***/
    //MutablePoint(x=10, y=42)
}

data class MutablePoint(var x: Int, var y: Int)

operator fun MutablePoint.set(index: Int, value: Int) {
    when(index) {
        0 -> x = value
        1 -> y = value
        else ->
        throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}
```

`set` 메소드의 `index` 파라미터는 인덱스 연산자`[]`에 들어가고, `value` 파라미터값이 대입문 우항에 들어감.



### 7.3.2 `in` 관례

객체가 컬렉션에 들어있는지 검사(membership test)함. (대응하는 함수는 `contains`)

#### 리스트7.11 `in` 관례 구현

```kotlin
fun main(args: Array<String>) {
    val rect = Rectangle(Point(10, 20), Point(50, 50))
    println(Point(20, 30) in rect)
    /*** output ***/
    //true
    println(Point(5, 5) in rect)
    /*** output ***/
    //false
}

data class Rectangle(val upperLeft: Point, val lowerRight: Point)

operator fun Rectangle.contains(p: Point): Boolean {
    return p.x in upperLeft.x until lowerRight.x &&
    p.y in upperLeft.y until lowerRight.y
}
```

`rect`가 수신 객체가 되고, `in` 좌항에 `Point` 객체가 `contains` 메소드에 인자로 전달됨.

> 열린 범위 : 끝 값을 포함하지 않는 범위. `10 until 20` == 10 이상 19이하인 범위
>
> 닫힌 범위 : 10..20 == 10이상 20이하인 범위

### 7.3.3 `rangeTo` 관례

범위를 만들려면 `..` 구문을 사용해야 함.

`..` 연산자는 `rangeTo` 함수를 간략하게 표현하는 방법.

코틀린 표준 라이브러리에는 모든 `Comparable` 객체에 `rangeTo` 함수가 들어감.

```kotlin
//Ranges.kt
public operator fun <T: Comparable<T>> T.rangeTo(that: T): ClosedRange<T> = ComparableRange(this, that)
```

#### 리스트7.12 날짜의 범위 다루기
```kotlin
import java.time.LocalDate
fun main(args: Array<String>) {
    val now = LocalDate.now()
    val vacation = now..now.plusDays(10)
    println(now.plusWeeks(1) in vacation)
    /*** output ***/
    //true
    
    val n = 9
    println(0..(n + 1))
    /*** output ***/
    //0..10
    
    (0..n).forEach { print(it)}
    /*** output ***/
    //0123456789
}
```
`rangeTo`함수는 `LocalDate`의 멤버가 아니지만 `Comparable`에 대한 확장 함수로 사용 가능



### 7.3.4 `for` 루프를 위한 `iterator` 관례
`for` 루프는 범위 검사와 똑같이 `in` 연산자 사용하지만 의미가 다름.

코틀린에서는 `iterator` 메소드를 확장 함수로 정의하여 사용할 수 있음.

`String`의 상위 클래스인 `CharSequence`에 대한 `iterator` 확장 함수를 제공함.

```kotlin
// Strings.kt
public operator fun CharSequence.iterator(): CharIterator = object : CharIterator() {
    private var index = 0
    public override fun nextChar(): Char = get(index++)
    public override fun hasNext(): Boolean = index < length
}
```
#### 리스트7.13 날짜 범위에 대한 이터레이터 구현
```kotlin
import java.time.LocalDate
fun main(args: Array<String>) {
    for(c in "abc") {
        println(c)
    }
    /*** output ***/
    //a
    //b
    //c

    val newYear = LocalDate.ofYearDay(2017, 1)
    val daysOff = newYear.minusDays(1)..newYear
    for(dayOff in daysOff) {
        println(dayOff)
    }
    /*** output ***/
    //2016-12-31
    //2017-01-01
}

operator fun ClosedRange<LocalDate>.iterator():Iterator<LocalDate> =
object : Iterator<LocalDate> {
    var current = start
    override fun hasNext() = current <=endInclusive
    override fun next() = current.apply {
        current = plusDays(1)
    }
}
```


## 7.4 구조 분해 선언과 `component` 함수
구조 분해를 사용하면 복합적인 값을 분해해서 여러 다른 변수를 한꺼번에 초기화할 수 있음.
```kotlin
fun main(args: Array<String>) {
    val p = Point(10, 20)
    val (x, y) = p
    println(x)
    /*** output ***/
    //10
    println(y)
    /*** output ***/
    //20
}
```

내부에서 구조 분해 선언은 다시 관례를 사용하며, 각 변수를 초기화하기 위해 `componentN`이라는 함수를 호출함.

여기서 N은 구조 분해 선언에 있는 변수 위치에 따라 붙는 번호임.

`data` 클래스의 주 생성자에 들어있는 프로퍼티에 대해서는 컴파일러가 자동으로 `componentN` 함수를 만들어 줌.

```kotlin
fun main(args: Array<String>) {
    val p = Point(10, 20)
    val (x, y) = p
    println(x)
    /*** output ***/
    //10
    println(y)
    /*** output ***/
    //20
}

class Point(val x: Int, val y: Int){
    operator fun component1() = x
    operator fun component2() = y
}
```
#### 리스트7.14 구조 분해 선언을 사용해 여러 값 반환
```kotlin
fun main(args: Array<String>) {
    val (name, ext) = splitFilename("example.kt")
    println(name)
    /*** output ***/
    //example
    println(ext)
    /*** output ***/
    //kt
}

data class NameComponents(val name: String, val extension: String)
fun splitFilename(fullName: String): NameComponents {
    val result = fullName.split('.', limit = 2)
    return NameComponents(result[0], result[1])
}
```

크기가 정해진 컬렉션을 다루는 경우 더 유용함.

#### 리스트7.15 컬렉션에 대해 구조 분해 선언 사용

```kotlin
fun main(args: Array<String>) {
    val (name, ext) = splitFilename("example2.kt2")
    println(name)
    /*** output ***/
    //example2
    println(ext)
    /*** output ***/
    //kt2
}
data class NameComponents(val name: String, val extension: String)
fun splitFilename(fullName: String): NameComponents {
    val(name, extension) = fullName.split('.', limit = 2)
    return NameComponents(name, extension)
}
```
이런 구문을 무한정 사용할 수는 없으나 컬렉션에 대한 구조 분해는 유용함.

**코틀린 표준 라이브러리에서는 맨 앞의 다섯 원소에 대한 `componentN`을 제공함**

```kotlin
fun main(args: Array<String>) {
    val x = listOf(1, 2)

    val (a, b, c, d, e) = x
    /*** output ***/
    //Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 2
    //at java.util.Arrays$ArrayList.get(Arrays.java:3841)

    //val (a, b, c, d, e, f) = x
    /*** output ***/
    //Error:(116, 30) Kotlin: Destructuring declaration initializer of type List<Int> must have a 'component6()' function
}
```
표준 라이브러리의 `Pair`나 `Triple` 클래스를 사용하면 여러 값을 더 간단하게 반환할 수 있음.



### 7.4.1 구조 분해 선언과 루프

변수 선언이 들어갈 수 있는 장소라면 어디든 구조 분해 선언을 사용할 수 있음.

맵의 원소에 대해 이터레이션할 때 등이 구조 분해 선언이 유용함.

#### 리스트7.16 구조 분해 선언을 사용해 맵 이터레이션하기

```kotlin
fun main(args: Array<String>) {
    val map = mapOf("Oracle" to "Java", "JetBrains" to "Kotlin")
    printEntries(map)
    /*** output ***/
    //Oracle -> Java
    //JetBrains -> Kotlin
}

fun printEntries(map: Map<String, String>) {
    for((key, value) in map) {
        println("$key -> $value")
    }
}
```

위 예제는 객체를 이터레이션하는 관례와 구조 분해 선언이 사용됨.

코틀린 표준 라이브러리에는 맵에 대한 확장 함수로 iterator가 들어가 있음.

그리고 `Map.Entry`에 대한 확장 함수로 `component1`(key)과 `component2`(value)를 제공함.

```kotlin
//Maps.kt
@kotlin.internal.InlineOnly
public inline operator fun <K, V> Map.Entry<K, V>.component1(): K = key
...
@kotlin.internal.InlineOnly
public inline operator fun <K, V> Map.Entry<K, V>.component2(): V = value
...
@kotlin.internal.InlineOnly
public inline operator fun <K, V> Map<out K, V>.iterator(): Iterator<Map.Entry<K, V>> = entries.iterator()
```



## 7.5 프로퍼티 접근자 로직 재활용: 위임 프로퍼티

**위임 프로퍼티(delegated property) : 코틀린이 제공하는 관례에 의존하는 특성 중 독특하면서 강력한 기능**

위임 프로퍼티를 사용하면 값을 단순히 필드에 저장하는 것보다 복잡한 방식으로 작동하는 프로퍼티를 쉽게 구현할 수 있음.

예를 들어 프로퍼티는 위임을 사용해 값을 필드가 아니라 DB 테이블이나 브라우저 세션, 맵 등에 저장할 수 있음.

**위임 : 객체가 직접 작업을 수행하지 않고 다른 도우미 객체(위임 객체, delegate)가 그 작업을 처리하게 맡기는 디자인 패턴**



### 7.5.1 위임 프로퍼티 소개

위임 프로퍼티의 일반적인 문법

```kotlin
class Foo {
    var p: Type by Delegate
}
```

프로퍼티 위임 관례를 따르는 `Delegate` 클래스는 `getValue`와 `setValue` 메소드를 제공해야 함.

`getValue`와 `setValue` 는 멤버 메소드이거나 확장 함수일 수 있음.

```kotlin
import kotlin.reflect.KProperty

fun main(args: Array<String>) {
    val e = Example()
    println(e.p)
    /*** output ***/
    //Example@6193b845, thank you for delegating 'p' to me!
    e.p = "New"
    /*** output ***/
    //New has been assigned to 'p' in Example@6193b845.
}

class Delegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$value has been assigned to '${property.name}' in $thisRef.")
    }
}
class Example {
    var p: String by Delegate()
}
```

`e.p`는 일반 프로퍼티처럼 쓸 수 있지만 실제로 `p`의 게터나 세터는 `Delegate` 타입의 위임 프로퍼티 객체에 있는 메소드를 호출함.



### 7.5.2 위임 프로퍼티 사용: `by lazy()`를 사용한 프로퍼티 초기화 지연

지연 초기화(lazy initialization)는 객체의 일부분을 초기화하지 않고 남겨뒀다가 실제 값이 필요할 경우 초기화할 때 흔히 쓰이는 패턴.

초기화 과정에 자원을 많이 사용하거나 객체를 사용할 때마다 꼭 초기화하지 않아도 되는 프로퍼티에 대해 사용할 수 있음.

#### 리스트7.17 지연 초기화를 뒷받침하는 프로퍼티를 통해 구현

```kotlin
fun main(args: Array<String>) {  
    val p = Person("Alice")
    p.emails
    /*** output ***/
    //Load emails for Alice
    p.emails
    /*** output ***/
}

class Email

class Person(val name: String) {
    private var _emails: List<Email>? = null
    val emails: List<Email>
        get() {
            if(_emails == null) {
                _emails = loadEmails(this)
            }
            return _emails!!
        }
}

fun loadEmails(person: Person2): List<Email> {
    println("Load emails for ${person.name}")
    return listOf()
}
```

뒷받침하는 프로퍼티(backing property) 기법을 사용하여 _emails에 값을 저장하고  emails에서 읽기 처리함.

위 코드는 지연 초기화해야 할 프로퍼티가 많아지면 성가시고, 스레드 안전하지 않음.

위임 프로퍼티를 사용하면 훨씬 간단해짐.

위임 프로퍼티는 뒷받침하는 프로퍼티와 값이 오직 한번만 초기화됨을 보장하는 게터 로직을 함께 캡슐화해줌.

위임 객체를 반환하는 표준 라이브러리 함수가 `lazy`임.

#### 리스트7.18 지연 초기화를 위임 프로퍼티를 통해 구현하기

```kotlin
fun main(args: Array<String>) {  
    val p = Person("Alice")
    p.emails
    /*** output ***/
    //Load emails for Alice
    p.emails
    /*** output ***/
}

class Email

class Person(val name: String) {
    val emails by lazy {loadEmails(this)}
}

fun loadEmails(person: Person): List<Email> {
    println("Load emails for ${person.name}")
    return listOf()
}
```

`lazy` 함수는 코틀린 관례에 맞는 시그니처의 `getValue` 메소드가 들어있는 객체를 반환함

`lazy` 함수의 인자는 값을 초기화할 때 호출할 람다임.

`lazy` 함수는 기본적으로 스레드 안전함.



### 7.5.3 위임 프로퍼티 구현

어떤 객체의 프로퍼티가 바뀔 때마다 리스너에게 변경 통지를 보내야 할 때.

자바에서는 `PropertyChangeSupport`와 `PropertyChangeEvent`클래스를 사용.

* `PropertyChangeSupport` : 리스너의 목록을 관리
* `PropertyChangeEvent` : 이벤트가 들어오면 목록의 모든 리스너에게 이벤트 통지

#### 리스트7.19 `PropertyChangeSupport` 를 사용하기 위한 도우미 클래스

```kotlin
import java.beans.PropertyChangeSupport

open class PropertyChangeAware {
    protected val changeSupport = PropertyChangeSupport(this)

    fun addPropertyChangeListener(listener: PropertyChangeListener) {
        changeSupport.addPropertyChangeListener(listener)
    }

    fun removePropertyChangeListener(listener: PropertyChangeListener) {
        changeSupport.removePropertyChangeListener(listener)
    }
}
```

#### 리스트7.20 프로퍼티 변경 통지를 직접 구현

```kotlin
fun main(args: Array<String>) {  
    val p = Person("Dmitry", 34, 2000)
    p.addPropertyChangeListener(
            PropertyChangeListener { event->
                println("Property ${event.propertyName} changed from ${event.oldValue} to ${event.newValue}")
            }
    )
    p.age = 35
    /*** output ***/
    //Property age changed from 34 to 35
    p.salary = 2100
    /*** output ***/
    //Property salary changed from 2000 to 2100
}

class Person(val name: String, age: Int, salary: Int): PropertyChangeAware() {
    var age: Int = age
    set(newValue) {
        val oldValue = field
        field = newValue
        changeSupport.firePropertyChange("age", oldValue, newValue)
    }

    var salary: Int =salary
    set(newValue) {
        val oldValue = field
        field = newValue
        changeSupport.firePropertyChange("salary", oldValue, newValue)
    }
}
```

`field` 키워드를 통해 `age`와 `salary` 프로퍼티를 뒷받침하는 필드에 접근하는 방법.

세터 코드 중복이 많아 보임.

#### 리스트 7.21 도우미 클래스를 통해 프로퍼티 변경 통지 구현

```kotlin
fun main(args: Array<String>) { 
    val p = Person("Dmitry", 34, 2000)
    p.addPropertyChangeListener(
            PropertyChangeListener { event->
                println("Property ${event.propertyName} changed from ${event.oldValue} to ${event.newValue}")
            }
    )
    p.age = 35
    /*** output ***/
    //Property age changed from 34 to 35
    p.salary = 2100
    /*** output ***/
    //Property salary changed from 2000 to 2100
}

class ObservableProperty(val propName: String, var propValue: Int, val changeSupport: PropertyChangeSupport) {
    fun getValue(): Int = propValue
    fun setValue(newValue: Int) {
        val oldValue = propValue
        propValue = newValue
        changeSupport.firePropertyChange(propName, oldValue, newValue)
    }
}

class Person(val name: String, age: Int, salary: Int): PropertyChangeAware() {
    val _age = ObservableProperty("age", age, changeSupport)
    var age: Int
        get() = _age.getValue()
        set(value) { _age.setValue(value) }

    val _salary = ObservableProperty("salary", salary, changeSupport)
    var salary: Int
        get() = _salary.getValue()
        set(value) { _salary.setValue(value) }
}
```

위 코드는 코틀린의 위임이 실제로 작동하는 방식과 비슷함.

로직의 중복을 상단 부분 제거하였지만 각각의 프로퍼티마다 `ObservableProperty`를 만들고 게터와 세터에서 준비 코드가 상당 부분 필요함. 

코틀린의 위임 프로퍼티 기능을 활용하면 이런 준비 코드를 없앨 수 있음.

위임 프로퍼티 사용 전 ObservableProperty에 있는 두 메소드의 시그니처를 코틀린 관례에 맞게 수정

#### 리스트 7.22 `ObservableProperty` 를 프로퍼티 위임에 사용할 수 있게 바꿈

```kotlin
class ObservableProperty(var propValue: Int, val changeSupport: PropertyChangeSupport) {
    operator fun getValue(p: Person, prop: KProperty<*>): Int = propValue
    operator fun setValue(p: Person, prop: KProperty<*>, newValue: Int) {
        val oldValue = propValue
        propValue = newValue
        changeSupport.firePropertyChange(prop.name, oldValue, newValue)
    }
}
```

7.21 코드와 차이점

* `getValue`와 `setValue` 함수에도 `operator` 변경자가 붙음
* `getValue`와 `setValue`는 프로퍼티가 포함된 객체와 프로퍼티를 표현하는 객체를 파라미터로 받음.
  코틀린은 `KProperty` 타입의 객체로 프로퍼티를 표현함.
* `KProperty` 인자를 통해 프로퍼티 이름을 전달받으므로 주 생성자에서는 `name` 프로퍼티를 없앰.

#### 리스트 7.23 위임 프로퍼티를 통해 프로퍼티 변경 통지 받기

```kotlin
class Person6(val name: String, age: Int, salary: Int): PropertyChangeAware() {
    var age: Int by ObservableProperty(age, changeSupport)
    var salary: Int by ObservableProperty(salary, changeSupport)
}
```

`by` 키워드를 사용해 위임 객체를 지정.

위임 객체를 감춰진 프로퍼티에 저장하고, 주 객체의 프로퍼티를 읽거나 쓸 때마다 위임 객체의 `getValue`와 `setValue`를 호출해 줌.

관찰 가능한 프로퍼티 로직을 `ObservableProperty`와 비슷한 코틀린 표준 라이브러리를 사용해도 되지만, `PropertyChangeSupport`와는 연결돼 있지 않음. 그래서 프로퍼티 값의 변경을 통지할 때 `PropertyChangeSupport` 를 사용하는 방법을 알려주는 람다를 넘겨야 함.

#### 리스트 7.24 `Delegates.observable`을 사용해 프로퍼티 변경 통지 구현

```kotlin
class Person(val name: String, age: Int, salary: Int): PropertyChangeAware() {
    private val observer = {
        prop: KProperty<*>, oldValue: Int, newValue: Int ->
        changeSupport.firePropertyChange(prop.name, oldValue, newValue)
    }
    var age: Int by Delegates.observable(age, observer)
    var salary: Int by Delegates.observable(salary, observer)
}
```

`by`의 우항에는 함수 호출, 다른 프로퍼티, 다른 식 등이 올 수 있지만 올바른 타입의 `getValue`와 `setValue` 를 제공해야 함.  `getValue`와 `setValue`  모두 객체 안에 정의된 메소드이거나 확장 함수일 수 있음.



### 7.5.4 위임 프로퍼티 컴파일 규칙

위임 프로퍼티 동작 정리

위임 프로퍼티가 있는 클래스 작성

```kotlin
class C {
    var prop: Type by MyDelegate()
}

val c = C()
```

컴파일러는 `MyDelegate` 클래스의 인스턴스를 감춰진 프로퍼티에 저장하며 `<delegate>`라는 이름으로 부름.

컴파일러는 프로퍼티를 표현하기 위해 `KProperty` 객체를 사용하며 `<property>`라 부름.

컴파일러는 다음 코드를 생성

```kotlin
class C {
    private val <delegate> = MyDelegate()
    var prop: Type
    get() = <delegate>.getValue(this, <property>)
    set(value: Type) = <delegate>.setValue(this, <property>, value)
}
```

위 매커니즘은 상당히 단순하지만 흥미로운 활용법이 많음.

프로퍼티 값이 저장될 장소를 바꾸거나, 프로퍼티를 읽거나 쓸 때 벌어지 일을 변경할 수도 있음.



### 7.5.5 프로퍼티 값을 맵에 저장

자신의 프로퍼티를 동적으로 정의할 수 있는 객체를 만들 때 위임 프로퍼티를 활용하는 경우가 자주 있음.

그런 객체를 확장 가능한 객체(expando object)라 부르기도 함. 필수 정보와 추가 정보가 있는 연락처 등.

정보를 모두 맵에 저장하되 맵을 통해 처리하는 프로퍼티를 통해 필수 정보 제공하는 방법이 있음.

#### 리스트7.25 값을 맵에 저장하는 프로퍼티 정의

```kotlin
fun main(args: Array<String>) {
    val p = Person()
    val data = mapOf("name" to "Dmitry", "company" to "JetBrains")
    for((attrName, value) in data)
        p.setAttribute(attrName, value)
    println(p.name)
    /*** output ***/
    //Dmitry
}

class Person {
    // 추가 정보
    private val _attributes = hashMapOf<String, String>()
    fun setAttribute(attrName: String, value: String) {
        _attributes[attrName] = value
    }
    // 필수 정보
    val name: String
    get() = _attributes["name"]!!
}
```

위 코드는 추가 데이터를 저장하기 위해 일반적인 API를 사용하고, 특정 프로퍼티를 처리하기 위해 구체적인 개별 API를 제공함.

`by` 키워드 뒤에 맵을 직접 넣어 위임 프로퍼티를 활용해보자.

#### 리스트7.26 값을 맵에 저장하는 위임 프로퍼티 사용

```kotlin
class Person {
    private val _attributes = hashMapOf<String, String>()
    fun setAttribute(attrName: String, value: String) {
        _attributes[attrName] = value
    }
    val name: String by _attributes
}
```

표준 라이브러리가 `Map`과 `MutableMap` 인터페이스에 대해 `getValue`와 `setValue` 확장함수를 제공함.

`getValue`에서 맵에 프로퍼티 값을 저장할 때는 자동으로 프로퍼티 이름을 키로 활용함.



### 7.5.6 프레임워크에서 위임 프로퍼티 활용

객체 프로퍼티를 저장하거나 변경하는 방법을 바꿀 수 있으면 프레임워크 개발시 유용함.

#### 리스트7.27 위임 프로퍼티를 사용해 데이터베이스 칼럼 접근하기

```kotlin
object Users: IdTable() {
    val name = varchar("name", length = 50).index()
    val age = integer("age")
}

class User(id:EntityId): Entity(id) {
    var name: String by Users.name
    var age: Int by Users.age
}
```

`Users` 객체는 데이터베이스 테이블을 표현함. 객체의 프로퍼티는 테이블 칼럼을 표현함.

`User`의 상위 클래스인 `Entity`클래스는 데이터베이스 칼럼을 엔티티의 속성 값으로 연결해주는 매핑이 있음.

각 `User` 프로퍼티 중에는 데이터베이스에서 가져온 `name`과 `age`가 있음.

각 엔티티 속성(`name`, `age`)는 위임 프로퍼티이며, 칼럼 객체(`Users.name`, `Users.age`)를 위임 객체로 사용함.

프레임워크는 `Column` 클래스 안에 `getValue`와 `setValue` 메소드를 정의함.

이 예제의 완전한 구현을 Exposed 프레임워크 소스코드에서 볼 수 있으며, 11장에서 더 살펴봄.



## 7.6 요약

- 산술 연산자
  - 정해진 이름의 함수를 오버로딩함으로써 표준 수학 연산자를 오버로딩할 수 있음.
  - 직접 새로운 연산자는 만들 수 없음.
- 비교 연산자
  - equals
  - compareTo
- 컬렉션과 범위
  - get, set, contains 함수 정의시 클래스의 인스턴스에 대해 []와 in 연산을 사용할 수 있음.
  - 그 객체를 코틀린 컬렉션 객체와 비슷하게 다룰 수 있음.
  - rangeTo, iterator 함수를 정의하면 범위를 만들거나 컬렉션과 배열의 원소를 이터레이션할 수 있음.
- 구조 분해 선언
  - 한 객체의 상태를 분해해서 여러 변수에 대입할 수 있음.
  - 함수가 여러 값을 한꺼번에 반환해야 하는 경우 유용.
  - 데이터 클래스에 대한 구조 분해는 거저 사용!
  - 커스텀 클래스의 인스턴스에서 구조 분해 사용시 componentN 함수를 정의해야 함.
- 위임 프로퍼티
  - 위임 프로퍼티를 통해 프로퍼티 값을 저장하거나 초기화하거나 읽거나 변경할 때 사용하는 로직을 재활용할 수 있음.
  - 프레임워크를 만들 때 아주 유용함.
  - lazy를 통해 지연 초기화 프로퍼티를 쉽게 구현할 수 있음.
  - Delegates.Observable 사용하면 프로퍼티 변경을 관찰할 수 있는 관찰자를 쉽게 추가할 수 있음.
  - 맵을 위임 객체로 사용하는 위임 프로퍼티를 통해 다양한 속성을 제공하는 객체를 유연하게 다룰 수 있음.