# 截面二次矩

包: `org.pcsoft.framework.kunit.kinematic.distance`
基本单位: **四次方米** (`m⁴`，distance 单位组中指数为 4 的分支)

类型: **构造单位**

截面二次矩 `I` (面积惯性矩) 是决定梁截面在弯曲时刚度大小的几何属性——它就是弯曲刚度 `EI` 中的
`I`。钢材型材表中通常以 `cm⁴` 为单位，较小的截面则以 `mm⁴` 为单位。

与本站的其他单位组不同，它并非独立的单位组：它是 distance 单位组中 **指数为 4 的分支**，即
`KSecondMomentOfAreaUnitInstance`，与 [长度](../kinematics/distance.zh.md) (指数 1)、面积 (指数 2)
和体积 (指数 3) 并列。

!!! warning "不是转动惯量"
    请勿将其与 *质量* [转动惯量](moment-of-inertia.zh.md) (`kg·m²`) 混淆，后者描述的是对角加速度的
    抵抗能力。两者名称相似，但量纲不同。

## 命名词元

| 单位                  | 符号 |                词元 | 1 单位对应的 m⁴ |
|-----------------------|--------|---------------------:|-------------:|
| 四次方米               | `m⁴`   |       `quarticMeters` |          1.0 |
| 四次方厘米             | `cm⁴`  |  `quarticCentimeters` |         1e-8 |
| 四次方毫米             | `mm⁴`  |  `quarticMillimeters` |        1e-12 |
| 四次方英寸             | `in⁴`  |       `quarticInches` | ≈ 4.16231e-7 |

所有词元均可接受任何 SI 词头。

## 使用该分支进行计算

现在，任何结果落在指数 4 上的乘积都会返回类型化的分支，而不是通用的
`KDistanceUnitInstance`:

| 表达式                        | 结果类型                              | 含义                       |
|---------------------------------|------------------------------------------|-------------------------------|
| `area * area`                  | `KSecondMomentOfAreaUnitInstance`      | m² · m² = m⁴                  |
| `volume * length`              | `KSecondMomentOfAreaUnitInstance`      | m³ · m = m⁴                   |
| `length * volume`              | `KSecondMomentOfAreaUnitInstance`      | m · m³ = m⁴                   |
| `secondMomentOfArea / length`  | `KVolumeUnitInstance`                  | 截面模量                       |
| `secondMomentOfArea / area`    | `KAreaUnitInstance`                    | m⁴/m² = m²                    |
| `secondMomentOfArea / volume`  | `KLengthUnitInstance`                  | m⁴/m³ = m                     |
| `secondMomentOfArea + …`       | `KSecondMomentOfAreaUnitInstance`      | 组合截面各部分之和              |

加法运算被限制在相同量纲内——`secondMomentOfArea + area` 会导致 **编译错误**，与
`length + area` 完全相同。

原生形式通过 `toSecondMomentOfArea()` 进行转换:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*

val native = ((1 of centi.meters).toUnit() pow 4).toSecondMomentOfArea()
native into quarticCentimeters      // 1.0
```

## 实际案例——矩形梁

对于宽 `b`、高 `h` 的矩形，`I = b·h³/12`。对于 100 mm × 200 mm 的截面:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*

val b = 100 of milli.meters
val h = 200 of milli.meters

val i = (b * (h * h * h)) / 12       // KSecondMomentOfAreaUnitInstance
i into quarticCentimeters             // ≈ 6666.7 cm⁴

// 截面模量 W = I / (h/2)
val w = i / (h / 2)                   // KVolumeUnitInstance
w.value                                // ≈ 6.667e-4 m³

// 组合截面：两根这样的梁并排放置
val doubled = i + i
doubled into quarticCentimeters        // ≈ 13333.3
```

## 值语义

`equals`/`hashCode` 和比较操作作用于归一化后的 `m⁴` 值，并限制在相同量纲内。
`exponent` 返回 `4`。

## 另请参阅

* [距离](../kinematics/distance.zh.md)——该分支所属的单位组。
* [转动惯量](moment-of-inertia.zh.md)——名称相似的 *质量* 基础量。
* [力学概述](overview.zh.md)
