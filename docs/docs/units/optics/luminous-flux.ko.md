# 광속

패키지: `org.pcsoft.framework.kunit.optic.luminousflux`
기본 단위: **루멘**(`KLuminousFluxUnit.BASE == KLuminousFluxUnit.LUMEN`)

종류: **구성 단위**

광속 `Φ`는 광원이 그것이 뒤덮는 모든 방향으로 방출하는 **가시광선의 총량**입니다 — 모든 램프 포장지에
인쇄된 수치입니다. 이는 광도를 입체각에 걸쳐 적분한 것입니다: `Φ = I · Ω`, 즉 `1 lm = 1 cd·sr`.

이 물리량의 정준 기본 차원 표준형은 `luminousIntensity¹ · solidAngle¹`입니다.

## 단위

| 단위               | 열거값                            | 기호  |               토큰 | 루멘 1단위당 |
|--------------------|---------------------------------------|---------|--------------------:|-----------------:|
| 루멘              | `KLuminousFluxUnit.LUMEN`             | `lm`    |            `lumens` |              1.0 |
| 칸델라 스테라디안  | `KLuminousFluxUnit.CANDELA_STERADIAN` | `cd·sr` | `candelaSteradians` |              1.0 |

`candelaSteradians`는 루멘의 정의를 풀어 쓴 것으로 — 수치상으로는 동일하지만, 단위가 어디서
비롯되었는지를 수식에서 명시적으로 드러낼 수 있습니다. 두 토큰 모두 모든 SI 접두사를 사용할 수
있습니다(`kilo.lumens`, `milli.lumens` 등).

## 분해

이 그룹에는 하나의 분해가 있으며, 두 형식 모두 동일한 타입이 지정된 값이 같은 인스턴스를 만들어
냅니다.

| 형식                | 표현식                                                       |
|---------------------|--------------------------------------------------------------------|
| 타입이 지정된 연산자      | `luminousIntensity * solidAngle`                                  |
| 네이티브 형식(`toX()`)    | `((100 of candelas).toUnit() * (2 of steradians).toUnit()).toLuminousFlux()` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminousflux.*

val typed = (100 of candelas) * (2 of steradians)
val native = ((100 of candelas).toUnit() * (2 of steradians).toUnit()).toLuminousFlux()

typed == native          // true
typed into lumens        // 200.0
```

## 그룹으로 계산하기

| 표현식                       | 결과 타입                      | 의미                       |
|----------------------------------|-----------------------------------|--------------------------------|
| `luminousIntensity * solidAngle` | `KLuminousFluxUnitInstance`      | `Φ = I · Ω`                   |
| `luminousFlux / solidAngle`      | `KLuminousIntensityUnitInstance` | `I = Φ / Ω`                   |
| `luminousFlux / luminousIntensity` | `KSolidAngleUnitInstance`      | 광속이 퍼지는 원뿔각 |
| `luminousFlux / area`            | `KIlluminanceUnitInstance`       | `E = Φ / A`                   |
| `luminousFlux * time`            | `KLuminousEnergyUnitInstance`    | `Q = Φ · t`                   |
| `luminousFlux / power`           | `KLuminousEfficacyUnitInstance`  | `η = Φ / P`                   |

## 실전 예제 — 등방성 전구

노출된 전구는 모든 방향으로 균등하게 방사합니다. 전체 구는 `4π sr`이므로, 100 cd 광원이 방출하는
광속은 다음과 같습니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminousflux.*

val phi = (100 of candelas) * ((4 * Math.PI) of steradians)
phi into lumens          // ≈ 1256.6 lm — 대략 100 W 백열전구에 해당
```

## 값 시맨틱

`equals`/`hashCode`는 **정규화된 루멘 값**을 비교하므로 `(1 of lumens) == (1000 of milli.lumens)`입니다.
`toString()`은 값을 기본 단위로 표시합니다: `"800.0 lm"`.

## 관련 항목

* [광도](luminous-intensity.ko.md) — 입체각당 광속.
* [조도](illuminance.ko.md) — 조명이 비추는 면적당 광속.
* [발광 효율](luminous-efficacy.ko.md) — 전력 1와트당 광속.
* [광학 개요](overview.ko.md)
</content>
