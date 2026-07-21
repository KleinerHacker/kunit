# 混合单位

**混合单位**(德语: *Mischeinheit*)是由多个 `KUnit` 组成的值,每个单位提升到各自的指数,例如速度为
`m^1 * s^-1`,力为 `m^1 * kg^1 * s^-2`。在 kunit 中,这由通用的 `KMixedUnitInstance` 类表示。

虽然组特定的包装器类(如 `KLengthUnitInstance`,见[预定义单位](units/distance.md))便于处理单个物理维度,但当
你需要组合**不同**组的单位,或者不想要包装器类提供的同组自动转换时,就要用到 `KMixedUnitInstance`。

## 结构

```kotlin
data class KUnitTerm(val unit: KUnit, val exponent: Int)

class KMixedUnitInstance(value: Number, val units: List<KUnitTerm>)
```

- `value` 是归一化的 `Double` 量级,始终相对于 `units` 中所列的确切单位和指数 —— 与组包装器不同,
  `KMixedUnitInstance` **不**执行到某个组基本单位的归一化。
- `units` 是描述物理维度的 `(KUnit, exponent)` 对的列表。

每个"纯"单位都暴露一个 `toUnit()` 扩展以转换为这种通用表示:

```kotlin
import org.pcsoft.framework.kunit.distance.*

val d = 5 of meters
val mixed = d.toUnit() // KMixedUnitInstance: value=5.0, units=[METER^1]
```

## 乘法和除法

`*` 和 `/` 在两个 `KMixedUnitInstance` 之间**始终**被允许 —— 由于单位的乘法/除法在物理上总是有意义的,因此没有
维度限制。

- `*` 将匹配单位的指数相加,并简单地保留仅存在于一侧的任何单位。
- `/` 从匹配单位中减去右侧的指数(对仅存在于右侧的单位则对指数取反)。
- 结果指数为 `0` 会将该单位从结果中完全移除。

```kotlin
import org.pcsoft.framework.kunit.distance.*

val distance = (10 of meters).toUnit()   // units=[METER^1]
val width = (4 of meters).toUnit()       // units=[METER^1]

val area = distance * width                     // value=40.0, units=[METER^2]
val backToLength = area / width                 // value=10.0, units=[METER^1]
```

混合两个不同的单位组(例如长度和时间)以完全相同的方式工作,并产生一个真正的混合单位:

```kotlin
import org.pcsoft.framework.kunit.time.seconds

val distance = (100 of meters).toUnit()
val time = (10 of seconds).toUnit()

val speed = distance / time // value=10.0, units=[METER^1, SECOND^-1]
```

## 用纯数字缩放

任何单位值都可以用纯 `Number` 缩放。这是一个**仅改变大小**的运算：它改变数值，但保持单位项和指数不变，因此结果保留其类型和量纲。

- `unit * n`、`n * unit` 和 `unit / n` 都返回**相同类型的单位**（长度仍是长度，面积仍是面积）。
- `n / unit` 会**反转**量纲（所有指数取负），因此得到一个通用的 `KMixedUnitInstance` —— 这是从周期构造频率之类倒数量的惯用方式。
- 有意**不提供**标量的 `+`/`-`：把无量纲的数加到有量纲的值上没有意义。

一个实际例子 —— 圆的面积 `A = π · r²`，完全通过单位系统计算：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.times
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.distance.*

val r = 12 of centi.meters       // KLengthUnitInstance，0.12 m
val area = Math.PI * (r * r)     // KAreaUnitInstance: π·r² ≈ 0.04524 m²
area into (meters * meters)      // ≈ 0.04524（平方米）
```

缩放一个长度，或把一段路程等分，做法相同：

```kotlin
val tripled = (12 of meters) * 3 // KLengthUnitInstance，36 m
val leg = (10 of kilo.meters) / 4 // KLengthUnitInstance，2.5 km（路程的四分之一）
```

用一个数**除以**单位会反转量纲，例如从周期得到频率：

```kotlin
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.time.seconds

val frequency = 1 / (2 of seconds) // KMixedUnitInstance: value=0.5，units=[SECOND^-1]（0.5 Hz）
```

仿射的**绝对温度**组是唯一的例外：用数字缩放绝对温度在物理上没有意义（其开尔文值带有 −273.15 的偏移），所以 `(20 of celsius) * 2` 是**编译错误**。请改为缩放线性的**温差**（参见[温差](units/temperature-difference.md)）。

## 加法和减法

与 `*`/`/` 不同,`+` 和 `-` 只允许在描述**相同物理维度**的两个 `KMixedUnitInstance` 之间进行: 对于一侧的每个项,
另一侧必须恰好有一个属于相同单位组(例如全都是 `KDistanceUnit` 值)且具有相同指数的项(与顺序无关)。`KUnit`
本身**不**需要完全相同 —— 匹配的项会通过归一化自动转换,方式与组特定包装器类(`KLengthUnitInstance` 等)对
"纯"单位所做的相同。结果以左操作数的 `units` 表示。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.distance.miles

val a = (5 of meters).toUnit()
val b = (3 of meters).toUnit()
(a + b).value // 8.0

val c = (3 of miles).toUnit()
(a + c).value // 4832.032(将 3 英里转换为米后相加), units=[METER^1]
```

不匹配的单位组或不匹配的指数仍然会失败:

```kotlin
import org.pcsoft.framework.kunit.time.seconds

a + (3 of seconds).toUnit()       // 抛出 IllegalStateException: 时间项没有匹配的单位组
a + ((2 of meters) pow 2).toUnit() // 抛出 IllegalStateException: 指数不匹配(1 对 2)
```

使用 `hasSameUnits` 预先检查**精确**匹配(相同的 `KUnit`,而不只是相同的组):

```kotlin
val x = (5 of meters).toUnit()
val y = (3 of meters).toUnit()
x.hasSameUnits(y) // 与顺序无关地比较 (unit -> exponent) 签名
```

## 读取值

`into` 以目标单位模板(裸令牌、带前缀的构建器模板或特殊的值 1 实例)读取值,返回一个纯 `Double`。两侧都必须
描述相同的物理维度。没有 `valueAs`,也没有自定义单位的 `toString`;特定单位格式化为 `"${v into kilo.meters} km"`。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds

val speed = (10 of meters) / (1 of seconds)

speed into (kilo.meters / hours)   // 36.0(km/h)

val area = (200 of meters) * (50 of meters)
area into hectares                 // 1.0
```

默认(无参)`toString()` 始终使用每个项自身的 `KUnit.symbol`,用 `*` 连接,例如 `"5.0 m*s^-1"`。

## 混合纯单位和混合单位

每个纯单位包装器类都直接支持对 `KMixedUnitInstance` 的 `*`/`/`,因此你很少需要为这些运算符显式调用 `toUnit()`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*

val distance = 100 of meters        // KLengthUnitInstance
val mixed = distance.toUnit()       // KMixedUnitInstance

val combined = distance * mixed              // KMixedUnitInstance: METER^2
```

## 转换回纯单位

一旦 `KMixedUnitInstance` 再次表示单个单位组的恰好一个项,就可以通过组特定的 `toXxxUnit()` 扩展(例如
`toDistance()`)转换回该组的包装器类:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.seconds

val speed = (10 of meters) / (2 of seconds)    // KSpeedUnitInstance
val distanceAgain = speed.toUnit() * (2 of seconds).toUnit() // units=[METER^1]
distanceAgain.toDistance().value               // 10.0

val area = (200 of meters) * (50 of meters)    // KAreaUnitInstance
area.toUnit().toDistance().value               // 10000.0(面积, 指数 2)
```

如果 `KMixedUnitInstance` **不**恰好由该组的一个项组成(例如它仍是混合的长度/时间值),转换会抛出
`IllegalStateException`。

同样的收窄也可以**直接在距离值上**使用(不仅限于 `KMixedUnitInstance`): 一般的 `KDistanceUnitInstance` ——
或任意叶子 —— 可以用 `toLength()`、`toArea()` 或 `toVolume()` 收窄到特定维度,它们会检查指数,并在不匹配时
抛出 `IllegalStateException`:

```kotlin
val area = (200 of meters) * (50 of meters)  // KAreaUnitInstance(指数 2)
area.toArea().value                          // 10000.0
area.toDistance().toArea().value             // 10000.0(先扩宽,再收窄回来)
area.toLength()                              // IllegalStateException(指数 2,而非 1)
```
