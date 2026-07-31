# 몰 질량 (Molar Mass)

패키지: `org.pcsoft.framework.kunit.thermo.molarmass`
기본 단위: **몰당 그램** (`KMolarMassUnit.BASE == KMolarMassUnit.GRAM_PER_MOLE`)

유형: **구성된 단위**

몰 질량은 물질량당 질량입니다: `mass / amountOfSubstance` (`g/mol`). 이는 거시적인 세계 (저울 위의 그램)와 입자의 세계 (몰)를 잇는 다리이며, 수치적으로 물질의 상대 원자량 또는
분자량과 같습니다.

`KMolarMassUnitInstance`는 정규 형식 `mass¹ · substance⁻¹` (`g·mol⁻¹`)의 정확히 두 항으로 이루어진
`KMixedUnitInstance`를 감싸며, 항상 g/mol로 정규화됩니다. 이 라이브러리는 질량을 그램으로 정규화하기 때문에, 원시 구성 요소의 기저가 곧 이름이 붙은 기본 단위입니다 — 별도의 변환 계수가
필요하지 않습니다.

밀도로 나누면 [몰 부피](molar-volume.md)가 되며, [주기율표](../../periodic-table.md)의 모든 원소는 이 그룹의 값으로 자신의 몰 질량을 노출합니다.

## 이름이 붙은 단위

| 단위              | 기호       |                 토큰 |     g/mol로 1 |
|-------------------|------------|---------------------:|--------------:|
| 몰당 그램         | `g/mol`    |       `gramsPerMole` |           1.0 |
| 몰당 킬로그램     | `kg/mol`   |   `kilogramsPerMole` |        1000.0 |
| 파운드몰당 파운드 | `lb/lbmol` | `poundsPerPoundMole` |           1.0 |
| 개체당 달톤       | `Da`       |   `daltonsPerEntity` | 1.00000000105 |

파운드몰은 파운드 단위의 질량이 몰 질량과 같도록 정의되어 있어서, `lb/lbmol`은 수치적으로 `g/mol`과 동일합니다. 2019년 SI 재정의 이후로 몰 질량 상수는 더 이상 정확히 1 g/mol이 아니며,
그래서 달톤 계수가 존재합니다. 모든 단위는 전체 SI 접두사 범위를 지원합니다 (`kilo.gramsPerMole`, `milli.kilogramsPerMole` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarmass.*

val water = 18.015 of gramsPerMole
water into gramsPerMole      // 18.015
water into kilogramsPerMole  // 0.018015
water into daltonsPerEntity  // ≈ 분자당 18.015 Da
```

## 실전 예제: 1몰 계량하기

레시피에서 식탁용 소금 (NaCl, 58.44 g/mol) 0.25 mol이 필요합니다. 얼마나 계량해야 하며, 500 g짜리 포장에는 몇 몰이 들어 있을까요?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.*

val saltMolarMass = 58.44 of gramsPerMole

// 0.25 mol은 얼마의 질량인가?
val portion = saltMolarMass * (0.25 of moles) // KMassUnitInstance
portion into grams                            // 14.61 g

// 500 g 포장에는 몇 몰이 들어 있는가?
val amount = (500 of grams) / saltMolarMass   // KAmountOfSubstanceUnitInstance
amount into moles                             // ≈ 8.556 mol

// 그리고 계량된 시료로부터 측정한 몰 질량 자체:
val measured = (14.61 of grams) / (0.25 of moles)
measured into gramsPerMole                    // 58.44
```

## 핵심 단위 (질량 & 물질량)로 계산하기

| 표현식                          | 결과 타입                        | 의미                       |
|---------------------------------|----------------------------------|----------------------------|
| `mass / amountOfSubstance`      | `KMolarMassUnitInstance`         | 몰 질량                    |
| `molarMass * amountOfSubstance` | `KMassUnitInstance`              | 총 질량                    |
| `amountOfSubstance * molarMass` | `KMassUnitInstance`              | 총 질량(교환 법칙)         |
| `mass / molarMass`              | `KAmountOfSubstanceUnitInstance` | 포함된 물질량              |
| `molarMass / density`           | `KMolarVolumeUnitInstance`       | [몰 부피](molar-volume.md) |

## 분해

두 분해 모두 동일한 타입이 지정된 값-동등 인스턴스를 생성합니다.

| 분해                       | 형식                              | 결과                          |
|----------------------------|-----------------------------------|-------------------------------|
| `mass / amountOfSubstance` | 타입이 지정된 연산자              | `KMolarMassUnitInstance` 직접 |
| `mass · substance⁻¹`       | 네이티브 표현식 + `toMolarMass()` | `KMolarMassUnitInstance`      |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.*

// 타입이 지정된 연산자 형식
val typed = (18.015 of grams) / (1 of moles)

// 네이티브 기저 차원 형식 (g·mol⁻¹), toMolarMass()가 인식
val native = ((18.015 of grams).toUnit() / (1 of moles).toUnit()).toMolarMass()

typed == native // true - 둘 다 18.015 g/mol
```

`toMolarMass()`는 **오직** 정규 형식만 인식하며, 잘못된 형태는 `IllegalStateException`을 던집니다.

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarmass.*

val total = (10 of gramsPerMole) + (4 of gramsPerMole) // 14 g/mol
val rest  = (10 of gramsPerMole) - (4 of gramsPerMole) // 6 g/mol

(1 of kilogramsPerMole) > (500 of gramsPerMole)   // true
(1 of kilogramsPerMole) == (1000 of gramsPerMole) // true
```

## toString 형식화

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarmass.*

(1 of kilogramsPerMole).toString()  // "1000.0 g/mol"
(18.015 of gramsPerMole).toString() // "18.015 g/mol"
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를 보여줍니다. 지수는 유니코드 위첨자 (`²`, `³`, `⁻¹`)를 사용하며, `·`는 곱셈을,
`/`는 분수를 나타냅니다. 어떤 양이 분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학          | Kotlin                               | 의미                                  |
|---------------|--------------------------------------|---------------------------------------|
| `g/mol`       | `gramsPerMole`                       | 몰 질량, 기본 단위 — 이름이 붙은 토큰 |
| `g·mol⁻¹`     | `grams / moles`                      | 기저 차원으로의 동일한 양             |
| `kg/mol`      | `kilogramsPerMole`                   | 몰당 킬로그램                         |
| `Da`          | `daltonsPerEntity`                   | 기본 개체당 달톤                      |
| `M = m / n`   | `(14.61 of grams) / (0.25 of moles)` | 질량 ÷ 물질량에서 몰 질량             |
| `m = M · n`   | `saltMolarMass * (0.25 of moles)`    | 몰 질량 × 물질량에서 질량             |
| `n = m / M`   | `(500 of grams) / saltMolarMass`     | 질량 ÷ 몰 질량에서 물질량             |
| `V_m = M / ρ` | `molarMass / density`                | 몰 질량 ÷ 밀도에서 몰 부피            |
