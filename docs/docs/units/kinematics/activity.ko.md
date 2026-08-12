# 방사능 (베크렐)

패키지: `org.pcsoft.framework.kunit.kinematic.frequency`
기본 단위: **헤르츠**(`KFrequencyUnit.BASE == KFrequencyUnit.HERTZ`)

유형: **네이티브 단위**

방사성 시료의 방사능 `A`는 초당 원자핵 붕괴 횟수입니다. 그 단위는 **베크렐**이며, `1 Bq = 1 s⁻¹`로
[주파수](frequency.ko.md)와 **차원적으로 동일**합니다.

## 베크렐이 별도의 타입을 갖지 않는 이유

KUnit은 방사능을 별도의 `KActivityUnitInstance`가 아니라 의도적으로 `KFrequencyUnitInstance`로 모델링합니다.
그 이유는 이 라이브러리의 형식 인식 계약에 있습니다:

* 모든 표준화된 그룹은 **단 하나**의 정규 기본 차원 정규형을 가지며,
* `toX()`는 정확히 그 형식만 인식합니다.

방사능과 주파수는 정규형 `time⁻¹`을 공유합니다. 하나의 정규형에 두 개의 타입이 있으면 네이티브 표현이
모호해집니다 — `toFrequency()`와 가상의 `toActivity()`는 동일한 혼합 단위에 매치되며, 어느 쪽도 다른
쪽보다 더 옳다고 할 수 없습니다. 단일 타입이 왕복 변환의 결정성을 유지합니다.

그 구분은 *변수를 어떻게 이름 붙이는가*의 문제입니다: 주파수는 주기적인 사이클을 세고, 방사능은 무작위
붕괴를 세지만, 둘 다 "초당 이벤트 수"입니다.

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.frequency.*
import org.pcsoft.framework.kunit.kinematic.time.seconds

val activity = 37 of giga.hertz     // 37 GBq로 읽음 — 라듐 1그램
activity into mega.hertz             // 37 000.0

// 1분 동안의 붕괴 수
val decays = activity * (60 of seconds)   // 무차원 개수
decays                                     // 2.22e12
```

!!! note "퀴리"
    역사적인 단위는 퀴리이며, 1 Ci = 3.7 × 10¹⁰ Bq입니다. 전용 토큰은 없으며,
    `37 of giga.hertz`로 작성하거나 자체 상수를 도입하십시오.

## 실제 사례 — 화재 감지기의 방사선원

가정용 화재 감지기에는 약 **30 kBq**의 아메리슘-241이 들어 있습니다:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.frequency.*
import org.pcsoft.framework.kunit.kinematic.time.hours

val source = 30 of kilo.hertz             // 30 kBq
source into hertz                          // 30 000.0

// 하루 동안의 붕괴 수
val perDay = source * (24 of hours)
perDay                                      // ≈ 2.59e9
```

## 참고

* [주파수](frequency.ko.md) — 동일한 타입을, 주기적 비율로 읽은 것.
* [선량률](../thermodynamics/dose-rate.ko.md) — 방사선원이 시간당 전달하는 선량.
* [흡수선량](../thermodynamics/absorbed-dose.ko.md) — 에너지 기반의 선량.
