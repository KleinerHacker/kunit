# 力学 — 概述

包：`org.pcsoft.framework.kunit.mass`、`…force`、`…pressure`、`…density`、`…areadensity`

力学(动力学)追问物体**为何**运动,以及物质如何分布 —— 质量、作用于它的力、力在面积上施加的压力,
以及体积或表面中填充了多少质量之间的相互作用。在[运动学](../kinematics/overview.md)的速率之上,
本主题增加了 1 个**原生**基本量(质量)以及由质量、长度、时间**构造**的 4 个量。

## 本主题的单位

| 单位 | 类型 | 基准单位 | 页面 |
|---|---|---|---|
| 质量 | 原生 | 克(`g`) | [质量](mass.md) |
| 力 | 构造 | 牛顿(`N`) | [力](force.md) |
| 压力 | 构造 | 帕斯卡(`Pa`) | [压力](pressure.md) |
| 密度 | 构造 | 千克每立方米(`kg/m³`) | [密度](density.md) |
| 面密度 | 构造 | 千克每平方米(`kg/m²`) | [面密度](areadensity.md) |

## 各量之间的关系

| 表达式 | 结果 | 公式 |
|---|---|---|
| `mass * acceleration` | 力 | `F = m · a` |
| `force / area` | 压力 | `p = F / A` |
| `pressure * area` | 力 | `F = p · A` |
| `mass / volume` | 密度 | `ρ = m / V` |
| `density * length` | 面密度 | `ρ_A = ρ · d` |

## 实例 —— 牛顿第二定律与接地压强

将一个 **2 kg** 的物块以标准重力加速,并将由此产生的重力分布在 **0.5 m²** 的接触面上。力为
`F = m · a`,压强为 `p = F / A`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.acceleration.*
import org.pcsoft.framework.kunit.force.*
import org.pcsoft.framework.kunit.pressure.*

val f = (2 of kilo.grams) * (1 of standardGravities)  // KForceUnitInstance
f into newtons                                         // ≈ 19.61(N)

val area = (1 of meters) * (0.5 of meters)             // KAreaUnitInstance,0.5 m²
val p = f / area                                       // KPressureUnitInstance
p into pascals                                         // ≈ 39.23(Pa)
```

## 实例 —— 由密度求钢制零件的质量

钢的密度为 **7850 kg/m³**。一个 **2 L** 零件的质量为 `m = ρ · V`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*

val steel = (7850 of kilo.grams) / (1 of (meters pow 3)) // KDensityUnitInstance,7850 kg/m³
val mass = steel * (2 of liters)                          // KMassUnitInstance
mass into kilo.grams                                      // 15.7(每 2 L 的 kg)
```

## 输出值(`toString`)

`toString()` 以该组的**基准单位**(值 + 符号)输出值;对于其他单位,在字符串模板中用 `into` 读取并自行
附加符号:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.force.*

val f = 10 of newtons
f.toString()                 // "10.0 N"(基准单位)
"${f into kilo.newtons} kN"  // "0.01 kN"
```

## 记法

下表以数学表记与 KUnit 的 Kotlin 表记对照本领域的核心关系。指数使用 Unicode 上标(`²`、`³`、`⁻¹`),
`·` 表示乘法,`/` 表示分数。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `F = m · a` | `(2 of kilo.grams) * (1 of standardGravities)` | 质量×加速度得力 |
| `p = F / A` | `f / area` | 力÷面积得压强 |
| `F = p · A` | `p * area` | 压强×面积得力 |
| `ρ = m / V` | `(6 of kilo.grams) / (2 of liters)` | 质量÷体积得密度 |
| `m = ρ · V` | `steel * (2 of liters)` | 密度×体积得质量 |

## 后续阅读

* [质量](mass.md) —— 原生基本量(以克归一化)。
* [力](force.md) 与 [压力](pressure.md) —— 牛顿定律与单位面积上的力。
* [密度](density.md) 与 [面密度](areadensity.md) —— 单位体积与单位表面的质量。
