4장 클래스, 객체, 인터페이스
===

##### 코틀린 클래스를 다루는 방법에 대해 배운다.

	4-1 클래스 계층 정의
    	-인터페이스
       	-상속제어 변경자
    	-추상 클래스
       	-봉인된 클래스
    
    4-2 생성자와 프로퍼티를 갖는 클래스
    	-주생성자
       	-부생성자
        
   	4-3 컴파일러가 생성한 메소드: 데이터 클래스와 클래스 위임
    	-toString()
       	-equals()
       	-hashCode()
        
    4-4 Object 키워드: 클래스 선언과 인스턴스 생성
    	-싱글턴
      	-동반객체
       	-무명객체
        
  
# 4-1. 클래스 계층 정의

코틀린은 Java와 같은 형태의 상속개념을 갖는다.
(인터페이스와 객체등의 성격을 그대로 물려받음)

하지만 실용성을 강조한 언어인만큼 자바에서 불필요하다고 생각되는 부분들은 과감하게 배제된 부분이 보입니다.
ex) class의 기본값이 final이면서 public

```md
# Kotlin
class 클래스명 constructor(변수) {}
or
class 클래스명(변수) {}

# Java
class 클래스명{
	클래스명(변수){
    }
}
```

#### 4.1.1 코틀린 인터페이스

코틀린의 인터페이스에는 자바와 다르게 구현부가 있는 함수가 정의될 수 있다.
단, 어떤 멤버(field)도 가질 수 없습니다.

```
interface Clickable {
	fun click()
}

class Button : Clickable {
	override fun click() = println("I was clicked")
}

fun main(args: Array){
	Button().click()
}
```
interface 키워드를 이용해 interface 정의하고, 이를 구현하는 클래스는 콜론(:)을 이용한다.
그리고 자식 클래스에서 부모의 함수를 override하려면 override라는 키워드를 반드시 사용해야 한다.
자바에서의 @Override 어노테이션은 옵션이지만, 코틀린은 필수라는 점!   


#### 4.1.2 class 한정자

코틀린의 인터페이스에는 자바와 다르게 구현부가 있는 함수가 정의될 수 있다.
- **open**: 상속이 가능한 클래스로 명명
- **final**: 상속이 불가능한 클래스로 명명 (기본값)
- **abstract**: 추상 클래스

자바와 다른점
	1.한정자 없이 사용한 class는 final이 기본 속성이라는 것
	2.상속이 가능한 class로 만들려면 open 키워드를 명시적으로 붙여야하는 것

*취약한 기반 클래스(Fragile Base Class)
부모 클래스가 명확하게 상속하는 방법과 규칙에 대해 정의하지 않는다면 해당 부모를 상속받는 자식들은 부모 클래스 작성당시의 의도와 다르게 상속받아 사용될 수 있다. 

이런 경우 부모 클래스가 바뀌면 하위 클래스가 영향을 받아 side-effect 발생하는 경우가 발생한다. (부모 클래스 변경시 이를 상속받는 모든 하위 클래스의 구현을 일일이 확인할수 없으므로 발생)

```
interface Clickable {
	fun click()
   	fun showOff() = println("I'm Clickable!")
}

open class RichButton : Clickable { // 다른 클래스가 상속 받을 수 있음

	fun disable() {} // 1
    
	open fun animate() {} // 2
  	
   	override fun click() {} // 3

}
```

(1)자식 클래스가 override 할 수 없다.

(2)자식 클래스가 override 할 수 있다.

(3)자식 클래스가 또 다시 override 할 수 있다. (override 함수는 기본값이 open이다.)

만약, override한 함수를 자식 클래스에서 다시 override하지 못하게 하려면? 앞에 final을 붙여준다.

```
open class RichButton : Clickable {
  	
   	final override fun click() {}

}
```

#### 4.1.3 가시성 변경자 (Visibility Modifier)

클래스 내에서 선언된 멤버의 경우

`private`: 이 클래스 안에서만 접근 가능

`protected`: 하위 클래스에서 접근 가능

`internal`: 모듈 단위로 접근 가능 (자바와 다른점 / 새로생김)

`public`: 해당 파일의 모든 클래스에서 접근 가능

```
open class Outer {
	private val a = 1
	protected open val b = 2
  	internal val c = 3
  	val d = 4
    
    protected class Nested {
    	public val e: Int = 5
    }
}

class Subclass : Outer() {
	// a에 접근 불가
   	// b, c, d 접근 가능
   	// Nested(), e에 접근 가능
}

class Unrelated(o: Outer){
	// o.a, o.b 접근 불가
   	// o.c, o.d 접근 가능	
   	// Outer.Nested()에 접근 불가 (Nested::e에도 접근 불가)
}

```

#### 4.1.4 내부 클래스

코틀린에서는 내부 클래스를 정의하면 기본적으로 **static class**가 됩니다.
따라서, 내부 클래스에서 외부 클래스의 변수나 함수에 접근할 수 없다.

*static은 정적이라는 뜻으로, 메모리에 미리 공간을 할당해 놓는 것을 뜻한다. 
즉, 인스턴스(객체)를 생성하기 전에 미리 메모리에 올려놔 고정시킨다는 뜻.

```
class Outer {
	//내부 클래스를 사용하려면 명시적으로 inner 키워드를 붙여줘야한다.
	inner class Inner{
    
	// 내부 클래스에서 외부 클래스 참조 얻기 this@외부 클래스명
    	fun getOuterReference(): Outer = this@Outer 
    }

}
```

#### 4.1.5 봉인된 클래스 (sealed class)

**sealed** 클랫는 말그대로 자식 클래스의 범위를 한정하는 경우를 말합니다.

부모 클래스를 sealed class로 만들고, 내부에 이 부모를 상속 받을 수 있는 자식 class를 지정.
즉, 자식 클래스를 묶어서 그룹처럼 관리한다고 생각하면 편합니다.

```
interface Expr

class Num(val value: Int) : Expr
class Sum(val left:  Expt, val right: Expr) : Expr

fun eval(e: Expr): Int =
	when (e) {
    	is Num -> e.value
        is Sum -> eval(e.right) + eval(e.left)
        else ->
        	throw IllegalArgumentException("Unknown expression")
    }
```
상위 클래스인 Expr에는 숫자를 표현하는 Num과 덧셈 연산을 표현하는 Sum이라는 두 하위 클래스가 있다.
when 식에서 이 모든 하위 클래스를 처리하면 편하다.
하지만 when 식에서 Num과 Sum이 아닌 경우를 처리하는 else 분기를 반드시 넣어줘야만 한다.

```
sealed class Expr {
	class Num(val value: Int) : Expr()
	class Sum(val left:  Expt, val right: Expr) : Expr()
}

fun eval(e: Expr): Int =
	when (e) {
    	is Expr.Num -> e.value
        is Expr.Sum -> eval(e.right) + eval(e.left)
    }
```
클래스 계층에 새로운 하위 클래스를 추가하더라도 컴파일러가 when이 모든 경우를 제대로 처리하고 있는지 알 수가 없다. 혹 실수로 새로운 클래스 처리를 잊어버렸더라도 디폴트 분기가 선택되기 때문에 심각한 버그가 발생할 수 있다.

이런 문제를 코틀린은 sealed 클래스를 통해 해결했다!

# 4-2. 클래스 생성자와 프로퍼티

코틀린에는 주 생성자(primary constructor)와 부 생성자(secondary constructor)가 존재한다.
주 생성자는 class 선언과 함께 선언하고, 부 생성자는 추가적인 생성자가 필요할 때 사용한다.

#### 4.2.1 주 생성자

```
class User(val nickname: String)
```
주 생성자는 클래스 선언과 함께 정의된다. (클래스 이름 뒤에 오는 괄호로 둘러싸인 코드를 주 생성자라고 부른다.)

```
class User constructor(_nickname: String){
    val nickname: String
    
    init{
    	nickname = _nickname
    }
}
```
**constructor**는 주 생성자를 나타내기 위한 키워드이나 한정자나, 다른 키워드가 붙지않는다면 생략가능
**init**는 초기화 작업을 할 때 사용(클래스가 객체로 생성될 때 해주는 초기화 작업)

```
open class Person(val age: Int) { ... }

class Batman(val age: Int): Person(int) {
    ...
}
```
만약 상속을 통해서 class를 만드는 경우 부모의 생성자가 있다면 자식 클래스에서 반드시 호출해야 한다.
생성자를 정의하지 않는 경우에는 기본적으로 인자가 없는 생성자를 만든다.

#### 4.2.2 부 생성자

생성자가 여러개 필요할 때는 부 생성자를 생성하여 사용합니다.
```md
class TextView: View {

	constructor(context: Context) : this(context, null){
  		...
   	}
    
  	constructor(context: Context, attr: AttributeSet) : super(context, attr){
   	 	...
    	}
}
```
부모의 생성자는 **super()** 내 생성자 중 다른 생성자는 **this()** 로 호출이 가능하다.

# 4-3. 데이터 클래스와 클래스 위임

#### 4.3.1 클래스의 기본 메소드 구현

자바의 클래스는 toString, equals, hashCode를 반드시 오버라이드 해야 합니다.
하지만 코틀린에서는 이를 자동으로 컴파일러 해줍니다. (따로 만들어야 하는 수고로움을 덜 수 있음.)

```
class Client(val name: String, val postalCode: Int) {
    override fun toString() = "Client(name=$name, postalCode=$postalCode)"
}

fun main(args: Array<String>) {
    val client1 = Client("Alice", 342562)
    println(client1)
    // Client(name=Alice, postalCode=342562)
}
```
toString()을 override하여 재정의한 코드.
만약 재정의 해주지 않으면 StringRepresentationToString.Client@610455d6 라는 값을 뱉어낸다.

```
class Client(val name: String, val postalCode: Int)

fun main(args: Array<String>) {
    val client1 = Client("Alice", 342562)
    val client2 = Client("Alice", 342562)
    println(client1 == client2)
    // false
}
```
위의 두 객체는 동일하지 않기 때문에 결과값이 false가 나온다.
만약, 서로 다른 두 객체가 내부에 동일한 데이터를 포함하는 경우 그 둘을 동등한 객체로 간주해야 하는 상황이 있을 수도 있다.

```
class Client(val name: String, val postalCode: Int){
    override fun equals(other: Any?): Boolean {
        if(other == null || other !is Client)
            return false
        return name == other.name &&
                postalCode == other.postalCode
    }

    override fun toString() = "Client(name=$name, postalCode=$postalCode)"
}

fun main(args: Array<String>){
    val processed = hashSetOf(Client("Alice", 342562))
    println(processed.contains(Client("Alice", 342562)))
}
```
결과값은?? fales 이다. 데이터가 똑같으면 같게끔 정의를 해줬는데도 왜 그럴까?
hashCode()가 없기 때문이다. 따라서, equals()를 override한다면 hashCode도 반드시 override 해야 한다.
이는 **JVM의 언어에서는 equals()가 true인 객체는 반드시 hashCode()값도 동일해야 한다** 는 제약이 있기 때문이다.

hashSet()은 객체를 찾을 때 hashCode()를 이용하여 찾는다. 그리고나서 hashCode()가 같은게 여러개 존재한다면 그때서야 값을 비교한다. 위 코드의 경우 hashCode()가 다르기 때문에 false가 발생했다.

```
override fun hashCode(): Int = name.hashCode()*31 + postalCode
```
따라서, 위와 같은 코드를 override해야 값이 true가 된다.

#### 4.3.2 데이터 클래스

위와 같은 귀찮은 작업을 data class를 통해서 한번에 해결할 수 있다.

```
data class Client(val name: String, val postalCode: Int)

fun main(args: Array<String>) {
    val bob = Client("Bob", 973293)
    println(bob.copy(postalCode = 382555))
    // Client(name=Bob, postalCode=382555)
}
```
class 앞에 data 키워드만 붙이면 data 클래스를 생성할 수 있다.

data로 선언된 Client 클래스는 컴파일시에 아래와 같은 작업이 추가적으로 수행된다.

- **equals()**: 인스턴스간 비교를 위한 equals() 자동 생성
- **hashCode()**: Hash 기반 container에서 키로 사용할 수 있도록 hashCode() 자동 생성
- **toString()**: property 순서대로 값을 반환해주는 toString() 자동 생성

이 외에도 copy() 함수를 제공.
data 클래스를 불변 클래스로 만들면서 일부 값이 변경되어야 할 때, 기존 값들을 복사하여 새 객체를 만들어주는 역할을 함.

#### 4.3.3 클래스 위임 (class delegation): by

클래스의 기능을 일부를 재구현 하는 클래스를 만들때 by를 사용하면 편리하게 만들 수 있습니다.
코틀린은 기본적으로 class의 상속을 제한합니다. (final class가 기본값임)
따라서, 상속을 원하는경우 명시적으로 open class로 만들어야 한다.(+ fragile base problem 발생하는 경우 때문에)

```
class CountingSet<T>(
        val innerSet: MutableCollection<T> = HashSet<T>()
) : MutableCollection<T> by innerSet { // MutableCollection의 구현을 innerSet에게 위임한다.

    var objectsAdded = 0

    override fun add(element: T): Boolean { // 위임하지 않고 새로운 구현을 제공
        objectsAdded++
        return innerSet.add(element)
    }

    override fun addAll(c: Collection<T>): Boolean { // 위임하지 않고 새로운 구현을 제공
        objectsAdded += c.size
        return innerSet.addAll(c)
    }
}

fun main(args: Array<String>) {
    val cset = CountingSet<Int>()
    cset.addAll(listOf(1, 1, 2))
    println("${cset.objectsAdded} objects were added, ${cset.size} remain")
    // 3 objects were added, 2 remain
}
```
CountingSet 함수는 MutableCollection을 상속받으나, innerSet에 해당 구현을 위임한다.
이를 데코레이터 패턴(Decorator pattern)이라고 한다.
상속할 수 없는 객체에 특정 기능을 추가하거나, 변경하려고 할 때 사용.
기반이 되는 클래스의를 property로 가지고 변경 또는 추가를 원하는 기능을 재정의 합니다.

# 4-4. Object

코틀린에는 static 개념이 없다.
사실 개념이 없다기 보다는 static keyword가 없기 때문에 java의 static 개념을 코틀린에서 어떻게 표현해야하는지를 중점적으로 살펴보자.

- **싱글턴** 을 정의하는 방법
- **동반객체(companion object)** 를 이용한 팩토리 메소드 구현
- **무명 클래스** 의 선언

*싱글턴 패턴이란 전역 변수를 사용하지 않고 객체를 하나만 생성하도록 하며, 생성된 객체를 어디에서든지 참조할 수 있도록 하는 패턴

#### 4.4.1 싱글턴 (Singleton)
코틀린에서는 object를 이용하여 클래스를 정의함과 동시에 객체를 생성할 수 있다.
즉, 싱글턴을 쉽게 구현할 수 있다는 뜻이다.

```
object Payroll{
	val allEmployees = arrayListOf()
    fun calculateSalary(){
    	for(person in allEmployees){
        	...
        }
    }
}

Payroll.allEmployees.add(Person("홍길동", "홍순동"))
Payroll.calculateSalary()
```
object로 선언하면 클래스 선언과 동시에 객체가 생성된다.
객체 이름을 통해 property나 메서드에 직접 접근할 수 있다.

object 객체 역시 다른 class를 상속하거나 interface를 구현할 수 있다.
```
object CaseInsensitiveFileComparator : Comparator<File>{
    override fun compare(file1: File, file2: File): Int {
        return file1.path.compareTo(file2.path, ignoreCase = true)
    }
}

fun main (args: Array<String>){
    println(CaseInsensitiveFileComparator.compare(
            File("/User"), File("/user")))
    val files = listOf(File("/Z"), File("/a"))
    println(files.sortedWith(CaseInsensitiveFileComparator))
}
```
comparator는 여러개를 구현할 필요가 없기 때문에 singleton으로 하나만 만들고 사용하면 편리하다.
직접 compare를 호출해서 사용할 수도 있고, list에서 sortedWith 함수를 이용하여 객체 자체를 넘겨줄 수도 있다. (sortedWith는 리스트를 정렬하는 함수이다.)

#### 4.4.2 동반 객체(Companion Object)

코틀린에서는 static을 지원하지 않는대신 top-level function을 통해 같은 효과를 낼 수 있다.
단, top-level function은 class 내부에 선언된 private property에는 접근할 수 없는 제한을 받는다.
이를 해결하기 위해 동반 객체라는 개념이 존재한다.

클래스의 인스턴스와 상관없이 호출해야 하지만 class의 내부 정보에 접근할 수 있는 함수가 필요할 때 동반객체를 class 내부에 선언한다. (java로 따지면 class 내부에 static 함수를 넣는다고 생각하면 된다.)

```
class A {
    companion object {
        fun bar() {
            println("Companion object called")
        }
    }
}

fun main(args: Array<String>){
    A.bar()
}
```
클래스 내부에 선언된 동반 객체는 호출할 때 클래스 이름으로 바로 호출할 수 있다. (java의 static과 동일)

또한 동반 객체는 외부 클래스의 private property에도 접근이 가능하기에, factory method를 만들 때도 적합하다.

```
fun getFacebookName(accountId: Int) = "fb:$accountId"

class User private constructor(val nickname: String){
    companion object {
        fun newSubscribingUser(email: String) =
                User(email.substringBefore('@'))

        fun newFacebookUser(accountId: Int) =
                User(getFacebookName(accountId))
    }
}

fun main(args: Array<String>){
    val subscribingUser = User.newSubscribingUser("seope@gmail.com")
    val facebookUser = User.newFacebookUser(4)
    println(subscribingUser.nickname)
}
```
User는 private constructor를 가지기 때문에 외부에서 생성할 수 없다.
따라서, 외부에서는 companion으로 제공되는 Factory Method를 이용해서만 객체를 생성할 수 있도록 제한할 수 있다.

*Factory Method Pattern
-객체를 만들어 내는 부분을 서브 클래스에 위임하는 것.
-즉, 객체를 만들어내는 공장(Factory)을 만드는 패턴



출처
https://brunch.co.kr/@mystoryg/9#_=_
https://tourspace.tistory.com/106
http://hadeslee.tistory.com/29