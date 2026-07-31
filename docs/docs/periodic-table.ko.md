# 주기율표 (Periodic Table)

패키지: `org.pcsoft.framework.kunit`
타입: `KChemicalElement`, `KChemicalElementCategory`

`KChemicalElement`는 화학 원소를 위한 중심 장소입니다. 이는 순수한 Kotlin enum이므로, 모든 원소는 컴파일 타임 상수이며 — 그것이 담고 있는 모든 물리 상수는 이 라이브러리의 **타입이
지정된 단위 인스턴스**로, 다른 모든 것과 즉시 조합할 준비가 되어 있습니다.

## 범위

이 enum은 고전적인 학교용 주기율표를 다룹니다: f-블록을 제외한 **1-6주기의 주족 및 부족**입니다. 따라서 란탄족 (57-71)은 빠져 있으며 — 원자 번호는 바륨 (56)에서 하프늄 (72)으로
건너뜁니다 — 악티늄족과 초악티늄족도 포함되지 않습니다. 그 결과 총 71개 항목이 됩니다.

## 위치 데이터

| 속성            | 타입                       | 의미                                         |
|-----------------|----------------------------|----------------------------------------------|
| `ordinalNumber` | `Int`                      | 원자 번호 Z, 주기율표에서의 인덱스           |
| `symbol`        | `String`                   | 원소 기호, 예: `"Pb"`                        |
| `fullName`      | `String`                   | 영어 이름, 예: `"Lead"`(enum 항목은 `LEAD`)  |
| `period`        | `Int`                      | 주기(행), 1-6                                |
| `mainGroup`     | `Int?`                     | s/p-블록 원소의 주족 1-8, 전이 금속은 `null` |
| `subGroup`      | `Int?`                     | d-블록 원소의 부족 1-8, 그 외에는 `null`     |
| `category`      | `KChemicalElementCategory` | 화학족                                       |

`mainGroup`과 `subGroup` 중 정확히 하나만 설정됩니다. 부족은 고전적인 번호 체계를 사용합니다 (Cu = 1, Zn = 2, Sc = 3 … Fe/Co/Ni = 8).

`KChemicalElementCategory`는 `HYDROGEN`, `ALKALI_METAL`, `ALKALINE_EARTH_METAL`,
`TRANSITION_METAL`, `POST_TRANSITION_METAL`, `METALLOID`, `NONMETAL`, `HALOGEN`, `NOBLE_GAS`
항목을 가집니다.

## 단위 데이터

| 속성                    | 타입                                 | 존재 여부 플래그           |
|-------------------------|--------------------------------------|----------------------------|
| `molarMass`             | `KMolarMassUnitInstance`             | 항상 존재                  |
| `molarVolume`           | `KMolarVolumeUnitInstance?`          | `hasMolarVolume`           |
| `atomicRadius`          | `KLengthUnitInstance?`               | `hasAtomicRadius`          |
| `covalentRadius`        | `KLengthUnitInstance?`               | `hasCovalentRadius`        |
| `density`               | `KDensityUnitInstance?`              | `hasDensity`               |
| `meltingPoint`          | `KTemperatureUnitInstance?`          | `hasMeltingPoint`          |
| `boilingPoint`          | `KTemperatureUnitInstance?`          | `hasBoilingPoint`          |
| `specificHeatCapacity`  | `KSpecificHeatCapacityUnitInstance?` | `hasSpecificHeatCapacity`  |
| `thermalConductivity`   | `KThermalConductivityUnitInstance?`  | `hasThermalConductivity`   |
| `ionizationEnergy`      | `KEnergyUnitInstance?`               | `hasIonizationEnergy`      |
| `electricalResistivity` | `KResistivityUnitInstance?`          | `hasElectricalResistivity` |
| `electronegativity`     | `Double?`(폴링, 무차원)              | `hasElectronegativity`     |

원소에 대해 의미 있게 정의되지 않는 상수는 `null`입니다 — 헬륨은 정상 압력에서 녹는점이 없고, 비소는 끓지 않고 승화하며, 아스타틴은 너무 희귀해서 밀도가 측정된 적이 없습니다. 이에 대응하는
`has...` 속성은 null 처리 없이 같은 질문에 답합니다.

`molarVolume`은 `molarMass / density`로부터 도출되며, 즉 [몰 부피](units/thermodynamics/molar-volume.md)
그룹의 두 번째 분해를 사용합니다.

## 실전 예제: 금괴는 얼마나 무거운가?

표준 금괴는 7 cm × 4 cm × 2 cm 크기입니다. 무게는 얼마이며, 이는 금 몇 몰에 해당할까요?

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.mechanic.density.times
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole

val gold = KChemicalElement.GOLD

val volume = (7 of centi.meters) * (4 of centi.meters) * (2 of centi.meters) // 56 cm³
val mass = gold.density!! * volume                                          // KMassUnitInstance
mass into kilo.grams                                                        // ≈ 1.081 kg

val amount = mass / gold.molarMass                                          // KAmountOfSubstanceUnitInstance
amount into moles                                                           // ≈ 5.49 mol

gold.molarMass into gramsPerMole                                            // 196.966569
```

## 실전 예제: 구리 프라이팬 가열하기

1.2 kg짜리 구리 프라이팬을 20 °C에서 200 °C로 가열하려면 얼마의 에너지가 필요할까요?

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin

val copper = KChemicalElement.COPPER
val c = copper.specificHeatCapacity!! into joulesPerKilogramKelvin // 385.0
val mass = 1.2 of kilo.grams

val energy = (mass into kilo.grams) * c * 180.0 // ΔT = 180 K
energy                                          // ≈ 83 160 J
```

## 조회

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.KChemicalElementCategory

KChemicalElement.ofSymbol("Fe")        // IRON(대소문자 구분 없음)
KChemicalElement.ofFullName("iron")    // IRON(대소문자 구분 없음)
KChemicalElement.ofOrdinalNumber(26)   // IRON
KChemicalElement.ofOrdinalNumber(57)   // null - 란탄족은 이 표에 포함되지 않음
KChemicalElement.ofMainGroup(4, 6)     // LEAD(주족 4, 주기 6)
KChemicalElement.ofSubGroup(8, 4)      // IRON(부족 8, 주기 4 - Fe/Co/Ni 중 첫 번째)
KChemicalElement.ofPeriod(1)           // [HYDROGEN, HELIUM]
KChemicalElement.ofCategory(KChemicalElementCategory.NOBLE_GAS)
// [HELIUM, NEON, ARGON, KRYPTON, XENON, RADON]
```

부족 8에는 주기당 세 개의 원소가 들어 있습니다. `ofSubGroup`은 첫 번째 (Fe, Ru, Os)를 반환합니다 — 전부를 얻으려면 `ofPeriod`를 사용해 필터링하세요.

## 표기법

| 수학          | Kotlin                                         | 의미                        |
|---------------|------------------------------------------------|-----------------------------|
| `Z`           | `element.ordinalNumber`                        | 원자 번호                   |
| `M`           | `element.molarMass`                            | 몰 질량, `g/mol`            |
| `V_m = M / ρ` | `element.molarVolume`                          | 몰 부피, `m³/mol`           |
| `ρ`           | `element.density`                              | 밀도                        |
| `T_m`, `T_b`  | `element.meltingPoint`, `element.boilingPoint` | 켈빈 단위의 녹는점 / 끓는점 |
| `m = ρ · V`   | `gold.density!! * volume`                      | 밀도 × 부피에서 질량        |
| `n = m / M`   | `mass / gold.molarMass`                        | 질량 ÷ 몰 질량에서 물질량   |
