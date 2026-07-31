# 열유속밀도 (Heat Flux Density)

패키지: `org.pcsoft.framework.kunit.thermo.heatfluxdensity`
기본 단위: **제곱미터당 와트** (`KHeatFluxDensityUnit.BASE == KHeatFluxDensityUnit.WATT_PER_SQUARE_METER`)

유형: **구성된 단위**

열유속밀도는 단위 면적당 열류입니다: `power / area` (`W/m²`). 동일한 단위가 *복사조도 (irradiance)*와 *복사발산도 (radiant exitance)* — 표면에 부딪히거나 떠나는
복사의 강도 — 도 측정합니다.

`KHeatFluxDensityUnitInstance`는 정규 형식 `mass¹ · time⁻³` (`kg·s⁻³`)의 정확히 두 항으로 이루어진
`KMixedUnitInstance`를 감싸며, 항상 W/m²로 정규화됩니다.

!!! note "거리 차원이 상쇄됨"
`W/m² = kg·m²·s⁻³/m² = kg·s⁻³`. 따라서 정규 형식에는 거리 항이 **없습니다**.

총 열류 자체는 단순히 [전력](power.md)입니다. [열류](heat-flow.md)를 참조하세요. 온도 차로 나누면 이는 [열전달계수](heat-transfer-coefficient.md)가 됩니다.

## 이름이 붙은 단위

| 단위                     | 기호          |                                토큰 |  W/m²로 1 |
|--------------------------|---------------|------------------------------------:|----------:|
| 제곱미터당 와트          | `W/m²`        |               `wattsPerSquareMeter` |       1.0 |
| 시간-제곱피트당 Btu      | `Btu/(h·ft²)` |             `btusPerHourSquareFoot` | ≈ 3.15459 |
| 초-제곱센티미터당 칼로리 | `cal/(s·cm²)` | `caloriesPerSecondSquareCentimeter` |   41840.0 |

모두 전체 SI 접두사 범위를 지원합니다 (`kilo.wattsPerSquareMeter`, `milli.wattsPerSquareMeter` 등).

## 태양 상수

이 그룹은 평균 대기권 밖 태양 복사조도를 `SOLAR_CONSTANT`(1361 W/m²)로 노출합니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val sun = SOLAR_CONSTANT of wattsPerSquareMeter
sun into wattsPerSquareMeter // 1361.0
```

## 실전 예제: 태양광 어레이 규모 결정

맑은 날 지붕이 800 W/m²를 받습니다. 어레이는 25 m²를 덮고 입사 복사의 20 %를 변환합니다. 얼마의 전기 전력을 공급할까요?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val irradiance = 800 of wattsPerSquareMeter
val roof = (5 of meters) * (5 of meters)   // 25 m²

val incident = irradiance * roof           // KPowerUnitInstance
incident into kilo.watts                   // 20.0 kW

val electrical = incident * 0.2            // 스칼라 스케일링은 타입을 유지
electrical into kilo.watts                 // 4.0 kW

// 역방향: 20 % 효율로 10 kW 전기를 얻으려면 얼마의 지붕 면적이 필요한가?
val needed = (50 of kilo.watts) / irradiance // KAreaUnitInstance
needed into ((1 of meters) * (1 of meters))  // 62.5 m²
```

## 핵심 단위 (전력 & 면적)로 계산하기

| 표현식                    | 결과 타입                      | 의미               |
|---------------------------|--------------------------------|--------------------|
| `power / area`            | `KHeatFluxDensityUnitInstance` | 열유속밀도         |
| `heatFluxDensity * area`  | `KPowerUnitInstance`           | 총 열류            |
| `area * heatFluxDensity`  | `KPowerUnitInstance`           | 총 열류(교환 법칙) |
| `power / heatFluxDensity` | `KAreaUnitInstance`            | 퍼져 있는 면적     |

## 분해

두 분해 모두 동일한 타입이 지정된 값-동등 인스턴스를 생성합니다.

| 분해            | 형식                                    | 결과                                |
|-----------------|-----------------------------------------|-------------------------------------|
| `power / area`  | 타입이 지정된 연산자                    | `KHeatFluxDensityUnitInstance` 직접 |
| `mass · time⁻³` | 네이티브 표현식 + `toHeatFluxDensity()` | `KHeatFluxDensityUnitInstance`      |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val typed  = (1 of watts) / ((1 of meters) * (1 of meters))
val native = ((1000 of grams).toUnit() / ((1 of seconds).toUnit() pow 3)).toHeatFluxDensity()

typed == native // true - 둘 다 1.0 W/m²
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val total = (1 of kilo.wattsPerSquareMeter) + (500 of wattsPerSquareMeter)  // 1500 W/m²
(1 of kilo.wattsPerSquareMeter) > (500 of wattsPerSquareMeter)              // true
(1 of kilo.wattsPerSquareMeter) == (1000 of wattsPerSquareMeter)            // true
```

## toString 형식화

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

(1361 of wattsPerSquareMeter).toString()                                 // "1361.0 W/m²"
"${(1361 of wattsPerSquareMeter) into btusPerHourSquareFoot} Btu/(h·ft²)" // "431.4..."
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를 보여줍니다. 지수는 유니코드 위첨자 (`²`, `³`, `⁻¹`)를 사용하며, `·`는 곱셈을,
`/`는 분수를 나타냅니다. 어떤 양이 분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학        | Kotlin                                  | 의미                                     |
|-------------|-----------------------------------------|------------------------------------------|
| `W/m²`      | `wattsPerSquareMeter`                   | 열유속밀도, 기본 단위 — 이름이 붙은 토큰 |
| `kg·s⁻³`    | `grams / (seconds pow 3)`               | 기저 차원으로의 동일한 양                |
| `kW/m²`     | `kilo.wattsPerSquareMeter`              | 제곱미터당 킬로와트                      |
| `E_0`       | `SOLAR_CONSTANT of wattsPerSquareMeter` | 태양 상수, 1361 W/m²                     |
| `q̇ = P / A` | `(1000 of watts) / roof`                | 전력 ÷ 면적에서 유속밀도                 |
| `P = q̇ · A` | `irradiance * roof`                     | 유속밀도 × 면적에서 전력                 |
| `A = P / q̇` | `(50 of kilo.watts) / irradiance`       | 전력 ÷ 유속밀도에서 면적                 |
