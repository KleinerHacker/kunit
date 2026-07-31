# 인덕턴스

패키지: `org.pcsoft.framework.kunit.electric.inductance`
기본 단위: **헨리**(`KInductanceUnit.BASE == KInductanceUnit.HENRY`)

유형: **구성된 단위**

인덕턴스는 **구성된** 단위로, 합성 `mass · length² · time⁻² · current⁻²`(`kg·m²·s⁻²·A⁻²`)입니다.
`KInductanceUnitInstance` 는 네 개의 항 — 지수 `+1` 의 `KMassUnit.BASE`(그램), 지수 `+2` 의
`KDistanceUnit.BASE`(미터), 지수 `-2` 의 `KTimeUnit.BASE`(초), 지수 `-2` 의
`KElectricCurrentUnit.BASE`(암페어) — 을 감쌉니다. 라이브러리의 질량 성분은 **그램**(킬로그램이 아님)으로 정규화되므로 헨리는 원시 성분 기준의 1000배입니다. 저장되는 값은 헨리로
정규화됩니다.

## 인덕턴스 만들기

명명된 토큰으로, 또는 분해 (아래 참조)로 인덕턴스를 만듭니다. 명명된 단위는 값이 1인 토큰으로 남습니다 (`of`/`into` 와 함께 사용):

| 인덕턴스          | 기호    |              토큰 |  1 단위의 H 값 |
|-------------------|---------|------------------:|---------------:|
| 헨리              | `H`     |         `henries` |            1.0 |
| 웨버 매 암페어    | `Wb/A`  | `webersPerAmpere` |            1.0 |
| 앱헨리(CGS-EMU)   | `abH`   |       `abhenries` |         1.0e-9 |
| 스탯헨리(CGS-ESU) | `statH` |     `stathenries` | 8.987551787e11 |

명명된 단위는 `KPrefixBuilder` 를 통해 SI 접두사를 지원합니다 (`milli.henries`, `micro.henries`,
`nano.henries` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.inductance.*

val l = 470 of micro.henries
l into henries               // 0.00047
l into milli.henries         // 0.47
(1 of henries) into milli.henries  // 1000.0
```

## 여러 분해

인덕턴스는 여러 **동등한 분해**로 얻을 수 있으며, 모두 값이 같은 인덕턴스를 생성합니다:

| 식                              | 결과 형식                 | 의미                                        |
|---------------------------------|---------------------------|---------------------------------------------|
| `flux / current`                | `KInductanceUnitInstance` | 정의 `L = Φ / I`                            |
| `resistance / frequency`        | `KInductanceUnitInstance` | 리액턴스 형태 `L = X / ω`(`Ω/Hz = Ω·s = H`) |
| `mass·length²/(time²·current²)` | `.toInductance()` 경유    | 네이티브 정규형 `kg·m²·s⁻²·A⁻²` 식          |

형식이 지정된 연산자 형태는 인덕턴스를 직접 반환합니다. 완전한 네이티브 식은 일반적인 `KMixedUnitInstance` 로 남아 있으며 `toInductance()`(정규형만 인식하고 그렇지 않으면
`IllegalStateException` 을 던짐)로 좁힙니다. 모든 경로는 값이 같습니다.

역연산자는 자기 선속, 전류, 주파수, 저항을 연결합니다:

| 식                       | 결과 형식                      | 의미                   |
|--------------------------|--------------------------------|------------------------|
| `inductance * current`   | `KMagneticFluxUnitInstance`    | `Φ = L · I`(교환 가능) |
| `flux / inductance`      | `KElectricCurrentUnitInstance` | `I = Φ / L`            |
| `inductance * frequency` | `KResistanceUnitInstance`      | `X = ω · L`            |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.frequency.hertz
import org.pcsoft.framework.kunit.electric.magneticflux.webers
import org.pcsoft.framework.kunit.electric.resistance.ohms
import org.pcsoft.framework.kunit.electric.inductance.*

// 실제 예 - 스위칭 전원의 초크 코일: 470 µH 코일에 2 A 가 흐르면 쇄교 자속은 0.00094 Wb 이고,
// 각주파수 100 kHz 에서는 리액턴스가 47 Ω 이 됩니다.
val l = 470 of micro.henries
val flux = l * (2 of amperes)          // KMagneticFluxUnitInstance, 0.00094 Wb
val x = l * (100_000 of hertz)         // KResistanceUnitInstance, 47 Ω

// 같은 인덕턴스를 정의식과 리액턴스 형태로:
(flux / (2 of amperes)) == l           // true
((47 of ohms) / (100_000 of hertz)) == l  // true

// 같은 인덕턴스를 네이티브 kg·m²·s⁻²·A⁻² 식으로:
val raw = 2 of (kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 2))
raw.toInductance() == (2 of henries)   // true
```

## 퍼미언스

자기 회로의 **퍼미언스**(permeance) `Λ` 는 [자기 릴럭턴스](reluctance.md)의 역수입니다,
`Λ = 1 / Rm`. 이는 인덕턴스와 **차원적으로 동일**하며 마찬가지로 헨리로 측정되므로, KUnit 은 이를 이 그룹과 기호 `H` 로 모델링합니다. 별도의 토큰이나 별도의 타입은 없습니다. 역수 연산자는
두 그룹을 서로 연결합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.inductance.*
import org.pcsoft.framework.kunit.electric.reluctance.*

// Rm = 500 A/Wb 인 자기 회로의 퍼미언스는 2 mH 입니다.
val permeance = 1 / (500 of amperesPerWeber)   // KInductanceUnitInstance
permeance into milli.henries                    // 2.0

// …그리고 다시 원래대로:
1 / (2 of milli.henries) == (500 of amperesPerWeber)  // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.inductance.*

val s = (100 of henries) + (40 of henries)  // 140 H
(100 of henries) > (40 of henries)          // true
(100 of henries) * (40 of henries)          // KMixedUnitInstance(그룹에서 벗어남)
```

## toString 형식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.inductance.*

(2 of henries).toString()     // "2.0 H"(기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 쓰는 방법과 KUnit 을 사용해 Kotlin 으로 쓰는 방법을 비교합니다. 지수는 유니코드 위첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·` 는 곱셈을,
`/` 는 분수를 나타냅니다. 어떤 양을 분수로도, 음의 지수를 가진 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형태를 모두 표시합니다.

| 수학            | Kotlin                                                              | 의미                                                  |
|-----------------|---------------------------------------------------------------------|-------------------------------------------------------|
| `H`             | `henries`                                                           | 인덕턴스, 기본 단위(명명된 토큰, 헨리)                |
| `Wb/A`          | `webersPerAmpere`                                                   | 웨버 매 암페어로서의 인덕턴스(명명된 토큰)            |
| `kg·m²/(s²·A²)` | `kilo.grams * (meters pow 2) / ((amperes pow 2) * (seconds pow 2))` | 질량·길이² / (시간²·전류²) 로서의 인덕턴스(분수 형태) |
| `kg·m²·s⁻²·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2) * (amperes pow -2)` | 같은 인덕턴스를 순수한 곱으로 표현                    |
| `mH`            | `milli.henries`                                                     | 접두사가 붙은 인덕턴스(밀리헨리)                      |
