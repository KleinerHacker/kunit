# 유전율 (Permittivity)

패키지: `org.pcsoft.framework.kunit.electric.permittivity`
기본 단위: **미터당 패럿(farad per meter)** (`KPermittivityUnit.BASE == KPermittivityUnit.FARAD_PER_METER`)

유형: **구성 단위(constructed unit)**

유전율은 **구성** 단위입니다: `질량⁻¹ · 길이⁻³ · 시간⁴ · 전류²`
(`kg⁻¹·m⁻³·s⁴·A²` = `F/m`)의 조합입니다. `KPermittivityUnitInstance`는 네 개의 항으로 이루어진
`KMixedUnitInstance`를 감쌉니다 — `KMassUnit.BASE`(그램)는 `-1`, `KDistanceUnit.BASE`(미터)는 `-3`,
`KTimeUnit.BASE`(초)는 `+4`, `KElectricCurrentUnit.BASE`(암페어)는 `+2`입니다. 라이브러리의 질량 구성
요소는 **그램**(킬로그램이 아님)으로 정규화되어 있고 질량 지수가 *음수*이므로, 정규 곱은 미터당 패럿에
도달하기 위해 1000을 곱합니다; 저장된 값은 항상 미터당 패럿으로 정규화됩니다.

유전율 `ε`는 물질의 전기 상수입니다: 이는 [전속 밀도](electricfluxdensity.md)를
[전기장 세기](electricfieldstrength.md)와 연결하며(`ε = D / E`), [정전용량](capacitance.md)을
판 형상과 연결합니다. 그 자기적 대응물은 [투자율](permeability.md)입니다.

## 유전율 만들기

이름이 붙은 토큰으로 유전율을 만들거나, 아래의 분해식으로부터 만들 수 있습니다. 이름이 붙은 단위는
값 1의 토큰으로 존재합니다(`of`/`into`와 함께 사용):

| 유전율 | 기호 | 토큰 | F/m 단위로 1 |
|---|---|---:|---:|
| 미터당 패럿 | `F/m` | `faradsPerMeter` | 1.0 |
| 센티미터당 패럿 | `F/cm` | `faradsPerCentimeter` | 100.0 |
| 진공 유전율 `ε₀` | `F/m` | `vacuumPermittivity` | 8.8541878188e-12 |

이름이 붙은 단위는 `KPrefixBuilder`를 통해 SI 접두사를 지원합니다(`pico.faradsPerMeter`,
`nano.faradsPerMeter` 등). 이 상수는 `KPermittivityUnit.VACUUM_PERMITTIVITY`로도 사용할 수 있습니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.electric.permittivity.*

val eps = 1 of vacuumPermittivity     // ε₀
eps into faradsPerMeter               // 8.8541878188e-12
eps into pico.faradsPerMeter          // 8.8541878188
(1 of faradsPerCentimeter) into faradsPerMeter // 100.0
```

## 다중 분해

유전율은 여러 **동등한 분해식**을 통해 도달할 수 있으며, 모두 동일한 값-동등 유전율을 생성합니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `capacitance / length` | `KPermittivityUnitInstance` | `ε = C · d / A`, 기하 계수 `d / A`는 길이입니다 |
| `electricFluxDensity / electricFieldStrength` | `KPermittivityUnitInstance` | `ε = D / E` |
| `(time⁴·current²)/(mass·length³)` | `.toPermittivity()`를 통해 | 네이티브 정규 `kg⁻¹·m⁻³·s⁴·A²` 표현식 |

타입이 지정된 연산자 형식은 유전율을 직접 반환합니다. 완전히 네이티브인 표현식은 일반적인
`KMixedUnitInstance`로 남아 있으며 `toPermittivity()`로 좁혀집니다(이는 정규 형식만 인식하고 그렇지
않으면 `IllegalStateException`을 발생시킵니다). 모든 경로는 값-동등합니다.

역연산자는 정전용량, 길이, 두 장 관련량을 함께 묶습니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `permittivity * length` | `KCapacitanceUnitInstance` | `C = ε · A / d` (교환 가능) |
| `capacitance / permittivity` | `KLengthUnitInstance` | 기하 계수 `A / d = C / ε` |
| `permittivity * electricFieldStrength` | `KElectricFluxDensityUnitInstance` | `D = ε · E` (교환 가능) |
| `electricFluxDensity / permittivity` | `KElectricFieldStrengthUnitInstance` | `E = D / ε` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.capacitance.farads
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.electric.fluxdensity.coulombsPerSquareMeter
import org.pcsoft.framework.kunit.electric.permittivity.*

// 실제 사례 - 진공에서 1 MV/m의 장은 8.854 µC/m²의 전속 밀도를 만듭니다.
val d = (1 of vacuumPermittivity) * (1_000_000 of voltsPerMeter)  // 8.8541878188e-6 C/m²

// 유전율에 대해 풀어낸 정의:
val eps = (6 of coulombsPerSquareMeter) / (3 of voltsPerMeter)    // 2 F/m
val fromCapacitance = (10 of farads) / (5 of meters)              // 2 F/m

// 네이티브 kg⁻¹·m⁻³·s⁴·A² 표현식으로서의 동일한 유전율:
val raw = 2 of ((seconds pow 4) * (amperes pow 2)) / (kilo.grams * (meters pow 3))
raw.toPermittivity() == (2 of faradsPerMeter)                     // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.permittivity.*

val s = (1 of faradsPerMeter) + (1 of faradsPerCentimeter)  // 101 F/m
(1 of faradsPerCentimeter) > (1 of faradsPerMeter)          // true
(2 of faradsPerMeter) * (3 of faradsPerMeter)               // KMixedUnitInstance (그룹을 벗어남)
```

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.permittivity.*

(1 of faradsPerCentimeter).toString()   // "100.0 F/m" (기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를
보여줍니다. 지수는 유니코드 위첨자(`⁴`, `⁻³`)를 사용하며, `·`는 곱셈을, `/`는 분수를 나타냅니다. 어떤 양이
분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `F/m` | `faradsPerMeter` | 유전율, 기본 단위(이름이 붙은 토큰, 미터당 패럿) |
| `ε₀` | `vacuumPermittivity` | 진공 유전율 상수, 8.854 pF/m |
| `D / E` | `(6 of coulombsPerSquareMeter) / (3 of voltsPerMeter)` | 전속 밀도와 장 세기의 비로서의 유전율 |
| `C · (d/A)` | `(10 of farads) / (5 of meters)` | 정전용량과 기하 계수로부터의 유전율 |
| `(s⁴·A²)/(kg·m³)` | `((seconds pow 4) * (amperes pow 2)) / (kilo.grams * (meters pow 3))` | (시간⁴·전류²) / (질량·길이³)로서의 유전율(분수 형식) |
| `kg⁻¹·m⁻³·s⁴·A²` | `(kilo.grams pow -1) * (meters pow -3) * (seconds pow 4) * (amperes pow 2)` | 순수 곱으로서의 동일한 유전율 |
| `pF/m` | `pico.faradsPerMeter` | 접두사가 붙은 유전율(피코패럿 매 미터) |
