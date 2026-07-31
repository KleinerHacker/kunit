# 열확산율 (Thermal Diffusivity)

패키지: `org.pcsoft.framework.kunit.common.diffusivity`
기본 단위: **초당 제곱미터** (`KDiffusivityUnit.BASE == KDiffusivityUnit.SQUARE_METER_PER_SECOND`)

유형: **구성된 단위**

열확산율 `α`는 온도 변화가 물질을 통해 얼마나 *빠르게* 전파되는지를 나타냅니다 — 이는 정상 상태에서 열이 얼마나 *많이* 흐르는지를 나타내는 [열전도율](thermal-conductivity.md)과
대조됩니다. 단위:
`m²/s`. 다음과 같이 정의됩니다:

```
α = λ / (ρ · c_p)
```

`KDiffusivityUnitInstance`는 정규 형식 `distance² · time⁻¹` (`m²·s⁻¹`)의 정확히 두 항으로 이루어진 `KMixedUnitInstance`를 감싸며, 항상 m²/s로
정규화됩니다.

!!! note "하나의 그룹, 두 개의 분야"
이 그룹은 `common.diffusivity`에 있습니다 — 바로 이 동일한 양이 두 분야에서 문서화되기 때문입니다:
여기서는 **열확산율**로, 역학에서는 [동점도](../mechanics/kinematic-viscosity.md) `ν = η / ρ`로 문서화됩니다. 두 읽기 모두 `KDiffusivityUnit`의 단위 어휘를
공유하며, 여기에는 전통적인 스토크스 표기도 포함됩니다.

## 이름이 붙은 단위

| 단위              | 기호    |                         토큰 |     m²/s로 1 |
|-------------------|---------|-----------------------------:|-------------:|
| 초당 제곱미터     | `m²/s`  |      `squareMetersPerSecond` |          1.0 |
| 초당 제곱밀리미터 | `mm²/s` | `squareMillimetersPerSecond` |         1e-6 |
| 시간당 제곱피트   | `ft²/h` |          `squareFeetPerHour` | ≈ 2.58064e-5 |
| 스토크스          | `St`    |                     `stokes` |         1e-4 |
| 센티스토크스      | `cSt`   |                `centistokes` |         1e-6 |

물질 표에는 보통 `α`를 mm²/s로 나타내며, 이는 정확히 `micro.squareMetersPerSecond`입니다. 모든 단위는 전체 SI 접두사 범위를 지원합니다.

## 전형적인 값

| 물질     |            α |
|----------|-------------:|
| 구리     |  ≈ 116 mm²/s |
| 강철     |   ≈ 14 mm²/s |
| 유리     | ≈ 0.34 mm²/s |
| 물       | ≈ 0.14 mm²/s |
| 미네랄울 |  ≈ 1.2 mm²/s |

## 실전 예제: 구리가 얼마나 빠르게 평형에 도달하는가

구리는 λ = 401 W/ (m·K), ρ = 8960 kg/m³, c_p = 385 J/ (kg·K)입니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.toDensity
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin

val density = ((8960 of kilo.grams).toUnit() / ((1 of meters).toUnit() pow 3)).toDensity()
val alpha = (401 of wattsPerMeterKelvin)
    .diffusivityWith(density, 385 of joulesPerKilogramKelvin)

alpha into squareMillimetersPerSecond // ≈ 116.25 mm²/s
alpha into squareMetersPerSecond      // ≈ 1.1625e-4 m²/s

// 역변환: 확산율로부터 전도율 복원
alpha.conductivityWith(density, 385 of joulesPerKilogramKelvin) into wattsPerMeterKelvin // 401.0
```

## 인접 단위로 계산하기

정의 관계는 **삼항**(`α = λ / (ρ · c_p)`)이므로, 여기 있는 다른 모든 그룹과 달리 이 라이브러리가 모델링하지 않는 체적 열용량 `ρ · c_p`(J/ (m³·K))에 대한 중간 타입을 새로
만들지 않는 한 단일 이항 연산자가 될 수 없습니다. 따라서 이 관계는 이름이 붙은, 강한 타입의 함수로 노출됩니다:

| 함수                                                                 | 결과 타입                           | 의미                |
|----------------------------------------------------------------------|-------------------------------------|---------------------|
| `thermalConductivity.diffusivityWith(density, specificHeatCapacity)` | `KDiffusivityUnitInstance`          | `α = λ / (ρ · c_p)` |
| `thermalDiffusivity.conductivityWith(density, specificHeatCapacity)` | `KThermalConductivityUnitInstance`  | `λ = α · ρ · c_p`   |
| `thermalDiffusivity.densityWith(conductivity, specificHeatCapacity)` | `KDensityUnitInstance`              | `ρ = λ / (α · c_p)` |
| `thermalDiffusivity.specificHeatCapacityWith(conductivity, density)` | `KSpecificHeatCapacityUnitInstance` | `c_p = λ / (α · ρ)` |

네 함수 모두 다른 모든 분해와 동일한 정규화 팩토리로 합쳐집니다.

## 분해

두 분해 모두 동일한 타입이 지정된 값-동등 인스턴스를 생성합니다.

| 분해                 | 형식                                 | 결과                       |
|----------------------|--------------------------------------|----------------------------|
| `λ / (ρ · c_p)`      | 타입이 지정된 함수 `diffusivityWith` | `KDiffusivityUnitInstance` |
| `distance² · time⁻¹` | 네이티브 표현식 + `toDiffusivity()`  | `KDiffusivityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.toDensity
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin

// λ = 1 W/(m·K), ρ = 1 kg/m³, c_p = 1 J/(kg·K)  =>  α = 1 m²/s
val unitDensity = ((1000 of grams).toUnit() / ((1 of meters).toUnit() pow 3)).toDensity()
val typed = (1 of wattsPerMeterKelvin).diffusivityWith(unitDensity, 1 of joulesPerKilogramKelvin)
val native = (((1 of meters).toUnit() pow 2) / (1 of seconds).toUnit()).toDiffusivity()

typed == native // true - 둘 다 1.0 m²/s
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.diffusivity.*

val sum = (10 of squareMillimetersPerSecond) + (4 of squareMillimetersPerSecond) // 14 mm²/s
(10 of squareMillimetersPerSecond) > (4 of squareMillimetersPerSecond)           // true
(1 of squareMetersPerSecond) == (1_000_000 of squareMillimetersPerSecond)        // true
```

## toString 형식화

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.diffusivity.*

(111 of squareMillimetersPerSecond).toString()                                   // "1.11E-4 m²/s"
"${(111 of squareMillimetersPerSecond) into squareMillimetersPerSecond} mm²/s"   // "111.0 mm²/s"
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를 보여줍니다. 지수는 유니코드 위첨자 (`²`, `³`, `⁻¹`)를 사용하며, `·`는 곱셈을,
`/`는 분수를 나타냅니다. 어떤 양이 분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학                | Kotlin                                                  | 의미                        |
|---------------------|---------------------------------------------------------|-----------------------------|
| `m²/s`              | `squareMetersPerSecond`                                 | 열확산율, 기본 단위         |
| `m²·s⁻¹`            | `(meters pow 2) / seconds`                              | 기저 차원으로의 동일한 양   |
| `mm²/s`             | `squareMillimetersPerSecond`                            | 제곱밀리미터 매 초(물질 표) |
| `α = λ / (ρ · c_p)` | `conductivity.diffusivityWith(density, heat)`           | 정의 관계                   |
| `λ = α · ρ · c_p`   | `alpha.conductivityWith(density, heat)`                 | 확산율로부터의 전도율       |
| `ρ = λ / (α · c_p)` | `alpha.densityWith(conductivity, heat)`                 | 확산율로부터의 밀도         |
| `c_p = λ / (α · ρ)` | `alpha.specificHeatCapacityWith(conductivity, density)` | 확산율로부터의 비열         |
