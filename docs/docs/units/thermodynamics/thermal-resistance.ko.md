# 열저항 (Thermal Resistance, R-값)

패키지: `org.pcsoft.framework.kunit.thermo.resistance`
기본 단위: **와트당 제곱미터-켈빈** (`KThermalResistanceUnit.BASE == KThermalResistanceUnit.SQUARE_METER_KELVIN_PER_WATT`)

유형: **구성된 단위**

열저항 — **R-값** — 은 어떤 층이 열의 흐름에 얼마나 강하게 저항하는지를 나타냅니다: `m²·K/W`. 이는
[열전달계수](heat-transfer-coefficient.md)(U-값)의 정확한 역수이며, 직렬로 연결된 층들의 R-값이 단순히 **합산**되기 때문에 단열 제품이 실제로 판매되는 형태이기도 합니다.

`KThermalResistanceUnitInstance`는 정규 형식 `mass⁻¹ · time³ · temperature¹` (`kg⁻¹·s³·K`)의 정확히 세 항으로 이루어진
`KMixedUnitInstance`를 감싸며, 항상 m²·K/W로 정규화됩니다.

!!! note "패키지 이름 대 클래스 이름"
패키지는 `thermo.resistance`이며, `thermo.thermalresistance`가 아닙니다 — 단위 패키지는 그 분야 패키지의 이름을 반복해서는 안 됩니다. **타입**은 전체 기술 용어
(`KThermalResistanceUnitInstance`)를 유지하며, 이것이 `electric.resistance`와 구분되는 지점입니다.

## 이름이 붙은 단위

| 단위                      | 기호           |                             토큰 | m²·K/W로 1 |
|---------------------------|----------------|---------------------------------:|-----------:|
| 와트당 제곱미터-켈빈(RSI) | `m²·K/W`       |       `squareMeterKelvinPerWatt` |        1.0 |
| 임페리얼 R-값             | `h·ft²·°F/Btu` | `hourSquareFootFahrenheitPerBtu` | ≈ 0.176110 |
| 클로                      | `clo`          |                            `clo` |      0.155 |
| 토그                      | `tog`          |                            `tog` |        0.1 |

미국의 "R-30" 단열재는 `30 of hourSquareFootFahrenheitPerBtu` ≈ 5.28 m²·K/W입니다. 정장은 대략 1 clo이며, 이불은 tog로 등급이 매겨집니다 (1 clo =
1.55 tog). 모든 단위는 전체 SI 접두사 범위를 지원합니다.

## 실전 예제: 층별로 구성된 단열 벽

벽은 20 cm 미네랄울 (λ = 0.04 W/ (m·K))과 12 cm 벽돌 (λ = 0.8 W/ (m·K))로 구성됩니다. 총 R-값, 결과적인 U-값, 그리고 ΔT = 25 K에서의 열 손실은 얼마일까요?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.wattsPerSquareMeterKelvin
import org.pcsoft.framework.kunit.thermo.resistance.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val wool  = (20 of centi.meters) / (0.04 of wattsPerMeterKelvin)  // 5.0 m²·K/W
val brick = (12 of centi.meters) / (0.8 of wattsPerMeterKelvin)   // 0.15 m²·K/W

val total = wool + brick                    // 직렬 층은 합산됩니다
total into squareMeterKelvinPerWatt         // 5.15 m²·K/W
total into hourSquareFootFahrenheitPerBtu   // ≈ 29.2 ("R-29" 벽)

val u = 1 / total                           // KHeatTransferCoefficientUnitInstance
u into wattsPerSquareMeterKelvin            // ≈ 0.194 W/(m²·K)

val drop = KTemperatureDifference.ofKelvin(25)
val flux = drop / total                     // KHeatFluxDensityUnitInstance
flux into wattsPerSquareMeter               // ≈ 4.85 W/m²

val wall = (10 of meters) * (2.5 of meters) // 25 m²
(flux * wall) into watts                    // ≈ 121 W
```

## 인접 단위로 계산하기

| 식                                          | 결과 타입                              | 의미                |
|---------------------------------------------|----------------------------------------|---------------------|
| `temperatureDifference / heatFluxDensity`   | `KThermalResistanceUnitInstance`       | 측정으로부터 R      |
| `length / thermalConductivity`              | `KThermalResistanceUnitInstance`       | 물질 + 두께로부터 R |
| `thermalResistance * heatFluxDensity`       | `KTemperatureDifferenceUnitInstance`   | 유지되는 온도 차    |
| `heatFluxDensity * thermalResistance`       | `KTemperatureDifferenceUnitInstance`   | 동일(교환 법칙)     |
| `temperatureDifference / thermalResistance` | `KHeatFluxDensityUnitInstance`         | 결과 유속           |
| `thermalResistance * thermalConductivity`   | `KLengthUnitInstance`                  | 필요한 두께         |
| `thermalConductivity * thermalResistance`   | `KLengthUnitInstance`                  | 동일(교환 법칙)     |
| `length / thermalResistance`                | `KThermalConductivityUnitInstance`     | 유도된 전도율       |
| `1 / heatTransferCoefficient`               | `KThermalResistanceUnitInstance`       | U로부터 R           |
| `1 / thermalResistance`                     | `KHeatTransferCoefficientUnitInstance` | R로부터 U           |

두 역수 연산자는 좁게 선언되어 있어, `1 / u`와 `1 / r`은 그룹에 무관한 `Number.div`가 만들어 낼 일반적인 혼합 단위가 아니라 **타입이 지정된** 값을 반환합니다.

## 분해

세 분해 모두 동일한 타입이 지정된 값-동등 인스턴스를 생성합니다.

| 분해                                      | 형식                               | 결과                             |
|-------------------------------------------|------------------------------------|----------------------------------|
| `temperatureDifference / heatFluxDensity` | 타입이 지정된 연산자               | `KThermalResistanceUnitInstance` |
| `length / thermalConductivity`            | 타입이 지정된 연산자               | `KThermalResistanceUnitInstance` |
| `mass⁻¹ · time³ · temperature¹`           | 네이티브 + `toThermalResistance()` | `KThermalResistanceUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.resistance.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaFlux      = KTemperatureDifference.ofKelvin(1) / (1 of wattsPerSquareMeter)
val viaThickness = (1 of meters) / (1 of wattsPerMeterKelvin)
val native = (
    ((1 of seconds).toUnit() pow 3) *
        KTemperatureDifference.ofKelvin(1).toUnit() /
        (1000 of grams).toUnit()
    ).toThermalResistance()

viaFlux == viaThickness // true
viaFlux == native       // true - 모두 1.0 m²·K/W
```

## 연산자

`+`와 `-`는 여기서 정확히 물리적으로 의미 있는 연산입니다: 직렬 층은 R-값을 합산합니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.resistance.*

val series = (5 of squareMeterKelvinPerWatt) + (0.15 of squareMeterKelvinPerWatt) // 5.15
(1 of squareMeterKelvinPerWatt) > (5 of tog)      // true (5 tog = 0.5 m²·K/W)
(1 of squareMeterKelvinPerWatt) == (10 of tog)    // true
```

## toString 형식화

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.resistance.*

(5 of squareMeterKelvinPerWatt).toString()                                        // "5.0 m²·K/W"
"R-${(5 of squareMeterKelvinPerWatt) into hourSquareFootFahrenheitPerBtu}"        // "R-28.39..."
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를 보여줍니다. 지수는 유니코드 위첨자 (`²`, `³`, `⁻¹`)를 사용하며, `·`는 곱셈을,
`/`는 분수를 나타냅니다. 어떤 양이 분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학                | Kotlin                                                 | 의미                      |
|---------------------|--------------------------------------------------------|---------------------------|
| `m²·K/W`            | `squareMeterKelvinPerWatt`                             | 열저항(R-값), 기본 단위   |
| `kg⁻¹·s³·K`         | `(seconds pow 3) * ΔK / grams`                         | 기저 차원으로의 동일한 양 |
| `h·ft²·°F/Btu`      | `hourSquareFootFahrenheitPerBtu`                       | 임페리얼 R-값             |
| `R = d / λ`         | `(20 of centi.meters) / (0.04 of wattsPerMeterKelvin)` | 두께 ÷ 전도율에서 R       |
| `R = ΔT / q̇`        | `drop / (4 of wattsPerSquareMeter)`                    | 온도 차 ÷ 유속에서 R      |
| `R_total = R₁ + R₂` | `wool + brick`                                         | 직렬 층                   |
| `U = 1 / R`         | `1 / total`                                            | R-값으로부터의 U-값       |
| `q̇ = ΔT / R`        | `drop / total`                                         | 온도 차 ÷ R에서의 유속    |
