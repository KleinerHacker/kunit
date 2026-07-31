# 토크

패키지: `org.pcsoft.framework.kunit.common.energy`
기본 단위: **줄**(`KEnergyUnit.BASE == KEnergyUnit.JOULE`), **뉴턴 미터**(`N·m`)로 읽음

유형: **구성된 단위**

토크 `M = F · r` 는 지레팔에 작용하는 힘의 회전 효과입니다. 차원적으로 이는 [에너지](energy.md)*입니다*:
`1 N·m = 1 J`. 따라서 KUnit은 이를 위한 두 번째 단위 그룹을 도입하지 **않습니다** — 토크는 에너지 그룹의 **읽기**입니다. 이 페이지는 그 읽기를 문서화하며, 그룹 자체는
[에너지 (역학)](energy.md) 페이지에 설명되어 있습니다.

!!! note "같은 차원, 다른 물리량"
토크와 일은 물리적으로 다릅니다 (토크는 축 벡터, 일은 스칼라입니다), 하지만 차원 `kg·m²·s⁻²`
를 정확히 공유합니다. KUnit은 *단위* 를 모델링하는 것이지 벡터 특성을 모델링하지 않으므로 둘 다 하나의 그룹에 있습니다. 이름으로 구분하세요:
`val torque = (100 of newtons) * (2 of meters)`
는 N·m 로 읽히고, 경로를 따라가는 `val work = force * distance` 는 J 로 읽힙니다.

## 토크 만들기

| 식                                 | 결과 타입                          | 의미                        |
|------------------------------------|------------------------------------|-----------------------------|
| `force * length`, `length * force` | `KEnergyUnitInstance`              | `M = F · r`(지레팔)         |
| `inertia * angularacceleration`    | `KEnergyUnitInstance`              | `M = J · α`(회전 뉴턴 법칙) |
| `power / angularvelocity`          | `KEnergyUnitInstance`              | `M = P / ω`(구동계 공식)    |
| `torque * angularvelocity`         | `KPowerUnitInstance`               | `P = M · ω`                 |
| `torque / inertia`                 | `KAngularAccelerationUnitInstance` | `α = M / J`                 |
| `torque / angularacceleration`     | `KInertiaUnitInstance`             | `J = M / α`                 |
| `power / torque`                   | `KAngularVelocityUnitInstance`     | `ω = P / M`                 |

세 가지 생성 형식 모두 에너지 그룹의 단일 팩토리로 합쳐지므로 값이 동등합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.energy.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.angularacceleration.radiansPerSecondSquared
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerSecond
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared

val viaLever = (100 of newtons) * (2 of meters)                          // 200 N·m
val viaPower = (200.0 * 2.0 * Math.PI of watts) / (1 of revolutionsPerSecond)
val viaInertia = (2 of kilogramMetersSquared) * (100 of radiansPerSecondSquared) // 200 N·m

viaLever into joules   // 200.0
viaPower into joules   // 200.0
viaInertia into joules // 200.0
```

## 이름이 붙은 단위

토크는 에너지 그룹의 토큰을 사용합니다. `newtons * meters` 는 관용적인 N·m 표기이며, 접두사가 붙은 읽기는 에너지 토큰에서 나옵니다 (`kilo.joules` = kN·m).

| 읽기            | 기호   | Kotlin                           |
|-----------------|--------|----------------------------------|
| 뉴턴 미터       | `N*m`  | `(1 of newtons) * (1 of meters)` |
| 줄(동일한 차원) | `J`    | `joules`                         |
| 킬로뉴턴 미터   | `kN*m` | `kilo.joules`                    |

## 실전 예제: 엔진 토크와 동력

엔진이 3000 rpm 에서 62.83 kW 를 냅니다. 토크는 얼마입니까? 같은 토크를 6000 rpm 에서 유지하면 동력은 얼마가 됩니까?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerMinute

val torque = (62.83 of kilo.watts) / (3000 of revolutionsPerMinute)
torque into joules                     // ≈ 200.0(N·m)

val doubled = torque * (6000 of revolutionsPerMinute)
doubled into kilo.watts                // ≈ 125.7
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.energy.*

val sum = (200 of joules) + (50 of joules) // 250 N·m
(200 of joules) > (150 of joules)          // true
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*

(200 of joules).toString()                 // "200.0 J"(그룹 기본 단위)
"${(200 of joules) into kilo.joules} kN*m" // "0.2 kN*m"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학        | Kotlin                                           | 의미                    |
|-------------|--------------------------------------------------|-------------------------|
| `N·m`       | `(1 of newtons) * (1 of meters)`                 | 토크, 지레팔 형식       |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)` | 기저 차원의 같은 양     |
| `M = F · r` | `force * length`                                 | 분해 A                  |
| `M = J · α` | `inertia * angularacceleration`                  | 분해 B                  |
| `M = P / ω` | `power / angularvelocity`                        | 분해 C(구동계)          |
| `P = M · ω` | `torque * angularvelocity`                       | 회전 동력               |
| `kN·m`      | `kilo.joules`                                    | 접두사가 붙은 토크 읽기 |
