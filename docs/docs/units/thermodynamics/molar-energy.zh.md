# 摩尔能量

包：`org.pcsoft.framework.kunit.thermo.molarenergy`
基本单位：**焦耳每摩尔**（`KMolarEnergyUnit.BASE == KMolarEnergyUnit.JOULE_PER_MOLE`）

类型：**构造单位**

摩尔能量是每单位物质的量的能量：`energy / amountOfSubstance`（`J/mol`）。
根据语境不同，同一个量也被称为*摩尔焓*、*反应焓*或*键能*。

`KMolarEnergyUnitInstance` 包装了一个恰好由四项组成的 `KMixedUnitInstance`，
处于规范正规形式 `mass¹ · distance² · time⁻² · substance⁻¹`（`kg·m²·s⁻²·mol⁻¹`），
始终以 J/mol 归一化。

每单位温度得到[摩尔热容](molar-heat-capacity.md)；每千克而非每摩尔则得到
[比能](specific-energy.md)。

## 命名单位

| 单位 | 符号 | 令牌 | 1 单位相当于多少 J/mol |
|---|---|---:|---:|
| 焦耳每摩尔 | `J/mol` | `joulesPerMole` | 1.0 |
| 卡路里每摩尔 | `cal/mol` | `caloriesPerMole` | 4.184 |
| 电子伏特每粒子 | `eV/entity` | `electronVoltsPerEntity` | 96485.33212 |

电子伏特每粒子令牌将*每粒子*能量转换为*每摩尔*能量 —— 其换算系数是法拉第常数。
所有单位均支持完整的 SI 前缀范围（`kilo.joulesPerMole`、`kilo.caloriesPerMole`、
`milli.electronVoltsPerEntity` 等）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarenergy.*

val dH = 286 of kilo.joulesPerMole
dH into joulesPerMole            // 286_000.0
dH into kilo.caloriesPerMole     // ≈ 68.36
dH into electronVoltsPerEntity   // ≈ 2.964 eV，每个分子
```

## 现实示例：氢气燃烧

液态水的生成焓为 −286 kJ/mol。4 摩尔氢气燃烧释放多少能量，每个分子又是多少？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarenergy.*

val formation = -286 of kilo.joulesPerMole
val hydrogen = 4 of moles

val released = formation * hydrogen   // KEnergyUnitInstance
released into kilo.joules             // -1144.0 kJ
released into mega.joules             // -1.144 MJ

// 以化学家惯用的单位表示，每个分子的能量
formation into electronVoltsPerEntity // ≈ -2.964 eV

// 反过来：1 MJ 对应多少物质的量？
val n = (1 of mega.joules) / formation // KAmountOfSubstanceUnitInstance
n into moles                           // ≈ -3.497 mol
```

## 用核心单位（能量与物质的量）计算

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `energy / amountOfSubstance` | `KMolarEnergyUnitInstance` | 摩尔能量 |
| `molarEnergy * amountOfSubstance` | `KEnergyUnitInstance` | 总能量 |
| `amountOfSubstance * molarEnergy` | `KEnergyUnitInstance` | 总能量（可交换） |
| `energy / molarEnergy` | `KAmountOfSubstanceUnitInstance` | 涉及的物质的量 |

## 分解方式

两种分解方式都产生相同的类型化、值相等的实例。

| 分解方式 | 形式 | 结果 |
|---|---|---|
| `energy / amountOfSubstance` | 类型化操作符 | 直接得到 `KMolarEnergyUnitInstance` |
| `mass · distance² · time⁻² · substance⁻¹` | 原生表达式 + `toMolarEnergy()` | `KMolarEnergyUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarenergy.*

// 类型化操作符形式
val typed = (1 of joules) / (1 of moles)

// 原生基础维度形式（kg·m²·s⁻²·mol⁻¹），由 toMolarEnergy() 识别
val native = (
    (1000 of grams).toUnit() *
        ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        (1 of moles).toUnit()
    ).toMolarEnergy()

typed == native // true —— 两者都是 1.0 J/mol
```

`toMolarEnergy()` 只识别规范正规形式；错误的形状会抛出 `IllegalStateException`。

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarenergy.*

val total = (1 of kilo.joulesPerMole) + (500 of joulesPerMole)  // 1500 J/mol
val rest  = (1 of kilo.joulesPerMole) - (250 of joulesPerMole)  // 750 J/mol

(1 of kilo.joulesPerMole) > (500 of joulesPerMole)   // true
(1 of kilo.joulesPerMole) == (1000 of joulesPerMole) // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarenergy.*

(286 of kilo.joulesPerMole).toString()                        // "286000.0 J/mol"
"${(286 of kilo.joulesPerMole) into caloriesPerMole} cal/mol" // "68355.6... cal/mol"
```

## 记法

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `J/mol` | `joulesPerMole` | 摩尔能量，基本单位 —— 命名令牌 |
| `kg·m²·s⁻²·mol⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / moles` | 相同的量以基础维度表示 |
| `kJ/mol` | `kilo.joulesPerMole` | 千焦耳每摩尔 |
| `eV`（每粒子） | `electronVoltsPerEntity` | 每个基本粒子的电子伏特 |
| `ΔH_m = Q / n` | `(572 of kilo.joules) / (2 of moles)` | 由能量 ÷ 物质的量得到摩尔能量 |
| `Q = ΔH_m · n` | `formation * hydrogen` | 由摩尔能量 × 物质的量得到能量 |
| `n = Q / ΔH_m` | `(1 of mega.joules) / formation` | 由能量 ÷ 摩尔能量得到物质的量 |
