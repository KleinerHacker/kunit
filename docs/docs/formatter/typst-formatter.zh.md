# Typst 格式化器

`KTypstUnitFormatter` 将数值渲染为 **Typst** 数学式。默认配置下，`3 of meters / seconds` 读作 `km/h` 时会得到
`$1.5 upright("km")/upright("h")$`。

它位于 `org.pcsoft.framework.kunit.formatter` 包中，是不可变且线程安全的 `class`。

## 生成内容

`FRACTION` 样式将规整的单分母形式表示为 `a/b` 分数形式（必要时用括号括住分子或带幂的分母）；其余形式
（以及整个 `EXPONENT` 样式）为用乘法符号连接、带符号指数的平铺乘积。无量纲值仅渲染数字。

## 配置

`KTypstFormatConfig` 是值类型；可选择预设或自行构建：

| 选项             | 取值                                      | 默认       |
|------------------|------------------------------------------|-----------|
| `fractionStyle`  | `FRACTION`, `EXPONENT`                   | `FRACTION`|
| `unitStyle`      | `UPRIGHT` (`upright("km")`), `TEXT` (`"km"`) | `UPRIGHT` |
| `multiplication` | `SPACE` (空格), `DOT` (`dot`), `TIMES` (`times`) | `SPACE` |
| `delimiter`      | `MATH` (`$…$`), `FRAGMENT`               | `MATH`    |

预设：`DEFAULT`、`FRAGMENT`（无 `$…$` 定界符）。

## 实际示例

由距离和时间求速度（`v = s / t`）：

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

加速度（`a = m/s²`）会用括号括住带幂的分母：

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KTypstUnitFormatter())
// $9.81 upright("m")/(upright("s")^2)$
```

如需输出完全不同的记法，请参阅[自定义格式化器](custom-formatters.md)。
