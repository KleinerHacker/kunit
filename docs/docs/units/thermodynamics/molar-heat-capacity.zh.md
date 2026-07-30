# 摩尔热容

包：`org.pcsoft.framework.kunit.thermo.molarheatcapacity`
基本单位：**焦耳每摩尔-开尔文**（`KMolarHeatCapacityUnit.BASE == KMolarHeatCapacityUnit.JOULE_PER_MOLE_KELVIN`）

类型：**构造单位**

摩尔热容是物质*每摩尔*的[热容](heat-capacity.md)：`J/(mol·K)`。它是气体和
化学热力学的自然表达形式，其中的量以摩尔而非千克计（那是
[比热容](specific-heat-capacity.md)）。

`KMolarHeatCapacityUnitInstance` 包装了一个恰好由五项组成的 `KMixedUnitInstance`，
处于规范正规形式 `mass¹ · distance² · time⁻² · substance⁻¹ · temperature⁻¹`
（`kg·m²·s⁻²·mol⁻¹·K⁻¹`）。温度维度是**差**组，永远不是仿射的绝对温度。

## 命名单位

| 单位 | 符号 | 令牌 | 1 单位相当于多少 J/(mol·K) |
|---|---|---:|---:|
| 焦耳每摩尔-开尔文 | `J/(mol·K)` | `joulesPerMoleKelvin` | 1.0 |
| 卡路里每摩尔-开尔文 | `cal/(mol·K)` | `caloriesPerMoleKelvin` | 4.184 |

两者都支持完整的 SI 前缀范围（`kilo.joulesPerMoleKelvin`、
`milli.joulesPerMoleKelvin` 等）。

## 气体常数

该组以 `GAS_CONSTANT`（8.31446261815324 J/(mol·K)）的形式暴露摩尔气体常数的精确
SI 值 —— 这是一个纯 `Double`，因此既可作为系数又可作为读数使用。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*

val r = GAS_CONSTANT of joulesPerMoleKelvin
r into joulesPerMoleKelvin   // 8.31446261815324
r into caloriesPerMoleKelvin // ≈ 1.987
```

## 现实示例：加热氮气（杜隆-珀替定律的合理性检验）

双原子氮气的 `c_p ≈ 29.1 J/(mol·K)`。将 3 摩尔氮气加热 50 K 需要多少能量，
每摩尔又是多少？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.molarenergy.joulesPerMole
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val nitrogen = 29.1 of joulesPerMoleKelvin
val sample = 3 of moles
val rise = KTemperatureDifference.ofKelvin(50)

// 路径 1：先得到样品的热容，再求能量
val sampleCapacity = nitrogen * sample     // KHeatCapacityUnitInstance
sampleCapacity into joulesPerKelvin        // 87.3 J/K
val energy = sampleCapacity * rise         // KEnergyUnitInstance
energy into joules                         // 4365.0 J

// 路径 2：先求每摩尔的值
val perMole = nitrogen * rise              // KMolarEnergyUnitInstance
perMole into joulesPerMole                 // 1455.0 J/mol
val sameEnergy = perMole * sample          // KEnergyUnitInstance
sameEnergy into joules                     // 4365.0 J —— 结果一致
```

## 用相邻单位计算

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `heatCapacity / amountOfSubstance` | `KMolarHeatCapacityUnitInstance` | 由样品得到物质属性 |
| `molarEnergy / temperatureDifference` | `KMolarHeatCapacityUnitInstance` | 相同，经由摩尔能量 |
| `molarHeatCapacity * amountOfSubstance` | `KHeatCapacityUnitInstance` | 样品的热容 |
| `amountOfSubstance * molarHeatCapacity` | `KHeatCapacityUnitInstance` | 相同（可交换） |
| `heatCapacity / molarHeatCapacity` | `KAmountOfSubstanceUnitInstance` | 物质的量 |
| `molarHeatCapacity * temperatureDifference` | `KMolarEnergyUnitInstance` | 每摩尔的能量 |
| `temperatureDifference * molarHeatCapacity` | `KMolarEnergyUnitInstance` | 相同（可交换） |
| `molarEnergy / molarHeatCapacity` | `KTemperatureDifferenceUnitInstance` | 可达到的温升 |

## 分解方式

全部三种分解方式都产生相同的类型化、值相等的实例。

| 分解方式 | 形式 | 结果 |
|---|---|---|
| `heatCapacity / amountOfSubstance` | 类型化操作符 | `KMolarHeatCapacityUnitInstance` |
| `molarEnergy / temperatureDifference` | 类型化操作符 | `KMolarHeatCapacityUnitInstance` |
| `mass · distance² · time⁻² · substance⁻¹ · temperature⁻¹` | 原生 + `toMolarHeatCapacity()` | `KMolarHeatCapacityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.molarenergy.joulesPerMole
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaHeatCapacity = (1 of joulesPerKelvin) / (1 of moles)
val viaMolarEnergy  = (1 of joulesPerMole) / KTemperatureDifference.ofKelvin(1)
val native = (
    (1000 of grams).toUnit() *
        ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        (1 of moles).toUnit() /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toMolarHeatCapacity()

viaHeatCapacity == viaMolarEnergy // true
viaHeatCapacity == native         // true —— 全部都是 1.0 J/(mol·K)
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*

val total = (1 of kilo.joulesPerMoleKelvin) + (500 of joulesPerMoleKelvin)  // 1500 J/(mol·K)
(1 of kilo.joulesPerMoleKelvin) > (500 of joulesPerMoleKelvin)              // true
(1 of kilo.joulesPerMoleKelvin) == (1000 of joulesPerMoleKelvin)            // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*

(29.1 of joulesPerMoleKelvin).toString()                                     // "29.1 J/(mol·K)"
"${(29.1 of joulesPerMoleKelvin) into caloriesPerMoleKelvin} cal/(mol·K)"    // "6.955... cal/(mol·K)"
```

## 记法

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `J/(mol·K)` | `joulesPerMoleKelvin` | 摩尔热容，基本单位 |
| `kg·m²·s⁻²·mol⁻¹·K⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / moles / ΔK` | 基础维度 |
| `cal/(mol·K)` | `caloriesPerMoleKelvin` | 卡路里每摩尔-开尔文 |
| `R` | `GAS_CONSTANT of joulesPerMoleKelvin` | 摩尔气体常数，8.3145 J/(mol·K) |
| `C_m = C / n` | `(58.2 of joulesPerKelvin) / (2 of moles)` | 由热容 ÷ 物质的量得到 |
| `C_m = ΔH_m / ΔT` | `(58.2 of joulesPerMole) / rise` | 由摩尔能量 ÷ 温升得到 |
| `Q = C_m · n · ΔT` | `nitrogen * sample * rise` | 总能量 |
