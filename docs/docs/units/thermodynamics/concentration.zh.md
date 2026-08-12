# 物质的量浓度（摩尔浓度）

包: `org.pcsoft.framework.kunit.thermo.concentration`
基本单位: **摩尔每立方米** (`KConcentrationUnit.BASE == KConcentrationUnit.MOLES_PER_CUBIC_METER`)

类型: **构造单位（constructed unit）**

物质的量浓度 `c` 是**每单位溶液体积**中溶解了多少物质: `c = n / V`。化学中几乎总是以摩尔每升表示，
称为**摩尔浓度**，记作 `M`；临床实验室则使用毫摩尔每升。

其规范基本量纲正规形式为 `substance¹ · length⁻³`。

## 命名单位

| 单位                | 符号      |                    令牌 | 1单位对应的mol/m³ |
|---------------------|-----------|------------------------:|-----------------:|
| 摩尔每立方米          | `mol/m^3` |    `molesPerCubicMeter` |              1.0 |
| 摩尔每升（摩尔浓度）    | `mol/l`   |         `molesPerLiter` |             1000 |
| 摩尔浓度（`M`）        | `mol/l`   |                 `molar` |             1000 |
| 毫摩尔每升            | `mmol/l`  |    `millimolesPerLiter` |              1.0 |

`molar` 是 `molesPerLiter` 的另一种拼写，而非独立的单位。请注意，毫摩尔每升在数值上与摩尔每
立方米相同 —— SI 基本单位在数值上正是临床单位。所有令牌都接受任何SI前缀
（`milli.molesPerLiter`、`micro.molar` 等）。

## 分解

该分组有一种分解方式，其两种形式都会生成相同的、值相等的类型化实例:

| 形式               | 表达式                                                                    |
|-------------------|--------------------------------------------------------------------------|
| 类型化运算符          | `amountOfSubstance / volume`                                             |
| 原生形式（`toX()`）   | `((0.5 of moles).toUnit() / (2 of liters).toUnit()).toConcentration()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.*

val typed = (0.5 of moles) / (2 of liters)
val native = ((0.5 of moles).toUnit() / (2 of liters).toUnit()).toConcentration()

typed == native            // true
typed into molesPerLiter   // 0.25
```

## 使用该分组进行计算

| 表达式                                  | 结果类型                          | 含义                     |
|--------------------------------------------|-----------------------------------|-----------------------------|
| `amountOfSubstance / volume`              | `KConcentrationUnitInstance`      | `c = n / V`                 |
| `concentration * volume`                  | `KAmountOfSubstanceUnitInstance`  | `n = c · V`                 |
| `amountOfSubstance / concentration`       | `KVolumeUnitInstance`             | 所需的体积                    |
| `conductivity / concentration`            | `KMolarConductivityUnitInstance`  | `Λ = κ / c`                 |

## 实际示例 — 血糖

大约5升血液中空腹血糖 **5.5 mmol/l** 对应于:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.*

val c = 5.5 of millimolesPerLiter
c into molesPerCubicMeter          // 5.5 — the SI unit is numerically the clinical one

val n = c * (5 of liters)          // KAmountOfSubstanceUnitInstance
n into milli.moles                 // 27.5 mmol of glucose in the bloodstream

// How much solution holds 1 mol at that concentration?
val v = (1 of moles) / c           // KVolumeUnitInstance
v into liters                       // ≈ 181.8 l
```

## 值语义

`equals`/`hashCode` 比较**归一化的mol/m³值**，因此
`(1 of molesPerLiter) == (1000 of molesPerCubicMeter)`。`toString()` 以基本单位渲染该值:
`"1000.0 mol/m^3"`。

## 另请参阅

* [质量摩尔浓度](molality.zh.md) —— 相同的思路，按**质量**计算，不受热膨胀影响。
* [物质的量](amount-of-substance.zh.md) —— 分子部分。
* [摩尔体积](molar-volume.zh.md) —— 纯物质的倒数量。
* [热力学概览](overview.zh.md)
