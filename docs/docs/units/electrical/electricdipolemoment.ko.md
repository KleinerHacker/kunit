# 전기 쌍극자 모멘트 (Electric Dipole Moment)

패키지: `org.pcsoft.framework.kunit.electric.electricdipolemoment`
기본 단위: **쿨롬미터(coulomb meter)**
(`KElectricDipoleMomentUnit.BASE == KElectricDipoleMomentUnit.COULOMB_METER`)

유형: **구성 단위(constructed unit)**

전기 쌍극자 모멘트는 **구성** 단위입니다: `전류 · 시간 · 길이`
(`A·s·m` = `C·m`)의 조합입니다. `KElectricDipoleMomentUnitInstance`는 세 개의 항으로 이루어진
`KMixedUnitInstance`를 감쌉니다 — `KElectricCurrentUnit.BASE`(암페어)는 `+1`, `KTimeUnit.BASE`(초)는
`+1`, `KDistanceUnit.BASE`(미터)는 `+1`입니다. 이 그룹은 질량 차원을 갖지 않으므로 그램/킬로그램 변환이
필요 없습니다; 저장된 값은 항상 쿨롬미터로 정규화됩니다.

전기 쌍극자 모멘트 `p = Q · d`는 양전하와 음전하 [전하](charge.md)의 분리를 측정합니다. 이는 분자를
[전기장 세기](electricfieldstrength.md)와 결합시키는 양입니다.

## 전기 쌍극자 모멘트 만들기

이름이 붙은 토큰으로 쌍극자 모멘트를 만들거나, 아래의 분해식으로부터 만들 수 있습니다. 이름이 붙은 단위는
값 1의 토큰으로 존재합니다(`of`/`into`와 함께 사용):

| 쌍극자 모멘트 | 기호 | 토큰 | C·m 단위로 1 |
|---|---|---:|---:|
| 쿨롬미터 | `C·m` | `coulombMeters` | 1.0 |
| 디바이(CGS) | `D` | `debyes` | 3.335640952e-30 |

디바이는 분자 물리학과 화학에서 주로 사용됩니다. 이름이 붙은 단위는 `KPrefixBuilder`를 통해 SI
접두사를 지원합니다(`pico.coulombMeters`, `milli.debyes` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.dipolemoment.*

val p = 1.85 of debyes        // 물 분자
p into debyes                 // 1.85
p into coulombMeters          // 6.1709357612e-30
```

## 다중 분해

전기 쌍극자 모멘트는 여러 **동등한 분해식**을 통해 도달할 수 있으며, 모두 동일한 값-동등 모멘트를
생성합니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `charge * length` | `KElectricDipoleMomentUnitInstance` | `p = Q · d`, 전하와 그 분리 거리의 곱(교환 가능) |
| `current·time·length` | `.toElectricDipoleMoment()`를 통해 | 네이티브 정규 `A·s·m` 표현식 |

타입이 지정된 연산자 형식은 쌍극자 모멘트를 직접 반환합니다. 완전히 네이티브인 표현식은 일반적인
`KMixedUnitInstance`로 남아 있으며 `toElectricDipoleMoment()`로 좁혀집니다(이는 정규 형식만 인식하고
그렇지 않으면 `IllegalStateException`을 발생시킵니다). 두 경로 모두 값-동등합니다.

역연산자는 전하, 분리 거리, 모멘트를 함께 묶습니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `electricDipoleMoment / charge` | `KLengthUnitInstance` | `d = p / Q` |
| `electricDipoleMoment / length` | `KChargeUnitInstance` | `Q = p / d` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.dipolemoment.*

// 실제 사례 - 1 nm 떨어진 1 pC은 1e-21 C·m, 약 3.0e8 디바이를 만듭니다.
val p = (1 of pico.coulombs) * (1 of nano.meters)   // KElectricDipoleMomentUnitInstance
p into debyes                                       // 2.997924579983392e8

// 분리 거리에 대해 다시 풀어냄:
val d = (6 of coulombMeters) / (2 of coulombs)      // KLengthUnitInstance, 3 m

// 네이티브 A·s·m 표현식으로서의 동일한 모멘트:
val raw = 6 of ((amperes pow 1) * (seconds pow 1) * (meters pow 1))
raw.toElectricDipoleMoment() == (6 of coulombMeters) // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.dipolemoment.*

val s = (2 of coulombMeters) + (3 of coulombMeters)  // 5 C·m
(1 of coulombMeters) > (1 of debyes)                 // true
(2 of coulombMeters) * (3 of coulombMeters)          // KMixedUnitInstance (그룹을 벗어남)
```

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.dipolemoment.*

(2 of coulombMeters).toString()   // "2.0 C·m" (기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를
보여줍니다. 지수는 유니코드 위첨자를 사용하며, `·`는 곱셈을, `/`는 분수를 나타냅니다. 어떤 양이 분수와
음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `C·m` | `coulombMeters` | 전기 쌍극자 모멘트, 기본 단위(이름이 붙은 토큰, 쿨롬미터) |
| `D` | `debyes` | CGS 디바이, 3.335 640 952e-30 C·m |
| `Q · d` | `(1 of pico.coulombs) * (1 of nano.meters)` | 전하와 그 분리 거리로부터의 모멘트 |
| `p / Q` | `(6 of coulombMeters) / (2 of coulombs)` | 모멘트 뒤에 있는 분리 거리 |
| `A·s·m` | `(amperes pow 1) * (seconds pow 1) * (meters pow 1)` | 전류·시간·길이(순수 곱)로서의 모멘트 |
| `pC·m` | `pico.coulombMeters` | 접두사가 붙은 모멘트(피코쿨롬미터) |
