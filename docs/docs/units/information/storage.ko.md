# 저장 용량

패키지: `org.pcsoft.framework.kunit.storage`
기본 단위: **바이트** (`KStorageUnit.BASE == KStorageUnit.BYTE`)

유형: **네이티브 단위**

저장 용량 그룹은 디지털 데이터 양을 모델링합니다. 이것은 **단순한 1차원** 그룹입니다(거리 그룹처럼 지수
특화 서브타입도 없고, 시간 그룹처럼 `Duration` 기반도 없음): `KStorageUnitInstance`는 단일 `KStorageUnit.BASE`
(바이트) 항을 감싸며 항상 바이트로 정규화되어 저장됩니다.

이 그룹을 특별하게 만드는 두 가지:

* **축소 접두사 없음.** 비트의 소수는 의미 있는 데이터 양이 아니므로, 축소 SI 접두사(`deci`, `centi`, `milli`
  등 — 계수 `< 1`)는 `bytes`/`bits`에서 **사용할 수 없습니다**. `milli.bytes`를 쓰는 것은 런타임 실패가 아니라
  **컴파일 오류**입니다: `bytes`/`bits` 프로퍼티는 증대 SI 빌더(`KAugmentingPrefixBuilder`)와 이진 빌더에만
  달리며, 축소 빌더에는 결코 달리지 않습니다.
* **이진(IEC) 접두사.** 십진 SI 빌더(`kilo` = 1000) 외에 두 번째 이진 빌더 체계(`kibi` = 1024, `mebi` = 1024²
  등)가 있어, 값이 십진 단계 1000과 이진 단계 1024를 구분할 수 있습니다.

## 단위

| 단위 | Enum 값 | 기호 | 토큰 | 1 단위 (바이트) |
|---|---|---|---:|---:|
| 바이트 | `KStorageUnit.BYTE` | `B` | `bytes` | 1.0 |
| 비트 | `KStorageUnit.BIT` | `bit` | `bits` | 0.125 |

1바이트는 8비트입니다. 각 `토큰`은 값 1의 `KStorageUnitInstance`이며 `of`(생성)와 `into`(읽기)에 사용됩니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*

val size = 5 of bytes
size.value          // 5.0(바이트로 정규화)
size into bits      // 40.0(비트로 다시 읽기)
(1 of bytes) into bits   // 8.0
(8 of bits) into bytes   // 1.0
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds

// + / - : 같은 그룹, 비트와 바이트 간 자동 변환
val a = (1 of bytes) + (8 of bits)   // KStorageUnitInstance: 2.0 B
val b = (4 of bytes) - (16 of bits)  // KStorageUnitInstance: 2.0 B

// 비교
(1 of bytes) == (8 of bits)          // true(정규화된 양이 같음)
(2 of bytes) > (1 of bytes)          // true

// storage / time 은 타입이 지정된 데이터 전송률(데이터 전송률 페이지 참조)
val rate = (1000 of bytes) / (2 of seconds)  // KDataRateUnitInstance: 500 B/s
```

### 비교와 동등성

`==`, `!=`, `<`, `<=`, `>`, `>=`는 두 `KStorageUnitInstance` 값의 정규화된 `value`(바이트)를 비교합니다.
`equals`는 정규화된 양 기준이므로 `(1 of bytes) == (8 of bits)` 입니다.

## `pow`를 통한 거듭제곱

infix `pow` 연산자로 값을 정수 거듭제곱합니다(Kotlin에는 오버로드 가능한 `^`가 없음). 저장 용량 그룹에서 `pow`는
범용 `KMixedUnitInstance`를 반환합니다(저장 용량에는 차원이 있는 거듭제곱 타입이 없음):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.storage.*

val squared = (2 of bytes) pow 2     // KMixedUnitInstance: 4.0 B²
```

## 십진 SI 접두사

모든 저장 용량 단위는 **증대**(1 초과) SI 접두사 빌더(`deca`, `hecto`, `kilo`, `mega`, `giga`, `tera`, `peta`,
`exa`, `zetta`, `yotta`, `ronna`, `quetta`)와 프로퍼티 접근으로 결합할 수 있습니다. 축소 빌더(`deci` 이하)에는
`bytes`/`bits` 프로퍼티가 **없으므로** `milli.bytes`는 컴파일되지 않습니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.*

val fiveKb = 5 of kilo.bytes         // KStorageUnitInstance(== 5000 B)
fiveKb.value                         // 5000.0

(3 of bytes) into kilo.bytes         // 0.003(kB)

// 5 of milli.bytes                  // 컴파일되지 않음: 축소 빌더에 `bytes` 없음
```

## 이진(IEC) 접두사

이진 접두사 빌더는 1024의 거듭제곱이며, 값이 1000(`kilo`)과 1024(`kibi`)를 구분하게 합니다: `kibi`, `mebi`,
`gibi`, `tebi`, `pebi`, `exbi`, `zebi`, `yobi`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.*

(1 of kilo.bytes).value   // 1000.0     (십진)
(1 of kibi.bytes).value   // 1024.0     (이진)
(1 of mega.bytes).value   // 1_000_000.0
(1 of mebi.bytes).value   // 1_048_576.0

val file = 4 of mebi.bytes
file into kibi.bytes      // 4096.0(KiB)
```

| 이진 빌더 | 기호 | 1 단위 (바이트) |
|---|---|---:|
| `kibi` | `Ki` | 1024 |
| `mebi` | `Mi` | 1024² |
| `gibi` | `Gi` | 1024³ |
| `tebi` | `Ti` | 1024⁴ |
| `pebi` | `Pi` | 1024⁵ |
| `exbi` | `Ei` | 1024⁶ |
| `zebi` | `Zi` | 1024⁷ |
| `yobi` | `Yi` | 1024⁸ |

## 다른 단위와의 혼합

저장 용량 값을 시간과 결합하면 데이터 전송률(`byte·second⁻¹`)이 되며, 다시 분해할 수 있습니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds

val rate = (1000 of bytes) / (1 of seconds)  // 1000 B/s(타입이 지정된 KDataRateUnitInstance)
val amount = rate * (60 of seconds)          // 60000 B(타입이 지정된 KStorageUnitInstance)
amount into kibi.bytes                        // ≈ 58.59(KiB)
```

## toString 형식화

기본 단위의 `toString()`만 존재합니다. 특정 단위는 `into`로 형식화합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*

(1024 of bytes).toString()               // "1024.0 B"(기본 단위 표현)
"${(5 of bits) into bits} bit"           // "5.0 bit"
"${(2048 of bytes) into kibi.bytes} KiB" // "2.0 KiB"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자(`²`, `³`, `⁻¹`)로 표기하며, `·`는 곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `B` | `bytes` | 데이터량, 기본 단위(바이트) |
| `bit` | `bits` | 비트(`1 B = 8 bit`) |
| `kB` | `kilo.bytes` | 십진 접두사가 붙은 바이트(1000 B) |
| `KiB` | `kibi.bytes` | 이진 접두사가 붙은 바이트(1024 B) |
