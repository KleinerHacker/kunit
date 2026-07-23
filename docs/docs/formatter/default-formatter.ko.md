# 기본 포매터

`KDefaultUnitFormatter` 는 kunit 이 기본으로 사용하는 포매터입니다. [`format`](formatting.md) 이나
매개변수가 있는 `toString` 을 자신의 포매터를 전달하지 **않고** 호출하면 항상 이 포매터가 결과를
생성합니다 — `"10.8 km/h"` 처럼 사람이 읽을 수 있는 평문 텍스트입니다. 이 페이지는 **무엇을**
**어떻게** 렌더링하는지 출력 예시와 함께 정확히 설명하고, 명시적으로 사용하는 방법도 보여줍니다.

이것은 상태가 없는 `object`(스레드 안전)이며 `org.pcsoft.framework.kunit.formatter` 패키지에 있습니다.

## 생성되는 내용

렌더링된 문자열은 **숫자** 부분과 **단위** 부분의 두 부분으로 구성되며 하나의 공백으로 구분됩니다
(`"<숫자> <단위>"`). 값이 무차원(단위 없음)이면 숫자만 렌더링됩니다.

### 숫자

- 패턴이 없으면 원시 `Double` 이 `Double.toString()` 으로 출력됩니다.
- `java.util.Formatter` 패턴(및 선택적 `Locale`)을 지정하면 숫자는
  `String.format(locale, pattern, value)` 로 렌더링됩니다. 패턴은 **숫자에만** 영향을 주며 단위
  부분에는 영향을 주지 않습니다.

| 호출                                              | 렌더링된 숫자 |
|--------------------------------------------------|-----------------|
| `format(kilo.meters / hours)`                    | `10.799999999999999` |
| `format(kilo.meters / hours, "%.1f")`            | `10.8` |
| `format(kilo.meters / hours, "%.1f", Locale.GERMAN)` | `10,8` |

### 단위 부분

각 단위 항은 자신의 **표기된 기호**(접두사 및 대체 단위 표시 메타데이터를 존중)로 렌더링되므로
`km`, `h`, `mi`, `KiB` 는 그룹 기준 기호가 아니라 그 자체로 렌더링됩니다. 전체 형태는 항에 따라
결정됩니다.

| 항                                       | 렌더링 결과            |
|------------------------------------------|-----------------------|
| 단일 단위, 지수 1                         | `km`                  |
| 지수 ≠ 1                                | `m^2`                 |
| 분자 1개 + 분모가 정확히 1개              | `km/h`, `m/s^2`       |
| 그 외                                     | `m*s^-3*A^-2`, `s^-1` |
| 단위 없음(무차원)                         | 숫자만                 |

단일 분수 형식(`a/b`)은 분자 항이 **정확히 1개**, 분모 항이 **정확히 1개** 일 때만 사용됩니다. 그 외
모든 경우는 명시적(음수일 수 있는) 지수를 가진 평평한 곱으로 렌더링됩니다.

## 출력 예시

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

(1500 of meters).toString()                          // "1500.0 m"
(3 of meters / seconds).format(kilo.meters / hours)  // "10.799999999999999 km/h"
(3 of meters / seconds).format(meters / seconds, "%.2f") // "3.33 m/s"
(9.81 of meters / (seconds pow 2)).format(meters / (seconds pow 2), "%.2f") // "9.81 m/s^2"
```

## 명시적으로 사용하기

기본 포매터는 자동으로 적용되므로 이름을 명시할 일은 거의 없습니다. 그래도 커스텀 포매터와의 대칭성을
위해, 또는 호출 지점에서 선택을 분명히 하기 위해 명시적으로 전달할 수 있습니다.

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KDefaultUnitFormatter
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 3 of meters / seconds

// 명시적 포매터, 기본 호출과 동일한 결과
v.format(kilo.meters / hours, "%.1f", Locale.US, KDefaultUnitFormatter) // "10.8 km/h"

// 타깃 없이 기본 포매터로 기준 단위 렌더링
(5 of meters).toString(pattern = null, formatter = KDefaultUnitFormatter) // "5.0 m"
```

완전히 다른 표기법을 출력하려면 [사용자 정의 포매터](custom-formatters.md) 를 참조하세요.
