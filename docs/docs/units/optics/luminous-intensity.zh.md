# 发光强度

包：`org.pcsoft.framework.kunit.optic.luminousintensity`
基本单位：**坎德拉**（`KLuminousIntensityUnit.BASE == KLuminousIntensityUnit.CANDELA`）

类型：**原生单位**

发光强度`I`是光源在给定方向**每单位立体角**发出的光通量。它的单位坎德拉是**第七个SI基本单位**——
也是唯一一个通过人类感知来定义的基本单位：1 cd是光源在该方向上发出频率为540 THz、辐射强度为1/683 W/sr
的单色辐射时的强度。

该组是一个**简单的一维**原生组（没有按指数特化的子类型）：
`KLuminousIntensityUnitInstance`包装单一的`KLuminousIntensityUnit.CANDELA`项，始终以坎德拉归一化存储。

## 单位

| 单位            | 枚举值                                | 符号   |          令牌 | 1单位对应坎德拉 |
|-----------------|-------------------------------------------|----------|---------------:|-------------------:|
| 坎德拉         | `KLuminousIntensityUnit.CANDELA`          | `cd`     |     `candelas` |                1.0 |
| 赫夫纳烛光   | `KLuminousIntensityUnit.HEFNER_CANDLE`    | `HK`     | `hefnerCandles` |              0.903 |
| 烛光功率     | `KLuminousIntensityUnit.CANDLEPOWER`      | `cp`     |  `candlepower` |              0.981 |
| 卡塞尔          | `KLuminousIntensityUnit.CARCEL`           | `carcel` |      `carcels` |               9.74 |

这三个非SI条目是坎德拉出现之前使用的历史国家标准——德国的赫夫纳灯、英国的国际烛光标准，
以及法国的卡塞尔油灯。它们被保留下来，以便可以直接读懂旧的数据表。

每个令牌都是一个值为1的`KLuminousIntensityUnitInstance`，与`of`（构建）和`into`（读取）配合使用。
所有令牌都支持所有SI词头（如`milli.candelas`、`kilo.candelas`等）。

## 使用该组进行计算

| 表达式                       | 结果类型                     | 含义                          |
|----------------------------------|----------------------------------|-----------------------------------|
| `luminousIntensity + …`          | `KLuminousIntensityUnitInstance` | 同类型相加               |
| `luminousIntensity * solidAngle` | `KLuminousFluxUnitInstance`     | `Φ = I · Ω`，发出的光通量    |
| `luminousIntensity / area`       | `KLuminanceUnitInstance`        | `L = I / A`，表面的光亮程度  |
| `luminousFlux / solidAngle`      | `KLuminousIntensityUnitInstance` | 从光通量反算回来                   |

原生形式通过`toLuminousIntensity()`转换：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.optic.luminousintensity.*

val raw = (1200 of candelas).toUnit()   // KMixedUnitInstance
raw.toLuminousIntensity() into candelas // 1200.0
```

## 实例——汽车前照灯

一个近光灯在其光轴上的规格为**1200 cd**。分布在0.05 sr的锥角内，实际指向道路的光通量为：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.*
import org.pcsoft.framework.kunit.optic.luminousflux.*

val i = 1200 of candelas
i into kilo.candelas                     // 1.2

val beam = i * (0.05 of steradians)      // KLuminousFluxUnitInstance
beam into lumens                         // 光束锥内为60.0 lm
```

## 值语义

`equals`/`hashCode`比较**归一化后的坎德拉值**，因此`(1 of candelas) == (1000 of milli.candelas)`。
`toString()`以基本单位渲染该值：`"1200.0 cd"`。

## 另请参阅

* [光通量](luminous-flux.zh.md) —— 强度在立体角上的积分。
* [亮度](luminance.zh.md) —— 每发光面积的强度。
* [辐射强度](radiant-intensity.zh.md) —— 对应的辐射度学量，不经过人眼加权。
* [光学概述](overview.zh.md)
</content>
