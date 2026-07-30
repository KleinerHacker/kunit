# 몰 열용량 (Molar Heat Capacity)

패키지: `org.pcsoft.framework.kunit.thermo.molarheatcapacity`
기본 단위: **몰-켈빈당 줄** (`KMolarHeatCapacityUnit.BASE == KMolarHeatCapacityUnit.JOULE_PER_MOLE_KELVIN`)

유형: **구성된 단위**

몰 열용량은 물질의 [열용량](heat-capacity.md)을 *몰당*으로 나타낸 것입니다: `J/(mol·K)`. 이는 기체와
화학 열역학의 자연스러운 형태이며, 이 분야에서는 양을 킬로그램이 아니라 몰로 셉니다
(킬로그램 단위는 [비열](specific-heat-capacity.md)입니다).

`KMolarHeatCapacityUnitInstance`는 정규 형식 `mass¹ · distance² · time⁻² · substance⁻¹ · temperature⁻¹`
(`kg·m²·s⁻²·mol⁻¹·K⁻¹`)의 정확히 다섯 항으로 이루어진 `KMixedUnitInstance`를 감쌉니다. 온도 차원은
**차** 그룹이며, 절대 온도가 아닙니다.

## 이름이 붙은 단위

| 단위 | 기호 | 토큰 | J/(mol·K)로 1 |
|---|---|---:|---:|
| 몰-켈빈당 줄 | `J/(mol·K)` | `joulesPerMoleKelvin` | 1.0 |
| 몰-켈빈당 칼로리 | `cal/(mol·K)` | `caloriesPerMoleKelvin` | 4.184 |

둘 다 전체 SI 접두사 범위를 지원합니다(`kilo.joulesPerMoleKelvin`, `milli.joulesPerMoleKelvin` 등).

## 기체 상수

이 그룹은 몰 기체 상수의 정확한 SI 값을 `GAS_CONSTANT`(8.31446261815324 J/(mol·K))로 노출합니다 —
순수한 `Double`이므로 계수로도, 값으로도 사용할 수 있습니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*

val r = GAS_CONSTANT of joulesPerMoleKelvin
r into joulesPerMoleKelvin   // 8.31446261815324
r into caloriesPerMoleKelvin // ≈ 1.987
```

## 실전 예제: 질소 가열(뒬롱-프티 법칙 확인)

이원자 질소는 `c_p ≈ 29.1 J/(mol·K)`입니다. 3몰을 50 K 가열하는 데 얼마의 에너지가 필요하며, 몰당으로는
얼마인가요?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.molarenergy.joulesPerMole
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val nitrogen = 29.1 of joulesPerMoleKelvin
val sample = 3 of moles
val rise = KTemperatureDifference.ofKelvin(50)

// 경로 1: 시료의 열용량을 먼저 구한 다음 에너지를 구함
val sampleCapacity = nitrogen * sample     // KHeatCapacityUnitInstance
sampleCapacity into joulesPerKelvin        // 87.3 J/K
val energy = sampleCapacity * rise         // KEnergyUnitInstance
energy into joules                         // 4365.0 J

// 경로 2: 몰당으로 먼저 구함
val perMole = nitrogen * rise              // KMolarEnergyUnitInstance
perMole into joulesPerMole                 // 1455.0 J/mol
val sameEnergy = perMole * sample          // KEnergyUnitInstance
sameEnergy into joules                     // 4365.0 J - 동일함
```

## 인접 단위로 계산하기

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `heatCapacity / amountOfSubstance` | `KMolarHeatCapacityUnitInstance` | 시료로부터 물질 속성 |
| `molarEnergy / temperatureDifference` | `KMolarHeatCapacityUnitInstance` | 몰 에너지를 통한 동일값 |
| `molarHeatCapacity * amountOfSubstance` | `KHeatCapacityUnitInstance` | 시료의 열용량 |
| `amountOfSubstance * molarHeatCapacity` | `KHeatCapacityUnitInstance` | 동일(교환 법칙) |
| `heatCapacity / molarHeatCapacity` | `KAmountOfSubstanceUnitInstance` | 물질량 |
| `molarHeatCapacity * temperatureDifference` | `KMolarEnergyUnitInstance` | 몰당 에너지 |
| `temperatureDifference * molarHeatCapacity` | `KMolarEnergyUnitInstance` | 동일(교환 법칙) |
| `molarEnergy / molarHeatCapacity` | `KTemperatureDifferenceUnitInstance` | 달성 가능한 상승 |

## 분해

세 분해 모두 동일한 타입이 지정된 값-동등 인스턴스를 생성합니다.

| 분해 | 형식 | 결과 |
|---|---|---|
| `heatCapacity / amountOfSubstance` | 타입이 지정된 연산자 | `KMolarHeatCapacityUnitInstance` |
| `molarEnergy / temperatureDifference` | 타입이 지정된 연산자 | `KMolarHeatCapacityUnitInstance` |
| `mass · distance² · time⁻² · substance⁻¹ · temperature⁻¹` | 네이티브 + `toMolarHeatCapacity()` | `KMolarHeatCapacityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.molarenergy.joulesPerMole
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaHeatCapacity = (1 of joulesPerKelvin) / (1 of moles)
val viaMolarEnergy  = (1 of joulesPerMole) / KTemperatureDifference.ofKelvin(1)
val native = (
    (1000 of grams).toUnit() *
        ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        (1 of moles).toUnit() /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toMolarHeatCapacity()

viaHeatCapacity == viaMolarEnergy // true
viaHeatCapacity == native         // true - 모두 1.0 J/(mol·K)
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*

val total = (1 of kilo.joulesPerMoleKelvin) + (500 of joulesPerMoleKelvin)  // 1500 J/(mol·K)
(1 of kilo.joulesPerMoleKelvin) > (500 of joulesPerMoleKelvin)              // true
(1 of kilo.joulesPerMoleKelvin) == (1000 of joulesPerMoleKelvin)            // true
```

## toString 형식화

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*

(29.1 of joulesPerMoleKelvin).toString()                                     // "29.1 J/(mol·K)"
"${(29.1 of joulesPerMoleKelvin) into caloriesPerMoleKelvin} cal/(mol·K)"    // "6.955... cal/(mol·K)"
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를
보여줍니다. 지수는 유니코드 위첨자(`²`, `³`, `⁻¹`)를 사용하며, `·`는 곱셈을, `/`는 분수를 나타냅니다.
어떤 양이 분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두
나열됩니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `J/(mol·K)` | `joulesPerMoleKelvin` | 몰 열용량, 기본 단위 |
| `kg·m²·s⁻²·mol⁻¹·K⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / moles / ΔK` | 기저 차원 |
| `cal/(mol·K)` | `caloriesPerMoleKelvin` | 몰-켈빈당 칼로리 |
| `R` | `GAS_CONSTANT of joulesPerMoleKelvin` | 몰 기체 상수, 8.3145 J/(mol·K) |
| `C_m = C / n` | `(58.2 of joulesPerKelvin) / (2 of moles)` | 열용량 ÷ 물질량으로부터 |
| `C_m = ΔH_m / ΔT` | `(58.2 of joulesPerMole) / rise` | 몰 에너지 ÷ 온도 상승으로부터 |
| `Q = C_m · n · ΔT` | `nitrogen * sample * rise` | 총 에너지 |
