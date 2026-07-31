# 物质的量

包：`org.pcsoft.framework.kunit.thermo.amountofsubstance`
基本单位： **摩尔**（`KAmountOfSubstanceUnit.BASE == KAmountOfSubstanceUnit.MOLE`）

类型： **原生单位**

物质的量是七个 SI 基本量之一 —— 一个可直接测量、非组合的量，因此是 **原生单位**。
`KAmountOfSubstanceUnitInstance` 是普通的一维包装形式：一个指数为 1 的
`KAmountOfSubstanceUnit.BASE`（摩尔）项，始终以摩尔归一化。

它是热力学领域中每一个 *摩尔*量的基础 （[摩尔能量](molar-energy.md)、[摩尔热容](molar-heat-capacity.md)）。

## 命名单位

| 单位   | 符号    |         令牌 | 1 单位相当于多少 mol |
|--------|---------|-------------:|---------------------:|
| 摩尔   | `mol`   |      `moles` |                  1.0 |
| 磅摩尔 | `lbmol` | `poundMoles` |            453.59237 |

两者都支持完整的 SI 前缀范围（`milli.moles`、`micro.moles`、`kilo.moles` 等）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

val n = 2 of moles
n.value                 // 2.0（归一化为摩尔）
n into milli.moles      // 2000.0
(1 of kilo.moles) into moles // 1000.0
(1 of poundMoles) into moles // 453.59237
```

## 阿伏伽德罗常数

该组以 `AVOGADRO_CONSTANT` 的形式暴露阿伏伽德罗常数的精确 SI 值 （6.02214076e23 mol⁻¹），并在实例上提供便捷方法
`particleCount()`。两者都返回 纯 `Double`，因为粒子数是无量纲的。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

AVOGADRO_CONSTANT             // 6.02214076e23
(2 of moles).particleCount()  // ≈ 1.20443e24 个粒子
```

## 现实示例：溶解食盐

25 g 食盐（氯化钠，摩尔质量 58.44 g/mol）中含有多少摩尔，又相当于多少个化学式单元？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

val molarMass = 58.44        // NaCl 的 g/mol
val sample = 25 of grams

val n = (sample.value / molarMass) of moles
n into moles                 // ≈ 0.4278 mol
n into milli.moles           // ≈ 427.8 mmol
n.particleCount()            // ≈ 2.576e23 个化学式单元
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

// + / - ：同组，不同单位与前缀之间自动转换
val total = (1 of moles) + (500 of milli.moles)   // 1.5 mol
val rest  = (1 of moles) - (250 of milli.moles)   // 0.75 mol

// 比较（按归一化的摩尔值）
(1 of moles) > (500 of milli.moles)   // true
(1 of moles) == (1000 of milli.moles) // true
```

用另一个量乘以或除以物质的量会脱离到通用的混合单位引擎，除非存在类型化结果 —— 例如 `energy / amountOfSubstance`
是一个类型化的[摩尔能量](molar-energy.md)。

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

(2 of moles).toString()                        // "2.0 mol"
"${(2 of moles) into milli.moles} mmol"        // "2000.0 mmol"
```

## 记法

下表展示了该单位在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·` 表示乘法，`/`
表示分数。

| 数学表示      | Kotlin                                | 含义                                  |
|---------------|---------------------------------------|---------------------------------------|
| `mol`         | `moles`                               | 物质的量，基本单位                    |
| `mmol`        | `milli.moles`                         | 毫摩尔                                |
| `kmol`        | `kilo.moles`                          | 千摩尔                                |
| `lbmol`       | `poundMoles`                          | 磅摩尔（英制工程单位）                |
| `n = m / M`   | `(sample.value / molarMass) of moles` | 由质量 ÷ 摩尔质量得到物质的量         |
| `N = n · N_A` | `n.particleCount()`                   | 由物质的量 × 阿伏伽德罗常数得到粒子数 |
