# 자기 릴럭턴스 (Magnetic Reluctance)

패키지: `org.pcsoft.framework.kunit.electric.reluctance`
기본 단위: **웨버당 암페어 (ampere per weber)** (`KReluctanceUnit.BASE == KReluctanceUnit.AMPERE_PER_WEBER`)

유형: **구성 단위 (constructed unit)**

자기 릴럭턴스는 **구성** 단위입니다: `질량⁻¹ · 길이⁻² · 시간² · 전류²`
(`kg⁻¹·m⁻²·s²·A²` = `A/Wb` = `H⁻¹`)의 조합입니다. `KReluctanceUnitInstance`는 네 개의 항으로 이루어진
`KMixedUnitInstance`를 감쌉니다 — `KMassUnit.BASE`(그램)는 `-1`, `KDistanceUnit.BASE`(미터)는 `-2`,
`KTimeUnit.BASE`(초)는 `+2`, `KElectricCurrentUnit.BASE`(암페어)는 `+2`입니다. 라이브러리의 질량 구성 요소는 **그램**(킬로그램이 아님)으로 정규화되어 있고 질량
지수가 *음수*이므로, 정규 곱은 웨버당 암페어에 도달하기 위해 1000을 곱합니다; 저장된 값은 항상 웨버당 암페어로 정규화됩니다.

릴럭턴스 `Rm`은 전기적 [저항](resistance.md)에 대응하는 자기 회로의 개념입니다: 이는 기자력 `Θ`
(암페어 턴으로 측정됨, [전류](ec.md) 참조)를 결과 [자속](magneticflux.md)과 홉킨슨 법칙
`Θ = Rm · Φ`을 통해 연결합니다. 그 역수는 **투자 (permeance)** `Λ`이며, 이는 헨리로 측정되므로
[인덕턴스](inductance.md) 그룹이 담당합니다.

## 릴럭턴스 만들기

이름이 붙은 토큰으로 릴럭턴스를 만들거나, 아래의 분해식으로부터 만들 수 있습니다. 이름이 붙은 단위는 값 1의 토큰으로 존재합니다 (`of`/`into`와 함께 사용):

| 릴럭턴스         | 기호    |                  토큰 | A/Wb 단위로 1 |
|------------------|---------|----------------------:|--------------:|
| 웨버당 암페어    | `A/Wb`  |     `amperesPerWeber` |           1.0 |
| 역헨리           | `H⁻¹`   |      `inverseHenries` |           1.0 |
| 웨버당 암페어 턴 | `At/Wb` | `ampereTurnsPerWeber` |           1.0 |

세 가지 표기 모두 동일한 양을 설명합니다 — 코일 감은 수는 순수한 개수이기 때문입니다 — 따라서 이들은 값-동등합니다; 별개의 기호는 관점의 차이를 나타냅니다. 이름이 붙은 단위는 `KPrefixBuilder`를
통해 SI 접두사를 지원합니다 (`mega.amperesPerWeber`, `kilo.inverseHenries` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.electric.reluctance.*

val rm = 2 of mega.amperesPerWeber    // 공극이 있는 철심
rm into mega.amperesPerWeber          // 2.0
rm into amperesPerWeber               // 2.0e6
(1 of amperesPerWeber) == (1 of inverseHenries) // true
```

## 다중 분해

릴럭턴스는 여러 **동등한 분해식**을 통해 도달할 수 있으며, 모두 동일한 값-동등 릴럭턴스를 생성합니다:

| 표현식                            | 결과 타입                 | 의미                                  |
|-----------------------------------|---------------------------|---------------------------------------|
| `current / magneticFlux`          | `KReluctanceUnitInstance` | 홉킨슨 법칙 `Rm = Θ / Φ`              |
| `1 / inductance`                  | `KReluctanceUnitInstance` | 투자의 역수, `Rm = 1 / Λ`             |
| `(time²·current²)/(mass·length²)` | `.toReluctance()`를 통해  | 네이티브 정규 `kg⁻¹·m⁻²·s²·A²` 표현식 |

타입이 지정된 연산자 형식은 릴럭턴스를 직접 반환합니다. 완전히 네이티브인 표현식은 일반적인
`KMixedUnitInstance`로 남아 있으며 `toReluctance()`로 좁혀집니다 (이는 정규 형식만 인식하고 그렇지 않으면 `IllegalStateException`을 발생시킵니다). 모든 경로는
값-동등합니다.

역연산자는 기자력, 자속, 투자, 릴럭턴스를 함께 묶습니다:

| 표현식                      | 결과 타입                      | 의미                          |
|-----------------------------|--------------------------------|-------------------------------|
| `reluctance * magneticFlux` | `KElectricCurrentUnitInstance` | `Θ = Rm · Φ` (교환 가능)      |
| `current / reluctance`      | `KMagneticFluxUnitInstance`    | `Φ = Θ / Rm`                  |
| `1 / reluctance`            | `KInductanceUnitInstance`      | 투자 `Λ = 1 / Rm` (헨리 단위) |

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
import org.pcsoft.framework.kunit.electric.current.ampereTurns
import org.pcsoft.framework.kunit.electric.magneticflux.webers
import org.pcsoft.framework.kunit.electric.inductance.henries
import org.pcsoft.framework.kunit.electric.reluctance.*

// 실제 사례 - 2 kAt의 기자력이 2 MA/Wb 철심을 통과하면 1 mWb의 자속이 생깁니다.
val rm = 2_000_000 of amperesPerWeber
val flux = (2000 of ampereTurns) / rm       // KMagneticFluxUnitInstance
flux into milli.webers                      // 1.0

// 릴럭턴스에 대해 풀어낸 정의:
val fromHopkinson = (6 of amperes) / (3 of webers)   // 2 A/Wb
val fromPermeance = 1 / (0.5 of henries)             // 2 A/Wb

// 네이티브 kg⁻¹·m⁻²·s²·A² 표현식으로서의 동일한 릴럭턴스:
val raw = 2 of ((seconds pow 2) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
raw.toReluctance() == (2 of amperesPerWeber)         // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.reluctance.*

val series = (1 of amperesPerWeber) + (1 of inverseHenries)  // 2 A/Wb (직렬 자기 회로)
(3 of amperesPerWeber) > (2 of amperesPerWeber)              // true
(2 of amperesPerWeber) * (3 of amperesPerWeber)              // KMixedUnitInstance (그룹을 벗어남)
```

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.reluctance.*

(2 of inverseHenries).toString()   // "2.0 A/Wb" (기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를 보여줍니다. 지수는 유니코드 위첨자 (`²`, `⁻²`)를 사용하며, `·`는 곱셈을, `/`는
분수를 나타냅니다. 어떤 양이 분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학              | Kotlin                                                                      | 의미                                                   |
|-------------------|-----------------------------------------------------------------------------|--------------------------------------------------------|
| `A/Wb`            | `amperesPerWeber`                                                           | 릴럭턴스, 기본 단위(이름이 붙은 토큰, 웨버당 암페어)   |
| `H⁻¹`             | `inverseHenries`                                                            | 동일한 양의 역인덕턴스 표기                            |
| `Θ / Φ`           | `(6 of amperes) / (3 of webers)`                                            | 홉킨슨 법칙으로부터의 릴럭턴스                         |
| `1 / Λ`           | `1 / (0.5 of henries)`                                                      | 투자의 역수로서의 릴럭턴스                             |
| `(s²·A²)/(kg·m²)` | `((seconds pow 2) * (amperes pow 2)) / (kilo.grams * (meters pow 2))`       | (시간²·전류²) / (질량·길이²)로서의 릴럭턴스(분수 형식) |
| `kg⁻¹·m⁻²·s²·A²`  | `(kilo.grams pow -1) * (meters pow -2) * (seconds pow 2) * (amperes pow 2)` | 순수 곱으로서의 동일한 릴럭턴스                        |
| `MA/Wb`           | `mega.amperesPerWeber`                                                      | 접두사가 붙은 릴럭턴스(메가암페어 매 웨버)             |
