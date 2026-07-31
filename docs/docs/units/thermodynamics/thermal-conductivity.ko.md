# 열전도율 (Thermal Conductivity)

패키지: `org.pcsoft.framework.kunit.thermo.conductivity`
기본 단위: **미터-켈빈당 와트** (`KThermalConductivityUnit.BASE == KThermalConductivityUnit.WATT_PER_METER_KELVIN`)

유형: **구성된 단위**

열전도율 `λ`(`k`로도 표기)는 푸리에 법칙에 등장하는 물질 속성입니다: 물질을 통과하는
[열유속밀도](heat-flux-density.md)는 전도율에 [온도 구배](temperature-gradient.md)를 곱한 값과 같습니다. 단위: `W/(m·K)`.

`KThermalConductivityUnitInstance`는 정규 형식 `mass¹ · distance¹ · time⁻³ · temperature⁻¹`
(`kg·m·s⁻³·K⁻¹`)의 정확히 네 항으로 이루어진 `KMixedUnitInstance`를 감싸며, 항상 W/ (m·K)로 정규화됩니다.

!!! note "패키지 이름 대 클래스 이름"
패키지는 `thermo.conductivity`이며, `thermo.thermalconductivity`가 아닙니다 — 단위 패키지는 그 분야 패키지의 이름을 반복해서는 안 됩니다. **타입**은 전체 기술 용어
(`KThermalConductivityUnitInstance`)를 유지하며, 이것이 `electric.conductivity`와 구분되는 지점입니다.

두께로 나누면 이는 [열전달계수](heat-transfer-coefficient.md)가 되며, 그것으로 두께를 나누면
[열저항](thermal-resistance.md)(R-값)이 됩니다.

## 이름이 붙은 단위

| 단위                   | 기호            |                                토큰 | W/(m·K)로 1 |
|------------------------|-----------------|------------------------------------:|------------:|
| 미터-켈빈당 와트       | `W/(m·K)`       |               `wattsPerMeterKelvin` |         1.0 |
| 시간-피트-화씨도당 Btu | `Btu/(h·ft·°F)` |         `btusPerHourFootFahrenheit` |  ≈ 1.730735 |
| 초-cm-켈빈당 칼로리    | `cal/(s·cm·K)`  | `caloriesPerSecondCentimeterKelvin` |       418.4 |

모두 전체 SI 접두사 범위를 지원합니다 — 단열재는 자연스럽게 `40 of milli.wattsPerMeterKelvin`처럼 표기됩니다.

## 전형적인 값

| 물질     |                            λ |
|----------|-----------------------------:|
| 구리     |                  401 W/(m·K) |
| 강철     |                 ≈ 50 W/(m·K) |
| 유리     |                  ≈ 1 W/(m·K) |
| 미네랄울 | ≈ 0.04 W/(m·K) = 40 mW/(m·K) |

## 실전 예제: 단열된 벽을 통한 열 손실

30 cm 두께의 미네랄울 층 (λ = 0.04 W/ (m·K))이 21 °C의 실내와 −5 °C의 실외 공기를 나눕니다. 벽 면적은 12 m²입니다. 얼마나 많은 열이 손실될까요?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.conductivity.*
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.temperature.celsius
import org.pcsoft.framework.kunit.thermo.temperaturegradient.kelvinPerMeter

val wool = 40 of milli.wattsPerMeterKelvin      // 0.04 W/(m·K)
val thickness = 30 of centi.meters
val drop = (21 of celsius) - (-5 of celsius)    // 26 K

val gradient = drop / thickness                 // KTemperatureGradientUnitInstance, ≈ 86.7 K/m
gradient into kelvinPerMeter                    // 86.666...

val flux = wool * gradient                      // KHeatFluxDensityUnitInstance (푸리에 법칙)
flux into wattsPerSquareMeter                   // ≈ 3.47 W/m²

val wall = (4 of meters) * (3 of meters)        // 12 m²
val loss = flux * wall                          // KPowerUnitInstance
loss into watts                                 // ≈ 41.6 W
```

## 인접 단위로 계산하기

| 식                                          | 결과 타입                          | 의미                    |
|---------------------------------------------|------------------------------------|-------------------------|
| `heatFluxDensity / temperatureGradient`     | `KThermalConductivityUnitInstance` | λ에 대해 푼 푸리에 법칙 |
| `thermalConductivity * temperatureGradient` | `KHeatFluxDensityUnitInstance`     | 푸리에 법칙             |
| `temperatureGradient * thermalConductivity` | `KHeatFluxDensityUnitInstance`     | 동일(교환 법칙)         |
| `heatFluxDensity / thermalConductivity`     | `KTemperatureGradientUnitInstance` | 유도된 구배             |

## 분해

두 분해 모두 동일한 타입이 지정된 값-동등 인스턴스를 생성합니다.

| 분해                                       | 형식                                 | 결과                               |
|--------------------------------------------|--------------------------------------|------------------------------------|
| `heatFluxDensity / temperatureGradient`    | 타입이 지정된 연산자                 | `KThermalConductivityUnitInstance` |
| `mass · distance · time⁻³ · temperature⁻¹` | 네이티브 + `toThermalConductivity()` | `KThermalConductivityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.*
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperaturegradient.kelvinPerMeter

val typed = (1 of wattsPerSquareMeter) / (1 of kelvinPerMeter)
val native = (
    (1000 of grams).toUnit() *
        (1 of meters).toUnit() /
        ((1 of seconds).toUnit() pow 3) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toThermalConductivity()

typed == native // true - 둘 다 1.0 W/(m·K)
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.conductivity.*

val total = (1 of kilo.wattsPerMeterKelvin) + (500 of wattsPerMeterKelvin)  // 1500 W/(m·K)
(1 of kilo.wattsPerMeterKelvin) > (500 of wattsPerMeterKelvin)              // true
(1 of kilo.wattsPerMeterKelvin) == (1000 of wattsPerMeterKelvin)            // true
```

## toString 형식화

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.conductivity.*

(401 of wattsPerMeterKelvin).toString()                                          // "401.0 W/(m·K)"
"${(401 of wattsPerMeterKelvin) into btusPerHourFootFahrenheit} Btu/(h·ft·°F)"   // "231.7..."
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를 보여줍니다. 지수는 유니코드 위첨자 (`²`, `³`, `⁻¹`)를 사용하며, `·`는 곱셈을,
`/`는 분수를 나타냅니다. 어떤 양이 분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학           | Kotlin                                   | 의미                        |
|----------------|------------------------------------------|-----------------------------|
| `W/(m·K)`      | `wattsPerMeterKelvin`                    | 열전도율, 기본 단위         |
| `kg·m·s⁻³·K⁻¹` | `grams * meters / (seconds pow 3) / ΔK`  | 기저 차원으로의 동일한 양   |
| `mW/(m·K)`     | `milli.wattsPerMeterKelvin`              | 밀리와트 매 미터-켈빈(단열) |
| `q̇ = λ · ∇T`   | `wool * gradient`                        | 푸리에 법칙                 |
| `λ = q̇ / ∇T`   | `(80 of wattsPerSquareMeter) / gradient` | 유속 ÷ 구배에서 전도율      |
| `∇T = q̇ / λ`   | `flux / wool`                            | 유속 ÷ 전도율에서 구배      |
