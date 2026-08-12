# 등가선량（시버트）

패키지: `org.pcsoft.framework.kunit.thermo.specificenergy`
기본 단위: **줄 매 킬로그램**
(`KSpecificEnergyUnit.BASE == KSpecificEnergyUnit.JOULE_PER_KILOGRAM`)

유형: **구성 단위（constructed unit）**

등가선량 `H`는 [흡수선량](absorbed-dose.ko.md)에 **무차원**의 방사선 가중 계수 `w_R`를 곱해
가중치를 부여한 값입니다. 이는 특정 방사선 유형이 얼마나 유해한지를 반영하며, `H = w_R · D`로
표현됩니다. 이 단위는 **시버트**이며, `w_R`가 무차원이므로 `1 Sv = 1 J/kg` — 그레이와 동일한
차원을 갖습니다.

## 시버트가 독자적인 타입을 갖지 않는 이유

KUnit은 그레이 및 비에너지와 동일한 타입인 `KSpecificEnergyUnitInstance`로 등가선량을
모델링합니다. 그 이유는 이 라이브러리의 형태 인식 계약에 있습니다:

* 모든 표준화된 그룹은 **하나**의 표준 기본 차원 정규형을 가지며,
* `toX()`는 정확히 그 형태만을 인식합니다.

시버트, 그레이, 비에너지는 모두 정규형 `length² · time⁻²`를 공유합니다. 하나의 정규형에 여러
타입이 있으면 네이티브 표현이 모호해지며, 어느 답도 다른 답보다 더 정확하다고 할 수 없습니다.
단일 타입은 왕복 변환을 결정적으로 유지합니다.

!!! warning "가중 계수는 사용자가 직접 적용해야 합니다"
    `w_R`가 무차원이므로 KUnit은 그레이와 시버트를 구분할 수 없습니다. 흡수선량에 가중 계수를
    곱하는 것은 단순한 스칼라 곱셈입니다 — 라이브러리가 이를 대신 처리해주지 않으며, 두 읽기
    값을 혼용하는 것을 막지도 않습니다. 값에 적절한 이름을 붙이세요.

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val absorbed = 2 of milli.joulesPerKilogram   // 2 mGy of alpha radiation
val wR = 20.0                                  // weighting factor for alpha

val equivalent = absorbed * wR                 // read as 40 mSv
equivalent into milli.joulesPerKilogram        // 40.0
```

## 실제 사례 — 비행과 연간 배경 방사선

자연 배경 방사선은 연간 약 **2.4 mSv**이며, 대서양 횡단 비행은 약 0.05 mSv를 추가합니다:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val perYear = 2.4 of milli.joulesPerKilogram
val flight = 0.05 of milli.joulesPerKilogram

(perYear into milli.joulesPerKilogram) / (flight into milli.joulesPerKilogram)  // 48 flights

// Ten flights added to the annual background
val total = perYear + (flight * 10)
total into milli.joulesPerKilogram                                              // 2.9
```

## 참고 항목

* [흡수선량](absorbed-dose.ko.md) — 가중되지 않은 그레이.
* [비에너지](specific-energy.ko.md) — 기반이 되는 타입.
* [선량률](dose-rate.ko.md) — 시간당 선량으로, 시버트 표기도 포함합니다.
* [조사선량](exposure.ko.md) — 전하 기반의 전리 선량.
