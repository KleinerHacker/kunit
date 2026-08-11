# 传热系数

包：`org.pcsoft.framework.kunit.thermo.heattransfercoefficient`
基本单位： **瓦特每平方米-开尔文**（
`KHeatTransferCoefficientUnit.BASE == KHeatTransferCoefficientUnit.WATT_PER_SQUARE_METER_KELVIN`）

类型： **构造单位**

传热系数 —— 在建筑物理学中称为 **U 值** —— 是构件在每开尔文温差下所传递的 热流密度：`W/(m²·K)`。U 值越低，保温性能越好。

`KHeatTransferCoefficientUnitInstance` 包装了一个恰好由三项组成的
`KMixedUnitInstance`，处于规范正规形式 `mass¹ · time⁻³ · temperature⁻¹`
（`kg·s⁻³·K⁻¹`），始终以 W/ (m²·K) 归一化。与[热流密度](heat-flux-density.md)
一样，面积抵消了瓦特中的长度维度，因此正规形式不携带长度项。

它的倒数是[热阻](thermal-insulance.md)（R 值）；乘以厚度就变成
[热导率](thermal-conductivity.md)。

## 命名单位

| 单位                           | 符号             |                                      令牌 | 1 单位相当于多少 W/(m²·K) |
|--------------------------------|------------------|------------------------------------------:|--------------------------:|
| 瓦特每平方米-开尔文            | `W/(m²·K)`       |               `wattsPerSquareMeterKelvin` |                       1.0 |
| 英热单位每小时-平方英尺-华氏度 | `Btu/(h·ft²·°F)` |         `btusPerHourSquareFootFahrenheit` |                ≈ 5.678263 |
| 卡路里每秒-平方厘米-开尔文     | `cal/(s·cm²·K)`  | `caloriesPerSecondSquareCentimeterKelvin` |                   41840.0 |

以上单位均支持完整的 SI 前缀范围（`milli.wattsPerSquareMeterKelvin` 等）。

## 典型 U 值

| 构件       |                    U |
|------------|---------------------:|
| 单层玻璃   |       ≈ 5.8 W/(m²·K) |
| 双层玻璃   |       ≈ 2.8 W/(m²·K) |
| 三层玻璃   | ≈ 0.7 … 1.3 W/(m²·K) |
| 被动房墙体 |      ≈ 0.15 W/(m²·K) |

## 现实示例：通过窗户的热损失

一扇 2.4 m² 的三层玻璃窗，U = 1.3 W/ (m²·K)。室内 21 °C，室外 1 °C。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*
import org.pcsoft.framework.kunit.thermo.temperature.celsius

val window = 1.3 of wattsPerSquareMeterKelvin
val drop = (21 of celsius) - (1 of celsius)      // 20 K
val glass = (2 of meters) * (1.2 of meters)      // 2.4 m²

val flux = window * drop                          // KHeatFluxDensityUnitInstance
flux into wattsPerSquareMeter                     // 26.0 W/m²

val loss = flux * glass                           // KPowerUnitInstance
loss into watts                                   // 62.4 W

// 单层玻璃会有多大代价？
val single = 5.8 of wattsPerSquareMeterKelvin
((single * drop) * glass) into watts              // 278.4 W —— 是原来的 4.5 倍
```

## 用相邻单位计算

| 表达式                                            | 结果类型                               | 含义                   |
|---------------------------------------------------|----------------------------------------|------------------------|
| `heatFluxDensity / temperatureDifference`         | `KHeatTransferCoefficientUnitInstance` | 由测量得到 U 值        |
| `thermalConductivity / length`                    | `KHeatTransferCoefficientUnitInstance` | 由材料 + 厚度得到 U 值 |
| `heatTransferCoefficient * temperatureDifference` | `KHeatFluxDensityUnitInstance`         | 通过构件的热流密度     |
| `temperatureDifference * heatTransferCoefficient` | `KHeatFluxDensityUnitInstance`         | 相同（可交换）         |
| `heatFluxDensity / heatTransferCoefficient`       | `KTemperatureDifferenceUnitInstance`   | 驱动温差               |
| `heatTransferCoefficient * length`                | `KThermalConductivityUnitInstance`     | 材料导热率             |
| `length * heatTransferCoefficient`                | `KThermalConductivityUnitInstance`     | 相同（可交换）         |
| `thermalConductivity / heatTransferCoefficient`   | `KLengthUnitInstance`                  | 所需厚度               |

## 分解方式

全部三种分解方式都产生相同的类型化、值相等的实例。

| 分解方式                                  | 形式                                 | 结果                                   |
|-------------------------------------------|--------------------------------------|----------------------------------------|
| `heatFluxDensity / temperatureDifference` | 类型化操作符                         | `KHeatTransferCoefficientUnitInstance` |
| `thermalConductivity / length`            | 类型化操作符                         | `KHeatTransferCoefficientUnitInstance` |
| `mass · time⁻³ · temperature⁻¹`           | 原生 + `toHeatTransferCoefficient()` | `KHeatTransferCoefficientUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaFlux         = (1 of wattsPerSquareMeter) / KTemperatureDifference.ofKelvin(1)
val viaConductivity = (1 of wattsPerMeterKelvin) / (1 of meters)
val native = (
    (1000 of grams).toUnit() /
        ((1 of seconds).toUnit() pow 3) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toHeatTransferCoefficient()

viaFlux == viaConductivity // true
viaFlux == native          // true —— 全部都是 1.0 W/(m²·K)
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*

val total = (1 of kilo.wattsPerSquareMeterKelvin) + (500 of wattsPerSquareMeterKelvin)  // 1500
(1 of kilo.wattsPerSquareMeterKelvin) > (500 of wattsPerSquareMeterKelvin)              // true
(1 of kilo.wattsPerSquareMeterKelvin) == (1000 of wattsPerSquareMeterKelvin)            // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*

(1.3 of wattsPerSquareMeterKelvin).toString()                                             // "1.3 W/(m²·K)"
"${(1.3 of wattsPerSquareMeterKelvin) into btusPerHourSquareFootFahrenheit} Btu/(h·ft²·°F)" // "0.229..."
```

## 记法

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·`
表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示         | Kotlin                                            | 含义                       |
|------------------|---------------------------------------------------|----------------------------|
| `W/(m²·K)`       | `wattsPerSquareMeterKelvin`                       | 传热系数（U 值），基本单位 |
| `kg·s⁻³·K⁻¹`     | `grams / (seconds pow 3) / ΔK`                    | 相同的量以基础维度表示     |
| `U = q̇ / ΔT`     | `(26 of wattsPerSquareMeter) / drop`              | 由热流密度 ÷ 温差得到 U 值 |
| `U = λ / d`      | `(0.04 of wattsPerMeterKelvin) / (0.2 of meters)` | 由导热率 ÷ 厚度得到 U 值   |
| `q̇ = U · ΔT`     | `window * drop`                                   | 由 U 值 × 温差得到热流密度 |
| `P = U · A · ΔT` | `(window * drop) * glass`                         | 总热损失                   |
