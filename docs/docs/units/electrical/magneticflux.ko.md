# 자기 선속 (Magnetic Flux)

패키지: `org.pcsoft.framework.kunit.electric.magneticflux`
기본 단위: **웨버(weber)** (`KMagneticFluxUnit.BASE == KMagneticFluxUnit.WEBER`)

유형: **구성 단위(constructed unit)**

자기 선속은 **구성** 단위입니다: `질량 · 길이² · 시간⁻² · 전류⁻¹` (`kg·m²·s⁻²·A⁻¹`)의 조합입니다.
`KMagneticFluxUnitInstance`는 네 개의 항으로 이루어진 `KMixedUnitInstance`를 감쌉니다 — `KMassUnit.BASE`
(그램)는 `+1`, `KDistanceUnit.BASE`(미터)는 `+2`, `KTimeUnit.BASE`(초)는 `-2`,
`KElectricCurrentUnit.BASE`(암페어)는 `-1`입니다. 라이브러리의 질량 구성 요소는 **그램**(킬로그램이 아님)으로
정규화되어 있으므로, 정규 곱은 웨버에 도달하기 위해 1000으로 나뉩니다; 저장된 값은 항상 웨버로 정규화됩니다.

## 자기 선속 만들기

이름이 붙은 토큰으로 선속을 만들거나, 아래의 분해식으로부터 만들 수 있습니다. 이름이 붙은 단위는 값 1의
토큰으로 존재합니다(`of`/`into`와 함께 사용):

| 자기 선속 | 기호 | 토큰 | Wb 단위로 1 |
|---|---|---:|---:|
| 웨버 | `Wb` | `webers` | 1.0 |
| 맥스웰(CGS-EMU) | `Mx` | `maxwells` | 1.0e-8 |
| 유닛 폴 | `pole` | `unitPoles` | 1.2566370614359173e-7 |

이름이 붙은 단위는 `KPrefixBuilder`를 통해 SI 접두사를 지원합니다(`milli.webers`, `micro.webers`,
`kilo.maxwells` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.magneticflux.*

val phi = 20 of milli.webers
phi into milli.webers          // 20.0
phi into webers                // 0.02
(1 of webers) into maxwells    // 1.0e8
```

## 다중 분해

자기 선속은 여러 **동등한 분해식**을 통해 도달할 수 있으며, 모두 동일한 값-동등 선속을 생성합니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `voltage * time` | `KMagneticFluxUnitInstance` | 패러데이의 유도 법칙 `Φ = U · t` (교환 가능) |
| `voltage / frequency` | `KMagneticFluxUnitInstance` | 역시간 형태 (`V/Hz = V·s`) |
| `inductance * current` | `KMagneticFluxUnitInstance` | `Φ = L · I` ([인덕턴스](inductance.md) 참조) |
| `fluxDensity * area` | `KMagneticFluxUnitInstance` | `Φ = B · A` ([자속 밀도](magneticfluxdensity.md) 참조) |
| `mass·length²/(time²·current)` | `.toMagneticFlux()`를 통해 | 네이티브 정규 `kg·m²·s⁻²·A⁻¹` 표현식 |

타입이 지정된 연산자 형식은 선속을 직접 반환합니다. 완전히 네이티브인 표현식은 일반적인
`KMixedUnitInstance`로 남아 있으며 `toMagneticFlux()`로 좁혀집니다(이는 정규 형식만 인식하고 그렇지 않으면
`IllegalStateException`을 발생시킵니다). 모든 경로는 값-동등합니다.

역연산자는 전압, 시간, 선속을 함께 묶습니다:

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `flux / time` | `KVoltageUnitInstance` | 유도 전압 `U = Φ / t` |
| `flux * frequency` | `KVoltageUnitInstance` | 역시간 대응 형태 |
| `flux / voltage` | `KTimeUnitInstance` | `t = Φ / U` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.frequency.hertz
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.electric.magneticflux.*

// 실제 사례 - 점화 코일: 20 mWb의 코어 선속이 4 ms 내에 붕괴되면 5 V가 유도됩니다.
val u = (20 of milli.webers) / (4 of milli.seconds)   // KVoltageUnitInstance, 5 V

// 선속에 대해 풀어낸 유도 법칙:
val phi = (10 of volts) * (0.2 of seconds)            // KMagneticFluxUnitInstance, 2 Wb

// 주파수로부터 동일한 선속, 그리고 네이티브 kg·m²·s⁻²·A⁻¹ 표현식으로:
val fromFrequency = (10 of volts) / (5 of hertz)      // 2 Wb
val raw = 2 of (kilo.grams * (meters pow 2)) / ((seconds pow 2) * (amperes pow 1))
raw.toMagneticFlux() == (2 of webers)                 // true
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticflux.*

val s = (100 of webers) + (40 of webers)  // 140 Wb
(100 of webers) > (40 of webers)          // true
(100 of webers) * (40 of webers)          // KMixedUnitInstance (그룹을 벗어남)
```

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticflux.*

(20 of webers).toString()     // "20.0 Wb" (기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를
보여줍니다. 지수는 유니코드 위첨자(`²`, `⁻¹`)를 사용하며, `·`는 곱셈을, `/`는 분수를 나타냅니다. 어떤 양이
분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두 나열됩니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `Wb` | `webers` | 자기 선속, 기본 단위(이름이 붙은 토큰, 웨버) |
| `V·s` | `(10 of volts) * (0.2 of seconds)` | 전압·시간으로서의 선속(유도 법칙) |
| `kg·m²/(s²·A)` | `(kilo.grams * (meters pow 2)) / ((seconds pow 2) * (amperes pow 1))` | 질량·길이² / (시간²·전류)로서의 선속(분수 형식) |
| `kg·m²·s⁻²·A⁻¹` | `kilo.grams * (meters pow 2) * (seconds pow -2) * (amperes pow -1)` | 순수 곱으로서의 동일한 선속 |
| `mWb` | `milli.webers` | 접두사가 붙은 선속(밀리웨버) |
