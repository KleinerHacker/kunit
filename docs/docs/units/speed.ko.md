# 속도

패키지: `org.pcsoft.framework.kunit.speed`
기본 단위: **초당 미터** (`KSpeedUnit.BASE == KSpeedUnit.METERS_PER_SECOND`)

속도는 최초의 **구성된(constructed)** 단위입니다. 길이나 시간과 달리 단일한 "실제" 물리량이 아니라
`길이 · 시간⁻¹` (`m/s`)의 조합입니다. 따라서 `KSpeedUnitInstance`는 정확히 두 개의 항 - 지수 `+1`의
`KDistanceUnit.BASE`(미터)와 지수 `-1`의 `KTimeUnit.BASE`(초) - 으로 이루어진 `KMixedUnitInstance`를
래핑합니다. 값은 어떤 단위, 접두어, 또는 길이/시간 조합으로 생성되었든 항상 초당 미터로 정규화되어
저장됩니다.

## 단위

| 단위 | Enum 값 | 기호 | 생성자 | 1 단위 (m/s) |
|---|---|---|---:|---:|
| 초당 미터 | `KSpeedUnit.METERS_PER_SECOND` | `m/s` | `Number.metersPerSecond` | 1.0 |
| 시속 킬로미터 | `KSpeedUnit.KILOMETERS_PER_HOUR` | `km/h` | `Number.kilometersPerHour` | 0.277778 (1000/3600) |
| 시속 마일 | `KSpeedUnit.MILES_PER_HOUR` | `mph` | `Number.milesPerHour` | 0.44704 (1609.344/3600) |
| 노트 | `KSpeedUnit.KNOT` | `kn` | `Number.knots` | 0.514444 (1852/3600) |
| 초당 피트 | `KSpeedUnit.FEET_PER_SECOND` | `ft/s` | `Number.feetPerSecond` | 0.3048 |
| 마하 (ISA 해수면) | `KSpeedUnit.MACH` | `Ma` | `Number.mach` | 340.29 |
| 광속 | `KSpeedUnit.LIGHT_SPEED` | `c` | `Number.speedOfLight` | 299792458.0 |

위의 모든 단위는 `valueAs`/`toString` 대상이나 접두어 infix 함수의 `unit` 인자로 사용할 수 있는 bare
`val` 별칭을 가지고 있습니다: `metersPerSecond`, `kilometersPerHour`, `milesPerHour`, `knots`,
`feetPerSecond`, `mach`, `speedOfLight`.

> **마하**는 해수면 국제 표준 대기(15 °C)에서의 음속입니다. 물리 상수가 아니라 편리한 기준점이며, 실제
> 음속은 온도와 고도에 따라 달라집니다.

```kotlin
import org.pcsoft.framework.kunit.speed.*

val v = 50.kilometersPerHour
v.value                                    // 13.888... (m/s로 정규화됨)
v.valueAs(KSpeedUnit.KILOMETERS_PER_HOUR)  // 50.0 (km/h로 다시 읽기)
v.valueAs(milesPerHour)                     // ≈ 31.07
v.valueAs(knots)                            // ≈ 26.998
v.valueAs(mach)                             // ≈ 0.0408 (음속의 비율)
```

## 핵심 단위(길이 & 시간)로 계산하기

이것이 구성된 단위의 핵심이며 직관적이지 **않은** 부분입니다 - 이 절을 주의 깊게 읽으세요.

**개념 모델:** 속도는 곧 길이를 시간으로 나눈 것입니다. KUnit은 세 가지 물리량 - 길이, 시간, 속도 -
사이를 평범한 `*`와 `/`로 오갈 수 있게 해주며, 각 결과는 **강타입**입니다. 원시 `KMixedUnitInstance`를
직접 만들거나 풀어낼 필요가 전혀 없습니다.

네 가지 유효한 조합과 그 결과 타입:

| 식 | 결과 타입 | 의미 |
|---|---|---|
| `length / time` | `KSpeedUnitInstance` | 속도 = 거리 / 시간 |
| `speed * time` | `KLengthUnitInstance` | 거리 = 속도 × 시간 |
| `time * speed` | `KLengthUnitInstance` | 거리 (교환법칙) |
| `length / speed` | `KTimeUnitInstance` | 시간 = 거리 / 속도 |

```kotlin
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.speed.*

// --- 핵심 단위 -> 속도 ---------------------------------------------------
val v = 100.meters / 10.seconds          // KSpeedUnitInstance (.toSpeed() 불필요!)
v.value                                     // 10.0 (m/s)
v.valueAs(KSpeedUnit.KILOMETERS_PER_HOUR)   // 36.0
v.valueAs(KSpeedUnit.MILES_PER_HOUR)        // ≈ 22.37
v.valueAs(KSpeedUnit.KNOT)                  // ≈ 19.44
v.valueAs(KSpeedUnit.MACH)                  // ≈ 0.0294
v.valueAs(KSpeedUnit.LIGHT_SPEED)           // ≈ 3.336e-8

// 대입 대상 타입은 아무것도 변환하지 않습니다 - 연산자가 이미
// KSpeedUnitInstance를 반환합니다. Kotlin에는 암시적 변환이 없습니다.
val explicit: KSpeedUnitInstance = 100.meters / 10.seconds

// --- 속도 -> 길이 (시간을 곱함) -----------------------------------------
val distance = v * 60.seconds             // KLengthUnitInstance
distance.value                              // 600.0 (m)
distance.valueAs(KDistanceUnit.METER)         // 600.0
distance.valueAs(feet)                      // ≈ 1968.5 (임의의 길이 단위로 다시 읽기)
distance.valueAs(miles)                     // ≈ 0.373
60.seconds * v                            // 동일한 결과 (교환법칙)

// --- 속도 -> 시간 (길이를 나눔) -----------------------------------------
val time = 600.meters / v                 // KTimeUnitInstance
time.value                                  // 60.0 (s)
time.valueAs(KTimeUnit.MINUTE)              // 1.0
time.valueAs(KTimeUnit.HOUR)                // ≈ 0.0167
```

!!! warning "**순수** 길이만 속도로 나눠집니다"
    `length / time`과 `length / speed`는 길이가 지수 1이어야 합니다. **넓이**(`m²`, 예: `2.hectares`)나
    **부피**(`m³`)는 길이가 아니므로 `area / time`은 속도가 아닌 `m²/s`가 됩니다 - 연산자는 잘못된 값을
    조용히 반환하는 대신 `IllegalStateException`을 던집니다. 마찬가지로 `length * time`(`m·s`, 속도
    아님)과 `length + speed`(차원 불일치)도 유효한 속도 구성이 아닙니다.

### 속도가 아닌 중간 결과(예: m²/s)를 의도적으로 계산하기

Kotlin 연산자는 컴파일 타임에 단일 반환 타입을 가지므로, `KLengthUnitInstance / KTimeUnitInstance`는
타입이 지정된 속도를 만드는 데 **예약**되어 있으며 대신 `m²/s`를 만들 수는 없습니다. 하지만 그 중간
결과가 **사라지는 것은 아닙니다** - 한 피연산자를 `toUnit()`로 혼합 레벨로 낮추면 범용
`KMixedUnitInstance`의 `/` 연산자(임의의 지수, 속도 검사 없음)가 선택됩니다. 이 명시적인
`toUnit()`가 바로 강타입 경로를 벗어난다는 의도된 신호입니다.

```kotlin
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

val area = 2.hectares                 // KLengthUnitInstance, 지수 2 (20 000 m²)

// area / 2.seconds                   // ❌ IllegalStateException 발생 (m²/s일 뿐 속도 아님)

// ✅ 의도적인 m²/s 중간 결과: 한 피연산자를 혼합 레벨로
val areaPerTime = area.toUnit() / 2.seconds.toUnit()
areaPerTime.value                       // 10000.0
areaPerTime.units                       // [METER^2, SECOND^-1]

// ...그리고 임의의 KMixedUnitInstance처럼 계속 연결됩니다
val backToArea = areaPerTime * 4.seconds.toUnit() // units=[METER^2], value=40000.0
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.speed.*

// + / - : 같은 그룹, 서로 다른 속도 단위 간 자동 변환
val a = 36.kilometersPerHour + 10.metersPerSecond  // KSpeedUnitInstance, 20 m/s
val b = 20.metersPerSecond - 36.kilometersPerHour  // 10 m/s

// 비교 (정규화된 m/s 값 기준)
50.kilometersPerHour > 10.metersPerSecond   // true  (13.89 m/s > 10 m/s)
36.kilometersPerHour == 10.metersPerSecond  // true  (같은 정규화 값)

// 두 속도 간 * / / 는 KMixedUnitInstance로 "빠져나갑니다"(더 이상 순수 속도가 아님)
val squared = 10.metersPerSecond * 2.metersPerSecond // KMixedUnitInstance, units=[m^2, s^-2]
```

## 비교와 동등성

`==`, `!=`, `<`, `<=`, `>`, `>=`는 두 `KSpeedUnitInstance`의 정규화된 `value`(초당 미터)를 비교합니다.
속도는 항상 같은 차원을 가지므로 지수 검사가 필요하지 않습니다(넓이와 길이를 비교할 수 없는 길이와
달리).

## SI 접두어

모든 `KSpeedUnit`은 24개의 SI 접두어(`KUnitPrefix`, Quetta/Q ~ Quecto/q)와 조합할 수 있습니다.
속도 그룹의 `infix` 생성 함수(직접 `KSpeedUnitInstance`를 반환)와 `with`(`valueAs`/`toString` 대상용)를
사용합니다:

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.speed.*

// 생성: "5 kilo metersPerSecond" -> KSpeedUnitInstance (직접, == 5000.metersPerSecond)
val fast = 5 kilo metersPerSecond
fast.value // 5000.0

// 접두어 대상으로 값 다시 읽기
val v = 5.metersPerSecond
v.valueAs(KUnitPrefix.KILO with KSpeedUnit.METERS_PER_SECOND)  // 0.005
```

속도를 명시적인 **길이/시간 쌍**(두 개의 대상)으로 다시 읽을 수도 있습니다. 이것이 "km/h"를 길이와
시간 부분으로 표현하는 방식입니다:

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.speed.*

val v = 10.metersPerSecond
v.valueAs(KUnitPrefix.KILO with KDistanceUnit.METER, KTimeUnit.HOUR)   // 36.0 (km per h)
v.toString(KUnitPrefix.KILO with KDistanceUnit.METER, KTimeUnit.HOUR)  // "36.0 km*h^-1"
```

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.speed.*

10.metersPerSecond.toString()                            // "10.0 m/s" (기본 단위)
(100.meters / 10.seconds).toString(KSpeedUnit.KILOMETERS_PER_HOUR) // "36.0 km/h"
1.mach.toString(KSpeedUnit.MACH)                          // "1.0 Ma"
```
