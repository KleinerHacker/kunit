# 선전하 밀도 (Linear Charge Density)

패키지: `org.pcsoft.framework.kunit.electric.linearchargedensity`
기본 단위: **미터당 쿨롬 (coulomb per meter)**
(`KLinearChargeDensityUnit.BASE == KLinearChargeDensityUnit.COULOMB_PER_METER`)

유형: **구성 단위 (constructed unit)**

선전하 밀도는 **구성** 단위입니다: `전류 · 시간 · 길이⁻¹`
(`A·s·m⁻¹` = `C/m`)의 조합입니다. `KLinearChargeDensityUnitInstance`는 세 개의 항으로 이루어진
`KMixedUnitInstance`를 감쌉니다 — `KElectricCurrentUnit.BASE`(암페어)는 `+1`, `KTimeUnit.BASE`(초)는
`+1`, `KDistanceUnit.BASE`(미터)는 `-1`입니다. 이 그룹은 질량 차원을 갖지 않으므로 그램/킬로그램 변환이 필요 없습니다; 저장된 값은 항상 미터당 쿨롬으로 정규화됩니다.

선전하 밀도 `λ`는 예를 들어 도선이나 대전된 필라멘트를 따라 단위 길이당 운반되는 전하입니다. 이 단위는 **고유한 이름 단위를 갖지 않습니다**: 모든 표기가 비율이므로 (C/m, µC/cm), 이 그룹은 순수
토큰이나 접두사 빌더를 갖지 않습니다 — 값은 표현식으로부터 또는 타입이 지정된 연산자를 통해 만들어집니다. 2차원 및 3차원 대응물은 [전속 밀도](electricfluxdensity.md)(C/m²)
와 [전하 밀도](chargedensity.md)(C/m³)입니다.

## 선전하 밀도 만들기

이름이 붙은 토큰은 없습니다. 전하를 길이로 나누어 값을 만듭니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

val lambda = (5 of micro.coulombs) / (2 of meters)  // 2.5e-6 C/m
lambda.value                                        // 2.5e-6 (C/m으로 정규화)
```

## 다중 분해

선전하 밀도는 여러 **동등한 분해식**을 통해 도달할 수 있으며, 모두 동일한 값-동등 밀도를 생성합니다:

| 표현식                | 결과 타입                          | 의미                               |
|-----------------------|------------------------------------|------------------------------------|
| `charge / length`     | `KLinearChargeDensityUnitInstance` | `λ = Q / l`, 길이를 따라 퍼진 전하 |
| `current·time/length` | `.toLinearChargeDensity()`를 통해  | 네이티브 정규 `A·s·m⁻¹` 표현식     |

타입이 지정된 연산자 형식은 선전하 밀도를 직접 반환합니다. 완전히 네이티브인 표현식은 일반적인
`KMixedUnitInstance`로 남아 있으며 `toLinearChargeDensity()`로 좁혀집니다 (이는 정규 형식만 인식하고 그렇지 않으면 `IllegalStateException`을 발생시킵니다).
두 경로 모두 값-동등합니다.

역연산자는 전하, 길이, 밀도를 함께 묶습니다:

| 표현식                         | 결과 타입             | 의미                    |
|--------------------------------|-----------------------|-------------------------|
| `linearChargeDensity * length` | `KChargeUnitInstance` | `Q = λ · l` (교환 가능) |
| `charge / linearChargeDensity` | `KLengthUnitInstance` | `l = Q / λ`             |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

// 실제 사례 - 2 m에 걸쳐 5 µC을 운반하는 필라멘트는 2.5 µC/m의 선전하 밀도를 가집니다.
val lambda = (5 of micro.coulombs) / (2 of meters)   // 2.5e-6 C/m

// 전하에 대해 다시 풀어냄:
val q = lambda * (2 of meters)                       // KChargeUnitInstance, 5 µC
q into micro.coulombs                                // 5.0

// 네이티브 A·s·m⁻¹ 표현식으로서의 동일한 밀도:
val raw = 2.5e-6 of ((amperes pow 1) * (seconds pow 1)) / (meters pow 1)
raw.toLinearChargeDensity() == lambda                // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

val a = (2 of coulombs) / (1 of meters)
val b = (3 of coulombs) / (1 of meters)
(a + b).value    // 5.0 C/m
b > a            // true
(a * b)          // KMixedUnitInstance (그룹을 벗어남)
```

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

((2 of coulombs) / (1 of meters)).toString()   // "2.0 C/m" (기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를 보여줍니다. 지수는 유니코드 위첨자 (`⁻¹`)를 사용하며, `·`는 곱셈을, `/`는 분수를
나타냅니다. 어떤 양이 분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학      | Kotlin                                                 | 의미                                          |
|-----------|--------------------------------------------------------|-----------------------------------------------|
| `C/m`     | `(1 of coulombs) / (1 of meters)`                      | 선전하 밀도, 기본 단위(이름이 붙은 토큰 없음) |
| `Q / l`   | `(5 of micro.coulombs) / (2 of meters)`                | 길이를 따른 전하로부터의 밀도                 |
| `λ · l`   | `lambda * (2 of meters)`                               | 길이가 운반하는 전하                          |
| `A·s/m`   | `((amperes pow 1) * (seconds pow 1)) / (meters pow 1)` | 전류·시간 / 길이로서의 밀도(분수 형식)        |
| `A·s·m⁻¹` | `(amperes pow 1) * (seconds pow 1) * (meters pow -1)`  | 순수 곱으로서의 동일한 밀도                   |
