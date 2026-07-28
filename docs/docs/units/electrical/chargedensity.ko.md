# 전하 밀도

패키지: `org.pcsoft.framework.kunit.electric.chargedensity`
기본 단위: **세제곱미터당 쿨롱**(`KChargeDensityUnit.BASE == KChargeDensityUnit.COULOMB_PER_CUBIC_METER`)

종류: **구성 단위**

(부피) 전하 밀도는 **구성** 단위로, 조합 `current¹ · time¹ · length⁻³`(`A·s·m⁻³` = `C/m³`)입니다.
`KChargeDensityUnitInstance` 는 세 개의 항 — 지수 `+1` 의 `KElectricCurrentUnit.BASE`(암페어), 지수 `+1` 의
`KTimeUnit.BASE`(초), 지수 `-3` 의 `KDistanceUnit.BASE`(미터) — 을 가진 `KMixedUnitInstance` 를 감쌉니다.
모든 성분이 각 그룹의 기본 단위로 저장되므로, 저장된 값은 곧 C/m³ 단위의 읽기값입니다.

## 전하 밀도 만들기

전하 밀도에는 **베어 토큰도 접두사 빌더도 없습니다** — 모든 표기(C/m³, mC/cm³ 등)가 비율입니다. 식으로
만들거나 타입이 지정된 `charge / volume` 연산자로 만들고, 그런 식에 대해 `into` 로 되읽습니다. 접두사는 성분
토큰(`milli.coulombs`, `centi.meters`)에서 옵니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

val rho = (6 of coulombs) / (2 of liters)  // KChargeDensityUnitInstance, 3 C/L = 3000 C/m³
rho into (coulombs / (meters pow 3))       // 3000.0
rho into (coulombs / (centi.meters pow 3)) // 0.003 (= 3 mC/cm³)
rho into (milli.coulombs / (meters pow 3)) // 3000000.0
```

## 여러 분해

전하 밀도는 여러 **동등한 분해**로 도달할 수 있으며, 모두 값이 같은 전하 밀도를 만듭니다:

| 식 | 결과 타입 | 의미 |
|---|---|---|
| `charge / volume` | `KChargeDensityUnitInstance` | 정의 `ρ = Q / V` |
| `current·time/length³` | `.toChargeDensity()` 경유 | 네이티브 표준형 `A·s·m⁻³` 식 |

타입이 지정된 연산자 형태는 전하 밀도를 바로 반환합니다. 완전한 네이티브 식은 일반 `KMixedUnitInstance` 로
남고 `toChargeDensity()` 로 좁혀집니다(표준형만 인식하며, 그 외에는 `IllegalStateException` 을 던집니다).
두 경로의 값은 동일합니다.

역연산자는 전하, 부피, 전하 밀도를 서로 연결합니다:

| 식 | 결과 타입 | 의미 |
|---|---|---|
| `chargeDensity * volume` | `KChargeUnitInstance` | `Q = ρ · V` |
| `volume * chargeDensity` | `KChargeUnitInstance` | `Q = V · ρ`(교환 가능) |
| `charge / chargeDensity` | `KVolumeUnitInstance` | `V = Q / ρ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

// 실제 예시 - 전해질 속 공간 전하: 전해질 4리터에 녹아 있는 순 전하 12 mC 는 전하 밀도 3 C/m³ 입니다.
val rho = (0.012 of coulombs) / (4 of liters)   // KChargeDensityUnitInstance, 3 C/m³

// 같은 전하 밀도를 네이티브 A·s·m⁻³ 식으로:
val raw = (0.012 of coulombs).toUnit() / (0.004 of (meters pow 3))
raw.toChargeDensity() == rho                    // true

// 4리터에 들어 있는 전하로, 그리고 12 mC 를 담는 부피로 되돌리기:
val q = rho * (4 of liters)                     // KChargeUnitInstance
q into coulombs                                 // 0.012
val v = (0.012 of coulombs) / rho               // KVolumeUnitInstance
v into liters                                   // 4.0
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

val a = (3 of coulombs) / (1 of liters)     // 3000 C/m³
val b = (1 of coulombs) / (1 of liters)     // 1000 C/m³
(a + b) into (coulombs / (meters pow 3))    // 4000.0
(a - b) into (coulombs / (meters pow 3))    // 2000.0
a > b                                       // true
a * b                                       // KMixedUnitInstance (그룹을 벗어남)
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

((1 of coulombs) / (1 of liters)).toString() // "1000.0 C/m³" (기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 쓰는 방법과 KUnit 을 사용한 Kotlin 표기를 비교합니다. 지수는 유니코드 위 첨자(`²`, `³`, `⁻¹`)를 사용하고, `·` 는 곱셈, `/` 는 분수를 나타냅니다. 분수로도 음의 지수를 가진 곱으로도 쓸 수 있는 양은 두 가지 동등한 Kotlin 형식을 모두 표시합니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `C/m³` | `coulombs / (meters pow 3)` | 전하 밀도, 기본 단위(세제곱미터당 쿨롱) — 분수 형태 |
| `C·m⁻³` | `coulombs * (meters pow -3)` | 같은 전하 밀도를 음의 지수 곱으로 표현 |
| `A·s/m³` | `amperes * seconds / (meters pow 3)` | 네이티브 표준형(전류·시간 / 길이³) |
| `mC/cm³` | `milli.coulombs / (centi.meters pow 3)` | 세제곱센티미터당 밀리쿨롱 |
| `12 mC / 4 L` | `(12 of milli.coulombs) / (4 of liters)` | 전하 ÷ 부피로 구성 |
