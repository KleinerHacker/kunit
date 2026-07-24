# Typst 포매터

`KTypstUnitFormatter` 는 값을 **Typst** 수식으로 렌더링합니다. 기본 구성에서 `3 of meters / seconds` 를 `km/h` 로
읽으면 `$1.5 upright("km")/upright("h")$` 가 됩니다.

`org.pcsoft.framework.kunit.formatter` 패키지에 있으며 불변이고 스레드 안전한 `class` 입니다.

## 생성 결과

`FRACTION` 스타일은 깔끔한 단일 분모 형태를 `a/b` 분수 형식으로 표시합니다(필요 시 분자나 거듭제곱된 분모를
괄호로 묶음). 그 외의 형태(및 `EXPONENT` 스타일 전체)는 곱셈 기호로 연결한 평면 곱을 부호 있는 지수로
표시합니다. 무차원 값은 숫자만 렌더링합니다.

## 구성

`KTypstFormatConfig` 는 값 타입입니다. 프리셋을 선택하거나 직접 구성하세요.

| 옵션             | 값                                        | 기본값     |
|------------------|------------------------------------------|-----------|
| `fractionStyle`  | `FRACTION`, `EXPONENT`                   | `FRACTION`|
| `unitStyle`      | `UPRIGHT` (`upright("km")`), `TEXT` (`"km"`) | `UPRIGHT` |
| `multiplication` | `SPACE` (공백), `DOT` (`dot`), `TIMES` (`times`) | `SPACE` |
| `delimiter`      | `MATH` (`$…$`), `FRAGMENT`               | `MATH`    |

프리셋: `DEFAULT`, `FRAGMENT`(`$…$` 구분자 없음).

## 실제 예제

거리와 시간으로부터 속도(`v = s / t`):

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KTypstUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

v.format(kilo.meters / hours, "%.1f", Locale.US, KTypstUnitFormatter())
// $90.0 upright("km")/upright("h")$
```

가속도(`a = m/s²`)는 거듭제곱된 분모를 괄호로 묶습니다:

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KTypstUnitFormatter())
// $9.81 upright("m")/(upright("s")^2)$
```

완전히 다른 표기법을 출력하려면 [사용자 정의 포매터](custom-formatters.md)를 참조하세요.
