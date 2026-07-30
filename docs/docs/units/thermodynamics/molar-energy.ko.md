# 몰 에너지 (Molar Energy)

패키지: `org.pcsoft.framework.kunit.thermo.molarenergy`
기본 단위: **몰당 줄** (`KMolarEnergyUnit.BASE == KMolarEnergyUnit.JOULE_PER_MOLE`)

유형: **구성된 단위**

몰 에너지는 물질량당 에너지입니다: `energy / amountOfSubstance` (`J/mol`). 맥락에 따라 동일한 양이
*몰 엔탈피*, *반응 엔탈피* 또는 *결합 에너지*라고 불립니다.

`KMolarEnergyUnitInstance`는 정규 형식 `mass¹ · distance² · time⁻² · substance⁻¹` (`kg·m²·s⁻²·mol⁻¹`)의
정확히 네 항으로 이루어진 `KMixedUnitInstance`를 감싸며, 항상 J/mol로 정규화됩니다.

단위 온도당으로는 [몰 열용량](molar-heat-capacity.md)이 되고, 몰 대신 킬로그램당으로는
[비에너지](specific-energy.md)가 됩니다.

## 이름이 붙은 단위

| 단위 | 기호 | 토큰 | J/mol로 1 |
|---|---|---:|---:|
| 몰당 줄 | `J/mol` | `joulesPerMole` | 1.0 |
| 몰당 칼로리 | `cal/mol` | `caloriesPerMole` | 4.184 |
| 개체당 전자볼트 | `eV/entity` | `electronVoltsPerEntity` | 96485.33212 |

개체당 전자볼트 토큰은 *입자당* 에너지를 *몰당* 에너지로 변환합니다 — 그 계수는 패러데이 상수입니다.
모든 단위는 전체 SI 접두사 범위를 지원합니다(`kilo.joulesPerMole`, `kilo.caloriesPerMole`,
`milli.electronVoltsPerEntity` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarenergy.*

val dH = 286 of kilo.joulesPerMole
dH into joulesPerMole            // 286_000.0
dH into kilo.caloriesPerMole     // ≈ 68.36
dH into electronVoltsPerEntity   // ≈ 2.964 eV(분자당)
```

## 실전 예제: 수소 연소

액체 물의 생성 엔탈피는 −286 kJ/mol입니다. 수소 4몰이 연소할 때 얼마의 에너지가 방출되며, 분자당으로는
얼마인가요?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarenergy.*

val formation = -286 of kilo.joulesPerMole
val hydrogen = 4 of moles

val released = formation * hydrogen   // KEnergyUnitInstance
released into kilo.joules             // -1144.0 kJ
released into mega.joules             // -1.144 MJ

// 분자당, 화학자의 단위로
formation into electronVoltsPerEntity // ≈ -2.964 eV

// 역방향: 1 MJ는 얼마의 물질량에 해당하는가?
val n = (1 of mega.joules) / formation // KAmountOfSubstanceUnitInstance
n into moles                           // ≈ -3.497 mol
```

## 핵심 단위(에너지 & 물질량)로 계산하기

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `energy / amountOfSubstance` | `KMolarEnergyUnitInstance` | 몰 에너지 |
| `molarEnergy * amountOfSubstance` | `KEnergyUnitInstance` | 총 에너지 |
| `amountOfSubstance * molarEnergy` | `KEnergyUnitInstance` | 총 에너지(교환 법칙) |
| `energy / molarEnergy` | `KAmountOfSubstanceUnitInstance` | 관련된 물질량 |

## 분해

두 분해 모두 동일한 타입이 지정된 값-동등 인스턴스를 생성합니다.

| 분해 | 형식 | 결과 |
|---|---|---|
| `energy / amountOfSubstance` | 타입이 지정된 연산자 | `KMolarEnergyUnitInstance` 직접 |
| `mass · distance² · time⁻² · substance⁻¹` | 네이티브 표현식 + `toMolarEnergy()` | `KMolarEnergyUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarenergy.*

// 타입이 지정된 연산자 형식
val typed = (1 of joules) / (1 of moles)

// 네이티브 기저 차원 형식 (kg·m²·s⁻²·mol⁻¹), toMolarEnergy()가 인식
val native = (
    (1000 of grams).toUnit() *
        ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        (1 of moles).toUnit()
    ).toMolarEnergy()

typed == native // true - 둘 다 1.0 J/mol
```

`toMolarEnergy()`는 **오직** 정규 형식만 인식하며, 잘못된 형태는 `IllegalStateException`을 던집니다.

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarenergy.*

val total = (1 of kilo.joulesPerMole) + (500 of joulesPerMole)  // 1500 J/mol
val rest  = (1 of kilo.joulesPerMole) - (250 of joulesPerMole)  // 750 J/mol

(1 of kilo.joulesPerMole) > (500 of joulesPerMole)   // true
(1 of kilo.joulesPerMole) == (1000 of joulesPerMole) // true
```

## toString 형식화

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarenergy.*

(286 of kilo.joulesPerMole).toString()                        // "286000.0 J/mol"
"${(286 of kilo.joulesPerMole) into caloriesPerMole} cal/mol" // "68355.6... cal/mol"
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를
보여줍니다. 지수는 유니코드 위첨자(`²`, `³`, `⁻¹`)를 사용하며, `·`는 곱셈을, `/`는 분수를 나타냅니다.
어떤 양이 분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두
나열됩니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `J/mol` | `joulesPerMole` | 몰 에너지, 기본 단위 — 이름이 붙은 토큰 |
| `kg·m²·s⁻²·mol⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / moles` | 기저 차원으로의 동일한 양 |
| `kJ/mol` | `kilo.joulesPerMole` | 몰당 킬로줄 |
| `eV`(입자당) | `electronVoltsPerEntity` | 원소 개체당 전자볼트 |
| `ΔH_m = Q / n` | `(572 of kilo.joules) / (2 of moles)` | 에너지 ÷ 물질량에서 몰 에너지 |
| `Q = ΔH_m · n` | `formation * hydrogen` | 몰 에너지 × 물질량에서 에너지 |
| `n = Q / ΔH_m` | `(1 of mega.joules) / formation` | 에너지 ÷ 몰 에너지에서 물질량 |
