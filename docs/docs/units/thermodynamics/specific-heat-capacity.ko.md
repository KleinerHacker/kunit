# 비열 (Specific Heat Capacity)

패키지: `org.pcsoft.framework.kunit.thermo.specificheatcapacity`
기본 단위: **킬로그램-켈빈당 줄** (`KSpecificHeatCapacityUnit.BASE == KSpecificHeatCapacityUnit.JOULE_PER_KILOGRAM_KELVIN`)

유형: **구성된 단위**

비열은 물질의 [열용량](heat-capacity.md)을 *단위 질량당*으로 나타낸 것입니다: `J/(kg·K)`. 이는
"이것을 데우는 데 얼마의 에너지가 필요한가"라는 모든 계산의 배후에 있는 물질 속성입니다.

`KSpecificHeatCapacityUnitInstance`는 정규 형식 `distance² · time⁻² · temperature⁻¹` (`m²·s⁻²·K⁻¹`)의 정확히 세 항으로 이루어진
`KMixedUnitInstance`를 감쌉니다 — [비에너지](specific-energy.md)와 정확히 동일하게 질량 차원이 상쇄됩니다. 온도 차원은 **차** 그룹
(`KTemperatureDifferenceUnit`)이며, 아핀 절대 온도가 아닙니다.

## 이름이 붙은 단위

| 단위                | 기호          |                      토큰 | J/(kg·K)로 1 |
|---------------------|---------------|--------------------------:|-------------:|
| 킬로그램-켈빈당 줄  | `J/(kg·K)`    | `joulesPerKilogramKelvin` |          1.0 |
| 그램-켈빈당 칼로리  | `cal/(g·K)`   |   `caloriesPerGramKelvin` |       4184.0 |
| 파운드-화씨도당 Btu | `Btu/(lb·°F)` |  `btusPerPoundFahrenheit` |       4186.8 |

모두 전체 SI 접두사 범위를 지원합니다 (`kilo.joulesPerKilogramKelvin` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*

val water = 4184 of joulesPerKilogramKelvin
water into caloriesPerGramKelvin   // 1.0 (칼로리의 정의상 물은 1 cal/(g·K))
```

## 실전 예제: 욕조 데우기

150리터의 물 (150 kg)을 12 °C에서 40 °C로 가열합니다. 물의 비열은 4184 J/ (kg·K)입니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.*

val water = 4184 of joulesPerKilogramKelvin
val bath = 150 of kilo.grams
val rise = (40 of celsius) - (12 of celsius)  // 28 K

// 경로 1: 먼저 욕조의 열용량을 구성
val tubCapacity = water * bath                // KHeatCapacityUnitInstance
tubCapacity into joulesPerKelvin              // 627_600.0 J/K
val energy = tubCapacity * rise               // KEnergyUnitInstance
energy into mega.joules                       // ≈ 17.57 MJ

// 경로 2: 대신 비에너지(킬로그램당 에너지)를 거쳐감
val perKilogram = water * rise                // KSpecificEnergyUnitInstance, 117_152 J/kg
val sameEnergy = perKilogram * bath           // KEnergyUnitInstance
sameEnergy into mega.joules                   // ≈ 17.57 MJ - 동일함
```

## 인접 단위로 계산하기

| 표현식                                         | 결과 타입                            | 의미                   |
|------------------------------------------------|--------------------------------------|------------------------|
| `heatCapacity / mass`                          | `KSpecificHeatCapacityUnitInstance`  | 물체로부터의 물질 속성 |
| `specificEnergy / temperatureDifference`       | `KSpecificHeatCapacityUnitInstance`  | 비에너지를 통한 동일값 |
| `specificHeatCapacity * mass`                  | `KHeatCapacityUnitInstance`          | 물체의 열용량          |
| `mass * specificHeatCapacity`                  | `KHeatCapacityUnitInstance`          | 동일(교환 법칙)        |
| `heatCapacity / specificHeatCapacity`          | `KMassUnitInstance`                  | 물체의 질량            |
| `specificHeatCapacity * temperatureDifference` | `KSpecificEnergyUnitInstance`        | 킬로그램당 에너지      |
| `temperatureDifference * specificHeatCapacity` | `KSpecificEnergyUnitInstance`        | 동일(교환 법칙)        |
| `specificEnergy / specificHeatCapacity`        | `KTemperatureDifferenceUnitInstance` | 달성 가능한 상승       |

## 분해

세 분해 모두 동일한 타입이 지정된 값-동등 인스턴스를 생성합니다.

| 분해                                     | 형식                                         | 결과                                |
|------------------------------------------|----------------------------------------------|-------------------------------------|
| `heatCapacity / mass`                    | 타입이 지정된 연산자                         | `KSpecificHeatCapacityUnitInstance` |
| `specificEnergy / temperatureDifference` | 타입이 지정된 연산자                         | `KSpecificHeatCapacityUnitInstance` |
| `distance² · time⁻² · temperature⁻¹`     | 네이티브 표현식 + `toSpecificHeatCapacity()` | `KSpecificHeatCapacityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaHeatCapacity   = (1 of joulesPerKelvin) / (1 of kilo.grams)
val viaSpecificEnergy = (1 of joulesPerKilogram) / KTemperatureDifference.ofKelvin(1)
val native = (
    ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toSpecificHeatCapacity()

viaHeatCapacity == viaSpecificEnergy // true
viaHeatCapacity == native            // true - 모두 1.0 J/(kg·K)
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*

val total = (1 of kilo.joulesPerKilogramKelvin) + (500 of joulesPerKilogramKelvin)  // 1500
(1 of kilo.joulesPerKilogramKelvin) > (500 of joulesPerKilogramKelvin)              // true
(1 of kilo.joulesPerKilogramKelvin) == (1000 of joulesPerKilogramKelvin)            // true
```

## toString 형식화

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*

(4184 of joulesPerKilogramKelvin).toString()                                // "4184.0 J/(kg·K)"
"${(4184 of joulesPerKilogramKelvin) into caloriesPerGramKelvin} cal/(g·K)" // "1.0 cal/(g·K)"
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를 보여줍니다. 지수는 유니코드 위첨자 (`²`, `³`, `⁻¹`)를 사용하며, `·`는 곱셈을,
`/`는 분수를 나타냅니다. 어떤 양이 분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학             | Kotlin                                          | 의미                          |
|------------------|-------------------------------------------------|-------------------------------|
| `J/(kg·K)`       | `joulesPerKilogramKelvin`                       | 비열, 기본 단위               |
| `m²·s⁻²·K⁻¹`     | `(meters pow 2) / (seconds pow 2) / ΔK`         | 기저 차원으로의 동일한 양     |
| `cal/(g·K)`      | `caloriesPerGramKelvin`                         | 그램-켈빈당 칼로리            |
| `c = C / m`      | `(4184 of joulesPerKelvin) / (1 of kilo.grams)` | 열용량 ÷ 질량으로부터         |
| `c = q / ΔT`     | `(8368 of joulesPerKilogram) / rise`            | 비에너지 ÷ 온도 상승으로부터  |
| `C = c · m`      | `water * bath`                                  | 물질 × 질량에서 물체의 열용량 |
| `Q = c · m · ΔT` | `water * bath * rise`                           | 총 에너지                     |
