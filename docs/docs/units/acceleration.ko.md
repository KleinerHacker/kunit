# 가속도

패키지: `org.pcsoft.framework.kunit.acceleration`
기본 단위: **미터 매 초 제곱**(`KAccelerationUnit.BASE == KAccelerationUnit.METERS_PER_SECOND_SQUARED`)

가속도는 **구성된** 단위로, 조합 `length · time⁻²`(`m/s²`)입니다. `KAccelerationUnitInstance` 는 정확히 두
항 — 지수 `+1` 의 `KDistanceUnit.BASE`(미터)와 지수 `-2` 의 `KTimeUnit.BASE`(초) — 으로 이루어진
`KMixedUnitInstance` 를 감쌉니다. 값은 항상 m/s² 로 정규화됩니다. 기본 단위가 성분 기본 단위(미터, 초)와
일치하므로 추가 배율 인자가 없습니다.

## 가속도 만들기

가속도는 보통 `speed / time` 으로, 또는 이름 있는 토큰으로 만듭니다. `metersPerSecondSquared` 토큰은 의도적으로
**없습니다**(그것은 곧 `meters / (seconds pow 2)`). 진정으로 이름이 있는 단위만 값 1 토큰으로 남습니다
(`of`/`into` 와 함께 사용):

| 가속도 | 기호 | 토큰 | 1 단위의 m/s² 환산 |
|---|---|---:|---:|
| 갈(갈릴레오) | `Gal` | `gals` | 0.01(1 cm/s²) |
| 표준 중력 | `g₀` | `standardGravities` | 9.80665 |

두 토큰 모두 전체 SI 접두사를 지원합니다(예: `milli.gals` = 1 mGal, 중력 측정의 일상 단위).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.acceleration.*

val a = 5 of gals               // KAccelerationUnitInstance
a.value                         // 0.05(m/s² 로 정규화)
a into standardGravities        // ≈ 0.0051
(1 of milli.gals).value         // 0.00001(1 mGal)
```

## 핵심 단위(속도와 시간)로 계산

| 식 | 결과 타입 | 의미 |
|---|---|---|
| `speed / time` | `KAccelerationUnitInstance` | 가속도 = Δ속도 / 시간 |
| `acceleration * time` | `KSpeedUnitInstance` | 속도 = 가속도 × 시간 |
| `time * acceleration` | `KSpeedUnitInstance` | 속도(교환 가능) |
| `speed / acceleration` | `KTimeUnitInstance` | 시간 = 속도 / 가속도 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*
import org.pcsoft.framework.kunit.acceleration.*

val a = ((100 of meters) / (10 of seconds)) / (5 of seconds) // KAccelerationUnitInstance, 2 m/s²
val v = a * (3 of seconds)      // KSpeedUnitInstance, 6 m/s
val t = ((100 of meters) / (10 of seconds)) / a             // KTimeUnitInstance
t into seconds                  // 5.0
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.acceleration.*

// + / - : 같은 그룹, 서로 다른 가속도 식 간 자동 변환
val s = (10 of gals) + (4 of gals)   // 0.14 m/s²
(10 of gals) > (4 of gals)           // true
// 두 가속도 간 * / / 는 KMixedUnitInstance 로 "탈출"합니다
(10 of gals) * (2 of gals)           // KMixedUnitInstance
```

## toString 서식

기본 단위 `toString()` 만 존재합니다. 특정 단위는 `into` 로 서식화합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.acceleration.*

(1 of gals).toString()               // "0.01 m/s²"(기본 단위)
"${(1 of standardGravities) into gals} Gal" // "980.665 Gal"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자(`²`, `³`, `⁻¹`)로 표기하며, `·`는 곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `m/s²` | `meters / (seconds pow 2)` | 가속도, 기본 단위(초당 초당 미터) — 분수 형식 |
| `m·s⁻²` | `meters * (seconds pow -2)` | 같은 가속도를 음의 지수 곱으로 표현 |
| `Gal` | `gals` | 명명된 단위(1 cm/s²) |
| `Δv / t` | `speed / time` | 속도 ÷ 시간으로 생성 |
