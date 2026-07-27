# 전도율 (Conductivity)

패키지: `org.pcsoft.framework.kunit.conductivity`
기본 단위: **미터당 지멘스(siemens per meter)** (`KConductivityUnit.BASE == KConductivityUnit.SIEMENS_PER_METER`)

유형: **구성 단위(constructed unit)**

전기 전도율은 **구성** 단위입니다: `질량⁻¹ · 길이⁻³ · 시간³ · 전류²`
(`kg⁻¹·m⁻³·s³·A²`)의 조합입니다. `KConductivityUnitInstance`는 네 개의 항으로 이루어진
`KMixedUnitInstance`를 감쌉니다 — `KMassUnit.BASE`(그램)는 `-1`, `KDistanceUnit.BASE`(미터)는 `-3`,
`KTimeUnit.BASE`(초)는 `+3`, `KElectricCurrentUnit.BASE`(암페어)는 `+2`입니다. 라이브러리의 질량 구성
요소는 **그램**(킬로그램이 아님)으로 정규화되어 있고 질량 지수가 *음수*이므로, 정규 곱은 미터당 지멘스에
도달하기 위해 1000을 곱합니다; 저장된 값은 항상 S/m으로 정규화됩니다.

전도율은 컨덕턴스의 배후에 있는 물질 특성이며 [저항률](resistivity.md)의 역수입니다(`σ = 1 / ρ`).

## 전도율 만들기

이름이 붙은 토큰으로 전도율을 만들거나, 아래의 분해식으로부터 만들 수 있습니다. 이름이 붙은 단위는
값 1의 토큰으로 존재합니다(`of`/`into`와 함께 사용):

| 전도율 | 기호 | 토큰 | S/m 단위로 1 |
|---|---|---:|---:|
| 미터당 지멘스 | `S/m` | `siemensPerMeter` | 1.0 |
| 센티미터당 지멘스 | `S/cm` | `siemensPerCentimeter` | 100.0 |
| 센티미터당 마이크로지멘스 | `µS/cm` | `microsiemensPerCentimeter` | 1.0e-4 |
| 미터당 메가지멘스 | `MS/m` | `megasiemensPerMeter` | 1.0e6 |

이름이 붙은 단위는 `KPrefixBuilder`를 통해 SI 접두사를 지원합니다(`mega.siemensPerMeter`,
`milli.siemensPerMeter` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.conductivity.*

val sigma = 58 of mega.siemensPerMeter        // 구리
sigma into mega.siemensPerMeter               // 58.0
sigma into siemensPerMeter                    // 5.8e7
(1 of siemensPerCentimeter) into siemensPerMeter // 100.0
```

## 다중 분해

전도율은 여러 **동등한 분해식**을 통해 도달할 수 있으며, 모두 동일한 값-동등 전도율을 생성합니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `1 / resistivity` | `KConductivityUnitInstance` | 역수 `σ = 1 / ρ` |
| `conductance / length` | `KConductivityUnitInstance` | `σ = G · l / A`; 기하 계수 `l / A`는 길이의 역수이므로 나눗셈으로 표현 |
| `current²·time³/(mass·length³)` | `.toConductivity()`를 통해 | 네이티브 정규 `kg⁻¹·m⁻³·s³·A²` 표현식 |

타입이 지정된 연산자 형식은 전도율을 직접 반환합니다. 완전히 네이티브인 표현식은 일반적인
`KMixedUnitInstance`로 남아 있으며 `toConductivity()`로 좁혀집니다(이는 정규 형식만 인식하고 그렇지
않으면 `IllegalStateException`을 발생시킵니다). 모든 경로는 값-동등합니다.

역연산자는 컨덕턴스, 길이, 전도율을 함께 묶습니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `conductivity * length` | `KConductanceUnitInstance` | `G = σ · A / l` (교환 가능) |
| `conductance / conductivity` | `KLengthUnitInstance` | 기하 계수 `A / l = G / σ` |
| `1 / conductivity` | `KResistivityUnitInstance` | 저항률로 다시 돌아감 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.conductance.siemens
import org.pcsoft.framework.kunit.resistivity.ohmMeters
import org.pcsoft.framework.kunit.conductivity.*

// 실제 사례 - 구리: 17 nΩ·m의 저항률은 약 58.8 MS/m의 전도율입니다.
val sigma = 1 / (17 of nano.ohmMeters)
sigma into mega.siemensPerMeter               // 58.82352941176471

// 도체 형상에 대한 컨덕턴스:
val fromConductance = (10 of siemens) / (5 of meters)  // KConductivityUnitInstance, 2 S/m

// 네이티브 kg⁻¹·m⁻³·s³·A² 표현식으로서의 동일한 전도율:
val raw = 2 of ((amperes pow 2) * (seconds pow 3)) / (kilo.grams * (meters pow 3))
raw.toConductivity() == (2 of siemensPerMeter) // true

// 역수 쌍은 대칭적입니다:
1 / (2 of siemensPerMeter) into ohmMeters      // 0.5
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.conductivity.*

val s = (100 of siemensPerMeter) + (40 of siemensPerMeter)  // 140 S/m
(100 of siemensPerMeter) > (40 of siemensPerMeter)          // true
(100 of siemensPerMeter) * (40 of siemensPerMeter)          // KMixedUnitInstance (그룹을 벗어남)
```

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.conductivity.*

(1 of siemensPerCentimeter).toString()   // "100.0 S/m" (기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를
보여줍니다. 지수는 유니코드 위첨자(`³`, `⁻¹`)를 사용하며, `·`는 곱셈을, `/`는 분수를 나타냅니다. 어떤 양이
분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `S/m` | `siemensPerMeter` | 전도율, 기본 단위(이름이 붙은 토큰, 미터당 지멘스) |
| `1 / ρ` | `1 / (17 of nano.ohmMeters)` | 역수 저항률로서의 전도율 |
| `A²·s³/(kg·m³)` | `((amperes pow 2) * (seconds pow 3)) / (kilo.grams * (meters pow 3))` | 전류²·시간³ / (질량·길이³)로서의 전도율(분수 형식) |
| `kg⁻¹·m⁻³·s³·A²` | `(kilo.grams pow -1) * (meters pow -3) * (seconds pow 3) * (amperes pow 2)` | 순수 곱으로서의 동일한 전도율 |
| `MS/m` | `mega.siemensPerMeter` | 접두사가 붙은 전도율(메가지멘스 매 미터) |
