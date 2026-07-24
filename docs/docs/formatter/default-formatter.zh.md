# 默认格式化器

`KDefaultUnitFormatter` 是 kunit 开箱即用的格式化器。每当你调用 [`format`](formatting.md) 或带参数的
`toString` 而 **不** 传入自己的格式化器时，都会由它生成结果 —— 纯粹、易读的文本，例如 `"10.8 km/h"`。
本页精确说明它 **渲染什么** 以及 **如何渲染**，附带输出示例，并展示如何显式使用它。

它是不可变且线程安全的 `class`，位于 `org.pcsoft.framework.kunit.formatter` 包中。无参构造保持历史行为，
或传入 `KDefaultFormatConfig` 以更改渲染方式。

## 生成的内容

渲染出的字符串由两部分组成：**数值** 和 **单位** 部分，以一个空格分隔（`"<数值> <单位>"`）。如果值是
无量纲的（没有单位），则只渲染数值。

### 数值

- 没有模式时，原始 `Double` 通过 `Double.toString()` 输出。
- 使用 `java.util.Formatter` 模式（以及可选的 `Locale`）时，数值通过
  `String.format(locale, pattern, value)` 渲染。该模式 **仅** 影响数值，绝不影响单位部分。

| 调用                                              | 渲染的数值 |
|--------------------------------------------------|-----------------|
| `format(kilo.meters / hours)`                    | `10.799999999999999` |
| `format(kilo.meters / hours, "%.1f")`            | `10.8` |
| `format(kilo.meters / hours, "%.1f", Locale.GERMAN)` | `10,8` |

### 单位部分

每个单位项都以其 **自身书写的符号**（遵循前缀和替代单位的显示元数据）渲染，因此 `km`、`h`、`mi`、`KiB`
渲染为它们自身，而不是分组基准符号。整体形态取决于各项：

| 各项                                     | 渲染结果               |
|------------------------------------------|-----------------------|
| 单个单位，指数为 1                        | `km`                  |
| 指数 ≠ 1                                | `m^2`                 |
| 一个分子 + 恰好一个分母                   | `km/h`, `m/s^2`       |
| 其他情况                                  | `m*s^-3*A^-2`, `s^-1` |
| 无单位（无量纲）                          | 仅数值                 |

单分数形式（`a/b`）仅在 **恰好一个** 分子项和 **恰好一个** 分母项时使用。其他所有情况都渲染为带有显式
（可能为负）指数的扁平乘积。

## 输出示例

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

(1500 of meters).toString()                          // "1500.0 m"
(3 of meters / seconds).format(kilo.meters / hours)  // "10.799999999999999 km/h"
(3 of meters / seconds).format(meters / seconds, "%.2f") // "3.33 m/s"
(9.81 of meters / (seconds pow 2)).format(meters / (seconds pow 2), "%.2f") // "9.81 m/s^2"
```

## 显式使用

默认格式化器会自动应用，因此你很少需要指定它。但你仍然可以显式传入它 —— 为了与自定义格式化器保持对称，
或让调用处的选择更明确：

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KDefaultUnitFormatter
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 3 of meters / seconds

// 显式格式化器，结果与默认调用相同
v.format(kilo.meters / hours, "%.1f", Locale.US, KDefaultUnitFormatter()) // "10.8 km/h"

// 不带目标，用默认格式化器渲染基准单位
(5 of meters).toString(pattern = null, formatter = KDefaultUnitFormatter()) // "5.0 m"
```

若要输出完全不同的记法，请参阅 [自定义格式化器](custom-formatters.md)。
