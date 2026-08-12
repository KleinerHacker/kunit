# 热导率

包：`org.pcsoft.framework.kunit.thermo.conductivity`
基本单位： **瓦特每米-开尔文**（`KThermalConductivityUnit.BASE == KThermalConductivityUnit.WATT_PER_METER_KELVIN`）

类型： **构造单位**

热导率 `λ`（也记作 `k`）是傅里叶定律中的材料属性：材料的
[热流密度](heat-flux-density.md)等于其导热率乘以
[温度梯度](temperature-gradient.md)。单位：`W/(m·K)`。

`KThermalConductivityUnitInstance` 包装了一个恰好由四项组成的
`KMixedUnitInstance`，处于规范正规形式 `mass¹ · distance¹ · time⁻³ · temperature⁻¹`
（`kg·m·s⁻³·K⁻¹`），始终以 W/ (m·K) 归一化。

!!! note "包名与类名的区别"
包名是 `thermo.conductivity`，而非 `thermo.thermalconductivity` —— 单位包不得重复其所属领域包的名称。 **类型**
保留完整的技术术语 （`KThermalConductivityUnitInstance`），这正是它与
`electric.conductivity` 的区别所在。

除以厚度即得到[传热系数](heat-transfer-coefficient.md)；厚度除以它就是
[热阻](thermal-insulance.md)（R 值）。

## 命名单位

| 单位                       | 符号            |                                令牌 | 1 单位相当于多少 W/(m·K) |
|----------------------------|-----------------|------------------------------------:|-------------------------:|
| 瓦特每米-开尔文            | `W/(m·K)`       |               `wattsPerMeterKelvin` |                      1.0 |
| 英热单位每小时-英尺-华氏度 | `Btu/(h·ft·°F)` |         `btusPerHourFootFahrenheit` |               ≈ 1.730735 |
| 卡路里每秒-厘米-开尔文     | `cal/(s·cm·K)`  | `caloriesPerSecondCentimeterKelvin` |                    418.4 |

以上单位均支持完整的 SI 前缀范围 —— 保温材料通常写作
`40 of milli.wattsPerMeterKelvin`。

## 典型数值

| 材料 |                            λ |
|------|-----------------------------:|
| 铜   |                  401 W/(m·K) |
| 钢   |                 ≈ 50 W/(m·K) |
| 玻璃 |                  ≈ 1 W/(m·K) |
| 矿棉 | ≈ 0.04 W/(m·K) = 40 mW/(m·K) |

## 现实示例：通过保温墙体的热损失

一层 30 cm 厚的矿棉（λ = 0.04 W/ (m·K)）将 21 °C 的室内与 −5 °C 的室外空气隔开。 墙面 12 m²。热损失是多少？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.conductivity.*
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.temperature.celsius
import org.pcsoft.framework.kunit.thermo.temperaturegradient.kelvinPerMeter

val wool = 40 of milli.wattsPerMeterKelvin      // 0.04 W/(m·K)
val thickness = 30 of centi.meters
val drop = (21 of celsius) - (-5 of celsius)    // 26 K

val gradient = drop / thickness                 // KTemperatureGradientUnitInstance，≈ 86.7 K/m
gradient into kelvinPerMeter                    // 86.666...

val flux = wool * gradient                      // KHeatFluxDensityUnitInstance（傅里叶定律）
flux into wattsPerSquareMeter                   // ≈ 3.47 W/m²

val wall = (4 of meters) * (3 of meters)        // 12 m²
val loss = flux * wall                          // KPowerUnitInstance
loss into watts                                 // ≈ 41.6 W
```

## 用相邻单位计算

| 表达式                                      | 结果类型                           | 含义                 |
|---------------------------------------------|------------------------------------|----------------------|
| `heatFluxDensity / temperatureGradient`     | `KThermalConductivityUnitInstance` | 求解傅里叶定律中的 λ |
| `thermalConductivity * temperatureGradient` | `KHeatFluxDensityUnitInstance`     | 傅里叶定律           |
| `temperatureGradient * thermalConductivity` | `KHeatFluxDensityUnitInstance`     | 相同（可交换）       |
| `heatFluxDensity / thermalConductivity`     | `KTemperatureGradientUnitInstance` | 隐含的梯度           |

## 分解方式

两种分解方式都产生相同的类型化、值相等的实例。

| 分解方式                                   | 形式                             | 结果                               |
|--------------------------------------------|----------------------------------|------------------------------------|
| `heatFluxDensity / temperatureGradient`    | 类型化操作符                     | `KThermalConductivityUnitInstance` |
| `mass · distance · time⁻³ · temperature⁻¹` | 原生 + `toThermalConductivity()` | `KThermalConductivityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.*
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperaturegradient.kelvinPerMeter

val typed = (1 of wattsPerSquareMeter) / (1 of kelvinPerMeter)
val native = (
    (1000 of grams).toUnit() *
        (1 of meters).toUnit() /
        ((1 of seconds).toUnit() pow 3) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toThermalConductivity()

typed == native // true —— 两者都是 1.0 W/(m·K)
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.conductivity.*

val total = (1 of kilo.wattsPerMeterKelvin) + (500 of wattsPerMeterKelvin)  // 1500 W/(m·K)
(1 of kilo.wattsPerMeterKelvin) > (500 of wattsPerMeterKelvin)              // true
(1 of kilo.wattsPerMeterKelvin) == (1000 of wattsPerMeterKelvin)            // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.conductivity.*

(401 of wattsPerMeterKelvin).toString()                                          // "401.0 W/(m·K)"
"${(401 of wattsPerMeterKelvin) into btusPerHourFootFahrenheit} Btu/(h·ft·°F)"   // "231.7..."
```

## 记法

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·`
表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示       | Kotlin                                   | 含义                        |
|----------------|------------------------------------------|-----------------------------|
| `W/(m·K)`      | `wattsPerMeterKelvin`                    | 热导率，基本单位            |
| `kg·m·s⁻³·K⁻¹` | `grams * meters / (seconds pow 3) / ΔK`  | 相同的量以基础维度表示      |
| `mW/(m·K)`     | `milli.wattsPerMeterKelvin`              | 毫瓦每米-开尔文（保温材料） |
| `q̇ = λ · ∇T`   | `wool * gradient`                        | 傅里叶定律                  |
| `λ = q̇ / ∇T`   | `(80 of wattsPerSquareMeter) / gradient` | 由热流密度 ÷ 梯度得到导热率 |
| `∇T = q̇ / λ`   | `flux / wool`                            | 由热流密度 ÷ 导热率得到梯度 |
