# 电通量

包: `org.pcsoft.framework.kunit.electric.flux`
基本单位: **伏特米**(`KElectricFluxUnit.BASE == KElectricFluxUnit.VOLT_METER`)

类型: **构成单位**

电通量 `Φ_E` 是电场强度对面积的积分: `Φ_E = E · A`。它是高斯定律所描述的量 — 通过闭合曲面的电通量等于所包含的
电荷除以介电常数。

其规范的基本量纲标准形式为 `mass · length³ · time⁻³ · current⁻¹`。

!!! note "不是电位移(电通密度)"
    [电通密度](electricfluxdensity.zh.md) `D`(`C/m²`)是量纲不同的另一个量。本页讨论的是电通量本身,单位为 `V·m`。

## 具名单位

| 单位            | 符号    |             标记 | 1单位折合 V·m |
|-----------------|---------|------------------:|--------------:|
| 伏特米          | `V*m`   |      `voltMeters` |           1.0 |
| 伏特厘米        | `V*cm`  | `voltCentimeters` |          0.01 |

所有标记都支持任意 SI 词头(`kilo.voltMeters` 等)。

## 分解

该组只有一种分解,两种形式都产生数值相等的同类型实例。原生形式由 **单位模板**组装而成,因为该组带有质量项:
原始混合值是以克为基准的乘积,而带类型的实例以具名单位存储其值。

| 形式             | 表达式                                                     |
|------------------|-----------------------------------------------------------------|
| 带类型运算符     | `electricFieldStrength * area`                                 |
| 原生 (`toX()`)   | `(125 of kilo.grams · m³ / s³ / A).toElectricFlux()`           |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.flux.*

val plate = (0.5 of meters) * (0.25 of meters)     // 0.125 m²

val typed = (1000 of voltsPerMeter) * plate
val native = (125 of kilo.grams.toUnit() * (meters pow 3) / (seconds pow 3) / amperes.toUnit())
    .toElectricFlux()

typed == native          // true
typed into voltMeters    // 125.0
```

## 与该组一起计算

| 表达式                              | 结果类型                            | 含义        |
|------------------------------------|----------------------------------------|----------------|
| `electricFieldStrength * area`     | `KElectricFluxUnitInstance`            | `Φ_E = E · A`  |
| `electricFlux / area`              | `KElectricFieldStrengthUnitInstance`   | `E = Φ_E / A`  |
| `electricFlux / electricFieldStrength` | `KAreaUnitInstance`                | 面积       |

## 实际例子 — 通过电容器极板的电通量

**1000 V/m** 的电场穿过一块 0.5 m × 0.25 m 的极板:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.flux.*

val plate = (0.5 of meters) * (0.25 of meters)
val phi = (1000 of voltsPerMeter) * plate
phi into voltMeters                 // 125.0

// 给定电通量在该极板上所隐含的电场
((125 of voltMeters) / plate) into voltsPerMeter   // 1000.0
```

## 值语义

`equals`/`hashCode` 比较**归一化的 V·m 值**,所以 `(1 of voltMeters) == (100 of voltCentimeters)`。
`toString()` 以基本单位显示数值: `"125.0 V*m"`。

## 另请参阅

* [电场强度](electricfieldstrength.zh.md) — 被积分的电场。
* [电通密度](electricfluxdensity.zh.md) — 量纲不同的 `D` 场。
* [电气工程概述](overview.zh.md)
