# 복사휘도

패키지: `org.pcsoft.framework.kunit.optic.radiance`
기본 단위: **와트 매 스테라디안 제곱미터**
(`KRadianceUnit.BASE == KRadianceUnit.WATT_PER_STERADIAN_SQUARE_METER`)

종류: **구성 단위**

복사휘도 `Lₑ`는 **발광 면적당** 복사도입니다: `Lₑ = Iₑ / A`. 이는 [휘도](luminance.ko.md)의
**방사량** 체계 대응 물리량이며, 원격 탐사와 열화상 촬영에서 다루는 물리량입니다 — 카메라 픽셀이
실제로 적분하는 값으로, 표면까지의 거리와 무관합니다.

이 물리량의 정준 기본 차원 표준형은 `mass¹ · time⁻³ · solidAngle⁻¹`입니다. 두 개의 길이 지수는
서로 상쇄됩니다: 와트는 `distance²`를, 면적은 `distance⁻²`를 각각 기여하기 때문입니다.

## 단위

| 단위                            | 열거값                                    | 기호       |                            토큰 | W/(sr·m²) 1단위당 |
|---------------------------------|-----------------------------------------------|--------------|---------------------------------:|--------------------:|
| 와트 매 스테라디안 제곱미터 | `KRadianceUnit.WATT_PER_STERADIAN_SQUARE_METER` | `W/(sr*m^2)` | `wattsPerSteradianSquareMeter`   |                 1.0 |

이 토큰은 모든 SI 접두사를 사용할 수 있습니다(`milli.wattsPerSteradianSquareMeter` 등).

## 분해

이 그룹에는 하나의 분해가 있으며, 두 형식 모두 동일한 타입이 지정된 값이 같은 인스턴스를 만들어
냅니다. 이 그룹은 질량 항을 가지므로 네이티브 형식은 **단위 템플릿**으로부터 조립됩니다.

| 형식             | 표현식                                                    |
|------------------|-----------------------------------------------------------------|
| 타입이 지정된 연산자   | `radiantIntensity / area`                                     |
| 네이티브 형식(`toX()`) | `(5 of kilo.grams / s³ / sr).toRadiance()`                    |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.radiantintensity.wattsPerSteradian
import org.pcsoft.framework.kunit.optic.radiance.*

val typed = (10 of wattsPerSteradian) / ((2 of meters) * (1 of meters))
val native = (5 of kilo.grams.toUnit() / (seconds pow 3) / steradians.toUnit()).toRadiance()

typed == native                              // true
typed into wattsPerSteradianSquareMeter      // 5.0
```

## 그룹으로 계산하기

| 표현식                        | 결과 타입                     | 의미         |
|-----------------------------------|---------------------------------|-----------------|
| `radiantIntensity / area`         | `KRadianceUnitInstance`         | `Lₑ = Iₑ / A`   |
| `radiance * area`                 | `KRadiantIntensityUnitInstance` | `Iₑ = Lₑ · A`   |
| `radiantIntensity / radiance`     | `KAreaUnitInstance`             | 발광 면적 |

## 실전 예제 — 열화상 카메라 픽셀

**2 m²**의 노벽이 카메라를 향해 **10 W/sr**를 방사합니다. 그 복사휘도 — 거리와 무관하게 카메라가
보고하는 값 — 는 다음과 같습니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.radiantintensity.wattsPerSteradian
import org.pcsoft.framework.kunit.optic.radiance.*

val wall = (2 of meters) * (1 of meters)
val l = (10 of wattsPerSteradian) / wall
l into wattsPerSteradianSquareMeter      // 5.0

// 같은 벽에서 0.5 m² 부분은 비례적으로 더 적은 강도를 방출한다 …
val patch = (0.5 of meters) * (1 of meters)
(l * patch) into wattsPerSteradian       // 2.5 — 그러나 복사휘도는 변하지 않는다
```

## 값 시맨틱

`equals`/`hashCode`는 **정규화된 W/(sr·m²) 값**을 비교하므로
`(1 of wattsPerSteradianSquareMeter) == (1000 of milli.wattsPerSteradianSquareMeter)`입니다.
`toString()`은 값을 기본 단위로 표시합니다: `"5.0 W/(sr*m^2)"`.

## 관련 항목

* [복사도](radiant-intensity.ko.md) — 분자.
* [휘도](luminance.ko.md) — 측광량 체계의 대응 물리량.
* [열유속 밀도](../thermodynamics/heat-flux-density.md) — 반구에 걸쳐 적분된 복사휘도.
* [광학 개요](overview.ko.md)
</content>
