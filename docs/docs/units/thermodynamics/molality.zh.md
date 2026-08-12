# 质量摩尔浓度

包: `org.pcsoft.framework.kunit.thermo.molality`
基本单位: **摩尔每千克** (`KMolalityUnit.BASE == KMolalityUnit.MOLES_PER_KILOGRAM`)

类型: **构造单位（constructed unit）**

质量摩尔浓度 `b` 是**每单位溶剂质量**中溶解了多少物质: `b = n / m`。与以体积为基准的
[物质的量浓度](concentration.zh.md)不同，质量摩尔浓度在溶液加热时不会改变 —— 溶剂的质量不受
热膨胀影响。这使其成为凝固点下降、沸点升高等依数性性质的首选量。

其规范基本量纲正规形式为 `substance¹ · mass⁻¹`。

## 命名单位

| 单位              | 符号      |                    令牌 | 1单位对应的mol/kg |
|-------------------|-----------|-------------------------:|-----------------:|
| 摩尔每千克           | `mol/kg`  |       `molesPerKilogram` |              1.0 |
| 毫摩尔每千克         | `mmol/kg` | `millimolesPerKilogram`  |            0.001 |

所有令牌都接受任何SI前缀（`milli.molesPerKilogram` 等）。

## 分解

该分组有一种分解方式，其两种形式都会生成相同的、值相等的类型化实例。请注意，原生形式是由
**单位模板**组装而成：对于带有质量项的分组，原始混合值是以克为基础的乘积，而类型化实例则以
命名单位存储其值。

| 形式               | 表达式                                              |
|-------------------|--------------------------------------------------------|
| 类型化运算符          | `amountOfSubstance / mass`                              |
| 原生形式（`toX()`）   | `(0.25 of moles / kilo.grams).toMolality()`             |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molality.*

val typed = (0.5 of moles) / (2 of kilo.grams)
val native = (0.25 of moles.toUnit() / kilo.grams.toUnit()).toMolality()

typed == native               // true
typed into molesPerKilogram   // 0.25
```

## 使用该分组进行计算

| 表达式                            | 结果类型                          | 含义                       |
|--------------------------------------|-------------------------------------|-----------------------------|
| `amountOfSubstance / mass`          | `KMolalityUnitInstance`             | `b = n / m`                 |
| `molality * mass`                   | `KAmountOfSubstanceUnitInstance`    | `n = b · m`                 |
| `amountOfSubstance / molality`      | `KMassUnitInstance`                 | 所需的溶剂质量                 |
| `1 / molarMass`                     | `KMolalityUnitInstance`             | 纯物质的质量摩尔浓度              |
| `1 / molality`                      | `KMolarMassUnitInstance`            | 换回摩尔质量                   |

最后两个关系反映了质量摩尔浓度与[摩尔质量](molar-mass.zh.md)互为倒数。

## 实际示例 — 一千克水中含有多少摩尔？

水的摩尔质量为18.015 g/mol，因此一千克水中约含55.5 mol —— 这正是倒数关系的体现:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molality.*

val b = 1 / (18.015 of gramsPerMole)   // KMolalityUnitInstance
b into molesPerKilogram                 // ≈ 55.51

// A 0.5 molal salt solution in 2 kg of water
val n = (0.5 of molesPerKilogram) * (2 of kilo.grams)
n into moles                            // 1.0

// And back to the molar mass
(1 / b) into gramsPerMole               // ≈ 18.015
```

## 值语义

`equals`/`hashCode` 比较**归一化的mol/kg值**，因此
`(1 of molesPerKilogram) == (1000 of millimolesPerKilogram)`。`toString()` 以基本单位渲染
该值: `"0.25 mol/kg"`。

## 另请参阅

* [物质的量浓度](concentration.zh.md) —— 相同的思路，按体积计算。
* [摩尔质量](molar-mass.zh.md) —— 倒数量。
* [热力学概览](overview.zh.md)
