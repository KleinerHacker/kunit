# 시간

패키지: `org.pcsoft.framework.kunit.time`
기본 단위: **초** (`KTimeUnit.BASE == KTimeUnit.SECOND`)

`KTimeUnitInstance`는 [`java.time.Duration`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/Duration.html)을
100% 래핑합니다. `Duration`이 유일한 원천(나노초 정밀도)이며, 전체 `Duration` API가 그대로 전달됩니다.
그 위에 다른 모든 "순수" 단위 래퍼와 동일한 표면(`value`/`valueAs`/`+`/`-`/`*`/`/`/`toString`/
`toKMixedUnitInstance`)을 제공하므로, 시간 값은 범용 혼합 단위 엔진에 그대로 연결됩니다(예: `length / time` =
속도). 값은 항상 초로 정규화되어 저장됩니다.

`Duration`은 항상 단순한 지속 시간만 나타내므로, 시간 값은 항상 지수 1입니다 - time² 또는 1/time 래퍼는
없습니다(곱하기/나누기는 길이와 마찬가지로 원시 `KMixedUnitInstance`로 "빠져나갑니다"). 따라서
`KMixedUnitInstance.toKTimeUnit()`은 **지수 1의** 단일 `KTimeUnit` 항만 허용합니다.

## 단위

| 단위 | Enum 값 | 기호 | 생성자 | 1 단위 (초) |
|---|---|---|---:|---:|
| 초 | `KTimeUnit.SECOND` | `s` | `Number.seconds()` | 1.0 |
| 분 | `KTimeUnit.MINUTE` | `min` | `Number.minutes()` | 60.0 |
| 시간 | `KTimeUnit.HOUR` | `h` | `Number.hours()` | 3600.0 |
| 일 | `KTimeUnit.DAY` | `d` | `Number.days()` | 86 400.0 |

물리적 시간 척도만 모델링합니다. 달력 기반 단위(주, 년)는 고정된 물리량이 아니라 달력에 의해 정의되므로
의도적으로 제외했습니다.

위의 모든 단위는 `valueAs`/`toString` 대상이나 접두어 infix 함수의 `unit` 인자로 사용할 수 있는 bare
`val` 별칭을 가지고 있습니다: `seconds`, `minutes`, `hours`, `days`.

밀리초, 마이크로초, 나노초 등의 하위 초 단위는 전용 단위가 **아니며**, `second`에 SI 접두어를 적용해
범용적으로 표현합니다(아래 [SI 접두어](#si) 참조).

```kotlin
import org.pcsoft.framework.kunit.time.*

val t = 2.hours()
t.value                      // 7200.0 (초로 정규화됨)
t.valueAs(KTimeUnit.HOUR)    // 2.0 (시간으로 다시 읽기)
t.valueAs(minutes)           // 120.0
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.time.*

// + / - : 같은 그룹, 서로 다른 시간 단위 간 자동 변환 (정확한 Duration 산술)
val a = 1.hours() + 30.minutes()   // KTimeUnitInstance, 초로 정규화됨 (5400.0)
val b = 2.hours() - 30.minutes()

// 비교
2.hours() > 90.minutes()            // true
1.hours() == 60.minutes()           // true (정규화된 값이 같음)

// * / / : 항상 허용되며, 새로운 지수를 가진 KMixedUnitInstance를 생성
val secondsSquared = 3.seconds() * 4.seconds()   // KMixedUnitInstance: value=12.0, units=[SECOND^2]
val ratio = 10.seconds() / 2.seconds()           // KMixedUnitInstance: value=5.0, 무차원
```

## 비교와 동등성

`==`, `!=`, `<`, `<=`, `>`, `>=`는 두 `KTimeUnitInstance`를 기저 `Duration`(나노초 정밀도)으로 비교합니다.
시간 값은 항상 지수 1이므로, 길이의 면적/부피처럼 지수 불일치 오류는 발생하지 않습니다.

## `java.time.Duration` 래퍼

`KTimeUnitInstance`는 `Duration`에 대한 드롭인 파사드입니다: 래핑된 `Duration`을 가져오고, 기존 것을
래핑하며, 전달된 `Duration` 메서드를 직접 사용할 수 있습니다(`Duration`을 반환하는 메서드는
`KTimeUnitInstance`를 반환하고, 조회 메서드는 그대로 전달됩니다).

```kotlin
import java.time.Duration
import org.pcsoft.framework.kunit.time.*

val t = 90.minutes()
t.toDuration()                       // PT1H30M
Duration.ofMinutes(90).toKTimeUnit() // KTimeUnitInstance, valueAs(HOUR) == 1.5

// 전달된 변경자는 KTimeUnitInstance를 반환
t.plusHours(1).valueAs(KTimeUnit.HOUR) // 2.5
t.negated().isNegative()               // true

// 전달된 조회 메서드는 그대로 통과
t.toHours()      // 1
t.toMinutesPart() // 30
t.dividedBy(30.minutes()) // 3
```

## SI 접두어

모든 `KTimeUnit`은 24개의 SI 접두어(`KUnitPrefix`, 루트 패키지, Quetta/Q부터 Quecto/q까지) 중 어떤
것과도 범용 infix 생성 함수와 `with`(valueAs/toString 대상용)를 사용해 결합할 수 있습니다. 하위 초(및
일 이상) 단위는 이렇게 표현합니다.

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.time.*

// 생성: "5 milli seconds" -> KPrefixBuilder -> KMixedUnitInstance -> KTimeUnitInstance
val fiveMillis = (5 milli seconds).toKMixedUnitInstance().toKTimeUnit()
fiveMillis.value // 0.005 (초)

// 접두어가 붙은 대상을 사용해 값을 다시 읽기
val t = 2.hours()
t.valueAs(KUnitPrefix.MILLI with KTimeUnit.SECOND)  // 7 200 000.0 (ms)
t.toString(KUnitPrefix.MILLI with KTimeUnit.SECOND) // "7200000.0 ms"
```

!!! note "Duration 범위"
    값이 `java.time.Duration`(정수 초는 `Long`으로 저장, 나노초 해상도)으로 지원되기 때문에,
    `KTimeUnitInstance`는 대략 `[1 ns, Long.MAX 초]`(≈ 2920억 년) 범위의 크기만 정확히 표현할 수
    있습니다. 일에 `quetta` 같은 극단적 접두어를 적용하면 이 범위를 초과하고, 나노초 미만 값은 0으로
    반올림됩니다. 범용 `KMixedUnitInstance`/접두어 계층 자체는 `Double` 기반이라 영향을 받지 않으며,
    Duration 기반 래퍼로의 변환만 범위가 제한됩니다.

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.time.*

2.hours().toString()               // "7200.0 s" (기본 단위 표현)
2.hours().toString(KTimeUnit.HOUR) // "2.0 h"
2.hours().toString(minutes)        // "120.0 min"
```

## 다른 단위와의 혼합

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.length.*
import org.pcsoft.framework.kunit.time.*

val speed = 10.meters() / 1.seconds().toKMixedUnitInstance()          // KMixedUnitInstance, units=[METER^1, SECOND^-1]
speed.toString(KUnitPrefix.KILO with KLengthUnit.METER, KTimeUnit.HOUR) // "36.0 km*h^-1"

// 속도에 시간을 다시 곱하면 순수한 길이를 복원
val distance = speed * 2.seconds().toKMixedUnitInstance()
distance.toKLengthUnit().value // 20.0
```
