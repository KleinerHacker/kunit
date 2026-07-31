# 表面张力

包: `org.pcsoft.framework.kunit.mechanic.lineforce`
基本单位: **牛顿每米**(`KLineForceUnit.BASE == KLineForceUnit.NEWTONS_PER_METER`)

类型： **构造单位**

表面张力 `σ` 是产生单位新表面所需的能量，等价地也是沿接触线作用的单位长度上的力：
`1 J/m² = 1 N/m`。其维度是 `mass · time⁻²`。

这恰好是 **单位长度上的力**的维度，与[刚度](stiffness.md)共享。因此 KUnit 为这两种读法建模了 一个中性组 `lineforce`
；表面张力是其中一种读法。本页描述该读法。

!!! note "一个组，两种读法"
`KLineForceUnitInstance` 是共享类型。除了你为其起的名字之外，没有什么能区分表面张力和 弹簧刚度系数——该组使用中性命名，以免任何一种读法占用另一种的名字。

## 命名单位

| 单位       | 符号     |                   令牌 | 1 单位等于多少 N/m |
|------------|----------|-----------------------:|-------------------:|
| 牛顿每米   | `N/m`    |      `newtonsPerMeter` |                1.0 |
| 达因每厘米 | `dyn/cm` |   `dynesPerCentimeter` |               1e-3 |
| 牛顿每毫米 | `N/mm`   | `newtonsPerMillimeter` |             1000.0 |
| 磅力每英寸 | `lbf/in` |   `poundsForcePerInch` |          ≈ 175.127 |
| 千克力每米 | `kp/m`   |    `kilopondsPerMeter` |            9.80665 |

表面张力通常以 mN/m 或数值相同的 dyn/cm 给出：25 °C 的水约为 72 mN/m = 72 dyn/cm。 毫牛顿每米是带前缀的写法
`milli.newtonsPerMeter`。

## 分解方式

| 形式        | Kotlin                                                  | 结果类型                 |
|-------------|---------------------------------------------------------|--------------------------|
| 能量 / 面积 | `energy / area`                                         | `KLineForceUnitInstance` |
| 力 / 长度   | `force / length`                                        | `KLineForceUnitInstance` |
| 原生表达式  | `(mass.toUnit() / (time.toUnit() pow 2)).toLineForce()` | `KLineForceUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val viaEnergy = (2 of joules) / ((1 of meters) * (1 of meters))
val viaForce = (2 of newtons) / (1 of meters)

viaEnergy == viaForce                  // true - 两者都是 2 N/m
(72 of milli.joules) / ((1 of meters) * (1 of meters)) into dynesPerCentimeter // 72.0
```

## 使用核心单位计算

| 表达式                                     | 结果类型                 | 含义               |
|--------------------------------------------|--------------------------|--------------------|
| `energy / area`                            | `KLineForceUnitInstance` | `σ = W / A`        |
| `lineforce * area`、`area * lineforce`     | `KEnergyUnitInstance`    | 表面能 `W = σ · A` |
| `energy / lineforce`                       | `KAreaUnitInstance`      | `A = W / σ`        |
| `force / length`                           | `KLineForceUnitInstance` | `σ = F / l`        |
| `lineforce * length`、`length * lineforce` | `KForceUnitInstance`     | `F = σ · l`        |

## 现实示例：吹起肥皂膜所需的能量

吹起一个 0.05 m² 的肥皂膜 (两个表面，每个表面 σ ≈ 25 mN/m)。这需要多少能量？ 该膜对一根 10 cm 的金属丝施加多大的力？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val sigma = 25 of milli.newtonsPerMeter
val area = (0.5 of meters) * (0.1 of meters)   // 0.05 m²

val energy = sigma * area                       // KEnergyUnitInstance
energy into milli.joules                        // 1.25

val force = sigma * (10 of centi.meters)        // KForceUnitInstance
force into milli.newtons                        // 2.5
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val sum = (72 of dynesPerCentimeter) + (8 of dynesPerCentimeter) // 80 dyn/cm
(72 of dynesPerCentimeter) > (50 of milli.newtonsPerMeter)       // true
(1 of dynesPerCentimeter) == (1 of milli.newtonsPerMeter)        // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineforce.*

(72 of dynesPerCentimeter).toString()                     // "0.072 N/m"(基本单位)
"${(72 of dynesPerCentimeter) into dynesPerCentimeter} dyn/cm" // "72.0 dyn/cm"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/`
表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学        | Kotlin                          | 含义                   |
|-------------|---------------------------------|------------------------|
| `N/m`       | `newtonsPerMeter`               | 表面张力，基本单位     |
| `kg·s⁻²`    | `kilo.grams * (seconds pow -2)` | 同一量以基础维度表示   |
| `mN/m`      | `milli.newtonsPerMeter`         | 日常使用的表面张力读法 |
| `dyn/cm`    | `dynesPerCentimeter`            | CGS 读法(= 1 mN/m)     |
| `σ = W / A` | `energy / area`                 | 分解方式 A             |
| `σ = F / l` | `force / length`                | 分解方式 B             |
| `W = σ · A` | `lineforce * area`              | 表面能                 |
