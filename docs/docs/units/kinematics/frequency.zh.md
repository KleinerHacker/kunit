# 频率

包：`org.pcsoft.framework.kunit.frequency`
基准单位：**赫兹** (`KFrequencyUnit.BASE == KFrequencyUnit.HERTZ`)

类型：**原生单位**

频率组用于建模某事在单位时间内发生的次数。它是一个 **原生的一维** 组，并且是 **时间的倒数** (`1 Hz = 1/s`)：
`KFrequencyUnitInstance` 包装单个 `KFrequencyUnit.HERTZ` 项，始终以赫兹归一化存储。

由于频率是时间的倒数，其跨组行为被定义为 **与时间完全相反**：乘以频率的行为与除以时间相同，除以频率的行为与乘以时间相同。

## 单位

| 单位 | 枚举值 | 符号 | 令牌 | 1 单位对应的赫兹值 |
|---|---|---|---:|---:|
| 赫兹 | `KFrequencyUnit.HERTZ` | `Hz` | `hertz` | 1.0 |
| 每秒转数 | `KFrequencyUnit.RPS` | `rps` | `rps` | 1.0 |
| 每秒帧数 | `KFrequencyUnit.FPS` | `fps` | `fps` | 1.0 |
| 每分钟转数 | `KFrequencyUnit.RPM` | `rpm` | `rpm` | 1/60 |
| 每分钟拍数 | `KFrequencyUnit.BPM` | `bpm` | `bpm` | 1/60 |

每个 `令牌` 都是值为 1 的 `KFrequencyUnitInstance`，配合 `of`（构建）和 `into`（读取）使用。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.frequency.*

val f = 2 of kilo.hertz      // 2000 Hz（通过 SI 前缀得到 kHz）
f.value                      // 2000.0（归一化为赫兹）
(3000 of rpm) into hertz     // 50.0（3000 rpm = 50 Hz）
(50 of hertz) into rpm       // 3000.0
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.frequency.*

// + / - ：同组，单位之间自动换算
val a = (1 of kilo.hertz) + (500 of hertz)   // KFrequencyUnitInstance: 1500.0 Hz
val b = (1 of kilo.hertz) - (500 of hertz)   // KFrequencyUnitInstance: 500.0 Hz

// 比较与相等（按归一化赫兹值）
(1 of kilo.hertz) == (1000 of hertz)         // true
(1 of kilo.hertz) > (500 of hertz)           // true
```

### 作为时间倒数的跨组运算符

频率与时间互为倒数，因此可组合为强类型结果：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.frequency.*

val f = 60 / (1 of seconds)          // KFrequencyUnitInstance, 60 Hz（次数 / 时间 = 频率）
val period = 1 / (2 of hertz)        // KTimeUnitInstance, 0.5 s（次数 / 频率 = 时间）
val count = (50 of hertz) * (2 of seconds)   // 100.0（频率 * 时间 = 无量纲次数）

val v = (2 of meters) * (5 of hertz) // KSpeedUnitInstance, 10 m/s（长度 * 频率 = 速度）
(v / (5 of hertz)) into meters       // 2.0（速度 / 频率 = 距离）
```

## 实际示例：旋转车轮的表面速度

一个周长为 **2 m** 的车轮以 **每秒 5 转** 旋转。将其周长乘以旋转频率即得表面（接触）速度——即
`length * frequency = speed`，是熟悉的 `length / time = speed` 的逆运算：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.frequency.*

val circumference = 2 of meters
val revolutions = 5 of rps               // 5 Hz
val surfaceSpeed = circumference * revolutions // KSpeedUnitInstance
surfaceSpeed.value                       // 10.0 m/s
```

## 使用 `pow` 求幂

用中缀运算符 `pow` 计算整数次幂（Kotlin 没有可重载的 `^`）。对于频率组，`pow` 返回通用的
`KMixedUnitInstance`（频率没有带量纲的幂类型）：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.frequency.*

val squared = (2 of hertz) pow 2     // KMixedUnitInstance: 4.0 Hz²
```

## SI 前缀

频率接受 **任意** 量级，因此每个 SI 前缀构建器（`quetta` … `quecto`）都可以通过属性访问与任意频率单位组合。
`kilo.hertz` 为 kHz，`mega.hertz` 为 MHz，`giga.hertz` 为 GHz。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.frequency.*

(1 of mega.hertz).value          // 1000000.0（MHz）
(2_400_000_000 of hertz) into giga.hertz // 2.4（GHz）
```

## toString 格式化

只有基准单位的 `toString()`；要格式化特定单位请使用 `into`：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.frequency.*

(1 of kilo.hertz).toString()             // "1000.0 Hz"（基准单位表示）
"${(50 of hertz) into rpm} rpm"          // "3000.0 rpm"
```

## 记法

下表展示该单位及其组成部分的数学写法与 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·` 表示乘法，`/` 表示分数。当一个量既可写成分数又可写成带负指数的乘积时，两种等价的 Kotlin 形式都会列出。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `Hz` | `hertz` | 频率，基准单位（赫兹） |
| `kHz` | `kilo.hertz` | 千赫兹（对赫兹应用前缀） |
| `1/s` = `s⁻¹` | `1 / (1 of seconds)` | 由周期得到的频率（类型化赫兹） |
| `Hz²` | `hertz pow 2` | 赫兹平方（通用混合单位） |
