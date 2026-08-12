# 저크 (가가속도)

패키지: `org.pcsoft.framework.kunit.kinematic.jerk`
기본 단위: **초당 세제곱미터**(`KJerkUnit.BASE == KJerkUnit.METER_PER_SECOND_CUBED`)

유형: **구성된 단위**

저크(가가속도) `j`는 **가속도**가 변화하는 비율입니다: `j = Δa / t`. 이는 승차감 기준이 실제로 제한하는
양입니다 — 엘리베이터나 열차는 강하게 가속할 수 있지만, 가속도가 갑작스럽게 변하면 승객이 휘청거리게 됩니다.
쾌적 한계는 대략 0.5 m/s³ 부근입니다.

정규 기본 차원 정규형은 `length · time⁻³`입니다.

## 명명된 단위

| 단위                    | 기호     |                          토큰 | m/s³ 환산(1 단위) |
|-------------------------|----------|--------------------------------:|--------------------:|
| 초당 세제곱미터         | `m/s^3`  |       `metersPerSecondCubed`    |                 1.0 |
| 초당 표준 중력          | `g/s`    | `standardGravitiesPerSecond`    |             9.80665 |
| 초당 세제곱피트         | `ft/s^3` |          `feetPerSecondCubed`   |              0.3048 |

모든 토큰은 모든 SI 접두어를 지원합니다(`milli.metersPerSecondCubed` 등).

## 분해

이 그룹은 하나의 분해를 가지며, 두 형태 모두 동일한 타입의, 값이 같은 인스턴스를 생성합니다:

| 형태                | 표현식                                                              |
|---------------------|------------------------------------------------------------------------|
| 타입 연산자         | `acceleration / time`                                               |
| 네이티브(`toX()`) | `(acceleration.toUnit() / (2 of seconds).toUnit()).toJerk()`        |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.acceleration.gals
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.jerk.*

val a = 120 of gals                    // 1.2 m/s²(1 Gal = 0.01 m/s²)

val typed = a / (2 of seconds)
val native = (a.toUnit() / (2 of seconds).toUnit()).toJerk()

typed == native                        // true
typed into metersPerSecondCubed        // 0.6
```

## 그룹으로 계산하기

| 표현식                 | 결과 타입                          | 의미                          |
|------------------------|--------------------------------------|--------------------------------|
| `acceleration / time`  | `KJerkUnitInstance`                  | `j = Δa / t`                   |
| `jerk * time`          | `KAccelerationUnitInstance`          | 누적된 가속도                  |
| `acceleration / jerk`  | `KTimeUnitInstance`                  | 램프에 걸리는 시간              |

## 실제 사례 — 쾌적 한계 내의 엘리베이터 램프

엘리베이터가 저크 **0.5 m/s³**를 초과하지 않고 **1 m/s²**에 도달해야 합니다. 램프는 얼마나 걸려야 할까요?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.acceleration.gals
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.jerk.*

val target = 100 of gals                        // 1 m/s²
val comfort = 0.5 of metersPerSecondCubed

val ramp = target / comfort                     // KTimeUnitInstance
ramp into seconds                                // 2.0 s

// 반대로: 1초 램프는 얼마의 저크를 부과하는가?
val harsh = target / (1 of seconds)
harsh into metersPerSecondCubed                  // 1.0 — 쾌적 한계의 두 배
```

## 값 의미론

`equals`/`hashCode`는 **정규화된 m/s³ 값**을 비교하므로
`(1 of metersPerSecondCubed) == (1000 of milli.metersPerSecondCubed)`입니다. `toString()`은 값을 기본
단위로 표시합니다: `"0.6 m/s^3"`.

## 참고

* [가속도](acceleration.ko.md) — 이 단위가 변화율을 나타내는 대상 물리량.
* [속도](speed.ko.md)와 [거리](distance.ko.md) — 운동 연쇄의 나머지.
* [운동학 개요](overview.ko.md)
