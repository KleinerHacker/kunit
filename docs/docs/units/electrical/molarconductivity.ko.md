# 몰 전도도

패키지: `org.pcsoft.framework.kunit.electric.molarconductivity`
기본 단위: **몰당 시멘스 제곱미터**
(`KMolarConductivityUnit.BASE == KMolarConductivityUnit.SIEMENS_SQUARE_METER_PER_MOLE`)

종류: **구성된 단위**

전해질의 몰 전도도 `Λ` 는 그 [전도도](conductivity.ko.md)를 [농도](../thermodynamics/concentration.ko.md)로
정규화한 것입니다: `Λ = κ / c`. 농도를 나눠 냄으로써 농도가 다른 용액들을 비교할 수 있게 됩니다 — 이는 "이 특정
비커가 얼마나 잘 전도하는가"가 아니라 "이 이온이 얼마나 잘 전도하는가"에 답합니다.

정규 기본 차원 표준형은 `mass⁻¹ · time³ · current² · substance⁻¹` 입니다. 길이 차원은 완전히 상쇄됩니다:
전도도가 `length⁻³` 을 기여하고, 분모의 농도가 또 다른 `length⁻³` 을 기여하기 때문입니다.

## 이름 있는 단위

| 단위                             | 기호       |                            토큰 | 1단위의 S·m²/mol 값 |
|----------------------------------|--------------|---------------------------------:|-------------------:|
| 몰당 시멘스 제곱미터            | `S*m^2/mol`  |    `siemensSquareMetersPerMole` |                1.0 |
| 몰당 시멘스 제곱센티미터        | `S*cm^2/mol` | `siemensSquareCentimetersPerMole` |             1e-4 |

전기화학 표는 보통 S·cm²/mol 로 표시합니다. SI 형식은 보통 milli 접두어를 붙여 씁니다
(`milli.siemensSquareMetersPerMole`). 모든 토큰은 모든 SI 접두어를 지원합니다.

## 분해

이 그룹은 하나의 분해를 가지며, 두 형태 모두 값이 같은 같은 타입의 인스턴스를 만듭니다. 네이티브 형태는
**단위 템플릿**으로 조립됩니다. 그룹이 질량 항을 가지기 때문입니다: 원시 혼합값은 그램 기반 곱이고,
타입이 지정된 인스턴스는 이름 있는 단위로 값을 저장합니다.

| 형태             | 식                                                          |
|------------------|---------------------------------------------------------------------|
| 타입이 지정된 연산자 | `conductivity / concentration`                                      |
| 네이티브 (`toX()`) | `(0.01 of s³ · A² / kilo.grams / moles).toMolarConductivity()`      |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.conductivity.siemensPerMeter
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.molesPerLiter
import org.pcsoft.framework.kunit.electric.molarconductivity.*

val typed = (1.0 of siemensPerMeter) / (0.1 of molesPerLiter)
val native = (
    0.01 of (seconds pow 3) * (amperes.toUnit() pow 2) / kilo.grams.toUnit() / moles.toUnit()
).toMolarConductivity()

typed == native                          // true
typed into siemensSquareMetersPerMole    // 0.01
```

## 그룹으로 계산하기

| 식                                  | 결과 타입                      | 의미       |
|-------------------------------------|-----------------------------------|---------------|
| `conductivity / concentration`      | `KMolarConductivityUnitInstance` | `Λ = κ / c`   |
| `molarConductivity * concentration` | `KConductivityUnitInstance`      | `κ = Λ · c`   |
| `conductivity / molarConductivity`  | `KConcentrationUnitInstance`     | `c = κ / Λ`   |
| `molarConductivity + …`             | `KMolarConductivityUnitInstance` | 콜라우슈 법칙 |

콜라우슈의 독립 이온 이동 법칙은 무한 희석 상태에서 몰 전도도가 이온 기여분의 **합**이라고 말합니다 —
이는 정확히 이 그룹의 같은 타입 간 `+` 연산 그 자체입니다.

## 실제 예 — KCl에 대한 콜라우슈 법칙

극한 이온 전도도는 K⁺ 가 7.35 mS·m²/mol, Cl⁻ 가 7.63 mS·m²/mol 입니다. 이 둘의 합이 염화칼륨의 극한 몰 전도도이며,
농도를 곱하면 계측기가 읽을 전도도를 얻습니다:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.conductivity.siemensPerMeter
import org.pcsoft.framework.kunit.thermo.concentration.molesPerLiter
import org.pcsoft.framework.kunit.electric.molarconductivity.*

val potassium = 7.350 of milli.siemensSquareMetersPerMole
val chloride  = 7.635 of milli.siemensSquareMetersPerMole

val kcl = potassium + chloride                       // 콜라우슈
kcl into milli.siemensSquareMetersPerMole            // 14.985
kcl into siemensSquareCentimetersPerMole             // ≈ 149.85(표의 값)

val kappa = kcl * (0.01 of molesPerLiter)            // KConductivityUnitInstance
kappa into siemensPerMeter                            // ≈ 0.1499 S/m
```

## 값 의미론

`equals`/`hashCode` 는 **정규화된 S·m²/mol 값**을 비교하므로,
`(1 of siemensSquareMetersPerMole) == (10000 of siemensSquareCentimetersPerMole)` 입니다. `toString()` 은
값을 기본 단위로 표시합니다: `"0.0126 S*m^2/mol"`.

## 참고 항목

* [전도도](conductivity.ko.md) — 분자.
* [물질량 농도](../thermodynamics/concentration.ko.md) — 분모.
* [전도](conductance.ko.md) — 계측기가 측정하는 정규화되지 않은 양.
* [전기공학 개요](overview.ko.md)
