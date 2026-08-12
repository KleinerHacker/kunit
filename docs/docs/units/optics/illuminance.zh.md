# 照度

包：`org.pcsoft.framework.kunit.optic.illuminance`
基本单位：**勒克斯**（`KIlluminanceUnit.BASE == KIlluminanceUnit.LUX`）

类型：**构造单位**

照度`E`是**到达表面**的光通量，除以该表面的单位面积：`E = Φ / A`，即
`1 lx = 1 lm/m²`。这是每一个工作场所照明标准所使用的量——与光通量不同，它不仅取决于灯本身，
还取决于灯的距离以及被照亮面积的大小。

其规范的基础量纲标准形式是`luminousIntensity¹ · solidAngle¹ · distance⁻²`。

## 单位

| 单位         | 枚举值                     | 符号 |         令牌 | 1单位对应勒克斯 |
|--------------|--------------------------------|--------|--------------:|--------------:|
| 勒克斯          | `KIlluminanceUnit.LUX`         | `lx`   |         `lux` |           1.0 |
| 辐透         | `KIlluminanceUnit.PHOT`        | `ph`   |       `phots` |        10 000 |
| 英尺烛光  | `KIlluminanceUnit.FOOT_CANDLE` | `fc`   | `footCandles` |    ≈ 10.76391 |
| 诺克斯          | `KIlluminanceUnit.NOX`         | `nx`   |         `nox` |         0.001 |

辐透是CGS单位（1 lm/cm²），英尺烛光是英制单位（1 lm/ft²），诺克斯用于非常低的光照水平，
例如月光。所有令牌都支持所有SI词头（如`kilo.lux`、`milli.lux`等）。

## 分解

该组有一种分解方式，其两种形式都会产生相同的、类型化且值相等的实例：

| 形式             | 表达式                                                             |
|------------------|------------------------------------------------------------------------|
| 类型化运算符   | `luminousFlux / area`                                                  |
| 原生形式（`toX()`） | `(cd.toUnit() * sr.toUnit() / (m.toUnit() pow 2)).toIlluminance()`     |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.illuminance.*

val native = (
    (1 of candelas).toUnit() * (1 of steradians).toUnit() / ((1 of meters).toUnit() pow 2)
).toIlluminance()
native into lux          // 1.0
```

## 使用该组进行计算

| 表达式                 | 结果类型                 | 含义                     |
|----------------------------|------------------------------|------------------------------|
| `luminousFlux / area`      | `KIlluminanceUnitInstance`  | `E = Φ / A`                 |
| `illuminance * area`       | `KLuminousFluxUnitInstance` | `Φ = E · A`                 |
| `luminousFlux / illuminance` | `KAreaUnitInstance`       | 光通量能照亮的面积   |
| `illuminance / solidAngle` | `KLuminanceUnitInstance`    | `L = E / Ω`                 |
| `illuminance * time`       | `KLuminousExposureUnitInstance` | `H = E · t`             |

## 实例——我的书桌足够亮吗？

办公照明大约需要**500 lx**。一个800 lm的灯泡挂在2 m²的书桌上方，能提供：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.illuminance.*

val desk = (2 of meters) * (1 of meters)     // 2 m²
val e = (800 of lumens) / desk               // KIlluminanceUnitInstance

e into lux                                    // 400.0 —— 低于500 lx的目标
e into footCandles                            // ≈ 37.2

val needed = (500 of lux) * desk              // KLuminousFluxUnitInstance
needed into lumens                            // 需要1000.0 lm
```

## 值语义

`equals`/`hashCode`比较**归一化后的勒克斯值**，因此`(1 of phots) == (10000 of lux)`。
`toString()`以基本单位渲染该值：`"500.0 lx"`。

## 另请参阅

* [光通量](luminous-flux.zh.md) —— 灯发出的光。
* [亮度](luminance.zh.md) —— 每立体角的照度，表面的"明亮程度"。
* [光照曝光量](luminous-exposure.zh.md) —— 随时间累积的照度。
* [光学概述](overview.zh.md)
</content>
