# 비음향 임피던스

패키지: `org.pcsoft.framework.kunit.mechanic.acousticimpedance`
기본 단위: **파스칼 초 매 미터**
(`KAcousticImpedanceUnit.BASE == KAcousticImpedanceUnit.PASCAL_SECOND_PER_METER`)

유형: **구성 단위**

비음향 임피던스 `Z`는 매질이 단위 입자 속도당 발생시키는 음압입니다:
`Z = p / v = ρ · c`. 이는 경계면에서 얼마나 많은 소리가 반사되는지를 결정합니다 — 공기는 약
413 Pa·s/m, 물은 약 1.48 MPa·s/m로, 그 비율은 약 3600입니다. 이것이 공기 중의 소리가 물 속으로
거의 전달되지 않는 이유입니다.

정준 기저 차원 표준형은 `mass · length⁻² · time⁻¹`입니다.

## 명명된 단위

| 단위                     | 기호         |                   토큰 | 1 단위 (Pa·s/m 기준) |
|--------------------------|--------------|------------------------:|-----------------:|
| 파스칼 초 매 미터          | `Pa*s/m`     | `pascalSecondsPerMeter` |              1.0 |
| SI 레일                   | `rayl`       |                 `rayls` |              1.0 |
| CGS 레일                  | `rayl (CGS)` |              `cgsRayls` |               10 |

`rayls`는 기본 단위의 또 다른 표기법일 뿐, 별도의 단위가 아닙니다. 모든 토큰은 모든 SI 접두사를
받아들입니다 (`mega.rayls`는 조직 및 물에 흔히 사용됩니다). 인접한 힘, 압력, 밀도 그룹과 마찬가지로
이 인스턴스는 **그램 기반의 원시 성분 값**을 저장합니다.

## 분해

이 그룹에는 **두 가지** 분해가 있습니다. 둘 다 동일한 정규화 팩토리로 합류합니다:

| 형태               | 표현식                                                          |
|--------------------|--------------------------------------------------------------------|
| 타입 연산자 A       | `pressure / speed`                                                |
| 타입 연산자 B       | `density * speed` (`Z = ρ · c`, 고유 임피던스)                     |
| 원시 형태 (`toX()`) | `(1 of kilo.grams / m² / s).toAcousticImpedance()`                |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.acousticimpedance.*

val air = (1.204 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val c = (343 of meters) / (1 of seconds)

val viaDensity = air * c                                        // B
val viaPressure = (412.972 of pascals) / ((1 of meters) / (1 of seconds))  // A

viaDensity into rayls        // ≈ 412.97
viaPressure into rayls       // ≈ 412.97
```

## 그룹으로 계산하기

| 표현식                             | 결과 타입                            | 의미                    |
|--------------------------------------|-----------------------------------------|--------------------------|
| `pressure / speed`                  | `KAcousticImpedanceUnitInstance`      | `Z = p / v`              |
| `density * speed`                   | `KAcousticImpedanceUnitInstance`      | `Z = ρ · c`              |
| `acousticImpedance * speed`         | `KPressureUnitInstance`               | 음압                       |
| `pressure / acousticImpedance`      | `KSpeedUnitInstance`                  | 입자 속도                  |
| `acousticImpedance / speed`         | `KDensityUnitInstance`                | `ρ`로 되돌아감              |
| `acousticImpedance / density`       | `KSpeedUnitInstance`                  | `c`로 되돌아감              |

## 실제 사례 — 공기/물 경계

물속에 있는 수영하는 사람의 머리를 향해 소리쳐도 왜 소용이 없을까요? 두 고유 임피던스를 비교해
봅시다:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.acousticimpedance.*

val air = (1.204 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

val zAir = air * ((343 of meters) / (1 of seconds))
val zWater = water * ((1480 of meters) / (1 of seconds))

zAir into rayls              // ≈ 413
zWater into mega.rayls       // ≈ 1.48

(zWater into rayls) / (zAir into rayls)   // ≈ 3584 — 거의 완전 반사
```

## 값 의미론

`equals`/`hashCode`는 **정규화된 성분 값**을 비교하므로 `(1 of cgsRayls) == (10 of rayls)`입니다.
`toString()`은 기본 단위로 값을 표현합니다: `"413.0 Pa*s/m"`.

## 참고

* [밀도](density.ko.md)와 [속력](../kinematics/speed.ko.md) — `Z = ρ · c`의 두 인자.
* [압력](pressure.ko.md) — 음압 측면.
* [역학 개요](overview.ko.md)
