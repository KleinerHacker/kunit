# 각가속도

패키지: `org.pcsoft.framework.kunit.mechanic.angularacceleration`
기본 단위: **초당 라디안 제곱**
(`KAngularAccelerationUnit.BASE == KAngularAccelerationUnit.RADIANS_PER_SECOND_SQUARED`)

유형: **구성된 단위**

각가속도 `α` 는 [가속도](../kinematics/acceleration.md)의 회전 대응물입니다: 단위 시간당
[각속도](angular-velocity.md)의 변화입니다. 이는 **구성된** 단위로, 조합 `angle · time⁻²`(`rad/s²`)입니다.

`KAngularAccelerationUnitInstance` 는 정규 형식으로 정확히 두 항 — 지수 `+1` 의
`KAngleUnit.BASE`(라디안)와 지수 `-2` 의 `KTimeUnit.BASE`(초) — 을 감싼 `KMixedUnitInstance` 를 감쌉니다. 값은 항상 rad/s² 로 정규화됩니다.

## 이름이 붙은 단위

| 단위             | 기호      |                            토큰 | rad/s²로 1 |
|------------------|-----------|--------------------------------:|-----------:|
| 초당 라디안 제곱 | `rad/s^2` |       `radiansPerSecondSquared` |        1.0 |
| 초당 도 제곱     | `°/s^2`   |       `degreesPerSecondSquared` |      π/180 |
| 초당 회전수 제곱 | `rps^2`   |   `revolutionsPerSecondSquared` |         2π |
| 초당 분당 회전수 | `rpm/s`   | `revolutionsPerMinutePerSecond` |      2π/60 |

접두사는 구성 요소에 적용되므로 (`kilo.radians / (seconds pow 2)`), 이 그룹에는 자체 접두사 빌더가 없습니다.

## 분해

각가속도에는 두 가지 동등한 분해가 있습니다. 둘 다 동일한 정규 값으로 축소됩니다.

| 형식                 | Kotlin                                                             | 결과 타입                          |
|----------------------|--------------------------------------------------------------------|------------------------------------|
| 타입이 지정된 연산자 | `angularvelocity / time`                                           | `KAngularAccelerationUnitInstance` |
| 네이티브 표현식      | `(angle.toUnit() / (time.toUnit() pow 2)).toAngularAcceleration()` | `KAngularAccelerationUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val typed = (6 of radians / seconds) / (3 of seconds)
val native = ((2 of radians).toUnit() / ((1 of seconds).toUnit() pow 2)).toAngularAcceleration()

typed == native                        // true - 둘 다 2 rad/s²
typed into radiansPerSecondSquared     // 2.0
```

## 핵심 단위로 계산

| 식                                      | 결과 타입                          | 의미                                     |
|-----------------------------------------|------------------------------------|------------------------------------------|
| `angularvelocity / time`                | `KAngularAccelerationUnitInstance` | `α = ω / t`                              |
| `angularacceleration * time`            | `KAngularVelocityUnitInstance`     | 얻은 속도 `ω = α · t`                    |
| `time * angularacceleration`            | `KAngularVelocityUnitInstance`     | 동일, 교환 가능                          |
| `angularvelocity / angularacceleration` | `KTimeUnitInstance`                | 가속 시간 `t = ω / α`                    |
| `inertia * angularacceleration`         | `KEnergyUnitInstance`              | 토크 `M = J · α`, [토크](torque.md) 참조 |

## 실전 예제: 모터 가속

서보 모터가 0.4 초 만에 3000 rpm에 도달합니다. 각가속도는 얼마이며, 정지 상태에서 0.2 초간 가속할 때 얼마나 회전합니까?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val alpha = (3000 of revolutionsPerMinute) / (0.4 of seconds)
alpha into radiansPerSecondSquared      // ≈ 785.4
alpha into revolutionsPerMinutePerSecond // 7500.0

val afterHalf = alpha * (0.2 of seconds) // KAngularVelocityUnitInstance
afterHalf into revolutionsPerMinute      // 1500.0
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*

val sum = (10 of radiansPerSecondSquared) + (4 of radiansPerSecondSquared) // 14 rad/s²
(1 of revolutionsPerSecondSquared) > (300 of degreesPerSecondSquared)      // true
(60 of revolutionsPerMinutePerSecond) == (1 of revolutionsPerSecondSquared) // true
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*

(2 of radiansPerSecondSquared).toString()                          // "2.0 rad/s^2"
"${(1 of revolutionsPerSecondSquared) into radiansPerSecondSquared} rad/s^2" // "6.283... rad/s^2"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학        | Kotlin                                                                  | 의미                           |
|-------------|-------------------------------------------------------------------------|--------------------------------|
| `rad/s²`    | `radiansPerSecondSquared`                                               | 각가속도, 기본 단위(명명 토큰) |
| `rad·s⁻²`   | `radians * (seconds pow -2)`                                            | 같은 양을 순수 곱으로 표현     |
| `rad/s²`    | `(radians.toUnit() / (seconds.toUnit() pow 2)).toAngularAcceleration()` | 네이티브 분해                  |
| `α = ω / t` | `angularvelocity / time`                                                | 타입이 지정된 분해             |
| `ω = α · t` | `angularacceleration * time`                                            | 각속도에 대해 푼 형식          |
| `rpm/s`     | `revolutionsPerMinutePerSecond`                                         | 기계 가속률                    |
