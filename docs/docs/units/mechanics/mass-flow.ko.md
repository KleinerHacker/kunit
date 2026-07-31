# 질량 유량

패키지: `org.pcsoft.framework.kunit.mechanic.massflow`
기본 단위: **초당 킬로그램**(`KMassFlowUnit.BASE == KMassFlowUnit.KILOGRAMS_PER_SECOND`)

유형: **구성된 단위**

질량 유량 `ṁ` 는 단위 시간당 이동한 질량입니다 — [체적 유량](../kinematics/volume-flow.md)의 질량 대응물입니다. 이는 **구성된** 단위로, 조합 `mass · time⁻¹`
(`kg/s`)입니다.

`KMassFlowUnitInstance` 는 정규 형식으로 정확히 두 항 — 지수 `+1` 의 `KMassUnit.BASE`(그램)와 지수
`-1` 의 `KTimeUnit.BASE`(초) — 을 감싼 `KMixedUnitInstance` 를 감쌉니다. 이 라이브러리의 질량 성분은 그램으로 정규화되므로, 저장 값은 원시 그램 기준 성분 값이며 kg/s 로
읽을 때 고정 인자로 나눕니다.

## 이름이 붙은 단위

| 단위            | 기호   |                 토큰 |           kg/s로 1 |
|-----------------|--------|---------------------:|-------------------:|
| 초당 킬로그램   | `kg/s` | `kilogramsPerSecond` |                1.0 |
| 초당 그램       | `g/s`  |     `gramsPerSecond` |               1e-3 |
| 시간당 킬로그램 | `kg/h` |   `kilogramsPerHour` |             1/3600 |
| 시간당 톤       | `t/h`  |      `tonnesPerHour` | 1000/3600 ≈ 0.2778 |
| 초당 파운드     | `lb/s` |    `poundsPerSecond` |         0.45359237 |
| 시간당 파운드   | `lb/h` |      `poundsPerHour` |       ≈ 1.25998e-4 |

모든 단위는 전체 SI 접두사 범위를 지원합니다 (도징 펌프용 `milli.gramsPerSecond`).

## 분해

질량 유량에는 두 가지 동등한 분해가 있으며, 둘 다 동일한 정규화 팩토리로 합쳐집니다.

| 형식             | Kotlin                                         | 결과 타입               |
|------------------|------------------------------------------------|-------------------------|
| 질량 / 시간      | `mass / time`                                  | `KMassFlowUnitInstance` |
| 밀도 × 체적 유량 | `density * volumeflow`                         | `KMassFlowUnitInstance` |
| 네이티브 표현식  | `(mass.toUnit() / time.toUnit()).toMassFlow()` | `KMassFlowUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.cubicMetersPerSecond
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.massflow.*

val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val viaMassTime = (2000 of kilo.grams) / (1 of seconds)
val viaDensityFlow = water * (2 of cubicMetersPerSecond)

viaMassTime == viaDensityFlow          // true - 둘 다 2000 kg/s
viaMassTime into kilogramsPerSecond    // 2000.0
```

## 핵심 단위로 계산

| 식                                             | 결과 타입                 | 의미                    |
|------------------------------------------------|---------------------------|-------------------------|
| `mass / time`                                  | `KMassFlowUnitInstance`   | `ṁ = m / t`             |
| `massflow * time`, `time * massflow`           | `KMassUnitInstance`       | 이동한 질량 `m = ṁ · t` |
| `mass / massflow`                              | `KTimeUnitInstance`       | 필요한 시간 `t = m / ṁ` |
| `density * volumeflow`, `volumeflow * density` | `KMassFlowUnitInstance`   | `ṁ = ρ · Q`             |
| `massflow / density`                           | `KVolumeFlowUnitInstance` | `Q = ṁ / ρ`             |
| `massflow / volumeflow`                        | `KDensityUnitInstance`    | `ρ = ṁ / Q`             |

## 실전 예제: 펌프 처리량

펌프가 물 (ρ = 998 kg/m³)을 15 m³/h 로 이동시킵니다. t/h 단위의 질량 유량은 얼마이며, 8시간 동안 얼마나 많은 질량이 통과합니까?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.volumeflow.cubicMetersPerHour
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.massflow.*

val water = (998 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val flow = water * (15 of cubicMetersPerHour)
flow into tonnesPerHour                 // ≈ 14.97

val perShift = flow * (8 of hours)      // KMassUnitInstance
perShift into kilo.grams                // ≈ 119760.0
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.massflow.*

val sum = (10 of kilogramsPerSecond) + (4 of kilogramsPerSecond) // 14 kg/s
(1 of kilogramsPerSecond) > (1 of tonnesPerHour)                 // true
(3.6 of tonnesPerHour) == (1 of kilogramsPerSecond)              // true
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.massflow.*

(2 of kilogramsPerSecond).toString()                     // "2.0 kg/s"(기본 단위)
"${(2 of kilogramsPerSecond) into tonnesPerHour} t/h"    // "7.2 t/h"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학        | Kotlin                          | 의미                            |
|-------------|---------------------------------|---------------------------------|
| `kg/s`      | `kilogramsPerSecond`            | 질량 유량, 기본 단위(명명 토큰) |
| `kg·s⁻¹`    | `kilo.grams * (seconds pow -1)` | 같은 양을 순수 곱으로 표현      |
| `t/h`       | `tonnesPerHour`                 | 산업용 처리량 읽기              |
| `ṁ = m / t` | `mass / time`                   | 분해 A                          |
| `ṁ = ρ · Q` | `density * volumeflow`          | 분해 B                          |
| `Q = ṁ / ρ` | `massflow / density`            | 체적 유량에 대해 푼 형식        |
| `mg/s`      | `milli.gramsPerSecond`          | 접두사가 붙은 질량 유량         |
