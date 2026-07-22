# 저항

패키지: `org.pcsoft.framework.kunit.resistance`
기본 단위: **옴**(`KResistanceUnit.BASE == KResistanceUnit.OHM`)

유형: **구성된 단위**

전기 저항은 **구성된** 단위로, 합성 `mass · length² · time⁻³ · current⁻²`(`kg·m²·s⁻³·A⁻²`)입니다.
`KResistanceUnitInstance` 는 네 개의 항 — 지수 `+1` 의 `KMassUnit.BASE`(그램), 지수 `+2` 의
`KDistanceUnit.BASE`(미터), 지수 `-3` 의 `KTimeUnit.BASE`(초), 지수 `-2` 의
`KElectricCurrentUnit.BASE`(암페어) — 을 감쌉니다. 라이브러리의 질량 성분은 **그램**(킬로그램이 아님)으로
정규화되므로 옴은 원시 성분 기준의 1000배입니다. 저장되는 값은 옴으로 정규화됩니다.

## 저항 만들기

명명된 토큰으로, 또는 분해(아래 참조)로 저항을 만듭니다. 명명된 단위는 값이 1인 토큰으로 남습니다
(`of`/`into` 와 함께 사용):

| 저항 | 기호 | 토큰 | 1 단위의 Ω 값 |
|---|---|---:|---:|
| 옴 | `Ω` | `ohms` | 1.0 |
| 스탯옴(CGS-ESU) | `statΩ` | `statohms` | 8.98755179e11 |
| 앱옴(CGS-EMU) | `abΩ` | `abohms` | 1.0e-9 |
| 국제 옴 | `Ω_int` | `internationalOhms` | 1.000049 |
| 법정 옴(1884) | `Ω_leg` | `legalOhms` | 0.9972 |
| 지멘스 수은 단위 | `Ω_S` | `siemensUnits` | 0.9534 |

명명된 단위는 `KPrefixBuilder` 를 통해 SI 접두사를 지원합니다(`kilo.ohms`, `mega.ohms`, `milli.ohms` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.resistance.*

val r = 470 of ohms
r into ohms                  // 470.0
r into kilo.ohms             // 0.47
(1 of kilo.ohms) into ohms   // 1000.0
```

## 여러 분해

저항은 여러 **동등한 분해**로 얻을 수 있으며, 모두 값이 같은 저항을 생성합니다:

| 식 | 결과 형식 | 의미 |
|---|---|---|
| `voltage / current` | `KResistanceUnitInstance` | 옴의 법칙 `R = U / I` |
| `mass·length²/(time³·current²)` | `.toResistance()` 경유 | 네이티브 정규형 `kg·m²·s⁻³·A⁻²` 식 |

형식이 지정된 연산자 형태는 저항을 직접 반환합니다. 완전한 네이티브 식은 일반적인 `KMixedUnitInstance` 로
남아 있으며 `toResistance()`(정규형만 인식하고 그렇지 않으면 `IllegalStateException` 을 던짐)로 좁힙니다.
두 경로는 값이 같습니다.

역 옴의 법칙 연산자는 전압, 저항, 전류를 연결합니다:

| 식 | 결과 형식 | 의미 |
|---|---|---|
| `resistance * current` | `KVoltageUnitInstance` | `U = R · I`(교환 가능) |
| `voltage / resistance` | `KElectricCurrentUnitInstance` | `I = U / R` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.resistance.*

// 실제 예시 - 옴의 법칙: 부하 양단 230 V, 2 A 가 흐르면 저항은 115 Ω 입니다.
val r = (230 of volts) / (2 of amperes)  // KResistanceUnitInstance, 115 Ω

// 같은 저항을 네이티브 kg·m²·s⁻³·A⁻² 식으로:
val raw = 115 of (kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 3))
raw.toResistance() == (115 of ohms)      // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.resistance.*

val s = (100 of ohms) + (40 of ohms)  // 140 Ω
(100 of ohms) > (40 of ohms)          // true
(100 of ohms) * (40 of ohms)          // KMixedUnitInstance(그룹에서 벗어남)
```

## toString 형식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.resistance.*

(470 of ohms).toString()     // "470.0 Ω"(기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit 을 사용해 Kotlin 에서 어떻게 쓰는지 대비합니다. 지수는 유니코드 위 첨자(`²`, `³`, `⁻¹`)로 표기하며, `·` 는 곱셈, `/` 는 분수를 나타냅니다. 분수로도, 음의 지수를 가진 곱으로도 쓸 수 있는 양에 대해서는 동등한 두 Kotlin 형식을 함께 나열합니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `Ω` | `ohms` | 저항, 기본 단위(명명된 토큰, 옴) |
| `kg·m²/(s³·A²)` | `kilo.grams * (meters pow 2) / ((amperes pow 2) * (seconds pow 3))` | 질량·길이² / (시간³·전류²) 로서의 저항(분수 형식) |
| `kg·m²·s⁻³·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -2)` | 같은 저항을 순수 곱으로 표현 |
| `kΩ` | `kilo.ohms` | 접두사가 붙은 저항(킬로옴) |
