# 磁偶极矩

包: `org.pcsoft.framework.kunit.electric.magneticmoment`
基本单位: **安培平方米**
(`KMagneticMomentUnit.BASE == KMagneticMomentUnit.AMPERE_SQUARE_METER`)

类型: **构成单位**

一个电流环的磁偶极矩 `m` 是电流乘以它所围成的面积: `m = I · A`。
它决定了磁场对该环产生的力矩,也是原子和原子核磁性(玻尔磁子和核磁子)所用的量。

其规范的基本量纲标准形式为 `current · length²`。

## 具名单位

| 单位                | 符号    |                标记 |     1单位折合 A·m² |
|---------------------|---------|---------------------:|-------------------:|
| 安培平方米          | `A*m^2` | `ampereSquareMeters` |                1.0 |
| 焦耳每特斯拉        | `J/T`   |      `joulesPerTesla` |                1.0 |
| 玻尔磁子            | `μB`    |       `bohrMagnetons` | 9.2740100783e-24   |
| 核磁子              | `μN`    |    `nuclearMagnetons` | 5.0507837461e-27   |

`joulesPerTesla` 是同一单位基于能量的写法 — 表示偶极子每单位磁通密度获得的能量。所有标记都支持任意 SI 词头。

## 分解

该组只有一种分解,两种形式都产生数值相等的同类型实例:

| 形式             | 表达式                                                       |
|------------------|-------------------------------------------------------------------|
| 带类型运算符     | `current * area`                                                 |
| 原生 (`toX()`)   | `((2 of amperes).toUnit() * loop.toUnit()).toMagneticMoment()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.magneticmoment.*

val loop = (0.1 of meters) * (0.05 of meters)      // 0.005 m²

val typed = (2 of amperes) * loop
val native = ((2 of amperes).toUnit() * loop.toUnit()).toMagneticMoment()

typed == native                 // true
typed into ampereSquareMeters   // 0.01
```

## 与该组一起计算

| 表达式                       | 结果类型                      | 含义          |
|-----------------------------|-----------------------------------|------------------|
| `current * area`            | `KMagneticMomentUnitInstance`    | `m = I · A`      |
| `magneticMoment / area`     | `KElectricCurrentUnitInstance`   | 环路电流 |
| `magneticMoment / current`  | `KAreaUnitInstance`              | 环路面积    |

## 实际例子 — 线圈环路与原子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.magneticmoment.*

val loop = (0.1 of meters) * (0.05 of meters)
val m = (2 of amperes) * loop
m into ampereSquareMeters          // 0.01

// 这相当于多少个玻尔磁子?
m into bohrMagnetons                // ≈ 1.078e21

// 反过来: 1 cm² 的环路需要多大电流才能得到 1 A·m²?
val small = (0.01 of meters) * (0.01 of meters)
((1 of ampereSquareMeters) / small) into amperes   // 10 000 A
```

## 值语义

`equals`/`hashCode` 比较**归一化的 A·m² 值**,所以
`(1 of ampereSquareMeters) == (1 of joulesPerTesla)`。`toString()` 以基本单位显示数值:
`"0.01 A*m^2"`。

## 另请参阅

* [磁通密度](magneticfluxdensity.zh.md) — 该磁矩与之相互作用的场。
* [电流](ec.zh.md) 与 [距离](../kinematics/distance.zh.md) — 两个因子。
* [电气工程概述](overview.zh.md)
