# 光学 — 概述

包：`org.pcsoft.framework.kunit.optic.luminousintensity`、`…luminousflux`、`…illuminance`、
`…luminance`、`…luminousenergy`、`…luminousexposure`、`…efficacy`、`…radiantintensity`、`…radiance`，
以及 `org.pcsoft.framework.kunit.common.reciprocallength`

光学描述的是**光**——光源发出多少光、有多少光到达一个表面，以及电功率转换为光的效率如何。该领域建立在
**坎德拉**之上，这是第七个也是最后一个SI基本单位，也是唯一一个基于人类感知定义的基本单位：它按照人眼的
灵敏度对辐射功率进行加权。

正因如此，该领域存在两个平行的体系。**光度学**量（坎德拉、流明、勒克斯、尼特）描述*人眼所见*的光；
**辐射度学**量（瓦特每球面度、瓦特每球面度平方米）描述同样的辐射，但不经过人眼加权，*如探测器所测量*的那样。
连接两者的桥梁是[发光效能](luminous-efficacy.zh.md)，其上限为683 lm/W。

## 本主题涉及的单位

| 单位     | 类型        | 基本单位                              | 页面                                     |
|--------------------|-------------|----------------------------------------|------------------------------------------|
| 发光强度 | 原生        | 坎德拉 (`cd`)                         | [发光强度](luminous-intensity.zh.md) |
| 光通量      | 构造 | 流明 (`lm`)                           | [光通量](luminous-flux.zh.md)        |
| 照度        | 构造 | 勒克斯 (`lx`)                             | [照度](illuminance.zh.md)            |
| 亮度          | 构造 | 坎德拉每平方米 (`cd/m²`)     | [亮度](luminance.zh.md)                |
| 发光能量    | 构造 | 流明秒 (`lm·s`)                  | [发光能量](luminous-energy.zh.md)    |
| 光照曝光量  | 构造 | 勒克斯秒 (`lx·s`)                    | [光照曝光量](luminous-exposure.zh.md) |
| 发光效能  | 构造 | 流明每瓦 (`lm/W`)                | [发光效能](luminous-efficacy.zh.md) |
| 辐射强度  | 构造 | 瓦特每球面度 (`W/sr`)            | [辐射强度](radiant-intensity.zh.md) |
| 辐亮度           | 构造 | 瓦特每球面度平方米 (`W/(sr·m²)`)    | [辐亮度](radiance.zh.md)                  |
| 屈光度   | 构造 | 屈光度 (`dpt` = `m⁻¹`)                | [屈光度](dioptre.zh.md)                    |

将强度类量与通量类量联系起来的立体角 **不** 属于本领域——它属于[力学](../mechanics/solid-angle.md)主题，
在此原样复用。

## 各量之间的关系

以下每个关系都会返回正确的**类型化**量；你永远不需要手动组装原始的混合单位：

| 表达式                     | 结果             | 公式        |
|--------------------------------|--------------------|----------------|
| `luminousIntensity * solidAngle` | 光通量    | `Φ = I · Ω`    |
| `luminousFlux / area`          | 照度        | `E = Φ / A`    |
| `luminousIntensity / area`     | 亮度          | `L = I / A`    |
| `illuminance / solidAngle`     | 亮度          | `L = E / Ω`    |
| `luminousFlux * time`          | 发光能量    | `Q = Φ · t`    |
| `illuminance * time`           | 光照曝光量  | `H = E · t`    |
| `luminousFlux / power`         | 发光效能  | `η = Φ / P`    |
| `power / solidAngle`           | 辐射强度  | `Iₑ = P / Ω`   |
| `radiantIntensity / area`      | 辐亮度           | `Lₑ = Iₑ / A`  |
| `1 / length`                   | 屈光度   | `D = 1 / f`    |

## 实例——这盏灯泡对我的书桌来说够亮吗？

一个LED灯泡额定为**800 lm**，功率**7 W**。它悬挂在**2 m²**的书桌上方。办公照明大约需要500 lx。它够用吗？
这个灯泡的效率又如何？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.luminousflux.*
import org.pcsoft.framework.kunit.optic.illuminance.*
import org.pcsoft.framework.kunit.optic.efficacy.*

val flux = 800 of lumens
val desk = (2 of meters) * (1 of meters)          // KAreaUnitInstance, 2 m²

val e = flux / desk                                // KIlluminanceUnitInstance
e into lux                                         // 400.0 —— 略低于500 lx的目标

val eta = flux / (7 of watts)                      // KLuminousEfficacyUnitInstance
eta into lumensPerWatt                             // ≈ 114.3
eta.value / MAX_LUMINOUS_EFFICACY                  // ≈ 0.167 —— 物理上限的17%
```

## 实例——老花镜

焦距为**40 cm**的镜片，其屈光度为`D = 1 / f`。两片贴合在一起的薄透镜的屈光度直接相加，
这正是同类型量的`+`操作所做的事情：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val d = 1 / (40 of centi.meters)   // KReciprocalLengthUnitInstance
d into dioptres                     // 2.5

val combined = d + (1.5 of dioptres) // 贴合的镜片屈光度相加
combined into dioptres               // 4.0
1 / combined into meters             // 0.25 —— 合成焦距
```

## 记号

下表以数学表示法和使用KUnit的Kotlin表示法展示了该领域的核心关系。指数使用Unicode上标
（`²`、`⁻¹`），`·`表示乘法，`/`表示分数。

| 数学表示   | Kotlin                                    | 含义                             |
|---------------|---------------------------------------------|--------------------------------------|
| `Φ = I · Ω`   | `(100 of candelas) * (2 of steradians)`   | 由强度×立体角得到光通量 |
| `E = Φ / A`   | `(800 of lumens) / desk`                  | 由光通量÷面积得到照度        |
| `L = I / A`   | `(250 of candelas) / screen`              | 由强度÷面积得到亮度     |
| `Q = Φ · t`   | `(800 of lumens) * (2 of hours)`          | 由光通量×时间得到发光能量    |
| `H = E · t`   | `(50 of lux) * (8 of hours)`              | 由照度×时间得到光照剂量  |
| `η = Φ / P`   | `(800 of lumens) / (7 of watts)`          | 发光效能                   |
| `Iₑ = P / Ω`  | `(20 of watts) / (4 of steradians)`       | 辐射强度                   |
| `D = 1 / f`   | `1 / (40 of centi.meters)`                | 由焦距得到屈光度  |

## 接下来阅读

* [发光强度](luminous-intensity.zh.md) —— 坎德拉，该领域的原生基本量。
* [光通量](luminous-flux.zh.md)与[照度](illuminance.zh.md) —— 灯发出的光与表面接收到的光。
* [亮度](luminance.zh.md) —— 显示器"尼特"额定值所指的量。
* [发光效能](luminous-efficacy.zh.md) —— 光度学与辐射度学体系之间的桥梁。
* [屈光度](dioptre.zh.md) —— 屈光度，以及它在光谱学中的孪生量[波数](../mechanics/wavenumber.md)。
</content>
