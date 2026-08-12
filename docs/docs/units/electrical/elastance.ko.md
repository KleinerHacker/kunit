# 일레스턴스(전기 탄성)

패키지: `org.pcsoft.framework.kunit.electric.elastance`
기본 단위: **역패럿**(`KElastanceUnit.BASE == KElastanceUnit.RECIPROCAL_FARAD`)

종류: **구성된 단위**

일레스턴스 `S = U / Q = 1 / C` 는 [정전용량](capacitance.ko.md)의 정확한 역수입니다. 커패시터가 **직렬**로 연결될 때 편리한 형태로,
직렬 일레스턴스는 직렬 저항과 마찬가지로 단순히 더해집니다. 이 단위인 역패럿은 고전적으로 **daraf**("farad"를 거꾸로 쓴 것)라고 불립니다.

정규 기본 차원 표준형은 `mass · length² · time⁻⁴ · current⁻²` 입니다.

## 이름 있는 단위

| 단위              | 기호    |              토큰 | 1단위의 F⁻¹ 값 |
|-------------------|---------|-------------------:|--------------:|
| 역패럿            | `1/F`   | `reciprocalFarads` |           1.0 |
| daraf             | `daraf` |            `darafs` |           1.0 |

`darafs` 는 기본 단위의 또 다른 표기이며 독자적인 단위가 아닙니다. 모든 토큰은 모든 SI 접두어를 지원합니다
(`mega.reciprocalFarads` 등).

## 분해

이 그룹은 하나의 분해를 가지며, 두 형태 모두 값이 같은 같은 타입의 인스턴스를 만듭니다. 네이티브 형태는
**단위 템플릿**으로 조립됩니다. 그룹이 질량 항을 가지기 때문입니다.

| 형태             | 식                                                    |
|------------------|----------------------------------------------------------------|
| 타입이 지정된 연산자 | `voltage / charge`                                            |
| 네이티브 (`toX()`) | `(1 of kilo.grams · m² / s⁴ / A²).toElastance()`              |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.elastance.*

val typed = (10 of volts) / (10 of milli.coulombs)
val native = (1000 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 4) / (amperes.toUnit() pow 2))
    .toElastance()

typed == native              // true
typed into reciprocalFarads  // 1000.0
```

## 그룹으로 계산하기

| 식                     | 결과 타입                     | 의미                    |
|------------------------|---------------------------------|----------------------------|
| `voltage / charge`     | `KElastanceUnitInstance`        | `S = U / Q`                |
| `elastance * charge`   | `KVoltageUnitInstance`          | `U = S · Q`                |
| `voltage / elastance`  | `KChargeUnitInstance`           | 저장된 전하             |
| `1 / capacitance`      | `KElastanceUnitInstance`        | `S = 1 / C`                |
| `1 / elastance`        | `KCapacitanceUnitInstance`      | `C = 1 / S`                |
| `elastance + …`        | `KElastanceUnitInstance`        | 직렬 커패시터            |

## 실제 예 — 직렬 커패시터 두 개

1 mF 커패시터 두 개를 직렬로 연결하면 단일 0.5 mF 커패시터처럼 동작합니다. 일레스턴스 관점에서는 단순한 덧셈입니다:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.capacitance.farads
import org.pcsoft.framework.kunit.electric.elastance.*

val total = (1 / (1 of milli.farads)) + (1 / (1 of milli.farads))
total into reciprocalFarads       // 2000.0

(1 / total) into milli.farads     // 0.5 — 등가 정전용량
```

## 값 의미론

`equals`/`hashCode` 는 **정규화된 F⁻¹ 값**을 비교하므로, `(1 of reciprocalFarads) == (1 of darafs)` 입니다.
`toString()` 은 값을 기본 단위로 표시합니다: `"1000.0 1/F"`.

## 참고 항목

* [정전용량](capacitance.ko.md) — 역수 관계의 양.
* [전압](voltage.ko.md) 과 [전하](charge.ko.md) — 분해의 두 피연산자.
* [전기공학 개요](overview.ko.md)
