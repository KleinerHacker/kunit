# 자속 밀도 (Magnetic Flux Density)

패키지: `org.pcsoft.framework.kunit.electric.magneticfluxdensity`
기본 단위: **테슬라(tesla)** (`KMagneticFluxDensityUnit.BASE == KMagneticFluxDensityUnit.TESLA`)

유형: **구성 단위(constructed unit)**

자속 밀도(자기 유도 `B`)는 **구성** 단위입니다:
`질량 · 시간⁻² · 전류⁻¹` (`kg·s⁻²·A⁻¹`)의 조합입니다. `KMagneticFluxDensityUnitInstance`는 세 개의
항으로 이루어진 `KMixedUnitInstance`를 감쌉니다 — `KMassUnit.BASE`(그램)는 `+1`, `KTimeUnit.BASE`(초)는
`-2`, `KElectricCurrentUnit.BASE`(암페어)는 `-1`입니다. 라이브러리의 질량 구성 요소는 **그램**
(킬로그램이 아님)으로 정규화되어 있으므로, 테슬라는 원시 구성 요소 기준값의 1000배입니다; 저장된 값은
테슬라로 정규화됩니다.

## 자속 밀도 만들기

이름이 붙은 토큰으로 자속 밀도를 만들거나, 아래의 분해식으로부터 만들 수 있습니다. 이름이 붙은 단위는
값 1의 토큰으로 존재합니다(`of`/`into`와 함께 사용):

| 자속 밀도 | 기호 | 토큰 | T 단위로 1 |
|---|---|---:|---:|
| 테슬라 | `T` | `teslas` | 1.0 |
| 제곱미터당 웨버 | `Wb/m²` | `webersPerSquareMeter` | 1.0 |
| 가우스(CGS-EMU) | `G` | `gauss` | 1.0e-4 |
| 감마 | `γ` | `gammas` | 1.0e-9 |

이름이 붙은 단위는 `KPrefixBuilder`를 통해 SI 접두사를 지원합니다(`milli.teslas`, `micro.teslas`,
`nano.teslas` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

val b = 50 of micro.teslas
b into teslas                 // 5.0e-5
b into gauss                  // 0.5
(1 of teslas) into gammas     // 1.0e9
```

## 다중 분해

자속 밀도는 여러 **동등한 분해식**을 통해 도달할 수 있으며, 모두 동일한 값-동등 자속 밀도를 생성합니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `flux / area` | `KMagneticFluxDensityUnitInstance` | 정의 `B = Φ / A` |
| `mass/(time²·current)` | `.toMagneticFluxDensity()`를 통해 | 네이티브 정규 `kg·s⁻²·A⁻¹` 표현식 |

타입이 지정된 연산자 형식은 자속 밀도를 직접 반환합니다. 완전히 네이티브인 표현식은 일반적인
`KMixedUnitInstance`로 남아 있으며 `toMagneticFluxDensity()`로 좁혀집니다(이는 정규 형식만 인식하고
그렇지 않으면 `IllegalStateException`을 발생시킵니다). 두 경로 모두 값-동등합니다.

역연산자는 선속, 자속 밀도, 면적을 함께 묶습니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `fluxDensity * area` | `KMagneticFluxUnitInstance` | `Φ = B · A` |
| `area * fluxDensity` | `KMagneticFluxUnitInstance` | `Φ = A · B` (교환 가능) |
| `flux / fluxDensity` | `KAreaUnitInstance` | `A = Φ / B` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.magneticflux.webers
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

// 실제 사례 - MRI 스캐너: 6 m² 코일을 통과하는 18 Wb의 선속은 3 T의 자기장입니다.
val b = (18 of webers) / ((2 of meters) * (3 of meters))  // KMagneticFluxDensityUnitInstance, 3 T

// 네이티브 kg·s⁻²·A⁻¹ 표현식으로서의 동일한 자속 밀도:
val raw = 3 of (kilo.grams / ((seconds pow 2) * (amperes pow 1)))
raw.toMagneticFluxDensity() == (3 of teslas)              // true

// 2 m² 루프를 통과하는 50 µT의 지구 자기장은 1e-4 Wb의 선속을 만듭니다.
val flux = (50 of micro.teslas) * ((2 of meters) * (1 of meters))  // KMagneticFluxUnitInstance, 1e-4 Wb
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

val s = (3 of teslas) + (1 of teslas)  // 4 T
(3 of teslas) > (1 of teslas)          // true
(3 of teslas) * (1 of teslas)          // KMixedUnitInstance (그룹을 벗어남)
```

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

(3 of teslas).toString()     // "3.0 T" (기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를
보여줍니다. 지수는 유니코드 위첨자(`²`, `³`, `⁻¹`)를 사용하며, `·`는 곱셈을, `/`는 분수를 나타냅니다.
어떤 양이 분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두
나열됩니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `T` | `teslas` | 자속 밀도, 기본 단위(이름이 붙은 토큰, 테슬라) |
| `Wb/m²` | `webersPerSquareMeter` | 면적당 선속으로서의 자속 밀도(이름이 붙은 토큰) |
| `kg/(s²·A)` | `kilo.grams / ((seconds pow 2) * (amperes pow 1))` | 질량 / (시간²·전류)로서의 자속 밀도(분수 형식) |
| `kg·s⁻²·A⁻¹` | `kilo.grams * (seconds pow -2) * (amperes pow -1)` | 순수 곱으로서의 동일한 자속 밀도 |
| `µT` | `micro.teslas` | 접두사가 붙은 자속 밀도(마이크로테슬라) |
