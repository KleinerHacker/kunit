# 광학 — 개요

패키지: `org.pcsoft.framework.kunit.optic.luminousintensity`, `…luminousflux`, `…illuminance`,
`…luminance`, `…luminousenergy`, `…luminousexposure`, `…efficacy`, `…radiantintensity`, `…radiance`,
그리고 `org.pcsoft.framework.kunit.common.reciprocallength`

광학은 **빛**에 대한 기술입니다 — 광원이 얼마나 많은 빛을 방출하는지, 그 빛이 얼마나 표면에 도달하는지, 그리고
전력이 얼마나 효율적으로 빛으로 변환되는지를 다룹니다. 이 분야는 **칸델라**를 기반으로 하며, 이는 일곱 번째이자
마지막 SI 기본 단위로서 유일하게 인간의 지각을 기준으로 정의된 기본 단위입니다: 방사 전력을 눈의 감도로
가중치를 부여합니다.

이 때문에 이 분야는 두 가지 병렬적인 체계를 가집니다. **측광량**(칸델라, 루멘, 럭스, 니트)은 빛을 *눈에 보이는 대로*
기술하고, **방사량**(와트 매 스테라디안, 와트 매 스테라디안 제곱미터)은 눈의 가중치 없이, 같은 방사를
*검출기가 측정하는 대로* 기술합니다. 이 둘을 잇는 다리가 [발광 효율](luminous-efficacy.ko.md)이며, 683 lm/W를
상한으로 합니다.

## 이 주제에 포함된 단위

| 단위               | 종류        | 기본 단위                              | 페이지                                     |
|--------------------|-------------|----------------------------------------|--------------------------------------------|
| 광도               | 네이티브    | 칸델라 (`cd`)                         | [광도](luminous-intensity.ko.md) |
| 광속               | 구성 단위   | 루멘 (`lm`)                           | [광속](luminous-flux.ko.md)        |
| 조도               | 구성 단위   | 럭스 (`lx`)                             | [조도](illuminance.ko.md)            |
| 휘도               | 구성 단위   | 칸델라 매 제곱미터 (`cd/m²`)     | [휘도](luminance.ko.md)                |
| 광량 (발광 에너지) | 구성 단위   | 루멘 초 (`lm·s`)                  | [광량](luminous-energy.ko.md)    |
| 노출량 (광노출량)  | 구성 단위   | 럭스 초 (`lx·s`)                    | [노출량](luminous-exposure.ko.md) |
| 발광 효율          | 구성 단위   | 루멘 매 와트 (`lm/W`)                | [발광 효율](luminous-efficacy.ko.md) |
| 복사도             | 구성 단위   | 와트 매 스테라디안 (`W/sr`)            | [복사도](radiant-intensity.ko.md) |
| 복사휘도           | 구성 단위   | 와트 매 스테라디안 제곱미터 (`W/(sr·m²)`)    | [복사휘도](radiance.ko.md)                  |
| 굴절력             | 구성 단위   | 디옵터 (`dpt` = `m⁻¹`)                | [디옵터](dioptre.ko.md)                    |

강도량과 광속량을 연결하는 입체각은 이 분야에 **속하지 않습니다** — 이는 [역학](../mechanics/solid-angle.md)
주제에 속하며 여기서는 그대로 재사용됩니다.

## 각 물리량의 관계

아래의 각 관계는 항상 올바르게 **타입이 지정된** 물리량을 반환합니다. 원시 혼합 단위를 직접 조립할 필요는 없습니다.

| 표현식                     | 결과             | 공식        |
|--------------------------------|--------------------|----------------|
| `luminousIntensity * solidAngle` | 광속    | `Φ = I · Ω`    |
| `luminousFlux / area`          | 조도        | `E = Φ / A`    |
| `luminousIntensity / area`     | 휘도          | `L = I / A`    |
| `illuminance / solidAngle`     | 휘도          | `L = E / Ω`    |
| `luminousFlux * time`          | 광량    | `Q = Φ · t`    |
| `illuminance * time`           | 노출량  | `H = E · t`    |
| `luminousFlux / power`         | 발광 효율  | `η = Φ / P`    |
| `power / solidAngle`           | 복사도  | `Iₑ = P / Ω`   |
| `radiantIntensity / area`      | 복사휘도           | `Lₑ = Iₑ / A`  |
| `1 / length`                   | 굴절력   | `D = 1 / f`    |

## 실전 예제 — 이 전구가 내 책상에 충분히 밝은가?

LED 전구의 정격은 **800 lm**, 소비 전력은 **7 W**입니다. **2 m²**의 책상 위에 걸려 있습니다. 사무 작업에는
약 500 lx가 필요합니다. 충분할까요? 이 전구는 얼마나 효율적일까요?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.luminousflux.*
import org.pcsoft.framework.kunit.optic.illuminance.*
import org.pcsoft.framework.kunit.optic.efficacy.*

val flux = 800 of lumens
val desk = (2 of meters) * (1 of meters)          // KAreaUnitInstance, 2 m²

val e = flux / desk                                // KIlluminanceUnitInstance
e into lux                                         // 400.0 — 500 lx 목표에 약간 못 미침

val eta = flux / (7 of watts)                      // KLuminousEfficacyUnitInstance
eta into lumensPerWatt                             // ≈ 114.3
eta.value / MAX_LUMINOUS_EFFICACY                  // ≈ 0.167 — 물리적 상한의 17%
```

## 실전 예제 — 돋보기 안경

초점 거리 **40 cm**인 렌즈의 굴절력은 `D = 1 / f`입니다. 밀착된 두 개의 얇은 렌즈는 각각의 굴절력을 단순히
합산합니다. 이는 타입이 지정된 물리량에 대한 `+` 연산이 정확히 하는 일입니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val d = 1 / (40 of centi.meters)   // KReciprocalLengthUnitInstance
d into dioptres                     // 2.5

val combined = d + (1.5 of dioptres) // 밀착된 렌즈는 굴절력이 더해짐
combined into dioptres               // 4.0
1 / combined into meters             // 0.25 — 합성 초점 거리
```

## 표기법

아래 표는 이 분야의 핵심 관계를 수학적 표기법과 KUnit을 사용한 Kotlin 표기법으로 비교하여 보여줍니다.
지수는 유니코드 위첨자(`²`, `⁻¹`)를 사용하고, `·`는 곱셈을, `/`는 분수를 나타냅니다.

| 수학 표기   | Kotlin                                    | 의미                             |
|---------------|---------------------------------------------|--------------------------------------|
| `Φ = I · Ω`   | `(100 of candelas) * (2 of steradians)`   | 강도×원뿔각으로부터 얻는 광속 |
| `E = Φ / A`   | `(800 of lumens) / desk`                  | 광속÷면적으로부터 얻는 조도        |
| `L = I / A`   | `(250 of candelas) / screen`              | 강도÷면적으로부터 얻는 휘도     |
| `Q = Φ · t`   | `(800 of lumens) * (2 of hours)`          | 광속×시간으로부터 얻는 광량    |
| `H = E · t`   | `(50 of lux) * (8 of hours)`              | 조도×시간으로부터 얻는 광량(노출)  |
| `η = Φ / P`   | `(800 of lumens) / (7 of watts)`          | 발광 효율                   |
| `Iₑ = P / Ω`  | `(20 of watts) / (4 of steradians)`       | 복사도                   |
| `D = 1 / f`   | `1 / (40 of centi.meters)`                | 초점 거리로부터 얻는 굴절력  |

## 다음으로 볼 페이지

* [광도](luminous-intensity.ko.md) — 칸델라, 이 분야의 네이티브 기본 물리량.
* [광속](luminous-flux.ko.md)과 [조도](illuminance.ko.md) — 램프가 방출하는 것과 표면이 받는 것.
* [휘도](luminance.ko.md) — 디스플레이의 "니트" 등급이 가리키는 물리량.
* [발광 효율](luminous-efficacy.ko.md) — 측광량 체계와 방사량 체계 사이의 다리.
* [디옵터](dioptre.ko.md) — 굴절력, 그리고 분광학에서의 쌍둥이 물리량인 [파수](../mechanics/wavenumber.md).
</content>
