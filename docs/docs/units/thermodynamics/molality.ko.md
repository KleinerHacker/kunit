# 몰랄 농도

패키지: `org.pcsoft.framework.kunit.thermo.molality`
기본 단위: **몰 매 킬로그램** (`KMolalityUnit.BASE == KMolalityUnit.MOLES_PER_KILOGRAM`)

유형: **구성 단위（constructed unit）**

몰랄 농도 `b`는 **용매의 질량당** 얼마나 많은 물질이 녹아 있는지를 나타냅니다: `b = n / m`.
부피를 기준으로 하는 [물질량 농도](concentration.ko.md)와 달리, 몰랄 농도는 용액을 가열해도
변하지 않습니다 — 용매의 질량은 열팽창의 영향을 받지 않기 때문입니다. 이는 어는점 내림과
끓는점 오름 같은 총괄성 물질을 다룰 때 몰랄 농도를 선호되는 값으로 만듭니다.

표준 기본 차원 정규형은 `substance¹ · mass⁻¹`입니다.

## 명명된 단위

| 단위                | 기호      |                    토큰 | mol/kg 기준 1단위 |
|---------------------|-----------|-------------------------:|-----------------:|
| 몰 매 킬로그램          | `mol/kg`  |       `molesPerKilogram` |              1.0 |
| 밀리몰 매 킬로그램       | `mmol/kg` | `millimolesPerKilogram`  |            0.001 |

모든 토큰은 모든 SI 접두사를 지원합니다 (`milli.molesPerKilogram` 등).

## 분해

이 그룹은 하나의 분해를 가지며, 두 형식 모두 동일한 타입의 값-동등 인스턴스를 생성합니다.
네이티브 형식은 **단위 템플릿**으로 조립된다는 점에 유의하세요: 질량 항을 가진 그룹의 경우
원시 혼합 값은 그램 기반의 곱이며, 타입화된 인스턴스는 명명된 단위로 값을 저장합니다.

| 형식                | 표현식                                              |
|--------------------|----------------------------------------------------------|
| 타입화된 연산자        | `amountOfSubstance / mass`                                |
| 네이티브 (`toX()`)   | `(0.25 of moles / kilo.grams).toMolality()`               |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molality.*

val typed = (0.5 of moles) / (2 of kilo.grams)
val native = (0.25 of moles.toUnit() / kilo.grams.toUnit()).toMolality()

typed == native               // true
typed into molesPerKilogram   // 0.25
```

## 그룹으로 계산하기

| 표현식                              | 결과 타입                          | 의미                       |
|----------------------------------------|--------------------------------------|-----------------------------|
| `amountOfSubstance / mass`            | `KMolalityUnitInstance`              | `b = n / m`                 |
| `molality * mass`                     | `KAmountOfSubstanceUnitInstance`     | `n = b · m`                 |
| `amountOfSubstance / molality`        | `KMassUnitInstance`                  | 필요한 용매 질량                |
| `1 / molarMass`                       | `KMolalityUnitInstance`              | 순물질의 몰랄 농도                |
| `1 / molality`                        | `KMolarMassUnitInstance`             | 몰 질량으로 되돌아감              |

마지막 두 관계는 몰랄 농도와 [몰 질량](molar-mass.ko.md)이 서로 역수 관계임을 보여줍니다.

## 실제 사례 — 물 1킬로그램에는 몇 몰이 들어 있을까?

물의 몰 질량은 18.015 g/mol이므로, 물 1킬로그램에는 약 55.5 mol이 들어 있습니다 —
이것이 역수 관계가 작동하는 예입니다:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molality.*

val b = 1 / (18.015 of gramsPerMole)   // KMolalityUnitInstance
b into molesPerKilogram                 // ≈ 55.51

// A 0.5 molal salt solution in 2 kg of water
val n = (0.5 of molesPerKilogram) * (2 of kilo.grams)
n into moles                            // 1.0

// And back to the molar mass
(1 / b) into gramsPerMole               // ≈ 18.015
```

## 값 의미론

`equals`/`hashCode`는 **정규화된 mol/kg 값**을 비교하므로,
`(1 of molesPerKilogram) == (1000 of millimolesPerKilogram)`입니다. `toString()`은 기본
단위로 값을 표시합니다: `"0.25 mol/kg"`.

## 참고 항목

* [물질량 농도](concentration.ko.md) — 같은 개념을 부피당으로 표현한 것.
* [몰 질량](molar-mass.ko.md) — 역수 값.
* [열역학 개요](overview.ko.md)
