# 力学 — 概述

包：`org.pcsoft.framework.kunit.mechanic.mass`、`…force`、`…pressure`、`…density`、`…areadensity`、`…power`、
`…energy`

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
| 功率 | 构造 | 瓦特(`W`) | [功率(力学)](power.md) |
| 能量 | 构造 | 焦耳(`J`) | [能量(力学)](energy.md) |

功率与能量在技术上分别是**同一个**量,与其他学科领域共享;它们按领域分别记录并互相交叉引用
([功率(电气)](../electrical/power.md)、[功率(热力学)](../thermodynamics/power.md)、
[能量(电气)](../electrical/energy.md)、[能量(热力学)](../thermodynamics/energy.md))。

## 各量之间的关系

| 表达式 | 结果 | 公式 |
|---|---|---|
| `mass * acceleration` | 力 | `F = m · a` |
| `force / area` | 压力 | `p = F / A` |
| `pressure * area` | 力 | `F = p · A` |
| `mass / volume` | 密度 | `ρ = m / V` |
| `density * length` | 面密度 | `ρ_A = ρ · d` |
| `force * speed` | 功率 | `P = F · v` |
| `power / speed` | 力 | `F = P / v` |
| `power / force` | 速度 | `v = P / F` |
| `force * length` | 能量(功) | `W = F · s` |
| `power * time` | 能量 | `W = P · t` |
| `energy / time` | 功率 | `P = W / t` |
| `energy / power` | 时间 | `t = W / P` |

## 实例 —— 牛顿第二定律与接地压强

将一个 **2 kg** 的物块以标准重力加速,并将由此产生的重力分布在 **0.5 m²** 的接触面上。力为
`F = m · a`,压强为 `p = F / A`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.acceleration.*
import org.pcsoft.framework.kunit.mechanic.force.*
import org.pcsoft.framework.kunit.mechanic.pressure.*

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
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mechanic.density.*

val steel = (7850 of kilo.grams) / (1 of (meters pow 3)) // KDensityUnitInstance,7850 kg/m³
val mass = steel * (2 of liters)                          // KMassUnitInstance
mass into kilo.grams                                      // 15.7(每 2 L 的 kg)
```

## 实例 —— 绞盘的功与功率

一台绞盘以 **100 N** 的力将物体拉动 **5 m**,耗时 **5 s**。功为 `W = F · s`,功率
`P = W / t` —— 这与直接的力学形式 `P = F · v` 结果相同:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.common.power.*
import org.pcsoft.framework.kunit.common.energy.*

val w = (100 of newtons) * (5 of meters)                    // KEnergyUnitInstance
w into joules                                                // 500.0

val p = w / (5 of seconds)                                   // KPowerUnitInstance
p into watts                                                 // 100.0

val direct = (100 of newtons) * ((1 of meters) / (1 of seconds)) // P = F · v,100 W
p == direct                                                  // true
```

## 输出值(`toString`)

`toString()` 以该组的**基准单位**(值 + 符号)输出值;对于其他单位,在字符串模板中用 `into` 读取并自行
附加符号:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.force.*

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
| `W = F · s` | `(100 of newtons) * (5 of meters)` | 力×长度得功 |
| `P = F · v` | `(100 of newtons) * ((1 of meters) / (1 of seconds))` | 力×速度得功率 |
| `P = W / t` | `w / (5 of seconds)` | 功÷时间得功率 |

## 后续阅读

* [质量](mass.md) —— 原生基本量(以克归一化)。
* [力](force.md) 与 [压力](pressure.md) —— 牛顿定律与单位面积上的力。
* [密度](density.md) 与 [面密度](areadensity.md) —— 单位体积与单位表面的质量。
