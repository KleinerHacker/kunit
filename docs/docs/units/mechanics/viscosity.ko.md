# 동적 점도

패키지: `org.pcsoft.framework.kunit.mechanic.viscosity`
기본 단위: **파스칼 초**(`KViscosityUnit.BASE == KViscosityUnit.PASCAL_SECOND`)

유형: **구성된 단위**

동적 점도 `η` 는 유체의 전단 저항을 나타냅니다. 이는 **구성된** 단위입니다 —
`pressure · time`, 즉 `mass · length⁻¹ · time⁻¹`(`Pa·s`)의 합성입니다.

`KViscosityUnitInstance` 는 정확히 세 항으로 된 표준 정규화 형태의 `KMixedUnitInstance` 를 감쌉니다: `KMassUnit.BASE`(그램)가 `+1`,
`KDistanceUnit.BASE`(미터)가 `-1`, `KTimeUnit.BASE`(초)가
`-1` 입니다. 이 라이브러리의 질량 성분은 그램으로 정규화되어 있으므로, 저장된 값은 원시 그램 기반 성분 값이며 Pa·s 읽기는 고정 계수로 나눕니다.

!!! note "동적 점도 대 동점도"
**동점도** `ν = η / ρ`(`m²/s`)는 다른 물리량이며 확산도 그룹에 속합니다 —
[동점도](kinematic-viscosity.md)를 참고하세요.

## 이름이 붙은 단위

| 단위                    | 기호         |                             토큰 | Pa·s 로 1 단위 |
|-------------------------|--------------|---------------------------------:|---------------:|
| 파스칼 초               | `Pa*s`       |                  `pascalSeconds` |            1.0 |
| 포아즈                  | `P`          |                         `poises` |            0.1 |
| 파운드힘 초 매 제곱피트 | `lbf*s/ft^2` | `poundForceSecondsPerSquareFoot` |      ≈ 47.8803 |
| 레인(lbf·s/in²)         | `reyn`       |                          `reyns` |     ≈ 6894.757 |

물과 같은 유체에 대한 두 가지 일상적인 표기는 접두사 형식이며 별도 토큰이 아닙니다:
**밀리파스칼 초**는 `milli.pascalSeconds` 이고 **센티포아즈**는 `centi.poises` 이며 — 서로 같습니다 (`1 mPa·s = 1 cP`, 20 °C 의 물).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.viscosity.*

val water = 1 of milli.pascalSeconds
water into centi.poises  // 1.0
water into pascalSeconds // 0.001
(1 of poises) into pascalSeconds // 0.1
```

## 핵심 단위로 계산하기 (압력 & 시간)

| 식                                   | 결과 타입                  | 의미               |
|--------------------------------------|----------------------------|--------------------|
| `pressure * time`, `time * pressure` | `KViscosityUnitInstance`   | `η = p · t`        |
| `viscosity / pressure`               | `KTimeUnitInstance`        | `t = η / p`        |
| `viscosity / time`                   | `KPressureUnitInstance`    | `p = η / t`        |
| `viscosity / density`                | `KDiffusivityUnitInstance` | 동점도 `ν = η / ρ` |
| `viscosity / diffusivity`            | `KDensityUnitInstance`     | `ρ = η / ν`        |

네이티브 형식은 `toViscosity()` 로 변환됩니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.viscosity.*

val typed = (2 of pascals) * (3 of seconds)
val native = ((2 of pascals).toUnit() * (3 of seconds).toUnit()).toViscosity()

typed == native            // true - 둘 다 6 Pa·s
typed into pascalSeconds   // 6.0
```

## 실전 예제: 작동 온도에서의 엔진 오일

SAE 30 오일은 100 °C 에서 9.3 cP 이며 밀도는 850 kg/m³ 입니다. 이는 Pa·s 로 얼마이며, 어떤 동점도에 해당합니까?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.diffusivity.centistokes
import org.pcsoft.framework.kunit.common.diffusivity.div
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.viscosity.*
import org.pcsoft.framework.kunit.pow

val oil = 9.3 of centi.poises
oil into pascalSeconds        // 0.0093

val rho = (850 of kilo.grams) / (1 of (meters pow 3))
val nu = oil / rho            // KDiffusivityUnitInstance
nu into centistokes           // ≈ 10.94
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.viscosity.*

val sum = (10 of pascalSeconds) + (4 of pascalSeconds) // 14 Pa·s
(1 of poises) > (1 of milli.pascalSeconds)             // true
(1 of poises) == (100 of milli.pascalSeconds)          // true
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.mechanic.viscosity.*

(2 of pascalSeconds).toString()                    // "2.0 Pa*s"(기본 단위)
"${(2 of pascalSeconds) into centi.poises} cP"     // "2000.0 cP"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학         | Kotlin                                            | 의미                                 |
|--------------|---------------------------------------------------|--------------------------------------|
| `Pa·s`       | `pascalSeconds`                                   | 동적 점도, 기본 단위(이름 붙은 토큰) |
| `kg·m⁻¹·s⁻¹` | `kilo.grams * (meters pow -1) * (seconds pow -1)` | 순수한 곱으로 표현한 같은 양         |
| `cP`         | `centi.poises`                                    | 센티포아즈(= 1 mPa·s)                |
| `η = p · t`  | `pressure * time`                                 | 타입 분해                            |
| `ν = η / ρ`  | `viscosity / density`                             | 동점도                               |
| `mPa·s`      | `milli.pascalSeconds`                             | 접두사가 붙은 점도                   |
