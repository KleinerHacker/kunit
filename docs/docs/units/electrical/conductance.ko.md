# 컨덕턴스

패키지: `org.pcsoft.framework.kunit.electric.conductance`
기본 단위: **지멘스**(`KConductanceUnit.BASE == KConductanceUnit.SIEMENS`)

종류: **구성된 단위**

전기 컨덕턴스는 **구성된** 단위로, 합성 `mass⁻¹ · length⁻² · time³ · current²`(`kg⁻¹·m⁻²·s³·A²`)입니다.
`KConductanceUnitInstance` 는 네 개의 항 — 지수 `-1` 의 `KMassUnit.BASE`(그램), 지수 `-2` 의
`KDistanceUnit.BASE`(미터), 지수 `+3` 의 `KTimeUnit.BASE`(초), 지수 `+2` 의
`KElectricCurrentUnit.BASE`(암페어) — 를 감쌉니다. 라이브러리의 질량 성분은 **그램**(킬로그램이 아님)으로
정규화되고 질량 지수가 음수이므로, 지멘스는 원시 성분 기준의 1/1000 배입니다. 저장되는 값은 지멘스로
정규화됩니다.

컨덕턴스는 [저항](resistance.md)의 역수(`G = 1 / R`)이며, 옴의 법칙을 통해 [전압](voltage.md)과
[전류](ec.md)를 연결합니다.

## 컨덕턴스 만들기

컨덕턴스는 이름 있는 토큰으로, 또는 분해(아래 참조)로 만듭니다. 이름 있는 단위는 값이 1인 토큰으로 남습니다
(`of`/`into` 와 함께 사용):

| 컨덕턴스 | 기호 | 토큰 | 1 단위(S) |
|---|---|---:|---:|
| 지멘스 | `S` | `siemens` | 1.0 |
| 모(전통 명칭) | `℧` | `mhos` | 1.0 |
| 앱모(CGS-EMU) | `ab℧` | `abmhos` | 1.0e9 |
| 스탯모(CGS-ESU) | `stat℧` | `statmhos` | 1.112650e-12 |

!!! note "`siemens` 와 `siemensUnits`"
    `siemens`(이 패키지)는 **컨덕턴스**의 SI 단위입니다. 이름이 비슷한
    `org.pcsoft.framework.kunit.electric.resistance` 의 `siemensUnits` 는 역사적인 **지멘스 수은 단위**로,
    0.9534 Ω 의 *저항*입니다. 서로 다른 패키지의 무관한 물리량입니다.

이름 있는 단위는 `KPrefixBuilder` 를 통해 SI 접두어를 지원합니다(`milli.siemens`, `micro.siemens`,
`kilo.siemens` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.conductance.*

val g = 4 of siemens
g into siemens                    // 4.0
g into milli.siemens              // 4000.0
(1 of milli.siemens) into siemens // 0.001
```

## 여러 분해

컨덕턴스는 여러 **동등한 분해**로 얻을 수 있으며, 모두 값이 같은 컨덕턴스를 만듭니다:

| 식 | 결과 타입 | 의미 |
|---|---|---|
| `current / voltage` | `KConductanceUnitInstance` | 옴의 법칙 `G = I / U` |
| `1 / resistance` | `KConductanceUnitInstance` | 저항의 역수 `G = 1 / R` |
| `time³·current²/(mass·length²)` | `.toConductance()` 경유 | 네이티브 정규형 `kg⁻¹·m⁻²·s³·A²` 식 |

타입이 지정된 연산자 형태는 컨덕턴스를 직접 반환합니다. 완전한 네이티브 식은 일반 `KMixedUnitInstance` 로
남으며 `toConductance()`(정규형만 인식하고 그렇지 않으면 `IllegalStateException` 발생)로 좁힙니다.
모든 경로는 값이 같습니다.

역연산자는 컨덕턴스, 전압, 전류를 서로 연결합니다:

| 식 | 결과 타입 | 의미 |
|---|---|---|
| `conductance * voltage` | `KElectricCurrentUnitInstance` | `I = G · U`(교환 가능) |
| `current / conductance` | `KVoltageUnitInstance` | `U = I / G` |
| `1 / conductance` | `KResistanceUnitInstance` | `R = 1 / G` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.electric.resistance.ohms
import org.pcsoft.framework.kunit.electric.conductance.*

// 실제 예 - 급전 케이블의 컨덕턴스: 2 A 가 흐르고 전압 강하가 1 V 인 케이블의
// 컨덕턴스는 2 S 입니다(즉 저항은 0.5 Ω).
val g = (2 of amperes) / (1 of volts)    // KConductanceUnitInstance, 2 S
val r = 1 / g                            // KResistanceUnitInstance, 0.5 Ω

// 저항과의 역수 관계:
1 / (1 of ohms) == (1 of siemens)        // true

// 같은 컨덕턴스를 네이티브 kg⁻¹·m⁻²·s³·A² 식으로:
val raw = 2 of ((seconds pow 3) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
raw.toConductance() == (2 of siemens)    // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.conductance.*

val s = (100 of siemens) + (40 of siemens)  // 140 S
(100 of siemens) > (40 of siemens)          // true
(100 of siemens) * (40 of siemens)          // KMixedUnitInstance(그룹에서 벗어남)
```

## toString 형식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.conductance.*

(4 of siemens).toString()     // "4.0 S"(기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, KUnit 을 사용해 Kotlin 으로 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자(`²`, `³`, `⁻¹`)로 표기하며, `·` 는 곱셈, `/` 는 분수를 나타냅니다. 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 양은 두 가지 동등한 Kotlin 형태를 함께 표기합니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `S` | `siemens` | 컨덕턴스, 기본 단위(이름 있는 토큰, 지멘스) |
| `s³·A²/(kg·m²)` | `((seconds pow 3) * (amperes pow 2)) / (kilo.grams * (meters pow 2))` | 시간³·전류² / (질량·길이²) 로서의 컨덕턴스(분수 형태) |
| `kg⁻¹·m⁻²·s³·A²` | `(kilo.grams pow -1) * (meters pow -2) * (seconds pow 3) * (amperes pow 2)` | 같은 컨덕턴스를 순수한 곱으로 표현 |
| `mS` | `milli.siemens` | 접두어가 붙은 컨덕턴스(밀리지멘스) |
