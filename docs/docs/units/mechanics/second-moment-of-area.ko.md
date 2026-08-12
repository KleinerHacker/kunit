# 단면 2차 모멘트

패키지: `org.pcsoft.framework.kunit.kinematic.distance`
기본 단위: **미터의 4제곱** (`m⁴`, distance 그룹의 지수 4 리프)

유형: **구성 단위**

단면 2차 모멘트 `I` (면적 관성 모멘트)는 보 단면이 굽힘에 대해 얼마나 강성을 갖는지를 결정하는
기하학적 속성입니다 — 굽힘 강성 `EI`의 `I`입니다. 강재 단면표에서는 `cm⁴`로, 작은 단면에서는
`mm⁴`로 표기됩니다.

이 사이트의 다른 그룹과 달리 독립된 그룹이 아닙니다: distance 그룹의 **지수 4 리프**인
`KSecondMomentOfAreaUnitInstance`이며, [길이](../kinematics/distance.ko.md) (지수 1), 면적
(지수 2), 부피 (지수 3)와 나란히 위치합니다.

!!! warning "관성 모멘트가 아닙니다"
    이를 각가속도에 대한 저항을 나타내는 *질량* [관성 모멘트](moment-of-inertia.ko.md) (`kg·m²`)와
    혼동하지 마십시오. 이름은 비슷하지만 차원은 다릅니다.

## 명명된 토큰

| 단위                  | 기호 |                토큰 | 1 단위 (m⁴ 기준) |
|-----------------------|--------|---------------------:|-------------:|
| 미터의 4제곱            | `m⁴`   |       `quarticMeters` |          1.0 |
| 센티미터의 4제곱        | `cm⁴`  |  `quarticCentimeters` |         1e-8 |
| 밀리미터의 4제곱        | `mm⁴`  |  `quarticMillimeters` |        1e-12 |
| 인치의 4제곱            | `in⁴`  |       `quarticInches` | ≈ 4.16231e-7 |

모든 토큰은 모든 SI 접두사를 받아들입니다.

## 리프로 계산하기

지수 4에 도달하는 모든 곱은 이제 일반적인 `KDistanceUnitInstance` 대신 타입화된 리프를
반환합니다:

| 표현식                        | 결과 타입                              | 의미                        |
|---------------------------------|---------------------------------------------|--------------------------------|
| `area * area`                  | `KSecondMomentOfAreaUnitInstance`          | m² · m² = m⁴                   |
| `volume * length`              | `KSecondMomentOfAreaUnitInstance`          | m³ · m = m⁴                    |
| `length * volume`              | `KSecondMomentOfAreaUnitInstance`          | m · m³ = m⁴                    |
| `secondMomentOfArea / length`  | `KVolumeUnitInstance`                      | 단면 계수                       |
| `secondMomentOfArea / area`    | `KAreaUnitInstance`                        | m⁴/m² = m²                     |
| `secondMomentOfArea / volume`  | `KLengthUnitInstance`                      | m⁴/m³ = m                      |
| `secondMomentOfArea + …`       | `KSecondMomentOfAreaUnitInstance`          | 조립 단면의 각 부분              |

덧셈은 동일한 차원으로 제한됩니다 — `secondMomentOfArea + area`는 `length + area`와
마찬가지로 **컴파일 오류**가 발생합니다.

원시 형태는 `toSecondMomentOfArea()`로 변환합니다:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*

val native = ((1 of centi.meters).toUnit() pow 4).toSecondMomentOfArea()
native into quarticCentimeters      // 1.0
```

## 실제 사례 — 직사각형 보

폭 `b`, 높이 `h`인 직사각형에 대해 `I = b·h³/12`입니다. 100 mm × 200 mm의 경우:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*

val b = 100 of milli.meters
val h = 200 of milli.meters

val i = (b * (h * h * h)) / 12       // KSecondMomentOfAreaUnitInstance
i into quarticCentimeters             // ≈ 6666.7 cm⁴

// 단면 계수 W = I / (h/2)
val w = i / (h / 2)                   // KVolumeUnitInstance
w.value                                // ≈ 6.667e-4 m³

// 조립 단면: 동일한 보 두 개를 나란히 배치
val doubled = i + i
doubled into quarticCentimeters        // ≈ 13333.3
```

## 값 의미론

`equals`/`hashCode` 및 비교는 정규화된 `m⁴` 값에 대해, 동일한 차원으로 제한하여 동작합니다.
`exponent`는 `4`를 반환합니다.

## 참고

* [거리](../kinematics/distance.ko.md) — 이 리프가 속한 그룹.
* [관성 모멘트](moment-of-inertia.ko.md) — 이름은 비슷하지만 *질량* 기반인 양.
* [역학 개요](overview.ko.md)
