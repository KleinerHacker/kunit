# AsciiMath 格式化器

`KAsciiMathUnitFormatter` 将数值渲染为 **AsciiMath**，即 MathJax 的简洁输入语法。默认配置下，
`3 of meters / seconds` 读作 `km/h` 时会得到 `10.8 "km"/"h"`。

它位于 `org.pcsoft.framework.kunit.formatter` 包中，是不可变且线程安全的 `class`。

## 生成内容

`FRACTION` 样式将规整的单分母形式表示为 `a/b` 分数形式（必要时用括号括住分子或带幂的分母）；其余形式
（以及整个 `EXPONENT` 样式）为用乘法符号连接、带符号指数的平铺乘积。无量纲值仅渲染数字。

## 配置

`KAsciiMathFormatConfig` 是值类型；可选择预设或自行构建：

| 选项             | 取值                                      | 默认       |
|------------------|------------------------------------------|-----------|
| `fractionStyle`  | `FRACTION`, `EXPONENT`                   | `FRACTION`|
| `quoting`        | `QUOTED` (`"km"`), `BARE` (`km`)         | `QUOTED`  |
| `multiplication` | `ASTERISK` (`*`), `TIMES` (`xx`), `SPACE` (空格) | `SPACE` |

预设：`DEFAULT`、`PLAIN`（用 `*` 连接的裸符号）。

## 实际示例

由距离和时间求速度（`v = s / t`）：

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KAsciiMathUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

v.format(kilo.meters / hours, "%.1f", Locale.US, KAsciiMathUnitFormatter())
// 90.0 "km"/"h"
```

加速度（`a = m/s²`）会用括号括住带幂的分母：

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KAsciiMathUnitFormatter())
// 9.81 "m"/("s"^2)
```

如需输出完全不同的记法，请参阅[自定义格式化器](custom-formatters.md)。
