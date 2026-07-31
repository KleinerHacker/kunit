# 동점도

패키지: `org.pcsoft.framework.kunit.common.diffusivity`
기본 단위: **제곱미터 매 초**
(`KDiffusivityUnit.BASE == KDiffusivityUnit.SQUARE_METER_PER_SECOND`)

유형: **구성된 단위**

동점도 `ν = η / ρ` 는 [동적 점도](viscosity.md)를 [밀도](density.md)로 나눈 값입니다 — 유체 내 운동량이 확산되는 방식을 지배하는 양입니다. 차원은
`length² · time⁻¹`(`m²/s`) 입니다.

이는 정확히 **확산도** 그룹의 차원과 물리량이며, 열역학의
[열확산도](../thermodynamics/thermal-diffusivity.md)와 공유됩니다. 따라서 KUnit은 이를 위한 두 번째 그룹을 도입하지 **않습니다**: 동점도는
`KDiffusivityUnitInstance` 의 **읽기**이며, 그래서 이 그룹은 `common` 에 있습니다. 이 페이지는 역학적 읽기를 문서화합니다.

!!! note "하나의 그룹, 두 개의 전문 분야"
`KDiffusivityUnit` 은 두 어휘를 모두 가집니다: 두 분야가 공유하는 미터법 읽기 (m²/s, mm²/s)와 전통적인 동점도 표기인 스토크스, 센티스토크스입니다.

## 이름이 붙은 단위

| 단위               | 기호    |                         토큰 | m²/s 로 1 단위 |
|--------------------|---------|-----------------------------:|---------------:|
| 제곱미터 매 초     | `m²/s`  |      `squareMetersPerSecond` |            1.0 |
| 제곱밀리미터 매 초 | `mm²/s` | `squareMillimetersPerSecond` |           1e-6 |
| 스토크스           | `St`    |                     `stokes` |           1e-4 |
| 센티스토크스       | `cSt`   |                `centistokes` |           1e-6 |
| 제곱피트 매 시간   | `ft²/h` |          `squareFeetPerHour` |   ≈ 2.58064e-5 |

`1 cSt = 1 mm²/s` 로 정확히 같습니다 — 20 °C 의 물은 ≈ 1 cSt 입니다. 모든 단위는 전체 SI 접두사 범위를 지원하므로, `centi.stokes` 는 센티스토크스의 또 다른 표기입니다.

## 분해

| 형식             | Kotlin                                                      | 결과 타입                  |
|------------------|-------------------------------------------------------------|----------------------------|
| 동적 점도 / 밀도 | `viscosity / density`                                       | `KDiffusivityUnitInstance` |
| 네이티브 식      | `((length.toUnit() pow 2) / time.toUnit()).toDiffusivity()` | `KDiffusivityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.viscosity.pascalSeconds

val water = (1000 of kilo.grams) / (1 of (meters pow 3))
val typed = (1 of milli.pascalSeconds) / water
val native = (((1 of milli.meters).toUnit() pow 2) / (1 of seconds).toUnit()).toDiffusivity()

typed == native          // true - 둘 다 1e-6 m²/s
typed into centistokes   // 1.0
```

## 핵심 단위로 계산하기

| 식                                               | 결과 타입                  | 의미        |
|--------------------------------------------------|----------------------------|-------------|
| `viscosity / density`                            | `KDiffusivityUnitInstance` | `ν = η / ρ` |
| `diffusivity * density`, `density * diffusivity` | `KViscosityUnitInstance`   | `η = ν · ρ` |
| `viscosity / diffusivity`                        | `KDensityUnitInstance`     | `ρ = η / ν` |

## 실전 예제: 유압유 선정

유압유가 ISO VG 46 으로 지정되어 있습니다, 즉 40 °C 에서 46 cSt 이며 밀도는 870 kg/m³ 입니다. 이는 어떤 동적 점도에 해당합니까?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.viscosity.pascalSeconds

val nu = 46 of centistokes
nu into squareMillimetersPerSecond // 46.0

val rho = (870 of kilo.grams) / (1 of (meters pow 3))
val eta = nu * rho                 // KViscosityUnitInstance
eta into pascalSeconds             // ≈ 0.04002
eta into centi.poises              // ≈ 40.02
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.diffusivity.*

val sum = (10 of centistokes) + (4 of centistokes) // 14 cSt
(1 of stokes) > (10 of centistokes)                // true
(1 of centistokes) == (1 of squareMillimetersPerSecond) // true (같은 값)
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.diffusivity.*

(46 of centistokes).toString()                  // "4.6E-5 m²/s"(기본 단위)
"${(46 of centistokes) into centistokes} cSt"   // "46.0 cSt"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학        | Kotlin                     | 의미                    |
|-------------|----------------------------|-------------------------|
| `m²/s`      | `squareMetersPerSecond`    | 동점도, 기본 단위       |
| `m²·s⁻¹`    | `(meters pow 2) / seconds` | 기저 차원의 같은 양     |
| `cSt`       | `centistokes`              | 센티스토크스(= 1 mm²/s) |
| `ν = η / ρ` | `viscosity / density`      | 타입 분해               |
| `η = ν · ρ` | `diffusivity * density`    | 동적 점도에 대해 정리   |
| `ρ = η / ν` | `viscosity / diffusivity`  | 밀도에 대해 정리        |
