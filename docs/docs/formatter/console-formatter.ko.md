# 콘솔 포매터

`KConsoleUnitFormatter` 는 **ANSI 지원 터미널** 을 위해 값을 렌더링합니다. 출력 표기법은
[기본 포매터](default-formatter.md) 와 완전히 동일하지만(`"10.8 km/h"`, `"m^2"`, `"m*s^-3*A^-2"`),
각 시각적 요소——숫자, 단위 기호, 연산자, 지수——를 ANSI 색상 시퀀스로 감싸므로 콘솔에서 각 부분이
두드러집니다.

`org.pcsoft.framework.kunit.formatter` 패키지에 있습니다. 기본 포매터와 달리 색상 팔레트를
보유하므로 일반적인(불변, 스레드 안전) `class` 입니다.

## 생성되는 내용

레이아웃은 [기본 포매터](default-formatter.md) 와 **동일** 합니다. `"<숫자> <단위>"` 형식이며,
단일 분수 형식 `a/b` 는 분자 항이 정확히 하나이고 분모 항이 정확히 하나일 때만 사용되고, 그 외에는
부호 있는 지수의 평탄한 곱, 무차원 값은 숫자만 표시됩니다. 유일한 차이는 각 부분이 ANSI SGR 색상으로
감싸이고 리셋 시퀀스 `ESC[0m` 로 닫힌다는 점입니다.

### 색상이 입혀지는 요소

네 가지 시각적 요소는 [`KConsoleColorPalette`](#팔레트) 를 통해 독립적으로 색이 지정됩니다.

| 요소      | 팔레트 필드     | 예시             |
|-----------|-----------------|------------------|
| 숫자      | `numberColor`   | `10.8`           |
| 단위 기호 | `symbolColor`   | `km`, `h`, `m`   |
| 연산자    | `operatorColor` | `*`, `/`         |
| 지수      | `exponentColor` | `^2`, `^-3`      |

색상이 **빈 문자열** 인 요소는 어떤 이스케이프 시퀀스도 없이 출력됩니다(해당 부분은 색이 지정되지
않음). `MONOCHROME` 가 지수에 색을 입히지 않는 것이 바로 이 방식입니다.

## 팔레트

색상은 값 타입 `KConsoleColorPalette` 입니다. 세 가지 팔레트가 미리 정의되어 있습니다.

| 팔레트       | 숫자                       | 기호              | 연산자          | 지수                     |
|--------------|----------------------------|-------------------|-----------------|--------------------------|
| `CLASSIC`    | 청록 `ESC[36m`            | 노랑 `ESC[33m`    | 회색 `ESC[90m`  | 자홍 `ESC[35m`           |
| `VIVID`      | 밝은 초록 굵게 `ESC[92;1m`| 밝은 파랑 `ESC[94m`| 흰색 `ESC[97m`  | 밝은 자홍 `ESC[95m`      |
| `MONOCHROME` | 굵게 `ESC[1m`            | 흐리게 `ESC[2m`   | 흐리게 `ESC[2m` | 색 없음(빈 값)           |

- `CLASSIC` 는 어두운 터미널에서 차분하고 읽기 쉬우며 **기본값** 입니다.
- `VIVID` 는 고대비이며 눈에 띕니다.
- `MONOCHROME` 는 색 없이 밝기만 사용하여 색 표현이 부족한 터미널에 적합합니다.

## 사용법

인자 없이 생성하면 기본 `CLASSIC` 팔레트를 사용하며, 미리 정의된 팔레트나 사용자 정의 팔레트를 넘길
수도 있습니다. 그런 다음 다른 포매터와 마찬가지로 [`format`](formatting.md) 동사(또는 매개변수형
`toString`)에 전달합니다.

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KConsoleColorPalette
import org.pcsoft.framework.kunit.formatter.KConsoleUnitFormatter
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 3 of meters / seconds

// 기본 CLASSIC 팔레트
v.format(kilo.meters / hours, "%.1f", Locale.US, KConsoleUnitFormatter())

// 미리 정의된 팔레트
v.format(kilo.meters / hours, "%.1f", Locale.US, KConsoleUnitFormatter(KConsoleColorPalette.VIVID))

// 대상 없이 기준 단위로 렌더링
(5 of meters).toString(pattern = "%.1f", formatter = KConsoleUnitFormatter(KConsoleColorPalette.MONOCHROME))
```

## 사용자 정의 팔레트 만들기

`KConsoleColorPalette` 는 단순한 데이터 클래스이므로 자신만의 색상 시퀀스를 지정할 수 있습니다. 각
필드는 ANSI **도입 시퀀스**(예: 빨강 `ESC[31m`, 여기서 `ESC` 는 코드 27 의 이스케이프 문자)를
보유하며, 공유 `reset`(기본값 `ESC[0m`)이 색이 지정된 각 부분 뒤에 붙습니다.

```kotlin
import org.pcsoft.framework.kunit.formatter.KConsoleColorPalette
import org.pcsoft.framework.kunit.formatter.KConsoleUnitFormatter

val esc = 27.toChar()   // ANSI 이스케이프 문자 (ESC)
val myPalette = KConsoleColorPalette(
    numberColor = "$esc[31m",   // 빨강
    symbolColor = "$esc[32m",   // 초록
    operatorColor = "$esc[34m", // 파랑
    exponentColor = "$esc[35m", // 자홍
)
val formatter = KConsoleUnitFormatter(myPalette)
```

색뿐 아니라 완전히 다른 표기법을 출력하려면 자신만의
[사용자 정의 포매터](custom-formatters.md) 를 구현하세요.
