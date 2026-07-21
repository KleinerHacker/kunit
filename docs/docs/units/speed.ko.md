# 속도

패키지: `org.pcsoft.framework.kunit.speed`
기본 단위: **초당 미터** (`KSpeedUnit.BASE == KSpeedUnit.METERS_PER_SECOND`)

속도는 첫 번째 **구성된** 단위입니다: 길이나 시간과 달리 단일한 "실제" 양이 아니라 `length · time⁻¹`(`m/s`)
이라는 합성입니다. 따라서 `KSpeedUnitInstance`는 정확히 두 개의 항 — 지수 `+1`의 `KDistanceUnit.BASE`(미터)와
지수 `-1`의 `KTimeUnit.BASE`(초) — 로 이루어진 `KMixedUnitInstance`를 감쌉니다. 값은 어떤 단위나 길이/시간
조합으로 생성되었든 관계없이 항상 초당 미터로 정규화되어 저장됩니다.

## 속도 만들기

속도는 **길이/시간 표현식**으로 만듭니다. 예: `10 of kilo.meters / hours` 또는
`100 of meters / (10 of seconds)` — 둘 다 `KSpeedUnitInstance`를 생성합니다. 임의의 길이/시간 템플릿으로 다시
읽습니다(`v into (kilo.meters / hours)`). `metersPerSecond`나 `kilometersPerHour` 같은 철자로 쓴 복합 토큰은
의도적으로 **없습니다**(그것들은 정확히 `meters / seconds` / `kilo.meters / hours` 입니다).

진정으로 단일하고 관습적인 이름을 가진 속도만 값 1 토큰으로 남습니다(`of`/`into`에 사용):

| 속도 | 기호 | 토큰 | 1 단위 (m/s) |
|---|---|---:|---:|
| 노트 | `kn` | `knots` | 0.514444 (1852/3600) |
| 마하 (ISA 해수면) | `Ma` | `mach` | 340.29 |
| 광속 | `c` | `speedOfLight` | 299792458.0 |

> **마하**는 해수면(15 °C)의 국제 표준 대기에서의 음속입니다. 이는 편리한 기준점이며 물리 상수가 아닙니다 —
> 실제 음속은 온도와 고도에 따라 달라집니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.miles
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.speed.*

val v = 50 of kilo.meters / hours
v.value                        // 13.888...(m/s로 정규화)
v into (kilo.meters / hours)   // 50.0(km/h로 다시 읽기)
v into (miles / hours)         // ≈ 31.07
v into knots                   // ≈ 26.998
v into mach                    // ≈ 0.0408(음속에 대한 비율)
```

## 핵심 단위(길이 & 시간)로 계산하기

이것이 구성된 단위의 핵심입니다. 속도*란* 길이를 시간으로 나눈 것입니다. KUnit에서는 세 가지 양 — 길이, 시간,
속도 — 사이를 단순한 `*`와 `/`로 오갈 수 있으며, 각 결과는 **강하게 타입이 지정**됩니다. 원시
`KMixedUnitInstance`를 직접 만들거나 풀어낼 필요가 전혀 없습니다.

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `length / time` | `KSpeedUnitInstance` | 속도 = 거리 / 시간 |
| `speed * time` | `KLengthUnitInstance` | 거리 = 속도 × 시간 |
| `time * speed` | `KLengthUnitInstance` | 거리(교환 법칙) |
| `length / speed` | `KTimeUnitInstance` | 시간 = 거리 / 속도 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.speed.*

// --- 핵심 단위 -> 속도 ------------------------------------------------
val v = (100 of meters) / (10 of seconds)  // KSpeedUnitInstance (.toSpeed() 불필요!)
v.value                    // 10.0(m/s)
v into (kilo.meters / hours) // 36.0
v into (miles / hours)     // ≈ 22.37
v into knots               // ≈ 19.44

// 간결한 비율을 위해 길이에 접두사 붙이기(괄호 없이, `of`가 `/`보다 약하게 결합):
val fast = 10 of kilo.meters / hours   // KSpeedUnitInstance

// --- 속도 -> 길이(시간을 곱함) -------------------------------
val distance = v * (60 of seconds)     // KLengthUnitInstance
distance into meters       // 600.0
distance into feet         // ≈ 1968.5
(60 of seconds) * v        // 같은 결과(교환 법칙)

// --- 속도 -> 시간(길이를 그것으로 나눔) ------------------------------
val time = (600 of meters) / v         // KTimeUnitInstance
time into minutes          // 1.0
```

!!! warning "*순수한* 길이만 속도로 나눠집니다"
    `length / time`과 `length / speed`는 길이가 지수 1일 것을 요구합니다. **면적**(`m²`)이나 **부피**(`m³`)는
    길이가 아니므로 `area / time`은 속도가 아니라 `m²/s`가 됩니다 — 연산자는 잘못된 값을 조용히 반환하는 대신
    `IllegalStateException`을 던집니다. 그런 중간 결과를 의도적으로 만들려면 `toUnit()`으로 한 피연산자를 혼합
    수준으로 내립니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

val area = (2 of meters) * (2 of meters)         // KAreaUnitInstance
val areaPerTime = area.toUnit() / (2 of seconds).toUnit() // KMixedUnitInstance, [METER^2, SECOND^-1]
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*

// + / - : 같은 그룹, 서로 다른 속도 표현식 간 자동 변환
val a = (36 of kilo.meters / hours) + (10 of meters / seconds)  // KSpeedUnitInstance, 20 m/s
val b = (20 of meters / seconds) - (36 of kilo.meters / hours)  // 10 m/s

// 비교(정규화된 m/s 값 기준)
(50 of kilo.meters / hours) > (10 of meters / seconds)   // true
(36 of kilo.meters / hours) == (10 of meters / seconds)  // true

// 두 속도 간의 * / / 는 KMixedUnitInstance로 탈출(더 이상 순수한 속도가 아님)
val squared = (10 of meters / seconds) * (2 of meters / seconds) // KMixedUnitInstance, [m^2, s^-2]
```

## toString 형식화

기본 단위의 `toString()`만 존재합니다. 특정 단위는 `into`로 형식화합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*

(10 of meters / seconds).toString()   // "10.0 m/s"(기본 단위)
"${(10 of meters / seconds) into (kilo.meters / hours)} km/h" // "36.0 km/h"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자(`²`, `³`, `⁻¹`)로 표기하며, `·`는 곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `m/s` | `meters / seconds` | 속도, 기본 단위(초당 미터) — 분수 형식 |
| `m·s⁻¹` | `meters * (seconds pow -1)` | 같은 속도를 음의 지수 곱으로 표현 |
| `km/h` | `kilo.meters / hours` | 시속 킬로미터 |
| `mi/h` | `miles / hours` | 시속 마일 |
| `100 m / 10 s` | `(100 of meters) / (10 of seconds)` | 길이 ÷ 시간으로 생성 |
