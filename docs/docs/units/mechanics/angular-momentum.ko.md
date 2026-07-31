# 각운동량

패키지: `org.pcsoft.framework.kunit.mechanic.angularmomentum`
기본 단위: **초당 킬로그램 제곱미터**
(`KAngularMomentumUnit.BASE == KAngularMomentumUnit.KILOGRAM_METERS_SQUARED_PER_SECOND`)

유형: **구성된 단위**

각운동량 `L` 은 [운동량](momentum.md)의 회전 대응물이며 회전 시스템의 보존량입니다. 이는 **구성된**
단위로, 조합 `mass · length² · time⁻¹`(`kg·m²/s`)입니다.

`KAngularMomentumUnitInstance` 는 정규 형식으로 정확히 세 항 — 지수 `+1` 의
`KMassUnit.BASE`(그램), 지수 `+2` 의 `KDistanceUnit.BASE`(미터), 지수 `-1` 의
`KTimeUnit.BASE`(초) — 을 감싼 `KMixedUnitInstance` 를 감쌉니다. 라디안은 정규 형식에 **나타나지 않습니다** — 이는 무차원 비율이기 때문입니다.

!!! note "작용은 동일한 양입니다"
**작용**(에너지 × 시간)은 정확히 이 차원을 공유하며, 이것이 줄 초 (`jouleSeconds`, 플랑크 상수의 단위)가 *이* 그룹의 토큰인 이유입니다: `1 J·s = 1 kg·m²/s`.

## 이름이 붙은 단위

| 단위                   | 기호       |                              토큰 | kg·m²/s로 1 |
|------------------------|------------|----------------------------------:|------------:|
| 초당 킬로그램 제곱미터 | `kg*m^2/s` |  `kilogramMetersSquaredPerSecond` |         1.0 |
| 뉴턴 미터 초           | `N*m*s`    |              `newtonMeterSeconds` |         1.0 |
| 줄 초                  | `J*s`      |                    `jouleSeconds` |         1.0 |
| 초당 그램 제곱센티미터 | `g*cm^2/s` | `gramCentimetersSquaredPerSecond` |        1e-7 |

모든 단위는 전체 SI 접두사 범위를 지원합니다 (`femto.jouleSeconds`, `milli.jouleSeconds`).

## 분해

각운동량에는 두 가지 동등한 분해가 있으며, 둘 다 동일한 정규화 팩토리로 합쳐집니다.

| 형식            | Kotlin                                                                          | 결과 타입                      |
|-----------------|---------------------------------------------------------------------------------|--------------------------------|
| 관성 × 각속도   | `inertia * angularvelocity`                                                     | `KAngularMomentumUnitInstance` |
| 운동량 × 지레팔 | `momentum * length`                                                             | `KAngularMomentumUnitInstance` |
| 네이티브 표현식 | `(mass.toUnit() * (length.toUnit() pow 2) / time.toUnit()).toAngularMomentum()` | `KAngularMomentumUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.radians
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.div
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.momentum.kilogramMetersPerSecond

val omega = (3 of radians) / (1 of seconds)
val viaInertia = (2 of kilogramMetersSquared) * omega
val viaMomentum = (3 of kilogramMetersPerSecond) * (2 of meters)
val viaNative =
    ((2000 of grams).toUnit() * ((3 of meters).toUnit() pow 2) / (1 of seconds).toUnit()).toAngularMomentum()

viaInertia == viaMomentum                       // true - 둘 다 6 kg·m²/s
viaInertia into kilogramMetersSquaredPerSecond  // 6.0
viaNative into kilogramMetersSquaredPerSecond   // 18.0
```

## 핵심 단위로 계산

| 식                                       | 결과 타입                      | 의미            |
|------------------------------------------|--------------------------------|-----------------|
| `inertia * angularvelocity`              | `KAngularMomentumUnitInstance` | `L = J · ω`     |
| `angularvelocity * inertia`              | `KAngularMomentumUnitInstance` | 동일, 교환 가능 |
| `momentum * length`, `length * momentum` | `KAngularMomentumUnitInstance` | `L = p · r`     |
| `angularmomentum / inertia`              | `KAngularVelocityUnitInstance` | `ω = L / J`     |
| `angularmomentum / angularvelocity`      | `KInertiaUnitInstance`         | `J = L / ω`     |
| `angularmomentum / length`               | `KMomentumUnitInstance`        | `p = L / r`     |
| `angularmomentum / momentum`             | `KLengthUnitInstance`          | `r = L / p`     |

## 실전 예제: 피겨 스케이터가 팔을 모을 때

스케이터가 관성 모멘트 4 kg·m² 로 2 회전/초로 회전하고 있습니다. 팔을 모으면 관성 모멘트가 1.6 kg·m² 로 줄어듭니다. 각운동량이 보존되므로, 새로운 회전율은 `ω = L / J` 로부터 나옵니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerSecond
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared

val l = (4 of kilogramMetersSquared) * (2 of revolutionsPerSecond)
l into kilogramMetersSquaredPerSecond // ≈ 50.27

val faster = l / (1.6 of kilogramMetersSquared) // KAngularVelocityUnitInstance
faster into revolutionsPerSecond                 // 5.0
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*

val sum = (10 of jouleSeconds) + (4 of jouleSeconds) // 14 J·s
(10 of jouleSeconds) > (4 of newtonMeterSeconds)     // true
(1 of jouleSeconds) == (1 of newtonMeterSeconds)     // true(동일한 차원)
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*

(6 of kilogramMetersSquaredPerSecond).toString()             // "6.0 kg*m^2/s"(기본 단위)
"${(6 of kilogramMetersSquaredPerSecond) into jouleSeconds} J*s" // "6.0 J*s"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학        | Kotlin                                           | 의미                           |
|-------------|--------------------------------------------------|--------------------------------|
| `kg·m²/s`   | `kilogramMetersSquaredPerSecond`                 | 각운동량, 기본 단위(명명 토큰) |
| `kg·m²·s⁻¹` | `kilo.grams * (meters pow 2) * (seconds pow -1)` | 같은 양을 순수 곱으로 표현     |
| `J·s`       | `jouleSeconds`                                   | 같은 차원의 작용량 표기        |
| `L = J · ω` | `inertia * angularvelocity`                      | 분해 A                         |
| `L = p · r` | `momentum * length`                              | 분해 B                         |
| `ω = L / J` | `angularmomentum / inertia`                      | 각속도에 대해 푼 형식          |
| `r = L / p` | `angularmomentum / momentum`                     | 지레팔에 대해 푼 형식          |
