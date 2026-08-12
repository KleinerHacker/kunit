# 体积热容

包：`org.pcsoft.framework.kunit.thermo.volumetricheatcapacity`
基本单位： **焦耳每立方米-开尔文**（`KVolumetricHeatCapacityUnit.BASE == KVolumetricHeatCapacityUnit.JOULE_PER_CUBIC_METER_KELVIN`）

类型： **构造单位**

体积热容 `c_v` 表示某种材料的 **体积** 每升高一开尔文所储存的热量：
`c_v = C / V = c · ρ`。这是决定建筑物、储水箱或散热器实际拥有多少热质量的量 —— 若密度不同，
两种比热容相同的材料所储存的热量也会大不相同。

其规范的基础维度正规形式为 `mass · length⁻¹ · time⁻² · temperature⁻¹`。

## 命名单位

| 单位                     | 符号           |                              令牌 | 1 单位相当于多少 J/(m³·K) |
|--------------------------|----------------|-----------------------------------:|--------------------------:|
| 焦耳每立方米-开尔文      | `J/(m^3*K)`    |       `joulesPerCubicMeterKelvin` |                        1.0 |
| 卡路里每立方厘米-开尔文  | `cal/(cm^3*K)` | `caloriesPerCubicCentimeterKelvin` |                    4.184e6 |

由于数值很大，实际使用中兆焦耳形式更为实用：水约为 4.18 MJ/(m³·K)。所有令牌均支持
所有 SI 前缀（`mega.joulesPerCubicMeterKelvin` 等）。

## 分解方式

该组共有**两种**分解方式。两者都汇入同一个规范化工厂，因此会产生
相同的类型化、值相等的实例：

| 形式                | 表达式                                                             |
|---------------------|---------------------------------------------------------------------|
| 类型化操作符 A      | `heatCapacity / volume`                                          |
| 类型化操作符 B      | `specificHeatCapacity * density`                                 |
| 原生表达式 (`toX()`) | `(1 of kilo.grams / m / s² / K).toVolumetricHeatCapacity()`      |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin
import org.pcsoft.framework.kunit.thermo.volumetricheatcapacity.*

val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

val viaHeatCapacity = (4184 of joulesPerKelvin) / (1 of liters)   // A
val viaDensity = (4184 of joulesPerKilogramKelvin) * water        // B

viaHeatCapacity == viaDensity                                      // true
viaHeatCapacity into mega.joulesPerCubicMeterKelvin                // 4.184
```

## 用该组进行计算

| 表达式                                            | 结果类型                                | 含义                    |
|----------------------------------------------------|------------------------------------------|-------------------------|
| `heatCapacity / volume`                            | `KVolumetricHeatCapacityUnitInstance`     | `c_v = C / V`           |
| `specificHeatCapacity * density`                   | `KVolumetricHeatCapacityUnitInstance`     | `c_v = c · ρ`           |
| `volumetricHeatCapacity * volume`                  | `KHeatCapacityUnitInstance`               | `C = c_v · V`           |
| `heatCapacity / volumetricHeatCapacity`            | `KVolumeUnitInstance`                     | 对应的体积              |
| `volumetricHeatCapacity / density`                 | `KSpecificHeatCapacityUnitInstance`       | 回到 `c`                |
| `volumetricHeatCapacity / specificHeatCapacity`    | `KDensityUnitInstance`                    | 回到 `ρ`                |

## 现实示例：储水箱的热质量

一个 **300 升** 的储水箱：将其升高 1 K 需要多少能量？与同体积的混凝土
（≈ 2.0 MJ/(m³·K)）相比又如何？

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.volumetricheatcapacity.*

val water = 4.184 of mega.joulesPerCubicMeterKelvin
val tank = water * (300 of liters)          // KHeatCapacityUnitInstance
tank into kilo.joulesPerKelvin              // ≈ 1255.2 kJ/K

val concrete = 2.0 of mega.joulesPerCubicMeterKelvin
(water into mega.joulesPerCubicMeterKelvin) /
    (concrete into mega.joulesPerCubicMeterKelvin)   // ≈ 2.09 倍的热质量
```

## 值语义

`equals`/`hashCode` 比较**规范化后的 J/(m³·K) 值**，因此
`(1 of caloriesPerCubicCentimeterKelvin) == (4.184e6 of joulesPerCubicMeterKelvin)`。`toString()` 会以
基本单位呈现该值：`"4184000.0 J/(m^3*K)"`。

## 另请参阅

* [热容](heat-capacity.zh.md) —— 未经规范化的量。
* [比热容](specific-heat-capacity.zh.md) —— 相同的概念，但以**质量**而非体积为基准。
* [热力学概述](overview.zh.md)
