# 흡수선량（그레이）

패키지: `org.pcsoft.framework.kunit.thermo.specificenergy`
기본 단위: **줄 매 킬로그램**
(`KSpecificEnergyUnit.BASE == KSpecificEnergyUnit.JOULE_PER_KILOGRAM`)

유형: **구성 단위（constructed unit）**

흡수선량 `D`는 전리 방사선이 단위 질량당 축적하는 에너지입니다: `D = E / m`. 이 단위는
**그레이**이며, `1 Gy = 1 J/kg`으로 [비에너지](specific-energy.ko.md)와 **차원적으로 동일**합니다.

## 그레이가 독자적인 타입을 갖지 않는 이유

KUnit은 의도적으로 별도의 `KAbsorbedDoseUnitInstance`가 아닌 `KSpecificEnergyUnitInstance`로
흡수선량을 모델링합니다. 그 이유는 이 라이브러리의 형태 인식 계약에 있습니다:

* 모든 표준화된 그룹은 **하나**의 표준 기본 차원 정규형을 가지며,
* `toX()`는 정확히 그 형태만을 인식합니다.

흡수선량과 비에너지는 정규형 `length² · time⁻²`를 공유합니다. 하나의 정규형에 두 개의 타입이 있으면
네이티브 표현이 모호해집니다 — `toSpecificEnergy()`와 가상의 `toAbsorbedDose()` 모두 같은 혼합
단위와 일치하며, 어느 쪽도 더 정확하다고 할 수 없습니다. 단일 타입은 왕복 변환을 결정적으로 유지합니다.

따라서 이 구분은 라이브러리가 건네주는 타입의 차이가 아니라, *변수에 어떤 이름을 붙이는가*의
문제입니다 — 물리학에서 그레이가 곧 줄 매 킬로그램인 것과 정확히 같습니다.

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val dose = 2 of milli.joulesPerKilogram      // read as 2 mGy
dose into joulesPerKilogram                   // 0.002

// The energy deposited in a 70 kg body
val energy = dose * (70 of kilo.grams)
energy into joules                            // 0.14 J
```

## 실제 사례 — 흉부 X선 촬영

흉부 X선 촬영은 대략 **0.1 mGy**를 축적합니다. 체중 70 kg인 사람에게 이는 총 얼마의 에너지에
해당하며, 1년간의 자연 배경 방사선(≈ 2.4 mGy)과 비교하면 어떨까요?

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val xray = 0.1 of milli.joulesPerKilogram
val background = 2.4 of milli.joulesPerKilogram

(xray * (70 of kilo.grams)) into milli.joules      // 7.0 mJ
(background into joulesPerKilogram) / (xray into joulesPerKilogram)   // 24 X-rays per year of background
```

## 참고 항목

* [비에너지](specific-energy.ko.md) — 같은 타입을, 에너지 밀도로 읽은 것.
* [등가선량](dose-equivalent.ko.md) — 생물학적 영향을 가중한 시버트.
* [선량률](dose-rate.ko.md) — 시간당 선량으로, 독자적인 타입을 **갖습니다**.
* [조사선량](exposure.ko.md) — 전하 기반의 전리 선량.
