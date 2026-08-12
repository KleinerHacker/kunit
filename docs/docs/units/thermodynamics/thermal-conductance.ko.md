# 열전도 (Thermal Conductance)

패키지: `org.pcsoft.framework.kunit.thermo.conductance`
기본 단위: **켈빈당 와트** (`KThermalConductanceUnit.BASE == KThermalConductanceUnit.WATT_PER_KELVIN`)

유형: **구성된 단위**

어떤 부품의 열전도 `G`는 단위 온도차당 얼마나 많은 열이 흐르는지를 나타냅니다: `G = P / ΔT`, 단위는
`W/K`입니다. 이는 [절대 열저항](thermal-resistance.md)의 정확한 역수이며, 열 경로가 **병렬**로 존재할 때
더 편리한 형태입니다 — 병렬 열전도는 단순히 더해집니다.

정규 기저 차원 형식은 `mass · length² · time⁻³ · temperature⁻¹`입니다.

## 이름이 붙은 단위

| 단위                  | 기호         |                   토큰 | W/K로 1 |
|-----------------------|--------------|------------------------:|--------:|
| 켈빈당 와트           | `W/K`        |         `wattsPerKelvin` |     1.0 |
| 시간-화씨도당 Btu     | `Btu/(h*°F)` | `btusPerHourFahrenheit` | ≈ 0.52753 |

모든 토큰이 전체 SI 접두사를 지원합니다 (`milli.wattsPerKelvin`, …).

## 분해

이 그룹은 하나의 분해를 가지며, 두 형식 모두 동일한 타입이 지정된 값-동등 인스턴스를 생성합니다. 이 그룹은
질량 항을 포함하므로 네이티브 형식은 **단위 템플릿**으로부터 조립됩니다.

| 형식                  | 표현                                                             |
|-----------------------|--------------------------------------------------------------------|
| 타입이 지정된 연산자  | `power / temperatureDifference`                                     |
| 네이티브 (`toX()`)    | `(0.4 of kilo.grams · m² / s³ / K).toThermalConductance()`          |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.conductance.*

val typed = (12 of watts) / KTemperatureDifference.ofKelvin(30)
val kelvinTerm = KTemperatureDifference.ofKelvin(1).toUnit()
val native = (0.4 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / kelvinTerm)
    .toThermalConductance()

typed == native            // true
typed into wattsPerKelvin  // 0.4
```

## 그룹을 이용한 계산

| 식                                            | 결과 타입                              | 의미                    |
|-------------------------------------------------|-----------------------------------------|-------------------------|
| `power / temperatureDifference`                 | `KThermalConductanceUnitInstance`      | `G = P / ΔT`            |
| `thermalConductance * temperatureDifference`    | `KPowerUnitInstance`                   | `P = G · ΔT`            |
| `power / thermalConductance`                    | `KTemperatureDifferenceUnitInstance`   | 필요한 온도차           |
| `thermalConductance + …`                        | `KThermalConductanceUnitInstance`      | 병렬 열 경로            |
| `1 / thermalConductance`                        | `KThermalResistanceUnitInstance`       | `R = 1 / G`             |
| `1 / thermalResistance`                         | `KThermalConductanceUnitInstance`      | `G = 1 / R`             |

## 실전 예제: 두 개의 병렬 열 경로

어떤 모듈이 밑판 (0.4 W/K)과 하우징 (0.1 W/K)을 통해 열을 손실합니다. 병렬이므로 열전도는 더해지고,
역수를 취하면 총 열저항을 얻습니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.thermo.resistance.kelvinsPerWatt
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.conductance.*

val total = (0.4 of wattsPerKelvin) + (0.1 of wattsPerKelvin)
total into wattsPerKelvin                                  // 0.5

val r = 1 / total                                           // KThermalResistanceUnitInstance
r into kelvinsPerWatt                                       // 2.0

val heat = total * KTemperatureDifference.ofKelvin(30)      // KPowerUnitInstance
heat into watts                                             // ΔT = 30 K에서 15.0 W가 방출됨
```

## 값 시맨틱

`equals`/`hashCode`는 **정규화된 W/K 값**을 비교하므로
`(1 of wattsPerKelvin) == (1000 of milli.wattsPerKelvin)`입니다. `toString()`은 기본 단위로 값을 표시합니다:
`"0.4 W/K"`.

## 참고

* [절대 열저항](thermal-resistance.ko.md) — 그 역수량.
* [열절연성](thermal-insulance.ko.md) — 저항의 단위 면적당 형태.
* [열전달계수](heat-transfer-coefficient.ko.md) — 이 양의 단위 면적당 형태.
* [열역학 개요](overview.ko.md)
