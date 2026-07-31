# 动力粘度

包: `org.pcsoft.framework.kunit.mechanic.viscosity`
基本单位: **帕斯卡秒**(`KViscosityUnit.BASE == KViscosityUnit.PASCAL_SECOND`)

类型： **构造单位**

动力粘度 `η` 描述了流体对剪切的抵抗能力。它是一个 **构造**单位——由 `pressure · time` 组成， 即 `mass · length⁻¹ · time⁻¹`
(`Pa·s`)。

`KViscosityUnitInstance` 包装了一个恰好由三项组成的 `KMixedUnitInstance`，采用规范的归一化形式：
`KMassUnit.BASE`(克)指数为 `+1`，`KDistanceUnit.BASE`(米)指数为 `-1`，`KTimeUnit.BASE`(秒)指数为 `-1`。
由于本库的质量分量以克为归一化基准，存储的值就是原始的以克为基础的分量值，而以 Pa·s 表示的读数 需要除以一个固定因子。

!!! note "动力粘度与运动粘度"
**运动**粘度 `ν = η / ρ`(`m²/s`)是不同的物理量，位于扩散率组中——参见
[运动粘度](kinematic-viscosity.md)。

## 命名单位

| 单位             | 符号         |                             令牌 | 1 单位等于多少 Pa·s |
|------------------|--------------|---------------------------------:|--------------------:|
| 帕斯卡秒         | `Pa*s`       |                  `pascalSeconds` |                 1.0 |
| 泊               | `P`          |                         `poises` |                 0.1 |
| 磅力秒每平方英尺 | `lbf*s/ft^2` | `poundForceSecondsPerSquareFoot` |           ≈ 47.8803 |
| 雷恩(lbf·s/in²)  | `reyn`       |                          `reyns` |          ≈ 6894.757 |

针对类水流体，两种日常写法都是带前缀的形式，而不是独立的令牌： **毫帕斯卡秒**是
`milli.pascalSeconds`， **厘泊**是 `centi.poises`——且两者相等 (`1 mPa·s = 1 cP`，即 20 °C 的水)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.viscosity.*

val water = 1 of milli.pascalSeconds
water into centi.poises  // 1.0
water into pascalSeconds // 0.001
(1 of poises) into pascalSeconds // 0.1
```

## 使用核心单位计算 (压强与时间)

| 表达式                               | 结果类型                   | 含义                 |
|--------------------------------------|----------------------------|----------------------|
| `pressure * time`、`time * pressure` | `KViscosityUnitInstance`   | `η = p · t`          |
| `viscosity / pressure`               | `KTimeUnitInstance`        | `t = η / p`          |
| `viscosity / time`                   | `KPressureUnitInstance`    | `p = η / t`          |
| `viscosity / density`                | `KDiffusivityUnitInstance` | 运动粘度 `ν = η / ρ` |
| `viscosity / diffusivity`            | `KDensityUnitInstance`     | `ρ = η / ν`          |

原生形式通过 `toViscosity()` 转换：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.viscosity.*

val typed = (2 of pascals) * (3 of seconds)
val native = ((2 of pascals).toUnit() * (3 of seconds).toUnit()).toViscosity()

typed == native            // true - 两者都是 6 Pa·s
typed into pascalSeconds   // 6.0
```

## 现实示例：工作温度下的发动机机油

一款 SAE 30 机油在 100 °C 时测得 9.3 cP，密度为 850 kg/m³。换算成 Pa·s 是多少？ 它对应的运动粘度又是多少？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.diffusivity.centistokes
import org.pcsoft.framework.kunit.common.diffusivity.div
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.viscosity.*
import org.pcsoft.framework.kunit.pow

val oil = 9.3 of centi.poises
oil into pascalSeconds        // 0.0093

val rho = (850 of kilo.grams) / (1 of (meters pow 3))
val nu = oil / rho            // KDiffusivityUnitInstance
nu into centistokes           // ≈ 10.94
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.viscosity.*

val sum = (10 of pascalSeconds) + (4 of pascalSeconds) // 14 Pa·s
(1 of poises) > (1 of milli.pascalSeconds)             // true
(1 of poises) == (100 of milli.pascalSeconds)          // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.mechanic.viscosity.*

(2 of pascalSeconds).toString()                    // "2.0 Pa*s"(基本单位)
"${(2 of pascalSeconds) into centi.poises} cP"     // "2000.0 cP"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/`
表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学         | Kotlin                                            | 含义                         |
|--------------|---------------------------------------------------|------------------------------|
| `Pa·s`       | `pascalSeconds`                                   | 动力粘度，基本单位(命名令牌) |
| `kg·m⁻¹·s⁻¹` | `kilo.grams * (meters pow -1) * (seconds pow -1)` | 同一量以纯乘积表示           |
| `cP`         | `centi.poises`                                    | 厘泊(= 1 mPa·s)              |
| `η = p · t`  | `pressure * time`                                 | 类型化分解                   |
| `ν = η / ρ`  | `viscosity / density`                             | 运动粘度                     |
| `mPa·s`      | `milli.pascalSeconds`                             | 带前缀的粘度读法             |
