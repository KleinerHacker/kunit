# 비전하

패키지: `org.pcsoft.framework.kunit.electric.specificcharge`
기본 단위: **킬로그램당 쿨롬**
(`KSpecificChargeUnit.BASE == KSpecificChargeUnit.COULOMB_PER_KILOGRAM`)

종류: **구성된 단위**

비전하 `q/m` 는 물체가 단위 질량당 지니는 전하입니다. 이는 J. J. 톰슨이 전자를 규명하기 위해 측정한 양이며,
질량분석법이 입자를 분리하는 기준이 되는 양입니다.

정규 기본 차원 표준형은 `current · time · mass⁻¹` 입니다.

!!! note "하나의 그룹, 두 가지 해석"
    같은 차원이 방사선 방호에서의 **전리선량**(피폭량)도 나타내며, 역사적으로 뢴트겐으로 측정되었습니다 —
    [피폭량](../thermodynamics/exposure.ko.md)을 참고하십시오. 단일 표준형은 단일 타입에 대응하므로, 두 해석
    모두 이 그룹을 공유합니다. 뢴트겐은 그 이름 있는 단위 중 하나입니다. 값의 이름을 통해 이들을 구분하십시오.

## 이름 있는 단위

| 단위                 | 기호 |                 토큰 | 1단위의 C/kg 값 |
|----------------------|--------|----------------------:|---------------:|
| 킬로그램당 쿨롬      | `C/kg` | `coulombsPerKilogram` |            1.0 |
| 뢴트겐               | `R`    |            `roentgens` |        2.58e-4 |

모든 토큰은 모든 SI 접두어를 지원합니다(`milli.roentgens` 등).

## 상수

| 상수                         | 값                  | 의미                                     |
|-----------------------------|---------------------|------------------------------------------|
| `ELECTRON_SPECIFIC_CHARGE`  | `1.75882001076e11 C/kg` | 전자의 전하 대 질량 비                     |

부호는 생략됩니다: 전자의 전하는 음수이지만, 이 비율은 크기로 표시됩니다.

## 분해

이 그룹은 하나의 분해를 가지며, 두 형태 모두 값이 같은 같은 타입의 인스턴스를 만듭니다. 네이티브 형태는
**단위 템플릿**으로 조립됩니다. 그룹이 질량 항을 가지기 때문입니다.

| 형태             | 식                                               |
|------------------|----------------------------------------------------------|
| 타입이 지정된 연산자 | `charge / mass`                                         |
| 네이티브 (`toX()`) | `(2 of A · s / kilo.grams).toSpecificCharge()`          |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

val typed = (4 of coulombs) / (2 of kilo.grams)
val native = (2 of amperes.toUnit() * (seconds pow 1) / kilo.grams.toUnit()).toSpecificCharge()

typed == native                   // true
typed into coulombsPerKilogram    // 2.0
```

## 그룹으로 계산하기

| 식                          | 결과 타입                     | 의미              |
|-----------------------------|----------------------------------|----------------------|
| `charge / mass`             | `KSpecificChargeUnitInstance`   | `q/m`                |
| `specificCharge * mass`     | `KChargeUnitInstance`           | 총 전하     |
| `charge / specificCharge`   | `KMassUnitInstance`             | 전하를 지닌 질량    |

## 실제 예 — 전자, 그리고 피폭량 판독값

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

// 톰슨의 비율
val electron = ELECTRON_SPECIFIC_CHARGE of coulombsPerKilogram
electron into coulombsPerKilogram          // ≈ 1.7588e11

// 서베이미터의 피폭량 판독값, 그리고 공기 1 kg 에서 방출되는 전하
val exposure = 1 of roentgens
exposure into coulombsPerKilogram          // 2.58e-4
(exposure * (1 of kilo.grams)) into coulombs   // 2.58e-4
```

## 값 의미론

`equals`/`hashCode` 는 **정규화된 C/kg 값**을 비교하므로,
`(1 of roentgens) == (2.58e-4 of coulombsPerKilogram)` 입니다. `toString()` 은 값을 기본 단위로 표시합니다:
`"1.0 C/kg"`.

## 참고 항목

* [전하](charge.ko.md) 와 [질량](../mechanics/mass.ko.md) — 두 피연산자.
* [피폭량](../thermodynamics/exposure.ko.md) — 전리선량으로 읽은 같은 타입.
* [전기공학 개요](overview.ko.md)
