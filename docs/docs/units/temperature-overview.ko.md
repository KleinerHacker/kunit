# 온도 — 개요

패키지: `org.pcsoft.framework.kunit.temperature`

온도는 **두 개의 관련 그룹**으로 모델링됩니다. 온도 값과 온도 *변화*는 물리적으로 서로 다른 종류의 양이기
때문입니다. 이 구별을 올바르게 다루는 것이 연산을 올바르게 만드는 핵심입니다.

| 개념 | 타입 | 성격 | 기본 단위 |
|---|---|---|---|
| [절대 온도](temperature.md) | `KTemperatureUnitInstance` | 아핀 **점** | 켈빈(`K`) |
| [온도 차](temperature-difference.md) | `KTemperatureDifferenceUnitInstance` | 선형 **구간** | 켈빈(`ΔK`) |

## 점 대 구간

**절대 온도**(`25 °C`, `300 K`)는 온도 척도 위의 아핀 *점*입니다. 고정된 영점에서 측정되며, 영점 선택
(0 K 대 0 °C)에 따라 수치가 달라집니다. **온도 차**(`20 ΔK`)는 *벡터*로, 두 점 사이의 간격입니다. 영점 부담이
없으며 `20 ΔK`의 차이는 척도 상 어디에 있든 `20 °C`의 차이와 같습니다.

이것이 바로 아핀 공간의 점/벡터 구별이며, 연산을 규정합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

val a = 30 of celsius
val b = 10 of celsius

a - b                                    // KTemperatureDifferenceUnitInstance: 20 ΔK  (구간)
a + KTemperatureDifference.ofKelvin(5)   // KTemperatureUnitInstance: 308.15 K         (여전히 점)
// a + b                                 // 컴파일되지 않음 — 두 점의 덧셈은 무의미
```

| 연산 | 결과 |
|---|---|
| `AbsTemp − AbsTemp` | **온도 차**(켈빈 구간) |
| `AbsTemp + 차이` | 절대 온도 |
| `AbsTemp − 차이` | 절대 온도 |
| `차이 ± 차이` | 온도 차 |
| `AbsTemp + AbsTemp` | **컴파일 오류**(물리적으로 무의미) |

## 물리적으로 중요한 이유

수식이 절대 온도를 원하는지 차이를 원하는지는 온도가 **어떻게** 나타나는지에 달려 있습니다:

* **차이 / 변화로서** → **온도 차**를 사용합니다. 영점이 상쇄되므로 여기서는 `°C`와 `K`가 서로 교환 가능합니다.
  전형적인 예 — 열에너지:

  $$Q = m \cdot c \cdot \Delta T$$

  `30 °C − 10 °C = 20 ΔK`이며 `Q = m·c·20 ΔK`. 열팽창(`ΔL = α·L·ΔT`), 전도, 비열·열전도율 단위
  (`J/(kg·ΔK)`, `W/(m·ΔK)`)도 마찬가지입니다.

* **곱셈적으로**(T 단독, 거듭제곱 `T⁴`, 비율 `T₁/T₂`) → **절대 온도(켈빈)**를 사용합니다. 절대 영도가 물리의
  일부이기 때문입니다: 이상기체 법칙 `pV = nRT`, 슈테판–볼츠만 복사 `P = εσA·T⁴`, 카르노 효율 `η = 1 − T_c/T_h`.

!!! warning "같은 차원, 다른 양"
    두 그룹 모두 차원은 *켈빈*이므로 순수 단위 수준에서는 `m·K`와 `m·ΔK`가 비슷해 보입니다. 하지만 여기서는
    **같은 단위가 아닙니다**: 둘은 서로 다른 단위 그룹을 사용하므로 절대 켈빈을 포함한 혼합 단위는 차이 켈빈을
    포함한 것과 동등하지도 더할 수도 없습니다. 구별된 **`ΔK`** 기호(대 `K`)는 바로 그것을 `toString`과 혼합 단위
    출력에서 보이게 하려고 표시됩니다 — 직접 혼합 단위를 구성할 때 유의하세요.

## 다음으로

* **[절대 온도](temperature.md)** — 단위(켈빈, 섭씨, 화씨, 랭킨), 아핀 `of`/`into` 생성, 비대칭 연산자.
* **[온도 차](temperature-difference.md)** — 선형 그룹, 명시적 `KTemperatureDifference.ofKelvin(…)` 생성,
  선형 연산자.
