import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

fun main(args: Array<String>) {
    /***** 7.1 산술 연산자 오버로딩 *****/
    /*** 7.1.1 이항 산술 연산 오버로딩 ***/
    //ch0701()              // plus 연산자 구현
    //ch0702()              // 연산자를 확장 함수로 정의
    //operatorPriority()    // 연산자 우선순위
    //operatorAndJava()     // 연산자 함수와 자바
    //ch0703()              // 두 피연산자의 타입이 다른 연산자 정의
    //ch0704()              // 결과 타입이 피연산자 타입과 다른 연산자 정의
    /*** 7.1.2 복합 대입 연산자 오버로딩 ***/
    //compoundAssignment()              // 복합 대입 연산자 오버로딩
    //compoundAssignmentCollection()    // 컬렉션에 대한 접근 방법
    /*** 7.1.3 단항 연산자 오버로딩 ***/
    //ch0705()              // 단항 연산자 정의
    //ch0706()              // 증가 연산자 정의

    /***** 7.2 비교 연산자 오버로딩 *****/
    /*** 7.2.1 동등성 연산자: equals ***/
    //ch0707()              // equals 메소드 구현
    /*** 7.2.2 순서 연산자: compareTo ***/
    //ch0708()              // compareTo 메소드 구현

    /***** 7.3 컬렉션과 범위에 대해 쓸 수 있는 관례 *****/
    /*** 7.3.1 인덱스로 원소에 접근: get과 set ***/
    //ch0709()              // get 관례 구현
    //ch0710()              // get 관례 구현
    /*** 7.3.2 in 관례 ***/
    //ch0711()              // in 관례 구현
    /*** 7.3.3 rangeTo 관례 ***/
    //ch0712()              // 날짜의 범위 다루기
    /*** 7.3.4 for 루프를 위한 iterator 관례 ***/
    //ch0713()              // 날짜 범위에 대한 이터레이터 구현

    /***** 7.4 구조 분해 선언과 component 함수 *****/
    //destDec()             // 구조 분해 선언
    //destDecComponent()    // data 클래스가 아닌 경우 구조 분해 선언
    //ch0714()              // 구조 분해 선언을 사용해 여러 값 반환
    //ch0715()              // 켈렉션에 대해 구조 분해 선언 사용
    //componentN()          // 켈렉션에 대해 구조 분해 선언 사용
    /*** 7.4.1 구조 분해 선언과 루프 ***/
    //ch0716()              // 구조 분해 선언을 사용해 맵 이터레이션 하기

    /***** 7.5 프로퍼티 접근자 로직 재활용: 위임 프로퍼티 *****/
    /*** 7.5.1 위임 프로퍼티 소개 ***/
    //delegatedProp()       // 구조 분해 선언을 사용해 맵 이터레이션 하기
    /*** 7.5.2 위임 프로퍼티 사용: by lazy()를 사용한 프로퍼티 초기화 지연 ***/
    //ch0717()              // 지연 초기화를 뒷받침하는 프로퍼티를 통해 구현
    //ch0718()              // 지연 초기화를 위임 프로퍼티를 통해 구현하기
    /*** 7.5.3 위임 프로퍼티 구현 ***/
    //ch0719()              //PropertyChangeSupport 를 사용하기 위한 도우미 클래스
    //ch0720()              //프로퍼티 변경 통지를 직접 구현
    //ch0721()              //도우미 클래스를 통해 프로퍼티 변경 통지 구현
    //ch0722()              //ObservableProperty 를 프로퍼티 위임에 사용할 수 있게 바꿈
    //ch0723()              //위임 프로퍼티를 통해 프로퍼티 변경 통지 받기
    //ch0724()              //Delegates.observable을 사용해 프로퍼티 변경 통지 구현
    /*** 7.5.4 위임 프로퍼티 컴파일 규칙 ***/
    /*** 7.5.5 프로퍼티 값을 맵에 저장 ***/
    //ch0725()              //값을 맵에 저장하는 프로퍼티 정의
    //ch0726()              //값을 맵에 저장하는 위임 프로퍼티 사용
    /*** 7.5.6 프레임워크에서 위임 프로퍼티 활용 ***/
    //ch0727()              //위임 프로퍼티를 사용해 데이터베이스 칼럼 접근하기
}

fun ch0727() {
    /*object Users: IdTable() {
        val name = varchar("name", length = 50).index()
        val age = integer("age")
    }

    class User(id:EntityId): Entity(id) {
        var name: String by Users.name
        var age: Int by Users.age
    }*/
}

fun ch0726() {
    class Person {
        private val _attributes = hashMapOf<String, String>()
        fun setAttribute(attrName: String, value: String) {
            _attributes[attrName] = value
        }
        val name: String by _attributes
    }
    val p = Person()
    val data = mapOf("name" to "Dmitry", "company" to "JetBrains")
    for((attrName, value) in data)
        p.setAttribute(attrName, value)
    println(p.name)
    /*** output ***/
    //Dmitry
}

fun ch0725() {
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
    val p = Person()
    val data = mapOf("name" to "Dmitry", "company" to "JetBrains")
    for((attrName, value) in data)
        p.setAttribute(attrName, value)
    println(p.name)
    /*** output ***/
    //Dmitry
}

fun ch0724() {
    class Person(val name: String, age: Int, salary: Int): PropertyChangeAware() {
        private val observer = {
            prop: KProperty<*>, oldValue: Int, newValue: Int ->
            changeSupport.firePropertyChange(prop.name, oldValue, newValue)
        }
        var age: Int by Delegates.observable(age, observer)
        var salary: Int by Delegates.observable(salary, observer)
    }

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

fun ch0723() {
    val p = Person6("Dmitry", 34, 2000)
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

class Person6(val name: String, age: Int, salary: Int): PropertyChangeAware() {
    var age: Int by ObservableProperty2(age, changeSupport)
    var salary: Int by ObservableProperty2(salary, changeSupport)
}

fun ch0722() {
}

class ObservableProperty2(var propValue: Int, val changeSupport: PropertyChangeSupport) {
    operator fun getValue(p: Person6, prop: KProperty<*>): Int = propValue
    operator fun setValue(p: Person6, prop: KProperty<*>, newValue: Int) {
        val oldValue = propValue
        propValue = newValue
        changeSupport.firePropertyChange(prop.name, oldValue, newValue)
    }
}

fun ch0721() {
    val p = Person5("Dmitry", 34, 2000)
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

class Person5(val name: String, age: Int, salary: Int): PropertyChangeAware() {
    val _age = ObservableProperty("age", age, changeSupport)
    var age: Int
        get() = _age.getValue()
        set(value) { _age.setValue(value) }

    val _salary = ObservableProperty("salary", salary, changeSupport)
    var salary: Int
        get() = _salary.getValue()
        set(value) { _salary.setValue(value) }
}

fun ch0720() {
    val p = Person4("Dmitry", 34, 2000)
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

class Person4(val name: String, age: Int, salary: Int): PropertyChangeAware() {
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

fun ch0719() {
}

open class PropertyChangeAware {
    protected val changeSupport = PropertyChangeSupport(this)

    fun addPropertyChangeListener(listener: PropertyChangeListener) {
        changeSupport.addPropertyChangeListener(listener)
    }

    fun removePropertyChangeListener(listener: PropertyChangeListener) {
        changeSupport.removePropertyChangeListener(listener)
    }
}

fun ch0718() {
    val p = Person3("Alice")
    p.emails
    /*** output ***/
    //Load emails for Alice
    p.emails
    /*** output ***/
}

class Person3(val name: String) {
    val emails by lazy {loadEmails3(this)}
}

fun loadEmails3(person: Person3): List<Email> {
    println("Load emails for ${person.name}")
    return listOf()
}

fun ch0717() {
    val p = Person2("Alice")
    p.emails
    /*** output ***/
    //Load emails for Alice
    p.emails
    /*** output ***/
}
class Email

class Person2(val name: String) {
    private var _emails: List<Email>? = null
    val emails: List<Email>
        get() {
            if(_emails == null) {
                _emails = loadEmails2(this)
            }
            return _emails!!
        }
}

fun loadEmails2(person: Person2): List<Email> {
    println("Load emails for ${person.name}")
    return listOf()
}

fun delegatedProp() {
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
    val e = Example()
    println(e.p)
    /*** output ***/
    //Example@6193b845, thank you for delegating 'p' to me!
    e.p = "New"
    /*** output ***/
    //New has been assigned to 'p' in Example@6193b845.
}

fun ch0716() {
    fun printEntries(map: Map<String, String>) {
        for((key, value) in map) {
            println("$key -> $value")
        }
    }

    val map = mapOf("Oracle" to "Java", "JetBrains" to "Kotlin")
    printEntries(map)
    /*** output ***/
    //Oracle -> Java
    //JetBrains -> Kotlin
}

fun componentN() {
    val x = listOf(1, 2)

    val (a, b, c, d, e) = x
    /*** output ***/
    //Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 2
    //at java.util.Arrays$ArrayList.get(Arrays.java:3841)

    //val (a, b, c, d, e, f) = x
    /*** output ***/
    //Error:(116, 30) Kotlin: Destructuring declaration initializer of type List<Int> must have a 'component6()' function
}

fun ch0715() {
    data class NameComponents(val name: String, val extension: String)
    fun splitFilename(fullName: String): NameComponents {
        val(name, extension) = fullName.split('.', limit = 2)
        return NameComponents(name, extension)
    }
    val (name, ext) = splitFilename("example2.kt2")
    println(name)
    /*** output ***/
    //example2
    println(ext)
    /*** output ***/
    //kt2
}

fun ch0714() {
    data class NameComponents(val name: String, val extension: String)
    fun splitFilename(fullName: String): NameComponents {
        val result = fullName.split('.', limit = 2)
        return NameComponents(result[0], result[1])
    }
    val (name, ext) = splitFilename("example.kt")
    println(name)
    /*** output ***/
    //example
    println(ext)
    /*** output ***/
    //kt
}

fun destDecComponent() {
    class Point(val x: Int, val y: Int){
        operator fun component1() = x
        operator fun component2() = y
    }
    val p = Point(10, 20)
    val (x, y) = p
    println(x)
    /*** output ***/
    //10
    println(y)
    /*** output ***/
    //20
}

fun destDec() {
    val p = Point(10, 20)
    val (x, y) = p
    println(x)
    /*** output ***/
    //10
    println(y)
    /*** output ***/
    //20
}

fun ch0713() {
    for(c in "abc") {
        println(c)
    }
    /*** output ***/
    //a
    //b
    //c

    operator fun ClosedRange<LocalDate>.iterator():Iterator<LocalDate> =
            object : Iterator<LocalDate> {
                var current = start
                override fun hasNext() = current <=endInclusive
                override fun next() = current.apply {
                    current = plusDays(1)
                }
            }
    val newYear = LocalDate.ofYearDay(2017, 1)
    val daysOff = newYear.minusDays(1)..newYear
    for(dayOff in daysOff) {
        println(dayOff)
    }
    /*** output ***/
    //2016-12-31
    //2017-01-01
}

fun ch0712() {
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

fun ch0711() {
    data class Rectangle(val upperLeft: Point, val lowerRight: Point)

    operator fun Rectangle.contains(p: Point): Boolean {
        return p.x in upperLeft.x until lowerRight.x &&
                p.y in upperLeft.y until lowerRight.y
    }

    val rect = Rectangle(Point(10, 20), Point(50, 50))
    println(Point(20, 30) in rect)
    /*** output ***/
    //true
    println(Point(5, 5) in rect)
    /*** output ***/
    //false
}

fun ch0710() {
    data class MutablePoint(var x: Int, var y: Int)

    operator fun MutablePoint.set(index: Int, value: Int) {
        when(index) {
            0 -> x = value
            1 -> y = value
            else ->
                throw IndexOutOfBoundsException("Invalid coordinate $index")
        }
    }
    val p = MutablePoint(10, 20)
    p[1] = 42
    println(p)
    /*** output ***/
    //MutablePoint(x=10, y=42)
}

fun ch0709() {
    data class Point(val x: Int, val y: Int)
    operator fun Point.get(index: Int): Int {
        return when(index) {
            0 -> x
            1 -> y
            else ->
                throw IndexOutOfBoundsException("Invalid coordinate $index")
        }
    }
    val p = Point(10, 20)
    println("p = ${p[0]}, ${p[1]}")
    /*** output ***/
    //p = 10, 20
}

fun ch0708() {
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

fun ch0707() {
    class Point(val x: Int, val y: Int) {
        override fun equals(obj: Any?): Boolean {
            if(obj === this) return true
            if(obj !is Point) return false
            return obj.x == x && obj.y == y
        }
    }
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

fun ch0706() {
    operator fun BigDecimal.inc() = this + BigDecimal.ONE
    var bd = BigDecimal.ZERO
    println(bd++)
    /*** output ***/
    //0
    println(++bd)
    /*** output ***/
    //2
}

fun ch0705() {
    data class Point(val x: Int, val y: Int)
    operator fun Point.unaryMinus(): Point {
        return Point(-x, -y)
    }
    val p = Point(10, 20)
    println(-p)
    /*** output ***/
    //Point(x=-10, y=-20)
}

fun compoundAssignmentCollection() {
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

fun compoundAssignment() {
    var point = Point(1, 2)
    point += Point(3, 4)
    println(point)
    /*** output ***/
    //Point(x=4, y=6)
    val numbers = ArrayList<Int>()
    numbers += 42
    println(numbers)
    /*** output ***/
    //[42]
}

fun ch0704() {
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

fun ch0703() {
    data class Point(val x: Int, val y: Int)
    operator fun Point.times(scale: Double): Point {
        return Point((x * scale).toInt(), (y * scale).toInt())
    }
    val p = Point(10, 20)
    println(p * 1.5)
    /*** output ***/
    //Point(x=15, y=30)
}

fun operatorAndJava() {
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
}

fun operatorPriority() {
    data class Point(val x: Int, val y: Int)
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

fun ch0702() {
    data class Point(val x: Int, val y: Int)
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

fun ch0701() {
    data class Point(val x: Int, val y: Int) {
        operator fun plus(other: Point): Point {
            return Point(x + other.x, y + other.y)
        }
    }
    val p1 = Point(10, 20)
    val p2 = Point(30, 40)
    println(p1 + p2)
    /*** output ***/
    //Point(x=40, y=60)
}
