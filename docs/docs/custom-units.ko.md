# 사용자 정의 단위 추가

kunit은 현재 하나의 단위 그룹([길이](units/length.md))만 제공하지만, 전체 엔진(`KUnit`, `KMixedUnitInstance`,
접두어, 파생 단위)은 범용적이며 그룹에 독립적입니다. 새로운 물리량을 추가한다는 것은 `length` 패키지가
이미 확립한 패턴을 따르는 것을 의미합니다. 이 페이지에서는 처음부터 예시로 **질량**(Mass) 그룹
(`org.pcsoft.framework.kunit.mass`)을 추가하는 과정을 안내합니다.

## 1. 서브 패키지와 `KUnit` enum 생성

모든 단위 그룹은 `org.pcsoft.framework.kunit` 아래에 자신만의 서브 패키지를 가지며, 단위는 `KUnit`을
구현하는 `enum class`로 선언됩니다. `baseValue`는 그룹의 기본 단위로 변환하는 계수입니다 - 기본 단위
자체는 `baseValue == 1.0`을 가집니다.

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KUnit

/**
 * 질량의 구체적인 단위를 열거합니다. [baseValue]는 그룹의 기본 단위([BASE], 킬로그램)로 변환하는
 * 계수입니다: `1 단위 = baseValue * 킬로그램`.
 */
enum class KMassUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** 킬로그램, 질량의 SI 기본 단위; 정의상 [baseValue] = 1.0. */
    KILOGRAM("kg", 1.0),

    /** 그램, 1 g = 0.001 kg. */
    GRAM("g", 0.001),

    /** 국제 상용 파운드, 1 lb = 0.45359237 kg. */
    POUND("lb", 0.45359237),

    /** 국제 상용 온스, 1 oz = 0.028349523125 kg. */
    OUNCE("oz", 0.028349523125);

    companion object {
        /** 질량 그룹의 기본 단위; [KMassUnitInstance]의 모든 내부 값은 이 단위로 정규화됩니다. */
        val BASE: KMassUnit = KILOGRAM
    }
}
```

## 2. 래퍼 클래스 생성

래퍼 클래스(`KMassUnitInstance`)는 **위임**(상속이 아닌)을 통해 `KMixedUnitInstance`를 캡슐화하며, 항상
그룹의 기본 단위로 값을 정규화합니다. `KLengthUnitInstance`의 형태를 그대로 복사하세요 - 이는 지수에
대해 범용적이므로, 나중에 필요하다면 동일한 래퍼가 질량의 파생 물리량도 처리할 수 있습니다.

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitTarget
import org.pcsoft.framework.kunit.KUnitTerm

class KMassUnitInstance internal constructor(internal val instance: KMixedUnitInstance) {

    private val exponent: Int get() = instance.units.single().exponent

    val value: Double get() = instance.value

    fun valueAs(target: KUnitTarget): Double = instance.valueAs(target)

    operator fun plus(other: KMassUnitInstance): KMassUnitInstance = KMassUnitInstance(instance + other.instance)
    operator fun minus(other: KMassUnitInstance): KMassUnitInstance = KMassUnitInstance(instance - other.instance)

    operator fun times(other: KMassUnitInstance): KMixedUnitInstance = instance * other.instance
    operator fun div(other: KMassUnitInstance): KMixedUnitInstance = instance / other.instance
    operator fun times(other: KMixedUnitInstance): KMixedUnitInstance = instance * other
    operator fun div(other: KMixedUnitInstance): KMixedUnitInstance = instance / other

    operator fun compareTo(other: KMassUnitInstance): Int {
        check(exponent == other.exponent) { "Cannot compare KMassUnitInstance with different exponents: $exponent vs ${other.exponent}" }
        return value.compareTo(other.value)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is KMassUnitInstance) return false
        check(exponent == other.exponent) { "Cannot compare KMassUnitInstance with different exponents: $exponent vs ${other.exponent}" }
        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = instance.toString()
    fun toString(target: KUnitTarget): String = instance.toString(target)

    fun toKMixedUnitInstance(): KMixedUnitInstance = instance
}

/** 순수 질량 [KMixedUnitInstance]를 [KMassUnit.BASE]로 정규화하여 [KMassUnitInstance]로 다시 변환합니다. */
fun KMixedUnitInstance.toKMassUnit(): KMassUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KMassUnit) {
        "KMixedUnitInstance $this does not represent a pure mass-based value (expected exactly one term of a KMassUnit)"
    }
    val normalizedValue = value * Math.pow(unit.baseValue, term.exponent.toDouble())
    return KMassUnitInstance(KMixedUnitInstance(normalizedValue, listOf(KUnitTerm(KMassUnit.BASE, term.exponent))))
}

internal fun massUnitInstanceOf(value: Double): KMassUnitInstance =
    KMassUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KMassUnit.BASE, 1))))
```

## 3. 생성자 확장 함수 추가

`K...UnitExtensions.kt` 패턴을 따라, 단위마다 bare `val` 별칭과 `Number` 확장 함수를 추가하세요. 이렇게
하면 호출부에서 `5.kilograms` 또는 `1 kilo grams`를 사용할 수 있고, `kilograms`를 순수 `valueAs` 대상
으로도 전달할 수 있습니다.

```kotlin
package org.pcsoft.framework.kunit.mass

/** [KMassUnit.KILOGRAM]에 대한 bare 참조, [valueAs][KMassUnitInstance.valueAs]이나 접두어 infix 함수에 사용. */
val kilograms: KMassUnit = KMassUnit.KILOGRAM

/** [KMassUnit.GRAM]에 대한 bare 참조. */
val grams: KMassUnit = KMassUnit.GRAM

/** [KMassUnit.POUND]에 대한 bare 참조. */
val pounds: KMassUnit = KMassUnit.POUND

/** [KMassUnit.OUNCE]에 대한 bare 참조. */
val ounces: KMassUnit = KMassUnit.OUNCE

private fun of(value: Number, unit: KMassUnit): KMassUnitInstance = massUnitInstanceOf(value.toDouble() * unit.baseValue)

/** 모든 [Number] 타입으로부터 킬로그램 단위의 순수 질량 값을 생성합니다. */
val Number.kilograms: KMassUnitInstance get() = of(this, KMassUnit.KILOGRAM)

/** 그램 단위의 순수 질량 값을 생성합니다. */
val Number.grams: KMassUnitInstance get() = of(this, KMassUnit.GRAM)

/** 파운드 단위의 순수 질량 값을 생성합니다. */
val Number.pounds: KMassUnitInstance get() = of(this, KMassUnit.POUND)

/** 온스 단위의 순수 질량 값을 생성합니다. */
val Number.ounces: KMassUnitInstance get() = of(this, KMassUnit.OUNCE)
```

이것으로 충분합니다 - 모든 로직이 범용 루트 패키지에 있고 `KMassUnit : KUnit`만 있으면 동작하기 때문에,
이미 완전한 `+`, `-`, `*`, `/`, 비교 연산, SI 접두어(`5 kilo grams`), 그리고
`toKMixedUnitInstance()`/`toKMassUnit()` 왕복 변환을 무료로 얻게 됩니다.

```kotlin
import org.pcsoft.framework.kunit.mass.*

val a = 500.grams
val b = 2.pounds
val total = a + b            // KMassUnitInstance, 킬로그램으로 정규화됨
println(total.valueAs(kilograms))
println(total.valueAs(grams))

val heavier = b > a          // true
```

## 4. (선택 사항) 특수/파생 단위 추가

그룹에 특정 지수에 고정된, 흔히 사용되는 명명된 단위(면적의 헥타르와 같은)가 있다면,
`KLengthDerivedUnit`과 유사한 `KDerivedUnit` 객체를 추가하세요.

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KDerivedUnit

object KMassDerivedUnit {
    /** 미터법 톤, 1 t = 1000 kg (지수 1, 기본 단위의 대체 "명명된" 스케일링). */
    val TONNE: KDerivedUnit<KMassUnit> = KDerivedUnit(symbol = "t", exponent = 1, baseValue = 1000.0, referenceUnit = KMassUnit.BASE)
}
```

```kotlin
val truckLoad = 3.pounds.toKMixedUnitInstance().toKMassUnit() // 예시 목적으로만
println(2500.grams.valueAs(KMassDerivedUnit.TONNE)) // 0.0025
```

## 5. 다른 그룹과 결합

모든 것이 결국 범용 `KMixedUnitInstance` 엔진을 통해 흐르기 때문에, 새 그룹은 `*`/`/`를 통해 즉시 다른
그룹(예: 길이)과 결합됩니다 - 전체 규칙은 [혼합 단위](mixed-units.md)를 참고하세요.

```kotlin
import org.pcsoft.framework.kunit.length.*
import org.pcsoft.framework.kunit.mass.*

// 밀도 = 질량 / 부피
val density = 5.kilograms.toKMixedUnitInstance() / 2.liters.toKMixedUnitInstance()
```

## 6. 네이밍과 테스트 체크리스트

- 모든 공개 타입은 `K`로 시작합니다(`KMassUnit`, `KMassUnitInstance`, `KMassDerivedUnit` 등). 생성자
  확장 함수와 bare `val` 별칭(`kilograms()`, `grams` 등)은 예외이며 언어에 자연스러운 이름을 유지합니다.
- 모든 공개 멤버를 영어로, Markdown 형식으로, 유용한 곳(특히 연산자)에는 예시와 함께 문서화하세요.
- `length` 아래의 구조를 그대로 반영하여 그룹별로 완전한 테스트 스위트를 작성하세요:
    - `KUnit` enum 값 자체를 위한 전용 테스트 클래스,
    - 모든 연산자(`+`, `-`, `*`, `/`)와 모든 비교 연산자(`==`, `!=`, `<`, `<=`, `>`, `>=`)를 성공
      케이스와(해당하는 경우) `IllegalStateException` 실패 케이스 모두로 다루는 래퍼 클래스 전용
      테스트 클래스,
    - 완전한 접두어 × 단위 테스트 매트릭스(모든 단위/파생 단위를 모든 SI 접두어와 조합), 그리고
      접두어별 독립 테스트 1개,
    - 새 그룹을 적어도 하나의 다른 그룹과 결합하는 혼합 단위 테스트.
