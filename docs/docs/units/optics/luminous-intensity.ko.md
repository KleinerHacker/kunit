# 광도

패키지: `org.pcsoft.framework.kunit.optic.luminousintensity`
기본 단위: **칸델라**(`KLuminousIntensityUnit.BASE == KLuminousIntensityUnit.CANDELA`)

종류: **네이티브 단위**

광도 `I`는 광원이 특정 방향으로 **입체각당** 방출하는 광속입니다. 이 물리량의 단위인 칸델라는
**일곱 번째 SI 기본 단위**이며 — 인간의 지각을 통해 정의되는 유일한 기본 단위입니다: 1 cd는 그
방향으로 1/683 W/sr의 복사도를 가진 540 THz의 단색 방사를 방출하는 광원의 강도입니다.

이 그룹은 (지수 특화된 하위 타입이 없는) **단순한 1차원** 네이티브 그룹입니다:
`KLuminousIntensityUnitInstance`는 단일 `KLuminousIntensityUnit.CANDELA` 항을 감싸며, 항상
칸델라로 정규화되어 저장됩니다.

## 단위

| 단위            | 열거값                                | 기호   |          토큰 | 칸델라 1단위당 |
|-----------------|-------------------------------------------|----------|---------------:|-------------------:|
| 칸델라         | `KLuminousIntensityUnit.CANDELA`          | `cd`     |     `candelas` |                1.0 |
| 헤프너 촛불   | `KLuminousIntensityUnit.HEFNER_CANDLE`    | `HK`     | `hefnerCandles` |              0.903 |
| 촉광     | `KLuminousIntensityUnit.CANDLEPOWER`      | `cp`     |  `candlepower` |              0.981 |
| 카르셀          | `KLuminousIntensityUnit.CARCEL`           | `carcel` |      `carcels` |               9.74 |

이 세 개의 비SI 항목은 칸델라 이전에 사용되던 역사적인 국가별 표준입니다 — 독일의 헤프너 램프,
영국의 국제 촉광, 프랑스의 카르셀 오일 램프입니다. 이들은 오래된 데이터시트를 그대로 읽을 수 있도록
보존되어 있습니다.

각 토큰은 `of`(생성)와 `into`(읽기)에 사용되는 값이 1인 `KLuminousIntensityUnitInstance`입니다.
모든 토큰은 모든 SI 접두사를 사용할 수 있습니다(`milli.candelas`, `kilo.candelas` 등).

## 그룹으로 계산하기

| 표현식                       | 결과 타입                     | 의미                          |
|----------------------------------|----------------------------------|-----------------------------------|
| `luminousIntensity + …`          | `KLuminousIntensityUnitInstance` | 동일 타입 덧셈               |
| `luminousIntensity * solidAngle` | `KLuminousFluxUnitInstance`     | `Φ = I · Ω`, 방출된 광속    |
| `luminousIntensity / area`       | `KLuminanceUnitInstance`        | `L = I / A`, 표면의 밝기  |
| `luminousFlux / solidAngle`      | `KLuminousIntensityUnitInstance` | 광속으로부터 되돌아감                   |

네이티브 형식은 `toLuminousIntensity()`로 변환합니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.optic.luminousintensity.*

val raw = (1200 of candelas).toUnit()   // KMixedUnitInstance
raw.toLuminousIntensity() into candelas // 1200.0
```

## 실전 예제 — 자동차 전조등

로우빔 전조등의 광축상 규격은 **1200 cd**입니다. 0.05 sr 원뿔각으로 퍼졌을 때, 실제로 도로를 향하는
광속은 다음과 같습니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.*
import org.pcsoft.framework.kunit.optic.luminousflux.*

val i = 1200 of candelas
i into kilo.candelas                     // 1.2

val beam = i * (0.05 of steradians)      // KLuminousFluxUnitInstance
beam into lumens                         // 빔 원뿔 내에서 60.0 lm
```

## 값 시맨틱

`equals`/`hashCode`는 **정규화된 칸델라 값**을 비교하므로 `(1 of candelas) == (1000 of milli.candelas)`입니다.
`toString()`은 값을 기본 단위로 표시합니다: `"1200.0 cd"`.

## 관련 항목

* [광속](luminous-flux.ko.md) — 입체각에 걸쳐 적분된 강도.
* [휘도](luminance.ko.md) — 발광 면적당 강도.
* [복사도](radiant-intensity.ko.md) — 눈에 의한 가중치가 없는, 방사량 체계의 대응 물리량.
* [광학 개요](overview.ko.md)
</content>
