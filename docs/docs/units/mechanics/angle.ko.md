# 각도

패키지: `org.pcsoft.framework.kunit.mechanic.angle`
기본 단위: **라디안**(`KAngleUnit.BASE == KAngleUnit.RADIAN`)

유형: **네이티브 단위**

평면각은 KUnit의 **네이티브** 단위입니다: 조합이 아니라 자체 단위 어휘를 가진 직접 측정 가능한 기본 양입니다. `KAngleUnitInstance` 는 지수 1의 단일 `KAngleUnit.BASE` 항을
감싼 `KMixedUnitInstance` 를 감싸며, 항상 라디안으로 정규화됩니다.

각도는 역학의 회전 부분 전체의 토대입니다: [각속도](angular-velocity.md),
[각가속도](angular-acceleration.md), [각운동량](angular-momentum.md), [입체각](solid-angle.md)이 모두 이를 기반으로 합니다.

## 이름이 붙은 단위

| 단위         | 기호  |         토큰 |           rad로 1 |
|--------------|-------|-------------:|------------------:|
| 라디안       | `rad` |    `radians` |               1.0 |
| 도           | `°`   |    `degrees` | π/180 ≈ 0.0174533 |
| 각분         | `'`   | `arcminutes` |           π/10800 |
| 각초         | `"`   | `arcseconds` |          π/648000 |
| 그라디안(곤) | `gon` |   `gradians` |             π/200 |
| 회전(회전수) | `tr`  |      `turns` |       2π ≈ 6.2832 |

모든 단위는 전체 SI 접두사 범위를 지원합니다 (`milli.radians`, 천체측정용 `micro.arcseconds` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.angle.*

val a = 90 of degrees
a into radians      // ≈ 1.5708
a into turns        // 0.25
a into gradians     // 100.0
1 of milli.radians  // 0.001 rad
```

## 각도로 계산하기

| 식                               | 결과 타입                      | 의미                |
|----------------------------------|--------------------------------|---------------------|
| `angle + angle`, `angle - angle` | `KAngleUnitInstance`           | 동일 타입 연산      |
| `angle * angle`                  | `KSolidAngleUnitInstance`      | 입체각(`rad² = sr`) |
| `angle / time`                   | `KAngularVelocityUnitInstance` | 각속도 `ω = φ / t`  |
| `angle / angularvelocity`        | `KTimeUnitInstance`            | 회전에 걸리는 시간  |
| `angle / angle`                  | `KMixedUnitInstance`           | 무차원 비율         |

삼각함수는 값에서 직접 사용할 수 있습니다. 라디안 읽기 값을 소비하기 때문입니다:
`angle.sin()`, `angle.cos()`, `angle.tan()`.

## 실전 예제: 기어박스 출력 각도

모터 축이 3바퀴 완전히 회전합니다. 비율 5:1의 기어 쌍이 이를 감속시킵니다. 출력 각도는 도 단위로 얼마이며, 600 rpm에서 이 운동에는 얼마나 걸릴까요?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val input = 3 of turns
val output = input / 5                 // KAngleUnitInstance, 0.6 turns
output into degrees                    // 216.0

val t = input / (600 of revolutionsPerMinute) // KTimeUnitInstance
t into seconds                                // 0.3
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.*

val sum = (90 of degrees) + (30 of degrees) // 120°
(1 of turns) > (359 of degrees)             // true
(180 of degrees) == (0.5 of turns)          // true(값 기반 동등성)
(90 of degrees).sin()                       // 1.0
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.*

(2 of radians).toString()                    // "2.0 rad"(기본 단위)
"${(1 of turns) into degrees} °"             // "360.0 °"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학            | Kotlin                      | 의미                           |
|-----------------|-----------------------------|--------------------------------|
| `rad`           | `radians`                   | 평면각, 기본 단위              |
| `°`             | `degrees`                   | 도                             |
| `mrad`          | `milli.radians`             | 접두사가 붙은 각도(밀리라디안) |
| `1 tr = 2π rad` | `(1 of turns) into radians` | 라디안으로 표현한 완전한 회전  |
| `ω = φ / t`     | `angle / time`              | 각도로부터의 각속도            |
| `Ω = φ²`        | `angle * angle`             | 두 평면각으로부터의 입체각     |
