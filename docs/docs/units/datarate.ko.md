# 데이터 전송률

패키지: `org.pcsoft.framework.kunit.datarate`
기본 단위: **초당 바이트** (`KDataRateUnit.BASE == KDataRateUnit.BYTES_PER_SECOND`)

데이터 전송률은 ([속도](speed.md)에 이어) 두 번째 **구성된(constructed)** 단위입니다. 단일한 "실제"
물리량이 아니라 `저장 용량 · 시간⁻¹` (`B/s`)의 조합입니다. 따라서 `KDataRateUnitInstance`는 정확히 두
항 - 지수 `+1`의 `KStorageUnit.BASE`(바이트)와 지수 `-1`의 `KTimeUnit.BASE`(초) - 으로 이루어진
`KMixedUnitInstance`를 감쌉니다. 값은 어떤 단위·접두사·저장/시간 조합으로 생성되든 항상 초당 바이트로
정규화되어 저장됩니다.

## 단위

| 단위 | Enum 값 | 기호 | 생성자 | 1 단위 (B/s) |
|---|---|---|---:|---:|
| 초당 바이트 | `KDataRateUnit.BYTES_PER_SECOND` | `B/s` | `Number.bytesPerSecond` | 1.0 |
| 초당 비트 | `KDataRateUnit.BITS_PER_SECOND` | `bit/s` | `Number.bitsPerSecond` | 0.125 |

두 단위 모두 `valueAs`/`toString` 대상이나 접두사 `infix` 함수의 `unit` 인자로 사용할 수 있는 bare `val`
별칭이 있습니다: `bytesPerSecond`, `bitsPerSecond`.

> **바이트 기반 기준.** 기본 단위는 초당 바이트로, 저장 용량 그룹(기준이 바이트)과 일관됩니다. 네트워크
> 에서 흔한 초당 비트 (`bps`)는 `0.125 B/s`입니다. 더 큰 단위(kB/s, MB/s, Mbit/s, KiB/s, …)는 전용
> enum 값이 아니라 아래의 접두사 DSL에서 얻습니다.

```kotlin
import org.pcsoft.framework.kunit.datarate.*

val r = 100.bytesPerSecond
r.value                                     // 100.0 (B/s로 정규화)
r.valueAs(KDataRateUnit.BITS_PER_SECOND)    // 800.0 (bit/s로 되읽기)
r.valueAs(bitsPerSecond)                     // 800.0 (bare 별칭 사용)
```

## 핵심 단위(저장 용량 & 시간)로 계산하기

이것이 바로 구성된 단위의 핵심입니다. 데이터 전송률*은* 저장 용량을 시간으로 나눈 것입니다. KUnit는
저장 용량·시간·데이터 전송률 세 양 사이를 평범한 `*`와 `/`로 오갈 수 있게 하며, 각 결과는 **강하게
타입 지정**됩니다. 원시 `KMixedUnitInstance`를 직접 만들거나 풀 필요가 없습니다.

유효한 네 가지 조합과 결과 타입:

| 식 | 결과 타입 | 의미 |
|---|---|---|
| `storage / time` | `KDataRateUnitInstance` | 전송률 = 용량 / 시간 |
| `data rate * time` | `KStorageUnitInstance` | 용량 = 전송률 × 시간 |
| `time * data rate` | `KStorageUnitInstance` | 용량 (교환 법칙) |
| `storage / data rate` | `KTimeUnitInstance` | 시간 = 용량 / 전송률 |

```kotlin
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.datarate.*

// --- 핵심 단위 -> 데이터 전송률 -----------------------------------------
val r = 100.bytes / 10.seconds            // KDataRateUnitInstance (.toDataRate() 불필요!)
r.value                                     // 10.0 (B/s)
r.valueAs(KDataRateUnit.BITS_PER_SECOND)    // 80.0

// 대입 대상 타입은 아무것도 변환하지 않습니다 - 연산자가 이미 KDataRateUnitInstance를 반환합니다.
val explicit: KDataRateUnitInstance = 100.bytes / 10.seconds

// --- 데이터 전송률 -> 저장 용량(시간을 곱함) ---------------------------
val amount = r * 60.seconds               // KStorageUnitInstance
amount.value                                // 600.0 (B)
amount.valueAs(bytes)                        // 600.0
amount.valueAs(bits)                         // 4800.0 (임의의 저장 단위로 되읽기)
60.seconds * r                            // 동일한 결과 (교환 법칙)

// --- 데이터 전송률 -> 시간(저장 용량을 나눔) ---------------------------
val time = 600.bytes / r                  // KTimeUnitInstance
time.value                                  // 60.0 (s)
time.valueAs(KTimeUnit.MINUTE)              // 1.0
```

!!! warning "*순수한* 저장 용량 / 시간 형태만 데이터 전송률"
    `KMixedUnitInstance.toDataRate()`는 지수 `+1`의 저장 항 하나와 지수 `-1`의 시간 항 하나를 정확히
    요구합니다. `B²`(저장 용량 제곱), `B·s⁻²`, `B·s` 형태는 데이터 전송률이 아니며, 변환은 잘못된 값을
    조용히 반환하는 대신 `IllegalStateException`을 던집니다. 마찬가지로 `storage + data rate`(차원이
    다름)는 컴파일 오류입니다.

## 연산자

```kotlin
import org.pcsoft.framework.kunit.datarate.*

// + / - : 동일 그룹, 서로 다른 데이터 전송률 단위 간 자동 변환
val a = 1.bytesPerSecond + 8.bitsPerSecond   // KDataRateUnitInstance, 2 B/s
val b = 2.bytesPerSecond - 8.bitsPerSecond   // 1 B/s

// 비교 (정규화된 B/s 값 기준)
1.bytesPerSecond > 4.bitsPerSecond           // true  (1 B/s > 0.5 B/s)
1.bytesPerSecond == 8.bitsPerSecond          // true  (같은 정규화 값)

// 두 데이터 전송률 간의 * / / 는 KMixedUnitInstance로 벗어납니다 (더 이상 순수한 전송률이 아님)
val squared = 10.bytesPerSecond * 2.bytesPerSecond // KMixedUnitInstance, units=[B^2, s^-2]
```

## 비교와 동등성

`==`, `!=`, `<`, `<=`, `>`, `>=` 는 두 `KDataRateUnitInstance`의 정규화된 `value`(초당 바이트)를
비교합니다. 데이터 전송률은 항상 같은 차원을 가지므로 지수 검사가 필요 없습니다.

## SI 접두사와 이진(IEC) 접두사

데이터 전송률 그룹은 [저장 용량](storage.md) 그룹의 접두사 정책을 그대로 따릅니다(분자가 저장 용량이므로):

* **줄어들지 않는** 십진 SI 접두사(`deca` 이상, 계수 >= 1)만 제공합니다. 줄어드는 접두사(`deci`,
  `centi`, `milli`, …)는 **존재하지 않습니다** - `5 milli bytesPerSecond`는 런타임 실패가 아니라
  **컴파일 오류**입니다.
* 십진 SI 접두사에 더해 **이진 IEC 접두사**(`kibi`, `mebi`, `gibi`, …, 1024의 거듭제곱,
  `KStorageBinaryPrefix`에서 재사용)를 사용할 수 있어, 전송률이 1000 (`kilo`)과 1024 (`kibi`)를
  구별할 수 있습니다.

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.storage.KStorageBinaryPrefix
import org.pcsoft.framework.kunit.storage.with
import org.pcsoft.framework.kunit.datarate.*

// 생성: "5 mega bytesPerSecond" -> KDataRateUnitInstance (직접, == 5_000_000.bytesPerSecond)
val download = 5 mega bytesPerSecond
download.value // 5000000.0

// 십진 vs 이진: 1000 (kilo) != 1024 (kibi)
(1 kilo bytesPerSecond).value // 1000.0
(1 kibi bytesPerSecond).value // 1024.0

// 스케일된 전체 전송률 대상으로 값 되읽기
val r = 4096.bytesPerSecond
r.valueAs(KUnitPrefix.KILO with bytesPerSecond)              // 4.096  (kB/s)
r.valueAs(KStorageBinaryPrefix.KIBI with bytesPerSecond)     // 4.0    (KiB/s)
```

데이터 전송률을 명시적인 **저장-당-시간 쌍**(두 개의 대상)으로 되읽을 수도 있습니다:

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.storage.KStorageUnit
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.datarate.*

val r = 5000.bytesPerSecond
r.valueAs(KUnitPrefix.KILO with KStorageUnit.BYTE, KTimeUnit.SECOND)   // 5.0 (초당 kB)
r.toString(KUnitPrefix.KILO with KStorageUnit.BYTE, KTimeUnit.SECOND)  // "5.0 kB*s^-1"
```

## toString 형식

```kotlin
import org.pcsoft.framework.kunit.storage.KStorageBinaryPrefix
import org.pcsoft.framework.kunit.storage.with
import org.pcsoft.framework.kunit.datarate.*

10.bytesPerSecond.toString()                                    // "10.0 B/s" (기본 단위)
(100.bytes / 10.seconds).toString(KDataRateUnit.BITS_PER_SECOND) // "80.0 bit/s"
4096.bytesPerSecond.toString(KStorageBinaryPrefix.KIBI with bytesPerSecond) // "4.0 KiB/s"
```
