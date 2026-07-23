# 사용자 정의 포매터

[`format`](formatting.md) 동사와 매개변수가 있는 `toString` 은 교체 가능한 `KUnitFormatter` 를 통해 텍스트를
렌더링합니다. 제공되는 `KDefaultUnitFormatter` 는 `"10.8 km/h"` 같은 평문을 생성하지만, 완전히 사용자 정의된
렌더링을 끼워 넣을 수 있습니다. 예를 들어 그래픽 수식 렌더러용 **LaTeX** 나 **MathML**, HTML, 임의의 도메인
특화 표기 등입니다. 이로써 kunit 을 문자열을 조판된 수식으로 바꾸는 서드파티 프레임워크로 쉽게 확장할 수
있습니다.

## 계약

포매터는 필요한 모든 것을 단일 `KUnitFormatContext` 로 받아 완성된 문자열을 반환합니다.

```kotlin
interface KUnitFormatter {
    fun format(context: KUnitFormatContext): String
}

data class KUnitFormatContext(
    val value: Double,            // 대상 단위로 이미 변환된 숫자
    val units: List<KUnitTerm>,   // 대상 차원의 항(접두사/지수 표시 메타데이터 포함)
    val pattern: String? = null,  // 숫자용 선택적 java.util.Formatter 패턴
    val locale: Locale = Locale.getDefault(),
)
```

모든 것이 **하나의** 컨텍스트 객체로 전달되므로, 인터페이스는 구현을 깨지 않고 가산적으로 확장(새 필드는 기본값
보유)할 수 있습니다. 흔한 구성 요소를 위한 두 가지 재사용 도우미가 있습니다.

- `KUnitFormatContext.renderValue()` — 숫자를 렌더링합니다. `pattern` 이 `null` 이면 `Double.toString()`,
  아니면 `String.format(locale, pattern, value)`.
- `KUnitTerm.displaySymbol` — 항이 작성된 그대로의 기호(`"km"`, `"h"`). 표시 메타데이터를 존중하며, 없으면
  그룹 기본 기호로 대체합니다.

항의 `exponent` 는 거듭제곱을 나타냅니다(양수 = 분자, 음수 = 분모). 지수를 어떻게 렌더링할지는 포매터가
결정합니다.

## 단계별: LaTeX 포매터

다음 포매터는 분자·분모 항으로부터 `\frac{...}{...}` 를 렌더링하며, 각 단위 기호에 `\mathrm{...}` 를 사용합니다.

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.*

object LatexFormatter : KUnitFormatter {
    override fun format(context: KUnitFormatContext): String {
        // 1. 분자(지수 > 0)와 분모(지수 < 0)로 분리
        val (numerator, denominator) = context.units.partition { it.exponent > 0 }

        // 2. 항 하나를 렌더링. 예: \mathrm{km} 또는 \mathrm{s}^{2}
        fun render(terms: List<KUnitTerm>) = terms.joinToString(" ") { term ->
            val magnitude = kotlin.math.abs(term.exponent)
            val base = "\\mathrm{${term.displaySymbol}}"      // 표시 메타데이터 사용
            if (magnitude == 1) base else "$base^{$magnitude}"
        }

        // 3. 도우미로 숫자 렌더링(패턴 + 로케일 존중)
        val value = context.renderValue()

        // 4. 조립
        if (denominator.isEmpty()) return "$value\\,${render(numerator)}".trim()
        return "$value\\,\\frac{${render(numerator)}}{${render(denominator)}}"
    }
}
```

## 사용

포매터를 명시적으로 전달합니다. 요청하지 않는 한 기본 동작은 결코 바뀌지 않습니다.

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 3 of meters / seconds

// 사용자 정의 포매터로 대상 단위로 서식 지정
v.format(kilo.meters / hours, "%.1f", Locale.US, LatexFormatter)
// "10.8\,\frac{\mathrm{km}}{\mathrm{h}}"

// 또는 대상 없이 기본 단위 렌더링
(5 of meters).toString(pattern = null, formatter = LatexFormatter)
// "5.0\,\mathrm{m}"
```

## 참고

- 포매터는 **상태가 없도록**, 따라서 스레드 안전하게 유지하세요. 제공되는 `KDefaultUnitFormatter` 는 평범한
  `object` 이며 위의 `LatexFormatter` 도 그렇습니다.
- `KUnitFormatContext` 는 대상 단위로 **이미 변환된** 값을 받으므로, 포매터는 단위 변환을 직접 수행하지
  않습니다. 렌더링만 합니다.
- `units` 항은 표시 메타데이터(`KUnitTerm.display`)를 지닙니다. 접두사가 붙거나 대체 단위(`km`, `mi`, `KiB`)가
  올바르게 렌더링되도록 기호는 항상 `displaySymbol` 로 읽으세요.
