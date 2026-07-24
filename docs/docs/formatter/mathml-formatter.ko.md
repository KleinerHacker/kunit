# MathML 포매터

`KMathMlUnitFormatter` 는 값을 **표현용 MathML** 로 렌더링하여 브라우저와 MathJax 에서 기본적으로 표시됩니다.
기본 구성에서 `3 of meters / seconds` 를 `km/h` 로 읽으면 `<mi>km</mi>` 를 `<mi>h</mi>` 로 나눈 `<mfrac>` 를
포함한 인라인 `<math>` 가 됩니다.

`org.pcsoft.framework.kunit.formatter` 패키지에 있으며 불변이고 스레드 안전한 `class` 입니다.

## 생성 결과

`MFRAC` 스타일은 깔끔한 단일 분모 형태를 `<mfrac>` 로 쌓습니다. 그 외의 형태(및 `EXPONENT` 스타일 전체)는
곱셈 `<mo>` 로 연결한 평면 곱을 부호 있는 `<msup>` 지수로 표시합니다. 무차원 값은 `<mn>` 만 렌더링합니다.

## 구성

`KMathMlFormatConfig` 는 값 타입입니다. 프리셋을 선택하거나 직접 구성하세요.

| 옵션             | 값                                        | 기본값         |
|------------------|------------------------------------------|----------------|
| `fractionStyle`  | `MFRAC`, `EXPONENT`                      | `MFRAC`        |
| `unitTag`        | `MI`, `MTEXT`                            | `MI`           |
| `multiplication` | `MIDDLE_DOT` (`·`), `TIMES` (`×`), `INVISIBLE_TIMES` | `INVISIBLE_TIMES` |
| `wrapper`        | `MATH_INLINE`, `MATH_BLOCK`, `FRAGMENT`  | `MATH_INLINE`  |

프리셋: `DEFAULT`, `INLINE`(인라인 `<msup>` 지수), `FRAGMENT`(`<math>` 루트 없음).

## 실제 예제

거리와 시간으로부터 속도(`v = s / t`):

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KMathMlUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

v.format(kilo.meters / hours, "%.1f", Locale.US, KMathMlUnitFormatter())
// <math display="inline"><mn>90.0</mn><mo>⁢</mo>
//   <mfrac><mrow><mi>km</mi></mrow><mrow><mi>h</mi></mrow></mfrac></math>
```

가속도(`a = m/s²`)는 지수를 분수 안의 `<msup>` 로 만듭니다:

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KMathMlUnitFormatter())
// <math display="inline"><mn>9.81</mn><mo>⁢</mo>
//   <mfrac><mrow><mi>m</mi></mrow><mrow><msup><mi>s</mi><mn>2</mn></msup></mrow></mfrac></math>
```

완전히 다른 표기법을 출력하려면 [사용자 정의 포매터](custom-formatters.md)를 참조하세요.
