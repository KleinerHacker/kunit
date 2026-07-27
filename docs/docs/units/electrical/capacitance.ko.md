# 정전용량

패키지: `org.pcsoft.framework.kunit.capacitance`
기본 단위: **패럿**(`KCapacitanceUnit.BASE == KCapacitanceUnit.FARAD`)

종류: **구성된 단위**

정전용량은 **구성된** 단위로, 합성 `mass⁻¹ · length⁻² · time⁴ · current²`(`kg⁻¹·m⁻²·s⁴·A²`)입니다.
`KCapacitanceUnitInstance` 는 네 개의 항을 가진 `KMixedUnitInstance` 를 감쌉니다 — 지수 `-1` 의
`KMassUnit.BASE`(그램), 지수 `-2` 의 `KDistanceUnit.BASE`(미터), 지수 `+4` 의 `KTimeUnit.BASE`(초),
지수 `+2` 의 `KElectricCurrentUnit.BASE`(암페어). 라이브러리의 질량 성분은 **그램**(킬로그램이 아님)으로
정규화되고 질량 지수가 *음수* 이므로, 패럿은 원시 성분 기준에 대해 반대 방향으로 1000배입니다.
저장되는 값은 패럿으로 정규화됩니다.

## 정전용량 만들기

이름 있는 토큰으로 만들거나 분해(아래 참조)로 만듭니다. 이름 있는 단위는 값 1 토큰으로 남습니다
(`of`/`into` 와 함께 사용):

| 정전용량 | 기호 | 토큰 | 1단위의 F 값 |
|---|---|---:|---:|
| 패럿 | `F` | `farads` | 1.0 |
| 앱패럿(CGS-EMU) | `abF` | `abfarads` | 1.0e9 |
| 스탯패럿(CGS-ESU) | `statF` | `statfarads` | 1.112650056e-12 |
| 자(라이덴병) | `jar` | `jars` | 1.11265e-9 |

이름 있는 단위는 `KPrefixBuilder` 를 통해 SI 접두어를 지원합니다(`micro.farads`, `nano.farads`, `pico.farads`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.capacitance.*

val c = 470 of micro.farads
c into micro.farads            // 470.0
c into farads                  // 4.7e-4
(1 of milli.farads) into farads // 0.001
```

## 여러 분해

정전용량은 여러 **동등한 분해**로 도달할 수 있으며, 모두 값이 같은 정전용량을 만듭니다:

| 식 | 결과 타입 | 의미 |
|---|---|---|
| `charge / voltage` | `KCapacitanceUnitInstance` | 정의 `C = Q / U` |
| `current²·time⁴/(mass·length²)` | `.toCapacitance()` 경유 | 네이티브 정규형 `kg⁻¹·m⁻²·s⁴·A²` 식 |

타입이 지정된 연산자 형태는 정전용량을 직접 반환합니다. 완전한 네이티브 식은 일반 `KMixedUnitInstance` 로
남아 있으며 `toCapacitance()`(정규형만 인식하고 그렇지 않으면 `IllegalStateException` 을 던짐)로 좁힙니다.
두 경로 모두 값이 같습니다.

역연산자는 전하, 전압, 정전용량을 서로 연결합니다:

| 식 | 결과 타입 | 의미 |
|---|---|---|
| `capacitance * voltage` | `KChargeUnitInstance` | `Q = C · U`(교환 가능) |
| `charge / capacitance` | `KVoltageUnitInstance` | `U = Q / C` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.capacitance.*

// 실제 예 - 충전된 커패시터: 470 µF 를 12 V 로 충전하면 5.64 mC 를 저장합니다.
val q = (470 of micro.farads) * (12 of volts)  // KChargeUnitInstance, 0.00564 C

// 정전용량에 대해 푼 정의식:
val c = (10 of coulombs) / (5 of volts)        // KCapacitanceUnitInstance, 2 F

// 같은 정전용량을 네이티브 kg⁻¹·m⁻²·s⁴·A² 식으로:
val raw = 2 of ((amperes pow 2) * (seconds pow 4)) / (kilo.grams * (meters pow 2))
raw.toCapacitance() == (2 of farads)           // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.capacitance.*

val s = (100 of farads) + (40 of farads)  // 140 F
(100 of farads) > (40 of farads)          // true
(100 of farads) * (40 of farads)          // KMixedUnitInstance(그룹에서 벗어남)
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.capacitance.*

(470 of farads).toString()     // "470.0 F"(기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 쓰는 방법과 KUnit 을 사용한 Kotlin 표기를 비교합니다. 지수는 유니코드 위첨자(`²`, `⁴`, `⁻¹`)를 사용하며 `·` 는 곱셈, `/` 는 분수를 뜻합니다. 분수와 음수 지수의 곱 두 가지로 쓸 수 있는 양은 두 Kotlin 형태를 모두 표기합니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `F` | `farads` | 정전용량, 기본 단위(이름 있는 토큰, 패럿) |
| `A²·s⁴/(kg·m²)` | `(amperes pow 2) * (seconds pow 4) / (kilo.grams * (meters pow 2))` | 전류²·시간⁴ / (질량·길이²) 로서의 정전용량(분수 형태) |
| `kg⁻¹·m⁻²·s⁴·A²` | `(kilo.grams pow -1) * (meters pow -2) * (seconds pow 4) * (amperes pow 2)` | 같은 정전용량을 순수 곱으로 표현 |
| `µF` | `micro.farads` | 접두어가 붙은 정전용량(마이크로패럿) |
