# 열류 (Heat Flow)

패키지: `org.pcsoft.framework.kunit.common.power`
기본 단위: **와트** (`KPowerUnit.BASE == KPowerUnit.WATT`)

유형: **구성된 단위**

열류 `Q̇`(열 전력, 또는 열류량이라고도 함)은 단위 시간당 전달되는 열의 양입니다: `W`. 이는
[전력](power.md) — 시간당 에너지 — 과 **차원적으로도 물리적으로도 동일**하며, 따라서 KUnit은 이를
`KPowerUnitInstance`로 모델링합니다.

## 열류에 자체 타입이 없는 이유

열류는 별도의 양이 아니라, 우연히 열적인 전력입니다. 정규 형식 `mass¹ · distance² · time⁻³`은 정확히 하나만 존재하며, 그 위에 두 번째 타입을 두면 물리적 의미를 추가하지 않고도
`toPower()`를 모호하게 만듭니다. 와트가 전기 모터, 레이저 또는 라디에이터를 나타내는지는 맥락의 문제이지 차원의 문제가 아닙니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.power.*

val motor = 2 of kilo.watts     // 기계적 전력
val radiator = 1500 of watts    // 열류
// 둘 다 KPowerUnitInstance
```

## 실전 예제: 라디에이터

1500 W 정격의 라디에이터가 4시간 동안 가동됩니다. 얼마나 많은 에너지를 전달하며, 0.6 m² 표면에 대해 어떤 열유속밀도를 발생시킬까요?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.common.power.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter

val radiator = 1500 of watts
val runtime = 4 of hours

val energy = radiator * runtime          // KEnergyUnitInstance
energy into kilo.joules                  // 21_600.0 kJ (= 6 kWh)

val surface = (1 of meters) * (0.6 of meters)  // 0.6 m²
val flux = radiator / surface            // KHeatFluxDensityUnitInstance
flux into wattsPerSquareMeter            // 2500.0 W/m²
```

## 이 분야에서 열류가 등장하는 곳

| 표현식                   | 결과 타입                      | 의미                               |
|--------------------------|--------------------------------|------------------------------------|
| `energy / time`          | `KPowerUnitInstance`           | 열 ÷ 지속 시간에서 열류            |
| `power * time`           | `KEnergyUnitInstance`          | 지속 시간 동안 전달된 열           |
| `power / area`           | `KHeatFluxDensityUnitInstance` | [열유속밀도](heat-flux-density.md) |
| `heatFluxDensity * area` | `KPowerUnitInstance`           | 표면을 통과하는 총 열류            |

벽의 열 손실은 고전적인 사슬입니다: [열전달계수](heat-transfer-coefficient.md)에 온도 차를 곱하면
[열유속밀도](heat-flux-density.md)가 나오고, 그것에 면적을 곱하면 와트 단위의 열류가 나옵니다.

## 함께 보기

* [전력](power.md) — 열류가 공유하는 타입으로, 전체 단위 표와 모든 분해 및 완전한 연산자 표면을 포함
* [열유속밀도](heat-flux-density.md) — 단위 면적당 열류
* [열전달계수](heat-transfer-coefficient.md) — 켈빈당 열유속밀도
* [에너지](energy.md) — 시간에 대해 적분된 열류

## 표기법

아래 표는 이 양이 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를 보여줍니다. 지수는 유니코드 위첨자 (`²`, `³`, `⁻¹`)를 사용하며, `·`는 곱셈을, `/`는 분수를
나타냅니다.

| 수학        | Kotlin                                     | 의미                         |
|-------------|--------------------------------------------|------------------------------|
| `W`         | `watts`                                    | 열류, 기본 단위(전력과 공유) |
| `kg·m²·s⁻³` | `grams * (meters pow 2) / (seconds pow 3)` | 기저 차원으로의 동일한 양    |
| `Q̇ = Q / t` | `(21600 of kilo.joules) / runtime`         | 열 ÷ 지속 시간에서 열류      |
| `Q = Q̇ · t` | `radiator * runtime`                       | 열류 × 지속 시간에서 열      |
| `q̇ = Q̇ / A` | `radiator / surface`                       | 열류 ÷ 면적에서 열유속밀도   |
