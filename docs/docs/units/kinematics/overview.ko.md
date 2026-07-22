# 운동학 — 개요

패키지: `org.pcsoft.framework.kunit.distance`, `…time`, `…speed`, `…acceleration`, `…frequency`

운동학은 **운동**에 대한 기술입니다 — 얼마나 멀리, 얼마나 오래, 얼마나 빠르게, 그리고 운동의 비율
자체가 어떻게 변하는지 — 아직 그 배후의 힘은 묻지 않습니다(그것은 [역학](../mechanics/overview.md)
주제입니다). KUnit은 이 분야를 2개의 **네이티브** 기본량과, 그것들로부터 **구성된** 3개의 양으로
모델링하므로, 고전적인 운동 공식이 강한 타입을 유지한 채 일반적인 `*`와 `/` 식이 됩니다.

## 이 주제의 단위

| 단위 | 유형 | 기준 단위 | 페이지 |
|---|---|---|---|
| 거리 | 네이티브 | 미터(`m`) | [거리](distance.md) |
| 시간 | 네이티브 | 초(`s`) | [시간](time.md) |
| 주파수 | 네이티브 | 헤르츠(`Hz`) | [주파수](frequency.md) |
| 속도 | 구성 | 미터 매 초(`m/s`) | [속도](speed.md) |
| 가속도 | 구성 | 미터 매 초²(`m/s²`) | [가속도](acceleration.md) |

## 양들의 관계

속도는 거리÷시간, 가속도는 속도÷시간, 주파수는 시간의 역수입니다. KUnit은 각 조합에 대해 올바른
**타입이 지정된** 양을 반환합니다 — 원시 혼합 단위를 직접 조립할 필요가 없습니다.

| 식 | 결과 | 공식 |
|---|---|---|
| `distance / time` | 속도 | `v = s / t` |
| `speed * time` | 거리 | `s = v · t` |
| `speed / time` | 가속도 | `a = Δv / t` |
| `acceleration * time` | 속도 | `v = a · t` |
| `distance * frequency` | 속도 | `v = s · f` |

## 실전 예제 — 주행의 평균 속도

자동차가 **120 km**를 **1.5 h** 만에 주행합니다. 평균 속도는 `v = s / t`이며, 그 속도에 소요 시간을
곱하면 다시 주행 거리가 나옵니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.speed.*

val v = (120 of kilo.meters) / (1.5 of hours)   // KSpeedUnitInstance
v into (kilo.meters / hours)                     // 80.0 (km/h)
v.value                                          // ≈ 22.22 (m/s)

val distance = v * (3 of hours)                  // KLengthUnitInstance
distance into kilo.meters                        // 240.0 (3 h 동안의 km)
```

## 실전 예제 — 단거리 주자의 가속도

단거리 주자가 정지 상태에서 **2 s** 만에 **10 m/s**에 도달합니다. 가속도는 `a = Δv / t`입니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*
import org.pcsoft.framework.kunit.acceleration.*

val a = ((10 of meters) / (1 of seconds)) / (2 of seconds) // KAccelerationUnitInstance, 5 m/s²
val reached = a * (2 of seconds)                            // KSpeedUnitInstance, 10 m/s
reached.value                                               // 10.0
a into standardGravities                                    // ≈ 0.51 (g에 대한 비율)
```

## 값 출력(`toString`)

`toString()`은 값을 해당 그룹의 **기준 단위**(값 + 기호)로 출력합니다. 다른 단위는 문자열 템플릿 안에서
`into`로 읽고 기호를 직접 붙이세요:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.speed.*

val v = (10 of meters) / (2 of seconds)   // KSpeedUnitInstance
v.toString()                              // "5.0 m/s" (기준 단위)
"${v into (kilo.meters / hours)} km/h"    // "18.0 km/h"
```

## 표기법

아래 표는 이 분야의 핵심 관계를 수학 표기와 KUnit의 Kotlin 표기로 대비합니다. 지수는 유니코드 위 첨자
(`²`, `⁻¹`), `·`는 곱셈, `/`는 분수를 나타냅니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `v = s / t` | `(120 of kilo.meters) / (1.5 of hours)` | 거리÷시간에서 속도 |
| `s = v · t` | `v * (3 of hours)` | 속도×시간에서 거리 |
| `a = Δv / t` | `((10 of meters) / (1 of seconds)) / (2 of seconds)` | 속도÷시간에서 가속도 |
| `v = a · t` | `a * (2 of seconds)` | 가속도×시간에서 속도 |
| `f = 1 / T` | `1 / (2 of hertz)` | 주기↔주파수(시간의 역수) |

## 다음에 볼 것

* [거리](distance.md) — 길이, 면적, 부피를 한 그룹에.
* [시간](time.md) — `Duration`에 기반한 지속 시간.
* [속도](speed.md) 및 [가속도](acceleration.md) — 구성된 운동 비율.
* [주파수](frequency.md) — 시간의 역수와 그 상호 연산자.
