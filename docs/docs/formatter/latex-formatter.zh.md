# LaTeX 格式化器

`KLatexUnitFormatter` 将数值渲染为 **LaTeX** 数学式，可直接用于 MathJax、KaTeX 或 LaTeX 文档。
默认配置下，`3 of meters / seconds` 读作 `km/h` 时会得到 `1.5\,\frac{\mathrm{km}}{\mathrm{h}}`。

它位于 `org.pcsoft.framework.kunit.formatter` 包中，是不可变且线程安全的 `class`。

## 生成内容

布局遵循通用规则：在 `FRACTION` 样式下，具有分子且恰好一个分母的规整形式会堆叠为 `\frac{…}{…}`；
其余形式（以及整个 `INLINE` 样式）为用乘法符号连接、带符号指数的平铺乘积。无量纲值仅渲染数字。

## 配置

`KLatexFormatConfig` 是值类型；可选择预设或自行构建：

| 选项             | 取值                                      | 默认       |
|------------------|------------------------------------------|-----------|
| `fractionStyle`  | `FRACTION`, `INLINE`                     | `FRACTION`|
| `unitWrapper`    | `MATHRM`, `TEXT`, `NONE`                 | `MATHRM`  |
| `multiplication` | `CDOT` (`\cdot`), `TIMES` (`\times`), `THIN_SPACE` (`\,`) | `CDOT` |
| `delimiter`      | `DOLLAR` (`$…$`), `PARENTHESES` (`\(…\)`), `NONE` | `NONE` |
| `spacing`        | `THIN` (`\,`), `NORMAL` (空格)            | `THIN`    |

预设：`DEFAULT`、`INLINE`（内联乘积）、`PLAIN`（无包装、常规空格）。

## 实际示例

由距离和时间求速度（`v = s / t`）：

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

加速度（`a = m/s²`）在分数中显示带幂的分母：

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KLatexUnitFormatter())
// 9.81\,\frac{\mathrm{m}}{\mathrm{s}^{2}}
```

如需输出完全不同的记法，请参阅[自定义格式化器](custom-formatters.md)。
