# 전류 밀도 (Current Density)

패키지: `org.pcsoft.framework.kunit.electric.currentdensity`
기본 단위: **제곱미터당 암페어(ampere per square meter)**
(`KCurrentDensityUnit.BASE == KCurrentDensityUnit.AMPERE_PER_SQUARE_METER`)

유형: **구성 단위(constructed unit)**

전류 밀도는 **구성** 단위입니다: `전류 · 길이⁻²` (`A/m²`)의 조합 — 도체 단면적당 전류입니다.
`KCurrentDensityUnitInstance`는 두 개의 항으로 이루어진 `KMixedUnitInstance`를 감쌉니다 —
`KElectricCurrentUnit.BASE`(암페어)는 `+1`, `KDistanceUnit.BASE`(미터)는 `-2`입니다. 두 구성 요소 모두
해당 그룹의 기본 단위로 저장되므로, 값은 곧바로 A/m² 단위의 값입니다.

## 전류 밀도 만들기

전류 밀도는 **이름이 붙은 토큰이 없으며** 자체 접두사 빌더도 없습니다: 모든 표기는 비율입니다
(`A/m²`, `A/mm²` 등). 표현식으로 만들거나, 타입이 지정된 `current / area` 연산자로 만들고,
그러한 표현식에 대해 `into`로 값을 읽습니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

val crossSection = (2.5 of milli.meters) * (1 of milli.meters)  // 2.5 mm²
val j = (16 of amperes) / crossSection                          // KCurrentDensityUnitInstance

j into (amperes / (meters pow 2))       // 6.4e6
j into (amperes / (milli.meters pow 2)) // 6.4
```

## 다중 분해

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `current / area` | `KCurrentDensityUnitInstance` | 정의 `J = I / A` |
| `current/length²` | `.toCurrentDensity()`를 통해 | 네이티브 정규 `A·m⁻²` 표현식 |

타입이 지정된 연산자 형식은 전류 밀도를 직접 반환합니다. 완전히 네이티브인 표현식은 일반적인
`KMixedUnitInstance`로 남아 있으며 `toCurrentDensity()`로 좁혀집니다(이는 정규 형식만 인식하고 그렇지
않으면 `IllegalStateException`을 발생시킵니다). 두 경로 모두 값-동등합니다.

역연산자는 전류, 면적, 전류 밀도를 함께 묶습니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `currentDensity * area` | `KElectricCurrentUnitInstance` | `I = J · A` (교환 가능) |
| `current / currentDensity` | `KAreaUnitInstance` | `A = I / J` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

// 실제 사례 - 전선 규격: 2.5 mm² 구리 전선을 통과하는 16 A는 6.4 A/mm²입니다.
val j = (16 of amperes) / ((2.5 of milli.meters) * (1 of milli.meters))
j into (amperes / (milli.meters pow 2))     // 6.4

// 특정 밀도에서 주어진 단면적이 운반할 수 있는 전류를 구하도록 풀면:
val i = j * ((4 of milli.meters) * (1 of milli.meters))  // KElectricCurrentUnitInstance, 25.6 A

// 네이티브 A·m⁻² 표현식으로서의 동일한 밀도:
val raw = (16 of amperes).toUnit() / (2.5e-6 of (meters pow 2))
raw.toCurrentDensity() == j                 // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

val a = (3 of amperes) / ((1 of meters) * (1 of meters))
val b = (1 of amperes) / ((1 of meters) * (1 of meters))
(a + b) into (amperes / (meters pow 2))  // 4.0
a > b                                     // true
a * b                                     // KMixedUnitInstance (그룹을 벗어남)
```

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

((5 of amperes) / ((1 of meters) * (1 of meters))).toString()  // "5.0 A/m²" (기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를
보여줍니다. 지수는 유니코드 위첨자(`²`, `⁻²`)를 사용하며, `·`는 곱셈을, `/`는 분수를 나타냅니다. 어떤 양이
분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `A/m²` | `amperes / (meters pow 2)` | 전류 밀도, 기본 단위(분수 형식) |
| `A·m⁻²` | `amperes * (meters pow -2)` | 순수 곱으로서의 동일한 전류 밀도 |
| `I / A` | `(16 of amperes) / crossSection` | 전류와 면적으로부터 계산된 전류 밀도 |
| `A/mm²` | `amperes / (milli.meters pow 2)` | 일반적인 배선 단위의 전류 밀도 |
