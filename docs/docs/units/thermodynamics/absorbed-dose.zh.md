# 吸收剂量（戈瑞）

包: `org.pcsoft.framework.kunit.thermo.specificenergy`
基本单位: **焦耳每千克**
(`KSpecificEnergyUnit.BASE == KSpecificEnergyUnit.JOULE_PER_KILOGRAM`)

类型: **构造单位（constructed unit）**

吸收剂量 `D` 是电离辐射沉积在单位质量中的能量: `D = E / m`。其单位是
**戈瑞**，`1 Gy = 1 J/kg` —— 与[比能](specific-energy.zh.md)**在量纲上完全相同**。

## 为什么戈瑞没有自己的类型

KUnit 有意使用 `KSpecificEnergyUnitInstance` 而不是单独的 `KAbsorbedDoseUnitInstance` 来建模吸收剂量。
原因在于本库的形式识别约定:

* 每个标准化分组都有**唯一**的规范基本量纲正规形式，且
* `toX()` 精确识别该形式。

吸收剂量与比能共享正规形式 `length² · time⁻²`。一种正规形式对应两种类型会使原生表达式产生歧义 ——
`toSpecificEnergy()` 和一个假想的 `toAbsorbedDose()` 都会匹配同一个混合单位，且没有哪个答案更正确。
单一类型保证了往返转换的确定性。

因此，二者的区别在于*你如何命名变量*，而不在于库返回给你什么类型 —— 这正如物理学中，
戈瑞**就是**焦耳每千克。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val dose = 2 of milli.joulesPerKilogram      // read as 2 mGy
dose into joulesPerKilogram                   // 0.002

// The energy deposited in a 70 kg body
val energy = dose * (70 of kilo.grams)
energy into joules                            // 0.14 J
```

## 实际示例 — 一次胸部X光检查

一次胸部X光检查大约沉积 **0.1 mGy**。对一个70公斤的人来说，这相当于多少总能量？
与一年的天然本底辐射（≈ 2.4 mGy）相比又如何？

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val xray = 0.1 of milli.joulesPerKilogram
val background = 2.4 of milli.joulesPerKilogram

(xray * (70 of kilo.grams)) into milli.joules      // 7.0 mJ
(background into joulesPerKilogram) / (xray into joulesPerKilogram)   // 24 X-rays per year of background
```

## 另请参阅

* [比能](specific-energy.zh.md) —— 同一类型，作为能量密度来解读。
* [剂量当量](dose-equivalent.zh.md) —— 按生物效应加权的希沃特。
* [剂量率](dose-rate.zh.md) —— 单位时间的剂量，**拥有**自己的类型。
* [照射量](exposure.zh.md) —— 基于电荷的电离剂量。
