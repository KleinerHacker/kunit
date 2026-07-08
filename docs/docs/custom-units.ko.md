# 사용자 정의 단위 추가

kunit은 현재 여러 단위 그룹을 제공합니다([거리](units/distance.md), [시간](units/time.md),
[저장 용량](units/storage.md), [속도](units/speed.md), [데이터 전송률](units/datarate.md)). 하지만 엔진 전체
(`KUnit`, `KMixedUnitInstance`, `of`/`into` 동사, 접두사 빌더)는 범용적이며 그룹에 독립적입니다. 새로운 물리량을
추가한다는 것은 같은 패턴을 따르는 것을 의미합니다. 이 페이지에서는 시연용 **질량** 그룹
(`org.pcsoft.framework.kunit.mass`) — 저장 용량 그룹을 본뜬 단순한 1차원 그룹 — 을 추가하는 과정을 단계별로
설명합니다.

## 1. 서브 패키지와 `KUnit` enum 만들기

각 단위 그룹은 `org.pcsoft.framework.kunit` 아래에 자체 서브 패키지를 가지며, 그 단위들은 `KUnit`을 구현하는
`enum class`로 선언됩니다. `baseValue`는 그룹의 기본 단위로의 변환 계수입니다 — 기본 단위 자체는
`baseValue == 1.0`을 가집니다.

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KUnit

enum class KMassUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** 킬로그램, 질량의 SI 기본 단위. 정의에 의해 [baseValue] = 1.0. */
    KILOGRAM("kg", 1.0),

    /** 그램, 1 g = 0.001 kg. */
    GRAM("g", 0.001),

    /** 국제 상용 파운드, 1 lb = 0.45359237 kg. */
    POUND("lb", 0.45359237),

    /** 국제 상용 온스, 1 oz = 0.028349523125 kg. */
    OUNCE("oz", 0.028349523125);

    companion object {
        /** 질량 그룹의 기본 단위. [KMassUnitInstance]의 모든 내부 값은 이 단위로 정규화된다. */
        val BASE: KMassUnit = KILOGRAM
    }
}
```

## 2. 래퍼 클래스 만들기

래퍼(`KMassUnitInstance`)는 `KMixedUnitInstance`를 **위임**(`KUnitMeasurable by instance`)으로 캡슐화하고
`KUnitInstance<KMassUnitInstance>`를 구현합니다. `KUnitInstance` 전용 멤버(`plus`/`minus`/`compareTo`)와 `of`를
뒷받침하는 `scaledBy` 오버라이드, 그리고 `equals`/`hashCode`/`toString`만 손으로 작성합니다.
`valueAs`/`toString(target)`는 **없습니다** — 읽기는 그룹 비의존적인 `into` 동사입니다. `KStorageUnitInstance`의
형태를 복사하세요.

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm

class KMassUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitMeasurable by instance, KUnitInstance<KMassUnitInstance> {

    /** `of`를 뒷받침: 값(킬로그램)을 스케일링하고 같은 타입을 반환한다. */
    override fun scaledBy(factor: Double): KMassUnitInstance = massUnitInstanceOf(value * factor)

    override operator fun plus(other: KMassUnitInstance): KMassUnitInstance = massUnitInstanceOf(value + other.value)
    override operator fun minus(other: KMassUnitInstance): KMassUnitInstance = massUnitInstanceOf(value - other.value)
    override operator fun compareTo(other: KMassUnitInstance): Int = value.compareTo(other.value)

    override fun equals(other: Any?): Boolean = other is KMassUnitInstance && value == other.value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = instance.toString()
}

/** 이미 킬로그램([KMassUnit.BASE])으로 표현된 값에서 [KMassUnitInstance]를 구축한다. */
internal fun massUnitInstanceOf(value: Double): KMassUnitInstance =
    KMassUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KMassUnit.BASE, 1))))

/** 순수 질량 [KMixedUnitInstance]를 [KMassUnitInstance]로 다시 변환하고 [KMassUnit.BASE]로 정규화한다. */
fun KMixedUnitInstance.toMass(): KMassUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KMassUnit) {
        "KMixedUnitInstance $this does not represent a pure mass value (expected exactly one term of a KMassUnit)"
    }
    return massUnitInstanceOf(value * unit.baseValue)
}
```

## 3. 값 1 맨 토큰과 접두사 빌더 프로퍼티 추가하기

프로젝트 관례에 따라 DSL 어휘를 두 파일로 나눕니다: 값 1 맨 토큰은 `K...UnitBareValues.kt`에, 접두사 빌더
프로퍼티 확장은 `K...UnitExtensions.kt`에 넣습니다. 이 둘을 합치면 호출자가 `5 of kilograms`나 `5 of kilo.grams`를
쓰고 `into`로 다시 읽을 수 있습니다.

`KMassUnitBareValues.kt`:

```kotlin
package org.pcsoft.framework.kunit.mass

/** 1 킬로그램([KMassUnit.KILOGRAM]). */
val kilograms: KMassUnitInstance = massUnitInstanceOf(KMassUnit.KILOGRAM.baseValue)

/** 1 그램([KMassUnit.GRAM]). */
val grams: KMassUnitInstance = massUnitInstanceOf(KMassUnit.GRAM.baseValue)

/** 1 파운드([KMassUnit.POUND]). */
val pounds: KMassUnitInstance = massUnitInstanceOf(KMassUnit.POUND.baseValue)

/** 1 온스([KMassUnit.OUNCE]). */
val ounces: KMassUnitInstance = massUnitInstanceOf(KMassUnit.OUNCE.baseValue)
```

`KMassUnitExtensions.kt`(질량은 임의의 크기를 받아들이므로 프로퍼티는 공통 기반 `KPrefixBuilder`에 달립니다):

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KPrefixBuilder

private fun prefixedMass(builder: KPrefixBuilder, unit: KMassUnit): KMassUnitInstance =
    massUnitInstanceOf(builder.prefix.factor * unit.baseValue)

/** 접두사가 붙은 킬로그램, 예: `kilo.kilograms`. */
val KPrefixBuilder.kilograms: KMassUnitInstance get() = prefixedMass(this, KMassUnit.KILOGRAM)

/** 접두사가 붙은 그램, 예: `milli.grams` = 1 mg. */
val KPrefixBuilder.grams: KMassUnitInstance get() = prefixedMass(this, KMassUnit.GRAM)

/** 접두사가 붙은 파운드. */
val KPrefixBuilder.pounds: KMassUnitInstance get() = prefixedMass(this, KMassUnit.POUND)

/** 접두사가 붙은 온스. */
val KPrefixBuilder.ounces: KMassUnitInstance get() = prefixedMass(this, KMassUnit.OUNCE)
```

이게 전부입니다 — 이것만으로 완전한 `+`, `-`, `*`, `/`, 비교, SI 접두사 빌더(`5 of milli.grams`),
`toUnit()`/`toMass()` 왕복 변환을 모두 공짜로 얻습니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mass.*

val a = 500 of grams
val b = 2 of pounds
val total = a + b            // KMassUnitInstance, 킬로그램으로 정규화
println(total into kilograms)
println(total into grams)

val heavier = b > a          // true
```

## 4. (선택) 특수/파생 단위 추가하기

특정 스케일링에 묶인 자주 사용되는 명명된 단위(면적의 헥타르 같은)가 그룹에 있다면, 명명된 값 1 인스턴스로
추가합니다 — 별도의 대상 타입은 필요 없습니다:

```kotlin
package org.pcsoft.framework.kunit.mass

/** 1 미터톤(1000 kg). */
val tonnes: KMassUnitInstance = massUnitInstanceOf(1000.0)
```

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mass.*

println((2500 of grams) into tonnes) // 0.0025
```

## 5. 다른 그룹과 결합하기

모든 것이 궁극적으로 범용 `KMixedUnitInstance` 엔진을 통과하므로, 새 그룹은 `*`/`/`를 통해 다른 어떤 그룹과도
즉시 조합됩니다 — 규칙은 [혼합 단위](mixed-units.md)를 참조하세요. 강하게 타입이 지정된 그룹 간 결과
(`mass / volume = density` 같은)를 위해서는 `KSpeedUnitOperators.kt`를 본떠 `K...UnitOperators.kt`에 타입이 지정된
연산자 확장을 추가합니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.mass.*

// 밀도 = 질량 / 부피(범용 KMixedUnitInstance: [KILOGRAM^1, METER^-3])
val density = (5 of kilograms) / (2 of liters)
```

## 6. 명명 및 테스트 체크리스트

- 모든 public 타입은 `K`로 시작합니다(`KMassUnit`, `KMassUnitInstance` 등). 값 1 맨 토큰과 접두사 빌더
  프로퍼티 확장(`kilograms`, `grams` 등)은 예외이며 언어적으로 자연스럽게 유지됩니다.
- 파라미터화된 크로스 매트릭스 테스트 절차로 그룹을 커버합니다. `of`/`into`를 통해 구축하고(원시 enum은 결코
  사용하지 않음): 단위 → 단위 변환, 모든 단위 쌍에 대해 연산자별·비교별 하나의 메서드, 접두사 빌더 매트릭스,
  `of` 타입 보존, `into` 오류 케이스 — `CLAUDE.md`의 "파라미터화된 크로스 매트릭스 테스트 절차" 절을 참조하세요.
- 모든 public 멤버를 영어로, Markdown으로, 유용한 경우 예시와 함께 문서화합니다 — 특히 연산자.
- 그룹이 크기 제한적이면(축소 접두사를 거부하는 저장 용량 같은), 그 단위 프로퍼티를 기반 `KPrefixBuilder`가 아닌
  `KAugmentingPrefixBuilder`/`KDiminishingPrefixBuilder`에 달아, 허용되지 않는 접두사를 **컴파일 오류**로
  만듭니다.
