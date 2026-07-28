# 저항률 (Resistivity)

패키지: `org.pcsoft.framework.kunit.electric.resistivity`
기본 단위: **옴미터(ohm meter)** (`KResistivityUnit.BASE == KResistivityUnit.OHM_METER`)

유형: **구성 단위(constructed unit)**

전기 저항률은 **구성** 단위입니다: `질량 · 길이³ · 시간⁻³ · 전류⁻²`
(`kg·m³·s⁻³·A⁻²`)의 조합입니다. `KResistivityUnitInstance`는 네 개의 항으로 이루어진
`KMixedUnitInstance`를 감쌉니다 — `KMassUnit.BASE`(그램)는 `+1`, `KDistanceUnit.BASE`(미터)는 `+3`,
`KTimeUnit.BASE`(초)는 `-3`, `KElectricCurrentUnit.BASE`(암페어)는 `-2`입니다. 라이브러리의 질량 구성
요소는 **그램**(킬로그램이 아님)으로 정규화되어 있으므로, 정규 곱은 옴미터에 도달하기 위해 1000으로
나뉩니다; 저장된 값은 항상 옴미터로 정규화됩니다.

저항률은 저항의 배후에 있는 물질 특성이며 [전도율](conductivity.md)의 역수입니다(`ρ = 1 / σ`).

## 저항률 만들기

이름이 붙은 토큰으로 저항률을 만들거나, 아래의 분해식으로부터 만들 수 있습니다. 이름이 붙은 단위는
값 1의 토큰으로 존재합니다(`of`/`into`와 함께 사용):

| 저항률 | 기호 | 토큰 | Ω·m 단위로 1 |
|---|---|---:|---:|
| 옴미터 | `Ω·m` | `ohmMeters` | 1.0 |
| 옴센티미터 | `Ω·cm` | `ohmCentimeters` | 0.01 |
| 스탯옴센티미터(CGS-ESU) | `statΩ·cm` | `statohmCentimeters` | 8.98755179e9 |

이름이 붙은 단위는 `KPrefixBuilder`를 통해 SI 접두사를 지원합니다(`nano.ohmMeters`,
`micro.ohmMeters`, `milli.ohmCentimeters` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.electric.resistivity.*

val rho = 17 of nano.ohmMeters     // 구리
rho into nano.ohmMeters            // 17.0
rho into ohmMeters                 // 1.7e-8
(1 of ohmMeters) into ohmCentimeters // 100.0
```

## 다중 분해

저항률은 여러 **동등한 분해식**을 통해 도달할 수 있으며, 모두 동일한 값-동등 저항률을 생성합니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `resistance * length` | `KResistivityUnitInstance` | `ρ = R · A / l`, 기하 계수 `A / l`은 길이입니다(교환 가능) |
| `1 / conductivity` | `KResistivityUnitInstance` | 역수 `ρ = 1 / σ` |
| `mass·length³/(time³·current²)` | `.toResistivity()`를 통해 | 네이티브 정규 `kg·m³·s⁻³·A⁻²` 표현식 |

타입이 지정된 연산자 형식은 저항률을 직접 반환합니다. 완전히 네이티브인 표현식은 일반적인
`KMixedUnitInstance`로 남아 있으며 `toResistivity()`로 좁혀집니다(이는 정규 형식만 인식하고 그렇지
않으면 `IllegalStateException`을 발생시킵니다). 모든 경로는 값-동등합니다.

역연산자는 저항, 길이, 저항률을 함께 묶습니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `resistivity / length` | `KResistanceUnitInstance` | `R = ρ · l / A` |
| `resistivity / resistance` | `KLengthUnitInstance` | 기하 계수 `A / l = ρ / R` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.resistance.ohms
import org.pcsoft.framework.kunit.electric.resistivity.*

// 실제 사례 - 구리 배선: 1 mm의 기하 계수에 대해 17 nΩ·m은 17 µΩ이 됩니다.
val r = (17 of nano.ohmMeters) / (1 of milli.meters)  // KResistanceUnitInstance, 1.7e-5 Ω

// 저항률에 대해 풀어낸 정의:
val rho = (5 of ohms) * (0.4 of meters)               // KResistivityUnitInstance, 2 Ω·m

// 네이티브 kg·m³·s⁻³·A⁻² 표현식으로서의 동일한 저항률:
val raw = 2 of (kilo.grams * (meters pow 3)) / ((seconds pow 3) * (amperes pow 2))
raw.toResistivity() == (2 of ohmMeters)               // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.resistivity.*

val s = (100 of ohmMeters) + (40 of ohmMeters)  // 140 Ω·m
(100 of ohmMeters) > (40 of ohmMeters)          // true
(100 of ohmMeters) * (40 of ohmMeters)          // KMixedUnitInstance (그룹을 벗어남)
```

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.resistivity.*

(1 of ohmCentimeters).toString()   // "0.01 Ω·m" (기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를
보여줍니다. 지수는 유니코드 위첨자(`³`, `⁻²`)를 사용하며, `·`는 곱셈을, `/`는 분수를 나타냅니다. 어떤 양이
분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `Ω·m` | `ohmMeters` | 저항률, 기본 단위(이름이 붙은 토큰, 옴미터) |
| `R · (A/l)` | `(5 of ohms) * (0.4 of meters)` | 저항과 기하 계수로부터 계산된 저항률 |
| `kg·m³/(s³·A²)` | `(kilo.grams * (meters pow 3)) / ((seconds pow 3) * (amperes pow 2))` | 질량·길이³ / (시간³·전류²)로서의 저항률(분수 형식) |
| `kg·m³·s⁻³·A⁻²` | `kilo.grams * (meters pow 3) * (seconds pow -3) * (amperes pow -2)` | 순수 곱으로서의 동일한 저항률 |
| `nΩ·m` | `nano.ohmMeters` | 접두사가 붙은 저항률(나노옴미터) |
