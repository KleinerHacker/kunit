# 质量流量

包: `org.pcsoft.framework.kunit.mechanic.massflow`
基本单位: **千克每秒**(`KMassFlowUnit.BASE == KMassFlowUnit.KILOGRAMS_PER_SECOND`)

类型： **构造单位**

质量流量 `ṁ` 是单位时间内输送的质量——[体积流量](../kinematics/volume-flow.md)在质量维度上的对应量。 它是一个 **构造**
单位——组合 `mass · time⁻¹`(`kg/s`)。

`KMassFlowUnitInstance` 包装了一个恰好由两项组成的 `KMixedUnitInstance`，处于规范正规形式： 指数为 `+1` 的
`KMassUnit.BASE`(克)和指数为 `-1` 的 `KTimeUnit.BASE`(秒)。由于本库的质量分量 归一化为克，存储值为原始的以克为基准的分量值，以
kg/s 读取时除以固定因子。

## 命名单位

| 单位       | 符号   |                 令牌 |  1 单位换算为 kg/s |
|------------|--------|---------------------:|-------------------:|
| 千克每秒   | `kg/s` | `kilogramsPerSecond` |                1.0 |
| 克每秒     | `g/s`  |     `gramsPerSecond` |               1e-3 |
| 千克每小时 | `kg/h` |   `kilogramsPerHour` |             1/3600 |
| 吨每小时   | `t/h`  |      `tonnesPerHour` | 1000/3600 ≈ 0.2778 |
| 磅每秒     | `lb/s` |    `poundsPerSecond` |         0.45359237 |
| 磅每小时   | `lb/h` |      `poundsPerHour` |       ≈ 1.25998e-4 |

所有单位均支持完整的 SI 前缀范围 (用于计量泵的 `milli.gramsPerSecond`)。

## 分解方式

质量流量有两种等价的分解方式；两者都汇入同一个归一化工厂。

| 形式            | Kotlin                                         | 结果类型                |
|-----------------|------------------------------------------------|-------------------------|
| 质量 / 时间     | `mass / time`                                  | `KMassFlowUnitInstance` |
| 密度 × 体积流量 | `density * volumeflow`                         | `KMassFlowUnitInstance` |
| 原生表达式      | `(mass.toUnit() / time.toUnit()).toMassFlow()` | `KMassFlowUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.cubicMetersPerSecond
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.massflow.*

val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val viaMassTime = (2000 of kilo.grams) / (1 of seconds)
val viaDensityFlow = water * (2 of cubicMetersPerSecond)

viaMassTime == viaDensityFlow          // true —— 两者都是 2000 kg/s
viaMassTime into kilogramsPerSecond    // 2000.0
```

## 使用核心单位进行计算

| 表达式                                         | 结果类型                  | 含义                   |
|------------------------------------------------|---------------------------|------------------------|
| `mass / time`                                  | `KMassFlowUnitInstance`   | `ṁ = m / t`            |
| `massflow * time`、`time * massflow`           | `KMassUnitInstance`       | 输送的质量 `m = ṁ · t` |
| `mass / massflow`                              | `KTimeUnitInstance`       | 所需时间 `t = m / ṁ`   |
| `density * volumeflow`、`volumeflow * density` | `KMassFlowUnitInstance`   | `ṁ = ρ · Q`            |
| `massflow / density`                           | `KVolumeFlowUnitInstance` | `Q = ṁ / ρ`            |
| `massflow / volumeflow`                        | `KDensityUnitInstance`    | `ρ = ṁ / Q`            |

## 现实示例:水泵流量

一台水泵每小时输送 15 m³ 的水 (ρ = 998 kg/m³)。以 t/h 为单位的质量流量是多少？8 小时内 通过的质量是多少？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.volumeflow.cubicMetersPerHour
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.massflow.*

val water = (998 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val flow = water * (15 of cubicMetersPerHour)
flow into tonnesPerHour                 // ≈ 14.97

val perShift = flow * (8 of hours)      // KMassUnitInstance
perShift into kilo.grams                // ≈ 119760.0
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.massflow.*

val sum = (10 of kilogramsPerSecond) + (4 of kilogramsPerSecond) // 14 kg/s
(1 of kilogramsPerSecond) > (1 of tonnesPerHour)                 // true
(3.6 of tonnesPerHour) == (1 of kilogramsPerSecond)              // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.massflow.*

(2 of kilogramsPerSecond).toString()                     // "2.0 kg/s"(基本单位)
"${(2 of kilogramsPerSecond) into tonnesPerHour} t/h"    // "7.2 t/h"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/`
表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学        | Kotlin                          | 含义                         |
|-------------|---------------------------------|------------------------------|
| `kg/s`      | `kilogramsPerSecond`            | 质量流量，基本单位(命名令牌) |
| `kg·s⁻¹`    | `kilo.grams * (seconds pow -1)` | 同一量写成纯乘积             |
| `t/h`       | `tonnesPerHour`                 | 工业吞吐量读法               |
| `ṁ = m / t` | `mass / time`                   | 分解方式 A                   |
| `ṁ = ρ · Q` | `density * volumeflow`          | 分解方式 B                   |
| `Q = ṁ / ρ` | `massflow / density`            | 求解体积流量                 |
| `mg/s`      | `milli.gramsPerSecond`          | 带前缀的质量流量             |
