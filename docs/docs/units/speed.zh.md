# 速度

包: `org.pcsoft.framework.kunit.speed`
基本单位: **米每秒** (`KSpeedUnit.BASE == KSpeedUnit.METERS_PER_SECOND`)

速度是第一个**构造(constructed)**单位: 与长度或时间不同, 它不是单一的"真实"物理量, 而是一个组合,
`长度 · 时间⁻¹` (`m/s`)。因此 `KSpeedUnitInstance` 包装了一个恰好包含两个项的 `KMixedUnitInstance` -
一个指数为 `+1` 的 `KDistanceUnit.BASE`(米)和一个指数为 `-1` 的 `KTimeUnit.BASE`(秒)。无论用哪个单位、
前缀或长度/时间组合创建, 其值始终以米每秒归一化存储。

## 单位

| 单位 | 枚举值 | 符号 | 创建器 | 1 单位 (m/s) |
|---|---|---|---:|---:|
| 米每秒 | `KSpeedUnit.METERS_PER_SECOND` | `m/s` | `Number.metersPerSecond` | 1.0 |
| 千米每小时 | `KSpeedUnit.KILOMETERS_PER_HOUR` | `km/h` | `Number.kilometersPerHour` | 0.277778 (1000/3600) |
| 英里每小时 | `KSpeedUnit.MILES_PER_HOUR` | `mph` | `Number.milesPerHour` | 0.44704 (1609.344/3600) |
| 节 | `KSpeedUnit.KNOT` | `kn` | `Number.knots` | 0.514444 (1852/3600) |
| 英尺每秒 | `KSpeedUnit.FEET_PER_SECOND` | `ft/s` | `Number.feetPerSecond` | 0.3048 |
| 马赫 (ISA 海平面) | `KSpeedUnit.MACH` | `Ma` | `Number.mach` | 340.29 |
| 光速 | `KSpeedUnit.LIGHT_SPEED` | `c` | `Number.speedOfLight` | 299792458.0 |

上述每个单位都有一个对应的 bare `val` 别名, 可用作 `valueAs`/`toString` 目标或前缀 infix 函数的 `unit`
参数: `metersPerSecond`, `kilometersPerHour`, `milesPerHour`, `knots`, `feetPerSecond`, `mach`,
`speedOfLight`。

> **马赫**是国际标准大气海平面(15 °C)下的声速。它是一个方便的参考点, 而非物理常数 - 真实声速随温度和
> 高度变化。

```kotlin
import org.pcsoft.framework.kunit.speed.*

val v = 50.kilometersPerHour
v.value                                    // 13.888... (归一化为 m/s)
v.valueAs(KSpeedUnit.KILOMETERS_PER_HOUR)  // 50.0 (读回为 km/h)
v.valueAs(milesPerHour)                     // ≈ 31.07
v.valueAs(knots)                            // ≈ 26.998
v.valueAs(mach)                             // ≈ 0.0408 (声速的比例)
```

## 用核心单位(长度和时间)计算

这正是构造单位的要点, 也是**不**直观的部分 - 请仔细阅读本节。

**思维模型:** 速度就是长度除以时间。KUnit 让你用普通的 `*` 和 `/` 在三个物理量 - 长度、时间和速度 -
之间转换, 每个结果都是**强类型**的。你从不需要自己构建或拆解原始的 `KMixedUnitInstance`。

四种合法组合及其结果类型:

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `length / time` | `KSpeedUnitInstance` | 速度 = 距离 / 时间 |
| `speed * time` | `KLengthUnitInstance` | 距离 = 速度 × 时间 |
| `time * speed` | `KLengthUnitInstance` | 距离(可交换) |
| `length / speed` | `KTimeUnitInstance` | 时间 = 距离 / 速度 |

```kotlin
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.speed.*

// --- 核心单位 -> 速度 ---------------------------------------------------
val v = 100.meters / 10.seconds          // KSpeedUnitInstance (无需 .toSpeed()!)
v.value                                     // 10.0 (m/s)
v.valueAs(KSpeedUnit.KILOMETERS_PER_HOUR)   // 36.0
v.valueAs(KSpeedUnit.MILES_PER_HOUR)        // ≈ 22.37
v.valueAs(KSpeedUnit.KNOT)                  // ≈ 19.44
v.valueAs(KSpeedUnit.MACH)                  // ≈ 0.0294
v.valueAs(KSpeedUnit.LIGHT_SPEED)           // ≈ 3.336e-8

// 赋值目标类型不会转换任何东西 - 运算符已经返回
// KSpeedUnitInstance。Kotlin 没有隐式转换。
val explicit: KSpeedUnitInstance = 100.meters / 10.seconds

// --- 速度 -> 长度 (乘以时间) --------------------------------------------
val distance = v * 60.seconds             // KLengthUnitInstance
distance.value                              // 600.0 (m)
distance.valueAs(KDistanceUnit.METER)         // 600.0
distance.valueAs(feet)                      // ≈ 1968.5 (读回为任意长度单位)
distance.valueAs(miles)                     // ≈ 0.373
60.seconds * v                            // 相同结果(可交换)

// --- 速度 -> 时间 (用它除以长度) ----------------------------------------
val time = 600.meters / v                 // KTimeUnitInstance
time.value                                  // 60.0 (s)
time.valueAs(KTimeUnit.MINUTE)              // 1.0
time.valueAs(KTimeUnit.HOUR)                // ≈ 0.0167
```

!!! warning "只有**纯**长度才能除成速度"
    `length / time` 和 `length / speed` 要求长度的指数为 1。**面积**(`m²`, 例如 `2.hectares`)或
    **体积**(`m³`)不是长度, 所以 `area / time` 会是 `m²/s` 而非速度 - 运算符会抛出
    `IllegalStateException`, 而不是悄悄返回错误值。同样, `length * time`(`m·s`, 非速度)和
    `length + speed`(维度不同)也不是有效的速度构造。

### 有意计算非速度的中间结果(例如 m²/s)

由于 Kotlin 运算符在编译期只有单一的返回类型, `KLengthUnitInstance / KTimeUnitInstance` 被**保留**用于
构建有类型的速度, 无法改为产生 `m²/s`。但那个中间结果**并未丢失** - 将一个操作数用
`toUnit()` 降到混合层, 这样就会选择通用的 `KMixedUnitInstance` 的 `/` 运算符(任意指数,
无速度检查)。这个显式的 `toUnit()` 正是你离开强类型路径的有意信号。

```kotlin
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

val area = 2.hectares                 // KLengthUnitInstance, 指数 2 (20 000 m²)

// area / 2.seconds                   // ❌ 抛出 IllegalStateException (只是 m²/s, 非速度)

// ✅ 有意的 m²/s 中间结果: 将一个操作数降到混合层
val areaPerTime = area.toUnit() / 2.seconds.toUnit()
areaPerTime.value                       // 10000.0
areaPerTime.units                       // [METER^2, SECOND^-1]

// ...并且它像任何 KMixedUnitInstance 一样继续链式计算
val backToArea = areaPerTime * 4.seconds.toUnit() // units=[METER^2], value=40000.0
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.speed.*

// + / - : 同组, 不同速度单位间自动转换
val a = 36.kilometersPerHour + 10.metersPerSecond  // KSpeedUnitInstance, 20 m/s
val b = 20.metersPerSecond - 36.kilometersPerHour  // 10 m/s

// 比较(按归一化 m/s 值)
50.kilometersPerHour > 10.metersPerSecond   // true  (13.89 m/s > 10 m/s)
36.kilometersPerHour == 10.metersPerSecond  // true  (相同归一化值)

// 两个速度间的 * / / 会"逃逸"为 KMixedUnitInstance(不再是纯速度)
val squared = 10.metersPerSecond * 2.metersPerSecond // KMixedUnitInstance, units=[m^2, s^-2]
```

## 比较与相等

`==`, `!=`, `<`, `<=`, `>`, `>=` 比较两个 `KSpeedUnitInstance` 的归一化 `value`(米每秒)。由于速度始终
具有相同维度, 无需指数检查(不像长度, 面积和长度无法比较)。

## SI 前缀

任何 `KSpeedUnit` 都可以与 24 个 SI 前缀(`KUnitPrefix`, Quetta/Q 到 Quecto/q)组合。使用速度组的
`infix` 构造函数(直接返回 `KSpeedUnitInstance`)和 `with`(用于 `valueAs`/`toString` 目标):

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.speed.*

// 构造: "5 kilo metersPerSecond" -> KSpeedUnitInstance (直接, == 5000.metersPerSecond)
val fast = 5 kilo metersPerSecond
fast.value // 5000.0

// 用前缀目标读回值
val v = 5.metersPerSecond
v.valueAs(KUnitPrefix.KILO with KSpeedUnit.METERS_PER_SECOND)  // 0.005
```

你也可以将速度读回为显式的**长度/时间对**(两个目标), 这就是从长度和时间部分表示"km/h"的方式:

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.speed.*

val v = 10.metersPerSecond
v.valueAs(KUnitPrefix.KILO with KDistanceUnit.METER, KTimeUnit.HOUR)   // 36.0 (km per h)
v.toString(KUnitPrefix.KILO with KDistanceUnit.METER, KTimeUnit.HOUR)  // "36.0 km*h^-1"
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.speed.*

10.metersPerSecond.toString()                            // "10.0 m/s" (基本单位)
(100.meters / 10.seconds).toString(KSpeedUnit.KILOMETERS_PER_HOUR) // "36.0 km/h"
1.mach.toString(KSpeedUnit.MACH)                          // "1.0 Ma"
```
