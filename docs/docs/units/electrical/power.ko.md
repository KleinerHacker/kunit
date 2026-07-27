# 전력 (Power, 전기)

패키지: `org.pcsoft.framework.kunit.power`
기본 단위: **와트(watt)** (`KPowerUnit.BASE == KPowerUnit.WATT`)

유형: **구성 단위(constructed unit)**

전력은 **구성** 단위입니다: `질량 · 길이² · 시간⁻³` (`kg·m²·s⁻³`)의 조합입니다.
`KPowerUnitInstance`는 세 개의 항으로 이루어진 `KMixedUnitInstance`를 감쌉니다 — `KMassUnit.BASE`(그램)는
`+1`, `KDistanceUnit.BASE`(미터)는 `+2`, `KTimeUnit.BASE`(초)는 `-3`입니다. 라이브러리의 질량 구성 요소는
**그램**(킬로그램이 아님)으로 정규화되어 있으므로, 정규 곱은 와트에 도달하기 위해 1000으로 나뉩니다;
저장된 값은 항상 와트로 정규화됩니다.

전력은 기술적으로 여러 주제 영역에 걸쳐 나타나는 **하나의** 물리량입니다. 이 페이지는 그 *전기적* 해석
(`P = U · I`)을 설명합니다. 동일한 Kotlin 그룹은 다른 영역에서도 [전력(역학)](../mechanics/power.md)과
[전력(열역학)](../thermodynamics/power.md)에서 문서화되어 있습니다.

## 전력 만들기

이름이 붙은 토큰으로 전력을 만들거나, 아래의 분해식으로부터 만들 수 있습니다. 이름이 붙은 단위는 값 1의
토큰으로 존재합니다(`of`/`into`와 함께 사용):

| 전력 | 기호 | 토큰 | W 단위로 1 |
|---|---|---:|---:|
| 와트 | `W` | `watts` | 1.0 |
| 미터법 마력 | `PS` | `metricHorsePowers` | 735.49875 |
| 기계식 마력 | `hp` | `mechanicalHorsePowers` | 745.6998715822702 |
| 초당 에르그(CGS) | `erg/s` | `ergsPerSecond` | 1.0e-7 |

이름이 붙은 단위는 `KPrefixBuilder`를 통해 SI 접두사를 지원합니다(`kilo.watts`, `mega.watts`,
`milli.watts` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.power.*

val p = 2 of kilo.watts
p into kilo.watts               // 2.0
p into watts                    // 2000.0
(100 of metricHorsePowers) into kilo.watts // 73.549875
```

## 다중 분해

전력은 여러 **동등한 분해식**을 통해 도달할 수 있으며, 모두 동일한 값-동등 전력을 생성합니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `voltage * current` | `KPowerUnitInstance` | 전기 전력 `P = U · I` (교환 가능) |
| `force * speed` | `KPowerUnitInstance` | 기계 전력 `P = F · v` (교환 가능) |
| `energy / time` | `KPowerUnitInstance` | `P = W / t` ([에너지](energy.md) 참조) |
| `mass·length²/time³` | `.toPower()`를 통해 | 네이티브 정규 `kg·m²·s⁻³` 표현식 |

타입이 지정된 연산자 형식은 전력을 직접 반환합니다. 완전히 네이티브인 표현식은 일반적인
`KMixedUnitInstance`로 남아 있으며 `toPower()`로 좁혀집니다(이는 정규 형식만 인식하고 그렇지 않으면
`IllegalStateException`을 발생시킵니다). 모든 경로는 값-동등합니다.

전기적 형태의 역연산자는 전압, 전류, 전력을 함께 묶습니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `power / current` | `KVoltageUnitInstance` | `U = P / I` |
| `power / voltage` | `KElectricCurrentUnitInstance` | `I = P / U` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.power.*

// 실제 사례 - 전원 콘센트: 10 A에서 230 V는 2.3 kW를 공급합니다.
val p = (230 of volts) * (10 of amperes)   // KPowerUnitInstance
p into kilo.watts                          // 2.3

// 230 V에서 2.3 kW 부하가 끌어들이는 전류를 구하도록 풀어낸 정의:
val i = (2.3 of kilo.watts) / (230 of volts) // KElectricCurrentUnitInstance, 10 A

// 네이티브 kg·m²·s⁻³ 표현식으로서의 동일한 전력:
val raw = 2300 of (kilo.grams * (meters pow 2)) / (seconds pow 3)
raw.toPower() == (2.3 of kilo.watts)       // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.power.*

val s = (100 of watts) + (40 of watts)  // 140 W
(100 of watts) > (40 of watts)          // true
(100 of watts) * (40 of watts)          // KMixedUnitInstance (그룹을 벗어남)
```

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.power.*

(1 of kilo.watts).toString()     // "1000.0 W" (기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를
보여줍니다. 지수는 유니코드 위첨자(`²`, `⁻³`)를 사용하며, `·`는 곱셈을, `/`는 분수를 나타냅니다. 어떤 양이
분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `W` | `watts` | 전력, 기본 단위(이름이 붙은 토큰, 와트) |
| `U · I` | `(230 of volts) * (10 of amperes)` | 전압과 전류로부터 계산된 전기 전력 |
| `kg·m²/s³` | `(kilo.grams * (meters pow 2)) / (seconds pow 3)` | 질량·길이² / 시간³으로서의 전력(분수 형식) |
| `kg·m²·s⁻³` | `kilo.grams * (meters pow 2) * (seconds pow -3)` | 순수 곱으로서의 동일한 전력 |
| `kW` | `kilo.watts` | 접두사가 붙은 전력(킬로와트) |
