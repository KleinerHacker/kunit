# 부피 열용량 (Volumetric Heat Capacity)

패키지: `org.pcsoft.framework.kunit.thermo.volumetricheatcapacity`
기본 단위: **입방미터-켈빈당 줄**
(`KVolumetricHeatCapacityUnit.BASE == KVolumetricHeatCapacityUnit.JOULE_PER_CUBIC_METER_KELVIN`)

유형: **구성된 단위**

부피 열용량 `c_v`는 어떤 물질의 **부피**가 켈빈당 얼마의 열을 저장하는지를 나타냅니다:
`c_v = C / V = c · ρ`. 이는 건물, 저장 탱크, 또는 방열판이 실제로 얼마의 열질량을 가지는지를
결정하는 값입니다 — 밀도가 다르면 비열이 같은 두 물질이라도 저장하는 열량은 크게 달라집니다.

정규 기저 차원 형식은 `mass · length⁻¹ · time⁻² · temperature⁻¹`입니다.

## 이름이 붙은 단위

| 단위                            | 기호           |                              토큰 | J/(m³·K)로 1 |
|---------------------------------|----------------|-----------------------------------:|-------------:|
| 입방미터-켈빈당 줄              | `J/(m^3*K)`    |       `joulesPerCubicMeterKelvin` |          1.0 |
| 입방센티미터-켈빈당 칼로리      | `cal/(cm^3*K)` | `caloriesPerCubicCentimeterKelvin` |      4.184e6 |

값이 크기 때문에 메가줄 형태가 실용적입니다: 물은 약 4.18 MJ/(m³·K)입니다. 모든 토큰은
모든 SI 접두사를 지원합니다 (`mega.joulesPerCubicMeterKelvin` 등).

## 분해

이 그룹에는 **두 가지** 분해가 있습니다. 둘 다 동일한 정규화 팩토리로 수렴되므로
동일한 타입이 지정된 값-동등 인스턴스를 생성합니다:

| 형식                 | 표현식                                                              |
|----------------------|------------------------------------------------------------------------|
| 타입이 지정된 연산자 A | `heatCapacity / volume`                                          |
| 타입이 지정된 연산자 B | `specificHeatCapacity * density`                                 |
| 네이티브 (`toX()`)   | `(1 of kilo.grams / m / s² / K).toVolumetricHeatCapacity()`      |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin
import org.pcsoft.framework.kunit.thermo.volumetricheatcapacity.*

val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

val viaHeatCapacity = (4184 of joulesPerKelvin) / (1 of liters)   // A
val viaDensity = (4184 of joulesPerKilogramKelvin) * water        // B

viaHeatCapacity == viaDensity                                      // true
viaHeatCapacity into mega.joulesPerCubicMeterKelvin                // 4.184
```

## 그룹으로 계산하기

| 표현식                                            | 결과 타입                                | 의미                    |
|------------------------------------------------------|--------------------------------------------|--------------------------|
| `heatCapacity / volume`                              | `KVolumetricHeatCapacityUnitInstance`       | `c_v = C / V`           |
| `specificHeatCapacity * density`                     | `KVolumetricHeatCapacityUnitInstance`       | `c_v = c · ρ`           |
| `volumetricHeatCapacity * volume`                    | `KHeatCapacityUnitInstance`                 | `C = c_v · V`           |
| `heatCapacity / volumetricHeatCapacity`              | `KVolumeUnitInstance`                       | 해당하는 부피            |
| `volumetricHeatCapacity / density`                   | `KSpecificHeatCapacityUnitInstance`         | 다시 `c`로              |
| `volumetricHeatCapacity / specificHeatCapacity`      | `KDensityUnitInstance`                      | 다시 `ρ`로              |

## 실전 예제: 물 저장 탱크의 열질량

물 **300 l**를 담은 저장 탱크: 1 K 올리는 데 얼마의 에너지가 필요하며, 동일한 부피의 콘크리트
(≈ 2.0 MJ/(m³·K))와 비교하면 어떨까요?

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.volumetricheatcapacity.*

val water = 4.184 of mega.joulesPerCubicMeterKelvin
val tank = water * (300 of liters)          // KHeatCapacityUnitInstance
tank into kilo.joulesPerKelvin              // ≈ 1255.2 kJ/K

val concrete = 2.0 of mega.joulesPerCubicMeterKelvin
(water into mega.joulesPerCubicMeterKelvin) /
    (concrete into mega.joulesPerCubicMeterKelvin)   // ≈ 2.09배의 열질량
```

## 값 시맨틱스

`equals`/`hashCode`는 **정규화된 J/(m³·K) 값**을 비교하므로,
`(1 of caloriesPerCubicCentimeterKelvin) == (4.184e6 of joulesPerCubicMeterKelvin)`입니다. `toString()`은
기본 단위로 값을 표시합니다: `"4184000.0 J/(m^3*K)"`.

## 참고

* [열용량](heat-capacity.ko.md) — 정규화되지 않은 양.
* [비열](specific-heat-capacity.ko.md) — 부피가 아닌 **질량**당의 동일한 개념.
* [열역학 개요](overview.ko.md)
