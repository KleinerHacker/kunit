# 자기 쌍극자 모멘트

패키지: `org.pcsoft.framework.kunit.electric.magneticmoment`
기본 단위: **암페어 제곱미터**
(`KMagneticMomentUnit.BASE == KMagneticMomentUnit.AMPERE_SQUARE_METER`)

종류: **구성된 단위**

전류 루프의 자기 쌍극자 모멘트 `m` 은 전류에 그것이 감싸는 면적을 곱한 것입니다: `m = I · A`.
이는 자기장이 루프에 가하는 토크를 결정하는 양이며, 원자 및 핵 자성(보어 마그네톤과 핵 마그네톤)이 표현되는 양입니다.

정규 기본 차원 표준형은 `current · length²` 입니다.

## 이름 있는 단위

| 단위                | 기호    |                토큰 |     1단위의 A·m² 값 |
|---------------------|---------|---------------------:|-------------------:|
| 암페어 제곱미터     | `A*m^2` | `ampereSquareMeters` |                1.0 |
| 줄 매 테슬라        | `J/T`   |      `joulesPerTesla` |                1.0 |
| 보어 마그네톤       | `μB`    |       `bohrMagnetons` | 9.2740100783e-24   |
| 핵 마그네톤         | `μN`    |    `nuclearMagnetons` | 5.0507837461e-27   |

`joulesPerTesla` 는 같은 단위의 에너지 기반 표기입니다 — 쌍극자가 단위 자속밀도당 얻는 에너지입니다.
모든 토큰은 모든 SI 접두어를 지원합니다.

## 분해

이 그룹은 하나의 분해를 가지며, 두 형태 모두 값이 같은 같은 타입의 인스턴스를 만듭니다:

| 형태             | 식                                                       |
|------------------|-------------------------------------------------------------------|
| 타입이 지정된 연산자 | `current * area`                                                 |
| 네이티브 (`toX()`) | `((2 of amperes).toUnit() * loop.toUnit()).toMagneticMoment()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.magneticmoment.*

val loop = (0.1 of meters) * (0.05 of meters)      // 0.005 m²

val typed = (2 of amperes) * loop
val native = ((2 of amperes).toUnit() * loop.toUnit()).toMagneticMoment()

typed == native                 // true
typed into ampereSquareMeters   // 0.01
```

## 그룹으로 계산하기

| 식                          | 결과 타입                      | 의미          |
|-----------------------------|-----------------------------------|------------------|
| `current * area`            | `KMagneticMomentUnitInstance`    | `m = I · A`      |
| `magneticMoment / area`     | `KElectricCurrentUnitInstance`   | 루프 전류 |
| `magneticMoment / current`  | `KAreaUnitInstance`              | 루프 면적    |

## 실제 예 — 코일 루프와 원자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.magneticmoment.*

val loop = (0.1 of meters) * (0.05 of meters)
val m = (2 of amperes) * loop
m into ampereSquareMeters          // 0.01

// 이는 몇 보어 마그네톤에 해당할까?
m into bohrMagnetons                // ≈ 1.078e21

// 반대로: 1 cm² 루프가 1 A·m² 를 얻으려면 얼마의 전류가 필요할까?
val small = (0.01 of meters) * (0.01 of meters)
((1 of ampereSquareMeters) / small) into amperes   // 10 000 A
```

## 값 의미론

`equals`/`hashCode` 는 **정규화된 A·m² 값**을 비교하므로,
`(1 of ampereSquareMeters) == (1 of joulesPerTesla)` 입니다. `toString()` 은 값을 기본 단위로 표시합니다:
`"0.01 A*m^2"`.

## 참고 항목

* [자속밀도](magneticfluxdensity.ko.md) — 이 모멘트가 상호작용하는 장.
* [전류](ec.ko.md) 와 [거리](../kinematics/distance.ko.md) — 두 인자.
* [전기공학 개요](overview.ko.md)
