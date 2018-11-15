## Q&A

### Q1

예제 7.18에서 처럼 lazy 사용시 예제 7.17처럼 backing property 기법을 사용하는 건지? 

그렇다면 예제 7.17의 _emails 처럼 값을 실제 저장하고 읽기 처리해주는 객체가 계속 존재하는 것인지?

### A1

Lazy.kt의 lazy 관련 부분을 보았을 때는 backing property 기법과 실제 저장하고 읽기 처리해주는 객체가 계속 존재할 것으로 보입니다.

대신 람다는 최초 실행 후 null 처리되는 것 같습니다.

관련 소스 아래에 첨부합니다.



#### Lazy.kt에서 lazy에 대한 처리

lazy 사용시 아래 코드 실행.

Lazy\<T\> 를 상속받는 SynchronizedLazyImpl 객체를 반환.

```kotlin
/**
 * Creates a new instance of the [Lazy] that uses the specified initialization function [initializer]
 * and the default thread-safety mode [LazyThreadSafetyMode.SYNCHRONIZED].
 *
 * If the initialization of a value throws an exception, it will attempt to reinitialize the value at next access.
 *
 * Note that the returned instance uses itself to synchronize on. Do not synchronize from external code on
 * the returned instance as it may cause accidental deadlock. Also this behavior can be changed in the future.
 */
@kotlin.jvm.JvmVersion
public fun <T> lazy(initializer: () -> T): Lazy<T> = SynchronizedLazyImpl(initializer)
```



Lazy 클래스는 value와 isInitialized() 메소드만 있음.

```kotlin
public interface Lazy<out T> {
    /**
     * Gets the lazily initialized value of the current Lazy instance.
     * Once the value was initialized it must not change during the rest of lifetime of this Lazy instance.
     */
    public val value: T
    /**
     * Returns `true` if a value for this Lazy instance has been already initialized, and `false` otherwise.
     * Once this function has returned `true` it stays `true` for the rest of lifetime of this Lazy instance.
     */
    public fun isInitialized(): Boolean
}
```



SynchronizedLazyImpl 에서는 Lazy의 value 를 오버로딩하여 get 시도시 SynchronizedLazyImpl 의 프로퍼티인 _value 값을 반환하는 것으로 보임. (Thread-Safe 하게...)

최초 get 시도 시에는 _value의 초기값(UNINITIALIZED_VALUE)을 확인 후 lazy 사용시 받았던 람다인 initializer는 처리 완료 후 리턴된 값을 _value에 설정함.

이때 람다로 받았던 initializer는 null 처리됨.

그 뒤로 get 시도시에는 _value가 초기값과 다르기 때문에 _value를 리턴함.

```kotlin
private object UNINITIALIZED_VALUE

private class SynchronizedLazyImpl<out T>(initializer: () -> T, lock: Any? = null) : Lazy<T>, Serializable {
    private var initializer: (() -> T)? = initializer
    @Volatile private var _value: Any? = UNINITIALIZED_VALUE
    // final field is required to enable safe publication of constructed instance
    private val lock = lock ?: this

    override val value: T
        get() {
            val _v1 = _value
            if (_v1 !== UNINITIALIZED_VALUE) {
                @Suppress("UNCHECKED_CAST")
                return _v1 as T
            }

            return synchronized(lock) {
                val _v2 = _value
                if (_v2 !== UNINITIALIZED_VALUE) {
                    @Suppress("UNCHECKED_CAST") (_v2 as T)
                }
                else {
                    val typedValue = initializer!!()
                    _value = typedValue
                    initializer = null
                    typedValue
                }
            }
        }

    override fun isInitialized(): Boolean = _value !== UNINITIALIZED_VALUE

    override fun toString(): String = if (isInitialized()) value.toString() else "Lazy value not initialized yet."

    private fun writeReplace(): Any = InitializedLazyImpl(value)
}
```



## 7장 관례 정리

| 구분  | 사용형식  | 오버로딩 키워드  | 함수이름  | 파라미터  | 리턴  | 비고 |
| -------- | ---------- | ---------- | ---------- | ---------- | ---------- | ---------- |
|  이항 산술 연산자  | a * b  | operator  | times  | 다른타입 가능  | 다른타입 가능  | *= 도 지원 |
|  이항 산술 연산자  | a / b  | operator  | div  | 다른타입 가능  | 다른타입 가능  | /= 도 지원 |
|  이항 산술 연산자  | a % b  | operator  | rem(1.1미만 mod)  | 다른타입 가능  | 다른타입 가능  | %= 도 지원 |
|  이항 산술 연산자  | a + b  | operator  | plus  | 다른타입 가능  | 다른타입 가능  | += 도 지원 |
|  이항 산술 연산자  | a - b  | operator  | minus  | 다른타입 가능  | 다른타입 가능  | -= 도 지원 |
|  비트 연산자  | shl  | -  | -  | -  | -  | 왼쪽 시프트(<<) |
|  비트 연산자  | shr  | -  | -  | -  | -  | 오른쪽 시프트(부호 비트 유지, >>) |
|  비트 연산자  | ushr  | -  | -  | -  | -  | 오른쪽 시프트(0으로 부호 비트 설정, >>>) |
|  비트 연산자  | and  | -  | -  | -  | -  | 비트 곱 (&) |
|  비트 연산자  | or  | -  | -  | -  | -  | 비트 합 (|) |
|  비트 연산자  | xor  | -  | -  | -  | -  | 비트 배타 합 (^) |
|  비트 연산자  | inv  | -  | -  | -  | -  | 비트반전 (~) |
|  복합 대입 연산자  | a += b  | operator  | plusAssign  | 다른타입 가능  | Unit  |  |
|  복합 대입 연산자  | a -= b  | operator  | minusAssign  | 다른타입 가능  | Unit  |  |
|  복합 대입 연산자  | a *= b  | operator  | timesAssign  | 다른타입 가능  | Unit  |  |
|  복합 대입 연산자  | a /= b  | operator  | divAssign  | 다른타입 가능  | Unit  |  |
|  복합 대입 연산자  | a %= b  | operator  | remAssign(1.1 미만 modAssign)  | 다른타입 가능  | Unit  |  |
|  단항 연산자  | +a  | operator  | unaryPlus  | -  | 다른타입 가능  |  |
|  단항 연산자  | -a  | operator  | unaryMinus  | -  | 다른타입 가능  |  |
|  단항 연산자  | !a  | operator  | not  | -  | 다른타입 가능  |  |
|  단항 연산자  | ++a, a++  | operator  | inc  | -  | 수신객체 타입  |  |
|  단항 연산자  | --a, a--  | operator  | dec  | -  | 수신객체 타입  |  |
|  동등성 연산자  | ==, !=  | override  | equals  | Any?  | Boolean  | null 체크 및 타입 체크 필요 |
|  순서 연산자  | "a > b, a < b,  |
|  a >= b, a <= b"  | override  | compareTo  | 수신객체 타입  | Int  |  |
|  컬렉션  | a[i]  | operator  | get  | 다른타입 가능  | 다른타입 가능  |  |
|  컬렉션  | a[i] = b  | operator  | set  | 다른타입 가능  | Unit  |  |
|  범위  | a in b, a !in b  | operator  | contains  | 다른타입 가능  | Boolean  |  |
|  범위  | a..b  | operator  | rangeTo  | 다른타입 가능  | ClosedRange<T>  |  |
|  범위  | for(b in a)  | operator  | iterator  | -  | Iterator  |  |
|  구조분해  | val (a, b) = c  | operator  | componentN(1~)  | -  | 프로퍼티 타입  | data class는 5개까지 자동으로 지원하며 주생성자의 파라미터 순서대로 |
|  위임 프로퍼티  | var a: Type by Delegate  | -  | -  | -  | -  | "Type, Delegate 클래스 생성 |
|  Delegate 클래스는 getValue/setValue 메소드 필요" |
|  위임 프로퍼티  | val a by lazy  | -  | -  | -  | -  | val a by lazy { 람다 } |

