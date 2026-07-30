# 열전달계수 (Heat Transfer Coefficient)

패키지: `org.pcsoft.framework.kunit.thermo.heattransfercoefficient`
기본 단위: **제곱미터-켈빈당 와트** (`KHeatTransferCoefficientUnit.BASE == KHeatTransferCoefficientUnit.WATT_PER_SQUARE_METER_KELVIN`)

유형: **구성된 단위**

열전달계수 — 건축 물리학에서의 **U-값** — 는 부재가 온도 차 1켈빈당 통과시키는 열유속밀도입니다:
`W/(m²·K)`. U-값이 낮을수록 단열 성능이 좋습니다.

`KHeatTransferCoefficientUnitInstance`는 정규 형식 `mass¹ · time⁻³ · temperature⁻¹` (`kg·s⁻³·K⁻¹`)의
정확히 세 항으로 이루어진 `KMixedUnitInstance`를 감싸며, 항상 W/(m²·K)로 정규화됩니다.
[열유속밀도](heat-flux-density.md)와 마찬가지로 면적이 와트의 길이 차원을 상쇄하므로, 정규 형식에는
거리 항이 없습니다.

그 역수는 [열저항](thermal-resistance.md)(R-값)이며, 두께를 곱하면 [열전도율](thermal-conductivity.md)이
됩니다.

## 이름이 붙은 단위

| 단위 | 기호 | 토큰 | W/(m²·K)로 1 |
|---|---|---:|---:|
| 제곱미터-켈빈당 와트 | `W/(m²·K)` | `wattsPerSquareMeterKelvin` | 1.0 |
| 시간-제곱피트-화씨도당 Btu | `Btu/(h·ft²·°F)` | `btusPerHourSquareFootFahrenheit` | ≈ 5.678263 |
| 초-cm²-켈빈당 칼로리 | `cal/(s·cm²·K)` | `caloriesPerSecondSquareCentimeterKelvin` | 41840.0 |

모두 전체 SI 접두사 범위를 지원합니다(`milli.wattsPerSquareMeterKelvin` 등).

## 전형적인 U-값

| 부재 | U |
|---|---:|
| 단창 유리 | ≈ 5.8 W/(m²·K) |
| 복층 유리 | ≈ 2.8 W/(m²·K) |
| 삼중 유리 | ≈ 0.7 … 1.3 W/(m²·K) |
| 패시브 하우스 벽 | ≈ 0.15 W/(m²·K) |

## 실전 예제: 창문을 통한 열 손실

2.4 m² 삼중 유리 창문의 U = 1.3 W/(m²·K)입니다. 실내는 21 °C, 실외는 1 °C입니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*
import org.pcsoft.framework.kunit.thermo.temperature.celsius

val window = 1.3 of wattsPerSquareMeterKelvin
val drop = (21 of celsius) - (1 of celsius)      // 20 K
val glass = (2 of meters) * (1.2 of meters)      // 2.4 m²

val flux = window * drop                          // KHeatFluxDensityUnitInstance
flux into wattsPerSquareMeter                     // 26.0 W/m²

val loss = flux * glass                           // KPowerUnitInstance
loss into watts                                   // 62.4 W

// 단창 유리라면 얼마나 손해일까?
val single = 5.8 of wattsPerSquareMeterKelvin
((single * drop) * glass) into watts              // 278.4 W - 4.5배나 많음
```

## 인접 단위로 계산하기

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `heatFluxDensity / temperatureDifference` | `KHeatTransferCoefficientUnitInstance` | 측정으로부터 U-값 |
| `thermalConductivity / length` | `KHeatTransferCoefficientUnitInstance` | 물질 + 두께로부터 U-값 |
| `heatTransferCoefficient * temperatureDifference` | `KHeatFluxDensityUnitInstance` | 부재를 통과하는 유속 |
| `temperatureDifference * heatTransferCoefficient` | `KHeatFluxDensityUnitInstance` | 동일(교환 법칙) |
| `heatFluxDensity / heatTransferCoefficient` | `KTemperatureDifferenceUnitInstance` | 구동 온도 차 |
| `heatTransferCoefficient * length` | `KThermalConductivityUnitInstance` | 물질 전도율 |
| `length * heatTransferCoefficient` | `KThermalConductivityUnitInstance` | 동일(교환 법칙) |
| `thermalConductivity / heatTransferCoefficient` | `KLengthUnitInstance` | 필요한 두께 |

## 분해

세 분해 모두 동일한 타입이 지정된 값-동등 인스턴스를 생성합니다.

| 분해 | 형식 | 결과 |
|---|---|---|
| `heatFluxDensity / temperatureDifference` | 타입이 지정된 연산자 | `KHeatTransferCoefficientUnitInstance` |
| `thermalConductivity / length` | 타입이 지정된 연산자 | `KHeatTransferCoefficientUnitInstance` |
| `mass · time⁻³ · temperature⁻¹` | 네이티브 + `toHeatTransferCoefficient()` | `KHeatTransferCoefficientUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaFlux         = (1 of wattsPerSquareMeter) / KTemperatureDifference.ofKelvin(1)
val viaConductivity = (1 of wattsPerMeterKelvin) / (1 of meters)
val native = (
    (1000 of grams).toUnit() /
        ((1 of seconds).toUnit() pow 3) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toHeatTransferCoefficient()

viaFlux == viaConductivity // true
viaFlux == native          // true - 모두 1.0 W/(m²·K)
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*

val total = (1 of kilo.wattsPerSquareMeterKelvin) + (500 of wattsPerSquareMeterKelvin)  // 1500
(1 of kilo.wattsPerSquareMeterKelvin) > (500 of wattsPerSquareMeterKelvin)              // true
(1 of kilo.wattsPerSquareMeterKelvin) == (1000 of wattsPerSquareMeterKelvin)            // true
```

## toString 형식화

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*

(1.3 of wattsPerSquareMeterKelvin).toString()                                             // "1.3 W/(m²·K)"
"${(1.3 of wattsPerSquareMeterKelvin) into btusPerHourSquareFootFahrenheit} Btu/(h·ft²·°F)" // "0.229..."
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를
보여줍니다. 지수는 유니코드 위첨자(`²`, `³`, `⁻¹`)를 사용하며, `·`는 곱셈을, `/`는 분수를 나타냅니다.
어떤 양이 분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두
나열됩니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `W/(m²·K)` | `wattsPerSquareMeterKelvin` | 열전달계수(U-값), 기본 단위 |
| `kg·s⁻³·K⁻¹` | `grams / (seconds pow 3) / ΔK` | 기저 차원으로의 동일한 양 |
| `U = q̇ / ΔT` | `(26 of wattsPerSquareMeter) / drop` | 유속 ÷ 온도 차에서 U-값 |
| `U = λ / d` | `(0.04 of wattsPerMeterKelvin) / (0.2 of meters)` | 전도율 ÷ 두께에서 U-값 |
| `q̇ = U · ΔT` | `window * drop` | U-값 × 온도 차에서 유속 |
| `P = U · A · ΔT` | `(window * drop) * glass` | 총 열 손실 |
