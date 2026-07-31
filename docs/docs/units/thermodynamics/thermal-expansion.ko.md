# 열팽창 계수 (Thermal Expansion Coefficient)

패키지: `org.pcsoft.framework.kunit.thermo.expansion`
기본 단위: **켈빈당** (`KThermalExpansionUnit.BASE == KThermalExpansionUnit.PER_KELVIN`)

유형: **구성된 단위**

열팽창 계수 `α`는 길이 (또는 면적, 부피)가 켈빈당 *상대적으로* 변화하는 정도입니다: `1/K`. 이는 온도 차의 역수입니다.

`KThermalExpansionUnitInstance`는 정규 형식 `temperature⁻¹` (`K⁻¹`)의 정확히 한 항으로 이루어진
`KMixedUnitInstance`를 감싸며, 항상 1/K로 정규화됩니다. 온도 차원은 **차** 그룹입니다 — 이 계수는 온도 *구간*당 변화를 나타냅니다.

!!! note "패키지 이름 대 클래스 이름"
패키지는 `thermo.expansion`이며, `thermo.thermalexpansion`이 아닙니다 — 단위 패키지는 그 분야 패키지의 이름을 반복해서는 안 됩니다. 타입은 전체 기술 용어
(`KThermalExpansionUnitInstance`)를 유지합니다.

## 이름이 붙은 단위

| 단위             | 기호    |            토큰 | 1/K로 1 |
|------------------|---------|----------------:|--------:|
| 켈빈당           | `1/K`   |     `perKelvin` |     1.0 |
| 화씨도당         | `1/°F`  | `perFahrenheit` |     1.8 |
| 백만분율 매 켈빈 | `ppm/K` |  `ppmPerKelvin` |    1e-6 |

물질 표에는 보통 `α`를 ppm/K로 나타내며, 이는 정확히 `micro.perKelvin`입니다. 모든 단위는 전체 SI 접두사 범위를 지원합니다.

## 전형적인 값

| 물질         |           α |
|--------------|------------:|
| 강철         |  ≈ 12 ppm/K |
| 콘크리트     |  ≈ 12 ppm/K |
| 알루미늄     |  ≈ 23 ppm/K |
| 유리(붕규산) | ≈ 3.3 ppm/K |

## 실전 예제: 여름철의 강철 빔

10 m 강철 빔 (α = 12 ppm/K)이 0 °C에서 50 °C로 따뜻해집니다. 얼마나 더 길어질까요? 이것이 다리에 신축 이음이 있는 이유입니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.expansion.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperature.celsius

val steel = 12 of ppmPerKelvin
val beam = 10 of meters
val rise = (50 of celsius) - (0 of celsius)   // 50 K

// 무차원의 상대적 변화
val strain = steel * rise                      // 6.0e-4

// 절대적 변화, 타입이 지정됨
val growth = steel.elongationOf(beam, rise)    // KLengthUnitInstance
growth into milli.meters                       // 6.0 mm

// 동일한 진폭에서의 100 m 교량 데크
steel.elongationOf(100 of meters, rise) into milli.meters // 60.0 mm
```

## 연산자

| 식                                                             | 결과 타입                            | 의미                    |
|----------------------------------------------------------------|--------------------------------------|-------------------------|
| `1 / temperatureDifference`                                    | `KThermalExpansionUnitInstance`      | 구간으로부터의 계수     |
| `1 / thermalExpansion`                                         | `KTemperatureDifferenceUnitInstance` | 계수로부터의 구간       |
| `thermalExpansion * temperatureDifference`                     | `Double`                             | **상대적** 변화(무차원) |
| `temperatureDifference * thermalExpansion`                     | `Double`                             | 동일(교환 법칙)         |
| `thermalExpansion.elongationOf(length, temperatureDifference)` | `KLengthUnitInstance`                | **절대적** 변화         |

두 역수 연산자는 좁게 선언되어 있어, `1 / d`와 `1 / α`는 그룹에 무관한 `Number.div`가 만들어 낼 일반적인 혼합 단위가 아니라 **타입이 지정된** 값을 반환합니다.

!!! warning "연쇄 `*` 대신 `elongationOf`"
`α · ΔT`는 의도적으로 일반 `Double`입니다 — 상대적 변화는 무차원이기 때문입니다. 그 `Double`을 길이에 곱하려면 루트 패키지의 일반 스칼라 `times`가 필요하며, 이를 명시적으로 임포트하면
이 그룹의
`times` 연산자를 **가립니다**. `elongationOf`는 정확히 이러한 이유로 가려질 수 없는 일반 함수이므로, 절대적 변화를 원할 때는 이것을 선호하세요.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.expansion.*

val sum = (12 of ppmPerKelvin) + (5 of ppmPerKelvin)   // 17 ppm/K
(12 of ppmPerKelvin) > (5 of ppmPerKelvin)             // true
(1 of perKelvin) == (1_000_000 of ppmPerKelvin)        // true
```

## 분해

두 분해 모두 동일한 타입이 지정된 값-동등 인스턴스를 생성합니다.

| 분해                        | 형식                                     | 결과                            |
|-----------------------------|------------------------------------------|---------------------------------|
| `1 / temperatureDifference` | 타입이 지정된 연산자                     | `KThermalExpansionUnitInstance` |
| `temperature⁻¹`             | 네이티브 표현식 + `toThermalExpansion()` | `KThermalExpansionUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.thermo.expansion.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val typed  = 1 / KTemperatureDifference.ofKelvin(1)
val native = (KTemperatureDifference.ofKelvin(1).toUnit() pow -1).toThermalExpansion()

typed == native // true - 둘 다 1.0 1/K
```

## toString 형식화

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.expansion.*

(12 of ppmPerKelvin).toString()                    // "1.2E-5 1/K"
"${(12 of ppmPerKelvin) into ppmPerKelvin} ppm/K"  // "12.0 ppm/K"
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를 보여줍니다. 지수는 유니코드 위첨자 (`²`, `³`, `⁻¹`)를 사용하며, `·`는 곱셈을,
`/`는 분수를 나타냅니다. 어떤 양이 분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학              | Kotlin                                   | 의미                         |
|-------------------|------------------------------------------|------------------------------|
| `1/K`             | `perKelvin`                              | 열팽창 계수, 기본 단위       |
| `K⁻¹`             | `ΔK pow -1`                              | 음의 지수로 표현된 동일한 양 |
| `ppm/K`           | `ppmPerKelvin`                           | 백만분율 매 켈빈(물질 표)    |
| `α = 1 / ΔT`      | `1 / KTemperatureDifference.ofKelvin(2)` | 구간으로부터의 계수          |
| `ε = α · ΔT`      | `steel * rise`                           | 상대적 변화(무차원)          |
| `Δl = α · l · ΔT` | `steel.elongationOf(beam, rise)`         | 절대적 길이 변화             |
