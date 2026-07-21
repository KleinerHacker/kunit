# 절대 온도

> **온도** 주제의 일부입니다 — [개요](temperature-overview.md) 및 선형 대응물
> [온도 차](temperature-difference.md)를 참조하세요.

패키지: `org.pcsoft.framework.kunit.temperature`
기본 단위: **켈빈**(`KTemperatureUnit.BASE == KTemperatureUnit.KELVIN`)

온도 그룹은 열역학적 온도를 모델링합니다. 이는 프레임워크의 **첫 번째(그리고 설계상 영구적인) 아핀 예외**입니다:
다른 모든 그룹과 달리 온도 단위 간 변환은 단일 곱셈 계수가 아니라 **오프셋과 스케일**(아핀) 변환입니다 —
`25 °C`는 `25 × 1 °C`가 **아닙니다**. 값은 **절대 켈빈**으로 정규화되어 저장되므로 `*`/`/`/`pow`는 일반 엔진을
그대로 통과합니다.

이 그룹을 특별하게 만드는 두 가지:

* **오버로드가 아닌 훅을 통한 아핀 변환.** 공유 엔진은 순수 곱셈으로 유지됩니다. 아핀 변환은 두 개의 measurable
  훅 `scaledBy`(생성, `of` 뒤)와 `readBaseValue`(읽기, `into` 뒤)를 통해 주입되므로 `25 of celsius`와
  `t into fahrenheit`는 일반 동사로 동작합니다 — 그룹별 `of`/`into` 오버로드(명시적으로 임포트한 일반 동사가
  가려버림)가 필요 없습니다.
* **접두사 없음.** 온도 그룹은 의도적으로 접두사 빌더를 **제공하지 않습니다**(`milli.celsius`는 모델링하지 않음).
  `KTemperatureUnitExtensions.kt`는 없습니다.

## 단위

| 단위 | Enum 값 | 기호 | 토큰 | 켈빈 변환 |
|---|---|---|---:|---|
| 켈빈 | `KTemperatureUnit.KELVIN` | `K` | `kelvin` | 항등 |
| 섭씨 | `KTemperatureUnit.CELSIUS` | `°C` | `celsius` | `K = °C + 273.15` |
| 화씨 | `KTemperatureUnit.FAHRENHEIT` | `°F` | `fahrenheit` | `K = (°F − 32)·5/9 + 273.15` |
| 랭킨 | `KTemperatureUnit.RANKINE` | `°R` | `rankine` | `K = °R·5/9` |

각 `토큰`은 값 1의 `KTemperatureUnitInstance`이며 `of`(생성)와 `into`(읽기)에 사용됩니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.temperature.*

val t = 25 of celsius
t.value             // 298.15(절대 켈빈으로 정규화)
t into fahrenheit   // 77.0
t into kelvin       // 298.15

(0 of celsius) into kelvin       // 273.15
(100 of celsius) into fahrenheit // 212.0
(32 of fahrenheit) into celsius  // 0.0
(-40 of celsius) into fahrenheit // -40.0(섭씨/화씨 교차점)
```

## 연산자

절대 온도는 아핀 **점**이며 벡터가 아닙니다. 따라서 연산은 의도적으로 비대칭이며, 이것이 물리적으로 올바른
동작입니다([온도 차](temperature-difference.md) 참조):

* `AbsTemp − AbsTemp` → **`KTemperatureDifferenceUnitInstance`**(둘 사이의 켈빈 *구간*. 예: `30 °C − 10 °C = 20 ΔK`,
  `20 °C`가 **아님**).
* `AbsTemp ± 차이` → 다시 절대 온도.
* `AbsTemp + AbsTemp` → **컴파일 오류**(두 절대 온도의 덧셈은 물리적으로 무의미).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

// 절대 − 절대 = 온도 차(켈빈)
val d = (30 of celsius) - (10 of celsius)          // KTemperatureDifferenceUnitInstance: 20 ΔK
d.value                                             // 20.0

// 절대 ± 차이 = 절대 온도
val a = (25 of celsius) + KTemperatureDifference.ofKelvin(5)   // KTemperatureUnitInstance: 303.15 K
val b = (25 of celsius) - KTemperatureDifference.ofKelvin(5)   // KTemperatureUnitInstance: 293.15 K

// (30 of celsius) + (10 of celsius)               // 컴파일되지 않음

// 비교(절대 켈빈 기준)
(0 of celsius) == (273.15 of kelvin)      // true(동일한 절대 온도)
(100 of celsius) > (100 of fahrenheit)    // true
```

### 비교와 동등성

`==`, `!=`, `<`, `<=`, `>`, `>=`는 정규화된 절대 켈빈 `value`를 비교합니다. `equals`는 생성 단위와 무관하게
절대 온도로 비교하므로 `(0 of celsius) == (273.15 of kelvin)`입니다.

## `pow`를 이용한 거듭제곱

중위 `pow` 연산자로 정수 거듭제곱을 계산합니다. 온도 그룹에서 `pow`는 일반 `KMixedUnitInstance`를 반환하며(온도에는
차원이 있는 거듭제곱 타입이 없음), 절대 켈빈 항에 선형으로 작용합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.temperature.*

val squared = (2 of kelvin) pow 2   // KMixedUnitInstance: 4.0 K²
```

## 다른 단위와의 혼합

온도를 다른 그룹과 곱하거나 나누면 일반 `KMixedUnitInstance`가 됩니다(표준화된 온도 조합은 없음). 계산은 절대
켈빈 값에 대해 수행됩니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*
import org.pcsoft.framework.kunit.time.seconds

val rate = (2 of kelvin) / (1 of seconds)   // KMixedUnitInstance: 2.0 K·s⁻¹
```

## toString 포매팅

기본 단위 `toString()`만 존재합니다. 특정 단위로 포매팅하려면 `into`를 사용하세요:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.temperature.*

(25 of celsius).toString()               // "298.15 K"(기본 단위 표현)
"${(25 of celsius) into fahrenheit} °F"  // "77.0 °F"
```
