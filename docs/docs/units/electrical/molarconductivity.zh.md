# 摩尔电导率

包: `org.pcsoft.framework.kunit.electric.molarconductivity`
基本单位: **西门子平方米每摩尔**
(`KMolarConductivityUnit.BASE == KMolarConductivityUnit.SIEMENS_SQUARE_METER_PER_MOLE`)

类型: **构成单位**

电解质的摩尔电导率 `Λ` 是其[电导率](conductivity.zh.md)按[浓度](../thermodynamics/concentration.zh.md)归一化后的值:
`Λ = κ / c`。除以浓度使不同强度的溶液具有可比性 — 它回答的是"这种离子的导电能力如何",而不是"这个特定烧杯的导电能力如何"。

其规范的基本量纲标准形式为 `mass⁻¹ · time³ · current² · substance⁻¹`。长度量纲完全抵消: 电导率贡献
`length⁻³`,而分母中的浓度又贡献了另一个 `length⁻³`。

## 具名单位

| 单位                             | 符号       |                            标记 | 1单位折合 S·m²/mol |
|----------------------------------|--------------|---------------------------------:|-------------------:|
| 西门子平方米每摩尔              | `S*m^2/mol`  |    `siemensSquareMetersPerMole` |                1.0 |
| 西门子平方厘米每摩尔            | `S*cm^2/mol` | `siemensSquareCentimetersPerMole` |             1e-4 |

电化学表格通常以 S·cm²/mol 表示;SI 形式通常带有 milli 词头书写(`milli.siemensSquareMetersPerMole`)。
所有标记都支持任意 SI 词头。

## 分解

该组只有一种分解,两种形式都产生数值相等的同类型实例。原生形式由 **单位模板**组装而成,因为该组带有质量项:
原始混合值是以克为基准的乘积,而带类型的实例以具名单位存储其值。

| 形式             | 表达式                                                          |
|------------------|---------------------------------------------------------------------|
| 带类型运算符     | `conductivity / concentration`                                      |
| 原生 (`toX()`)   | `(0.01 of s³ · A² / kilo.grams / moles).toMolarConductivity()`      |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.conductivity.siemensPerMeter
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.molesPerLiter
import org.pcsoft.framework.kunit.electric.molarconductivity.*

val typed = (1.0 of siemensPerMeter) / (0.1 of molesPerLiter)
val native = (
    0.01 of (seconds pow 3) * (amperes.toUnit() pow 2) / kilo.grams.toUnit() / moles.toUnit()
).toMolarConductivity()

typed == native                          // true
typed into siemensSquareMetersPerMole    // 0.01
```

## 与该组一起计算

| 表达式                              | 结果类型                      | 含义       |
|-------------------------------------|-----------------------------------|---------------|
| `conductivity / concentration`      | `KMolarConductivityUnitInstance` | `Λ = κ / c`   |
| `molarConductivity * concentration` | `KConductivityUnitInstance`      | `κ = Λ · c`   |
| `conductivity / molarConductivity`  | `KConcentrationUnitInstance`     | `c = κ / Λ`   |
| `molarConductivity + …`             | `KMolarConductivityUnitInstance` | 科尔劳施定律 |

科尔劳施离子独立迁移定律指出,在无限稀释时摩尔电导率是各离子贡献的**总和** — 这正是该组同类型的 `+` 运算。

## 实际例子 — KCl 的科尔劳施定律

K⁺ 的极限离子电导率为 7.35 mS·m²/mol,Cl⁻ 为 7.63 mS·m²/mol。二者之和就是氯化钾的极限摩尔电导率,乘以浓度即可得到
仪表所读取的电导率:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.conductivity.siemensPerMeter
import org.pcsoft.framework.kunit.thermo.concentration.molesPerLiter
import org.pcsoft.framework.kunit.electric.molarconductivity.*

val potassium = 7.350 of milli.siemensSquareMetersPerMole
val chloride  = 7.635 of milli.siemensSquareMetersPerMole

val kcl = potassium + chloride                       // 科尔劳施
kcl into milli.siemensSquareMetersPerMole            // 14.985
kcl into siemensSquareCentimetersPerMole             // ≈ 149.85(表格值)

val kappa = kcl * (0.01 of molesPerLiter)            // KConductivityUnitInstance
kappa into siemensPerMeter                            // ≈ 0.1499 S/m
```

## 值语义

`equals`/`hashCode` 比较**归一化的 S·m²/mol 值**,所以
`(1 of siemensSquareMetersPerMole) == (10000 of siemensSquareCentimetersPerMole)`。`toString()` 以基本单位
显示数值: `"0.0126 S*m^2/mol"`。

## 另请参阅

* [电导率](conductivity.zh.md) — 分子。
* [物质的量浓度](../thermodynamics/concentration.zh.md) — 分母。
* [电导](conductance.zh.md) — 仪表测量的未归一化量。
* [电气工程概述](overview.zh.md)
