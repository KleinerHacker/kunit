# 自定义格式化器

[`format`](formatting.md) 动词和带参数的 `toString` 通过可插拔的 `KUnitFormatter` 渲染文本。随附的
`KDefaultUnitFormatter` 生成诸如 `"10.8 km/h"` 的纯文本，但你可以插入完全自定义的渲染——例如为图形公式渲染器
输出 **LaTeX** 或 **MathML**、HTML，或任何领域特定表示法。这使得 kunit 易于扩展到把字符串转换为排版公式的
第三方框架。

## 契约

格式化器在单个 `KUnitFormatContext` 中接收所需的一切，并返回最终字符串：

```kotlin
interface KUnitFormatter {
    fun format(context: KUnitFormatContext): String
}

data class KUnitFormatContext(
    val value: Double,            // 已换算为目标单位的数字
    val units: List<KUnitTerm>,   // 目标量纲的各项（带前缀/指数显示元数据）
    val pattern: String? = null,  // 数字的可选 java.util.Formatter 模式
    val locale: Locale = Locale.getDefault(),
)
```

一切都通过**一个**上下文对象传入，因此该接口可以增量扩展（新字段带默认值）而不破坏你的实现。两个可复用的辅助
函数涵盖常见的构建块：

- `KUnitFormatContext.renderValue()` — 渲染数字：当 `pattern` 为 `null` 时用 `Double.toString()`，否则用
  `String.format(locale, pattern, value)`。
- `KUnitTerm.displaySymbol` — 项被写出时的符号（`"km"`、`"h"`），尊重显示元数据；没有时回退到组的基本符号。

项的 `exponent` 表示幂（正 = 分子，负 = 分母）；如何渲染指数由格式化器决定。

## 分步：LaTeX 格式化器

下面的格式化器从分子项和分母项渲染 `\frac{...}{...}`，每个单位符号使用 `\mathrm{...}`：

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.*

object LatexFormatter : KUnitFormatter {
    override fun format(context: KUnitFormatContext): String {
        // 1. 拆分为分子（指数 > 0）和分母（指数 < 0）
        val (numerator, denominator) = context.units.partition { it.exponent > 0 }

        // 2. 渲染单个项，例如 \mathrm{km} 或 \mathrm{s}^{2}
        fun render(terms: List<KUnitTerm>) = terms.joinToString(" ") { term ->
            val magnitude = kotlin.math.abs(term.exponent)
            val base = "\\mathrm{${term.displaySymbol}}"      // 使用显示元数据
            if (magnitude == 1) base else "$base^{$magnitude}"
        }

        // 3. 通过辅助函数渲染数字（尊重模式 + 区域设置）
        val value = context.renderValue()

        // 4. 组装
        if (denominator.isEmpty()) return "$value\\,${render(numerator)}".trim()
        return "$value\\,\\frac{${render(numerator)}}{${render(denominator)}}"
    }
}
```

## 使用

显式传入格式化器——除非你要求，默认行为绝不改变：

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 3 of meters / seconds

// 用自定义格式化器格式化为目标单位
v.format(kilo.meters / hours, "%.1f", Locale.US, LatexFormatter)
// "10.8\,\frac{\mathrm{km}}{\mathrm{h}}"

// 或用自定义格式化器渲染基本单位（无目标）
(5 of meters).toString(pattern = null, formatter = LatexFormatter)
// "5.0\,\mathrm{m}"
```

## 注意事项

- 让格式化器保持**无状态**，因而线程安全——随附的 `KDefaultUnitFormatter` 是普通 `object`，上面的
  `LatexFormatter` 也是。
- `KUnitFormatContext` 接收**已换算**为目标单位的值，因此格式化器从不自行进行单位换算——它只负责渲染。
- `units` 各项携带显示元数据（`KUnitTerm.display`）；请始终通过 `displaySymbol` 读取符号，以便带前缀和别名的
  单位（`km`、`mi`、`KiB`）正确渲染。
