# 자기장 세기

패키지: `org.pcsoft.framework.kunit.magneticfieldstrength`
기본 단위: **암페어 매 미터**(`KMagneticFieldStrengthUnit.BASE == KMagneticFieldStrengthUnit.AMPERE_PER_METER`)

유형: **구성 단위**

자기장 세기(자화장 `H`)는 **구성** 단위로, 조합 `current · length⁻¹`(`A/m`)입니다.
`KMagneticFieldStrengthUnitInstance` 는 두 개의 항 — 지수 `+1` 의 `KElectricCurrentUnit.BASE`(암페어)와
지수 `-1` 의 `KDistanceUnit.BASE`(미터) — 을 감쌉니다. 저장되는 값은 항상 암페어 매 미터로 정규화됩니다.

관련 문서: [전류](ec.md) 와 [거리](../kinematics/distance.md) 가 이 단위의 두 구성 그룹입니다.

## 자기장 세기 만들기

이름 있는 토큰으로 만들거나 분해식(아래 참조)에서 만들 수 있습니다. 이름 있는 단위는 값 1의 토큰으로
제공됩니다(`of`/`into` 와 함께 사용):

| 자기장 세기 | 기호 | 토큰 | 1 단위의 A/m 값 |
|---|---|---:|---:|
| 암페어 매 미터 | `A/m` | `amperesPerMeter` | 1.0 |
| 에르스텟(CGS-EMU) | `Oe` | `oersteds` | 79.57747154594767 |
| 길버트 매 센티미터 | `Gb/cm` | `gilbertsPerCentimeter` | 79.57747154594767 |
| 암페어턴 매 인치 | `At/in` | `ampereTurnsPerInch` | 39.37007874015748 |

이름 있는 단위는 `KPrefixBuilder` 를 통해 SI 접두어를 지원합니다(`kilo.amperesPerMeter`, `milli.oersteds` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.magneticfieldstrength.*

val h = 470 of amperesPerMeter
h into amperesPerMeter                  // 470.0
h into kilo.amperesPerMeter             // 0.47
(1 of kilo.amperesPerMeter) into amperesPerMeter // 1000.0
```

## 여러 분해

자기장 세기는 여러 **동등한 분해**로 얻을 수 있으며, 모두 값이 같은 자기장 세기를 만듭니다:

| 식 | 결과 타입 | 의미 |
|---|---|---|
| `current / length` | `KMagneticFieldStrengthUnitInstance` | 정의식 `H = I / l` |
| `current·length⁻¹` | `.toMagneticFieldStrength()` 경유 | 네이티브 정규형 `A·m⁻¹` 식 |

타입이 지정된 연산자 형태는 자기장 세기를 직접 반환합니다. 완전한 네이티브 식은 일반적인
`KMixedUnitInstance` 로 남으며 `toMagneticFieldStrength()`(정규형만 인식하고 그렇지 않으면
`IllegalStateException` 발생)로 좁힙니다. 두 경로는 값이 같습니다.

역연산자는 전류, 길이, 자기장 세기를 연결합니다:

| 식 | 결과 타입 | 의미 |
|---|---|---|
| `fieldStrength * length` | `KElectricCurrentUnitInstance` | `I = H · l` |
| `length * fieldStrength` | `KElectricCurrentUnitInstance` | 교환 형태 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.magneticfieldstrength.*

// 실제 예 - 500회 감긴 코일에 2 A 가 흐르고 길이가 0.25 m 인 경우:
// H = N · I / l = 500 · 2 A / 0.25 m = 4000 A/m
val h = (1000 of amperes) / (0.25 of meters)  // KMagneticFieldStrengthUnitInstance, 4000 A/m

// 같은 자기장 세기를 네이티브 A·m⁻¹ 식으로:
val raw = 4000 of (amperes pow 1) / (meters pow 1)
raw.toMagneticFieldStrength() == (4000 of amperesPerMeter)  // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.magneticfieldstrength.*

val s = (100 of amperesPerMeter) + (40 of amperesPerMeter)  // 140 A/m
(100 of amperesPerMeter) > (40 of amperesPerMeter)          // true
(100 of amperesPerMeter) * (40 of amperesPerMeter)          // KMixedUnitInstance (그룹에서 벗어남)
```

## toString 형식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.magneticfieldstrength.*

(470 of amperesPerMeter).toString()     // "470.0 A/m" (기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, KUnit 을 사용한 Kotlin 에서 어떻게 쓰는지 비교합니다. 지수는 유니코드 위첨자(`²`, `³`, `⁻¹`)를 사용하고, `·` 는 곱셈, `/` 는 분수를 나타냅니다. 분수로도 음의 지수 곱으로도 쓸 수 있는 양은 두 가지 동등한 Kotlin 형태를 모두 표기합니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `A/m` | `amperesPerMeter` | 자기장 세기, 기본 단위(이름 있는 토큰) |
| `A/m` | `(amperes pow 1) / (meters pow 1)` | 전류 / 길이 로서의 자기장 세기(분수 형태) |
| `A·m⁻¹` | `(amperes pow 1) * (meters pow -1)` | 같은 자기장 세기를 순수한 곱으로 표현 |
| `kA/m` | `kilo.amperesPerMeter` | 접두어가 붙은 자기장 세기(킬로암페어 매 미터) |
