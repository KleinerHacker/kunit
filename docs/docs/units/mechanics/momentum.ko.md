# 운동량

패키지: `org.pcsoft.framework.kunit.mechanic.momentum`
기본 단위: **초당 킬로그램 미터**
(`KMomentumUnit.BASE == KMomentumUnit.KILOGRAM_METERS_PER_SECOND`)

유형: **구성된 단위**

운동량 `p = m · v` 는 물체의 "운동량"입니다. 이는 **구성된** 단위로, 조합
`mass · length · time⁻¹`(`kg·m/s`)입니다.

`KMomentumUnitInstance` 는 정규 형식으로 정확히 세 항 — 지수 `+1` 의 `KMassUnit.BASE`(그램), 지수 `+1`
의 `KDistanceUnit.BASE`(미터), 지수 `-1` 의 `KTimeUnit.BASE`(초) — 을 감싼 `KMixedUnitInstance` 를 감쌉니다. 이 라이브러리의 질량 성분은 그램으로
정규화되므로, 저장 값은 원시 그램 기준 성분 값이며 kg·m/s 로 읽을 때 고정 인자로 나눕니다.

!!! note "충격량은 동일한 양입니다"
**충격량** `F · t` 는 정확히 이 차원 (`1 N·s = 1 kg·m/s`)을 가지므로, 별도의 그룹이 아니라 *이*
그룹입니다 — [충격량](impulse.md) 페이지를 참조하세요.

## 이름이 붙은 단위

| 단위               | 기호      |                       토큰 | kg·m/s로 1 |
|--------------------|-----------|---------------------------:|-----------:|
| 초당 킬로그램 미터 | `kg*m/s`  |  `kilogramMetersPerSecond` |        1.0 |
| 뉴턴 초            | `N*s`     |            `newtonSeconds` |        1.0 |
| 초당 그램 센티미터 | `g*cm/s`  | `gramCentimetersPerSecond` |       1e-5 |
| 초당 파운드-피트   | `lb*ft/s` |       `poundFeetPerSecond` | ≈ 0.138255 |

모든 단위는 전체 SI 접두사 범위를 지원합니다 (`kilo.newtonSeconds`, `milli.kilogramMetersPerSecond`).

## 분해

운동량에는 두 가지 동등한 분해가 있으며, 모두 동일한 정규화 팩토리로 합쳐져 동일한 타입이 지정된 값-동등 결과를 만듭니다.

| 형식              | Kotlin                                                           | 결과 타입               |
|-------------------|------------------------------------------------------------------|-------------------------|
| 질량 × 속력       | `mass * speed`                                                   | `KMomentumUnitInstance` |
| 힘 × 시간(충격량) | `force * time`                                                   | `KMomentumUnitInstance` |
| 네이티브 표현식   | `(mass.toUnit() * length.toUnit() / time.toUnit()).toMomentum()` | `KMomentumUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.momentum.*

val speed = (3 of meters) / (1 of seconds)
val viaMassSpeed = (2 of kilo.grams) * speed
val viaForceTime = (6 of newtons) * (1 of seconds)
val viaNative =
    ((2000 of grams).toUnit() * (3 of meters).toUnit() / (1 of seconds).toUnit()).toMomentum()

viaMassSpeed == viaForceTime            // true
viaMassSpeed == viaNative               // true
viaMassSpeed into kilogramMetersPerSecond // 6.0
```

## 핵심 단위로 계산

| 식                             | 결과 타입                      | 의미                            |
|--------------------------------|--------------------------------|---------------------------------|
| `mass * speed`, `speed * mass` | `KMomentumUnitInstance`        | `p = m · v`                     |
| `force * time`, `time * force` | `KMomentumUnitInstance`        | 충격량 `p = F · t`              |
| `momentum / mass`              | `KSpeedUnitInstance`           | `v = p / m`                     |
| `momentum / speed`             | `KMassUnitInstance`            | `m = p / v`                     |
| `momentum / time`              | `KForceUnitInstance`           | 평균 힘 `F = p / t`             |
| `momentum / force`             | `KTimeUnitInstance`            | 작용 시간 `t = p / F`           |
| `momentum * length`            | `KAngularMomentumUnitInstance` | [각운동량](angular-momentum.md) |

## 실전 예제: 자동차 제동

1200 kg 의 자동차가 20 m/s 로 주행합니다. 운동량은 얼마이며, 5초 안에 정지시키려면 어떤 일정한 힘이 필요합니까?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.momentum.*

val v = (20 of meters) / (1 of seconds)
val p = (1200 of kilo.grams) * v
p into kilogramMetersPerSecond      // 24000.0

val brakingForce = p / (5 of seconds)
brakingForce into newtons           // 4800.0
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

val sum = (10 of newtonSeconds) + (4 of newtonSeconds) // 14 N·s
(10 of kilogramMetersPerSecond) > (4 of newtonSeconds) // true
(1 of newtonSeconds) == (1 of kilogramMetersPerSecond) // true(동일한 차원)
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

(6 of kilogramMetersPerSecond).toString()          // "6.0 kg*m/s"(기본 단위)
"${(6 of kilogramMetersPerSecond) into newtonSeconds} N*s" // "6.0 N*s"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학        | Kotlin                                   | 의미                         |
|-------------|------------------------------------------|------------------------------|
| `kg·m/s`    | `kilogramMetersPerSecond`                | 운동량, 기본 단위(명명 토큰) |
| `kg·m·s⁻¹`  | `kilo.grams * meters * (seconds pow -1)` | 같은 양을 순수 곱으로 표현   |
| `N·s`       | `newtonSeconds`                          | 같은 차원의 충격량 표기      |
| `p = m · v` | `mass * speed`                           | 분해 A                       |
| `p = F · t` | `force * time`                           | 분해 B(충격량)               |
| `v = p / m` | `momentum / mass`                        | 속력에 대해 푼 형식          |
| `F = p / t` | `momentum / time`                        | 평균 힘에 대해 푼 형식       |
