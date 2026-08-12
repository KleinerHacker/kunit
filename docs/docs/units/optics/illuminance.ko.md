# 조도

패키지: `org.pcsoft.framework.kunit.optic.illuminance`
기본 단위: **럭스**(`KIlluminanceUnit.BASE == KIlluminanceUnit.LUX`)

종류: **구성 단위**

조도 `E`는 **표면에 도달하는** 광속을, 그 표면의 단위 면적당으로 나타낸 것입니다: `E = Φ / A`, 즉
`1 lx = 1 lm/m²`. 이는 모든 작업장 조명 기준이 기술되는 물리량입니다 — 광속과 달리, 램프까지의 거리와
비추어지는 면적의 크기에 따라 달라지며, 램프 자체에만 의존하지 않습니다.

이 물리량의 정준 기본 차원 표준형은 `luminousIntensity¹ · solidAngle¹ · distance⁻²`입니다.

## 단위

| 단위         | 열거값                     | 기호 |         토큰 | 럭스 1단위당 |
|--------------|--------------------------------|--------|--------------:|--------------:|
| 럭스          | `KIlluminanceUnit.LUX`         | `lx`   |         `lux` |           1.0 |
| 포트         | `KIlluminanceUnit.PHOT`        | `ph`   |       `phots` |        10 000 |
| 풋캔들  | `KIlluminanceUnit.FOOT_CANDLE` | `fc`   | `footCandles` |    ≈ 10.76391 |
| 녹스          | `KIlluminanceUnit.NOX`         | `nx`   |         `nox` |         0.001 |

포트는 CGS 단위(1 lm/cm²)이고, 풋캔들은 영국식 단위(1 lm/ft²)이며, 녹스는 달빛과 같은 매우 낮은
조도 수준에 사용됩니다. 모든 토큰은 모든 SI 접두사를 사용할 수 있습니다(`kilo.lux`, `milli.lux` 등).

## 분해

이 그룹에는 하나의 분해가 있으며, 두 형식 모두 동일한 타입이 지정된 값이 같은 인스턴스를 만들어 냅니다.

| 형식             | 표현식                                                             |
|------------------|------------------------------------------------------------------------|
| 타입이 지정된 연산자   | `luminousFlux / area`                                                  |
| 네이티브 형식(`toX()`) | `(cd.toUnit() * sr.toUnit() / (m.toUnit() pow 2)).toIlluminance()`     |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.illuminance.*

val native = (
    (1 of candelas).toUnit() * (1 of steradians).toUnit() / ((1 of meters).toUnit() pow 2)
).toIlluminance()
native into lux          // 1.0
```

## 그룹으로 계산하기

| 표현식                 | 결과 타입                 | 의미                     |
|----------------------------|------------------------------|------------------------------|
| `luminousFlux / area`      | `KIlluminanceUnitInstance`  | `E = Φ / A`                 |
| `illuminance * area`       | `KLuminousFluxUnitInstance` | `Φ = E · A`                 |
| `luminousFlux / illuminance` | `KAreaUnitInstance`       | 특정 광속이 비출 수 있는 면적   |
| `illuminance / solidAngle` | `KLuminanceUnitInstance`    | `L = E / Ω`                 |
| `illuminance * time`       | `KLuminousExposureUnitInstance` | `H = E · t`             |

## 실전 예제 — 내 책상이 충분히 밝은가?

사무 작업에는 대략 **500 lx**가 필요합니다. 800 lm 전구를 2 m² 책상 위에 두면 다음과 같은 조도가 나옵니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.illuminance.*

val desk = (2 of meters) * (1 of meters)     // 2 m²
val e = (800 of lumens) / desk               // KIlluminanceUnitInstance

e into lux                                    // 400.0 — 500 lx 목표에 못 미침
e into footCandles                            // ≈ 37.2

val needed = (500 of lux) * desk              // KLuminousFluxUnitInstance
needed into lumens                            // 목표 달성에 1000.0 lm 필요
```

## 값 시맨틱

`equals`/`hashCode`는 **정규화된 럭스 값**을 비교하므로 `(1 of phots) == (10000 of lux)`입니다.
`toString()`은 값을 기본 단위로 표시합니다: `"500.0 lx"`.

## 관련 항목

* [광속](luminous-flux.ko.md) — 램프가 방출하는 것.
* [휘도](luminance.ko.md) — 입체각당 조도, 표면의 "밝기".
* [노출량](luminous-exposure.ko.md) — 시간에 걸쳐 누적된 조도.
* [광학 개요](overview.ko.md)
</content>
