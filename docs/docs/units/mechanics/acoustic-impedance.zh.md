# 比声阻抗

包: `org.pcsoft.framework.kunit.mechanic.acousticimpedance`
基本单位: **帕斯卡秒每米**
(`KAcousticImpedanceUnit.BASE == KAcousticImpedanceUnit.PASCAL_SECOND_PER_METER`)

类型: **构造单位**

比声阻抗 `Z` 是介质在单位质点速度下产生的声压:
`Z = p / v = ρ · c`。它决定了在边界处有多少声音被反射——空气约为 413 Pa·s/m，水约为
1.48 MPa·s/m，两者比值约为 3600，这就是为什么空气中的声音几乎无法进入水中的原因。

其规范的基本量纲标准形式为 `mass · length⁻² · time⁻¹`。

## 命名单位

| 单位                    | 符号         |                   词元 | 1 单位对应的 Pa·s/m |
|-------------------------|--------------|------------------------:|-----------------:|
| 帕斯卡秒每米             | `Pa*s/m`     | `pascalSecondsPerMeter` |              1.0 |
| SI 瑞利                  | `rayl`       |                 `rayls` |              1.0 |
| CGS 瑞利                 | `rayl (CGS)` |              `cgsRayls` |               10 |

`rayls` 是基本单位的另一种写法，而非独立的单位。所有词元均可接受任何 SI 词头
(`mega.rayls` 是组织和水中常用的表示)。与相邻的力、压力和密度单位组一样，实例存储的是
**以克为基础的原始分量值**。

## 分解形式

该单位组有 **两种** 分解形式。两者都汇入同一个规范化工厂:

| 形式               | 表达式                                                          |
|--------------------|------------------------------------------------------------------|
| 类型化运算符 A     | `pressure / speed`                                              |
| 类型化运算符 B     | `density * speed` (`Z = ρ · c`，特性阻抗)                        |
| 原生形式 (`toX()`) | `(1 of kilo.grams / m² / s).toAcousticImpedance()`               |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.acousticimpedance.*

val air = (1.204 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val c = (343 of meters) / (1 of seconds)

val viaDensity = air * c                                        // B
val viaPressure = (412.972 of pascals) / ((1 of meters) / (1 of seconds))  // A

viaDensity into rayls        // ≈ 412.97
viaPressure into rayls       // ≈ 412.97
```

## 使用该单位组进行计算

| 表达式                            | 结果类型                            | 含义                    |
|------------------------------------|--------------------------------------|--------------------------|
| `pressure / speed`                | `KAcousticImpedanceUnitInstance`   | `Z = p / v`              |
| `density * speed`                 | `KAcousticImpedanceUnitInstance`   | `Z = ρ · c`              |
| `acousticImpedance * speed`       | `KPressureUnitInstance`            | 声压                      |
| `pressure / acousticImpedance`    | `KSpeedUnitInstance`               | 质点速度                  |
| `acousticImpedance / speed`       | `KDensityUnitInstance`             | 回到 `ρ`                 |
| `acousticImpedance / density`     | `KSpeedUnitInstance`               | 回到 `c`                 |

## 实际案例——空气与水的边界

为什么对着水下游泳者的头喊话没有用？比较两者的特性阻抗:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.acousticimpedance.*

val air = (1.204 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

val zAir = air * ((343 of meters) / (1 of seconds))
val zWater = water * ((1480 of meters) / (1 of seconds))

zAir into rayls              // ≈ 413
zWater into mega.rayls       // ≈ 1.48

(zWater into rayls) / (zAir into rayls)   // ≈ 3584 —— 几乎完全反射
```

## 值语义

`equals`/`hashCode` 比较的是 **归一化后的分量值**，因此 `(1 of cgsRayls) == (10 of rayls)`。
`toString()` 以基本单位表示该值: `"413.0 Pa*s/m"`。

## 另请参阅

* [密度](density.zh.md) 和 [速度](../kinematics/speed.zh.md)——`Z = ρ · c` 的两个因子。
* [压力](pressure.zh.md)——声压那一侧。
* [力学概述](overview.zh.md)
