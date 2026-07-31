# 온도 구배 (Temperature Gradient)

패키지: `org.pcsoft.framework.kunit.thermo.temperaturegradient`
기본 단위: **미터당 켈빈** (`KTemperatureGradientUnit.BASE == KTemperatureGradientUnit.KELVIN_PER_METER`)

유형: **구성된 단위**

온도 구배는 단위 길이당 온도 변화입니다: `temperatureDifference / length` (`K/m`). 이는 전도의 구동 양입니다 — [열전도율](thermal-conductivity.md)을
곱하면 [열유속밀도](heat-flux-density.md)가 됩니다.

`KTemperatureGradientUnitInstance`는 정규 형식 `temperature¹ · distance⁻¹` (`K·m⁻¹`)의 정확히 두 항으로 이루어진 `KMixedUnitInstance`를
감싸며, 항상 K/m로 정규화됩니다.

!!! note "구배는 길이당 *변화*"
온도 차원은 **차** 그룹 (`KTemperatureDifferenceUnit`)입니다. 오프셋을 가진 절대 척도 (°C, °F)는 구배에서 의미가 없습니다 — 구간만이 의미가 있습니다. 이 때문에 `°F/ft`는
−32 오프셋이 아니라 화씨 *구간* 계수 5/9로 변환됩니다.

## 이름이 붙은 단위

| 단위            | 기호    |                 토큰 |    K/m로 1 |
|-----------------|---------|---------------------:|-----------:|
| 미터당 켈빈     | `K/m`   |     `kelvinPerMeter` |        1.0 |
| 킬로미터당 켈빈 | `K/km`  | `kelvinPerKilometer` |      0.001 |
| 피트당 화씨도   | `°F/ft` |  `fahrenheitPerFoot` | ≈ 1.822689 |

모두 전체 SI 접두사 범위를 지원합니다 (`milli.kelvinPerMeter` 등).

## 실전 예제: 지열 구배

지각은 깊이 1km당 약 25 K 따뜻해집니다. 시추공이 3.5 km에 도달합니다. 바닥에서는 얼마나 더 따뜻하며, 100 K 상승을 위해서는 얼마나 깊이 뚫어야 할까요?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val geothermal = 25 of kelvinPerKilometer
val borehole = 3.5 of kilo.meters

val rise = geothermal * borehole            // KTemperatureDifferenceUnitInstance
rise into KTemperatureDifference.ofKelvin(1) // 바닥에서 87.5 K 더 따뜻함

val depthFor100K = KTemperatureDifference.ofKelvin(100) / geothermal // KLengthUnitInstance
depthFor100K into kilo.meters               // 4.0 km
depthFor100K into meters                    // 4000.0 m
```

## 핵심 단위 (온도 차 & 길이)로 계산하기

| 표현식                                        | 결과 타입                            | 의미             |
|-----------------------------------------------|--------------------------------------|------------------|
| `temperatureDifference / length`              | `KTemperatureGradientUnitInstance`   | 구배             |
| `temperatureGradient * length`                | `KTemperatureDifferenceUnitInstance` | 길이에 걸친 상승 |
| `length * temperatureGradient`                | `KTemperatureDifferenceUnitInstance` | 상승(교환 법칙)  |
| `temperatureDifference / temperatureGradient` | `KLengthUnitInstance`                | 걸쳐 있는 길이   |

## 분해

두 분해 모두 동일한 타입이 지정된 값-동등 인스턴스를 생성합니다.

| 분해                             | 형식                                        | 결과                               |
|----------------------------------|---------------------------------------------|------------------------------------|
| `temperatureDifference / length` | 타입이 지정된 연산자                        | `KTemperatureGradientUnitInstance` |
| `temperature · distance⁻¹`       | 네이티브 표현식 + `toTemperatureGradient()` | `KTemperatureGradientUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val typed  = KTemperatureDifference.ofKelvin(1) / (1 of meters)
val native = (KTemperatureDifference.ofKelvin(1).toUnit() / (1 of meters).toUnit()).toTemperatureGradient()

typed == native // true - 둘 다 1.0 K/m
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*

val total = (1 of kelvinPerMeter) + (500 of kelvinPerKilometer)  // 1.5 K/m
(1 of kelvinPerMeter) > (500 of kelvinPerKilometer)              // true
(1 of kelvinPerMeter) == (1000 of kelvinPerKilometer)            // true
```

## toString 형식화

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*

(25 of kelvinPerKilometer).toString()                        // "0.025 K/m"
"${(25 of kelvinPerKilometer) into kelvinPerKilometer} K/km" // "25.0 K/km"
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를 보여줍니다. 지수는 유니코드 위첨자 (`²`, `³`, `⁻¹`)를 사용하며, `·`는 곱셈을,
`/`는 분수를 나타냅니다. 어떤 양이 분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학          | Kotlin                                                     | 의미                       |
|---------------|------------------------------------------------------------|----------------------------|
| `K/m`         | `kelvinPerMeter`                                           | 온도 구배, 기본 단위       |
| `K·m⁻¹`       | `ΔK / meters`                                              | 기저 차원으로의 동일한 양  |
| `K/km`        | `kelvinPerKilometer`                                       | 킬로미터당 켈빈(지열 구배) |
| `°F/ft`       | `fahrenheitPerFoot`                                        | 피트당 화씨도              |
| `∇T = ΔT / L` | `KTemperatureDifference.ofKelvin(25) / (1 of kilo.meters)` | 상승 ÷ 길이에서 구배       |
| `ΔT = ∇T · L` | `geothermal * borehole`                                    | 구배 × 길이에서 상승       |
| `L = ΔT / ∇T` | `KTemperatureDifference.ofKelvin(100) / geothermal`        | 상승 ÷ 구배에서 길이       |
