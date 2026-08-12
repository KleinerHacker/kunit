# 波数

包: `org.pcsoft.framework.kunit.common.reciprocallength`
基本单位: **米的倒数** (`KReciprocalLengthUnit.BASE == KReciprocalLengthUnit.RECIPROCAL_METER`)

类型: **构造单位**

波的波数 `ṽ` 是其波长的倒数: `ṽ = 1 / λ`——即单位长度内的波周期数。光谱学中使用波数而非波长，
因为它与光子能量成正比，并且几乎总是以 **每厘米的倒数** (`cm⁻¹`，历史上称为 *kayser*) 来表示：
可见光大约在 14,000–25,000 cm⁻¹ 之间，红外指纹区则在 400–1500 cm⁻¹ 之间。

其量纲为 `distance⁻¹`——**与** 透镜的屈光度 [屈光度](../optics/dioptre.zh.md) **相同**。KUnit
为这两种读法建模了同一个中性单位组 `reciprocallength`，波数便是其中之一。本页面记录的正是这种读法。

!!! note "一个单位组，两种读法"
    `KReciprocalLengthUnitInstance` 是共享的类型，因此就 KUnit 而言，波数与屈光度是同一个单位。
    该单位组使用中性名称 `reciprocallength`，以避免任何一种读法独占其名。请通过为变量命名来区分它们。

## 命名单位

| 单位                  | 符号 |                   词元 | 1 单位对应的 m⁻¹ |
|-----------------------|--------|------------------------:|--------------:|
| 米的倒数               | `1/m`  |      `reciprocalMeters` |           1.0 |
| 厘米的倒数             | `1/cm` | `reciprocalCentimeters` |         100.0 |
| 凯泽 (Kayser)          | `1/cm` |                `kaysers` |         100.0 |
| 屈光度                 | `dpt`  |               `dioptres` |           1.0 |

所有词元均可接受任何 SI 词头 (`kilo.reciprocalCentimeters` 等)。

## 使用该单位组进行计算

| 表达式                      | 结果类型                          | 含义                             |
|-----------------------------|--------------------------------------|-------------------------------------|
| `1 / length`                | `KReciprocalLengthUnitInstance`     | `ṽ = 1 / λ`                         |
| `1 / reciprocalLength`      | `KLengthUnitInstance`               | 回到波长                            |
| `reciprocalLength * length` | `Double`                            | 无量纲的周期数                      |
| `reciprocalLength + …`      | `KReciprocalLengthUnitInstance`     | 相同类型之间的加法                  |

原生形式通过 `toReciprocalLength()` 进行转换:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val native = (100 of ((1 of meters).toUnit() pow -1)).toReciprocalLength()
native into reciprocalCentimeters      // 1.0
```

## 实际案例——绿色激光光线

一条 500 nm 的激光光线换算成波数为 20,000 cm⁻¹，由此可直接得出 1 mm 光程中容纳的周期数:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val k = 1 / (500 of nano.meters)       // KReciprocalLengthUnitInstance
k into reciprocalCentimeters            // 20_000.0
k into kaysers                          // 20_000.0（同一单位，经典名称）

val cycles = k * (1 of milli.meters)    // Double
cycles                                   // 2000.0 —— 每毫米的波周期数

val lambda = 1 / k                       // KLengthUnitInstance
lambda into nano.meters                  // 500.0
```

## 值语义

`equals`/`hashCode` 比较的是 **归一化后的 m⁻¹ 值**，因此
`(1 of reciprocalCentimeters) == (100 of reciprocalMeters)`。`toString()` 以基本单位表示该值:
`"2000000.0 1/m"`。

## 另请参阅

* [屈光度](../optics/dioptre.zh.md)——作为屈光度读取的同一种类型。
* [频率](../kinematics/frequency.zh.md)——时间的倒数，是该单位组在时间维度上的类似量。
* [力学概述](overview.zh.md)
