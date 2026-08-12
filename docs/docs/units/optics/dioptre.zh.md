# 屈光度（折射本领）

包：`org.pcsoft.framework.kunit.common.reciprocallength`
基本单位：**每米**（`KReciprocalLengthUnit.BASE == KReciprocalLengthUnit.RECIPROCAL_METER`）

类型：**构造单位**

镜片的屈光度`D`是其焦距的倒数：`D = 1 / f`。它的单位是**屈光度**，恰好等于每米的倒数——
在1 m处聚焦的镜片是1 dpt，在0.5 m处聚焦的镜片是2 dpt。

它的量纲是`distance⁻¹`——与光谱学中的[波数](../mechanics/wavenumber.md)**相同**。KUnit为这两种读法建模为
一个中立的组`reciprocallength`；屈光度是其中的一种读法。本页面介绍这种读法。

!!! note "一个组，两种读法"
    `KReciprocalLengthUnitInstance`是共享类型，因此就KUnit而言，屈光度和波数是同一个单位。该组使用中立的
    名称`reciprocallength`，以免任一读法独占其名称。请通过为你的值命名来区分它们。

## 命名单位

| 单位                  | 符号 |                  令牌 | 1单位对应m⁻¹ |
|-----------------------|--------|-----------------------:|--------------:|
| 每米      | `1/m`  |     `reciprocalMeters` |           1.0 |
| 屈光度               | `dpt`  |             `dioptres` |           1.0 |
| 每厘米 | `1/cm` | `reciprocalCentimeters` |         100.0 |
| 凯泽                | `1/cm` |               `kaysers` |         100.0 |

`dioptres`和`kaysers`分别是每米和每厘米的替代写法，不是独立的单位。所有令牌都支持所有SI词头
（如`milli.dioptres`等）。

## 使用该组进行计算

| 表达式                       | 结果类型                      | 含义                          |
|----------------------------------|-----------------------------------|-----------------------------------|
| `1 / length`                     | `KReciprocalLengthUnitInstance`  | `D = 1 / f`                      |
| `1 / reciprocalLength`           | `KLengthUnitInstance`            | 反算回焦距         |
| `reciprocalLength + …`           | `KReciprocalLengthUnitInstance`  | 贴合的薄透镜屈光度相加 |
| `reciprocalLength * length`      | `Double`                         | 无量纲计数（`m⁻¹ · m`）  |

原生形式通过`toReciprocalLength()`转换：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val native = (2.5 of ((1 of meters).toUnit() pow -1)).toReciprocalLength()
native into dioptres      // 2.5
```

## 实例——老花镜

焦距为**40 cm**的镜片的屈光度为`D = 1 / 0.4 m = 2.5 dpt`。将第二片较弱的镜片贴合放置，其屈光度直接相加——
这正是同类型`+`所做的事情：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val d = 1 / (40 of centi.meters)     // KReciprocalLengthUnitInstance
d into dioptres                       // 2.5

val combined = d + (1.5 of dioptres)  // 贴合的镜片
combined into dioptres                // 4.0

val f = 1 / combined                  // KLengthUnitInstance
f into centi.meters                   // 25.0 —— 合成焦距
```

## 值语义

`equals`/`hashCode`比较**归一化后的m⁻¹值**，因此
`(1 of reciprocalCentimeters) == (100 of reciprocalMeters)`。`toString()`以基本单位渲染该值：
`"2.5 1/m"`。

## 另请参阅

* [波数](../mechanics/wavenumber.md) —— 相同的类型，作为光谱学中的量来解读。
* [距离](../kinematics/distance.md) —— 该组是其倒数所对应的组。
* [光学概述](overview.zh.md)
</content>
