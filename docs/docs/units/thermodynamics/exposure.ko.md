# 조사선량（전리 선량）

패키지: `org.pcsoft.framework.kunit.electric.specificcharge`
기본 단위: **쿨롱 매 킬로그램**
(`KSpecificChargeUnit.BASE == KSpecificChargeUnit.COULOMB_PER_KILOGRAM`)

유형: **구성 단위（constructed unit）**

조사선량 `X` — 고전적인 **전리 선량** — 은 단위 공기 질량당 방출되는 전하로 전리 방사선을
측정합니다: `X = Q / m`, 단위는 `C/kg`. 역사적으로 사용된 단위는 **뢴트겐**입니다
(1 R = 2.58 × 10⁻⁴ C/kg).

이 차원은 `current · time · mass⁻¹`이며 — 입자의 [비전하](../electrical/specificcharge.ko.md)와
**동일**합니다. KUnit은 두 읽기 방식 모두에 대해 하나의 그룹을 모델링하며, 조사선량은 그 중
하나입니다. 이 페이지는 그 읽기 방식을 문서화합니다.

## 조사선량이 독자적인 타입을 갖지 않는 이유

KUnit은 의도적으로 별도의 `KExposureUnitInstance`가 아닌 `KSpecificChargeUnitInstance`로
조사선량을 모델링합니다. 그 이유는 이 라이브러리의 형태 인식 계약에 있습니다:

* 모든 표준화된 그룹은 **하나**의 표준 기본 차원 정규형을 가지며,
* `toX()`는 정확히 그 형태만을 인식합니다.

조사선량과 비전하는 정규형 `current¹ · time¹ · mass⁻¹`를 공유합니다. 하나의 정규형에 두 개의
타입이 있으면 네이티브 표현이 모호해집니다 — `toSpecificCharge()`와 가상의 `toExposure()`
모두 같은 혼합 단위와 일치하며, 어느 쪽도 더 정확하다고 할 수 없습니다. 단일 타입은 왕복 변환을
결정적으로 유지합니다.

따라서 이 구분은 라이브러리가 건네주는 타입의 차이가 아니라, *변수에 어떤 이름을 붙이는가*의
문제입니다 — 물리학에서 두 값이 모두 C/kg로 쓰이는 것과 정확히 같습니다.

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

val exposure = 1 of roentgens                   // read as an ionisation dose
exposure into coulombsPerKilogram                // 2.58e-4

// The charge liberated in 1 kg of air
val q = exposure * (1 of kilo.grams)
q into coulombs                                   // 2.58e-4

// A survey reading in milliroentgen
val small = 20 of milli.roentgens
small into coulombsPerKilogram                    // ≈ 5.16e-6
```

## 실제 사례 — 옛날식 선량계 판독값

펜형 선량계가 근무 후 **200 mR**을 표시합니다. 이를 SI 단위로 환산하고, 챔버가 교정된 기준인
1 kg의 공기에서 방출되는 전하로도 환산합니다:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

val shift = 200 of milli.roentgens
shift into coulombsPerKilogram                    // ≈ 5.16e-5
(shift * (1 of kilo.grams)) into micro.coulombs   // ≈ 51.6 µC
```

## 참고 항목

* [비전하](../electrical/specificcharge.ko.md) — 같은 타입을, 입자 속성으로 읽은 것.
* [흡수선량](absorbed-dose.ko.md)과 [등가선량](dose-equivalent.ko.md) — 에너지 기반의 선량.
* [선량률](dose-rate.ko.md) — 시간당 선량.
