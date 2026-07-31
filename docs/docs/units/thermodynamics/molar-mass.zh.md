# 摩尔质量

包：`org.pcsoft.framework.kunit.thermo.molarmass`
基本单位： **克每摩尔**（`KMolarMassUnit.BASE == KMolarMassUnit.GRAM_PER_MOLE`）

类型： **构造单位**

摩尔质量是每单位物质的量的质量：`mass / amountOfSubstance`（`g/mol`）。它是宏观世界（天平上的克）
与微观粒子世界（摩尔）之间的桥梁，数值上等于物质的相对原子质量或分子质量。

`KMolarMassUnitInstance` 包装了一个恰好由两项组成的 `KMixedUnitInstance`，处于规范正规形式
`mass¹ · substance⁻¹`（`g·mol⁻¹`），始终以 g/mol 归一化。由于本库将质量归一化为克， 原始分量基本单位 *就是*
该命名基本单位——不涉及任何桥接系数。

除以密度即得到[摩尔体积](molar-volume.md）；[元素周期表](../../periodic-table.md)中的每个元素 都以此单位组表示其摩尔质量。

## 命名单位

| 单位         | 符号       |                 令牌 | 1 单位相当于多少 g/mol |
|--------------|------------|---------------------:|-----------------------:|
| 克每摩尔     | `g/mol`    |       `gramsPerMole` |                    1.0 |
| 千克每摩尔   | `kg/mol`   |   `kilogramsPerMole` |                 1000.0 |
| 磅每磅摩尔   | `lb/lbmol` | `poundsPerPoundMole` |                    1.0 |
| 道尔顿每粒子 | `Da`       |   `daltonsPerEntity` |          1.00000000105 |

磅摩尔的定义使其以磅为单位的质量等于摩尔质量，这使得 `lb/lbmol` 在数值上与 `g/mol` 相同。 自 2019 年 SI
重新定义以来，摩尔质量常数不再精确等于 1 g/mol，因此才有了道尔顿系数。 所有单位均支持完整的 SI 前缀范围（
`kilo.gramsPerMole`、`milli.kilogramsPerMole` 等）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarmass.*

val water = 18.015 of gramsPerMole
water into gramsPerMole      // 18.015
water into kilogramsPerMole  // 0.018015
water into daltonsPerEntity  // ≈ 18.015 Da，每个分子
```

## 现实示例：称量一摩尔物质

一份食谱需要 0.25 mol 食盐（NaCl，58.44 g/mol）。你需要称量多少克——一袋 500 g 的食盐 又含有多少摩尔？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.*

val saltMolarMass = 58.44 of gramsPerMole

// 0.25 mol 对应多少质量？
val portion = saltMolarMass * (0.25 of moles) // KMassUnitInstance
portion into grams                            // 14.61 g

// 500 g 一袋含有多少摩尔？
val amount = (500 of grams) / saltMolarMass   // KAmountOfSubstanceUnitInstance
amount into moles                             // ≈ 8.556 mol

// 以及从称量样本反推出的摩尔质量本身：
val measured = (14.61 of grams) / (0.25 of moles)
measured into gramsPerMole                    // 58.44
```

## 用核心单位（质量与物质的量）计算

| 表达式                          | 结果类型                         | 含义                        |
|---------------------------------|----------------------------------|-----------------------------|
| `mass / amountOfSubstance`      | `KMolarMassUnitInstance`         | 摩尔质量                    |
| `molarMass * amountOfSubstance` | `KMassUnitInstance`              | 总质量                      |
| `amountOfSubstance * molarMass` | `KMassUnitInstance`              | 总质量（可交换）            |
| `mass / molarMass`              | `KAmountOfSubstanceUnitInstance` | 涉及的物质的量              |
| `molarMass / density`           | `KMolarVolumeUnitInstance`       | [摩尔体积](molar-volume.md) |

## 分解方式

两种分解方式都产生相同的类型化、值相等的实例。

| 分解方式                   | 形式                         | 结果                              |
|----------------------------|------------------------------|-----------------------------------|
| `mass / amountOfSubstance` | 类型化操作符                 | 直接得到 `KMolarMassUnitInstance` |
| `mass · substance⁻¹`       | 原生表达式 + `toMolarMass()` | `KMolarMassUnitInstance`          |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.*

// 类型化操作符形式
val typed = (18.015 of grams) / (1 of moles)

// 原生基础维度形式（g·mol⁻¹），由 toMolarMass() 识别
val native = ((18.015 of grams).toUnit() / (1 of moles).toUnit()).toMolarMass()

typed == native // true —— 两者都是 18.015 g/mol
```

`toMolarMass()` 只识别规范正规形式；错误的形状会抛出 `IllegalStateException`。

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarmass.*

val total = (10 of gramsPerMole) + (4 of gramsPerMole) // 14 g/mol
val rest  = (10 of gramsPerMole) - (4 of gramsPerMole) // 6 g/mol

(1 of kilogramsPerMole) > (500 of gramsPerMole)   // true
(1 of kilogramsPerMole) == (1000 of gramsPerMole) // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarmass.*

(1 of kilogramsPerMole).toString()  // "1000.0 g/mol"
(18.015 of gramsPerMole).toString() // "18.015 g/mol"
```

## 记法

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·`
表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示      | Kotlin                               | 含义                           |
|---------------|--------------------------------------|--------------------------------|
| `g/mol`       | `gramsPerMole`                       | 摩尔质量，基本单位 —— 命名令牌 |
| `g·mol⁻¹`     | `grams / moles`                      | 相同的量以基础维度表示         |
| `kg/mol`      | `kilogramsPerMole`                   | 千克每摩尔                     |
| `Da`          | `daltonsPerEntity`                   | 道尔顿每基本粒子               |
| `M = m / n`   | `(14.61 of grams) / (0.25 of moles)` | 由质量 ÷ 物质的量得到摩尔质量  |
| `m = M · n`   | `saltMolarMass * (0.25 of moles)`    | 由摩尔质量 × 物质的量得到质量  |
| `n = m / M`   | `(500 of grams) / saltMolarMass`     | 由质量 ÷ 摩尔质量得到物质的量  |
| `V_m = M / ρ` | `molarMass / density`                | 由摩尔质量 ÷ 密度得到摩尔体积  |
