# 투자율 (Permeability)

패키지: `org.pcsoft.framework.kunit.electric.permeability`
기본 단위: **미터당 헨리(henry per meter)** (`KPermeabilityUnit.BASE == KPermeabilityUnit.HENRY_PER_METER`)

유형: **구성 단위(constructed unit)**

투자율은 **구성** 단위입니다: `질량 · 길이 · 시간⁻² · 전류⁻²`
(`kg·m·s⁻²·A⁻²` = `H/m`)의 조합입니다. `KPermeabilityUnitInstance`는 네 개의 항으로 이루어진
`KMixedUnitInstance`를 감쌉니다 — `KMassUnit.BASE`(그램)는 `+1`, `KDistanceUnit.BASE`(미터)는 `+1`,
`KTimeUnit.BASE`(초)는 `-2`, `KElectricCurrentUnit.BASE`(암페어)는 `-2`입니다. 라이브러리의 질량 구성
요소는 **그램**(킬로그램이 아님)으로 정규화되어 있으므로, 정규 곱은 미터당 헨리에 도달하기 위해 1000으로
나뉩니다; 저장된 값은 항상 미터당 헨리로 정규화됩니다.

투자율 `μ`는 물질의 자기 상수입니다: 이는 [자속 밀도](magneticfluxdensity.md)를
[자기장 세기](magneticfieldstrength.md)와 연결하며(`μ = B / H`), [인덕턴스](inductance.md)를
코일 형상과 연결합니다. 그 전기적 대응물은 [유전율](permittivity.md)입니다.

## 투자율 만들기

이름이 붙은 토큰으로 투자율을 만들거나, 아래의 분해식으로부터 만들 수 있습니다. 이름이 붙은 단위는
값 1의 토큰으로 존재합니다(`of`/`into`와 함께 사용):

| 투자율 | 기호 | 토큰 | H/m 단위로 1 |
|---|---|---:|---:|
| 미터당 헨리 | `H/m` | `henriesPerMeter` | 1.0 |
| 센티미터당 헨리 | `H/cm` | `henriesPerCentimeter` | 100.0 |
| 진공 투자율 `μ₀` | `H/m` | `vacuumPermeability` | 1.25663706127e-6 |

이름이 붙은 단위는 `KPrefixBuilder`를 통해 SI 접두사를 지원합니다(`micro.henriesPerMeter`,
`milli.henriesPerMeter` 등). 이 상수는 `KPermeabilityUnit.VACUUM_PERMEABILITY`로도 사용할 수 있습니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.permeability.*

val mu = 1 of vacuumPermeability      // μ₀
mu into henriesPerMeter               // 1.25663706127e-6
mu into micro.henriesPerMeter         // 1.25663706127
(1 of henriesPerCentimeter) into henriesPerMeter // 100.0
```

## 다중 분해

투자율은 여러 **동등한 분해식**을 통해 도달할 수 있으며, 모두 동일한 값-동등 투자율을 생성합니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `inductance / length` | `KPermeabilityUnitInstance` | `μ = L · l / (N² · A)`, 기하 계수는 길이입니다 |
| `magneticFluxDensity / magneticFieldStrength` | `KPermeabilityUnitInstance` | `μ = B / H` |
| `mass·length/(time²·current²)` | `.toPermeability()`를 통해 | 네이티브 정규 `kg·m·s⁻²·A⁻²` 표현식 |

타입이 지정된 연산자 형식은 투자율을 직접 반환합니다. 완전히 네이티브인 표현식은 일반적인
`KMixedUnitInstance`로 남아 있으며 `toPermeability()`로 좁혀집니다(이는 정규 형식만 인식하고 그렇지
않으면 `IllegalStateException`을 발생시킵니다). 모든 경로는 값-동등합니다.

역연산자는 인덕턴스, 길이, 두 자기 장 관련량을 함께 묶습니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `permeability * length` | `KInductanceUnitInstance` | `L = μ · N² · A / l` (교환 가능) |
| `inductance / permeability` | `KLengthUnitInstance` | 기하 계수 `N² · A / l = L / μ` |
| `permeability * magneticFieldStrength` | `KMagneticFluxDensityUnitInstance` | `B = μ · H` (교환 가능) |
| `magneticFluxDensity / permeability` | `KMagneticFieldStrengthUnitInstance` | `H = B / μ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.inductance.henries
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.amperesPerMeter
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.teslas
import org.pcsoft.framework.kunit.electric.permeability.*

// 실제 사례 - 진공에서 1000 A/m의 장은 1.257 mT의 자속 밀도를 만듭니다.
val b = (1 of vacuumPermeability) * (1000 of amperesPerMeter)  // 1.25663706127e-3 T

// 투자율에 대해 풀어낸 정의:
val mu = (6 of teslas) / (3 of amperesPerMeter)                // 2 H/m
val fromInductance = (10 of henries) / (5 of meters)           // 2 H/m

// 네이티브 kg·m·s⁻²·A⁻² 표현식으로서의 동일한 투자율:
val raw = 2 of (kilo.grams * (meters pow 1)) / ((seconds pow 2) * (amperes pow 2))
raw.toPermeability() == (2 of henriesPerMeter)                 // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.permeability.*

val s = (1 of henriesPerMeter) + (1 of henriesPerCentimeter)  // 101 H/m
(1 of henriesPerCentimeter) > (1 of henriesPerMeter)          // true
(2 of henriesPerMeter) * (3 of henriesPerMeter)               // KMixedUnitInstance (그룹을 벗어남)
```

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.permeability.*

(1 of henriesPerCentimeter).toString()   // "100.0 H/m" (기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를
보여줍니다. 지수는 유니코드 위첨자(`²`, `⁻²`)를 사용하며, `·`는 곱셈을, `/`는 분수를 나타냅니다. 어떤 양이
분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `H/m` | `henriesPerMeter` | 투자율, 기본 단위(이름이 붙은 토큰, 미터당 헨리) |
| `μ₀` | `vacuumPermeability` | 진공 투자율 상수, 1.257 µH/m |
| `B / H` | `(6 of teslas) / (3 of amperesPerMeter)` | 자속 밀도와 장 세기의 비로서의 투자율 |
| `L · l / (N²·A)` | `(10 of henries) / (5 of meters)` | 인덕턴스와 코일 형상으로부터의 투자율 |
| `kg·m/(s²·A²)` | `(kilo.grams * (meters pow 1)) / ((seconds pow 2) * (amperes pow 2))` | 질량·길이 / (시간²·전류²)로서의 투자율(분수 형식) |
| `kg·m·s⁻²·A⁻²` | `kilo.grams * (meters pow 1) * (seconds pow -2) * (amperes pow -2)` | 순수 곱으로서의 동일한 투자율 |
| `µH/m` | `micro.henriesPerMeter` | 접두사가 붙은 투자율(마이크로헨리 매 미터) |
