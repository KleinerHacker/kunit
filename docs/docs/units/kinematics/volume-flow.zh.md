# 体积流量

包：`org.pcsoft.framework.kunit.kinematic.volumeflow`
基本单位：**立方米每秒**（`KVolumeFlowUnit.BASE == KVolumeFlowUnit.CUBIC_METER_PER_SECOND`）

类型：**构造单位**

体积流量（容积流率）描述单位时间内流经某截面的体积：
`distance³ · time⁻¹`（`m³/s`）。`KVolumeFlowUnitInstance` 包装了一个恰好由两项组成的
`KMixedUnitInstance` —— 一个 `KDistanceUnit.BASE`（米）项，指数为 `+3`，以及一个
`KTimeUnit.BASE`（秒）项，指数为 `-1`。无论该值是由哪种单位或体积/时间组合构建而来，
它始终以立方米每秒归一化存储。

与能量或功率不同，体积流量**没有**质量维度，因此其存储值*就是* `m³/s` 中的读数 ——
不涉及克/千克的桥接。

## 命名单位

| 单位 | 符号 | 令牌 | 1 单位相当于多少 m³/s |
|---|---|---:|---:|
| 立方米每秒 | `m³/s` | `cubicMetersPerSecond` | 1.0 |
| 立方米每小时 | `m³/h` | `cubicMetersPerHour` | 1/3600 ≈ 2.778e-4 |
| 升每秒 | `l/s` | `litersPerSecond` | 0.001 |
| 升每分钟 | `l/min` | `litersPerMinute` | 0.001/60 ≈ 1.667e-5 |
| 美制加仑每分钟 | `gpm` | `usGallonsPerMinute` | ≈ 6.309e-5 |

以上单位均支持完整的 SI 前缀范围（`milli.litersPerSecond`、`kilo.cubicMetersPerHour` 等）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

val q = 5 of litersPerSecond
q.value                       // 0.005（归一化为 m³/s）
q into litersPerMinute        // 300.0
q into cubicMetersPerHour     // 18.0
q into usGallonsPerMinute     // ≈ 79.25
(250 of milli.litersPerSecond) into litersPerSecond // 0.25
```

## 现实示例：给雨水箱注水

一台园艺水泵以 300 l/min 的速度向一个 5 m³ 的水箱注水。水箱需要多久才能注满，
以及该流量用水泵铭牌上惯用的单位表示是多少？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

val pump = 300 of litersPerMinute
val tank = 5000 of liters

val fillTime = tank / pump          // KTimeUnitInstance
fillTime into minutes               // ≈ 16.67 分钟

pump into cubicMetersPerHour        // 18.0 m³/h（铭牌单位）
pump into usGallonsPerMinute        // ≈ 79.25 gpm

// 反过来：一刻钟能供多少水？
val volume = pump * (15 of minutes) // KVolumeUnitInstance
volume into liters                  // 4500.0
```

## 用核心单位（体积与时间）计算

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `volume / time` | `KVolumeFlowUnitInstance` | 流量 = 体积 / 时长 |
| `volumeFlow * time` | `KVolumeUnitInstance` | 体积 = 流量 × 时长 |
| `time * volumeFlow` | `KVolumeUnitInstance` | 体积（可交换） |
| `volume / volumeFlow` | `KTimeUnitInstance` | 时长 = 体积 / 流量 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

val q = (600 of liters) / (2 of minutes)  // KVolumeFlowUnitInstance
q into cubicMetersPerSecond               // 0.005

val v = q * (60 of seconds)               // KVolumeUnitInstance
v into liters                             // 300.0

val t = (600 of liters) / q               // KTimeUnitInstance
t into minutes                            // 2.0
```

## 分解方式

体积流量可以通过两种方式得到；两者都产生相同的类型化、值相等的实例。

| 分解方式 | 形式 | 结果 |
|---|---|---|
| `volume / time` | 类型化操作符 | 直接得到 `KVolumeFlowUnitInstance` |
| `distance³ · time⁻¹` | 原生表达式 + `toVolumeFlow()` | `KVolumeFlowUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

// 类型化操作符形式
val typed = (8000 of liters) / (4 of seconds)

// 原生基础维度形式（m³ · s⁻¹），由 toVolumeFlow() 识别
val native = (((2 of meters).toUnit() pow 3) / (4 of seconds).toUnit()).toVolumeFlow()

typed == native // true —— 两者都是 2.0 m³/s
```

`toVolumeFlow()` 只识别**规范正规形式**（一个指数为 `+3` 的 `KDistanceUnit` 项和一个指数为
`-1` 的 `KTimeUnit` 项）；任何等价表达式都会自动归约到该形式上。错误的形状会抛出
`IllegalStateException`，而不是悄悄返回一个错误的值。

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

// + / - ：同组，不同流量单位之间自动转换
val a = (1 of litersPerSecond) + (60 of litersPerMinute)   // 2 l/s
val b = (1 of litersPerSecond) - (30 of litersPerMinute)   // 0.5 l/s

// 比较（按归一化的 m³/s 值）
(1 of litersPerSecond) > (30 of litersPerMinute)   // true
(1 of litersPerSecond) == (60 of litersPerMinute)  // true

// 两个流量之间的 * / / 会脱离到 KMixedUnitInstance
val squared = (1 of litersPerSecond) * (1 of litersPerSecond) // KMixedUnitInstance, [m^6, s^-2]
```

## toString 格式化

`toString()` 以基本单位输出值；如需其他单位，请使用 `into`：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

(5 of litersPerSecond).toString()                       // "0.005 m³/s"
"${(5 of litersPerSecond) into litersPerMinute} l/min"  // "300.0 l/min"
```

## 记法

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `m³/s` | `cubicMetersPerSecond` | 体积流量，基本单位 —— 命名令牌 |
| `m³·s⁻¹` | `(meters pow 3) / seconds` | 作为基础维度表达式的相同流量 |
| `l/s` | `litersPerSecond` | 升每秒 |
| `l/min` | `litersPerMinute` | 升每分钟 |
| `m³/h` | `cubicMetersPerHour` | 立方米每小时 |
| `V / t` | `(600 of liters) / (2 of minutes)` | 由体积 ÷ 时间构建 |
| `V = q̇ · t` | `q * (60 of seconds)` | 由流量 × 时长得到体积 |
| `t = V / q̇` | `(600 of liters) / q` | 由体积 ÷ 流量得到时长 |
