# 发光效能

包：`org.pcsoft.framework.kunit.optic.efficacy`
基本单位：**流明每瓦**（`KLuminousEfficacyUnit.BASE == KLuminousEfficacyUnit.LUMEN_PER_WATT`）

类型：**构造单位**

发光效能`η`是灯**每瓦电功率**产生的光通量：`η = Φ / P`。它是衡量光源好坏的单一数值，
也是光度学体系与辐射度学体系之间的桥梁：它将探测器测得的瓦特转换为人眼感知的流明。

其规范的基础量纲标准形式是`luminousIntensity¹ · solidAngle¹ · mass⁻¹ · distance⁻² · time³`。

## 单位

| 单位           | 枚举值                              | 符号 |           令牌 | 1单位对应lm/W |
|----------------|-----------------------------------------|--------|----------------:|---------------:|
| 流明每瓦 | `KLuminousEfficacyUnit.LUMEN_PER_WATT`  | `lm/W` | `lumensPerWatt` |            1.0 |

该令牌支持所有SI词头（如`milli.lumensPerWatt`、`kilo.lumensPerWatt`等）。

## 常量

| 常量                | 值       | 含义                                                       |
|-------------------------|-------------|-----------------------------------------------------------------|
| `MAX_LUMINOUS_EFFICACY` | `683 lm/W`  | 源自SI坎德拉定义的、555 nm处的物理上限 |

没有任何光源能超过683 lm/W，因为这是单色绿光在明视觉光效函数峰值处的效能。
每一盏实际的灯都只是这个数值的一部分。

## 分解

该组有一种分解方式，其两种形式都会产生相同的、类型化且值相等的实例。请注意，
原生形式是由**单位模板**组装而成的：对于带有质量项的组，原始混合值是以克为基础的乘积，
而类型化实例则以命名单位存储其值。

| 形式             | 表达式                                                                       |
|------------------|-------------------------------------------------------------------------------------|
| 类型化运算符   | `luminousFlux / power`                                                            |
| 原生形式（`toX()`） | `(120 of (cd·sr) / (kilo.grams · m² / s³)).toLuminousEfficacy()`                  |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.efficacy.*

val typed = (1200 of lumens) / (10 of watts)
val native = (
    120 of (candelas.toUnit() * steradians.toUnit()) /
        (kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3))
).toLuminousEfficacy()

typed == native              // true
typed into lumensPerWatt     // 120.0
```

## 使用该组进行计算

| 表达式                          | 结果类型                     | 含义                |
|--------------------------------------|----------------------------------|-------------------------|
| `luminousFlux / power`              | `KLuminousEfficacyUnitInstance` | `η = Φ / P`            |
| `luminousEfficacy * power`          | `KLuminousFluxUnitInstance`     | `Φ = η · P`            |
| `luminousFlux / luminousEfficacy`   | `KPowerUnitInstance`            | 所需的功率     |

## 实例——比较三种灯泡

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.efficacy.*

val incandescent = (800 of lumens) / (60 of watts)
val halogen      = (800 of lumens) / (42 of watts)
val led          = (800 of lumens) / (7 of watts)

incandescent into lumensPerWatt      // ≈ 13.3
halogen into lumensPerWatt           // ≈ 19.0
led into lumensPerWatt               // ≈ 114.3

led.value / MAX_LUMINOUS_EFFICACY    // ≈ 0.167 —— 物理上限的17%

// 一条LED灯带要输出3000 lm需要多少功率？
val p = (3000 of lumens) / led       // KPowerUnitInstance
p into watts                          // 26.25
```

## 值语义

`equals`/`hashCode`比较**归一化后的lm/W值**，因此
`(1 of lumensPerWatt) == (1000 of milli.lumensPerWatt)`。`toString()`以基本单位渲染该值：
`"120.0 lm/W"`。

## 另请参阅

* [光通量](luminous-flux.zh.md) —— 分子。
* [辐射强度](radiant-intensity.zh.md)与[辐亮度](radiance.zh.md) —— 桥梁的辐射度学一侧。
* [功率（电学）](../electrical/power.md) —— 分母。
* [光学概述](overview.zh.md)
</content>
