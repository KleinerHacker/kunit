# 压缩率

包: `org.pcsoft.framework.kunit.mechanic.compressibility`
基本单位: **帕斯卡的倒数**
(`KCompressibilityUnit.BASE == KCompressibilityUnit.RECIPROCAL_PASCAL`)

类型: **构造单位**

压缩率 `κ = −(1/V)·(∂V/∂p)` 表示材料的体积在单位压力下收缩了多少。
它正好是 **体积模量** `K` 的倒数，而体积模量是一种弹性模量，因此也是一种
[压力](pressure.zh.md)。水的压缩率约为 4.5 × 10⁻¹⁰ Pa⁻¹——这也是为什么在流体力学中水通常被
视为不可压缩的原因。

其规范的基本量纲标准形式为 `mass⁻¹ · length · time²`。

## 命名单位

| 单位                            | 符号    |                   词元 | 1 单位对应的 1/Pa |
|---------------------------------|---------|------------------------:|---------------:|
| 帕斯卡的倒数                     | `1/Pa`  |     `reciprocalPascals` |            1.0 |
| 巴的倒数                         | `1/bar` |        `reciprocalBars` |           1e-5 |
| 标准大气压的倒数                 | `1/atm` | `reciprocalAtmospheres` |      1/101 325 |

所有词元均可接受任何 SI 词头 (`pico.reciprocalPascals` 等)。与相邻的压力单位组一样，实例存储的是
**以克为基础的原始分量值**。

## 使用该单位组进行计算

| 表达式                        | 结果类型                         | 含义                            |
|--------------------------------|-------------------------------------|-----------------------------------|
| `1 / pressure`                | `KCompressibilityUnitInstance`     | `κ = 1 / K`                       |
| `1 / compressibility`         | `KPressureUnitInstance`            | `K = 1 / κ`                       |
| `compressibility * pressure`  | `Double`                           | 相对体积变化 `ΔV/V`               |

这两个倒数是精确的：分量的基础单位 (压力的 `g·m⁻¹·s⁻²` 与此处的 `g⁻¹·m·s²`) 互为倒数，
因此不需要任何桥接系数。

## 实际案例——水会压缩多少

水的体积模量约为 **2.2 GPa**。它的压缩率是多少？在 10 MPa (大约相当于 1000 米水深) 的压力下，
它会收缩多少？

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.compressibility.*

val kappa = 1 / (2.2 of giga.pascals)          // KCompressibilityUnitInstance
kappa into reciprocalPascals                    // ≈ 4.545e-10

val shrink = kappa * (10 of mega.pascals)       // Double
shrink                                           // ≈ 0.00455 —— 体积损失 0.45 %

// 再转换回体积模量
(1 / kappa) into giga.pascals                    // ≈ 2.2
```

## 值语义

`equals`/`hashCode` 比较的是 **归一化后的分量值**，因此
`(1 of reciprocalBars) == (1e-5 of reciprocalPascals)`。`toString()` 以基本单位表示该值:
`"1.0 1/Pa"`。

## 另请参阅

* [压力](pressure.zh.md)——其倒数量 (体积模量)。
* [应力与弹性模量](stress.zh.md)——作为材料属性来读取的同一种类型。
* [力学概述](overview.zh.md)
