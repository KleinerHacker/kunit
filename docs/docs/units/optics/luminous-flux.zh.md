# 光通量

包：`org.pcsoft.framework.kunit.optic.luminousflux`
基本单位：**流明**（`KLuminousFluxUnit.BASE == KLuminousFluxUnit.LUMEN`）

类型：**构造单位**

光通量`Φ`是光源向其覆盖的所有方向发出的**可见光总量**——印在每一个灯具包装上的数值。
它是发光强度在立体角上的积分：`Φ = I · Ω`，即`1 lm = 1 cd·sr`。

其规范的基础量纲标准形式是`luminousIntensity¹ · solidAngle¹`。

## 单位

| 单位               | 枚举值                            | 符号  |               令牌 | 1单位对应流明 |
|--------------------|---------------------------------------|---------|--------------------:|-----------------:|
| 流明              | `KLuminousFluxUnit.LUMEN`             | `lm`    |            `lumens` |              1.0 |
| 坎德拉球面度  | `KLuminousFluxUnit.CANDELA_STERADIAN` | `cd·sr` | `candelaSteradians` |              1.0 |

`candelaSteradians`是流明定义的完整写法——数值上相同，但它能让公式清楚地表明该单位的来源。
两个令牌都支持所有SI词头（如`kilo.lumens`、`milli.lumens`等）。

## 分解

该组有一种分解方式，其两种形式都会产生相同的、类型化且值相等的实例：

| 形式                | 表达式                                                       |
|---------------------|--------------------------------------------------------------------|
| 类型化运算符      | `luminousIntensity * solidAngle`                                  |
| 原生形式（`toX()`）    | `((100 of candelas).toUnit() * (2 of steradians).toUnit()).toLuminousFlux()` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminousflux.*

val typed = (100 of candelas) * (2 of steradians)
val native = ((100 of candelas).toUnit() * (2 of steradians).toUnit()).toLuminousFlux()

typed == native          // true
typed into lumens        // 200.0
```

## 使用该组进行计算

| 表达式                       | 结果类型                      | 含义                       |
|----------------------------------|-----------------------------------|--------------------------------|
| `luminousIntensity * solidAngle` | `KLuminousFluxUnitInstance`      | `Φ = I · Ω`                   |
| `luminousFlux / solidAngle`      | `KLuminousIntensityUnitInstance` | `I = Φ / Ω`                   |
| `luminousFlux / luminousIntensity` | `KSolidAngleUnitInstance`      | 光通量所分布的立体角 |
| `luminousFlux / area`            | `KIlluminanceUnitInstance`       | `E = Φ / A`                   |
| `luminousFlux * time`            | `KLuminousEnergyUnitInstance`    | `Q = Φ · t`                   |
| `luminousFlux / power`           | `KLuminousEfficacyUnitInstance`  | `η = Φ / P`                   |

## 实例——各向同性灯泡

一个裸露的灯泡向所有方向均匀辐射。全球面对应`4π sr`，因此一个100 cd的光源发出的光通量为：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminousflux.*

val phi = (100 of candelas) * ((4 * Math.PI) of steradians)
phi into lumens          // ≈ 1256.6 lm —— 大约相当于一个100 W的白炽灯泡
```

## 值语义

`equals`/`hashCode`比较**归一化后的流明值**，因此`(1 of lumens) == (1000 of milli.lumens)`。
`toString()`以基本单位渲染该值：`"800.0 lm"`。

## 另请参阅

* [发光强度](luminous-intensity.zh.md) —— 每立体角的光通量。
* [照度](illuminance.zh.md) —— 每被照亮面积的光通量。
* [发光效能](luminous-efficacy.zh.md) —— 每瓦电功率的光通量。
* [光学概述](overview.zh.md)
</content>
