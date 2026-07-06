# 저장 용량

패키지: `org.pcsoft.framework.kunit.storage`
기준 단위: **바이트** (`KStorageUnit.BASE == KStorageUnit.BYTE`)

저장 용량 그룹은 디지털 데이터 양을 모델링합니다. 이 그룹은 **단순한 1차원** 그룹입니다(거리 그룹처럼
지수별 하위 타입이 없고, 시간 그룹처럼 `Duration` 기반도 아닙니다). `KStorageUnitInstance` 는 단일
`KStorageUnit.BASE`(바이트) 항을 감싸며, 항상 바이트로 정규화되어 저장됩니다.

이 그룹에는 두 가지 특별한 점이 있습니다:

* **값을 축소하는 접두사 없음.** 비트의 분수는 의미 있는 데이터 양이 아니므로, 값을 축소하는 SI 접두사
  (`deci`, `centi`, `milli`, … — 계수 `< 1`)는 **제공되지 않습니다**. `5 milli bytes` 를 작성하면
  런타임 오류가 아니라 **컴파일 오류**가 됩니다. 축소하지 않는 SI 접두사(`deca` 이상)만 존재합니다.
* **이진(IEC) 접두사.** 십진 SI 접두사(`kilo` = 1000) 외에 두 번째 이진 접두사 체계
  (`KStorageBinaryPrefix`: `kibi` = 1024, `mebi` = 1024², …)가 있어, 값이 십진 단계 1000 과 이진 단계
  1024 를 구분할 수 있습니다.

## 단위

| 단위 | 열거형 값 | 기호 | 생성자 | 바이트 환산 |
|---|---|---|---:|---:|
| 바이트 | `KStorageUnit.BYTE` | `B` | `Number.bytes` | 1.0 |
| 비트 | `KStorageUnit.BIT` | `bit` | `Number.bits` | 0.125 |

1 바이트는 8 비트입니다. 두 단위 모두 `valueAs`/`toString` 대상 또는 접두사 `infix` 함수의 `unit` 인수로
사용할 수 있는 bare `val` 별칭(`bytes`, `bits`)이 있습니다.

```kotlin
import org.pcsoft.framework.kunit.storage.*

val size = 5.bytes
size.value                     // 5.0 (normalized to bytes)
size.valueAs(bits)             // 40.0 (read back in bits)
1.bytes.valueAs(bits)          // 8.0
8.bits.valueAs(bytes)          // 1.0
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.storage.*

// + / - : 같은 그룹, 비트와 바이트 간 자동 변환
val a = 1.bytes + 8.bits        // KStorageUnitInstance: 2.0 B
val b = 4.bytes - 16.bits       // KStorageUnitInstance: 2.0 B

// 비교
1.bytes == 8.bits               // true (same normalized amount)
2.bytes > 1.bytes               // true

// * / / 는 혼합 엔진에 위임 (KMixedUnitInstance 대상)
val rate = 1000.bytes.toUnit() / 2.seconds.toUnit() // KMixedUnitInstance: 500 B·s⁻¹
```

### 비교와 동등성

`==`, `!=`, `<`, `<=`, `>`, `>=` 는 두 `KStorageUnitInstance` 값의 정규화된 `value`(바이트)를 비교합니다.
`equals` 는 정규화된 양을 기준으로 하므로 `1.bytes == 8.bits` 입니다.

## `pow` 를 사용한 거듭제곱

infix `pow` 연산자로 값을 정수 거듭제곱합니다(Kotlin 에는 오버로드 가능한 `^` 가 없습니다). 저장 용량
그룹에서 `pow` 는 일반적인 `KMixedUnitInstance` 를 반환합니다(저장 용량에는 차원이 있는 거듭제곱 타입이
없습니다):

```kotlin
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.storage.*

val squared = 2.bytes pow 2     // KMixedUnitInstance: 4.0 B²
```

`pow` 는 명명된 infix 함수이므로 `* / + -` 보다 **약하게** 결합합니다. 혼합 식에서는 괄호를 사용하세요
(`(a * b) pow 2`).

## 십진 SI 접두사

모든 `KStorageUnit` 은 **축소하지 않는** SI 접두사(`deca`, `hecto`, `kilo`, `mega`, `giga`, `tera`,
`peta`, `exa`, `zetta`, `yotta`, `ronna`, `quetta`)와 결합할 수 있습니다. 저장 용량 그룹의 `infix` 생성
함수(직접 `KStorageUnitInstance` 반환)와 `with`(`valueAs`/`toString` 대상용)를 사용합니다. 축소하는
접두사(`deci` 이하)는 저장 용량에 **존재하지 않습니다**.

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.storage.*

val fiveKb = 5 kilo bytes                 // KStorageUnitInstance (== 5000.bytes)
fiveKb.value                              // 5000.0

val big = 3.bytes
big.valueAs(KUnitPrefix.KILO with bytes)  // 0.003 (kB)

// 5 milli bytes                          // 컴파일되지 않음: 축소 접두사는 제공되지 않음
```

## 이진(IEC) 접두사

이진 접두사는 1024 의 거듭제곱이며, 값이 1000(`kilo`)과 1024(`kibi`)를 구분할 수 있게 합니다. `infix`
생성 함수(`kibi`, `mebi`, `gibi`, `tebi`, `pebi`, `exbi`, `zebi`, `yobi`)와 `KStorageBinaryPrefix` +
`with` 를 통한 `valueAs`/`toString` 대상으로 제공됩니다.

```kotlin
import org.pcsoft.framework.kunit.storage.*

(1 kilo bytes).value                                  // 1000.0  (decimal)
(1 kibi bytes).value                                  // 1024.0  (binary)
(1 mega bytes).value                                  // 1_000_000.0
(1 mebi bytes).value                                  // 1_048_576.0

val file = 4 mebi bytes
file.valueAs(KStorageBinaryPrefix.KIBI with bytes)    // 4096.0 (KiB)
file.toString(KStorageBinaryPrefix.MEBI with bytes)   // "4.0 MiB"
```

| 이진 접두사 | 열거형 값 | 기호 | `infix` | 계수 |
|---|---|---|---:|---:|
| Kibi | `KStorageBinaryPrefix.KIBI` | `Ki` | `kibi` | 1024 |
| Mebi | `KStorageBinaryPrefix.MEBI` | `Mi` | `mebi` | 1024² |
| Gibi | `KStorageBinaryPrefix.GIBI` | `Gi` | `gibi` | 1024³ |
| Tebi | `KStorageBinaryPrefix.TEBI` | `Ti` | `tebi` | 1024⁴ |
| Pebi | `KStorageBinaryPrefix.PEBI` | `Pi` | `pebi` | 1024⁵ |
| Exbi | `KStorageBinaryPrefix.EXBI` | `Ei` | `exbi` | 1024⁶ |
| Zebi | `KStorageBinaryPrefix.ZEBI` | `Zi` | `zebi` | 1024⁷ |
| Yobi | `KStorageBinaryPrefix.YOBI` | `Yi` | `yobi` | 1024⁸ |

## 다른 단위와의 혼합

저장 용량 값을 시간과 결합하면 혼합 엔진을 통해 데이터 전송률(`byte·second⁻¹`)이 되며, 다시 분해할 수
있습니다:

```kotlin
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds

val rate = 1000.bytes.toUnit() / 1.seconds.toUnit()   // 1000 B/s
val amount = (rate * 60.seconds.toUnit()).toStorage() // 60000 B
amount.valueAs(KStorageBinaryPrefix.KIBI with bytes)  // ≈ 58.59 (KiB)
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.storage.*

1024.bytes.toString()                                   // "1024.0 B" (base unit representation)
5.bits.toString(bits)                                   // "5.0 bit"
2048.bytes.toString(KStorageBinaryPrefix.KIBI with bytes) // "2.0 KiB"
```
