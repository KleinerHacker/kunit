# 전하

패키지: `org.pcsoft.framework.kunit.electric.charge`
기본 단위: **쿨롱**(`KChargeUnit.BASE == KChargeUnit.COULOMB`)

유형: **구성 단위**

전하는 `current · time`(`A·s`) 조합으로 이루어진 **구성** 단위입니다. `KChargeUnitInstance` 는 두 개의 항 — 지수 `+1` 의
`KElectricCurrentUnit.BASE`(암페어)와 지수 `+1` 의 `KTimeUnit.BASE`(초) — 을 가진
`KMixedUnitInstance` 를 감쌉니다. 어떤 명명 단위, SI 접두사, 전류/시간 조합으로 만들든 저장되는 값은 항상 쿨롱으로 정규화됩니다.

## 전하 생성

명명 토큰으로 전하를 만들거나 분해 (아래 참조)로 만들 수 있습니다. 명명 단위는 값 1 토큰으로 남아
`of`/`into` 와 함께 사용됩니다:

| 전하              | 기호    |                토큰 |   1 단위의 C 값 |
|-------------------|---------|--------------------:|----------------:|
| 쿨롱              | `C`     |          `coulombs` |             1.0 |
| 암페어초          | `As`    |     `ampereSeconds` |             1.0 |
| 암페어시          | `Ah`    |       `ampereHours` |          3600.0 |
| 앱쿨롱(CGS-EMU)   | `abC`   |        `abcoulombs` |            10.0 |
| 스탯쿨롱(CGS-ESU) | `statC` |      `statcoulombs` |    3.335641e-10 |
| 패러데이          | `F_c`   |          `faradays` |       96485.332 |
| 기본 전하         | `e`     | `elementaryCharges` | 1.602176634e-19 |

명명 단위는 `KPrefixBuilder` 를 통해 SI 접두사를 지원합니다 (`kilo.coulombs`, `milli.coulombs`,
`milli.ampereHours` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.charge.*

val q = 470 of coulombs
q into coulombs                        // 470.0
q into kilo.coulombs                   // 0.47
(1 of ampereHours) into coulombs       // 3600.0
(2000 of milli.ampereHours) into coulombs // 7200.0
```

## 여러 분해

전하는 여러 **동등한 분해**로 얻을 수 있으며 모두 값이 같은 전하를 만듭니다:

| 식                    | 결과 타입             | 의미                                |
|-----------------------|-----------------------|-------------------------------------|
| `current * time`      | `KChargeUnitInstance` | 정의 `Q = I · t`                    |
| `time * current`      | `KChargeUnitInstance` | `Q = I · t` 의 교환 형태            |
| `current / frequency` | `KChargeUnitInstance` | 역시간 형태 `Q = I / f`(`1/Hz = s`) |
| `current·time`        | `.toCharge()` 경유    | 네이티브 정규형 `A·s` 식            |

타입이 지정된 연산자 형태는 전하를 직접 반환합니다. 완전한 네이티브 식은 일반 `KMixedUnitInstance` 로 남으며 `toCharge()` 로 좁힙니다 (정규형 — 지수 `+1` 의
`KElectricCurrentUnit` 항 하나와 지수 `+1` 의
`KTimeUnit` 항 하나 — 만 인식하고 그렇지 않으면 `IllegalStateException` 을 던집니다). 모든 경로는 값이 같습니다.

역연산자는 전하, 전류, 시간을 연결합니다:

| 식                   | 결과 타입                      | 의미                     |
|----------------------|--------------------------------|--------------------------|
| `charge / time`      | `KElectricCurrentUnitInstance` | `I = Q / t`              |
| `charge / current`   | `KTimeUnitInstance`            | `t = Q / I`              |
| `charge * frequency` | `KElectricCurrentUnitInstance` | `I = Q · f`(역시간 형태) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.charge.*

// 실제 예시 - 배터리 용량: 2000 mAh 셀은 7200 C 를 저장합니다.
val battery = 2000 of milli.ampereHours   // KChargeUnitInstance, 7200 C

// 250 mA 로 일정하게 방전하면 얼마나 오래 갈까요?
battery / (0.25 of amperes)               // KTimeUnitInstance, 28800 s(8 시간)

// 같은 전하를 타입 분해와 네이티브 A·s 식으로:
val typed = (2 of amperes) * (1 of hours)                  // KChargeUnitInstance, 7200 C
val raw = (2 of amperes).toUnit() * (1 of hours).toUnit()  // KMixedUnitInstance
raw.toCharge() == typed                                    // true
```

## 전기 선속

닫힌 곡면을 통과하는 **전기 선속** `Ψ` 는 그 안에 둘러싸인 전하와 같습니다 (가우스 법칙, `Ψ = Q`). 따라서 전하와 **차원적으로 동일**하며 마찬가지로 쿨롱으로 측정됩니다. KUnit 은 이를 이
그룹과 기호
`C` 로 모델링합니다. 별도의 토큰이나 별도의 타입은 없습니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.charge.*

// 2 µC 를 둘러싼 구는 2 µC 의 전기 선속을 갖습니다.
val psi = 2 of micro.coulombs
psi into micro.coulombs        // 2.0
```

선속을 면적으로 나누면 [전기 선속 밀도](electricfluxdensity.md) `D = Ψ / A` 를 얻습니다.

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.*

val s = (100 of coulombs) + (40 of coulombs)  // 140 C
(100 of coulombs) > (40 of coulombs)          // true
(100 of coulombs) * (40 of coulombs)          // KMixedUnitInstance(그룹을 벗어남)
```

## toString 형식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.*

(470 of coulombs).toString()   // "470.0 C"(기본 단위)
(1 of ampereHours).toString()  // "3600.0 C"(기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지와 KUnit 을 사용한 Kotlin 표기를 비교합니다. 지수는 유니코드 위첨자 (`²`, `³`, `⁻¹`)로 표기하며 `·` 는 곱셈, `/` 는
분수를 뜻합니다. 분수와 음의 지수를 쓴 곱 모두로 표현할 수 있는 양은 두 가지 동등한 Kotlin 형태를 함께 나열합니다.

| 수학   | Kotlin              | 의미                                          |
|--------|---------------------|-----------------------------------------------|
| `C`    | `coulombs`          | 전하, 기본 단위(명명 토큰, 쿨롱)              |
| `A·s`  | `amperes * seconds` | 전류·시간으로서의 전하(곱 형태)               |
| `A/Hz` | `amperes / hertz`   | 전류를 주파수로 나눈 같은 전하(`1/Hz = s`)    |
| `mAh`  | `milli.ampereHours` | 접두사가 붙은 전하(밀리암페어시, 배터리 용량) |

## 함께 보기

- [전류](ec.md) — 전하 구성의 전류 인자
- [전압](voltage.md) — 전위차
- [저항](resistance.md) — 옴의 법칙이 전기 그룹을 완성합니다
