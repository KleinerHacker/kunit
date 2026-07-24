# 그래픽 포매터

`KGraphicalConsoleUnitFormatter` 는 ANSI 지원 터미널을 위해 값을 **여러 줄에 걸쳐 그래픽으로** 렌더링합니다.
분수는 실제 2차원 스택(분자, 가로선, 분모)으로 그려지며 값은 가로선(가운데) 줄에 놓입니다. 지수는 항상 실제
유니코드 위 첨자 숫자로 표시되고, 모든 시각적 역할은 `KGraphicalConsoleColorPalette` 로 색칠됩니다.

`org.pcsoft.framework.kunit.formatter` 패키지에 있으며 불변이고 스레드 안전한 `class` 입니다.

## 생성 결과

깔끔한 단일 분모 형태는 세 줄로 쌓이고, 그 외에는 곱셈 기호로 연결한 위 첨자 지수의 한 줄 곱이 됩니다.
무차원 값은 색칠된 숫자만 표시됩니다. 분자와 분모는 **보이는** 너비(ANSI 색상 시퀀스는 너비에 포함되지 않음)
기준으로 가로선 위에 가운데 정렬됩니다. 가속도 `9.81 m/s²` 는 (색 없이) 다음과 같이 그려집니다:

```
     m
9.81 ──
     s²
```

## 구성

`KGraphicalConsoleFormatConfig` 는 값 타입입니다. `DEFAULT` 프리셋을 선택하거나 직접 구성하세요.

| 옵션              | 값 / 타입                                        | 기본값     |
|-------------------|-------------------------------------------------|------------|
| `palette`         | `KGraphicalConsoleColorPalette` — `CLASSIC`, `VIVID`, `MONOCHROME` | `CLASSIC` |
| `fractionBar`     | `LINE` (`─`), `HEAVY` (`━`), `ASCII` (`-`)      | `LINE`     |
| `multiplication`  | `ASTERISK` (`*`), `MIDDLE_DOT` (`·`), `CROSS` (`×`) | `MIDDLE_DOT` |
| `functionSymbols` | `KGraphicalFunctionSymbols` — `UNICODE`, `ASCII` | `UNICODE`  |

팔레트는 다섯 역할(숫자, 기호, 연산자, 지수, 가로선)을 색칠합니다. 색이 빈 문자열인 역할은 색 없이 남습니다
(`MONOCHROME` 가 지수를 색 없이 두는 방식).

## 실제 예제

터미널에서의 속도와 가속도:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KGraphicalConsoleUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

// 기본 CLASSIC 팔레트(여기서는 색 생략). 배치:
//      km
// 90.0 ──
//      h
println(v.format(kilo.meters / hours, "%.1f", Locale.US, KGraphicalConsoleUnitFormatter()))

// 굵은 가로선
val config = KGraphicalConsoleFormatConfig(fractionBar = KGraphicalFractionBar.HEAVY)
println(v.format(kilo.meters / hours, "%.1f", Locale.US, KGraphicalConsoleUnitFormatter(config)))
```

완전히 다른 표기법을 출력하려면 [사용자 정의 포매터](custom-formatters.md)를 참조하세요.
