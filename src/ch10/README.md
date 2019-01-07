# 애노테이션과 리플렉션

```
1. 애노테이션 적용과 정의
2. 리플렉션을 사용해 실행 시점에 객체 내부 관찰
3. 코틀린 실전 프로젝트 예제
```

* 애노테이션을 사용하면 라이브러리가 요구하는 의미를 클레스에 부여 할 수 있다
* 애노테이션 직접 만들기 어렵고, 처리하는 코드 작성하기는 더 어렵다
* 애노테이션 사용은 자바와 동일, 선언 하는 문법이 약간 다르다.
* 리플렉션을 사용하면 실행 시점에 컴팡일러 내부 구조를 분석할 수 있다.
* 리플렉션 API의 일반 구조는 자바와 같지만 세부 사항에서 약간 차이가 있다.

## 10.1 애노테이션 선언과 적용

### 10.1.1 애노테이션 적용
> 애노테이션은 @ 과 이름 으로 이뤄진다.

```kotlin
class Test {
    // @TEST 애노테이션으로 제이유닛 프레임워크에 메서드를 호출하라고 지시
    @Test fun test() {
        Assert.assertTrue(true)
    }
}
```


@Deprecated 
 * 자바와 동일. 
 * replaceWith 파라미터 제공 
 *  인텔리J 에서 경고 메시지와 퀵픽스 제시

```kotlin
@deprecated("Use removeAt(index) instead.", ReplaceWith("removeAt(index)"))
fun remove(index: Int) {...}
```

> 애노테이션 인자를 넘길 때는 괄호 안에 인자를 넣는다. 

> 애노테이션 인자로 원시 타입의 값, 문자열, enum, 클래스 참조, 다른 애노테이션클래스, 배열(앞에 요소) 
 * 클래스를 애노테이션 인자로 지정할 때는 ::class를 넣어야 한다. ex) @MyAnnotation(MyClass::class)
 * 다른 애노테이션을 인저로 넣을 때는 @를 넣지 않아야 한다. ReplaceWith는 애노테이션 이다.
 * 배열을 지정할려면 arrayOf 함수를 사용한다. ex) @RequestMapping(path = arrayOf("/foo","/var"))
 * 자바에서 선언한 애노테이션 클래스를 사용한다면 value 라는 이름의 파라미터가 필요에 따라 자동으로 가변 길이 인자로 변환 된다. @JavaAnnotaionWithArrayValue("acb", "foo", "bar") 처럼 arrrayOf 함수를 쓰지 않아도 된다.
 * 프로퍼티는 인자로 사용하려면 const를 붙여야 한다. 
 

 ### 10.1.2 애노테이션 대상
 > 코틀린 소스코드에서 한 선언을 컴파일한 결과가 여러 자바선언과 대응하는 경우가 자주 있다. 코틀린 선언에 대응하는 자바 선언에 각각 애노테이션을 붙여야 할 때가 있다. ex) 코틀린 프로퍼티는 자바 필드와 게터 메서드 선언과 대응한다.

 사용지점대상(use-site target) 선언으로 애노테이션을 붙일 요소를 정할 수 있다.
 
 @사용지점대상:애노테이션이름 

 @get:Rule

규칙을 지정하려면 공개(public) 필드, 메스드 앞에 @Rule 붙여야 한다.

```kotlin
class HasTempFolder {
    @get:Rule //프로퍼티가 아닌 게터에 애노테이션을 붙인다.
    val folder = TemporaryFolder()

    @Test
    fun testUsingTempFolder() {...}
}
```

* property - 프로퍼티 전체, 자바에서 선언된 애노이테션에서는 사용 불가
* field - 프로퍼티에 의해 생성되는 필드
* get
* set
* receiver - 확장 함수, 프로퍼티 수신 객체 파라미터
* param - 생성자 파라미터
* setparam - 새터 파라미터
* delegate - 위임 프로퍼티의 위임 인스턴스를 담아둔 필드
* file - 파일 안에 선언된 최상위 함수와 프로퍼티를 담아두는 클래스, package 선언 앞에서 적용 할 수 있다. ex) @file:JvmName("StringFunctions")

```
자바 API를 애노테이션으로 제어하기
@Volatile, @Strictfp
@JvmName - 자바 필드나 메서드 이름을 변경
@JvmStatic - 자바 정적 메서드
@JvmOverloads - 오버로딩 함수 생성
@JvmField - 게터 세터가 없는 public 자바 필드로 프로퍼티 노출
```

### 10.1.3 애노테이션을 활용한 JSON 직렬화 제어

직렬화 - 객체를 저장하거나 네트워크로 전송하기 위해 텍스트나 이진 형식으로 변환 한다

역직렬화 - 텍스트나 이진 형식으로 저장된 데이터를 원래의 객체를 만들어 낸다.

JSON 라이브러리 - Jackson, GSON, jkid

```kotlin
data class Pserson(val name: String, val age: Int)

val person = Person("Alice", 29)
println(serialize(person))

{"age":29, "name": "Alice"}
```

```kotlin
val json = """{"name": "Alice", "age": 29}"""
println(deserialize<Person>(json)) //타입 인자 클래스를 명시해야한다.

Person(name=Alice, age=29)
```

* @JsonExclude - 직렬화, 역직렬화시 프로퍼티 무시
* @JsonName - 프로퍼티 이름 대신 애노테이션 이름을 쓴다.

```kotlin
data class Person(
    @JsonName("alias") val firstName: String, 
    @JsonExclude val age: Int? = null) //디폴트 값을 지정해야한다. 디폴트 값이 없으면 역직렬화 할수 없다.
```

### 10.1.4 애노테이션 선언

```kotlin
annotation class JsonExclude
```

* 애노테이션클래스는 선언이나 식과 관련 있는 메타데이터의 구조만 정의 할 수 있다. 내부에 아무 코드도 들어있을 수 없다.

```kotlin
annotation class JsonName(val name: String) // 파라미터에 val을 붙여야 한다.

@JsonName(name = "Lee")
@JsonName("Lee")

//자바
public @interface JsonName {
    String value(); //특별??
}
```

### 10.1.5 메타애노테이션: 애노테이션을 처리하는 방법 제어

> 메타애노테이션 - 애노테이션 클래스에 적용할 수 있는 애노테이션

```kotlin
// 표준 라이브러리 메타애노테이션. 적용가능 대상 지정
// @Target(AnnotationTarget.CLASS, AnnotationTarget.METHOD)
@Target(AnnotationTarget.PROPERTY) 
annotation class JsonExclude
```

```kotlin
@Target(AnnotationTarget.ANNOTATION_CLASS) //메타애노테이션을 직접 만들때 사용
annotation calss BindingAnnotation

@BindingAnnotation
annotation class MyBinding
```
 PROPERTY로 지정한 애노테이션은 자바에서 사용 할 수 없다. FIELD를 추가 해야한다.



@Retention 애노테이션 
 - 애노테이션클래스를 소스 수준, .class 파일에 저장, 실행 시점에 리플렉션을 사용 가능 여부를 지정하는 메타애노테이션
 - 자바는 기본 .class는 저장하지만 런타임에는 사용할수 없게 한다.
 - 코틀린에서는 기본 @Retention을 RUNTIME으로 지정

### 10.1.6 애노테이션 파라미터로 클래스 사용

@DeserializeInterface 인터페이스 타입인 프로퍼티에 대한 역직렬화를 제어할 때 쓰는 애노테이션

```kotlin
interface Company {
    val name: String
}
data class CompanyImple(override val name: String) : Company

//Person 역직렬화 시 company에 CompanyImple 인스턴스를 만들어 설정한다.
data class Person(
    val name: String,
    @DeserializeInterface(CompanyImpl::class) val company: Company
)
```

```kotlin
annotation class DeserializeInterface(val targetClass: KClass<out Any>)
//공병성.....KClass<CompanyImpl> 은 KClass<out Any>의 하위 타입이다.
```

### 10.1.7 애노테이션 파라미터로 제네릭 클래스 받기

> 제이키드는 원시 타입이 아닌 프로퍼티를 중첩된 객체롤 직렬화한다.

@CustomSerializer 커스텀 직렬화 클래스에 대한 참조를 인자로 받는다. 커스텀 직렬화 클래스는 ValueSerializer 인터페이스를 구현해야한다.

```kotlin
interface ValueSerializer<T> {
    fun toJsonValue(value: T) : Any?
    fun fromJsonValue(jsonValue: Any?): T
}
```

```kotlin
object DateSerializer : ValueSerializer<Date> {
    private val dateFormat = SimpleDateFormat("dd-mm-yyyy")

    override fun toJsonValue(value: Date): Any? =
            dateFormat.format(value)

    override fun fromJsonValue(jsonValue: Any?): Date =
            dateFormat.parse(jsonValue as String)
}

data class Person(
    val name: String,
    @CustomSerializer(DateSerializer::class) val birthDate: Date
)
```

```kotlin
annotation class CustomSerializer(
    //어떤 타입이 올지 알수 없으므로 스타프로젝션을 사용
    val serializerClass: KClass<out ValueSerializer<*>>
)
```

* 클래스 인자 - KClass<out 허용할 클래스 이름>
* 제너릭 클래스 인자 - KClass<out 허용할 클래스 이름<*>>

## 10.2 리플렉션: 실행 시점에 코틀린 객체 내부 관찰

* 실행 시점에 객체 프로퍼티와 메서드에 접근할 수 있게 해주는 방법
* 타입과 관계없이 객체를 다뤄야 하거나 객체가 제공하는 메서드나 프로퍼티 이름을 오직 실행 시점에만 알 수 있는 경우

> java.lang.reflect

> kotlin.reflect - 프로퍼티, 널이 될 수 있는 타입 같은 코틀린 고유 개념에 대한 리플렉션

### 10.2.1 코틀린 리플렉션 API: KClass, Kcallable, KFunctin, KProperty

[KClass](https://goo.gl/UNXeJM)

[표준라이브러리](http://mng.bz/em4i)
```kotlin
class Person(val name: String, val age: Int)

import kotlin.reflect.full.* //memberProperties 확장 함수 임포트
val person = Person("Alice", 29)
val kClass = person.javaClass.kotlin //KClass<Person>
pritnln(kClass.simpleName) //Person

kClass.memberProperties.forEach {println(it.name)}
//age
//name
```

* KCallable 은 함수와 프로퍼티 공통 상위 인터페이스. call 메서드가 들어 있다. call은 함수 프로퍼티의 게터 호출

```kotlin
interface KCallable<out R> {
    fun call(vararg args: Any?):R
    ...
}

// call 사용
fun foo(x: Int) = println(x)
val kFunction = ::foo //멤버참조, KFunction1<Int, Unit>
kFunction.call(42) //42

```

* call에 넘긴 인자 개수와 함수의 정의된 파라미터 개수가 맞아야 한다.
* KFunction1 - 파라미터 1개, 함수 호출 하려면 invoke 메서드사용 or 직접 호출

```kotlin
import kotlin.reflect.KFunction2

fun sum(x: Int, y: Int) = x + y
val kFunction: KFunction2<Int, Int, Int> = ::sum
println(kFunction.invoke(1,2) + kFunction(3,4)) // 10

kFunction(1) //ERROR: No value passed for parameter p2
```

KProperty

 * call 메서드 호출하면 게터 호출

 ```kotlin
 var counter = 0
 val kProperty = ::counter //최상위 프로퍼티 KProperty0
 kProperty.setter.call(21) //세터로 21 넘긴다.
 pritnln(kProperty.get()) //21
 ```


 ```kotlin
class Person(val name: String, val age: Int)

val person = Person("Alice", 29)
val memberProperty = Person::age //멤버 프로퍼티 KProperty1
// KProperty<Person, Int> <수신 객체 타입, 프로퍼티 타입>
pritnln(memberProperty.get(person)) //29
 ```
* 함수의 로컬 번수는 접근 할 수 없다.

<img src="https://t1.daumcdn.net/cfile/tistory/99178A33599A8FFB37"/>


### 10.2.2 리플렉션을 사용한 객체 직렬화 구현

제이키드 직렬화 함수

`fun serialize(obj:Any): String`

`private fun StringBuilder.serializeObject(x: Any) {append(...)}`

`fun serialize(obj: Any) : String = buildString { serializeObject(obj)}`

```kotlin
private fun StringBuilder.serializeObject(obj: Any) {
    val kCalss = obj.javaClass.kotlin // KClass
    val properties = kClass.memberProperties //프로퍼티
    properties.jotinToStringBuilder(this, prefix = "{", postfix = "}") { prop -> serializeString(prop.name)} // 프로퍼티 이름
    append(":")
    serializePropertyValue(prop.get(obj)) // 프로퍼티 값
}


{prop1:value1, prop2:value2}
```

### 10.2.3 애노테이션을 활용한 직렬화 제어

 * @JsonExclude, @JsonName, @CustomSerializer 를 serializeObject 에서 어떻게 처리 하는지...

 * @JsonExclude - 특정 프로퍼티를 직렬화 제외
    * KClass - memberProperties
    * KAnnotatedElement 인터페이스에 annotations 프로퍼티가 있다
    * property.annotations을 통해 애노테이션 얻을 수 있다
    * findAnnotation 함수 

```kotlin
inline fun <reified T> KAnnotatedElement.findAnnotation() : T? = annotations.filterIsInstance<T>().firstOrNull()

val properties = kClass.memberProperties.filter { it.findAnnotation<JsonExclude>() == null}
```

 * @JsonName
    * ex) @JasonName("alias") val firstName: String
    * 애노테이션 존재 여유뿐 아니라 애노테이션에 전달한 인자도 알아야 한다.
    
```kotlin
val jsonNameAnn = prop.findAnnotation<JsonName>() //@JsonName 애노테이션 인스턴스를 얻는다
val propName = jsonNameAnn?.name ?: prop.name // name 인자를 찾고 없으면 prop.name 을 사용한다.
```



> 프로퍼티 필터링을 포함하는 객체 직렬화
```kotlin
private fun StringBuilder.serializeObject(obj: Any)  {
    obj.javaClass.kotlin.memberProperties
        .filter {it.findAnnotation<JsonExclude>() == null}
        .joinToStringBuilder(this, prefix = "{", postfix = "}") {
            serializeProperty(it, obj)
        }
}
```

> 프로퍼티 직렬화
```kotlin
private fun StringBuilder.serializeProperty(prop: KProperty1<Any, *>, obj: Any) {
    val name = prop.findAnnotation<JsonName>()?.name ?: prop.name
    serializeString(name)
    append(": ")

    val value = prop.get(obj)
    val jsonValue = prop.getSerializer()?.toJsonValue(value) ?: value
    serializePropertyValue(jsonValue)
}
```

> 프로퍼티의 값을 직렬화하는 직렬화기 가져오기
```kotlin
fun KProperty<*>.getSerializer(): ValueSerializer<Any?>? {
    val customSerializerAnn = findAnnotation<CustomSerializer>() ?: return null
    val serializerClass = customSerializerAnn.serializerClass
    val valudeSerializer = serializerClass.objectInstance ?: serializerClass.createInstance()

    @Suppress("UNCHECKED_CAST")
    return valueSerializer as ValueSerializer<Any?>
}
```

### 10.2.4 JSON 파싱과 객체 역직렬화

 * deserialize 함수에 실체화할 타입파라미터를 넘겨 객체 인스턴스를 얻는다.
```kotlin
inline fun <reified T: Any> deserialize(json: String) : T
```
1. 어휘북석기(렉서) - 문자열을 토큰 리스트로 변환(문자토큰, 값토큰, {, 문자열 값, 정수 값)
2. 문법 분석기(파서) - 토큰의 리스트를 구조화된 표현으로 변환 , 키/값 쌍의 배열
3. 역직렬화 컴포넌트(객체 생성)

> Json 파서 콜백 인터페이스
```kotlin
interface JsonObject {
    fun setSimpleProperty(propertyName: String, value: Any?)
    fun createObject(propertyName: String) : JsonObject
    fun createArray(propertyName: String) : JsonObject
}
```

<img src="https://t1.daumcdn.net/cfile/tistory/99F57933599A90221C"/>


> JSON 데이터로부터 객체를 만들어내기 위한 인터페이스

```kotlin
interface Seed: JsonObject {
    fun spawn(): Any? //객체 생성이 끝난 후 결과 인스턴스 제공
    
    fun createCompositeProperty(propertyName: String, isList: Boolean): JsonObject // 중첩 리스트, 중첩 객체 생성

    override fun createObject(propertyName: String) = createCompositeProperty(propertyName, false)

    override fun createArray(propertyName: String) = createCompositeProperty(propertyName, true)
}
```

```kotlin
// 최상위 역직렬화 함수
fun<T: Any> deserialize(json: Reader, targetClass: KClass<T>): T{
    val seed = ObjectSeed(targetClass, ClassInfoCache())
    Parser(json, seed).parse()
    return seed.spawn() 
}

// 객체 역직렬화하기
class ObjectSeed<out T: Any>(
    targetClass:KClass<T>,
    val classInfoCache: ClassInfoCache
    ) : Seed {
    private val classInfo: ClassInfo<T> = classInfoCache[targetClass] //인스턴스 만들 때 필요한 정보 캐시
    private val valueArguments = mutableMapOf<KParameter, Any?>()
    private val seedArguments = mutableMapOf(KParameter, Seed)()
    private val arguments: Map<KParameter, Any?> //생성자 파라미터
        get() = valueArguments + seedArguments.mapValue { it.value.spawn() } 

    override fun setSimpleProperty(propertyName: String, value: Any?) {
        val param = classInfo.getConstructorParameter(propertyName)
        valueArguments[param] = classInfo.deserializeConstructorArgument(param, value)
    }

    override fun createCompositeProperty(propertyName: String, isList:Boolean): Seed {
        val param = classInfo.getConstructorParameter(propertyName)
        val deserializeAs = classInfo.getDeserializeClass(propertyName)
        val seed = createSeedForType(deserializeAs ?: param.type.javaType, isList)
        return seed.apply { seedArguments[param] = this }
    }

    override fun spawn() : T = classInfo.createInstance(arguments)
}
```

### 10.2.5 최종 역직렬화 단게: callBy(), 리플렉션을 사용해 객체 만들기

 > ClassInfo - 객체 인스턴스를 생성하고 생성자 파라미터 정보를 캐시, 리플렉션 연산 비용을 줄여주는 클래스

  * KCallable.call 은 디폴트 파라미터를 미지원
  * KCallable.callBy 디폴트 파라미터 지원

`fun callBy(args: Map<KParameter, Any?>):R`
  
  * args 각 값의 타입이 생성자의 파라미터 타입과 일치 해야 한다. 
  * 숫자 Int, Long, Double 등에 주의
  * KParameter.type 파라미터 타입을 알 수 있다.

```kotlin
fun serializerForType(type: Type) : ValueSerializer<out Any?>? =
when(type) {
    Byte::class.java -> ByteSerializer
    Int::class.java -> IntSerializer
    Boolean::class.java -> BooleanSerializer
    ...
    else -> null
}

object BooleanSerializer : ValueSerializer<Boolean> {
    override fun fromJsonValue(jsonValue: Any?) : Boolean {
        if(jaonValue !is Boolean) throw JKidException("Boolean expected")
        return jsonValue
    }
    override fun toJsonValue(value: Boolean) = value
}
```

 * callBy 메서드에 생성자 파라미터와 그 값을 연결해주느 맵을 넘기면 객체의 주 생성자를 호출할 수 있다.
 * 클래스별로 한번만 검색을 수행하고 검색 결과 캐시

 ```kotlin
class ClassInfoCache {
    private val cacheDate = mutableMapOf<KClass<*>, ClassInfo<*>>()
    @Suppress("UNCHECKED_CAST")
    operator fun <T: Any> get(cls: KClass<T>): ClassInfo<T> = cacheData.getOrPut(clas) {ClassInfo(cls)} as ClassInfo<T>
}
 ```

 ```kotlin 
 class ClassInfo<T: Any>(cls: KClass<T>) {
     private val constructor = cls.primaryConstructor!!
     private val jsonNameToParam = hashMapOf<String, KParameter>() //JSON 파일의 각 키, 파라미터
     private val paramToSerializer = hashMapOf<KParameter, ValueSeriralizer<out Any?>>() // 각 파라미터에 대한 직렬화기
     private val jsonNameToDeserializeClass = hashMapOf<String, Class<out Any>?>() // @DeserializeInterface 애노테이션을 지정한 클래스

     init {
         constructor.parameters.forEach { cacheDataForParameter(cls, it)}
     }

     fun getConstructorParameter(propertyName: String): KParameter = jsonNameToParam[propertyName]!!
     fun deserializeConstructorArgument(para: KParameter, value: Any?): Any? {
         val serializer = paramToSerializer[param]
         if(serializer != null) return serializer.fromJsonValue(value)
         validateArgumentType(param, value)
         return value
     }

     fun createInstance(arguments: Map<KParameter, Any?>): T{
         ensureAllParameterPresent(arguments)
         return constructor.callBy(arguments)
     }

    //생성자가 필요한 필수 파라미터가 맵에 있는지 검사
     private fun ensureAllParameterPresent(arguments: Map<KParameter, Any?>){
         for(param in constructor.parameters) {
             if(arguments[param] == null && !param.isOptional && !param.type.isMarkedNullable) {
                 throw JKidException("Missing value for parameter ${param.name}")
             }
         }
     }
    private fun cacheDataForParameter{}
    private fun validateArgumentType{}
 }
 ```

 ## 10.3 요약
  * 코틀린에서 애노테이션 문법은 자바와 거의 같다
  * 코틀린에서는 자바보다 더 넓은 대상에 애노테이션을 적용할 수 있다. ex) 파일, 식
  * 애노테이션 인자로 원시 타입 값, 문자열, 이넘, 클래스 참조, 다른 애노테이션 클래스의 인스턴스. 앞의 타입의 배열
  * @get:Rule - 코틀린 선언이 여러 바이트코드 요소를 만들어 낼 때 애노테이션 적용 대상을 명시 
  * 애노테이션 클래스 정의할 때는 본문이 없고 주 생성자의 모든 파라미터를 val 프로퍼티로 표시한 코틀린 클래스를 사용
  * 메타애노테이션 - 대상, 애노테이션 유지 방식 등 애노테이션 특성을 지정
  * 리플랙션 API로 실행 시점에 메소드, 프로퍼티를 열거하고 접근 할 수 있다. Kclass, KFunction등
  * 클래스를 아는경우 - ClassName::class 로 KClass 인스턴스를 얻을 수 있다. 실행 시점에는 obj.javaClass.kotlin으로..
  * KFunction, FProperty 는 KCallable 확장.  call 메서드 제공
  * KCallable.callBy 호출 하면 디폴트 파라미터 값을 사용 할 수 있다
  * KFunction0, KFunction1 파라미터 수가 다른 함수 표현. invoke 로 호출 할 수 있다.
  * KProperty0는 최상위 프로퍼티나 변수, KProperty1은 수신 객체가 있는 프로퍼티에 접근 할 때 쓴다. get 메서드 제공
  * KMutableProperty0, KMutableProperty1은 KProperty0, KProperty1 을 확장한다. set 메서드 제공 


<img src="https://cdn.clien.net/web/api/file/F03/7452296/23c81e1ae7d590.jpg?w=640&h=1260"/>

