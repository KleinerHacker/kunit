# 전기 선속

패키지: `org.pcsoft.framework.kunit.electric.flux`
기본 단위: **볼트미터**(`KElectricFluxUnit.BASE == KElectricFluxUnit.VOLT_METER`)

종류: **구성된 단위**

전기 선속 `Φ_E` 는 전기장 세기를 면적에 대해 적분한 것입니다: `Φ_E = E · A`. 이는 가우스 법칙이 표현하는 양으로 —
닫힌 표면을 통과하는 선속은 그 안에 포함된 전하를 유전율로 나눈 값과 같습니다.

정규 기본 차원 표준형은 `mass · length³ · time⁻³ · current⁻¹` 입니다.

!!! note "전속밀도가 아닙니다"
    [전속밀도](electricfluxdensity.ko.md) `D`(`C/m²`)는 차원이 다른 별개의 양입니다. 이 페이지는 선속 자체,
    즉 `V·m` 에 관한 것입니다.

## 이름 있는 단위

| 단위            | 기호    |             토큰 | 1단위의 V·m 값 |
|-----------------|---------|------------------:|--------------:|
| 볼트미터        | `V*m`   |      `voltMeters` |           1.0 |
| 볼트센티미터    | `V*cm`  | `voltCentimeters` |          0.01 |

모든 토큰은 모든 SI 접두어를 지원합니다(`kilo.voltMeters` 등).

## 분해

이 그룹은 하나의 분해를 가지며, 두 형태 모두 값이 같은 같은 타입의 인스턴스를 만듭니다. 네이티브 형태는
**단위 템플릿**으로 조립됩니다. 그룹이 질량 항을 가지기 때문입니다: 원시 혼합값은 그램 기반 곱이고,
타입이 지정된 인스턴스는 이름 있는 단위로 값을 저장합니다.

| 형태             | 식                                                     |
|------------------|-----------------------------------------------------------------|
| 타입이 지정된 연산자 | `electricFieldStrength * area`                                 |
| 네이티브 (`toX()`) | `(125 of kilo.grams · m³ / s³ / A).toElectricFlux()`           |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.flux.*

val plate = (0.5 of meters) * (0.25 of meters)     // 0.125 m²

val typed = (1000 of voltsPerMeter) * plate
val native = (125 of kilo.grams.toUnit() * (meters pow 3) / (seconds pow 3) / amperes.toUnit())
    .toElectricFlux()

typed == native          // true
typed into voltMeters    // 125.0
```

## 그룹으로 계산하기

| 식                                 | 결과 타입                            | 의미        |
|------------------------------------|----------------------------------------|----------------|
| `electricFieldStrength * area`     | `KElectricFluxUnitInstance`            | `Φ_E = E · A`  |
| `electricFlux / area`              | `KElectricFieldStrengthUnitInstance`   | `E = Φ_E / A`  |
| `electricFlux / electricFieldStrength` | `KAreaUnitInstance`                | 면적       |

## 실제 예 — 커패시터 극판을 통과하는 전기 선속

**1000 V/m** 의 전기장이 0.5 m × 0.25 m 극판을 통과합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.flux.*

val plate = (0.5 of meters) * (0.25 of meters)
val phi = (1000 of voltsPerMeter) * plate
phi into voltMeters                 // 125.0

// 주어진 선속이 그 극판에 함의하는 전기장
((125 of voltMeters) / plate) into voltsPerMeter   // 1000.0
```

## 값 의미론

`equals`/`hashCode` 는 **정규화된 V·m 값**을 비교하므로, `(1 of voltMeters) == (100 of voltCentimeters)` 입니다.
`toString()` 은 값을 기본 단위로 표시합니다: `"125.0 V*m"`.

## 참고 항목

* [전기장 세기](electricfieldstrength.ko.md) — 적분되는 전기장.
* [전속밀도](electricfluxdensity.ko.md) — 차원이 다른 `D` 필드.
* [전기공학 개요](overview.ko.md)
