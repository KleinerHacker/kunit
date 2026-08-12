# 절대 열저항

패키지: `org.pcsoft.framework.kunit.thermo.resistance`
기본 단위: **와트당 켈빈** (`KThermalResistanceUnit.BASE == KThermalResistanceUnit.KELVIN_PER_WATT`)

유형: **구성된 단위**

부품의 절대 열저항 `R`은 그것을 통과하는 단위 열량당 유지되는 온도 차입니다: `R = ΔT / P`, 단위는
`K/W`로 측정됩니다. 이는 **하나의 개체 전체**를 나타냅니다 — 이 히트싱크, 이 트랜지스터 패키지,
이 크기의 이 벽처럼요.

정준 정규형은 `mass⁻¹ · length⁻² · time³ · temperature`입니다.

!!! warning "열저항계수(thermal insulance)와는 다릅니다"
    이 그룹을 [열저항계수](thermal-insulance.md) `m²·K/W`(R-값)와 혼동하지 마십시오. 이는 동일한
    개념을 **단위 면적당**으로 정규화한 것입니다. 두 그룹은 면적 계수만큼 다르며, 정규형도 다르고
    따라서 타입도 다릅니다. 버전 0.8.0까지는 이름 `thermo.resistance` / `KThermalResistanceUnit`이
    이 열저항계수를 가리켰으나, 이제는 본 그룹을 가리킵니다.

## 이름이 붙은 단위

| 단위                       | 기호       |                             토큰 | K/W로 1 |
|----------------------------|------------|------------------------:|--------------:|
| 와트당 켈빈            | `K/W`      |         `kelvinsPerWatt` |           1.0 |
| 와트당 섭씨도    | `°C/W`     |  `degreesCelsiusPerWatt` |           1.0 |
| Btu당 시간·°F                | `h*°F/Btu` |    `hourFahrenheitPerBtu` |     ≈ 1.89563 |

1 °C의 온도 **차**는 1 K이므로, 반도체 및 히트싱크 데이터시트에서 사용되는 표기인
`degreesCelsiusPerWatt`는 수치적으로 `kelvinsPerWatt`와 동일합니다. 모든 토큰은 전체 SI 접두사
범위를 지원합니다.

## 분해

이 그룹은 하나의 분해를 가지며, 두 형식 모두 동일한 타입이 지정된 값-동등 인스턴스를 생성합니다.
이 그룹은 질량 항을 포함하므로 네이티브 형식은 **단위 템플릿**으로부터 조립됩니다: 원시 혼합 값은
그램 기반의 곱이며, 타입이 지정된 인스턴스는 그 값을 이름이 붙은 단위로 저장합니다.

| 형식             | 표현식                                                            |
|------------------|------------------------------------------------------------------------|
| 타입이 지정된 연산자   | `temperatureDifference / power`                                        |
| 네이티브 (`toX()`) | `(2.5 of s³ · K / kilo.grams / m²).toThermalResistance()`              |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.resistance.*

val typed = KTemperatureDifference.ofKelvin(30) / (12 of watts)
val kelvinTerm = KTemperatureDifference.ofKelvin(1).toUnit()
val native = (2.5 of (seconds pow 3) * kelvinTerm / kilo.grams.toUnit() / (meters pow 2))
    .toThermalResistance()

typed == native            // true
typed into kelvinsPerWatt  // 2.5
```

## 그룹으로 계산하기

| 식                                | 결과 타입                            | 의미              |
|-------------------------------------------|----------------------------------------|----------------------|
| `temperatureDifference / power`           | `KThermalResistanceUnitInstance`       | `R = ΔT / P`         |
| `thermalResistance * power`               | `KTemperatureDifferenceUnitInstance`   | `ΔT = R · P`         |
| `temperatureDifference / thermalResistance` | `KPowerUnitInstance`                 | 유도되는 열류 |
| `thermalResistance + …`                   | `KThermalResistanceUnitInstance`       | 직렬 저항 |
| `1 / thermalResistance`                   | `KThermalConductanceUnitInstance`      | `G = 1 / R`          |

열저항은 **직렬로 합산됩니다** — 이는 정확히 그룹의 동형 `+` 연산이 수행하는 일입니다.

## 실전 예제 — 히트싱크 예산

한 전력 트랜지스터가 **12 W**를 방출합니다. 열 경로는 접합-케이스 간 0.5 K/W, 케이스-히트싱크 간
0.2 °C/W, 히트싱크-대기 간 1.8 K/W입니다. 접합부는 주변 온도보다 얼마나 높아질까요?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.resistance.*

val chain = (0.5 of kelvinsPerWatt) + (0.2 of degreesCelsiusPerWatt) + (1.8 of kelvinsPerWatt)
chain into kelvinsPerWatt                                   // 2.5

val rise = chain * (12 of watts)                            // KTemperatureDifferenceUnitInstance
rise into KTemperatureDifference.ofKelvin(1)                // 주변보다 30.0 K 높음

// 25 K 한계를 위해 얼마나 많은 전력을 방출할 수 있을까?
val budget = KTemperatureDifference.ofKelvin(25) / chain    // KPowerUnitInstance
budget into watts                                            // 10.0 W
```

## 값 의미론

`equals`/`hashCode`는 **정규화된 K/W 값**을 비교하므로
`(1 of kelvinsPerWatt) == (1 of degreesCelsiusPerWatt)`입니다. `toString()`은 값을 기본 단위로
표시합니다: `"2.5 K/W"`.

## 참고

* [열저항계수](thermal-insulance.ko.md) — 단위 면적당으로 표현한 동일한 개념(R-값).
* [열전도도](thermal-conductance.ko.md) — 그 역수 값.
* [열역학 개요](overview.ko.md)
