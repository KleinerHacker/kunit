# 촉매 활성

패키지: `org.pcsoft.framework.kunit.thermo.catalyticactivity`
기본 단위: **카탈** (`KCatalyticActivityUnit.BASE == KCatalyticActivityUnit.KATAL`)

유형: **구성 단위（constructed unit）**

효소 제제의 촉매 활성 `z`는 **시간당** 얼마나 많은 기질을 전환하는지를 나타냅니다:
`z = n / t`. SI 단위는 **카탈**(1 kat = 1 mol/s)이며, 매우 큰 단위이므로 실무에서는
마이크로카탈 또는 전통적인 **효소 단위** `U`(분당 1마이크로몰)를 사용합니다.

표준 기본 차원 정규형은 `substance¹ · time⁻¹`입니다.

## 명명된 단위

| 단위     | 기호   |          토큰 |          kat 기준 1단위 |
|---------|--------|--------------:|-----------------------:|
| 카탈     | `kat`  |      `katals` |                    1.0 |
| 효소 단위 | `U`    | `enzymeUnits` | 1/60 × 10⁻⁶ ≈ 1.667e-8 |

1 U = 1 µmol/min이므로 1 kat = 60,000,000 U이고 1 U ≈ 16.67 nkat입니다. 모든 토큰은
모든 SI 접두사를 지원합니다 (`micro.katals`, `nano.katals` 등).

## 분해

이 그룹은 하나의 분해를 가지며, 두 형식 모두 동일한 타입의 값-동등 인스턴스를 생성합니다:

| 형식                | 표현식                                                                    |
|--------------------|-----------------------------------------------------------------------------|
| 타입화된 연산자        | `amountOfSubstance / time`                                                  |
| 네이티브 (`toX()`)   | `((2 of moles).toUnit() / (4 of seconds).toUnit()).toCatalyticActivity()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.catalyticactivity.*

val typed = (2 of moles) / (4 of seconds)
val native = ((2 of moles).toUnit() / (4 of seconds).toUnit()).toCatalyticActivity()

typed == native      // true
typed into katals    // 0.5
```

## 그룹으로 계산하기

| 표현식                                     | 결과 타입                          | 의미                  |
|----------------------------------------------|------------------------------------|-----------------------|
| `amountOfSubstance / time`                   | `KCatalyticActivityUnitInstance`   | `z = n / t`           |
| `catalyticActivity * time`                   | `KAmountOfSubstanceUnitInstance`   | `n = z · t`           |
| `amountOfSubstance / catalyticActivity`      | `KTimeUnitInstance`                | 걸리는 시간             |

## 실제 사례 — 효소 분석

한 분석에서 **10초** 동안 **0.5 mmol**의 기질이 전환됩니다. 두 가지 방식으로 표현하고,
더 작은 배치가 걸리는 시간도 계산합니다:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.catalyticactivity.*

val z = (0.5 of milli.moles) / (10 of seconds)
z into micro.katals        // 50.0
z into enzymeUnits         // ≈ 3000.0 U

// The enzyme unit by definition: one micromole per minute
val one = (1 of micro.moles) / (1 of minutes)
one into enzymeUnits       // 1.0

// How long for 2 mmol at that activity?
val t = (2 of milli.moles) / z
t into seconds             // 40.0
```

## 값 의미론

`equals`/`hashCode`는 **정규화된 kat 값**을 비교하므로, `(1 of katals) == (1000 of milli.katals)`입니다.
`toString()`은 기본 단위로 값을 표시합니다: `"5.0E-5 kat"`.

## 참고 항목

* [물질량](amount-of-substance.ko.md) — 분자에 해당하는 값.
* [물질량 농도](concentration.ko.md) — 분석에서 일반적으로 측정하는 값.
* [열역학 개요](overview.ko.md)
