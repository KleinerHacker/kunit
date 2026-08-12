# 디옵터 (굴절력)

패키지: `org.pcsoft.framework.kunit.common.reciprocallength`
기본 단위: **매 미터**(`KReciprocalLengthUnit.BASE == KReciprocalLengthUnit.RECIPROCAL_METER`)

종류: **구성 단위**

렌즈의 굴절력 `D`는 초점 거리의 역수입니다: `D = 1 / f`. 그 단위는 **디옵터**이며, 이는 정확히 매 미터의
역수와 같습니다 — 1 m에서 초점을 맺는 렌즈는 1 dpt, 0.5 m에서 초점을 맺는 렌즈는 2 dpt입니다.

이 물리량의 차원은 `distance⁻¹`로, 분광학의 [파수](../mechanics/wavenumber.md)와 **동일**합니다. KUnit은
이 두 가지 해석을 위해 `reciprocallength`라는 중립적인 하나의 그룹을 모델링하며, 굴절력은 그중 하나의
해석입니다. 이 페이지는 그 해석을 다룹니다.

!!! note "하나의 그룹, 두 가지 해석"
    `KReciprocalLengthUnitInstance`는 공유 타입이므로, KUnit 입장에서는 굴절력과 파수가 동일한 단위입니다.
    이 그룹은 어느 쪽 해석도 그 이름을 독점하지 않도록 중립적인 이름인 `reciprocallength`를 갖습니다.
    값에 이름을 붙여 구분하시기 바랍니다.

## 명명된 단위

| 단위                  | 기호 |                  토큰 | m⁻¹ 단위 1개당 |
|-----------------------|--------|-----------------------:|--------------:|
| 매 미터      | `1/m`  |     `reciprocalMeters` |           1.0 |
| 디옵터               | `dpt`  |             `dioptres` |           1.0 |
| 매 센티미터 | `1/cm` | `reciprocalCentimeters` |         100.0 |
| 카이저                | `1/cm` |               `kaysers` |         100.0 |

`dioptres`와 `kaysers`는 각각 매 미터와 매 센티미터의 대체 표기이며, 독자적인 단위가 아닙니다. 모든 토큰은
모든 SI 접두사를 사용할 수 있습니다(`milli.dioptres` 등).

## 그룹으로 계산하기

| 표현식                       | 결과 타입                      | 의미                          |
|----------------------------------|-----------------------------------|-----------------------------------|
| `1 / length`                     | `KReciprocalLengthUnitInstance`  | `D = 1 / f`                      |
| `1 / reciprocalLength`           | `KLengthUnitInstance`            | 초점 거리로 되돌아감         |
| `reciprocalLength + …`           | `KReciprocalLengthUnitInstance`  | 밀착된 얇은 렌즈는 굴절력이 더해짐 |
| `reciprocalLength * length`      | `Double`                         | 무차원 계수(`m⁻¹ · m`)  |

네이티브 형식은 `toReciprocalLength()`로 변환합니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val native = (2.5 of ((1 of meters).toUnit() pow -1)).toReciprocalLength()
native into dioptres      // 2.5
```

## 실전 예제 — 돋보기 안경

초점 거리 **40 cm**인 렌즈는 `D = 1 / 0.4 m = 2.5 dpt`가 됩니다. 두 번째로 더 약한 렌즈를 밀착시키면
굴절력이 단순히 더해집니다 — 이는 동일 타입 `+`가 정확히 하는 일입니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val d = 1 / (40 of centi.meters)     // KReciprocalLengthUnitInstance
d into dioptres                       // 2.5

val combined = d + (1.5 of dioptres)  // 밀착된 렌즈
combined into dioptres                // 4.0

val f = 1 / combined                  // KLengthUnitInstance
f into centi.meters                   // 25.0 — 합성 초점 거리
```

## 값 시맨틱

`equals`/`hashCode`는 **정규화된 m⁻¹ 값**을 비교하므로
`(1 of reciprocalCentimeters) == (100 of reciprocalMeters)`입니다. `toString()`은 값을 기본 단위로
표시합니다: `"2.5 1/m"`.

## 관련 항목

* [파수](../mechanics/wavenumber.md) — 분광학적 물리량으로 해석되는 동일한 타입.
* [거리](../kinematics/distance.md) — 이 그룹이 역수를 취하는 대상 그룹.
* [광학 개요](overview.ko.md)
</content>
