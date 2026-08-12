# 비중량

패키지: `org.pcsoft.framework.kunit.mechanic.specificweight`
기본 단위: **뉴턴 매 세제곱미터**
(`KSpecificWeightUnit.BASE == KSpecificWeightUnit.NEWTON_PER_CUBIC_METER`)

유형: **구성 단위**

비중량 `γ`는 단위 부피당 **중력**입니다: `γ = F / V = ρ · g`. 이는 정수역학이 기술되는
기준이며 — 특정 깊이에서의 압력은 단순히 `p = γ · h`입니다 — 토목 공학에서 토양 및 건설 자재에
대해 사용됩니다. 물은 약 9.81 kN/m³입니다.

정준 기저 차원 표준형은 `mass · length⁻² · time⁻²`입니다.

!!! note "중량이지 질량이 아닙니다"
    비중량은 지역의 중력 가속도에 의존하지만, [밀도](density.ko.md)는 그렇지 않습니다. 달에서는
    재료가 밀도를 유지하지만 비중량은 약 6분의 1이 됩니다.

## 명명된 단위

| 단위                          | 기호     |                     토큰 | 1 단위 (N/m³ 기준) |
|--------------------------------|------------|--------------------------:|---------------:|
| 뉴턴 매 세제곱미터                | `N/m^3`    |    `newtonsPerCubicMeter` |            1.0 |
| 킬로뉴턴 매 세제곱미터            | `kN/m^3`   | `kilonewtonsPerCubicMeter` |           1000 |
| 파운드힘 매 세제곱피트            | `lbf/ft^3` | `poundsForcePerCubicFoot` |     ≈ 157.0875 |

모든 토큰은 모든 SI 접두사를 받아들입니다. 인접한 힘, 압력, 밀도 그룹과 마찬가지로 이 인스턴스는
**그램 기반의 원시 성분 값**을 저장하며, N/m³로 읽을 때는 1000으로 나눕니다.

## 분해

이 그룹에는 **두 가지** 분해가 있습니다. 둘 다 동일한 정규화 팩토리로 합류합니다:

| 형태               | 표현식                                                        |
|--------------------|--------------------------------------------------------------------|
| 타입 연산자 A       | `force / volume`                                                  |
| 타입 연산자 B       | `density * acceleration` (`γ = ρ · g`)                             |
| 원시 형태 (`toX()`) | `(1 of kilo.grams / m² / s²).toSpecificWeight()`                   |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.acceleration.standardGravities
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.specificweight.*

val cubicMeter = (1 of meters) * (1 of meters) * (1 of meters)
val water = (1000 of kilo.grams) / cubicMeter

val viaForce = (9806.65 of newtons) / cubicMeter        // A
val viaDensity = water * (1 of standardGravities)       // B

viaForce == viaDensity                                   // true
viaForce into newtonsPerCubicMeter                       // 9806.65
```

## 그룹으로 계산하기

| 표현식                            | 결과 타입                      | 의미                  |
|--------------------------------------|-----------------------------------|--------------------------|
| `force / volume`                    | `KSpecificWeightUnitInstance`    | `γ = F / V`              |
| `density * acceleration`            | `KSpecificWeightUnitInstance`    | `γ = ρ · g`              |
| `specificWeight * volume`           | `KForceUnitInstance`             | 중력                       |
| `force / specificWeight`            | `KVolumeUnitInstance`            | 그것이 채우는 부피          |
| `specificWeight / acceleration`     | `KDensityUnitInstance`           | `ρ`로 되돌아감              |
| `specificWeight / density`          | `KAccelerationUnitInstance`      | `g`로 되돌아감              |

## 실제 사례 — 물탱크의 무게

**300 리터** 물탱크와 그 내용물이 바닥에 가하는 힘:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.specificweight.*

val water = 9.80665 of kilonewtonsPerCubicMeter
val weight = water * (300 of liters)      // KForceUnitInstance
weight into newtons                        // ≈ 2942.0 N

// 반대로: 1 kN의 무게가 나가는 부피는 얼마인가?
val v = (1000 of newtons) / water          // KVolumeUnitInstance
v into liters                               // ≈ 102.0 l
```

## 값 의미론

`equals`/`hashCode`는 **정규화된 성분 값**을 비교하므로
`(1 of kilonewtonsPerCubicMeter) == (1000 of newtonsPerCubicMeter)`입니다. `toString()`은
기본 단위로 값을 표현합니다: `"9807.0 N/m^3"`.

## 참고

* [밀도](density.ko.md) — 중력에 무관한 질량 기반 대응량.
* [힘](force.ko.md)과 [압력](pressure.ko.md) — 인접한 그룹.
* [역학 개요](overview.ko.md)
