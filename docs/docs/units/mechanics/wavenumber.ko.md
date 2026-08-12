# 파수

패키지: `org.pcsoft.framework.kunit.common.reciprocallength`
기본 단위: **미터의 역수** (`KReciprocalLengthUnit.BASE == KReciprocalLengthUnit.RECIPROCAL_METER`)

유형: **구성 단위**

파동의 파수 `ṽ`는 파장의 역수입니다: `ṽ = 1 / λ` — 단위 길이당 파동 주기의 수입니다. 분광학에서는
파장 대신 파수를 사용하는데, 이는 광자 에너지에 비례하기 때문이며, 거의 항상 **매 센티미터의 역수**
(`cm⁻¹`, 역사적으로 *카이저*라 불림)로 표기됩니다: 가시광선은 대략 14,000–25,000 cm⁻¹, 적외선
지문 영역은 400–1500 cm⁻¹입니다.

그 차원은 `distance⁻¹`로, 렌즈의 굴절력인 [디옵터](../optics/dioptre.ko.md)와 **동일**합니다.
KUnit은 두 가지 읽기 방식 모두에 대해 하나의 중립적인 그룹 `reciprocallength`를 모델링하며,
파수는 그중 하나입니다. 이 페이지는 그 읽기 방식을 문서화합니다.

!!! note "하나의 그룹, 두 가지 읽기 방식"
    `KReciprocalLengthUnitInstance`는 공유되는 타입이므로, KUnit 입장에서는 파수와 굴절력이 동일한
    단위입니다. 이 그룹은 중립적인 이름 `reciprocallength`를 사용하여 어느 쪽 읽기 방식도 그 이름을
    독점하지 않도록 합니다. 값에 이름을 붙여서 이들을 구분하십시오.

## 명명된 단위

| 단위                    | 기호 |                   토큰 | 1 단위 (m⁻¹ 기준) |
|-----------------------|--------|------------------------:|--------------:|
| 미터의 역수               | `1/m`  |      `reciprocalMeters` |           1.0 |
| 센티미터의 역수            | `1/cm` | `reciprocalCentimeters` |         100.0 |
| 카이저                    | `1/cm` |                `kaysers` |         100.0 |
| 디옵터                    | `dpt`  |               `dioptres` |           1.0 |

모든 토큰은 모든 SI 접두사를 받아들입니다 (`kilo.reciprocalCentimeters` 등).

## 그룹으로 계산하기

| 표현식                      | 결과 타입                           | 의미                             |
|-----------------------------|-----------------------------------------|-------------------------------------|
| `1 / length`                | `KReciprocalLengthUnitInstance`        | `ṽ = 1 / λ`                         |
| `1 / reciprocalLength`      | `KLengthUnitInstance`                  | 파장으로 되돌아감                    |
| `reciprocalLength * length` | `Double`                               | 무차원 주기 수                       |
| `reciprocalLength + …`      | `KReciprocalLengthUnitInstance`        | 동일 타입 간 덧셈                    |

원시 형태는 `toReciprocalLength()`로 변환합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val native = (100 of ((1 of meters).toUnit() pow -1)).toReciprocalLength()
native into reciprocalCentimeters      // 1.0
```

## 실제 사례 — 녹색 레이저 광선

500 nm 레이저 광선은 파수 20,000 cm⁻¹로 변환되며, 1 mm 경로에 들어맞는 주기 수는 이로부터
직접 도출됩니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val k = 1 / (500 of nano.meters)       // KReciprocalLengthUnitInstance
k into reciprocalCentimeters            // 20_000.0
k into kaysers                          // 20_000.0 (동일 단위, 고전적 명칭)

val cycles = k * (1 of milli.meters)    // Double
cycles                                   // 2000.0 — 밀리미터당 파동 주기 수

val lambda = 1 / k                       // KLengthUnitInstance
lambda into nano.meters                  // 500.0
```

## 값 의미론

`equals`/`hashCode`는 **정규화된 m⁻¹ 값**을 비교하므로
`(1 of reciprocalCentimeters) == (100 of reciprocalMeters)`입니다. `toString()`은 기본 단위로
값을 표현합니다: `"2000000.0 1/m"`.

## 참고

* [디옵터](../optics/dioptre.ko.md) — 굴절력으로 읽히는 동일한 타입.
* [주파수](../kinematics/frequency.ko.md) — 시간의 역수로, 이 그룹의 시간적 유사체.
* [역학 개요](overview.ko.md)
