# 速度

包: `org.pcsoft.framework.kunit.speed`
基本单位: **米每秒**(`KSpeedUnit.BASE == KSpeedUnit.METERS_PER_SECOND`)

速度是第一个**构造**单位: 与长度或时间不同,它不是单一的"真实"量,而是一个合成量 `length · time⁻¹`(`m/s`)。
因此 `KSpeedUnitInstance` 封装了一个恰好包含两个项的 `KMixedUnitInstance` —— 指数为 `+1` 的
`KDistanceUnit.BASE`(米)和指数为 `-1` 的 `KTimeUnit.BASE`(秒)。无论从哪种单位或长度/时间组合创建,值始终
归一化为米每秒后存储。

## 构建速度

速度以**长度每时间表达式**构建,例如 `10 of kilo.meters / hours` 或 `100 of meters / (10 of seconds)` ——
两者都生成 `KSpeedUnitInstance`。用任意长度每时间模板读回(`v into (kilo.meters / hours)`)。刻意**没有**像
`metersPerSecond` 或 `kilometersPerHour` 这样拼写出来的复合令牌(它们正是 `meters / seconds` /
`kilo.meters / hours`)。

只有拥有真正单一、约定俗成名称的速度才作为值为 1 的令牌保留(用于 `of`/`into`):

| 速度 | 符号 | 令牌 | 1 单位对应 m/s |
|---|---|---:|---:|
| 节 | `kn` | `knots` | 0.514444(1852/3600) |
| 马赫(ISA 海平面) | `Ma` | `mach` | 340.29 |
| 光速 | `c` | `speedOfLight` | 299792458.0 |

> **马赫**是国际标准大气中海平面(15 °C)的声速。它是一个方便的参考点,而非物理常数 —— 实际声速随温度和
> 高度变化。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.miles
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.speed.*

val v = 50 of kilo.meters / hours
v.value                        // 13.888...(归一化为 m/s)
v into (kilo.meters / hours)   // 50.0(以 km/h 读回)
v into (miles / hours)         // ≈ 31.07
v into knots                   // ≈ 26.998
v into mach                    // ≈ 0.0408(声速的比例)
```

## 用核心单位(长度和时间)计算

这正是构造单位的意义所在。速度*就是*长度除以时间。KUnit 让你可以用普通的 `*` 和 `/` 在三个量 —— 长度、
时间和速度 —— 之间移动,并且每个结果都是**强类型**的。你永远不必自己构建或拆解原始的 `KMixedUnitInstance`。

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `length / time` | `KSpeedUnitInstance` | 速度 = 距离 / 时间 |
| `speed * time` | `KLengthUnitInstance` | 距离 = 速度 × 时间 |
| `time * speed` | `KLengthUnitInstance` | 距离(可交换) |
| `length / speed` | `KTimeUnitInstance` | 时间 = 距离 / 速度 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.speed.*

// --- 核心单位 -> 速度 ------------------------------------------------
val v = (100 of meters) / (10 of seconds)  // KSpeedUnitInstance(无需 .toSpeed()!)
v.value                    // 10.0(m/s)
v into (kilo.meters / hours) // 36.0
v into (miles / hours)     // ≈ 22.37
v into knots               // ≈ 19.44

// 为紧凑的速率给长度加前缀(无括号,`of` 比 `/` 结合更弱):
val fast = 10 of kilo.meters / hours   // KSpeedUnitInstance

// --- 速度 -> 长度(乘以时间) -------------------------------
val distance = v * (60 of seconds)     // KLengthUnitInstance
distance into meters       // 600.0
distance into feet         // ≈ 1968.5
(60 of seconds) * v        // 相同结果(可交换)

// --- 速度 -> 时间(用长度除以它) ------------------------------
val time = (600 of meters) / v         // KTimeUnitInstance
time into minutes          // 1.0
```

!!! warning "只有*纯*长度才能除成速度"
    `length / time` 和 `length / speed` 要求长度的指数为 1。**面积**(`m²`)或**体积**(`m³`)不是长度,所以
    `area / time` 会是 `m²/s` 而非速度 —— 运算符会抛出 `IllegalStateException`,而不是悄悄返回错误的值。要有意
    构建这样的中间结果,用 `toUnit()` 将一个操作数降到混合层级:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

val area = (2 of meters) * (2 of meters)         // KAreaUnitInstance
val areaPerTime = area.toUnit() / (2 of seconds).toUnit() // KMixedUnitInstance, [METER^2, SECOND^-1]
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*

// + / - : 同组,不同速度表达式间自动转换
val a = (36 of kilo.meters / hours) + (10 of meters / seconds)  // KSpeedUnitInstance, 20 m/s
val b = (20 of meters / seconds) - (36 of kilo.meters / hours)  // 10 m/s

// 比较(按归一化的 m/s 值)
(50 of kilo.meters / hours) > (10 of meters / seconds)   // true
(36 of kilo.meters / hours) == (10 of meters / seconds)  // true

// 两个速度之间的 * / / 会逃逸为 KMixedUnitInstance(不再是纯速度)
val squared = (10 of meters / seconds) * (2 of meters / seconds) // KMixedUnitInstance, [m^2, s^-2]
```

## toString 格式化

只存在基本单位的 `toString()`;通过 `into` 格式化特定单位:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*

(10 of meters / seconds).toString()   // "10.0 m/s"(基本单位)
"${(10 of meters / seconds) into (kilo.meters / hours)} km/h" // "36.0 km/h"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/` 表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `m/s` | `meters / seconds` | 速度，基本单位（米每秒）— 分数形式 |
| `m·s⁻¹` | `meters * (seconds pow -1)` | 同一速度写成带负指数的乘积 |
| `km/h` | `kilo.meters / hours` | 千米每小时 |
| `mi/h` | `miles / hours` | 英里每小时 |
| `100 m / 10 s` | `(100 of meters) / (10 of seconds)` | 由 长度 ÷ 时间 构造 |
