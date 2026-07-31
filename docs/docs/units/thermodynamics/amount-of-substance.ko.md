# 물질량 (Amount of Substance)

패키지: `org.pcsoft.framework.kunit.thermo.amountofsubstance`
기본 단위: **몰** (`KAmountOfSubstanceUnit.BASE == KAmountOfSubstanceUnit.MOLE`)

유형: **네이티브 단위**

물질량은 SI 기본량 7개 중 하나입니다 — 직접 측정 가능하고 합성되지 않은 양이므로 **네이티브 단위**입니다.
`KAmountOfSubstanceUnitInstance`는 단일하고 1차원적인 래퍼 형태입니다: 지수 1의 단일한
`KAmountOfSubstanceUnit.BASE`(몰) 항이며, 항상 몰로 정규화됩니다.

이는 열역학 분야의 모든 *몰* 양의 기초입니다 ([몰 에너지](molar-energy.md),
[몰 열용량](molar-heat-capacity.md)).

## 이름이 붙은 단위

| 단위     | 기호    |         토큰 |   mol로 1 |
|----------|---------|-------------:|----------:|
| 몰       | `mol`   |      `moles` |       1.0 |
| 파운드몰 | `lbmol` | `poundMoles` | 453.59237 |

둘 다 전체 SI 접두사 범위를 지원합니다 (`milli.moles`, `micro.moles`, `kilo.moles` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

val n = 2 of moles
n.value                 // 2.0 (몰로 정규화)
n into milli.moles      // 2000.0
(1 of kilo.moles) into moles // 1000.0
(1 of poundMoles) into moles // 453.59237
```

## 아보가드로 상수

이 그룹은 아보가드로 상수의 정확한 SI 값을 `AVOGADRO_CONSTANT`(6.02214076e23 mol⁻¹)로, 그리고 인스턴스의 편의 함수 `particleCount()`로 노출합니다. 둘 다 순수한
`Double`을 반환합니다. 입자 수는 차원이 없기 때문입니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

AVOGADRO_CONSTANT             // 6.02214076e23
(2 of moles).particleCount()  // ≈ 1.20443e24개 입자
```

## 실전 예제: 식탁용 소금 녹이기

25 g의 소금 (염화나트륨, 몰질량 58.44 g/mol)에는 몇 몰이 들어 있으며, 이는 몇 개의 화학식 단위에 해당하나요?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

val molarMass = 58.44        // NaCl의 g/mol
val sample = 25 of grams

val n = (sample.value / molarMass) of moles
n into moles                 // ≈ 0.4278 mol
n into milli.moles           // ≈ 427.8 mmol
n.particleCount()            // ≈ 2.576e23개 화학식 단위
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

// + / - : 같은 그룹, 서로 다른 단위와 접두사 간 자동 변환
val total = (1 of moles) + (500 of milli.moles)   // 1.5 mol
val rest  = (1 of moles) - (250 of milli.moles)   // 0.75 mol

// 비교(정규화된 몰 값 기준)
(1 of moles) > (500 of milli.moles)   // true
(1 of moles) == (1000 of milli.moles) // true
```

물질량을 다른 양과 곱하거나 나누면, 타입이 지정된 결과가 없는 한 일반적인 혼합 단위 엔진으로 탈출합니다 — 예를 들어 `energy / amountOfSubstance`는 타입이
지정된 [몰 에너지](molar-energy.md)입니다.

## toString 형식화

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

(2 of moles).toString()                        // "2.0 mol"
"${(2 of moles) into milli.moles} mmol"        // "2000.0 mmol"
```

## 표기법

아래 표는 이 단위가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를 보여줍니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)를 사용하며, `·`는 곱셈을, `/`는 분수를
나타냅니다.

| 수학          | Kotlin                                | 의미                                 |
|---------------|---------------------------------------|--------------------------------------|
| `mol`         | `moles`                               | 물질량, 기본 단위                    |
| `mmol`        | `milli.moles`                         | 밀리몰                               |
| `kmol`        | `kilo.moles`                          | 킬로몰                               |
| `lbmol`       | `poundMoles`                          | 파운드몰(영국식 공학 단위)           |
| `n = m / M`   | `(sample.value / molarMass) of moles` | 질량 ÷ 몰질량에서 물질량             |
| `N = n · N_A` | `n.particleCount()`                   | 물질량 × 아보가드로 상수에서 입자 수 |
