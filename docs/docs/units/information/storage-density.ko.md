# 저장 밀도

패키지: `org.pcsoft.framework.kunit.it.storagedensity`
기본 단위: **제곱미터당 바이트**(`KStorageDensityUnit.BASE == KStorageDensityUnit.BYTES_PER_SQUARE_METER`)

종류: **구성 단위**

저장 밀도는 **구성** 단위입니다. 단일한 "실재하는" 양이 아니라 조합, 즉 `storage · distance⁻²`(`B/m²`)입니다. 따라서 `KStorageDensityUnitInstance` 는 정확히
두 항 — 지수 `+1` 의 `KStorageUnit.BASE`(바이트)와 지수 `-2`
의 `KDistanceUnit.BASE`(미터) — 로 이루어진 `KMixedUnitInstance` 를 감쌉니다. 값은 어떤 단위나 저장/면적 조합으로 생성되었든 항상 제곱미터당 바이트로 정규화되어 저장됩니다.

## 저장 밀도 만들기

저장 밀도는 **저장량 나누기 면적 표현식**으로 만듭니다. 예: `100 of bytes / area`, `5 of mega.bytes / area`. 면적은 임의의 `KAreaUnitInstance`(예:
`(1 of meters) * (1 of meters)`)이므로 모든 SI/이진 접두사와 길이 단위가 자유롭게 결합됩니다. 임의의 저장량-면적 템플릿으로 다시 읽습니다 (`d into (bits / area)`).
철자로 쓴 복합 토큰은 의도적으로 **없습니다**.

기본 단위: 저장 그룹과 일관되게 *바이트* 제곱미터당입니다. "제곱미터당 비트"는 `0.125 B/m²` 입니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)  // 1 m²
val d = 100 of bytes / area
d.value               // 100.0 (B/m² 로 정규화)
d into (bits / area)  // 800.0 (bit/m² 로 다시 읽음)
```

## 실제 예: SSD 다이의 면기록 밀도

플래시 다이는 **100 mm²** 의 면적에 **256 GB** 를 저장합니다. 면저장 밀도는 데이터 양을 면적으로 나눈 것입니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val data = 256 of giga.bytes                       // 256 GB
val side = 10 of milli.meters                      // 10 mm × 10 mm 다이 = 100 mm²
val area = side * side
val density = data / area                          // KStorageDensityUnitInstance
density.value                                       // 2.56e15 (B/m²)
density into (giga.bytes / (side * side))           // 256.0 (100 mm² 당 GB)
```

## 핵심 단위 (저장 및 면적)로 계산하기

저장 밀도는 *바로* 저장량을 면적으로 나눈 것입니다. 세 양 — 저장량, 면적, 저장 밀도 — 사이를 평범한 `*` 와
`/` 로 오갈 수 있습니다. 각 결과는 **강타입**입니다.

| 표현식                      | 결과 타입                     | 의미             |
|-----------------------------|-------------------------------|------------------|
| `storage / area`            | `KStorageDensityUnitInstance` | 밀도 = 양 / 면적 |
| `storage density * area`    | `KStorageUnitInstance`        | 양 = 밀도 × 면적 |
| `area * storage density`    | `KStorageUnitInstance`        | 양(교환법칙)     |
| `storage / storage density` | `KAreaUnitInstance`           | 면적 = 양 / 밀도 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)   // 1 m²

// --- 핵심 단위 -> 저장 밀도 --------------------------------------
val d = (100 of bytes) / area   // KStorageDensityUnitInstance (.toStorageDensity() 불필요!)
d.value               // 100.0 (B/m²)

// --- 저장 밀도 -> 저장(면적을 곱함)-------------------
val amount = d * area           // KStorageUnitInstance
amount into bytes     // 100.0
area * d              // 같은 결과(교환법칙)

// --- 저장 밀도 -> 면적(저장량을 나눔)------------------
val a = (600 of bytes) / d      // KAreaUnitInstance (6 m²)
```

!!! warning "*순수한* 저장 / 면적 형태만이 저장 밀도입니다"
`KMixedUnitInstance.toStorageDensity()` 는 지수 `+1` 의 저장 항 정확히 하나와 지수 `-2` 의 거리 항 정확히 하나를 요구합니다. `B²·m⁻²`, `B·m⁻¹`, `B·m²`
형태는 저장 밀도가 아니며 변환은 `IllegalStateException`
을 던집니다. 마찬가지로 `storage + storage density`(서로 다른 차원)는 컴파일 오류입니다.

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)

// + / - : 같은 그룹, 바이트 및 비트 기반 밀도 간 자동 변환
val a = (1 of bytes / area) + (8 of bits / area)   // KStorageDensityUnitInstance, 2 B/m²
val b = (2 of bytes / area) - (8 of bits / area)   // 1 B/m²

// 비교(정규화된 B/m² 값 기준)
(1 of bytes / area) > (4 of bits / area)           // true
(1 of bytes / area) == (8 of bits / area)          // true

// 두 저장 밀도 사이의 * / / 는 KMixedUnitInstance 로 벗어납니다(더 이상 순수한 밀도가 아님)
val squared = (10 of bytes / area) * (2 of bytes / area) // KMixedUnitInstance, [B^2, m^-4]
```

## SI 및 이진 (IEC) 접두사

저장 밀도 그룹은 [저장](storage.md) 그룹의 접두사 정책을 따릅니다 (분자가 저장량입니다): 분자는 **증가** SI 빌더 (`kilo`, `mega`, …) 또는 **이진** 빌더 (`kibi`,
`mebi`, …)를 사용합니다. 분모 (면적)는 임의의 길이 단위와 접두사를 사용합니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val mm2 = (1 of milli.meters) * (1 of milli.meters)  // 1 mm²
val d = 1 of kilo.bytes / mm2                         // 1 kB/mm²
d into (kilo.bytes / mm2)  // 1.0
```

## toString 서식화

기본 단위 `toString()` 만 존재합니다. 특정 단위는 `into` 또는 `format` 로 서식화합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.it.storage.bytes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)
((1000 of bytes) / area).toString()  // "1000.0 B/m²" (기본 단위)
((1000 of bytes) / area) format (kilo.bytes.toUnit() / area.toUnit()) // "1.0 kB/m^2"
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 그리고 KUnit 을 사용한 Kotlin 에서 어떻게 표기되는지 보여줍니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)를 사용하고, `·` 는 곱셈을,
`/` 는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형태를 모두 나열합니다.

| 수학               | Kotlin                                | 의미                                                |
|--------------------|---------------------------------------|-----------------------------------------------------|
| `B/m²`             | `bytes / area`                        | 저장 밀도, 기본 단위(제곱미터당 바이트) — 분수 형태 |
| `B·m⁻²`            | `bytes * (meters pow -2)`             | 음의 지수 곱으로 표현한 같은 밀도                   |
| `bit/m²`           | `bits / area`                         | 제곱미터당 비트                                     |
| `kB/mm²`           | `kilo.bytes / mm2`                    | 제곱밀리미터당 킬로바이트                           |
| `256 GB / 100 mm²` | `(256 of giga.bytes) / (side * side)` | 저장 ÷ 면적 으로 구성                               |
