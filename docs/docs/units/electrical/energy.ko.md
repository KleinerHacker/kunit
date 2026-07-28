# 에너지 (Energy, 전기)

패키지: `org.pcsoft.framework.kunit.common.energy`
기본 단위: **줄(joule)** (`KEnergyUnit.BASE == KEnergyUnit.JOULE`)

유형: **구성 단위(constructed unit)**

에너지는 **구성** 단위입니다: `질량 · 길이² · 시간⁻²` (`kg·m²·s⁻²`)의 조합입니다.
`KEnergyUnitInstance`는 세 개의 항으로 이루어진 `KMixedUnitInstance`를 감쌉니다 — `KMassUnit.BASE`(그램)는
`+1`, `KDistanceUnit.BASE`(미터)는 `+2`, `KTimeUnit.BASE`(초)는 `-2`입니다. 라이브러리의 질량 구성 요소는
**그램**(킬로그램이 아님)으로 정규화되어 있으므로, 정규 곱은 줄에 도달하기 위해 1000으로 나뉩니다;
저장된 값은 항상 줄로 정규화됩니다.

에너지는 기술적으로 여러 주제 영역에 걸쳐 나타나는 **하나의** 물리량입니다. 이 페이지는 그 *전기적* 해석
(`W = Q · U`, 그리고 소비된 전기 에너지의 경우 `W = P · t`)을 설명합니다. 동일한 Kotlin 그룹은 다른
영역에서도 [에너지(역학)](../mechanics/energy.md)와 [에너지(열역학)](../thermodynamics/energy.md)에서
문서화되어 있습니다.

## 에너지 만들기

이름이 붙은 토큰으로 에너지를 만들거나, 아래의 분해식으로부터 만들 수 있습니다. 이름이 붙은 단위는 값
1의 토큰으로 존재합니다(`of`/`into`와 함께 사용):

| 에너지 | 기호 | 토큰 | J 단위로 1 |
|---|---|---:|---:|
| 줄 | `J` | `joules` | 1.0 |
| 에르그(CGS) | `erg` | `ergs` | 1.0e-7 |
| 칼로리(열화학) | `cal` | `calories` | 4.184 |
| 전자볼트 | `eV` | `electronVolts` | 1.602176634e-19 |
| 영국 열단위 | `BTU` | `britishThermalUnits` | 1055.05585262 |

이름이 붙은 단위는 `KPrefixBuilder`를 통해 SI 접두사를 지원합니다(`kilo.joules`, `mega.joules`,
`mega.electronVolts` 등).

**킬로와트시(kilowatt hour)는 자체 토큰이 없습니다** — 진정한 의미의 이름이 붙은 단위가 아니라 곱셈식
`kilo.watts * hours`이며 그렇게 만들어집니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.common.energy.*

val w = 500 of kilo.joules
w into kilo.joules                          // 500.0
w into joules                               // 500000.0

val kwh = (1 of kilo.watts) * (1 of hours)  // 1 kWh = 3.6 MJ
kwh into kilo.joules                        // 3600.0
```

## 다중 분해

에너지는 여러 **동등한 분해식**을 통해 도달할 수 있으며, 모두 동일한 값-동등 에너지를 생성합니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `charge * voltage` | `KEnergyUnitInstance` | 전기 에너지 `W = Q · U` (교환 가능) |
| `power * time` | `KEnergyUnitInstance` | 소비된 에너지 `W = P · t` (교환 가능) |
| `power / frequency` | `KEnergyUnitInstance` | 역시간 형태 (`W/Hz = W·s`) |
| `force * length` | `KEnergyUnitInstance` | 역학적 일 `W = F · s` ([에너지(역학)](../mechanics/energy.md) 참조) |
| `mass·length²/time²` | `.toEnergy()`를 통해 | 네이티브 정규 `kg·m²·s⁻²` 표현식 |

타입이 지정된 연산자 형식은 에너지를 직접 반환합니다. 완전히 네이티브인 표현식은 일반적인
`KMixedUnitInstance`로 남아 있으며 `toEnergy()`로 좁혀집니다(이는 정규 형식만 인식하고 그렇지 않으면
`IllegalStateException`을 발생시킵니다). 모든 경로는 값-동등합니다.

역연산자는 전하, 전압, 전력, 시간, 에너지를 함께 묶습니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `energy / charge` | `KVoltageUnitInstance` | `U = W / Q` |
| `energy / time` | `KPowerUnitInstance` | `P = W / t` |
| `energy / power` | `KTimeUnitInstance` | `t = W / P` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.common.energy.*

// 실제 사례 - 3시간 동안 작동하는 2 kW 히터는 6 kWh = 21600 kJ를 소비합니다.
val w = (2 of kilo.watts) * (3 of hours)   // KEnergyUnitInstance
w into kilo.joules                         // 21600.0

// 전하와 전압으로부터의 전기 에너지: 50 V에서 이동한 10 C는 500 J입니다.
val fromCharge = (10 of coulombs) * (50 of volts)  // KEnergyUnitInstance, 500 J

// 전압에 대해 풀어낸 정의:
val u = (500 of joules) / (10 of coulombs)         // KVoltageUnitInstance, 50 V

// 네이티브 kg·m²·s⁻² 표현식으로서의 동일한 에너지:
val raw = 500 of (kilo.grams * (meters pow 2)) / (seconds pow 2)
raw.toEnergy() == (500 of joules)                  // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.energy.*

val s = (100 of joules) + (40 of joules)  // 140 J
(100 of joules) > (40 of joules)          // true
(100 of joules) * (40 of joules)          // KMixedUnitInstance (그룹을 벗어남)
```

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*

(1 of kilo.joules).toString()     // "1000.0 J" (기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를
보여줍니다. 지수는 유니코드 위첨자(`²`, `⁻²`)를 사용하며, `·`는 곱셈을, `/`는 분수를 나타냅니다. 어떤 양이
분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `J` | `joules` | 에너지, 기본 단위(이름이 붙은 토큰, 줄) |
| `Q · U` | `(10 of coulombs) * (50 of volts)` | 전하와 전압으로부터 계산된 전기 에너지 |
| `P · t` | `(2 of kilo.watts) * (3 of hours)` | 소비된 에너지(kWh는 토큰이 없음) |
| `kg·m²/s²` | `(kilo.grams * (meters pow 2)) / (seconds pow 2)` | 질량·길이² / 시간²으로서의 에너지(분수 형식) |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)` | 순수 곱으로서의 동일한 에너지 |
| `kJ` | `kilo.joules` | 접두사가 붙은 에너지(킬로줄) |
