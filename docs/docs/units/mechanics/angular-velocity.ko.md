# 각속도

패키지: `org.pcsoft.framework.kunit.mechanic.angularvelocity`
기본 단위: **초당 라디안**(`KAngularVelocityUnit.BASE == KAngularVelocityUnit.RADIANS_PER_SECOND`)

유형: **구성된 단위**

각속도 `ω` 는 [속력](../kinematics/speed.md)의 회전 대응물입니다: 단위 시간당 휩쓸린 각도입니다. 이는 **구성된** 단위로, 조합 `angle · time⁻¹`(`rad/s`)입니다.

`KAngularVelocityUnitInstance` 는 정규 형식으로 정확히 두 항 — 지수 `+1` 의 `KAngleUnit.BASE`(라디안)와 지수 `-1` 의 `KTimeUnit.BASE`(초) — 을
감싼 `KMixedUnitInstance` 를 감쌉니다. 값은 항상 rad/s 로 정규화됩니다.

## 각속도 만들기

`angle / time` 로, 또는 전통적인 회전율 토큰 중 하나로 만듭니다. 단순 조합 표기는 의도적으로 자체 토큰이 **없습니다**: `rad/s` 는 `radians / seconds` 이고 `°/s` 는
`degrees / seconds` 입니다. 접두사는 구성 요소에 적용되므로 (`kilo.radians / seconds`), 이 그룹에는 자체 접두사 빌더가 없습니다.

| 단위        | 기호    |                   토큰 |       rad/s로 1 |
|-------------|---------|-----------------------:|----------------:|
| 초당 라디안 | `rad/s` |    `radians / seconds` |             1.0 |
| 초당 도     | `°/s`   |    `degrees / seconds` |           π/180 |
| 분당 회전수 | `rpm`   | `revolutionsPerMinute` | 2π/60 ≈ 0.10472 |
| 초당 회전수 | `rps`   | `revolutionsPerSecond` |     2π ≈ 6.2832 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val w = (1 of turns) / (1 of seconds)
w into revolutionsPerMinute  // 60.0
w into (radians / seconds)   // ≈ 6.2832
```

## 핵심 단위 (각도와 시간)로 계산

| 식                          | 결과 타입                          | 의미                                |
|-----------------------------|------------------------------------|-------------------------------------|
| `angle / time`              | `KAngularVelocityUnitInstance`     | `ω = φ / t`                         |
| `angularvelocity * time`    | `KAngleUnitInstance`               | 휩쓸린 각도 `φ = ω · t`             |
| `time * angularvelocity`    | `KAngleUnitInstance`               | 동일, 교환 가능                     |
| `angle / angularvelocity`   | `KTimeUnitInstance`                | 필요한 시간 `t = φ / ω`             |
| `angularvelocity / time`    | `KAngularAccelerationUnitInstance` | [각가속도](angular-acceleration.md) |
| `inertia * angularvelocity` | `KAngularMomentumUnitInstance`     | [각운동량](angular-momentum.md)     |
| `torque * angularvelocity`  | `KPowerUnitInstance`               | 회전 동력, [토크](torque.md) 참조   |

네이티브 형식도 사용할 수 있습니다: 제네릭 엔진을 통해 만들어진 모든 `angle / time` 식은
`toAngularVelocity()` 로 변환됩니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val typed = (2 of radians) / (4 of seconds)
val native = ((2 of radians).toUnit() / (4 of seconds).toUnit()).toAngularVelocity()

typed == native // true - 둘 다 0.5 rad/s
```

## 실전 예제: 스핀들 속도

밀링 스핀들이 12,000 rpm으로 회전합니다. 공구 원주 위의 한 점은 초당 각도로 얼마나 움직이며, 한 바퀴 회전에는 얼마나 걸립니까?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val spindle = 12_000 of revolutionsPerMinute
val perSecond = spindle * (1 of seconds)   // KAngleUnitInstance
perSecond into turns                        // 200.0

val perTurn = (1 of turns) / spindle        // KTimeUnitInstance
perTurn into seconds                        // 0.005
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val sum = (1000 of revolutionsPerMinute) + (500 of revolutionsPerMinute) // 1500 rpm
(1 of revolutionsPerSecond) > (59 of revolutionsPerMinute)               // true
(60 of revolutionsPerMinute) == (1 of revolutionsPerSecond)              // true
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

(1 of revolutionsPerSecond).toString()                        // "6.283185307179586 rad/s"
"${(1 of revolutionsPerSecond) into revolutionsPerMinute} rpm" // "60.0 rpm"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학        | Kotlin                       | 의미                         |
|-------------|------------------------------|------------------------------|
| `rad/s`     | `radians / seconds`          | 각속도, 기본 단위(분수 형식) |
| `rad·s⁻¹`   | `radians * (seconds pow -1)` | 같은 양을 순수 곱으로 표현   |
| `rpm`       | `revolutionsPerMinute`       | 분당 회전수(명명 토큰)       |
| `ω = φ / t` | `angle / time`               | 타입이 지정된 분해           |
| `φ = ω · t` | `angularvelocity * time`     | 각도에 대해 푼 형식          |
| `t = φ / ω` | `angle / angularvelocity`    | 시간에 대해 푼 형식          |
