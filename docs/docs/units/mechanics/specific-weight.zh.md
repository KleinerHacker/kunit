# 比重

包: `org.pcsoft.framework.kunit.mechanic.specificweight`
基本单位: **牛顿每立方米**
(`KSpecificWeightUnit.BASE == KSpecificWeightUnit.NEWTON_PER_CUBIC_METER`)

类型: **构造单位**

比重 `γ` 是单位体积材料所受的 **重力**: `γ = F / V = ρ · g`。流体静力学正是以此为基础——
某深度处的压力可简单表示为 `p = γ · h`——而土木工程中也常用它来描述土壤和建筑材料。水的比重约为
9.81 kN/m³。

其规范的基本量纲标准形式为 `mass · length⁻² · time⁻²`。

!!! note "是重量，不是质量"
    比重取决于当地的重力加速度，而 [密度](density.zh.md) 则不然。在月球上，某种材料的密度保持不变，
    但其比重约为地球上的六分之一。

## 命名单位

| 单位                       | 符号     |                     词元 | 1 单位对应的 N/m³ |
|----------------------------|------------|--------------------------:|---------------:|
| 牛顿每立方米                 | `N/m^3`    |    `newtonsPerCubicMeter` |            1.0 |
| 千牛顿每立方米               | `kN/m^3`   | `kilonewtonsPerCubicMeter` |           1000 |
| 磅力每立方英尺               | `lbf/ft^3` | `poundsForcePerCubicFoot` |     ≈ 157.0875 |

所有词元均可接受任何 SI 词头。与相邻的力、压力和密度单位组一样，实例存储的是
**以克为基础的原始分量值**；以 N/m³ 读取时会除以 1000。

## 分解形式

该单位组有 **两种** 分解形式。两者都汇入同一个规范化工厂:

| 形式               | 表达式                                                        |
|--------------------|------------------------------------------------------------------|
| 类型化运算符 A     | `force / volume`                                                 |
| 类型化运算符 B     | `density * acceleration` (`γ = ρ · g`)                            |
| 原生形式 (`toX()`) | `(1 of kilo.grams / m² / s²).toSpecificWeight()`                  |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.acceleration.standardGravities
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.specificweight.*

val cubicMeter = (1 of meters) * (1 of meters) * (1 of meters)
val water = (1000 of kilo.grams) / cubicMeter

val viaForce = (9806.65 of newtons) / cubicMeter        // A
val viaDensity = water * (1 of standardGravities)       // B

viaForce == viaDensity                                   // true
viaForce into newtonsPerCubicMeter                       // 9806.65
```

## 使用该单位组进行计算

| 表达式                           | 结果类型                       | 含义                  |
|------------------------------------|-----------------------------------|-------------------------|
| `force / volume`                  | `KSpecificWeightUnitInstance`    | `γ = F / V`             |
| `density * acceleration`          | `KSpecificWeightUnitInstance`    | `γ = ρ · g`             |
| `specificWeight * volume`         | `KForceUnitInstance`             | 重力                     |
| `force / specificWeight`          | `KVolumeUnitInstance`            | 所占据的体积              |
| `specificWeight / acceleration`   | `KDensityUnitInstance`           | 回到 `ρ`                |
| `specificWeight / density`        | `KAccelerationUnitInstance`      | 回到 `g`                |

## 实际案例——水箱的重量

一个 **300 升** 的水箱，以及其内容物对地面施加的力:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.specificweight.*

val water = 9.80665 of kilonewtonsPerCubicMeter
val weight = water * (300 of liters)      // KForceUnitInstance
weight into newtons                        // ≈ 2942.0 N

// 反过来：重达 1 kN 的水体积是多少？
val v = (1000 of newtons) / water          // KVolumeUnitInstance
v into liters                               // ≈ 102.0 l
```

## 值语义

`equals`/`hashCode` 比较的是 **归一化后的分量值**，因此
`(1 of kilonewtonsPerCubicMeter) == (1000 of newtonsPerCubicMeter)`。`toString()` 以基本单位表示该值:
`"9807.0 N/m^3"`。

## 另请参阅

* [密度](density.zh.md)——不依赖重力的、以质量为基础的对应量。
* [力](force.zh.md) 和 [压力](pressure.zh.md)——相邻的单位组。
* [力学概述](overview.zh.md)
