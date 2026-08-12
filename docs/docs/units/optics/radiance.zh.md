# 辐亮度

包：`org.pcsoft.framework.kunit.optic.radiance`
基本单位：**瓦特每球面度平方米**
（`KRadianceUnit.BASE == KRadianceUnit.WATT_PER_STERADIAN_SQUARE_METER`）

类型：**构造单位**

辐亮度`Lₑ`是**每单位发光面积**的辐射强度：`Lₑ = Iₑ / A`。它是[亮度](luminance.zh.md)对应的
**辐射度学**量，也是遥感和热成像所使用的量——相机像素实际积分的正是这个量，与表面的距离无关。

其规范的基础量纲标准形式是`mass¹ · time⁻³ · solidAngle⁻¹`。两个长度指数相互抵消：
瓦特贡献了`distance²`，而面积贡献了`distance⁻²`。

## 单位

| 单位                            | 枚举值                                    | 符号       |                            令牌 | 1单位对应W/(sr·m²) |
|---------------------------------|-----------------------------------------------|--------------|---------------------------------:|--------------------:|
| 瓦特每球面度平方米 | `KRadianceUnit.WATT_PER_STERADIAN_SQUARE_METER` | `W/(sr*m^2)` | `wattsPerSteradianSquareMeter`   |                 1.0 |

该令牌支持所有SI词头（如`milli.wattsPerSteradianSquareMeter`等）。

## 分解

该组有一种分解方式，其两种形式都会产生相同的、类型化且值相等的实例。由于该组带有质量项，
原生形式是由**单位模板**组装而成的。

| 形式             | 表达式                                                    |
|------------------|-----------------------------------------------------------------|
| 类型化运算符   | `radiantIntensity / area`                                     |
| 原生形式（`toX()`） | `(5 of kilo.grams / s³ / sr).toRadiance()`                    |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.radiantintensity.wattsPerSteradian
import org.pcsoft.framework.kunit.optic.radiance.*

val typed = (10 of wattsPerSteradian) / ((2 of meters) * (1 of meters))
val native = (5 of kilo.grams.toUnit() / (seconds pow 3) / steradians.toUnit()).toRadiance()

typed == native                              // true
typed into wattsPerSteradianSquareMeter      // 5.0
```

## 使用该组进行计算

| 表达式                        | 结果类型                     | 含义         |
|-----------------------------------|---------------------------------|-----------------|
| `radiantIntensity / area`         | `KRadianceUnitInstance`         | `Lₑ = Iₑ / A`   |
| `radiance * area`                 | `KRadiantIntensityUnitInstance` | `Iₑ = Lₑ · A`   |
| `radiantIntensity / radiance`     | `KAreaUnitInstance`             | 发光面积 |

## 实例——热成像相机像素

一面**2 m²**的炉壁朝向相机辐射**10 W/sr**。其辐亮度——即相机所报告的、与距离无关的数值——为：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.radiantintensity.wattsPerSteradian
import org.pcsoft.framework.kunit.optic.radiance.*

val wall = (2 of meters) * (1 of meters)
val l = (10 of wattsPerSteradian) / wall
l into wattsPerSteradianSquareMeter      // 5.0

// 同一面墙上0.5 m²的一小块区域发出的强度按比例更小……
val patch = (0.5 of meters) * (1 of meters)
(l * patch) into wattsPerSteradian       // 2.5 —— 但辐亮度不变
```

## 值语义

`equals`/`hashCode`比较**归一化后的W/(sr·m²)值**，因此
`(1 of wattsPerSteradianSquareMeter) == (1000 of milli.wattsPerSteradianSquareMeter)`。
`toString()`以基本单位渲染该值：`"5.0 W/(sr*m^2)"`。

## 另请参阅

* [辐射强度](radiant-intensity.zh.md) —— 分子。
* [亮度](luminance.zh.md) —— 对应的光度学量。
* [热流密度](../thermodynamics/heat-flux-density.md) —— 在半球上积分的辐亮度。
* [光学概述](overview.zh.md)
</content>
