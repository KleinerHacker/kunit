# 控制台格式化器

`KConsoleUnitFormatter` 面向 **支持 ANSI 的终端** 渲染数值。它产生的记法与
[默认格式化器](default-formatter.md) 完全相同（`"10.8 km/h"`、`"m^2"`、`"m*s^-3*A^-2"`），但会用
ANSI 颜色序列包裹每个视觉部分——数字、单位符号、运算符和指数——使各部分在控制台上更醒目。

它位于 `org.pcsoft.framework.kunit.formatter` 包中。与默认格式化器不同，由于它携带一个颜色调色板，
因此是一个普通的（不可变、线程安全的）`class`。

## 生成内容

布局与 [默认格式化器](default-formatter.md) **完全一致**：`"<数字> <单位>"` 形式，单一分数形式
`a/b` 仅在恰好一个分子项和恰好一个分母项时使用，否则为带符号指数的扁平乘积，无量纲值仅显示数字。
唯一的区别是每个部分都被 ANSI SGR 颜色包裹，并以重置序列 `ESC[0m` 结束。

### 着色的部分

四个视觉部分通过 [`KConsoleColorPalette`](#调色板) 独立着色：

| 部分     | 调色板字段      | 示例             |
|----------|-----------------|------------------|
| 数字     | `numberColor`   | `10.8`           |
| 单位符号 | `symbolColor`   | `km`、`h`、`m`   |
| 运算符   | `operatorColor` | `*`、`/`         |
| 指数     | `exponentColor` | `^2`、`^-3`      |

颜色为 **空字符串** 的部分不输出任何转义序列（该部分不着色）——`MONOCHROME` 正是以此方式保持指数
不着色。

## 调色板

颜色是值类型 `KConsoleColorPalette`。预定义了三种调色板：

| 调色板       | 数字                       | 符号              | 运算符          | 指数                     |
|--------------|----------------------------|-------------------|-----------------|--------------------------|
| `CLASSIC`    | 青色 `ESC[36m`            | 黄色 `ESC[33m`    | 灰色 `ESC[90m`  | 品红 `ESC[35m`           |
| `VIVID`      | 亮绿加粗 `ESC[92;1m`      | 亮蓝 `ESC[94m`    | 白色 `ESC[97m`  | 亮品红 `ESC[95m`         |
| `MONOCHROME` | 加粗 `ESC[1m`            | 暗色 `ESC[2m`     | 暗色 `ESC[2m`   | 不着色（空）             |

- `CLASSIC` 在深色终端上柔和易读，是 **默认** 调色板。
- `VIVID` 高对比、醒目。
- `MONOCHROME` 仅使用亮度（不使用颜色），适合颜色支持较差的终端。

## 使用方式

不带参数构造时使用默认的 `CLASSIC` 调色板，也可以传入预定义或自定义的调色板。随后像任何其他
格式化器一样将其交给 [`format`](formatting.md) 动词（或带参数的 `toString`）：

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KConsoleColorPalette
import org.pcsoft.framework.kunit.formatter.KConsoleUnitFormatter
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 3 of meters / seconds

// 默认的 CLASSIC 调色板
v.format(kilo.meters / hours, "%.1f", Locale.US, KConsoleUnitFormatter())

// 预定义调色板
v.format(kilo.meters / hours, "%.1f", Locale.US, KConsoleUnitFormatter(KConsoleColorPalette.VIVID))

// 无目标单位，按基准单位渲染
(5 of meters).toString(pattern = "%.1f", formatter = KConsoleUnitFormatter(KConsoleColorPalette.MONOCHROME))
```

## 定义自己的调色板

`KConsoleColorPalette` 是一个普通的数据类，因此你可以提供自己的颜色序列。每个字段保存 ANSI
**引导序列**（例如红色 `ESC[31m`，其中 `ESC` 是编码为 27 的转义字符）；共享的 `reset`（默认
`ESC[0m`）会附加在每个着色部分之后：

```kotlin
import org.pcsoft.framework.kunit.formatter.KConsoleColorPalette
import org.pcsoft.framework.kunit.formatter.KConsoleUnitFormatter

val esc = 27.toChar()   // ANSI 转义字符 (ESC)
val myPalette = KConsoleColorPalette(
    numberColor = "$esc[31m",   // 红
    symbolColor = "$esc[32m",   // 绿
    operatorColor = "$esc[34m", // 蓝
    exponentColor = "$esc[35m", // 品红
)
val formatter = KConsoleUnitFormatter(myPalette)
```

若需输出完全不同的记法（而不仅是颜色），请改为实现你自己的
[自定义格式化器](custom-formatters.md)。

## 配置（指数、符号、函数符号）

与颜色无关，第二个参数 `KConsoleFormatConfig` 控制记法（与[默认格式化器](default-formatter.md)相同）：
`exponentStyle`（`CARET` = `m^2` / `SUPERSCRIPT` = `m²`）、`multiplication`（`*` / `·` / `×`）、
`division`（`/` / `÷`）以及 `functionSymbols`（`UNICODE` / `ASCII`）。两个参数都有默认值，因此
`KConsoleUnitFormatter()` 与以往一致。按 `KConsoleUnitFormatter(palette, config)` 传入。

若需带真实分数线的多行二维分数，请参阅[图形格式化器](graphical-formatter.md)。
