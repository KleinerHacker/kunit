# 선량률

패키지: `org.pcsoft.framework.kunit.thermo.doserate`
기본 단위: **그레이 매 초** (`KDoseRateUnit.BASE == KDoseRateUnit.GRAY_PER_SECOND`)

유형: **구성 단위（constructed unit）**

선량률은 **시간당** 흡수되는 방사선량입니다: `Ḋ = D / t`. 이는 서베이 미터가 표시하는 값이며 —
거의 항상 마이크로시버트 매 시간으로 표시됩니다 — 누적 선량은 노출 시간에 대한 적분값입니다.

표준 기본 차원 정규형은 `length² · time⁻³`입니다. 그레이의 `J/kg`에서 킬로그램이 줄의
킬로그램과 상쇄되므로 질량 항이 남지 않습니다.

## 명명된 단위

| 단위               | 기호    | 토큰                   | Gy/s 기준 1단위 |
|--------------------|---------|------------------------|----------------:|
| 그레이 매 초         | `Gy/s`  | `graysPerSecond`       |            1.0 |
| 그레이 매 시간        | `Gy/h`  | `graysPerHour`         |         1/3600 |
| 시버트 매 초         | `Sv/s`  | `sievertsPerSecond`    |            1.0 |
| 시버트 매 시간        | `Sv/h`  | `sievertsPerHour`      |         1/3600 |

그레이(흡수선량)와 시버트(등가선량)는 하나의 차원을 공유하므로, KUnit은 두 가지에 대해 하나의
그룹을 모델링합니다 — 시버트 표기는 방사선 방호 측정값을 직접 작성할 수 있도록 존재합니다.
모든 토큰은 모든 SI 접두사를 지원합니다; `micro.sievertsPerHour`가 일상적으로 사용되는 형태입니다.

!!! note "하나의 그룹, 두 가지 읽기"
    그레이와 시버트는 차원이 아니라 무차원의 방사선 가중 계수에 의해 구별됩니다. 단일 정규형은
    단일 타입에 매핑되어야 하므로 ([엔트로피](entropy.ko.md)에서도 같은 논리를 참조하세요),
    이 구분은 값에 어떤 이름을 붙이는가의 문제입니다.

## 분해

이 그룹은 하나의 분해를 가지며, 두 형식 모두 동일한 타입의 값-동등 인스턴스를 생성합니다:

| 형식                | 표현식                                                                        |
|--------------------|----------------------------------------------------------------------------------|
| 타입화된 연산자        | `specificEnergy / time`                                                         |
| 네이티브 (`toX()`)   | `((6 of joulesPerKilogram).toUnit() / (2 of seconds).toUnit()).toDoseRate()`    |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import org.pcsoft.framework.kunit.thermo.doserate.*

val typed = (6 of joulesPerKilogram) / (2 of seconds)
val native = ((6 of joulesPerKilogram).toUnit() / (2 of seconds).toUnit()).toDoseRate()

typed == native            // true
typed into graysPerSecond  // 3.0
```

## 그룹으로 계산하기

| 표현식                          | 결과 타입                          | 의미                  |
|------------------------------------|--------------------------------------|-----------------------|
| `specificEnergy / time`           | `KDoseRateUnitInstance`              | `Ḋ = D / t`           |
| `doseRate * time`                 | `KSpecificEnergyUnitInstance`        | 누적된 선량              |
| `specificEnergy / doseRate`       | `KTimeUnitInstance`                  | 노출 시간                |

흡수선량 자체는 [비에너지](specific-energy.ko.md) 그룹에 속합니다 — 1 Gy = 1 J/kg.

## 실제 사례 — 연간 자연 배경 방사선

자연 배경 방사선은 대략 **0.274 µSv/h**입니다. 1년(8766시간) 동안 이는 익숙한 2.4 mSv로
누적됩니다:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import org.pcsoft.framework.kunit.thermo.doserate.*

val background = 0.274 of micro.sievertsPerHour
val year = 8766 of hours

val dose = background * year                       // KSpecificEnergyUnitInstance
dose into milli.joulesPerKilogram                  // ≈ 2.4 (mSv)

// How long until a 1 mSv limit is reached?
val t = (1 of milli.joulesPerKilogram) / background
t into hours                                        // ≈ 3650 h
```

## 값 의미론

`equals`/`hashCode`는 **정규화된 Gy/s 값**을 비교하므로,
`(1 of graysPerHour) == (1 of sievertsPerHour)`입니다. `toString()`은 기본 단위로 값을
표시합니다: `"1.0 Gy/s"`.

## 참고 항목

* [비에너지](specific-energy.ko.md) — 흡수선량 자체 (`Gy` = `J/kg`).
* [열역학 개요](overview.ko.md)
