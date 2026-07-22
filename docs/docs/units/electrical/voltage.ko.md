# 전압

패키지: `org.pcsoft.framework.kunit.voltage`
기본 단위: **볼트**(`KVoltageUnit.BASE == KVoltageUnit.VOLT`)

유형: **구성된 단위**

전압(전위차)은 **구성된** 단위로, 합성 `mass · length² · time⁻³ · current⁻¹`(`kg·m²·s⁻³·A⁻¹`)입니다.
`KVoltageUnitInstance` 는 네 개의 항 — 지수 `+1` 의 `KMassUnit.BASE`(그램), 지수 `+2` 의
`KDistanceUnit.BASE`(미터), 지수 `-3` 의 `KTimeUnit.BASE`(초), 지수 `-1` 의
`KElectricCurrentUnit.BASE`(암페어) — 을 감쌉니다. 라이브러리의 질량 성분은 **그램**(킬로그램이 아님)으로
정규화되므로 볼트는 원시 성분 기준의 1000배입니다. 저장되는 값은 볼트로 정규화됩니다.

## 전압 만들기

명명된 토큰으로, 또는 분해(아래 참조)로 전압을 만듭니다. 명명된 단위는 값이 1인 토큰으로 남습니다
(`of`/`into` 와 함께 사용):

| 전압 | 기호 | 토큰 | 1 단위의 V 값 |
|---|---|---:|---:|
| 볼트 | `V` | `volts` | 1.0 |
| 스탯볼트(CGS-ESU) | `statV` | `statvolts` | 299.792458 |
| 앱볼트(CGS-EMU) | `abV` | `abvolts` | 1.0e-8 |
| 웨스턴 전지 | `V_W` | `westonCells` | 1.0183 |
| 다니엘 전지 | `V_Da` | `daniells` | 1.1 |

명명된 단위는 `KPrefixBuilder` 를 통해 SI 접두사를 지원합니다(`kilo.volts`, `mega.volts`, `milli.volts` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.voltage.*

val u = 230 of volts
u into volts                 // 230.0
u into kilo.volts            // 0.23
(1 of kilo.volts) into volts // 1000.0
```

## 여러 분해

전압은 여러 **동등한 분해**로 얻을 수 있으며, 모두 값이 같은 전압을 생성합니다:

| 식 | 결과 형식 | 의미 |
|---|---|---|
| `resistance * current` | `KVoltageUnitInstance` | 옴의 법칙 `U = R · I`(저항 참조) |
| `current * resistance` | `KVoltageUnitInstance` | 옴의 법칙(교환 가능) |
| `mass·length²/(time³·current)` | `.toVoltage()` 경유 | 네이티브 정규형 `kg·m²·s⁻³·A⁻¹` 식 |

형식이 지정된 연산자 형태는 전압을 직접 반환합니다. 완전한 네이티브 식은 일반적인 `KMixedUnitInstance` 로
남아 있으며 `toVoltage()`(정규형만 인식하고 그렇지 않으면 `IllegalStateException` 을 던짐)로 좁힙니다.
두 경로는 값이 같습니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.resistance.ohms
import org.pcsoft.framework.kunit.voltage.*

// 실제 예시 - 옴의 법칙: 2 A 가 흐르는 115 Ω 저항은 230 V 의 전압 강하를 만듭니다.
val u = (115 of ohms) * (2 of amperes)   // KVoltageUnitInstance, 230 V

// 같은 전압을 네이티브 kg·m²·s⁻³·A⁻¹ 식으로:
val raw = 230 of (kilo.grams * (meters pow 2)) / (amperes * (seconds pow 3))
raw.toVoltage() == (230 of volts)        // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.voltage.*

val s = (100 of volts) + (40 of volts)  // 140 V
(100 of volts) > (40 of volts)          // true
(100 of volts) * (40 of volts)          // KMixedUnitInstance(그룹에서 벗어남)
```

## toString 형식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.voltage.*

(230 of volts).toString()    // "230.0 V"(기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit 을 사용해 Kotlin 에서 어떻게 쓰는지 대비합니다. 지수는 유니코드 위 첨자(`²`, `³`, `⁻¹`)로 표기하며, `·` 는 곱셈, `/` 는 분수를 나타냅니다. 분수로도, 음의 지수를 가진 곱으로도 쓸 수 있는 양에 대해서는 동등한 두 Kotlin 형식을 함께 나열합니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `V` | `volts` | 전압, 기본 단위(명명된 토큰, 볼트) |
| `kg·m²/(s³·A)` | `kilo.grams * (meters pow 2) / (amperes * (seconds pow 3))` | 질량·길이² / (시간³·전류) 로서의 전압(분수 형식) |
| `kg·m²·s⁻³·A⁻¹` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -1)` | 같은 전압을 순수 곱으로 표현 |
| `kV` | `kilo.volts` | 접두사가 붙은 전압(킬로볼트) |
