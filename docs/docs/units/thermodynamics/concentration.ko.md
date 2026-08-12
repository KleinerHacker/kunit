# 물질량 농도（몰농도）

패키지: `org.pcsoft.framework.kunit.thermo.concentration`
기본 단위: **몰 매 세제곱미터** (`KConcentrationUnit.BASE == KConcentrationUnit.MOLES_PER_CUBIC_METER`)

유형: **구성 단위（constructed unit）**

물질량 농도 `c`는 **용액의 부피당** 얼마나 많은 물질이 녹아 있는지를 나타냅니다: `c = n / V`.
화학에서는 거의 항상 몰 매 리터로 표시하며 이를 **몰농도**라 부르고 `M`으로 표기합니다;
임상 검사실에서는 밀리몰 매 리터를 사용합니다.

표준 기본 차원 정규형은 `substance¹ · length⁻³`입니다.

## 명명된 단위

| 단위                    | 기호      |                    토큰 | mol/m³ 기준 1단위 |
|-------------------------|-----------|------------------------:|-----------------:|
| 몰 매 세제곱미터           | `mol/m^3` |    `molesPerCubicMeter` |              1.0 |
| 몰 매 리터 (몰농도)        | `mol/l`   |         `molesPerLiter` |             1000 |
| 몰농도 (`M`)              | `mol/l`   |                 `molar` |             1000 |
| 밀리몰 매 리터             | `mmol/l`  |    `millimolesPerLiter` |              1.0 |

`molar`는 `molesPerLiter`의 또 다른 표기이며, 별도의 단위가 아닙니다. 밀리몰 매 리터는
수치적으로 몰 매 세제곱미터와 같다는 점에 유의하세요 — SI 기본 단위가 곧 수치적으로 임상 단위와
같습니다. 모든 토큰은 모든 SI 접두사를 지원합니다 (`milli.molesPerLiter`, `micro.molar` 등).

## 분해

이 그룹은 하나의 분해를 가지며, 두 형식 모두 동일한 타입의 값-동등 인스턴스를 생성합니다:

| 형식                | 표현식                                                                    |
|--------------------|------------------------------------------------------------------------------|
| 타입화된 연산자        | `amountOfSubstance / volume`                                                 |
| 네이티브 (`toX()`)   | `((0.5 of moles).toUnit() / (2 of liters).toUnit()).toConcentration()`       |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.*

val typed = (0.5 of moles) / (2 of liters)
val native = ((0.5 of moles).toUnit() / (2 of liters).toUnit()).toConcentration()

typed == native            // true
typed into molesPerLiter   // 0.25
```

## 그룹으로 계산하기

| 표현식                                      | 결과 타입                          | 의미                     |
|------------------------------------------------|-------------------------------------|-----------------------------|
| `amountOfSubstance / volume`                  | `KConcentrationUnitInstance`        | `c = n / V`                 |
| `concentration * volume`                      | `KAmountOfSubstanceUnitInstance`    | `n = c · V`                 |
| `amountOfSubstance / concentration`           | `KVolumeUnitInstance`               | 필요한 부피                  |
| `conductivity / concentration`                | `KMolarConductivityUnitInstance`    | `Λ = κ / c`                 |

## 실제 사례 — 혈당

약 5리터의 혈액에서 공복 혈당 **5.5 mmol/l**는 다음에 해당합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.*

val c = 5.5 of millimolesPerLiter
c into molesPerCubicMeter          // 5.5 — the SI unit is numerically the clinical one

val n = c * (5 of liters)          // KAmountOfSubstanceUnitInstance
n into milli.moles                 // 27.5 mmol of glucose in the bloodstream

// How much solution holds 1 mol at that concentration?
val v = (1 of moles) / c           // KVolumeUnitInstance
v into liters                       // ≈ 181.8 l
```

## 값 의미론

`equals`/`hashCode`는 **정규화된 mol/m³ 값**을 비교하므로,
`(1 of molesPerLiter) == (1000 of molesPerCubicMeter)`입니다. `toString()`은 기본 단위로 값을
표시합니다: `"1000.0 mol/m^3"`.

## 참고 항목

* [몰랄 농도](molality.ko.md) — 같은 개념을 **질량**당으로, 열팽창의 영향을 받지 않습니다.
* [물질량](amount-of-substance.ko.md) — 분자에 해당하는 값.
* [몰 부피](molar-volume.ko.md) — 순물질에 대한 역수 값.
* [열역학 개요](overview.ko.md)
