# 에너지 (Energy, 열역학)

패키지: `org.pcsoft.framework.kunit.common.energy`
기본 단위: **줄 (joule)** (`KEnergyUnit.BASE == KEnergyUnit.JOULE`)

유형: **구성 단위 (constructed unit)**

에너지는 **구성** 단위입니다: `질량 · 길이² · 시간⁻²` (`kg·m²·s⁻²`)의 조합입니다.
`KEnergyUnitInstance`는 세 개의 항으로 이루어진 `KMixedUnitInstance`를 감쌉니다 — `KMassUnit.BASE`(그램)는
`+1`, `KDistanceUnit.BASE`(미터)는 `+2`, `KTimeUnit.BASE`(초)는 `-2`입니다. 라이브러리의 질량 구성 요소는 **그램**(킬로그램이 아님)으로 정규화되어 있으므로, 정규
곱은 줄에 도달하기 위해 1000으로 나뉩니다; 저장된 값은 항상 줄로 정규화됩니다.

에너지는 기술적으로 여러 주제 영역에 걸쳐 나타나는 **하나의** 물리량입니다. 이 페이지는 그 *열역학적*
해석 — **열 (heat)**, `Q = Φ · t`를 설명합니다. 동일한 Kotlin 그룹은 다른 영역에서도
[에너지 (전기)](../electrical/energy.md)와 [에너지 (역학)](../mechanics/energy.md)에서 문서화되어 있습니다.

온도 차로 나누면 에너지는 [열용량](heat-capacity.md)이 됩니다 (또는 `ΔS = Q / T`로 읽으면
[엔트로피](entropy.md)가 됩니다). 킬로그램당으로 나누면 [비에너지](specific-energy.md)가 되고, 몰당으로 나누면 [몰 에너지](molar-energy.md)가 됩니다.

## 에너지 만들기

이름이 붙은 토큰으로 에너지를 만들거나, 아래의 분해식으로부터 만들 수 있습니다. 이름이 붙은 단위는 값 1의 토큰으로 존재합니다 (`of`/`into`와 함께 사용). 이 그룹의 열적 단위는 칼로리와 영국
열단위입니다:

| 에너지         | 기호  |                  토큰 |      J 단위로 1 |
|----------------|-------|----------------------:|----------------:|
| 줄             | `J`   |              `joules` |             1.0 |
| 에르그(CGS)    | `erg` |                `ergs` |          1.0e-7 |
| 칼로리(열화학) | `cal` |            `calories` |           4.184 |
| 전자볼트       | `eV`  |       `electronVolts` | 1.602176634e-19 |
| 영국 열단위    | `BTU` | `britishThermalUnits` |   1055.05585262 |

이름이 붙은 단위는 `KPrefixBuilder`를 통해 SI 접두사를 지원합니다 (`kilo.calories` — "식품 칼로리" —,
`kilo.joules`, `mega.joules` 등).

**킬로와트시 (kilowatt hour)는 자체 토큰이 없습니다** — 진정한 의미의 이름이 붙은 단위가 아니라 곱셈식
`kilo.watts * hours`이며 그렇게 만들어집니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*

val q = 2000 of kilo.calories   // 일일 식단
q into kilo.joules              // 8368.0
q into britishThermalUnits      // 7931.79...
```

## 다중 분해

에너지는 여러 **동등한 분해식**을 통해 도달할 수 있으며, 모두 동일한 값-동등 에너지를 생성합니다:

| 표현식               | 결과 타입             | 의미                                                                   |
|----------------------|-----------------------|------------------------------------------------------------------------|
| `power * time`       | `KEnergyUnitInstance` | 시간에 걸친 열유량으로부터 계산된 열 `Q = Φ · t` (교환 가능)           |
| `power / frequency`  | `KEnergyUnitInstance` | 역시간 형태 (`W/Hz = W·s`)                                             |
| `force * length`     | `KEnergyUnitInstance` | 역학적 일 `W = F · s` ([에너지(역학)](../mechanics/energy.md) 참조)    |
| `charge * voltage`   | `KEnergyUnitInstance` | 전기 에너지 `W = Q · U` ([에너지(전기)](../electrical/energy.md) 참조) |
| `mass·length²/time²` | `.toEnergy()`를 통해  | 네이티브 정규 `kg·m²·s⁻²` 표현식                                       |

타입이 지정된 연산자 형식은 에너지를 직접 반환합니다. 완전히 네이티브인 표현식은 일반적인
`KMixedUnitInstance`로 남아 있으며 `toEnergy()`로 좁혀집니다 (이는 정규 형식만 인식하고 그렇지 않으면
`IllegalStateException`을 발생시킵니다). 모든 경로는 값-동등합니다.

역연산자는 열유량, 시간, 열을 함께 묶습니다:

| 표현식           | 결과 타입            | 의미                                               |
|------------------|----------------------|----------------------------------------------------|
| `energy / time`  | `KPowerUnitInstance` | 열유량 `Φ = Q / t` ([전력(열역학)](power.md) 참조) |
| `energy / power` | `KTimeUnitInstance`  | 가열 시간 `t = Q / Φ`                              |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.common.energy.*

// 실제 사례 - 온수기: 10분에 걸친 2 kW의 열유량은 1200 kJ의 열을 전달합니다.
val q = (2 of kilo.watts) * (10 of minutes)   // KEnergyUnitInstance
q into kilo.joules                            // 1200.0

// 2 kW 온수기의 가열 시간을 구하도록 풀어낸 열:
val t = (1200 of kilo.joules) / (2 of kilo.watts)  // KTimeUnitInstance, 600 s

// 그리고 열유량을 구하도록 풀면:
val flow = (1200 of kilo.joules) / (10 of minutes) // KPowerUnitInstance, 2 kW

// 네이티브 kg·m²·s⁻² 표현식으로서의 동일한 열:
val raw = 1_200_000 of (kilo.grams * (meters pow 2)) / (seconds pow 2)
raw.toEnergy() == (1200 of kilo.joules)            // true
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
import org.pcsoft.framework.kunit.common.energy.*

(1 of britishThermalUnits).toString()     // "1055.05585262 J" (기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를 보여줍니다. 지수는 유니코드 위첨자 (`²`, `⁻²`)를 사용하며, `·`는 곱셈을, `/`는
분수를 나타냅니다. 어떤 양이 분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학        | Kotlin                                            | 의미                                         |
|-------------|---------------------------------------------------|----------------------------------------------|
| `J`         | `joules`                                          | 에너지(열), 기본 단위(이름이 붙은 토큰, 줄)  |
| `Φ · t`     | `(2 of kilo.watts) * (10 of minutes)`             | 열유량과 시간으로부터 계산된 열              |
| `kcal`      | `kilo.calories`                                   | 접두사가 붙은 열에너지(식품 칼로리)          |
| `kg·m²/s²`  | `(kilo.grams * (meters pow 2)) / (seconds pow 2)` | 질량·길이² / 시간²으로서의 에너지(분수 형식) |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)`  | 순수 곱으로서의 동일한 에너지                |
