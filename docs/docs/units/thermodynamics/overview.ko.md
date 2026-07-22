# 열역학 — 개요

패키지: `org.pcsoft.framework.kunit.temperature`

열역학은 **열과 온도**의 물리학입니다. KUnit에서 이 분야는 현재 온도를 중심으로 하며, 온도는 **관련된
2개의 네이티브 그룹**으로 모델링됩니다 — 왜냐하면 온도의 *측정값*과 온도의 *변화*는 물리적으로 다른
종류의 양이며, 이 둘을 구분하는 것이 산술을 올바르게 만들기 때문입니다.

## 이 주제의 단위

| 단위 | 유형 | 성질 | 기준 단위 | 페이지 |
|---|---|---|---|---|
| 절대 온도 | 네이티브 | 아핀 **점** | 켈빈(`K`) | [절대 온도](temperature.md) |
| 온도 차 | 네이티브 | 선형 **구간** | 켈빈(`ΔK`) | [온도 차](temperature-difference.md) |

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

## 다음에 볼 것

* [온도 개요](temperature-overview.md) — 점 대 구간의 전체 논의와 그것이 물리적으로 왜 중요한지
  (열에너지, 복사, 이상 기체 법칙).
* [절대 온도](temperature.md) — 켈빈, 섭씨, 화씨, 랭킨과 아핀 연산자.
* [온도 차](temperature-difference.md) — 선형 켈빈 구간 그룹.
