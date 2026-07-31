# 열용량 (Heat Capacity)

패키지: `org.pcsoft.framework.kunit.thermo.heatcapacity`
기본 단위: **켈빈당 줄** (`KHeatCapacityUnit.BASE == KHeatCapacityUnit.JOULE_PER_KELVIN`)

유형: **구성된 단위**

열용량은 물체가 단위 온도 상승당 흡수하는 에너지입니다: `energy / temperature` (`J/K`).
`KHeatCapacityUnitInstance`는 정규 형식 `mass¹ · distance² · time⁻² · temperature⁻¹` (`kg·m²·s⁻²·K⁻¹`)의 정확히 네 항으로 이루어진
`KMixedUnitInstance`를 감싸며, 항상 J/K로 정규화됩니다.

!!! note "온도 *차*이며, 절대 온도가 아님"
온도 차원은 **차** 그룹 (`KTemperatureDifferenceUnit`, 기호 `ΔK`)이며, 아핀 절대값인
`KTemperatureUnit`이 아닙니다. 열용량은 에너지와 온도 *구간*을 관계시킵니다; 오프셋을 가진 절대 척도 (°C, °F)는 몫 연산에서 물리적으로 잘못될 것입니다.

동일한 차원 `J/K`는 **엔트로피**도 나타냅니다 — 그 양이 자체 타입이 아니라 이 타입을 공유하는 이유는
[엔트로피](entropy.md)를 참조하세요. 질량 단위당으로는 [비열](specific-heat-capacity.md)이, 몰당으로는
[몰 열용량](molar-heat-capacity.md)이 됩니다.

## 이름이 붙은 단위

| 단위          | 기호     |                토큰 |     J/K로 1 |
|---------------|----------|--------------------:|------------:|
| 켈빈당 줄     | `J/K`    |   `joulesPerKelvin` |         1.0 |
| 켈빈당 칼로리 | `cal/K`  | `caloriesPerKelvin` |       4.184 |
| 화씨도당 Btu  | `Btu/°F` | `btusPerFahrenheit` | ≈ 1899.1005 |

모두 전체 SI 접두사 범위를 지원합니다 (`kilo.joulesPerKelvin`, `kilo.caloriesPerKelvin` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

val c = 4184 of joulesPerKelvin
c into kilo.joulesPerKelvin  // 4.184
c into caloriesPerKelvin     // 1000.0
```

## 실전 예제: 주전자의 물 데우기

1리터의 물 (4184 J/K)을 20 °C에서 100 °C로 가열합니다. 얼마나 많은 에너지가 필요할까요?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.*

val kettle = 4184 of joulesPerKelvin          // 물 1리터
val rise = (100 of celsius) - (20 of celsius) // KTemperatureDifferenceUnitInstance, 80 K

val energy = kettle * rise                    // KEnergyUnitInstance
energy into joules                            // 334_720.0 J
energy into kilo.joules                       // 334.72 kJ

// ... 그리고 반대로: 100 kJ로 얼마나 도달할 수 있는가?
val reachable = (100 of kilo.joules) / kettle // KTemperatureDifferenceUnitInstance
reachable into KTemperatureDifference.ofKelvin(1) // ≈ 23.9 K
```

## 핵심 단위 (에너지 & 온도 차)로 계산하기

| 표현식                                 | 결과 타입                            | 의미                  |
|----------------------------------------|--------------------------------------|-----------------------|
| `energy / temperatureDifference`       | `KHeatCapacityUnitInstance`          | 열용량                |
| `heatCapacity * temperatureDifference` | `KEnergyUnitInstance`                | 필요한 에너지         |
| `temperatureDifference * heatCapacity` | `KEnergyUnitInstance`                | 에너지(교환 법칙)     |
| `energy / heatCapacity`                | `KTemperatureDifferenceUnitInstance` | 달성 가능한 온도 상승 |

## 분해

두 분해 모두 동일한 타입이 지정된 값-동등 인스턴스를 생성합니다.

| 분해                                        | 형식                                 | 결과                             |
|---------------------------------------------|--------------------------------------|----------------------------------|
| `energy / temperatureDifference`            | 타입이 지정된 연산자                 | `KHeatCapacityUnitInstance` 직접 |
| `mass · distance² · time⁻² · temperature⁻¹` | 네이티브 표현식 + `toHeatCapacity()` | `KHeatCapacityUnitInstance`      |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

// 타입이 지정된 연산자 형식
val typed = (1 of joules) / KTemperatureDifference.ofKelvin(1)

// 네이티브 기저 차원 형식 (kg·m²·s⁻²·K⁻¹), toHeatCapacity()가 인식
val native = (
    (1000 of grams).toUnit() *
        ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toHeatCapacity()

typed == native // true - 둘 다 1.0 J/K
```

`toHeatCapacity()`는 **오직** 정규 형식만 인식합니다. 동등한 표현식은 자동으로 이 형식으로 환원되며, 잘못된 형태는 `IllegalStateException`을 던집니다.

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

// + / - : 같은 그룹, 단위와 접두사 간 자동 변환
val total = (1 of kilo.joulesPerKelvin) + (500 of joulesPerKelvin)  // 1500 J/K
val rest  = (1 of kilo.joulesPerKelvin) - (250 of joulesPerKelvin)  // 750 J/K

// 비교(정규화된 J/K 값 기준)
(1 of kilo.joulesPerKelvin) > (500 of joulesPerKelvin)   // true
(1 of kilo.joulesPerKelvin) == (1000 of joulesPerKelvin) // true

// 두 열용량 간의 * / / 는 KMixedUnitInstance로 탈출
val squared = (2 of joulesPerKelvin) * (2 of joulesPerKelvin)
```

## toString 형식화

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

(4184 of joulesPerKelvin).toString()                          // "4184.0 J/K"
"${(4184 of joulesPerKelvin) into caloriesPerKelvin} cal/K"   // "1000.0 cal/K"
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를 보여줍니다. 지수는 유니코드 위첨자 (`²`, `³`, `⁻¹`)를 사용하며, `·`는 곱셈을,
`/`는 분수를 나타냅니다. 어떤 양이 분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학            | Kotlin                                          | 의미                                 |
|-----------------|-------------------------------------------------|--------------------------------------|
| `J/K`           | `joulesPerKelvin`                               | 열용량, 기본 단위 — 이름이 붙은 토큰 |
| `kg·m²·s⁻²·K⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / ΔK` | 기저 차원으로의 동일한 양            |
| `kJ/K`          | `kilo.joulesPerKelvin`                          | 킬로줄 매 켈빈                       |
| `cal/K`         | `caloriesPerKelvin`                             | 켈빈당 칼로리                        |
| `C = Q / ΔT`    | `(4184 of joules) / rise`                       | 에너지 ÷ 온도 상승에서 열용량        |
| `Q = C · ΔT`    | `kettle * rise`                                 | 열용량 × 온도 상승에서 에너지        |
| `ΔT = Q / C`    | `(100 of kilo.joules) / kettle`                 | 에너지 ÷ 열용량에서 온도 상승        |
