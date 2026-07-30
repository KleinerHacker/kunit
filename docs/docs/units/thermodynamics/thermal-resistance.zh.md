# 热阻（R 值）

包：`org.pcsoft.framework.kunit.thermo.resistance`
基本单位：**平方米-开尔文每瓦特**（`KThermalResistanceUnit.BASE == KThermalResistanceUnit.SQUARE_METER_KELVIN_PER_WATT`）

类型：**构造单位**

热阻 —— **R 值** —— 是一层材料抵抗热流的强弱：`m²·K/W`。它恰好是
[传热系数](heat-transfer-coefficient.md)（U 值）的倒数，也是保温产品实际
出售时所用的形式，因为串联层的 R 值可以简单地**相加**。

`KThermalResistanceUnitInstance` 包装了一个恰好由三项组成的
`KMixedUnitInstance`，处于规范正规形式 `mass⁻¹ · time³ · temperature¹`
（`kg⁻¹·s³·K`），始终以 m²·K/W 归一化。

!!! note "包名与类名的区别"
    包名是 `thermo.resistance`，而非 `thermo.thermalresistance` —— 单位包
    不得重复其所属领域包的名称。**类型**保留完整的技术术语
    （`KThermalResistanceUnitInstance`），这将它与 `electric.resistance`
    区分开来。

## 命名单位

| 单位 | 符号 | 令牌 | 1 单位相当于多少 m²·K/W |
|---|---|---:|---:|
| 平方米-开尔文每瓦特（RSI） | `m²·K/W` | `squareMeterKelvinPerWatt` | 1.0 |
| 英制 R 值 | `h·ft²·°F/Btu` | `hourSquareFootFahrenheitPerBtu` | ≈ 0.176110 |
| 克罗（clo） | `clo` | `clo` | 0.155 |
| 托格（tog） | `tog` | `tog` | 0.1 |

美国的"R-30"保温层是 `30 of hourSquareFootFahrenheitPerBtu` ≈ 5.28 m²·K/W。
一套西装大约是 1 clo；羽绒被以托格计。所有单位均支持完整的 SI 前缀范围。

## 现实示例：分层的保温墙体

一堵墙由 20 cm 矿棉（λ = 0.04 W/(m·K)）和 12 cm 砖块（λ = 0.8 W/(m·K)）组成。
总 R 值、由此得到的 U 值以及在 ΔT = 25 K 下的热损失分别是多少？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.wattsPerSquareMeterKelvin
import org.pcsoft.framework.kunit.thermo.resistance.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val wool  = (20 of centi.meters) / (0.04 of wattsPerMeterKelvin)  // 5.0 m²·K/W
val brick = (12 of centi.meters) / (0.8 of wattsPerMeterKelvin)   // 0.15 m²·K/W

val total = wool + brick                    // 各层串联，直接相加
total into squareMeterKelvinPerWatt         // 5.15 m²·K/W
total into hourSquareFootFahrenheitPerBtu   // ≈ 29.2（一堵"R-29"墙）

val u = 1 / total                           // KHeatTransferCoefficientUnitInstance
u into wattsPerSquareMeterKelvin            // ≈ 0.194 W/(m²·K)

val drop = KTemperatureDifference.ofKelvin(25)
val flux = drop / total                     // KHeatFluxDensityUnitInstance
flux into wattsPerSquareMeter               // ≈ 4.85 W/m²

val wall = (10 of meters) * (2.5 of meters) // 25 m²
(flux * wall) into watts                    // ≈ 121 W
```

## 用相邻单位计算

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `temperatureDifference / heatFluxDensity` | `KThermalResistanceUnitInstance` | 由测量得到 R 值 |
| `length / thermalConductivity` | `KThermalResistanceUnitInstance` | 由材料 + 厚度得到 R 值 |
| `thermalResistance * heatFluxDensity` | `KTemperatureDifferenceUnitInstance` | 维持的温差 |
| `heatFluxDensity * thermalResistance` | `KTemperatureDifferenceUnitInstance` | 相同（可交换） |
| `temperatureDifference / thermalResistance` | `KHeatFluxDensityUnitInstance` | 得到的热流密度 |
| `thermalResistance * thermalConductivity` | `KLengthUnitInstance` | 所需厚度 |
| `thermalConductivity * thermalResistance` | `KLengthUnitInstance` | 相同（可交换） |
| `length / thermalResistance` | `KThermalConductivityUnitInstance` | 隐含的导热率 |
| `1 / heatTransferCoefficient` | `KThermalResistanceUnitInstance` | 由 U 得到 R |
| `1 / thermalResistance` | `KHeatTransferCoefficientUnitInstance` | 由 R 得到 U |

这两个倒数操作符被窄化声明，因此 `1 / u` 与 `1 / r` 返回的是**类型化**值，
而非通用的 `Number.div` 与分组无关时会产生的混合单位。

## 分解方式

全部三种分解方式都产生相同的类型化、值相等的实例。

| 分解方式 | 形式 | 结果 |
|---|---|---|
| `temperatureDifference / heatFluxDensity` | 类型化操作符 | `KThermalResistanceUnitInstance` |
| `length / thermalConductivity` | 类型化操作符 | `KThermalResistanceUnitInstance` |
| `mass⁻¹ · time³ · temperature¹` | 原生 + `toThermalResistance()` | `KThermalResistanceUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.resistance.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaFlux      = KTemperatureDifference.ofKelvin(1) / (1 of wattsPerSquareMeter)
val viaThickness = (1 of meters) / (1 of wattsPerMeterKelvin)
val native = (
    ((1 of seconds).toUnit() pow 3) *
        KTemperatureDifference.ofKelvin(1).toUnit() /
        (1000 of grams).toUnit()
    ).toThermalResistance()

viaFlux == viaThickness // true
viaFlux == native       // true —— 全部都是 1.0 m²·K/W
```

## 操作符

`+` 与 `-` 在这里恰好对应着物理上有意义的运算：串联层的 R 值相加。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.resistance.*

val series = (5 of squareMeterKelvinPerWatt) + (0.15 of squareMeterKelvinPerWatt) // 5.15
(1 of squareMeterKelvinPerWatt) > (5 of tog)      // true（5 tog = 0.5 m²·K/W）
(1 of squareMeterKelvinPerWatt) == (10 of tog)    // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.resistance.*

(5 of squareMeterKelvinPerWatt).toString()                                        // "5.0 m²·K/W"
"R-${(5 of squareMeterKelvinPerWatt) into hourSquareFootFahrenheitPerBtu}"        // "R-28.39..."
```

## 记法

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `m²·K/W` | `squareMeterKelvinPerWatt` | 热阻（R 值），基本单位 |
| `kg⁻¹·s³·K` | `(seconds pow 3) * ΔK / grams` | 相同的量以基础维度表示 |
| `h·ft²·°F/Btu` | `hourSquareFootFahrenheitPerBtu` | 英制 R 值 |
| `R = d / λ` | `(20 of centi.meters) / (0.04 of wattsPerMeterKelvin)` | 由厚度 ÷ 导热率得到 R |
| `R = ΔT / q̇` | `drop / (4 of wattsPerSquareMeter)` | 由温差 ÷ 热流密度得到 R |
| `R_total = R₁ + R₂` | `wool + brick` | 串联层 |
| `U = 1 / R` | `1 / total` | 由 R 值得到 U 值 |
| `q̇ = ΔT / R` | `drop / total` | 由温差 ÷ R 得到热流密度 |
