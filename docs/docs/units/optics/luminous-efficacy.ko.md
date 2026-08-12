# 발광 효율

패키지: `org.pcsoft.framework.kunit.optic.efficacy`
기본 단위: **루멘 매 와트**(`KLuminousEfficacyUnit.BASE == KLuminousEfficacyUnit.LUMEN_PER_WATT`)

종류: **구성 단위**

발광 효율 `η`는 램프가 **전력 1와트당** 생성하는 광속입니다: `η = Φ / P`. 이는 광원의 우수성을
나타내는 단일 수치이며, 측광량 체계와 방사량 체계 사이를 잇는 다리이기도 합니다: 검출기가 측정하는
와트를 눈이 지각하는 루멘으로 변환합니다.

이 물리량의 정준 기본 차원 표준형은 `luminousIntensity¹ · solidAngle¹ · mass⁻¹ · distance⁻² · time³`입니다.

## 단위

| 단위           | 열거값                              | 기호 |           토큰 | lm/W 1단위당 |
|----------------|-----------------------------------------|--------|----------------:|---------------:|
| 루멘 매 와트 | `KLuminousEfficacyUnit.LUMEN_PER_WATT`  | `lm/W` | `lumensPerWatt` |            1.0 |

이 토큰은 모든 SI 접두사를 사용할 수 있습니다(`milli.lumensPerWatt`, `kilo.lumensPerWatt` 등).

## 상수

| 상수                | 값       | 의미                                                       |
|-------------------------|-------------|-----------------------------------------------------------------|
| `MAX_LUMINOUS_EFFICACY` | `683 lm/W`  | SI 칸델라 정의로부터 나온, 555 nm에서의 물리적 상한 |

어떤 광원도 683 lm/W를 초과할 수 없습니다. 이는 명소시 광시감도 함수의 정점에서 단색 녹색광의
효율이기 때문입니다. 모든 실제 램프는 이 값의 일부에 불과합니다.

## 분해

이 그룹에는 하나의 분해가 있으며, 두 형식 모두 동일한 타입이 지정된 값이 같은 인스턴스를 만들어
냅니다. 네이티브 형식은 **단위 템플릿**으로부터 조립된다는 점에 유의하십시오: 질량 항을 가진 그룹의
경우 원시 혼합 값은 그램 기반의 곱이지만, 타입이 지정된 인스턴스는 이름 붙여진 단위로 그 값을 저장합니다.

| 형식             | 표현식                                                                       |
|------------------|-------------------------------------------------------------------------------------|
| 타입이 지정된 연산자   | `luminousFlux / power`                                                            |
| 네이티브 형식(`toX()`) | `(120 of (cd·sr) / (kilo.grams · m² / s³)).toLuminousEfficacy()`                  |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.efficacy.*

val typed = (1200 of lumens) / (10 of watts)
val native = (
    120 of (candelas.toUnit() * steradians.toUnit()) /
        (kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3))
).toLuminousEfficacy()

typed == native              // true
typed into lumensPerWatt     // 120.0
```

## 그룹으로 계산하기

| 표현식                          | 결과 타입                     | 의미                |
|--------------------------------------|----------------------------------|-------------------------|
| `luminousFlux / power`              | `KLuminousEfficacyUnitInstance` | `η = Φ / P`            |
| `luminousEfficacy * power`          | `KLuminousFluxUnitInstance`     | `Φ = η · P`            |
| `luminousFlux / luminousEfficacy`   | `KPowerUnitInstance`            | 필요한 전력     |

## 실전 예제 — 세 가지 전구 비교

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.efficacy.*

val incandescent = (800 of lumens) / (60 of watts)
val halogen      = (800 of lumens) / (42 of watts)
val led          = (800 of lumens) / (7 of watts)

incandescent into lumensPerWatt      // ≈ 13.3
halogen into lumensPerWatt           // ≈ 19.0
led into lumensPerWatt               // ≈ 114.3

led.value / MAX_LUMINOUS_EFFICACY    // ≈ 0.167 — 물리적 상한의 17%

// LED 스트립이 3000 lm을 내는 데 필요한 전력은?
val p = (3000 of lumens) / led       // KPowerUnitInstance
p into watts                          // 26.25
```

## 값 시맨틱

`equals`/`hashCode`는 **정규화된 lm/W 값**을 비교하므로
`(1 of lumensPerWatt) == (1000 of milli.lumensPerWatt)`입니다. `toString()`은 값을 기본 단위로
표시합니다: `"120.0 lm/W"`.

## 관련 항목

* [광속](luminous-flux.ko.md) — 분자.
* [복사도](radiant-intensity.ko.md)와 [복사휘도](radiance.ko.md) — 다리의 방사량 쪽.
* [전력(전기)](../electrical/power.md) — 분모.
* [광학 개요](overview.ko.md)
</content>
