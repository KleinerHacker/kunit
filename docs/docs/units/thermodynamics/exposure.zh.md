# 照射量（电离剂量）

包: `org.pcsoft.framework.kunit.electric.specificcharge`
基本单位: **库仑每千克**
(`KSpecificChargeUnit.BASE == KSpecificChargeUnit.COULOMB_PER_KILOGRAM`)

类型: **构造单位（constructed unit）**

照射量 `X` —— 经典的**电离剂量** —— 通过测量电离辐射每单位空气质量所释放的电荷来度量:
`X = Q / m`，单位为 `C/kg`。其历史单位是**伦琴**（1 R = 2.58 × 10⁻⁴ C/kg）。

它的量纲是 `current · time · mass⁻¹` —— 与粒子的[比电荷](../electrical/specificcharge.zh.md)
**相同**。KUnit 为这两种读法建模了同一个分组；照射量是其中之一。本页记录的正是这种读法。

## 为什么照射量没有自己的类型

KUnit 有意使用 `KSpecificChargeUnitInstance` 而不是单独的 `KExposureUnitInstance` 来建模
照射量。原因在于本库的形式识别约定:

* 每个标准化分组都有**唯一**的规范基本量纲正规形式，且
* `toX()` 精确识别该形式。

照射量与比电荷共享正规形式 `current¹ · time¹ · mass⁻¹`。一种正规形式对应两种类型会使原生表达式
产生歧义 —— `toSpecificCharge()` 和一个假想的 `toExposure()` 都会匹配同一个混合单位，且没有
哪个答案更正确。单一类型保证了往返转换的确定性。

因此，二者的区别在于*你如何命名变量*，而不在于库返回给你什么类型 —— 正如物理学中两者都写作
C/kg 一样。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

val exposure = 1 of roentgens                   // read as an ionisation dose
exposure into coulombsPerKilogram                // 2.58e-4

// The charge liberated in 1 kg of air
val q = exposure * (1 of kilo.grams)
q into coulombs                                   // 2.58e-4

// A survey reading in milliroentgen
val small = 20 of milli.roentgens
small into coulombsPerKilogram                    // ≈ 5.16e-6
```

## 实际示例 — 一次老式剂量计读数

一支笔式剂量计在一个班次后显示 **200 mR**。将其换算为SI单位，以及换算为该电离室校准所用的
1公斤空气中释放的电荷:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

val shift = 200 of milli.roentgens
shift into coulombsPerKilogram                    // ≈ 5.16e-5
(shift * (1 of kilo.grams)) into micro.coulombs   // ≈ 51.6 µC
```

## 另请参阅

* [比电荷](../electrical/specificcharge.zh.md) —— 同一类型，作为粒子属性来解读。
* [吸收剂量](absorbed-dose.zh.md)与[剂量当量](dose-equivalent.zh.md) —— 基于能量的剂量。
* [剂量率](dose-rate.zh.md) —— 单位时间的剂量。
