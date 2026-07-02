# 混合单位

**混合单位**（德语：*Mischeinheit*）是由多个 `KUnit` 组成的值，每个都带有自己的指数，例如速度的
`m^1 * s^-1`，或力的 `m^1 * kg^1 * s^-2`。在 kunit 中，这由通用的 `KUnitInstance` 类表示。

虽然特定于分组的包装类（如 `KLengthUnitInstance`，参见[预定义单位](units/length.md)）在处理单一物理量纲时
很方便，但一旦需要组合**不同**组的单位，或者不希望使用包装类提供的同组自动换算，就需要直接使用
`KUnitInstance`。

## 结构

```kotlin
data class KUnitTerm(val unit: KUnit, val exponent: Int)

class KUnitInstance(value: Number, val units: List<KUnitTerm>)
```

- `value` 是归一化后的 `Double` 数值，始终相对于 `units` 中列出的确切单位和指数——与分组包装类不同，
  `KUnitInstance` **不会**归一化到某个组的基础单位。
- `units` 是描述物理量纲的 `(KUnit, 指数)` 对列表。

每个"纯"单位都提供 `toKUnitInstance()` 扩展函数，用于转换为这种通用表示形式：

```kotlin
import org.pcsoft.framework.kunit.length.*

val d = 5.meters()
val mixed = d.toKUnitInstance() // KUnitInstance: value=5.0, units=[METER^1]
```

!!! note
    下面示例中引用 `seconds()`/`TimeUnit` 的部分仅用于说明"时间"单位组与长度组合时的样子——kunit 目前
    只提供 `length` 组（参见[预定义单位](units/length.md)）。要自行添加，请参阅
    [添加自定义单位](custom-units.md)。

## 乘法与除法

两个 `KUnitInstance` 之间的 `*` 和 `/` **始终**被允许——单位的乘除在物理上总是有意义的，因此没有量纲限制。

- `*` 会累加匹配单位的指数，并将只存在于一侧的单位原样带入结果。
- `/` 会从匹配单位中减去右侧操作数的指数（对于只存在于右侧的单位，则取反其指数）。
- 结果指数为 `0` 时，该单位会从结果中被完全移除。

```kotlin
import org.pcsoft.framework.kunit.length.*

val distance = 10.meters().toKUnitInstance()   // units=[METER^1]
val width = 4.meters().toKUnitInstance()       // units=[METER^1]

val area = distance * width                     // value=40.0, units=[METER^2]
val backToLength = area / width                 // value=10.0, units=[METER^1]
```

混合两个不同的单位组（例如长度，以及未来可能加入的时间）的方式完全相同，并会产生一个真正的混合单位：

```kotlin
// 假设已按照《添加自定义单位》中的模式存在一个"时间"单位组：
val distance = 100.meters().toKUnitInstance()
val time = 10.seconds().toKUnitInstance()

val speed = distance / time // value=10.0, units=[METER^1, SECOND^-1]
```

## 加法与减法

与 `*`/`/` 不同，`+` 和 `-` 只有在两个 `KUnitInstance` 拥有**完全相同**的 `units`（相同的 `KUnit` 且指数
相同，顺序无关）时才被允许。这里**没有**不同单位之间的自动换算，因为 `KUnitInstance` 并不知道哪些单位属于
同一个组——这正是特定于分组的包装类（`KLengthUnitInstance` 等）额外提供的功能。

```kotlin
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.length.KLengthUnit

val a = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
val b = KUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
(a + b).value // 8.0

val c = KUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.MILE, 1)))
a + c // 抛出 IllegalStateException：METER != MILE，尽管两者都属于"长度"
```

使用 `hasSameUnits` 可以预先检查兼容性：

```kotlin
val a = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
val b = KUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KLengthUnit.METER, 0)))
a.hasSameUnits(b) // 比较 (单位 -> 指数) 签名，与顺序无关
```

## 读取与格式化数值

`valueAs` 将数值转换为一组任意的目标单位——每个目标必须按单位组（对于派生单位还需按指数）恰好匹配一个项。
`toString` 重载执行相同的操作，但同时会渲染符号。

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.length.*

val speed = 10.meters().toKUnitInstance() / 1.seconds().toKUnitInstance()

speed.valueAs(KUnitPrefix.KILO with KLengthUnit.METER, TimeUnit.HOUR) // 36.0 (km/h)
speed.toString(KUnitPrefix.KILO with KLengthUnit.METER, TimeUnit.HOUR) // "36.0 km*h^-1"

val area = 200.meters().toKUnitInstance() * 50.meters().toKUnitInstance()
area.valueAs(KLengthDerivedUnit.HECTARE) // 1.0
```

默认（无参数）的 `toString()` 始终使用每个项自身的 `KUnit.symbol`，并用 `*` 连接，例如 `"5.0 m*s^-1"`。

## 混合使用纯单位与混合单位

每个纯单位包装类都直接支持对 `KUnitInstance` 使用 `*`/`/`，因此在这些运算符中你几乎不需要显式调用
`toKUnitInstance()`：

```kotlin
import org.pcsoft.framework.kunit.length.*

val distance = 100.meters()                 // KLengthUnitInstance
val mixed = distance.toKUnitInstance()       // KUnitInstance

val combined = distance * mixed              // KUnitInstance: METER^2
```

## 转换回纯单位

一旦 `KUnitInstance` 再次恰好只包含单个单位组的一个项，就可以通过该组特定的 `toXxxUnit()` 扩展函数
（例如 `toKLengthUnit()`）将其转换回该组的包装类：

```kotlin
import org.pcsoft.framework.kunit.length.*

val speed = 10.meters() / 2.seconds()          // KUnitInstance（假设时间组存在）
val distanceAgain = speed.toKUnitInstance() * 2.seconds() // units=[METER^1]
distanceAgain.toKLengthUnit().value             // 10.0

val area = 200.meters() * 50.meters()           // units=[METER^2]
area.toKLengthUnit().value                        // 10000.0（面积，指数为2）
```

如果 `KUnitInstance` **不是**恰好由该组的一个项组成（例如仍然是混合的长度/时间值），转换会抛出
`IllegalStateException`。
