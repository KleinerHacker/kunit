# 比热容

包：`org.pcsoft.framework.kunit.thermo.specificheatcapacity`
基本单位：**焦耳每千克-开尔文**（`KSpecificHeatCapacityUnit.BASE == KSpecificHeatCapacityUnit.JOULE_PER_KILOGRAM_KELVIN`）

类型：**构造单位**

比热容是材料*每单位质量*的[热容](heat-capacity.md)：`J/(kg·K)`。它是每一个
"加热到某温度需要多少能量"计算背后的材料属性。

`KSpecificHeatCapacityUnitInstance` 包装了一个恰好由三项组成的
`KMixedUnitInstance`，处于规范正规形式 `distance² · time⁻² · temperature⁻¹`
（`m²·s⁻²·K⁻¹`）—— 质量维度会相互抵消，这与[比能](specific-energy.md)完全一致。
温度维度是**差**组（`KTemperatureDifferenceUnit`），永远不是仿射的绝对温度。

## 命名单位

| 单位 | 符号 | 令牌 | 1 单位相当于多少 J/(kg·K) |
|---|---|---:|---:|
| 焦耳每千克-开尔文 | `J/(kg·K)` | `joulesPerKilogramKelvin` | 1.0 |
| 卡路里每克-开尔文 | `cal/(g·K)` | `caloriesPerGramKelvin` | 4184.0 |
| 英热单位每磅-华氏度 | `Btu/(lb·°F)` | `btusPerPoundFahrenheit` | 4186.8 |

以上单位均支持完整的 SI 前缀范围（`kilo.joulesPerKilogramKelvin` 等）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*

val water = 4184 of joulesPerKilogramKelvin
water into caloriesPerGramKelvin   // 1.0（根据卡路里的定义，水恰好是 1 cal/(g·K)）
```

## 现实示例：给浴缸加热

150 升水（150 kg）从 12 °C 加热到 40 °C。水的比热容是 4184 J/(kg·K)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.*

val water = 4184 of joulesPerKilogramKelvin
val bath = 150 of kilo.grams
val rise = (40 of celsius) - (12 of celsius)  // 28 K

// 路径 1：先构建浴缸的热容
val tubCapacity = water * bath                // KHeatCapacityUnitInstance
tubCapacity into joulesPerKelvin              // 627_600.0 J/K
val energy = tubCapacity * rise               // KEnergyUnitInstance
energy into mega.joules                       // ≈ 17.57 MJ

// 路径 2：改为先经过比能（每千克能量）
val perKilogram = water * rise                // KSpecificEnergyUnitInstance，117_152 J/kg
val sameEnergy = perKilogram * bath           // KEnergyUnitInstance
sameEnergy into mega.joules                   // ≈ 17.57 MJ —— 结果一致
```

## 用相邻单位计算

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `heatCapacity / mass` | `KSpecificHeatCapacityUnitInstance` | 由物体得到材料属性 |
| `specificEnergy / temperatureDifference` | `KSpecificHeatCapacityUnitInstance` | 相同，经由比能 |
| `specificHeatCapacity * mass` | `KHeatCapacityUnitInstance` | 该物体的热容 |
| `mass * specificHeatCapacity` | `KHeatCapacityUnitInstance` | 相同（可交换） |
| `heatCapacity / specificHeatCapacity` | `KMassUnitInstance` | 该物体的质量 |
| `specificHeatCapacity * temperatureDifference` | `KSpecificEnergyUnitInstance` | 每千克的能量 |
| `temperatureDifference * specificHeatCapacity` | `KSpecificEnergyUnitInstance` | 相同（可交换） |
| `specificEnergy / specificHeatCapacity` | `KTemperatureDifferenceUnitInstance` | 可达到的温升 |

## 分解方式

全部三种分解方式都产生相同的类型化、值相等的实例。

| 分解方式 | 形式 | 结果 |
|---|---|---|
| `heatCapacity / mass` | 类型化操作符 | `KSpecificHeatCapacityUnitInstance` |
| `specificEnergy / temperatureDifference` | 类型化操作符 | `KSpecificHeatCapacityUnitInstance` |
| `distance² · time⁻² · temperature⁻¹` | 原生表达式 + `toSpecificHeatCapacity()` | `KSpecificHeatCapacityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaHeatCapacity   = (1 of joulesPerKelvin) / (1 of kilo.grams)
val viaSpecificEnergy = (1 of joulesPerKilogram) / KTemperatureDifference.ofKelvin(1)
val native = (
    ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toSpecificHeatCapacity()

viaHeatCapacity == viaSpecificEnergy // true
viaHeatCapacity == native            // true —— 全部都是 1.0 J/(kg·K)
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*

val total = (1 of kilo.joulesPerKilogramKelvin) + (500 of joulesPerKilogramKelvin)  // 1500
(1 of kilo.joulesPerKilogramKelvin) > (500 of joulesPerKilogramKelvin)              // true
(1 of kilo.joulesPerKilogramKelvin) == (1000 of joulesPerKilogramKelvin)            // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*

(4184 of joulesPerKilogramKelvin).toString()                                // "4184.0 J/(kg·K)"
"${(4184 of joulesPerKilogramKelvin) into caloriesPerGramKelvin} cal/(g·K)" // "1.0 cal/(g·K)"
```

## 记法

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `J/(kg·K)` | `joulesPerKilogramKelvin` | 比热容，基本单位 |
| `m²·s⁻²·K⁻¹` | `(meters pow 2) / (seconds pow 2) / ΔK` | 相同的量以基础维度表示 |
| `cal/(g·K)` | `caloriesPerGramKelvin` | 卡路里每克-开尔文 |
| `c = C / m` | `(4184 of joulesPerKelvin) / (1 of kilo.grams)` | 由热容 ÷ 质量得到 |
| `c = q / ΔT` | `(8368 of joulesPerKilogram) / rise` | 由比能 ÷ 温升得到 |
| `C = c · m` | `water * bath` | 由材料 × 质量得到物体热容 |
| `Q = c · m · ΔT` | `water * bath * rise` | 总能量 |
