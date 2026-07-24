# 图形格式化器

`KGraphicalConsoleUnitFormatter` 为支持 ANSI 的终端**以多行图形方式**渲染数值：分数被绘制为真正的二维堆叠
（分子、横线、分母），数值位于横线（中间）行。指数始终以真正的 Unicode 上标数字表示，每个视觉角色都通过
`KGraphicalConsoleColorPalette` 着色。

它位于 `org.pcsoft.framework.kunit.formatter` 包中，是不可变且线程安全的 `class`。

## 生成内容

规整的单分母形式堆叠为三行；其余形式为用乘法符号连接、带上标指数的单行乘积；无量纲值仅为着色的数字。
分子和分母按其**可见**宽度（ANSI 颜色序列不计入宽度）在横线上居中。加速度 `9.81 m/s²` 渲染（无着色）为：

```
     m
9.81 ──
     s²
```

## 配置

`KGraphicalConsoleFormatConfig` 是值类型；可选择 `DEFAULT` 预设或自行构建：

| 选项              | 值 / 类型                                        | 默认       |
|-------------------|-------------------------------------------------|------------|
| `palette`         | `KGraphicalConsoleColorPalette` — `CLASSIC`, `VIVID`, `MONOCHROME` | `CLASSIC` |
| `fractionBar`     | `LINE` (`─`), `HEAVY` (`━`), `ASCII` (`-`)      | `LINE`     |
| `multiplication`  | `ASTERISK` (`*`), `MIDDLE_DOT` (`·`), `CROSS` (`×`) | `MIDDLE_DOT` |
| `functionSymbols` | `KGraphicalFunctionSymbols` — `UNICODE`, `ASCII` | `UNICODE`  |

调色板为五个角色（数字、符号、运算符、指数和横线）着色。颜色为空字符串的角色保持无着色（这就是
`MONOCHROME` 让指数保持无着色的方式）。

## 实际示例

终端中的速度与加速度：

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KGraphicalConsoleUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

// 默认 CLASSIC 调色板（此处省略颜色）；布局：
//      km
// 90.0 ──
//      h
println(v.format(kilo.meters / hours, "%.1f", Locale.US, KGraphicalConsoleUnitFormatter()))

// 粗横线
val config = KGraphicalConsoleFormatConfig(fractionBar = KGraphicalFractionBar.HEAVY)
println(v.format(kilo.meters / hours, "%.1f", Locale.US, KGraphicalConsoleUnitFormatter(config)))
```

如需输出完全不同的记法，请参阅[自定义格式化器](custom-formatters.md)。
