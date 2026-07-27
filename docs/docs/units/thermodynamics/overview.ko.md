# 열역학 — 개요

패키지: `org.pcsoft.framework.kunit.temperature`, `…energy`, `…power`

열역학은 **열과 온도**의 물리학입니다. KUnit에서 이 분야는 현재 온도를 중심으로 하며, 온도는 **관련된
2개의 네이티브 그룹**으로 모델링됩니다 — 왜냐하면 온도의 *측정값*과 온도의 *변화*는 물리적으로 다른
종류의 양이며, 이 둘을 구분하는 것이 산술을 올바르게 만들기 때문입니다. 그 주위에는 모든 열 수지에
등장하는 2개의 **구성** 양이 있습니다: 열 자체(에너지)와 그것이 흐르는 속도(전력)입니다.

## 이 주제의 단위

| 단위 | 유형 | 성질 | 기준 단위 | 페이지 |
|---|---|---|---|---|
| 절대 온도 | 네이티브 | 아핀 **점** | 켈빈(`K`) | [절대 온도](temperature.md) |
| 온도 차 | 네이티브 | 선형 **구간** | 켈빈(`ΔK`) | [온도 차](temperature-difference.md) |
| 에너지 | 구성 | 선형량 | 줄(`J`) | [에너지(열역학)](energy.md) |
| 전력 | 구성 | 선형량 | 와트(`W`) | [전력(열역학)](power.md) |

에너지(열)와 전력(열류량)은 각각 기술적으로 **하나**의 양이며, 다른 주제 분야와 공유됩니다. 이들은
분야별로 문서화되며 서로 참조합니다([에너지(전기)](../electrical/energy.md),
[에너지(역학)](../mechanics/energy.md), [전력(전기)](../electrical/power.md),
[전력(역학)](../mechanics/power.md)).

전용 [온도 개요](temperature-overview.md)가 점 대 구간 구분을 깊이 있게 설명합니다. 이 페이지는 열역학
분야 전체의 진입점입니다.

## 점 대 구간 — 연산자 규칙

| 연산 | 결과 |
|---|---|
| `절대온도 − 절대온도` | **온도 차** |
| `절대온도 + 차` | 절대 온도 |
| `절대온도 − 차` | 절대 온도 |
| `차 ± 차` | 온도 차 |
| `절대온도 + 절대온도` | **컴파일 오류**(물리적으로 무의미) |

## 타입 지정 연산자로서의 열과 열류

| 식 | 결과 | 공식 |
|---|---|---|
| `power * time` | 에너지(열) | `Q = Φ · t` |
| `energy / time` | 전력(열류) | `Φ = Q / t` |
| `energy / power` | 시간 | `t = Q / Φ` |
| `power / frequency` | 에너지 | `Q = Φ / f` |

## 실전 예제 — 가열 단계

물을 **10 °C**에서 **30 °C**로 가열합니다. 그 *변화*는 온도 **차**(`ΔT`)이며, 이는 `Q = m · c · ΔT`와
같은 열 공식에 들어가는 양입니다. 영점이 상쇄되므로 `°C`와 `K`는 단계 크기에서 일치합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

val start = 10 of celsius
val end   = 30 of celsius

val deltaT = end - start                     // KTemperatureDifferenceUnitInstance: 20 ΔK
deltaT.value                                 // 20.0 (켈빈 구간)

val back = start + KTemperatureDifference.ofKelvin(20) // KTemperatureUnitInstance: 303.15 K
```

## 실전 예제 — 보일러의 열과 가열 시간

**2 kW** 보일러가 **10분** 동안 가동됩니다. 공급된 열은 `Q = Φ · t`이며, 이를 다시 열류로 나누면
가열 시간이 나옵니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.time.minutes
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.power.*
import org.pcsoft.framework.kunit.energy.*

val q = (2 of kilo.watts) * (10 of minutes)   // KEnergyUnitInstance
q into kilo.joules                            // 1200.0
q into kilo.calories                          // ≈ 286.8 (kcal)

val t = q / (2 of kilo.watts)                 // KTimeUnitInstance
t into seconds                                // 600.0
```

## 값 출력(`toString`)

`toString()`은 값을 해당 그룹의 **기준 단위**(켈빈)로 출력합니다: 절대 온도는 `K`로, 차는 구별되는
`ΔK` 기호로 표시됩니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

(25 of celsius).toString()                       // "298.15 K" (절대, 기준 단위)
KTemperatureDifference.ofKelvin(20).toString()   // "20.0 ΔK" (구간)
```

## 표기법

아래 표는 온도 관계를 수학 표기와 KUnit의 Kotlin 표기로 대비합니다. `Δ`는 구간 양을 나타내며, 절대적인
점과 의도적으로 구분됩니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `ΔT = T₂ − T₁` | `(30 of celsius) - (10 of celsius)` | 두 절대 온도로부터의 차 |
| `T + ΔT` | `(10 of celsius) + KTemperatureDifference.ofKelvin(20)` | 구간만큼 이동한 절대 온도 |
| `ΔK` | `KTemperatureDifference.ofKelvin(20)` | 명시적 온도 구간 |
| `20 ΔK + 10 ΔK` | `KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10)` | 두 구간의 합 |
| `Q = Φ · t` | `(2 of kilo.watts) * (10 of minutes)` | 열류×시간에서 열 |
| `Φ = Q / t` | `(1200 of kilo.joules) / (10 of minutes)` | 열÷시간에서 열류 |

## 다음에 볼 것

* [온도 개요](temperature-overview.md) — 점 대 구간의 전체 논의와 그것이 물리적으로 왜 중요한지
  (열에너지, 복사, 이상 기체 법칙).
* [절대 온도](temperature.md) — 켈빈, 섭씨, 화씨, 랭킨과 아핀 연산자.
* [온도 차](temperature-difference.md) — 선형 켈빈 구간 그룹.
* [에너지(열역학)](energy.md) — 열로서의 줄, 그리고 칼로리와 BTU.
* [전력(열역학)](power.md) — 열류량으로서의 와트, `Q / t`.
