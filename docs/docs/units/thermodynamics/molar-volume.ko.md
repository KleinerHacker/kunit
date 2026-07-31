# 몰 부피 (Molar Volume)

패키지: `org.pcsoft.framework.kunit.thermo.molarvolume`
기본 단위: **몰당 세제곱미터** (`KMolarVolumeUnit.BASE == KMolarVolumeUnit.CUBIC_METERS_PER_MOLE`)

유형: **구성된 단위**

몰 부피는 물질량당 부피입니다: `volume / amountOfSubstance` (`m³/mol`). 이상 기체의 경우 모든 물질에 대해 동일하며 (0 °C, 100 kPa에서 22.711 l/mol), 고체와
액체의 경우 [몰 질량](molar-mass.md)과 밀도로부터 구해집니다.

`KMolarVolumeUnitInstance`는 정규 형식 `distance³ · substance⁻¹` (`m³·mol⁻¹`)의 정확히 두 항으로 이루어진
`KMixedUnitInstance`를 감싸며, 항상 m³/mol로 정규화됩니다. 두 구성 요소 모두 각 그룹의 기본 단위로 저장되므로, 원시 구성 요소의 기저가 곧 이름이 붙은 기본 단위입니다.

[주기율표](../../periodic-table.md)의 모든 원소는 아래 두 번째 분해를 통해 몰 질량과 밀도로부터 몰 부피를 도출합니다.

## 이름이 붙은 단위

| 단위                | 기호       |                      토큰 | m³/mol로 1 |
|---------------------|------------|--------------------------:|-----------:|
| 몰당 세제곱미터     | `m^3/mol`  |      `cubicMetersPerMole` |        1.0 |
| 몰당 리터           | `l/mol`    |           `litersPerMole` |      0.001 |
| 몰당 세제곱센티미터 | `cm^3/mol` | `cubicCentimetersPerMole` |     1.0e-6 |

모든 단위는 전체 SI 접두사 범위를 지원합니다 (`milli.cubicMetersPerMole`, `milli.litersPerMole` 등). 이 패키지는 추가로 표준 상태에서 이상 기체의 몰 부피인 상수
`MOLAR_VOLUME_IDEAL_GAS_STP` = 0.02271095464 (m³/mol)를 노출합니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val ideal = MOLAR_VOLUME_IDEAL_GAS_STP of cubicMetersPerMole
ideal into litersPerMole          // ≈ 22.711
ideal into cubicCentimetersPerMole // ≈ 22711.0
```

## 실전 예제: 헬륨으로 가득 찬 풍선

표준 상태에서 이상 기체 2몰은 얼마의 공간을 차지하며, 5리터 풍선에는 몇 몰이 들어갈까요?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val ideal = MOLAR_VOLUME_IDEAL_GAS_STP of cubicMetersPerMole

// 2몰의 부피
val volume = ideal * (2 of moles) // KVolumeUnitInstance
volume into liters                // ≈ 45.42 l

// 5 l 풍선에는 몇 몰이 들어가는가?
val amount = (5 of liters) / ideal // KAmountOfSubstanceUnitInstance
amount into moles                  // ≈ 0.2202 mol

// 그리고 채워진 풍선으로부터 측정한 몰 부피:
val measured = (45.42 of liters) / (2 of moles)
measured into litersPerMole        // ≈ 22.71
```

## 실전 예제: 물 1몰의 부피

물의 몰 질량은 18.015 g/mol이고 밀도는 1 kg/l이므로, 1몰은 약 18 cm³ — 큰술 하나 분량 — 를 차지합니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val density = (1 of kilo.grams) / (1 of liters)      // KDensityUnitInstance
val molarVolume = (18.015 of gramsPerMole) / density // KMolarVolumeUnitInstance
molarVolume into cubicCentimetersPerMole             // 18.015
```

## 핵심 단위로 계산하기

| 표현식                            | 결과 타입                        | 의미                     |
|-----------------------------------|----------------------------------|--------------------------|
| `volume / amountOfSubstance`      | `KMolarVolumeUnitInstance`       | 몰 부피                  |
| `molarMass / density`             | `KMolarVolumeUnitInstance`       | 몰 부피(두 번째 분해)    |
| `molarVolume * amountOfSubstance` | `KVolumeUnitInstance`            | 총 부피                  |
| `amountOfSubstance * molarVolume` | `KVolumeUnitInstance`            | 총 부피(교환 법칙)       |
| `volume / molarVolume`            | `KAmountOfSubstanceUnitInstance` | 포함된 물질량            |
| `molarVolume * density`           | `KMolarMassUnitInstance`         | [몰 질량](molar-mass.md) |
| `density * molarVolume`           | `KMolarMassUnitInstance`         | 몰 질량(교환 법칙)       |
| `molarMass / molarVolume`         | `KDensityUnitInstance`           | 밀도                     |

## 분해

모든 분해가 동일한 타입이 지정된 값-동등 인스턴스를 생성합니다.

| 분해                         | 형식                                | 결과                            |
|------------------------------|-------------------------------------|---------------------------------|
| `volume / amountOfSubstance` | 타입이 지정된 연산자                | `KMolarVolumeUnitInstance` 직접 |
| `molarMass / density`        | 타입이 지정된 연산자                | `KMolarVolumeUnitInstance` 직접 |
| `distance³ · substance⁻¹`    | 네이티브 표현식 + `toMolarVolume()` | `KMolarVolumeUnitInstance`      |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molarvolume.*

// 타입이 지정된 연산자 형식: 부피 / 물질량
val typedVolume = (0.018015 of liters) / (1 of moles)

// 타입이 지정된 연산자 형식: 몰 질량 / 밀도
val typedMolarMass = (18.015 of gramsPerMole) / ((1 of kilo.grams) / (1 of liters))

// 네이티브 기저 차원 형식 (m³·mol⁻¹), toMolarVolume()가 인식
val native = (((18.015e-6 of (meters pow 3)).toUnit()) / (1 of moles).toUnit()).toMolarVolume()

typedVolume == typedMolarMass // true
typedVolume == native         // true - 모두 1.8015e-5 m³/mol
```

`toMolarVolume()`는 **오직** 정규 형식만 인식하며, 잘못된 형태는 `IllegalStateException`을 던집니다.

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val total = (10 of litersPerMole) + (4 of litersPerMole) // 14 l/mol
val rest  = (10 of litersPerMole) - (4 of litersPerMole) // 6 l/mol

(1 of litersPerMole) > (500 of cubicCentimetersPerMole)   // true
(1 of litersPerMole) == (1000 of cubicCentimetersPerMole) // true
```

## toString 형식화

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarvolume.*

(1 of litersPerMole).toString()    // "0.001 m^3/mol"
(22.4 of litersPerMole).toString() // "0.0224 m^3/mol"
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를 보여줍니다. 지수는 유니코드 위첨자 (`²`, `³`, `⁻¹`)를 사용하며, `·`는 곱셈을,
`/`는 분수를 나타냅니다. 어떤 양이 분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학          | Kotlin                               | 의미                                  |
|---------------|--------------------------------------|---------------------------------------|
| `m³/mol`      | `cubicMetersPerMole`                 | 몰 부피, 기본 단위 — 이름이 붙은 토큰 |
| `m³·mol⁻¹`    | `(meters pow 3) / moles`             | 기저 차원으로의 동일한 양             |
| `l/mol`       | `litersPerMole`                      | 몰당 리터                             |
| `cm³/mol`     | `cubicCentimetersPerMole`            | 몰당 세제곱센티미터                   |
| `V_m = V / n` | `(45.42 of liters) / (2 of moles)`   | 부피 ÷ 물질량에서 몰 부피             |
| `V_m = M / ρ` | `(18.015 of gramsPerMole) / density` | 몰 질량 ÷ 밀도에서 몰 부피            |
| `V = V_m · n` | `ideal * (2 of moles)`               | 몰 부피 × 물질량에서 부피             |
| `n = V / V_m` | `(5 of liters) / ideal`              | 부피 ÷ 몰 부피에서 물질량             |
| `ρ = M / V_m` | `molarMass / molarVolume`            | 몰 질량 ÷ 몰 부피에서 밀도            |
