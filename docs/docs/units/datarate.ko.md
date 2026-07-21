# 데이터 전송률

패키지: `org.pcsoft.framework.kunit.datarate`
기본 단위: **초당 바이트** (`KDataRateUnit.BASE == KDataRateUnit.BYTES_PER_SECOND`)

데이터 전송률은 **구성된** 단위입니다([속도](speed.md) 다음의 두 번째): 단일한 "실제" 양이 아니라
`storage · time⁻¹`(`B/s`)이라는 합성입니다. 따라서 `KDataRateUnitInstance`는 정확히 두 개의 항 — 지수 `+1`의
`KStorageUnit.BASE`(바이트)와 지수 `-1`의 `KTimeUnit.BASE`(초) — 로 이루어진 `KMixedUnitInstance`를 감쌉니다.
값은 어떤 단위나 저장 용량/시간 조합으로 생성되었든 관계없이 항상 초당 바이트로 정규화되어 저장됩니다.

## 데이터 전송률 만들기

데이터 전송률은 **저장 용량/시간 표현식**으로 만듭니다. 예: `100 of bytes / seconds`,
`5 of mega.bytes / seconds`, `10 of kibi.bytes / seconds` — 각각 `KDataRateUnitInstance`를 생성합니다. 임의의
저장 용량/시간 템플릿으로 다시 읽습니다(`r into (bits / seconds)`). `bytesPerSecond` 같은 철자로 쓴 복합 토큰은
의도적으로 **없습니다**(그것들은 정확히 `bytes / seconds` 입니다).

기본 단위: 저장 용량 그룹과 일관되게 초당 *바이트*입니다. 네트워크에서 흔한 bit/s(`bps`)는 `0.125 B/s`이며,
"초당 메가비트"는 `1 of mega.bits / seconds` 입니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

val r = 100 of bytes / seconds
r.value                  // 100.0(B/s로 정규화)
r into (bits / seconds)  // 800.0(bit/s로 다시 읽기)
```

## 핵심 단위(저장 용량 & 시간)로 계산하기

데이터 전송률*이란* 저장 용량을 시간으로 나눈 것입니다. 세 가지 양 — 저장 용량, 시간, 데이터 전송률 — 사이를
단순한 `*`와 `/`로 오갈 수 있으며, 각 결과는 **강하게 타입이 지정**됩니다.

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `storage / time` | `KDataRateUnitInstance` | 전송률 = 양 / 시간 |
| `data rate * time` | `KStorageUnitInstance` | 양 = 전송률 × 시간 |
| `time * data rate` | `KStorageUnitInstance` | 양(교환 법칙) |
| `storage / data rate` | `KTimeUnitInstance` | 시간 = 양 / 전송률 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.datarate.*

// --- 핵심 단위 -> 데이터 전송률 --------------------------------------------
val r = (100 of bytes) / (10 of seconds)   // KDataRateUnitInstance (.toDataRate() 불필요!)
r.value                  // 10.0(B/s)
r into (bits / seconds)  // 80.0

// 접두사가 붙은 분자, 괄호 없이:
val download = 5 of mega.bytes / seconds   // KDataRateUnitInstance(5 MB/s)

// --- 데이터 전송률 -> 저장 용량(시간을 곱함) --------------------------
val amount = r * (60 of seconds)   // KStorageUnitInstance
amount into bytes     // 600.0
amount into bits      // 4800.0
(60 of seconds) * r   // 같은 결과(교환 법칙)

// --- 데이터 전송률 -> 시간(저장 용량을 그것으로 나눔) ------------------
val time = (600 of bytes) / r      // KTimeUnitInstance
time into minutes     // 1.0
```

!!! warning "*순수한* 저장 용량 / 시간 형태만 데이터 전송률입니다"
    `KMixedUnitInstance.toDataRate()`는 정확히 하나의 지수 `+1` 저장 용량 항과 하나의 지수 `-1` 시간 항을
    요구합니다. `B²`(저장 용량 제곱), `B·s⁻²`, `B·s` 형태는 데이터 전송률이 아닙니다 — 변환은
    `IllegalStateException`을 던집니다. 마찬가지로 `storage + data rate`(서로 다른 차원)는 컴파일 오류입니다.

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

// + / - : 같은 그룹, 바이트 기반과 비트 기반 전송률 간 자동 변환
val a = (1 of bytes / seconds) + (8 of bits / seconds)   // KDataRateUnitInstance, 2 B/s
val b = (2 of bytes / seconds) - (8 of bits / seconds)   // 1 B/s

// 비교(정규화된 B/s 값 기준)
(1 of bytes / seconds) > (4 of bits / seconds)           // true
(1 of bytes / seconds) == (8 of bits / seconds)          // true

// 두 데이터 전송률 간의 * / / 는 KMixedUnitInstance로 탈출(더 이상 순수한 전송률이 아님)
val squared = (10 of bytes / seconds) * (2 of bytes / seconds) // KMixedUnitInstance, [B^2, s^-2]
```

## SI 및 이진(IEC) 접두사

데이터 전송률 그룹은 [저장 용량](storage.md) 그룹의 접두사 정책을 반영합니다(그 분자가 저장 용량이기 때문에):
분자는 **증대** SI 빌더(`kilo`, `mega` 등) 또는 **이진** 빌더(`kibi`, `mebi` 등)를 사용합니다. 축소 빌더에는
`bytes`/`bits` 프로퍼티가 없으므로 `milli.bytes / seconds`는 컴파일되지 않습니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

// 십진 vs 이진: 1000(kilo)!= 1024(kibi)
(1 of kilo.bytes / seconds).value // 1000.0
(1 of kibi.bytes / seconds).value // 1024.0

// 저장 용량/시간 템플릿으로 값을 다시 읽기
val r = 4096 of bytes / seconds
r into (kilo.bytes / seconds)  // 4.096(kB/s)
r into (kibi.bytes / seconds)  // 4.0  (KiB/s)
```

## toString 형식화

기본 단위의 `toString()`만 존재합니다. 특정 단위는 `into`로 형식화합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.storage.kibi
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

(10 of bytes / seconds).toString()  // "10.0 B/s"(기본 단위)
"${(4096 of bytes / seconds) into (kibi.bytes / seconds)} KiB/s" // "4.0 KiB/s"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자(`²`, `³`, `⁻¹`)로 표기하며, `·`는 곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `B/s` | `bytes / seconds` | 데이터 전송률, 기본 단위(초당 바이트) — 분수 형식 |
| `B·s⁻¹` | `bytes * (seconds pow -1)` | 같은 전송률을 음의 지수 곱으로 표현 |
| `bit/s` | `bits / seconds` | 초당 비트 |
| `MB/s` | `mega.bytes / seconds` | 초당 메가바이트 |
| `100 B / 10 s` | `(100 of bytes) / (10 of seconds)` | 저장 용량 ÷ 시간으로 생성 |
