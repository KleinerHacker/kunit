# 加速度

包: `org.pcsoft.framework.kunit.acceleration`
基本单位: **米每二次方秒**(`KAccelerationUnit.BASE == KAccelerationUnit.METERS_PER_SECOND_SQUARED`)

加速度是一个**构造**单位,即组合 `length · time⁻²`(`m/s²`)。`KAccelerationUnitInstance` 包装一个恰好包含
两项的 `KMixedUnitInstance`——指数为 `+1` 的 `KDistanceUnit.BASE`(米)和指数为 `-2` 的 `KTimeUnit.BASE`(秒)。
其值始终归一化为 m/s²。由于基本单位与分量基本单位(米、秒)一致,因此没有额外的比例因子。

## 构建加速度

加速度通常由 `speed / time` 构建,或使用命名令牌。故意**没有** `metersPerSecondSquared` 令牌(它就是
`meters / (seconds pow 2)`)。只有真正有名称的单位才作为值为 1 的令牌保留(与 `of`/`into` 一起使用):

| 加速度 | 符号 | 令牌 | 1 单位换算为 m/s² |
|---|---|---:|---:|
| 伽(伽利略) | `Gal` | `gals` | 0.01(1 cm/s²) |
| 标准重力 | `g₀` | `standardGravities` | 9.80665 |

两个令牌都支持完整的 SI 前缀(例如 `milli.gals` = 1 mGal,重力测量的日常单位)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.acceleration.*

val a = 5 of gals               // KAccelerationUnitInstance
a.value                         // 0.05(归一化为 m/s²)
a into standardGravities        // ≈ 0.0051
(1 of milli.gals).value         // 0.00001(1 mGal)
```

## 使用核心单位(速度与时间)进行计算

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `speed / time` | `KAccelerationUnitInstance` | 加速度 = Δ速度 / 时间 |
| `acceleration * time` | `KSpeedUnitInstance` | 速度 = 加速度 × 时间 |
| `time * acceleration` | `KSpeedUnitInstance` | 速度(可交换) |
| `speed / acceleration` | `KTimeUnitInstance` | 时间 = 速度 / 加速度 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*
import org.pcsoft.framework.kunit.acceleration.*

val a = ((100 of meters) / (10 of seconds)) / (5 of seconds) // KAccelerationUnitInstance, 2 m/s²
val v = a * (3 of seconds)      // KSpeedUnitInstance, 6 m/s
val t = ((100 of meters) / (10 of seconds)) / a             // KTimeUnitInstance
t into seconds                  // 5.0
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.acceleration.*

// + / - : 同组,不同加速度表达式之间自动转换
val s = (10 of gals) + (4 of gals)   // 0.14 m/s²
(10 of gals) > (4 of gals)           // true
// 两个加速度之间的 * / / 会“逃逸”为 KMixedUnitInstance
(10 of gals) * (2 of gals)           // KMixedUnitInstance
```

## toString 格式化

仅存在基本单位的 `toString()`;通过 `into` 格式化特定单位:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.acceleration.*

(1 of gals).toString()               // "0.01 m/s²"(基本单位)
"${(1 of standardGravities) into gals} Gal" // "980.665 Gal"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/` 表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `m/s²` | `meters / (seconds pow 2)` | 加速度，基本单位（米每二次方秒）— 分数形式 |
| `m·s⁻²` | `meters * (seconds pow -2)` | 同一加速度写成带负指数的乘积 |
| `Gal` | `gals` | 命名单位（1 cm/s²） |
| `Δv / t` | `speed / time` | 由 速度 ÷ 时间 构造 |
