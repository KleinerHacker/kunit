# 온도 차

패키지: `org.pcsoft.framework.kunit.temperature`
기본 단위: **켈빈**(`KTemperatureDifferenceUnit.BASE == KTemperatureDifferenceUnit.KELVIN`)

온도 *차*는 두 온도 사이의 구간으로, 아핀이며 절대적인 [온도](temperature.md) 그룹과 달리 **선형** 양입니다.
오프셋을 **가지지 않으며**(켈빈의 스케일만) 따라서 일반적인 단위 그룹처럼 동작하고 범용 엔진을 그대로 통과합니다.

물리적으로 이것이 두 절대 온도를 빼면 켈빈이 되는 이유입니다: `30 °C − 10 °C = 20 ΔK`이며 `20 °C`가 아닙니다.
`20 ΔK`의 차이는 수치적으로 `20 °C`의 차이와 같으므로(스텝 크기가 동일), 이 그룹은 의도적으로 **켈빈만** 제공하고
**접두사는 없습니다**.

## 단위

| 단위 | Enum 값 | 기호 | 켈빈 변환 |
|---|---|---|---|
| 켈빈 | `KTemperatureDifferenceUnit.KELVIN` | `ΔK` | 항등 |

!!! note "기호는 `K`가 아니라 `ΔK`"
    온도 차는 기호 **`ΔK`**(예: `"20.0 ΔK"`)로 표시되어 절대 켈빈(`K`)과 의도적으로 구별됩니다. 둘은 같은
    *차원*(켈빈)이지만 서로 다른 양입니다 — 아핀 점 대 선형 구간. 따라서 [혼합 단위](../mixed-units.md)에서
    `m·K`(절대)와 `m·ΔK`(차이)는 **같은 단위가 아니며** 동등하지도 더할 수도 없습니다. 구별된 기호가 이를
    한눈에 보여줍니다.

## 생성

차이는 범용 `of` 동사(절대 양 전용)로 만들지 않습니다. **두 절대 온도의 뺄셈** 또는
`KTemperatureDifference.ofKelvin(…)` 팩토리를 통한 **명시적** 생성으로 얻어집니다 — 이는 "이것은 구간이다"라는
의도를 명확히 합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

val d1 = (30 of celsius) - (10 of celsius)   // KTemperatureDifferenceUnitInstance: 20 ΔK
val d2 = KTemperatureDifference.ofKelvin(20) // 명시적, d1과 동일
d1.value                                      // 20.0(켈빈)
```

## 연산자

`+`/`-`/비교는 일반적인 선형 동형 연산자입니다(차이 더하기 차이는 차이):

```kotlin
import org.pcsoft.framework.kunit.temperature.*

val sum  = KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10) // 30 ΔK
val diff = KTemperatureDifference.ofKelvin(20) - KTemperatureDifference.ofKelvin(10) // 10 ΔK

KTemperatureDifference.ofKelvin(20) > KTemperatureDifference.ofKelvin(10) // true
```

차이는 절대 온도에 더하거나 빼서 다시 절대 온도를 얻을 수 있습니다([온도](temperature.md) 참조):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

(25 of celsius) + KTemperatureDifference.ofKelvin(5) // KTemperatureUnitInstance: 303.15 K
```

## 다른 단위와의 혼합

차이를 다른 그룹과 곱하거나 나누면 범용 `KMixedUnitInstance`가 됩니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.times
import org.pcsoft.framework.kunit.temperature.*

KTemperatureDifference.ofKelvin(2) * (3 of bytes) // KMixedUnitInstance
```

## toString 서식

기본 단위 `toString()`만 존재합니다(켈빈):

```kotlin
import org.pcsoft.framework.kunit.temperature.*

KTemperatureDifference.ofKelvin(20).toString() // "20.0 ΔK"
```
