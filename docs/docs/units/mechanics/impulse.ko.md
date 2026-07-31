# 충격량

패키지: `org.pcsoft.framework.kunit.mechanic.momentum`
기본 단위: **초당 킬로그램 미터**(`KMomentumUnit.BASE`), **뉴턴 초**(`KMomentumUnit.NEWTON_SECOND`)로 읽음

유형: **구성된 단위**

충격량 `J = F · t` 는 힘이 작용하는 시간 동안 전달하는 운동량입니다. 차원적으로 이는
[운동량](momentum.md)*입니다*: `1 N·s = 1 kg·m/s`. 따라서 KUnit은 이를 위한 두 번째 단위 그룹을 도입하지 **않습니다** — 충격량은 운동량 그룹의 **읽기**이며,
`newtonSeconds` 토큰으로 표현됩니다. 이 페이지는 그 읽기를 문서화하며, 그룹 자체는 [운동량](momentum.md) 페이지에 설명되어 있습니다.

!!! note "같은 그룹, 두 가지 읽기"
`(1 of newtonSeconds) == (1 of kilogramMetersPerSecond)` 는 `true` 입니다. 토큰을 선택하는 것은 값을 읽는 방식만 바꿀 뿐, 값이 무엇인지는 절대 바꾸지 않습니다.
"힘 × 시간" 으로 생각할 때는
`newtonSeconds`, "질량 × 속도" 로 생각할 때는 `kilogramMetersPerSecond` 를 사용하세요.

## 이름이 붙은 단위

| 단위               | 기호      |                       토큰 | kg·m/s로 1 |
|--------------------|-----------|---------------------------:|-----------:|
| 뉴턴 초            | `N*s`     |            `newtonSeconds` |        1.0 |
| 초당 킬로그램 미터 | `kg*m/s`  |  `kilogramMetersPerSecond` |        1.0 |
| 초당 그램 센티미터 | `g*cm/s`  | `gramCentimetersPerSecond` |       1e-5 |
| 초당 파운드-피트   | `lb*ft/s` |       `poundFeetPerSecond` | ≈ 0.138255 |

모든 토큰에 대해 접두사가 붙은 형식이 존재합니다 (`kilo.newtonSeconds` = kN·s,
`milli.newtonSeconds` = mN·s).

## 충격량 계산하기

| 식                | 결과 타입               | 의미                   |
|-------------------|-------------------------|------------------------|
| `force * time`    | `KMomentumUnitInstance` | `J = F · t`            |
| `time * force`    | `KMomentumUnitInstance` | 동일, 교환 가능        |
| `impulse / time`  | `KForceUnitInstance`    | 평균 힘 `F = J / t`    |
| `impulse / force` | `KTimeUnitInstance`     | 작용 시간 `t = J / F`  |
| `impulse / mass`  | `KSpeedUnitInstance`    | 속도 변화 `Δv = J / m` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.momentum.*

val j = (10 of newtons) * (3 of seconds)
j into newtonSeconds             // 30.0
j into kilogramMetersPerSecond   // 30.0(동일한 차원)
```

## 실전 예제: 로켓 단 연소

모형 로켓 모터가 평균 추력 12 N 을 1.6 초 동안 냅니다. 총 충격량은 얼마이며, 이는 0.8 kg 로켓에 어떤 속도 변화를 줍니까?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.momentum.*

val impulse = (12 of newtons) * (1.6 of seconds)
impulse into newtonSeconds              // 19.2

val deltaV = impulse / (0.8 of kilo.grams) // KSpeedUnitInstance
deltaV into (meters / seconds)             // 24.0
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

val total = (19.2 of newtonSeconds) + (5 of newtonSeconds) // 24.2 N·s
(19.2 of newtonSeconds) > (10 of newtonSeconds)            // true
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

(19.2 of newtonSeconds).toString()                  // "19.2 kg*m/s"(그룹 기본 단위)
"${(19.2 of newtonSeconds) into newtonSeconds} N*s" // "19.2 N*s"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학         | Kotlin                                   | 의미                            |
|--------------|------------------------------------------|---------------------------------|
| `N·s`        | `newtonSeconds`                          | 충격량(운동량 그룹의 명명 토큰) |
| `kg·m·s⁻¹`   | `kilo.grams * meters * (seconds pow -1)` | 기저 차원의 같은 양             |
| `J = F · t`  | `force * time`                           | 타입이 지정된 분해              |
| `F = J / t`  | `impulse / time`                         | 평균 힘에 대해 푼 형식          |
| `Δv = J / m` | `impulse / mass`                         | 질량의 속도 변화                |
| `kN·s`       | `kilo.newtonSeconds`                     | 접두사가 붙은 충격량            |
