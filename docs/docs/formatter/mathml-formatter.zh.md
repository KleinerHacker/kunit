# MathML 格式化器

`KMathMlUnitFormatter` 将数值渲染为**展示型 MathML**，可由浏览器和 MathJax 原生渲染。默认配置下，
`3 of meters / seconds` 读作 `km/h` 时会得到一个内联 `<math>`，其中 `<mfrac>` 为 `<mi>km</mi>` 除以 `<mi>h</mi>`。

它位于 `org.pcsoft.framework.kunit.formatter` 包中，是不可变且线程安全的 `class`。

## 生成内容

`MFRAC` 样式将规整的单分母形式堆叠为 `<mfrac>`；其余形式（以及整个 `EXPONENT` 样式）为用乘法 `<mo>` 连接、
带符号 `<msup>` 指数的平铺乘积。无量纲值仅渲染 `<mn>`。

## 配置

`KMathMlFormatConfig` 是值类型；可选择预设或自行构建：

| 选项             | 取值                                      | 默认           |
|------------------|------------------------------------------|----------------|
| `fractionStyle`  | `MFRAC`, `EXPONENT`                      | `MFRAC`        |
| `unitTag`        | `MI`, `MTEXT`                            | `MI`           |
| `multiplication` | `MIDDLE_DOT` (`·`), `TIMES` (`×`), `INVISIBLE_TIMES` | `INVISIBLE_TIMES` |
| `wrapper`        | `MATH_INLINE`, `MATH_BLOCK`, `FRAGMENT`  | `MATH_INLINE`  |

预设：`DEFAULT`、`INLINE`（内联 `<msup>` 指数）、`FRAGMENT`（无 `<math>` 根）。

## 实际示例

由距离和时间求速度（`v = s / t`）：

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

加速度（`a = m/s²`）将指数置于分数内的 `<msup>`：

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KMathMlUnitFormatter())
// <math display="inline"><mn>9.81</mn><mo>⁢</mo>
//   <mfrac><mrow><mi>m</mi></mrow><mrow><msup><mi>s</mi><mn>2</mn></msup></mrow></mfrac></math>
```

如需输出完全不同的记法，请参阅[自定义格式化器](custom-formatters.md)。
