# 출력 서식

이 페이지는 **포매터** 그룹의 개요입니다. 모든 서식의 진입점인 `format` 동사를 설명합니다. 두 개의 전용
페이지가 더 자세히 다룹니다.

- [기본 포매터](default-formatter.md) — 기본 제공되는 `KDefaultUnitFormatter` 가 단위 부분을 어떻게
  렌더링하는지(기본 표기)를 출력 예시와 함께 설명합니다.
- [사용자 정의 포매터](custom-formatters.md) — 자신만의 렌더링(LaTeX, MathML, HTML 등)을 연결하는 방법.

모든 값은 `toString()` 으로 자신의 **기본 단위**로 출력할 수 있고, [`into`](../mixed-units.md) 로 특정 단위로
**읽어들일** 수 있습니다. 하지만 `into` 는 단위 기호가 없는 순수한 `Double` 만 반환합니다. `format` 동사가 그
간극을 메웁니다. `into` 의 표시용 대응물로, 값**과** 단위 기호를 `String` 으로 반환합니다.

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds

val v = 3 of meters / seconds

v format kilo.meters / hours       // "10.799999999999999 km/h"
```

`into` 와 마찬가지로 `format` 은 먼저 값을 대상 단위로 읽어들인 뒤(동일한 차원 검사와 아핀 변환 수행), 대상의
단위 기호를 덧붙입니다. 대상은 작성된 그대로의 단위를 지니므로, 접두사가 붙거나 대체 단위는 그룹 기본 기호
(`m`, `s`)가 아니라 **자신의** 기호(`km`, `h`, `mi`)로 렌더링됩니다.

## 숫자 서식: 패턴과 로케일

중위 형식은 원시 `Double` 을 렌더링합니다. **숫자 부분**을 반올림하거나 지역화하려면
[`java.util.Formatter`](https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html) 패턴과 선택적
`Locale` 을 받는 `format` 오버로드를 사용하세요.

```kotlin
import java.util.Locale

v.format(kilo.meters / hours, "%.1f")                // "10.8 km/h"
v.format(kilo.meters / hours, "%.1f", Locale.GERMAN) // "10,8 km/h"
```

패턴은 **숫자에만** 영향을 주며 단위 부분은 바뀌지 않습니다. 잘못된 패턴은 `java.util.IllegalFormatException`
을, 호환되지 않는 대상 차원은 (`into` 와 마찬가지로) `IllegalStateException` 을 던집니다.

## 분수 표기 vs 곱 표기

내장 포매터는 단위 부분을 다음과 같이 렌더링합니다.

| 항                              | 렌더링          |
|---------------------------------|-----------------|
| 단일 단위, 지수 1               | `km`            |
| 지수 ≠ 1                       | `m^2`           |
| 분자 + 정확히 하나의 분모       | `km/h`, `m/s^2` |
| 그 외                           | `m*s^-3*A^-2`, `s^-1` |
| 단위 없음(무차원)               | 숫자만          |

## 패턴이 있는 `toString`

인자 없는 `toString()` 은 변경되지 않습니다(기본 단위 렌더링). 추가 오버로드는 동일한 숫자 패턴/로케일을 기본
단위 출력에 적용합니다. 대상이 없는 `format` 동사입니다.

```kotlin
(3 of meters / seconds).toString("%.2f", Locale.US) // "3.00 m/s"
(1500 of meters).toString("%.1f", Locale.US)        // "1500.0 m"
```

## 실제 예제

달리기 페이스를 변환하여 깔끔하게 출력합니다.

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*
import java.util.Locale

val distance = 10 of kilo.meters
val time = 50 of minutes
val speed = distance / time                    // KSpeedUnitInstance

println(speed.format(kilo.meters / hours, "%.1f", Locale.US)) // "12.0 km/h"
println(speed.format(meters / seconds, "%.2f", Locale.US))    // "3.33 m/s"
```

## 사용자 정의 렌더링

단위 부분은 교체 가능한 [`KUnitFormatter`](custom-formatters.md) 가 생성합니다. 제공되는
`KDefaultUnitFormatter` 는 위의 평문을 생성합니다. 정확한 규칙과 출력 예시는 [기본 포매터](default-formatter.md) 를 참고하세요.
완전히 다른 표기(그래픽 수식 렌더러용 LaTeX나 MathML, HTML
등)를 내보내려면 직접 포매터를 구현하여 명시적으로 전달하세요. [사용자 정의 포매터](custom-formatters.md)를
참고하세요.
