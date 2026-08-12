# 辐射强度

包：`org.pcsoft.framework.kunit.optic.radiantintensity`
基本单位：**瓦特每球面度**（`KRadiantIntensityUnit.BASE == KRadiantIntensityUnit.WATT_PER_STERADIAN`）

类型：**构造单位**

辐射强度`Iₑ`是光源**每单位立体角**发出的辐射通量（功率）：`Iₑ = P / Ω`。它是
[发光强度](luminous-intensity.zh.md)对应的**辐射度学**量——几何关系相同，但以瓦特而非流明来测量，
因此它计入了包括人眼无法看见的红外线和紫外线在内的所有辐射。

其规范的基础量纲标准形式是`mass¹ · distance² · time⁻³ · solidAngle⁻¹`。

## 单位

| 单位               | 枚举值                                   | 符号 |               令牌 | 1单位对应W/sr |
|--------------------|------------------------------------------------|--------|--------------------:|---------------:|
| 瓦特每球面度 | `KRadiantIntensityUnit.WATT_PER_STERADIAN`   | `W/sr` | `wattsPerSteradian` |            1.0 |

该令牌支持所有SI词头（如`milli.wattsPerSteradian`、`kilo.wattsPerSteradian`等）。

## 分解

该组有一种分解方式，其两种形式都会产生相同的、类型化且值相等的实例。由于该组带有质量项，
原生形式是由**单位模板**组装而成的（同样的说明也见于[发光效能](luminous-efficacy.zh.md)）。

| 形式             | 表达式                                                        |
|------------------|---------------------------------------------------------------------|
| 类型化运算符   | `power / solidAngle`                                              |
| 原生形式（`toX()`） | `(5 of kilo.grams · m² / s³ / sr).toRadiantIntensity()`           |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.radiantintensity.*

val typed = (20 of watts) / (4 of steradians)
val native = (
    5 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / steradians.toUnit()
).toRadiantIntensity()

typed == native                 // true
typed into wattsPerSteradian    // 5.0
```

## 使用该组进行计算

| 表达式                        | 结果类型                       | 含义                    |
|-----------------------------------|------------------------------------|-----------------------------|
| `power / solidAngle`              | `KRadiantIntensityUnitInstance`   | `Iₑ = P / Ω`               |
| `radiantIntensity * solidAngle`   | `KPowerUnitInstance`              | `P = Iₑ · Ω`               |
| `power / radiantIntensity`        | `KSolidAngleUnitInstance`         | 所分布的立体角 |
| `radiantIntensity / area`         | `KRadianceUnitInstance`           | `Lₑ = Iₑ / A`              |

## 实例——红外LED

一个红外发射器向0.2 sr的锥角辐射**20 mW**的功率。其辐射强度，以及0.05 sr探测器孔径所接收到的功率为：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.radiantintensity.*

val i = (20 of milli.watts) / (0.2 of steradians)
i into milli.wattsPerSteradian       // 100.0

val caught = i * (0.05 of steradians)  // KPowerUnitInstance
caught into milli.watts                // 有5.0 mW到达探测器
```

## 值语义

`equals`/`hashCode`比较**归一化后的W/sr值**，因此
`(1 of wattsPerSteradian) == (1000 of milli.wattsPerSteradian)`。`toString()`以基本单位渲染该值：
`"5.0 W/sr"`。

## 另请参阅

* [发光强度](luminous-intensity.zh.md) —— 对应的光度学量。
* [辐亮度](radiance.zh.md) —— 每发光面积的辐射强度。
* [发光效能](luminous-efficacy.zh.md) —— 瓦特与流明之间的桥梁。
* [光学概述](overview.zh.md)
</content>
