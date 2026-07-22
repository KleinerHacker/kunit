# 시간

패키지: `org.pcsoft.framework.kunit.time`
기본 단위: **초** (`KTimeUnit.BASE == KTimeUnit.SECOND`)

유형: **네이티브 단위**

`KTimeUnitInstance`는 [`java.time.Duration`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/Duration.html)
을 100 % 감싼 래퍼입니다: `Duration`이 유일한 진실의 원천(나노초 정밀도)이며 전체 `Duration` API가
그대로 전달됩니다. 그 위에 다른 모든 "순수" 단위 래퍼와 동일한 표면(`value`/`+`/`-`/`*`/`/`/`toString`/
`toUnit` 및 `of`/`into` 동사)을 제공하므로, 시간 값은 범용 혼합 단위 엔진에 연결됩니다(예:
`length / time` = 속도). 값은 항상 초로 정규화되어 저장됩니다.

`Duration`은 언제나 단순한 지속 시간만 나타내므로 시간 값은 항상 지수 1입니다 — 시간² 이나 1/시간 래퍼는
없습니다(곱셈/나눗셈은 길이와 정확히 같은 방식으로 순수한 `KMixedUnitInstance`로 "탈출"합니다). 따라서
`KMixedUnitInstance.toTime()`은 **지수 1의** 단일 `KTimeUnit` 항만 받아들입니다.

## 단위

| 단위 | Enum 값 | 기호 | 토큰 | 1 단위 (초) |
|---|---|---|---:|---:|
| 초 | `KTimeUnit.SECOND` | `s` | `seconds` | 1.0 |
| 분 | `KTimeUnit.MINUTE` | `min` | `minutes` | 60.0 |
| 시 | `KTimeUnit.HOUR` | `h` | `hours` | 3600.0 |
| 일 | `KTimeUnit.DAY` | `d` | `days` | 86 400.0 |

물리적 시간 척도만 모델링합니다. 달력 기반 단위(주, 년)는 고정된 물리량이 아니라 달력으로 정의되므로
의도적으로 생략되었습니다. 각 `토큰`은 값 1의 `KTimeUnitInstance`이며 `of`(생성)와 `into`(읽기)에 사용됩니다.

초 미만의 척도(밀리초, 마이크로초, 나노초 등)는 전용 단위가 **아닙니다** — `seconds`에 대한 SI 접두사 빌더를
통해 범용적으로 표현됩니다(아래 [SI 접두사](#si) 참조).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.time.*

val t = 2 of hours
t.value          // 7200.0(초로 정규화)
t into hours     // 2.0(시로 다시 읽기)
t into minutes   // 120.0
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.*

// + / - : 같은 그룹, 서로 다른 시간 단위 간 자동 변환(정확한 Duration 산술)
val a = (1 of hours) + (30 of minutes)   // KTimeUnitInstance, 초로 정규화(5400.0)
val b = (2 of hours) - (30 of minutes)

// 비교
(2 of hours) > (90 of minutes)           // true
(1 of hours) == (60 of minutes)          // true(정규화된 값이 같음)

// * / / : 항상 허용되며, 새로운 지수를 가진 KMixedUnitInstance를 생성
val secondsSquared = (3 of seconds) * (4 of seconds)   // KMixedUnitInstance: value=12.0, units=[SECOND^2]
val ratio = (10 of seconds) / (2 of seconds)           // KMixedUnitInstance: value=5.0, 무차원
```

## 비교와 동등성

`==`, `!=`, `<`, `<=`, `>`, `>=`는 두 `KTimeUnitInstance`를 그 기반이 되는 `Duration`(나노초 정밀도)으로
비교합니다. 시간 값은 항상 지수 1이므로 길이의 면적/부피처럼 지수 불일치 오류는 발생하지 않습니다.

## `java.time.Duration` 래퍼

`KTimeUnitInstance`는 `Duration`에 대한 드롭인 파사드입니다: 감싼 `Duration`을 얻거나, 기존 `Duration`을
감싸거나, 전달된 `Duration` 메서드를 직접 사용할 수 있습니다(`Duration`을 반환하는 것은 `KTimeUnitInstance`를
반환하고, 조회 메서드는 그대로 통과합니다).

```kotlin
import java.time.Duration
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.time.*

val t = 90 of minutes
t.toDuration()                  // PT1H30M
Duration.ofMinutes(90).toTime() into hours // 1.5

// 전달된 뮤테이터는 KTimeUnitInstance를 반환
t.plusHours(1) into hours       // 2.5
t.negated().isNegative()        // true

// 전달된 조회 메서드는 그대로 통과
t.toHours()             // 1
t.toMinutesPart()       // 30
t.dividedBy(30 of minutes) // 3
```

## <a name="si"></a>SI 접두사

모든 시간 단위는 24개의 SI 접두사 **빌더**(`kilo`, `milli`, `micro` 등, 루트 패키지) 중 어느 것과도 프로퍼티
접근으로 결합할 수 있으며, `of`/`into`용 값 1 템플릿을 생성합니다. 이것이 초 미만 척도를 표현하는 방법입니다.
`Duration` 기반이 표현 가능한 범위를 제한하므로(아래 참고), 여러 초 단위의 기반에 대한 극단적인 접두사는
표현할 수 없다는 점에 유의하세요:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.time.*

// 생성: "5 of milli.seconds" -> KTimeUnitInstance
val fiveMillis = 5 of milli.seconds
fiveMillis.value // 0.005(초)

// 접두사가 붙은 단위로 값을 다시 읽기
val t = 2 of hours
t into milli.seconds  // 7 200 000.0(ms)
```

!!! note "Duration 범위"
    값이 `java.time.Duration`(정수 초를 `Long`으로 저장, 나노초 해상도)으로 뒷받침되므로,
    `KTimeUnitInstance`는 대략 `[1 ns, Long.MAX 초]`(≈ 2920억 년) 범위 내의 크기만 충실하게 표현할 수 있습니다.
    `quetta`를 일에 적용하는 것과 같은 극단적인 접두사는 이 범위를 초과하며, 나노초 미만 값은 0으로 반올림됩니다.
    범용 `KMixedUnitInstance`/접두사 계층 자체는 `Double` 기반이며 영향을 받지 않습니다 — 범위가 제한되는 것은
    Duration 기반 래퍼로의 변환뿐입니다.

## toString 형식화

기본 단위의 `toString()`만 존재합니다. 특정 단위는 `into`로 형식화합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.time.*

(2 of hours).toString()          // "7200.0 s"(기본 단위 표현)
"${(2 of hours) into hours} h"   // "2.0 h"
"${(2 of hours) into minutes} min" // "120.0 min"
```

## 다른 단위와의 혼합

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

val speed = (10 of meters) / (1 of seconds)  // KSpeedUnitInstance
speed into (kilo.meters / hours)             // 36.0(km/h)

// 속도에 시간을 다시 곱하면 순수한 길이가 복원됨
val distance = speed * (2 of seconds)
distance into meters // 20.0
```

전용 그룹 간 연산자가 **없는** 그룹의 두 순수 단위(예: `(2 of hours) * (5 of bytes)`)는 `.toUnit()` 없이
직접 `KMixedUnitInstance`로 결합됩니다.

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자(`²`, `³`, `⁻¹`)로 표기하며, `·`는 곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `s` | `seconds` | 시간, 기본 단위(초) |
| `min` | `minutes` | 분 |
| `h` | `hours` | 시 |
| `ms` | `milli.seconds` | 접두사가 붙은 시간(밀리초) |
| `s⁻¹` | `seconds pow -1` | 시간의 역수(혼합 단위로 확장) |
