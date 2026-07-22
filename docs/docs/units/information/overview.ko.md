# 정보 기술 — 개요

패키지: `org.pcsoft.framework.kunit.storage`, `…datarate`

정보 기술은 **디지털 데이터 양**과 그것이 얼마나 빠르게 이동하는지를 다룹니다. KUnit은 저장된 양을
**네이티브** 기본량(스토리지, 바이트 단위)으로, 처리량을 그것으로부터 **구성된** 양(데이터 전송률 =
스토리지 ÷ 시간)으로 모델링하므로, "이 다운로드는 얼마나 걸릴까?"라는 일상적인 질문이 타입이 지정된
식이 됩니다.

## 이 주제의 단위

| 단위 | 유형 | 기준 단위 | 페이지 |
|---|---|---|---|
| 스토리지 | 네이티브 | 바이트(`B`) | [저장 용량](storage.md) |
| 데이터 전송률 | 구성 | 바이트 매 초(`B/s`) | [데이터 전송률](datarate.md) |

두 그룹은 동일한 접두사 정책을 공유합니다: **감소 접두사 없음**(비트의 소수 부분은 무의미), 그리고
10진 SI 접두사(`kilo` = 1000)에 더해 두 번째 **바이너리(IEC)** 계열(`kibi` = 1024).

## 양들의 관계

| 식 | 결과 | 공식 |
|---|---|---|
| `storage / time` | 데이터 전송률 | `r = 양 / t` |
| `data rate * time` | 스토리지 | `양 = r · t` |
| `time * data rate` | 스토리지 | `양 = r · t`(교환 가능) |
| `storage / data rate` | 시간 | `t = 양 / r` |

## 실전 예제 — 다운로드 시간

**500 MB** 파일을 **10 MB/s** 회선으로 다운로드합니다. 시간은 `t = 양 / 전송률`이며, 전송률에 그 시간을
곱하면 양 `양 = r · t`를 재현합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.datarate.*

val amount = 500 of mega.bytes
val rate   = 10 of mega.bytes / seconds        // KDataRateUnitInstance, 10 MB/s

val time = amount / rate                        // KTimeUnitInstance
time into seconds                               // 50.0 (s)

val transferred = rate * (50 of seconds)        // KStorageUnitInstance
transferred into mega.bytes                     // 500.0 (MB)
```

## 실전 예제 — 10진 vs 바이너리

동일한 수치의 양이 10진(`kB`)과 바이너리(`KiB`) 템플릿에서 다르게 읽힙니다 — 1000 대 1024:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.*

val size = 4096 of bytes
size into kilo.bytes    // 4.096 (kB, 10진 1000)
size into kibi.bytes    // 4.0   (KiB, 바이너리 1024)
```

## 값 출력(`toString`)

`toString()`은 값을 해당 그룹의 **기준 단위**(값 + 기호)로 출력합니다. 다른 단위는 문자열 템플릿 안에서
`into`로 읽고 기호를 직접 붙이세요:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

val r = (10 of bytes) / (1 of seconds)   // KDataRateUnitInstance
r.toString()                             // "10.0 B/s" (기준 단위)
"${(4096 of bytes / seconds) into (kibi.bytes / seconds)} KiB/s" // "4.0 KiB/s"
```

## 표기법

아래 표는 이 분야의 핵심 관계를 수학 표기와 KUnit의 Kotlin 표기로 대비합니다. 지수는 유니코드 위 첨자
(`⁻¹`), `·`는 곱셈, `/`는 분수를 나타냅니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `r = 양 / t` | `(500 of mega.bytes) / (50 of seconds)` | 양÷시간에서 데이터 전송률 |
| `양 = r · t` | `rate * (50 of seconds)` | 전송률×시간에서 양 |
| `t = 양 / r` | `amount / rate` | 양÷전송률에서 시간 |
| `1 kB = 1000 B` | `kilo.bytes` | 10진 접두사 바이트 |
| `1 KiB = 1024 B` | `kibi.bytes` | 바이너리 접두사 바이트 |

## 다음에 볼 것

* [저장 용량](storage.md) — 네이티브 바이트 그룹, 10진 및 바이너리 접두사.
* [데이터 전송률](datarate.md) — 스토리지 ÷ 시간과 스토리지↔시간↔전송률 연산자.
