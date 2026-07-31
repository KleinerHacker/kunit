# 부피 유량 (Volumetric Flow)

패키지: `org.pcsoft.framework.kunit.kinematic.volumeflow`
기본 단위: **초당 세제곱미터** (`KVolumeFlowUnit.BASE == KVolumeFlowUnit.CUBIC_METER_PER_SECOND`)

유형: **구성된 단위**

부피 유량 (체적 유량률)은 단위 시간당 한 단면을 통과하는 부피를 나타냅니다:
`distance³ · time⁻¹` (`m³/s`). `KVolumeFlowUnitInstance`는 정확히 두 항으로 이루어진 `KMixedUnitInstance`를 감쌉니다 — 지수 `+3`의
`KDistanceUnit.BASE`(미터)와 지수 `-1`의 `KTimeUnit.BASE`(초)입니다. 값은 어떤 단위나 부피/시간 조합으로 생성되었든 관계없이 항상 초당 세제곱미터로 정규화되어 저장됩니다.

에너지나 전력과 달리 부피 유량에는 질량 차원이 **없으므로**, 저장된 값이 곧 `m³/s`의 측정값입니다 — 그램/킬로그램 사이의 다리는 관여하지 않습니다.

## 이름이 붙은 단위

| 단위              | 기호    |                   토큰 |            m³/s로 1 |
|-------------------|---------|-----------------------:|--------------------:|
| 초당 세제곱미터   | `m³/s`  | `cubicMetersPerSecond` |                 1.0 |
| 시간당 세제곱미터 | `m³/h`  |   `cubicMetersPerHour` |   1/3600 ≈ 2.778e-4 |
| 초당 리터         | `l/s`   |      `litersPerSecond` |               0.001 |
| 분당 리터         | `l/min` |      `litersPerMinute` | 0.001/60 ≈ 1.667e-5 |
| 분당 US 갤런      | `gpm`   |   `usGallonsPerMinute` |          ≈ 6.309e-5 |

이들 모두 전체 SI 접두사 범위를 지원합니다 (`milli.litersPerSecond`, `kilo.cubicMetersPerHour` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

val q = 5 of litersPerSecond
q.value                       // 0.005 (m³/s로 정규화)
q into litersPerMinute        // 300.0
q into cubicMetersPerHour     // 18.0
q into usGallonsPerMinute     // ≈ 79.25
(250 of milli.litersPerSecond) into litersPerSecond // 0.25
```

## 실전 예제: 빗물 탱크 채우기

정원용 펌프가 300 l/min을 5 m³ 탱크에 공급합니다. 탱크가 가득 차는 데 얼마나 걸리며, 펌프 데이터시트가 사용하는 단위로는 유량이 얼마인가요?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

val pump = 300 of litersPerMinute
val tank = 5000 of liters

val fillTime = tank / pump          // KTimeUnitInstance
fillTime into minutes               // ≈ 16.67분

pump into cubicMetersPerHour        // 18.0 m³/h (데이터시트 단위)
pump into usGallonsPerMinute        // ≈ 79.25 gpm

// 반대 방향: 15분 동안 얼마나 많은 물이 공급되는가?
val volume = pump * (15 of minutes) // KVolumeUnitInstance
volume into liters                  // 4500.0
```

## 핵심 단위 (부피 & 시간)로 계산하기

| 표현식                | 결과 타입                 | 의미               |
|-----------------------|---------------------------|--------------------|
| `volume / time`       | `KVolumeFlowUnitInstance` | 유량 = 부피 / 시간 |
| `volumeFlow * time`   | `KVolumeUnitInstance`     | 부피 = 유량 × 시간 |
| `time * volumeFlow`   | `KVolumeUnitInstance`     | 부피(교환 법칙)    |
| `volume / volumeFlow` | `KTimeUnitInstance`       | 시간 = 부피 / 유량 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

val q = (600 of liters) / (2 of minutes)  // KVolumeFlowUnitInstance
q into cubicMetersPerSecond               // 0.005

val v = q * (60 of seconds)               // KVolumeUnitInstance
v into liters                             // 300.0

val t = (600 of liters) / q               // KTimeUnitInstance
t into minutes                            // 2.0
```

## 분해

부피 유량은 두 가지 방식으로 도달할 수 있으며, 둘 다 동일한 타입이 지정된 값-동등 인스턴스를 생성합니다.

| 분해                 | 형식                               | 결과                           |
|----------------------|------------------------------------|--------------------------------|
| `volume / time`      | 타입이 지정된 연산자               | `KVolumeFlowUnitInstance` 직접 |
| `distance³ · time⁻¹` | 네이티브 표현식 + `toVolumeFlow()` | `KVolumeFlowUnitInstance`      |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

// 타입이 지정된 연산자 형식
val typed = (8000 of liters) / (4 of seconds)

// 네이티브 기저 차원 형식 (m³ · s⁻¹), toVolumeFlow()가 인식
val native = (((2 of meters).toUnit() pow 3) / (4 of seconds).toUnit()).toVolumeFlow()

typed == native // true - 둘 다 2.0 m³/s
```

`toVolumeFlow()`는 **오직** 정규 형식 (지수 `+3`의 `KDistanceUnit` 항 하나와 지수 `-1`의 `KTimeUnit` 항 하나)만 인식합니다. 동등한 표현식은 자동으로 이 형식으로
환원됩니다. 잘못된 형태는 조용히 잘못된 값을 반환하는 대신
`IllegalStateException`을 던집니다.

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

// + / - : 같은 그룹, 서로 다른 유량 단위 간 자동 변환
val a = (1 of litersPerSecond) + (60 of litersPerMinute)   // 2 l/s
val b = (1 of litersPerSecond) - (30 of litersPerMinute)   // 0.5 l/s

// 비교(정규화된 m³/s 값 기준)
(1 of litersPerSecond) > (30 of litersPerMinute)   // true
(1 of litersPerSecond) == (60 of litersPerMinute)  // true

// 두 유량 간의 * / / 는 KMixedUnitInstance로 탈출
val squared = (1 of litersPerSecond) * (1 of litersPerSecond) // KMixedUnitInstance, [m^6, s^-2]
```

## toString 형식화

`toString()`은 값을 기본 단위로 출력합니다. 다른 단위는 `into`를 사용하세요:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

(5 of litersPerSecond).toString()                       // "0.005 m³/s"
"${(5 of litersPerSecond) into litersPerMinute} l/min"  // "300.0 l/min"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학        | Kotlin                             | 의미                                    |
|-------------|------------------------------------|-----------------------------------------|
| `m³/s`      | `cubicMetersPerSecond`             | 부피 유량, 기본 단위 — 이름이 붙은 토큰 |
| `m³·s⁻¹`    | `(meters pow 3) / seconds`         | 같은 유량을 기저 차원 표현식으로        |
| `l/s`       | `litersPerSecond`                  | 초당 리터                               |
| `l/min`     | `litersPerMinute`                  | 분당 리터                               |
| `m³/h`      | `cubicMetersPerHour`               | 시간당 세제곱미터                       |
| `V / t`     | `(600 of liters) / (2 of minutes)` | 부피 ÷ 시간으로 생성                    |
| `V = q̇ · t` | `q * (60 of seconds)`              | 유량 × 시간에서 부피                    |
| `t = V / q̇` | `(600 of liters) / q`              | 부피 ÷ 유량에서 시간                    |
