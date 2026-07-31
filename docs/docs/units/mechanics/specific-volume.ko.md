# 비체적

패키지: `org.pcsoft.framework.kunit.mechanic.specificvolume`
기본 단위: **세제곱미터 매 킬로그램**
(`KSpecificVolumeUnit.BASE == KSpecificVolumeUnit.CUBIC_METERS_PER_KILOGRAM`)

유형: **구성된 단위**

비체적 `v` 는 단위 질량이 차지하는 부피입니다 — **[밀도](density.md)의 역수**입니다. 이는 **구성된** 단위입니다 — `length³ · mass⁻¹`(`m³/kg`)의 합성입니다.

`KSpecificVolumeUnitInstance` 는 정확히 두 항으로 된 표준 정규화 형태의 `KMixedUnitInstance` 를 감쌉니다: `KDistanceUnit.BASE`(미터)가 `+3`,
`KMassUnit.BASE`(그램)가 `-1` 입니다. 이 라이브러리의 질량 성분은 그램으로 정규화되어 있으므로, 저장된 값은 원시 그램 기반 성분 값이며 m³/kg 읽기는 고정 계수로 연결됩니다.

## 이름이 붙은 단위

| 단위                   | 기호      |                      토큰 | m³/kg 로 1 단위 |
|------------------------|-----------|--------------------------:|----------------:|
| 세제곱미터 매 킬로그램 | `m^3/kg`  |  `cubicMetersPerKilogram` |             1.0 |
| 리터 매 킬로그램       | `l/kg`    |       `litersPerKilogram` |            1e-3 |
| 세제곱센티미터 매 그램 | `cm^3/g`  | `cubicCentimetersPerGram` |            1e-3 |
| 세제곱피트 매 파운드   | `ft^3/lb` |       `cubicFeetPerPound` |     ≈ 0.0624280 |

모든 단위는 전체 SI 접두사 범위를 지원합니다 (`milli.cubicMetersPerKilogram`).

## 핵심 단위로 계산하기

| 식                                               | 결과 타입                     | 의미        |
|--------------------------------------------------|-------------------------------|-------------|
| `volume / mass`                                  | `KSpecificVolumeUnitInstance` | `v = V / m` |
| `specificvolume * mass`, `mass * specificvolume` | `KVolumeUnitInstance`         | `V = v · m` |
| `volume / specificvolume`                        | `KMassUnitInstance`           | `m = V / v` |
| `1 / density`                                    | `KSpecificVolumeUnitInstance` | `v = 1 / ρ` |
| `1 / specificvolume`                             | `KDensityUnitInstance`        | `ρ = 1 / v` |

역수 연산자는 타입이 있습니다: `1 / density` 는 제네릭 혼합 단위로 격하되지 않고 실제 단위 타입을 유지합니다. 네이티브 형식은 `toSpecificVolume()` 으로 변환됩니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mechanic.specificvolume.*

val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

val viaQuotient = (2 of liters) / (1 of kilo.grams)
val viaReciprocal = 1 / water

viaQuotient into litersPerKilogram   // 2.0
viaReciprocal into litersPerKilogram // 1.0
(1 / viaReciprocal).value == water.value // true - 정확한 왕복 변환
```

## 실전 예제: 증기표 조회

1 bar 에서의 포화 증기는 비체적이 약 1.694 m³/kg 입니다. 그 증기 2 kg 이 차지하는 부피는 얼마이며, 밀도는 얼마입니까?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.specificvolume.*
import org.pcsoft.framework.kunit.pow

val v = 1.694 of cubicMetersPerKilogram
val volume = v * (2 of kilo.grams)   // KVolumeUnitInstance
volume into liters                   // 3388.0

val rho = 1 / v                      // KDensityUnitInstance
rho into (kilo.grams / (meters pow 3)) // ≈ 0.5903
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.specificvolume.*

val sum = (10 of litersPerKilogram) + (4 of litersPerKilogram) // 14 l/kg
(1 of cubicMetersPerKilogram) > (1 of litersPerKilogram)       // true
(1 of litersPerKilogram) == (1 of cubicCentimetersPerGram)     // true (같은 값)
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.specificvolume.*

(2 of cubicMetersPerKilogram).toString()                      // "2.0 m^3/kg"(기본 단위)
"${(2 of cubicMetersPerKilogram) into litersPerKilogram} l/kg" // "2000.0 l/kg"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학        | Kotlin                                 | 의미                              |
|-------------|----------------------------------------|-----------------------------------|
| `m³/kg`     | `cubicMetersPerKilogram`               | 비체적, 기본 단위(이름 붙은 토큰) |
| `m³·kg⁻¹`   | `(meters pow 3) * (kilo.grams pow -1)` | 순수한 곱으로 표현한 같은 양      |
| `l/kg`      | `litersPerKilogram`                    | 리터 매 킬로그램 읽기             |
| `v = V / m` | `volume / mass`                        | 타입 분해                         |
| `v = 1 / ρ` | `1 / density`                          | 밀도의 역수                       |
| `ρ = 1 / v` | `1 / specificvolume`                   | 다시 밀도로                       |
