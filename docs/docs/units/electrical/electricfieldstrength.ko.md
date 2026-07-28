# 전기장 세기 (Electric Field Strength)

패키지: `org.pcsoft.framework.kunit.electric.electricfieldstrength`
기본 단위: **미터당 볼트(volt per meter)** (`KElectricFieldStrengthUnit.BASE == KElectricFieldStrengthUnit.VOLT_PER_METER`)

유형: **구성 단위(constructed unit)**

전기장 세기는 **구성** 단위입니다: `질량 · 길이 · 시간⁻³ · 전류⁻¹`
(`kg·m·s⁻³·A⁻¹`)의 조합입니다. `KElectricFieldStrengthUnitInstance`는 네 개의 항으로 이루어진
`KMixedUnitInstance`를 감쌉니다 — `KMassUnit.BASE`(그램)는 `+1`, `KDistanceUnit.BASE`(미터)는 `+1`,
`KTimeUnit.BASE`(초)는 `-3`, `KElectricCurrentUnit.BASE`(암페어)는 `-1`입니다. 라이브러리의 질량 구성
요소는 **그램**(킬로그램이 아님)으로 정규화되어 있으므로, 정규 곱은 미터당 볼트에 도달하기 위해 1000으로
나뉩니다; 저장된 값은 항상 미터당 볼트로 정규화됩니다.

장 세기 `E`는 단위 길이당 전압 강하이며, 동시에 단위 전하에 작용하는 힘입니다. 이는
[전속 밀도](electricfluxdensity.md)와 [유전율](permittivity.md)을 통해 연결되며(`D = ε · E`),
전하 운반자를 [전기 이동도](electricmobility.md)로 주어지는 속도로 움직이게 합니다(`v = μ · E`).

## 전기장 세기 만들기

이름이 붙은 토큰으로 전기장 세기를 만들거나, 아래의 분해식으로부터 만들 수 있습니다. 이름이 붙은 단위는
값 1의 토큰으로 존재합니다(`of`/`into`와 함께 사용):

| 장 세기 | 기호 | 토큰 | V/m 단위로 1 |
|---|---|---:|---:|
| 미터당 볼트 | `V/m` | `voltsPerMeter` | 1.0 |
| 센티미터당 볼트 | `V/cm` | `voltsPerCentimeter` | 100.0 |
| 센티미터당 스탯볼트(CGS-ESU) | `statV/cm` | `statvoltsPerCentimeter` | 29979.2458 |

이름이 붙은 단위는 `KPrefixBuilder`를 통해 SI 접두사를 지원합니다(`kilo.voltsPerMeter`,
`mega.voltsPerMeter`, `kilo.voltsPerCentimeter` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.electric.fieldstrength.*

val e = 3 of mega.voltsPerMeter        // 공기의 유전 강도
e into mega.voltsPerMeter              // 3.0
e into voltsPerMeter                   // 3.0e6
(1 of voltsPerCentimeter) into voltsPerMeter // 100.0
```

## 다중 분해

전기장 세기는 여러 **동등한 분해식**을 통해 도달할 수 있으며, 모두 동일한 값-동등 장 세기를 생성합니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `voltage / length` | `KElectricFieldStrengthUnitInstance` | `E = U / l`, 단위 길이당 전압 강하 |
| `force / charge` | `KElectricFieldStrengthUnitInstance` | `E = F / Q`, 단위 전하에 작용하는 힘 |
| `mass·length/(time³·current)` | `.toElectricFieldStrength()`를 통해 | 네이티브 정규 `kg·m·s⁻³·A⁻¹` 표현식 |

타입이 지정된 연산자 형식은 장 세기를 직접 반환합니다. 완전히 네이티브인 표현식은 일반적인
`KMixedUnitInstance`로 남아 있으며 `toElectricFieldStrength()`로 좁혀집니다(이는 정규 형식만 인식하고
그렇지 않으면 `IllegalStateException`을 발생시킵니다). 모든 경로는 값-동등합니다.

역연산자는 전압, 길이, 힘, 전하, 장 세기를 함께 묶습니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `electricFieldStrength * length` | `KVoltageUnitInstance` | `U = E · l` (교환 가능) |
| `voltage / electricFieldStrength` | `KLengthUnitInstance` | `l = U / E` |
| `electricFieldStrength * charge` | `KForceUnitInstance` | `F = E · Q` (교환 가능) |
| `force / electricFieldStrength` | `KChargeUnitInstance` | `Q = F / E` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.electric.fieldstrength.*

// 실제 사례 - 2 mm 공기 간극에 걸친 상용 전원 전압은 115 kV/m을 만듭니다.
val e = (230 of volts) / (2 of milli.meters)   // KElectricFieldStrengthUnitInstance, 115000 V/m

// 힘 분해식으로부터 얻은 동일한 장 세기:
val fromForce = (6 of newtons) / (3 of coulombs)  // 2 V/m

// 네이티브 kg·m·s⁻³·A⁻¹ 표현식으로서의 동일한 장 세기:
val raw = 2 of (kilo.grams * (meters pow 1)) / ((seconds pow 3) * (amperes pow 1))
raw.toElectricFieldStrength() == (2 of voltsPerMeter)  // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.fieldstrength.*

val s = (1 of voltsPerMeter) + (1 of voltsPerCentimeter)  // 101 V/m
(1 of voltsPerCentimeter) > (1 of voltsPerMeter)          // true
(2 of voltsPerMeter) * (3 of voltsPerMeter)               // KMixedUnitInstance (그룹을 벗어남)
```

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.fieldstrength.*

(1 of voltsPerCentimeter).toString()   // "100.0 V/m" (기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를
보여줍니다. 지수는 유니코드 위첨자(`³`, `⁻¹`)를 사용하며, `·`는 곱셈을, `/`는 분수를 나타냅니다. 어떤 양이
분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `V/m` | `voltsPerMeter` | 전기장 세기, 기본 단위(이름이 붙은 토큰, 미터당 볼트) |
| `U / l` | `(230 of volts) / (2 of milli.meters)` | 거리에 걸친 전압으로부터의 장 세기 |
| `F / Q` | `(6 of newtons) / (3 of coulombs)` | 단위 전하당 힘으로서의 장 세기 |
| `kg·m/(s³·A)` | `(kilo.grams * (meters pow 1)) / ((seconds pow 3) * (amperes pow 1))` | 질량·길이 / (시간³·전류)로서의 장 세기(분수 형식) |
| `kg·m·s⁻³·A⁻¹` | `kilo.grams * (meters pow 1) * (seconds pow -3) * (amperes pow -1)` | 순수 곱으로서의 동일한 장 세기 |
| `kV/m` | `kilo.voltsPerMeter` | 접두사가 붙은 장 세기(킬로볼트 매 미터) |
