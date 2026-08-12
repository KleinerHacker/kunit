# 输出格式化

本页是 **格式化器** 分组的概述。它讲解 `format` 动词 —— 所有格式化的入口。另有两个专门页面更深入：

- [默认格式化器](default-formatter.md) —— 内置的 `KDefaultUnitFormatter` 如何渲染单位部分（开箱即用的记法）， 附带输出示例。
- [自定义格式化器](custom-formatters.md) —— 如何接入你自己的渲染（LaTeX、MathML、HTML 等）。

每个值都能通过 `toString()` 以其 **基本单位**打印自身，也能用 [`into`](../mixed-units.md) **读取**为特定单位——但
`into` 只返回一个不带单位符号的裸 `Double`。`format` 动词填补了这一空缺：它是 `into` 的显示对应物，返回值 **和**
单位符号组成的 `String`。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds

val v = 3 of meters / seconds

v format kilo.meters / hours       // "10.799999999999999 km/h"
```

与 `into` 一样，`format` 先将值读取为目标单位（执行相同的量纲检查与仿射转换），再附加目标单位的符号。由于目标
保留了它被写出时的单位，带前缀的和别名单位会以 **自身**的符号（`km`、`h`、`mi`）渲染，而不是组的基本符号 （`m`、`s`）。

## 数字格式：模式与区域设置

中缀形式渲染原始 `Double`。要对 **数字部分**进行舍入或本地化，请使用带数字模式和可选 `KLocale` 的
`format` 重载：

```kotlin
import org.pcsoft.framework.kunit.formatter.KLocale

v.format(kilo.meters / hours, "%.1f")                 // "10.8 km/h"
v.format(kilo.meters / hours, "%.1f", KLocale.DE_DE)  // "10,8 km/h"
```

模式 **只**影响数字，单位部分不变。无效模式会抛出 `IllegalArgumentException`，不兼容的目标量纲会（与
`into` 一样）抛出 `IllegalStateException`。

### `KLocale`

Kotlin 没有通用的区域设置 API —— `java.util.Locale` 仅存在于 JVM 上 —— 因此 kunit 携带了自己的、对数字书写方式
的最小化描述：小数分隔符、分组分隔符和分组大小。由于这些约定随值一起传递，**同一个模式在任何目标平台上都会渲染出
相同的字符串**。

`KLocale.ROOT`（点号小数、逗号分组）是默认值。预定义的常量涵盖了常见情形：
`EN_US`、`EN_GB`、`DE_DE`、`FR_FR`、`ES_ES`、`IT_IT`、`PT_BR`、`NL_NL`、`RU_RU`、`JA_JP`、`ZH_CN`、`KO_KR`、
`AR_SA` 和 `HI_IN`（对应印度式的 3-then-2 分组）。其他任何约定都可以通过直接构造 `KLocale` 来表达。

在 JVM 上，`java.util.Locale` 仍然可用：接受它的重载在 JVM 源代码集中提供，并通过 `toKLocale()` 转换。

```kotlin
import java.util.Locale

v.format(kilo.meters / hours, "%.1f", Locale.GERMANY) // "10,8 km/h"（仅限 JVM）
```

### 支持的模式

模式是应用于单个数字值的 printf 子集：

```
%[flags][width][.precision]conversion
```

| 部分       | 含义                                                                        |
|------------|-----------------------------------------------------------------------------|
| flags      | `-` 左对齐 · `+` 始终显示符号 · 空格表示正数 · `0` 补零 · `,` 分组          |
| width      | 字符总数的最小值                                                             |
| precision  | 小数位数（转换符 `f`、`e`、`E`）                                             |
| conversion | `f` 定点 · `e`/`E` 科学计数法 · `d` 整数 · `s` 原样渲染                     |

`%%` 输出一个字面百分号，转换符周围的字面文本会原样复制。

```kotlin
(1500 of meters).toString("%,.2f", KLocale.EN_US) // "1,500.00 m"
(1500 of meters).toString("%,.2f", KLocale.DE_DE) // "1.500,00 m"
(1500 of meters).toString("%.2e", KLocale.EN_US)  // "1.50e+03 m"
```

## 分数表示与乘积表示

内置格式化器按如下方式渲染单位部分：

| 项                      | 渲染结果              |
|-------------------------|-----------------------|
| 单个单位，指数 1        | `km`                  |
| 指数 ≠ 1                | `m^2`                 |
| 一个分子 + 恰好一个分母 | `km/h`、`m/s^2`       |
| 其他情况                | `m*s^-3*A^-2`、`s^-1` |
| 无单位（无量纲）        | 仅数字                |

## 带模式的 `toString`

无参 `toString()` 保持不变（基本单位渲染）。额外的重载把同样的数字模式／区域设置应用到基本单位输出——这就是 不带目标的
`format` 动词：

```kotlin
(3 of meters / seconds).toString("%.2f", KLocale.EN_US) // "3.00 m/s"
(1500 of meters).toString("%.1f", KLocale.EN_US)        // "1500.0 m"
```

## 实际示例

换算跑步配速并整洁地打印：

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*
import org.pcsoft.framework.kunit.kinematic.time.*
import org.pcsoft.framework.kunit.formatter.KLocale

val distance = 10 of kilo.meters
val time = 50 of minutes
val speed = distance / time                    // KSpeedUnitInstance

println(speed.format(kilo.meters / hours, "%.1f", KLocale.EN_US)) // "12.0 km/h"
println(speed.format(meters / seconds, "%.2f", KLocale.EN_US))    // "3.33 m/s"
```

## 自定义渲染

单位部分由可插拔的 [`KUnitFormatter`](custom-formatters.md) 生成；随附的 `KDefaultUnitFormatter` 生成上面的 纯文本 ——
其确切规则和输出示例见[默认格式化器](default-formatter.md)。要输出完全不同的表示法（用于图形公式渲染器的 LaTeX 或
MathML、HTML 等），请实现你自己的格式化器并显式 传入。参见[自定义格式化器](custom-formatters.md)。
