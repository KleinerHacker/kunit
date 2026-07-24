# LaTeX 포매터

`KLatexUnitFormatter` 는 값을 **LaTeX** 수식으로 렌더링하여 MathJax, KaTeX 또는 LaTeX 문서에 바로 사용할 수
있습니다. 기본 구성에서 `3 of meters / seconds` 를 `km/h` 로 읽으면 `1.5\,\frac{\mathrm{km}}{\mathrm{h}}` 가 됩니다.

`org.pcsoft.framework.kunit.formatter` 패키지에 있으며 불변이고 스레드 안전한 `class` 입니다.

## 생성 결과

레이아웃은 공통 규칙을 따릅니다. `FRACTION` 스타일에서는 분자가 있고 분모가 정확히 하나인 깔끔한 형태를
`\frac{…}{…}` 로 쌓습니다. 그 외의 형태(및 `INLINE` 스타일 전체)는 곱셈 기호로 연결한 평면 곱을 부호 있는
지수로 표시합니다. 무차원 값은 숫자만 렌더링합니다.

## 구성

`KLatexFormatConfig` 는 값 타입입니다. 프리셋을 선택하거나 직접 구성하세요.

| 옵션             | 값                                        | 기본값     |
|------------------|------------------------------------------|-----------|
| `fractionStyle`  | `FRACTION`, `INLINE`                     | `FRACTION`|
| `unitWrapper`    | `MATHRM`, `TEXT`, `NONE`                 | `MATHRM`  |
| `multiplication` | `CDOT` (`\cdot`), `TIMES` (`\times`), `THIN_SPACE` (`\,`) | `CDOT` |
| `delimiter`      | `DOLLAR` (`$…$`), `PARENTHESES` (`\(…\)`), `NONE` | `NONE` |
| `spacing`        | `THIN` (`\,`), `NORMAL` (공백)            | `THIN`    |

프리셋: `DEFAULT`, `INLINE`(인라인 곱), `PLAIN`(래퍼 없음, 일반 공백).

## 실제 예제

거리와 시간으로부터 속도(`v = s / t`):

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KLatexUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

v.format(kilo.meters / hours, "%.1f", Locale.US, KLatexUnitFormatter())
// 90.0\,\frac{\mathrm{km}}{\mathrm{h}}
```

가속도(`a = m/s²`)는 분수 안에 거듭제곱된 분모를 표시합니다:

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KLatexUnitFormatter())
// 9.81\,\frac{\mathrm{m}}{\mathrm{s}^{2}}
```

완전히 다른 표기법을 출력하려면 [사용자 정의 포매터](custom-formatters.md)를 참조하세요.
