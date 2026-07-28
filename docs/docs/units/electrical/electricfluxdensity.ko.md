# 전속 밀도 (Electric Flux Density)

패키지: `org.pcsoft.framework.kunit.electricfluxdensity`
기본 단위: **제곱미터당 쿨롬(coulomb per square meter)**
(`KElectricFluxDensityUnit.BASE == KElectricFluxDensityUnit.COULOMB_PER_SQUARE_METER`)

유형: **구성 단위(constructed unit)**

전속 밀도는 **구성** 단위입니다: `전류 · 시간 · 길이⁻²`
(`A·s·m⁻²` = `C/m²`)의 조합입니다. `KElectricFluxDensityUnitInstance`는 세 개의 항으로 이루어진
`KMixedUnitInstance`를 감쌉니다 — `KElectricCurrentUnit.BASE`(암페어)는 `+1`, `KTimeUnit.BASE`(초)는
`+1`, `KDistanceUnit.BASE`(미터)는 `-2`입니다. 이 그룹은 질량 차원을 갖지 않으므로 그램/킬로그램 변환이
필요 없습니다; 저장된 값은 항상 제곱미터당 쿨롬으로 정규화됩니다.

전속 밀도 `D`(전기 변위라고도 함)는 단위 면적당 전하입니다. **표면 전하 밀도** `σ`는 차원적으로 동일한
양이므로 별도의 그룹이 아니라 바로 이 그룹으로 표현됩니다. `D`는 [유전율](permittivity.md)을 통해
[전기장 세기](electricfieldstrength.md)와 연결됩니다(`D = ε · E`). 1차원 대응물은
[선전하 밀도](linearchargedensity.md)이고, 3차원 대응물은 [전하 밀도](chargedensity.md)입니다.

## 전속 밀도 만들기

이름이 붙은 토큰으로 전속 밀도를 만들거나, 아래의 분해식으로부터 만들 수 있습니다. 이름이 붙은 단위는
값 1의 토큰으로 존재합니다(`of`/`into`와 함께 사용):

| 전속 밀도 | 기호 | 토큰 | C/m² 단위로 1 |
|---|---|---:|---:|
| 제곱미터당 쿨롬 | `C/m²` | `coulombsPerSquareMeter` | 1.0 |
| 제곱센티미터당 쿨롬 | `C/cm²` | `coulombsPerSquareCentimeter` | 1.0e4 |

이름이 붙은 단위는 `KPrefixBuilder`를 통해 SI 접두사를 지원합니다(`micro.coulombsPerSquareMeter`,
`milli.coulombsPerSquareMeter` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electricfluxdensity.*

val d = 5 of micro.coulombsPerSquareMeter   // 대전된 축전기 판
d into micro.coulombsPerSquareMeter         // 5.0
d into coulombsPerSquareMeter               // 5.0e-6
(1 of coulombsPerSquareCentimeter) into coulombsPerSquareMeter // 10000.0
```

## 다중 분해

전속 밀도는 여러 **동등한 분해식**을 통해 도달할 수 있으며, 모두 동일한 값-동등 밀도를 생성합니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `charge / area` | `KElectricFluxDensityUnitInstance` | `D = Q / A`, 면적에 퍼진 전하 |
| `permittivity * electricFieldStrength` | `KElectricFluxDensityUnitInstance` | `D = ε · E` (교환 가능, [유전율](permittivity.md) 참조) |
| `current·time/length²` | `.toElectricFluxDensity()`를 통해 | 네이티브 정규 `A·s·m⁻²` 표현식 |

타입이 지정된 연산자 형식은 전속 밀도를 직접 반환합니다. 완전히 네이티브인 표현식은 일반적인
`KMixedUnitInstance`로 남아 있으며 `toElectricFluxDensity()`로 좁혀집니다(이는 정규 형식만 인식하고
그렇지 않으면 `IllegalStateException`을 발생시킵니다). 모든 경로는 값-동등합니다.

역연산자는 전하, 면적, 전속 밀도를 함께 묶습니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `electricFluxDensity * area` | `KChargeUnitInstance` | `Q = D · A` (교환 가능) |
| `charge / electricFluxDensity` | `KAreaUnitInstance` | `A = Q / D` |
| `electricFluxDensity / electricFieldStrength` | `KPermittivityUnitInstance` | `ε = D / E` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.distance.ares
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.electricfluxdensity.*

// 실제 사례 - 4 m² 축전기 판에 퍼진 20 µC은 5 µC/m²을 만듭니다.
val plate: KAreaUnitInstance = 0.04 of ares            // 4 m²
val d = (20 of micro.coulombs) / plate                 // 5e-6 C/m²

// 네이티브 A·s·m⁻² 표현식으로서의 동일한 전속 밀도:
val raw = 5e-6 of ((amperes pow 1) * (seconds pow 1)) / (meters pow 2)
raw.toElectricFluxDensity() == d                       // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricfluxdensity.*

val s = (1 of coulombsPerSquareMeter) + (1 of coulombsPerSquareCentimeter)  // 10001 C/m²
(1 of coulombsPerSquareCentimeter) > (1 of coulombsPerSquareMeter)          // true
(2 of coulombsPerSquareMeter) * (3 of coulombsPerSquareMeter)               // KMixedUnitInstance
```

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricfluxdensity.*

(1 of coulombsPerSquareCentimeter).toString()   // "10000.0 C/m²" (기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를
보여줍니다. 지수는 유니코드 위첨자(`²`, `⁻²`)를 사용하며, `·`는 곱셈을, `/`는 분수를 나타냅니다. 어떤 양이
분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `C/m²` | `coulombsPerSquareMeter` | 전속 밀도, 기본 단위(이름이 붙은 토큰) |
| `Q / A` | `(20 of micro.coulombs) / plate` | 면적에 걸친 전하로부터의 전속 밀도 |
| `ε · E` | `(1 of vacuumPermittivity) * (1 of voltsPerMeter)` | 유전율과 장 세기로부터의 전속 밀도 |
| `A·s/m²` | `((amperes pow 1) * (seconds pow 1)) / (meters pow 2)` | 전류·시간 / 길이²로서의 전속 밀도(분수 형식) |
| `A·s·m⁻²` | `(amperes pow 1) * (seconds pow 1) * (meters pow -2)` | 순수 곱으로서의 동일한 전속 밀도 |
| `µC/m²` | `micro.coulombsPerSquareMeter` | 접두사가 붙은 전속 밀도(마이크로쿨롬 매 제곱미터) |
