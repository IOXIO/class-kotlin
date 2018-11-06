5장 람다로 프로그래밍
===
* **람다식(lamda expression)**: **다른 함수에 넘길 수 있는 작은 코드 조각**
  (익명 함수, **고차 함수**에 인자(argument)로 전달되거나 고차 함수가 돌려주는 결과값)
* **고차함수(High-order function)** - 함수를 받아서 함수를 반환하는 것을 말하는데, 다른 함수를 이용해서 완전히 새로운 함수를 조립 하는 방법으로 프로그램을 만들 수 있다.
* 컬렉션 처리에 자주 사용
* 문을 식으로 바꾸는 효과

# 5-1. 람다 식과 멤버 참조

#### 5.1.1 람다 소개: 코드 블록을 함수 인자로 넘기기

* 함수에 넘기기 위하거나 일시적인 상태를 **무명 내부 클래스** 사용없이 간결하게 표현 가능
* 의미없는(일시적인) 클래스나 변수 생성으로 부터 자유로울 수 있다.
* 함수를 값처럼 넘길 수 있기 때문이다.

> JAVA
```
button.setOnClickListener(new onClickListener() {
  @Override
  public void onClick(View view) {
    /* 클릭 시 수행할 동작 */
  }
})
```

> KOTLIN
```
button.setOnClickListener { /* 클릭 시 수행할 동작 */ }
                            ------------------------
                                     람다
```
 [https://realjenius.com/2017/08/24/kotlin-curry/] kotlin의 currying

#### 5.1.2 람다와 컬렉션

* Prson(이름, 나이)에서 가장 연장자를 찾는 코드
> 절차형
1. 루프
2. 상태1(maxAge): 나이의 최대값
3. 상태2(theOldest): 상태1에 해당하는 나이를 가진 인물
```
data class Person(val name: String, val age: Int)

fun findTheOldest (people: List<Person>) {
  var maxAge = 0
  var theOldest: Person? = null
  for (person in people) {      // 1
    if (person.age > maxAge) {
      maxAge = person.age       // 2
      theOldest = person        // 3
    }
  }
  println(theOldest)
}

fun main(args: Array<String>){
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    findTheOldest(people)
    // 출력 Person(name=Bob, age=31)
}
```
> 함수형
* kotline에서 해당하는 역할(MAX값을 찾아 리턴)을 하는 함수를 제공하기 때문
* it 키워드: 인자가 하나라면 그 인자는 람다식 내부에서 it으로 받을 수 있다.
* collection은 iterator가 기준이 되기 때문에 반환 타입은 주로 list 내부의 타입이나 list 타입 (List<Person>, Person)
```
data class Person(val name: String, val age: Int)

fun main(args: Array<String>){
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    println(people.maxBy { it.age })
    // 출력 Person(name=Bob, age=31)
}
```
Q. 원하는 조건이 달라질 경우?
* maxByAge, maxByName, minByName 생성 필요 없음
```
println(people.maxBy { it.name })
println(people.minBy { it.name })
// 출력: Person(name=Bob, age=31)
// 출력: Person(name=Alice, age=29)
```

Q. 다른 출력 결과를 원하는 경우?

#### 5.1.3 람다 식의 문법
> { x, y -> x + y } -> (x, y) { return x + y }
```
val sum = { x: Int, y: Int -> x + y }
println(sum(1,2))
```
```
sum(x, y) {
  retun x + y
}
```

>  run : 즉시 실행
```
{ println(42) }()
```
```
run { println(42) }
```
> 다양한 구문 지원
```
people.maxBy { it.age }
people.maxBy({ p:Person -> p.age })
people.maxBy() { p:Person -> p.age }
people.maxBy { p:Person -> p.age } // 인자가 하나일때만 가능 ()가 생략된
```
> 이름 붙인 인자를 사용해 람다 넘기기
```
val people = listOf(Person("이몽룡", 29), Person("성춘향", 31))
val names = people.joinToString(
  separator = " ", transform = { p: Person -> p.name })
println(names)
  ```

> 람다를 괄호 밖에 전달하기
```
val names = people.joinToString(" ") { p: Person -> p.name }
```

> 람다 파라미터 타입 제거하기
```
people.maxBy { p:Person -> p.age }
```
```
people.maxBy { p -> p.age }
```
> 디폴트 파라미터 이름 it 사용하기
```
people.maxBy { it.age }
```

> 타입 추론 불가능 할 때
```
val getAge = { p: Person -> p.age }
people.maxBy(getAge)
```

> multi line 람다
```
val sum = {
  x: Int, y: Int -> println("Computing the sum of $x and $y") // 실행 문
  x + y // 결과 값 - return
}
println(sum(1,2))
```

#### 5.1.4 현재 영역에 있는 변수에 접근
> 람다를 함수 안에서 정의하면 함수의 파라미터와 로컬 변수를 사용할 수 있다.
```
fun printMessagesWithPrefix(messages: Collection<String>, prefix: String) {
  messages.forEach {
    println("$prefix $it")
  }
}

fun main(args: Array<String>) {
  val errors = listOf("403 Forbidden", "404 Not Found")
  printMessagesWithPrefix(errors, "Error:")
}
```

> * 람다에서는 final이 아닌 변수(람다가 포획한 변수)에 접근 가능
> * 람다가 변수를 포획, 저장하면 함수의 생명주기 달라진다. - javascript 클로저
```
fun printProblmCounts(responses: Collection<String>) {
  var clientErrors = 0
  var setverErrors = 0
  responses.forEach {
    if (it.startsWith("4")) {
      clientErrors++
    } else if (it.startsWith("5")) {
      setverErrors++
    }
  }
  println("$clientErrors client rrors, $setverErrors server errors")
}

fun main(args: Array<String>) {
  val responses = listOf("200 OK", "418 I'm a teapot", "500 Internal Server Error")
  printProblmCounts(responses)
  // 출력: 1 client rrors, 1 server errors
}
```

> 비동기 유의
```
fun tryToCountButtonClicks(button: Button): Int {
  var clicks = 0
  button.onClick { clicks++ }
  retun clicks
}
```

#### 5.1.5 멤버 참조
> 멤버 참조 (::) : 이미 선언된 함수를 다른 함수의 인자로 넘길 때 사용
```
val getAge = Person::age
```
```
val getAge = { Person: Person -> Person.age }
```
```
people.maxBy(Person::age)
people.maxBy { p -> p.age }
people.maxBy { it.age }
```

> 최상위 함수, 프로퍼티 참조
> [./5.1/test.kt](테스트)
```
fun salute() = println("Salute!")

fun main(args: Array<String>) {
  run(::salute) // 실행 결과: salute!
}
```

> 생성자 참조
> [./5.1/test2.kt](테스트2)
```
data class Person(val name: String, val age: Int)

fun main(args: Array<String>) {
  val createPerson = ::Person*
  val p = createPerson("Alice", 29)
  println(p)  // 실행결과 Person(name=Alice, age=29)
}
```
* 코틀린은 데이터와 타입(클래스)를 명확히 구분

> 확장함수도 ::가능
```
fun Person.isAdult() = age >= 21
val predicate = Person::isAdult
```

> 바운드 멤버 참조
```
val p = Person("Dmitry", 34)
val personsAgeFunction = Person::age
println(personAgeFunction(p))
```
```
val p = Person("Dmitry", 34)
val dmitrysAgeFunction = p::age
println(dmitrysAgeFunction())
```

# 5.2 컬렉션 함수형 API

#### 5.2.1 필수적인 함수: filter와 map
> filter: 이터레이션 하면서 해당조건에 true한 원소만 반환
> * 모든 원소의 개수보다 결과물의 개수가 같거나 줄어든다.
```
val list = listOf(1,2,3,4)
println(list.filter { it % 2 == 0 })  // 실행 결과 [2,4]
```
```
val people = listOf(Person("Alice", 29), Person("Bob", 31))
println(people.filter { it.age > 30 })
// 실행결과 [Peoson(name=Bob, age=31)]
```

> map: 모든 원소를 순회하면서 원소에 해당조건을 적용
> * 모든 원소의 개수와 결과물의 개수가 동일하다.
```
val list = listOf(1,2,3,4)
println(list.map { it * it })
// 실행결과 [1, 4, 9, 16]
```
```
val people = listOf(Person("Alice", 29), Person("Bob", 31))
println(people.map { it.name })
// println(people.map(Person::name))
// 실행결과 [Alice, Bob]
```
> 함수의 조합
```
people.filter { it.age > 30 }.map(Person::name) // filter 먼저
```

> 제일 나이 많은 사람
```
var maxAge = people.maxBy(Person::age)!!.age
people.filter { it.age == maxAge }
```
Q. !!: null이 아니여야 할 때,
 * ? null 허용: 문법오류는 안 남
 * error: only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type Person?

> map구조의 map 함수 (filterKeys, mapKeys, filterValues, mapValues)
 ```
val numbers = mapOf(0 to "zero", 1 to "one")
println(numbers.mapValues { it.value.toUpperCase() })
 ```
#### 5.2.2 all, any, count, find: 컬렉션에 술어 적용
> 컬렉션의 모든 원소가 어떤 조건을 만족하는지 판단하는 연산
> * 반환 결과값 개수가 1
> * 컬렉션(itertator) 함수를 다룰 때, DB 쿼리처럼 사고

* all : 모든 원소가 조건을 만족하면 true
* any : 하나라도 조건을 만족하면 true
* count: 조건을 만족하는 원소의 개수
* find: 조건을 만족하는 첫 번째 원소 (any랑 반환 타입만 다르다. 조건이 만족하면 iterator를 빠져나온다.)

```
val canbeInClub27 = { p: Person -> p.age <= 27 }
val people = listOf(Person("Alice", 27), Person("Bob", 31))

people.all(canBeInClub27) // false
people.any(canBiInClub27) // true
people.count(canBeInClub27)  // 1
people.find(canBeInClub27)  // Person(name=Alice, age=27)
```
> all, any - !(모두 3인지) = 3이 아닌 게 하나라도 있는지
```
val list = listOf(1,2,3)
println(!list.all { it == 3 })  // true
```
```
println(list.any { it != 3 }) // true
```

#### 5.2.3 groupBy: 리스트를 여러 그룹으로 이뤄진 맵으로 변경
```
val people = listOf(Person("Alice", 31), Person("Bob", 29), Person("Carol", 31))
println(people.groupBy { it.age })

// 실행결과: type = Map<Int, List<Person>>
{
 29=[Person(name=Bob, age=29)],
 31=[Person(name=Alice, age=31), Person(name=Carol, age=31)]
}
```
```
val list = listOf("a", "ab", "b")
println(list.groupBy(String::first))  // first는 String의 확장함수
// 실행결과: {a=[a, ab], b=[b]}
```
#### 5.2.4 flatMap과 flatten: 중첩된 컬렉션 안의 원소 처리
> flatMap - 하나의 depth로 펼친 map
```
class Book(val title: String, val authors: List<String>)
books.flatMap { it.authors }.toSet()  // toSet은 순서 유지, 중복 제거
```
```
val strings = listOf("abc", "def")
println(strings.flatMap { it.toList() })  // [a, b, c, d, e, f]
```
> toSet
```
val books = listOf(Book("Thursday Next", listOf("Jasper Fforde")),
                  Book("Mort", listOf("Terry Pratchett")),
                  Book("Good Omens", listOf("Terry Pratchett", "Neil Gaiman")),
)
println(boos.flatMap { it.authors }.toSet())
// [Jasper Fforde, Terry Pratchett, Neil Gaiman]
```
* listOfLists.flatten()
# 5.3 지연 계산(lazy) 컬렉션 연상
* 컬렉션: 함수 연쇄하는 과정중에 결과 컬렉션을 즉시 생성
* 시퀀스: 컬렉션 함수에서 연쇄하는 과정 중 임시 컬렉션을 생성하지 않음
* 클로저 기반 fnjs에서는 파이프로 처리.
```
people.map(Person::name)  // 임시 컬렉션 생성
  .filter { it.startsWith("A") }
```
```
people.asSequence()
  .map(Person::name)
  .filter { it.startsWith("A") }
  .toList() // list 타입이 아니기 때문에 변경 필요
```
> 시퀀스 인터페이스
* iterator 메소드만 존재 (iterator 방향이 다르다)
* 로직에 문제가 없는 한 반환 COUNT가 적은 것에서 많은 순서로..
  (map - filter -> filter - map)
* asSequenc 확장 함수를 호출하면 어떤 컬렉션이든 시퀀스로 변경 가능
* 항상 시퀀스가 좋은것만은 아니다.

#### 5.3.1 시퀀스 연산 실행: 중간 연산과 최종 연산
> 중간 연산과 최종 연산
* 중간 연산: 다른 시퀀스를 반환, 지연 계산(결과값 sequence)
* 최종 연산: 결과를 반환, 시퀀스를 다른 결과 타입으로 변환하는 연산
* 지연계산은 그림 보면서 설명하기

#### 5.3.2 시퀀스 만들기
> 자연수의 시퀀스를 생성하고 사용하기
```
val naturalNumbers = generateSequence(0) { it + 1 }
val numbersTo100 = naturalNumbers.takeWhile { it <= 100 }
println(numbersTo100.sum())
```
# 5.4 자바 함수형 인터페이스 활용
> 자바
```
public interface OnClickListener {
  void onClick(View v);
}
button.setOnClickListener (new OnClickListener()) {
  @Override
  public void onClick(View v) {
    ...
  }
}
```

> 코틀린
* 함수형 인터페이스 (SAM 인터페이스): 추상 메소드가 하나만 있는 인터페이스
```
button.setOnClickListener { view -> ... }
// view는 onClick의 인자인 View 타입
```
#### 5.4.1 자바 메소드에 람다를 인자로 전달
> 자바
```
void postponeComputation(int delay, Runnable computation);
```
> 코틀린
```
postponeComputation(1000) { println(42) }
```
* Runnable 타입: 평가된 상태를 리턴

> Runnable을 구현하는 무명 객체를 명시적으로 만들어서 사용
* 호출할때마다 새로운 객체 생성
```
postponeComputation(1000, object : Runnable {
  override fun run() {
    println(42)
  }
})
```

> 명시적 선언을 하면서 람다와 동일(객체 생성X)한 코드
```
val runnable = Runnable { println(42) }
fun handleComputation() {
  postponeComputation(1000, runnable)
}
```
> 호출시 새로운 Runnable 인스턴스 생성
```
fun handleComputation(id: String) {
  postponeComputation(1000) { println(id) }
}
```
#### 5.4.2 SAM 생성자: 람다를 함수형 인터페이스로 명시적으로 변경
```
fun createAllConeRunnable():Runnable {
  return Runnable { println("All done!") }
}

fun main(args: Array<String>) {
  createAllConeRunnable().run() // 실행결과: All done!
}
```
* 람다는 객체가 아니기 때문에 리스너 해제가 필요할 경우에는 무명 객체를 사용하는 것이 좋다.

# 5.5 수신 객체 지정 람다: with와 apply
* 수신 객체 지정 람다: 바로 수신객체를 명시하지 않고, 람다의 본문 안에서 다른 객체의 메소드를 호출할 수 있는 람다
#### 5.5.1 with 함수
> 알파벳 만들기
```
fun alphabet(): String {
  val result = StringBuilder()
  for (letter in 'A'..'Z') {
    result.append(letter)
  }
  result.append("\nNow I know the alphabet!")
  return result.toString()
}

fun main(args: Array<String>) {
  println(alphabet())
  // 실행결과:
  // ABCDEFGHIJKLMNOPQRSTUVWXYZ
  // Now I know the alphabet!
}
```
> with를 사용해 알파벳 만들기
```
fun alphabet(): String {
  val stringBuilder = StringBuilder()
  return with(stringBuilder) {
    for (letter in 'A'..'Z') {
      this.append(letter)
    }
    append("\nNow I know the alphabet!")
    this.toString()
  }
}
```
> with와 식을 본문으로 하는 함수를 활용해 알파벳 만들기
```
fun alphabet() = with(StringBuilder()) {
  for (letter in 'A'..'Z') {
    append(letter)
  }
  append("\nNow I know the alphabet!")
}

fun main(args: Array<String>) {
  println(alphabet())
}
```
#### 5.5.2 apply 함수
> apply를 사용한 알파벳 만들기
```
fun alphabet() = StringBuilder().apply {
  for (letter in 'A'..'Z') {
    append(letter)
  }
  append("\nNow I know the alphabet!")
}.toString()

fun main(args: Array<String>) {
  println(alphabet())
}
```
# 5.6 요약
* 람다를 사용하면 코드 조각을 다른 함수에게 인자로 넘길 수 있다.
* 코틀린에서는 람다가 함수 인자인 경우 괄호 밖으로 람다를 빼낼 수 있고, 람다의 인자가 단 하나뿐인 경우 인자 이름을 지정하지 앟고 it이라는 디폴트 이름으로 부를 수 있다.
* 람다 안에 있는 코드는 그 람다가 들어있는 바깥 함수의 변수를 읽거나 쓸 수 있다.
* 메소드, 생성자, 프로퍼티의 이름 앞에 ::을 붙이면 각각에 대한 참조를 만들 수 있다. 그런 참조를 람다 대신 다른 함수에게 넘길 수 있다.
* filter, map, all, any 등의 함수를 활용하면 컬렉션에 대한 대부분의 연산을 직접 원소를 이터레이션하지 않고 수행할 수 있다.
* 시퀀스를 사용하면 중간 결과를 담는 컬렉션을 생성하지 않고도 컬렉션에 대한 여러 연산을 조합할 수 있다.
* 함수형 인터페이스(추상 메소드가 단 하나뿐이 SAM 인터페이스)를 인자로 받는 자바함수를 호출할 경우 람다를 함수형 인터페이스 인자 대신 넘길 수 있다.
* 수신 객체 지정 람다를 사용하면 람다 안에서 미리 정해둔 수식 객체의 메소드를 직접 호출할 수 있다.
* 표준 라이브러리의 with 함수를 사용하면 어떤 객체에 대한 참조를 반복해서 언급하지 않으면서 그 객체의 메소드를 호출할 수 있다. apply를 사용하면 어떤 객체라도 빌더 스타일의 API를 사용해 생성하고 초기화할 수 있다.