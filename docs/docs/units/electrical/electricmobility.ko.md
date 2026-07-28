# 전기 이동도 (Electric Mobility)

패키지: `org.pcsoft.framework.kunit.electricmobility`
기본 단위: **볼트초당 제곱미터(square meter per volt second)**
(`KElectricMobilityUnit.BASE == KElectricMobilityUnit.SQUARE_METER_PER_VOLT_SECOND`)

유형: **구성 단위(constructed unit)**

전기 이동도는 **구성** 단위입니다: `질량⁻¹ · 시간² · 전류`
(`kg⁻¹·s²·A` = `m²/(V·s)`)의 조합입니다. `KElectricMobilityUnitInstance`는 세 개의 항으로 이루어진
`KMixedUnitInstance`를 감쌉니다 — `KMassUnit.BASE`(그램)는 `-1`, `KTimeUnit.BASE`(초)는 `+2`,
`KElectricCurrentUnit.BASE`(암페어)는 `+1`입니다. 길이 차원은 볼트가 이미 `m²`을 포함하고 있으므로
상쇄되며, 정규 형식은 세 개의 항만 갖습니다. 라이브러리의 질량 구성 요소는 **그램**(킬로그램이 아님)으로
정규화되어 있고 질량 지수가 *음수*이므로, 정규 곱은 기본 단위에 도달하기 위해 1000을 곱합니다; 저장된
값은 항상 볼트초당 제곱미터로 정규화됩니다.

전기 이동도 `μ`는 전하 운반자가 전기장 안에서 얼마나 빨리 이동하는지를 나타냅니다: `v = μ · E`, 여기서
`E`는 [전기장 세기](electricfieldstrength.md)입니다.

## 전기 이동도 만들기

이름이 붙은 토큰으로 이동도를 만들거나, 아래의 분해식으로부터 만들 수 있습니다. 이름이 붙은 단위는
값 1의 토큰으로 존재합니다(`of`/`into`와 함께 사용):

| 이동도 | 기호 | 토큰 | m²/(V·s) 단위로 1 |
|---|---|---:|---:|
| 볼트초당 제곱미터 | `m²/(V·s)` | `squareMetersPerVoltSecond` | 1.0 |
| 볼트초당 제곱센티미터 | `cm²/(V·s)` | `squareCentimetersPerVoltSecond` | 1.0e-4 |

센티미터 형식은 반도체 물리학 전반에서 사용되는 표기법입니다. 이름이 붙은 단위는 `KPrefixBuilder`를
통해 SI 접두사를 지원합니다(`milli.squareMetersPerVoltSecond`, `kilo.squareCentimetersPerVoltSecond`
등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electricmobility.*

val mu = 1400 of squareCentimetersPerVoltSecond   // 실리콘 내 전자 이동도
mu into squareCentimetersPerVoltSecond            // 1400.0
mu into squareMetersPerVoltSecond                 // 0.14
```

## 다중 분해

전기 이동도는 여러 **동등한 분해식**을 통해 도달할 수 있으며, 모두 동일한 값-동등 이동도를 생성합니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `speed / electricFieldStrength` | `KElectricMobilityUnitInstance` | `μ = v / E`, 단위 장에 대한 드리프트 속도 |
| `(time²·current)/mass` | `.toElectricMobility()`를 통해 | 네이티브 정규 `kg⁻¹·s²·A` 표현식 |

타입이 지정된 연산자 형식은 이동도를 직접 반환합니다. 완전히 네이티브인 표현식은 일반적인
`KMixedUnitInstance`로 남아 있으며 `toElectricMobility()`로 좁혀집니다(이는 정규 형식만 인식하고
그렇지 않으면 `IllegalStateException`을 발생시킵니다). 두 경로 모두 값-동등합니다.

역연산자는 드리프트 속도, 장 세기, 이동도를 함께 묶습니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `electricMobility * electricFieldStrength` | `KSpeedUnitInstance` | `v = μ · E` (교환 가능) |
| `speed / electricMobility` | `KElectricFieldStrengthUnitInstance` | `E = v / μ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.speed.div
import org.pcsoft.framework.kunit.electricfieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.electricmobility.*

// 실제 사례 - 1400 cm²/(V·s)의 실리콘 전자는 1 kV/m의 장에서 140 m/s로 이동합니다.
val v = (1400 of squareCentimetersPerVoltSecond) * (1000 of voltsPerMeter)  // KSpeedUnitInstance, 140 m/s

// 이동도에 대해 풀어낸 정의:
val mu = ((6 of meters) / (1 of seconds)) / (3 of voltsPerMeter)   // 2 m²/(V·s)

// 네이티브 kg⁻¹·s²·A 표현식으로서의 동일한 이동도:
val raw = 2 of ((seconds pow 2) * (amperes pow 1)) / (kilo.grams pow 1)
raw.toElectricMobility() == (2 of squareMetersPerVoltSecond)       // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricmobility.*

val s = (1 of squareMetersPerVoltSecond) + (1 of squareCentimetersPerVoltSecond)  // 1.0001 m²/(V·s)
(1 of squareMetersPerVoltSecond) > (1 of squareCentimetersPerVoltSecond)          // true
(2 of squareMetersPerVoltSecond) * (3 of squareMetersPerVoltSecond)               // KMixedUnitInstance
```

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricmobility.*

(1400 of squareCentimetersPerVoltSecond).toString()   // "0.14 m²/(V·s)" (기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를
보여줍니다. 지수는 유니코드 위첨자(`²`, `⁻¹`)를 사용하며, `·`는 곱셈을, `/`는 분수를 나타냅니다. 어떤 양이
분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `m²/(V·s)` | `squareMetersPerVoltSecond` | 전기 이동도, 기본 단위(이름이 붙은 토큰) |
| `cm²/(V·s)` | `squareCentimetersPerVoltSecond` | 반도체 물리학 표기법, 1e-4 m²/(V·s) |
| `v / E` | `((6 of meters) / (1 of seconds)) / (3 of voltsPerMeter)` | 드리프트 속도와 장 세기의 비로서의 이동도 |
| `μ · E` | `(1400 of squareCentimetersPerVoltSecond) * (1000 of voltsPerMeter)` | 주어진 장에서의 드리프트 속도 |
| `(s²·A)/kg` | `((seconds pow 2) * (amperes pow 1)) / (kilo.grams pow 1)` | (시간²·전류) / 질량으로서의 이동도(분수 형식) |
| `kg⁻¹·s²·A` | `(kilo.grams pow -1) * (seconds pow 2) * (amperes pow 1)` | 순수 곱으로서의 동일한 이동도 |
