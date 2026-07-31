# 热流密度

包：`org.pcsoft.framework.kunit.thermo.heatfluxdensity`
基本单位： **瓦特每平方米**（`KHeatFluxDensityUnit.BASE == KHeatFluxDensityUnit.WATT_PER_SQUARE_METER`）

类型： **构造单位**

热流密度是单位面积上的热流：`power / area`（`W/m²`）。同一个单位也用于度量 *辐照度*和 *辐射出射度* —— 即照射到或离开某表面的辐射强度。

`KHeatFluxDensityUnitInstance` 包装了一个恰好由两项组成的 `KMixedUnitInstance`， 处于规范正规形式 `mass¹ · time⁻³`（
`kg·s⁻³`），始终以 W/m² 归一化。

!!! note "长度维度会相互抵消"
`W/m² = kg·m²·s⁻³/m² = kg·s⁻³`。因此规范正规形式 **不**携带长度项。

总热流本身就是一个简单的[功率](power.md)；参见[热流](heat-flow.md)。
除以温度差后，它就变成[传热系数](heat-transfer-coefficient.md)。

## 命名单位

| 单位                    | 符号          |                                令牌 | 1 单位相当于多少 W/m² |
|-------------------------|---------------|------------------------------------:|----------------------:|
| 瓦特每平方米            | `W/m²`        |               `wattsPerSquareMeter` |                   1.0 |
| 英热单位每小时-平方英尺 | `Btu/(h·ft²)` |             `btusPerHourSquareFoot` |             ≈ 3.15459 |
| 卡路里每秒-平方厘米     | `cal/(s·cm²)` | `caloriesPerSecondSquareCentimeter` |               41840.0 |

以上单位均支持完整的 SI 前缀范围（`kilo.wattsPerSquareMeter`、
`milli.wattsPerSquareMeter` 等）。

## 太阳常数

该组以 `SOLAR_CONSTANT`（1361 W/m²）的形式暴露平均地外太阳辐照度，是一个纯 `Double`。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val sun = SOLAR_CONSTANT of wattsPerSquareMeter
sun into wattsPerSquareMeter // 1361.0
```

## 现实示例：太阳能阵列的选型

某屋顶在晴天接收 800 W/m² 的辐照度。阵列覆盖 25 m²，将 20% 的入射辐射转化为电能。 它能输出多少电功率？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val irradiance = 800 of wattsPerSquareMeter
val roof = (5 of meters) * (5 of meters)   // 25 m²

val incident = irradiance * roof           // KPowerUnitInstance
incident into kilo.watts                   // 20.0 kW

val electrical = incident * 0.2            // 标量缩放保持类型不变
electrical into kilo.watts                 // 4.0 kW

// 反过来：20% 效率下要输出 10 kW 电功率需要多大屋顶面积？
val needed = (50 of kilo.watts) / irradiance // KAreaUnitInstance
needed into ((1 of meters) * (1 of meters))  // 62.5 m²
```

## 用核心单位（功率与面积）计算

| 表达式                    | 结果类型                       | 含义             |
|---------------------------|--------------------------------|------------------|
| `power / area`            | `KHeatFluxDensityUnitInstance` | 热流密度         |
| `heatFluxDensity * area`  | `KPowerUnitInstance`           | 总热流           |
| `area * heatFluxDensity`  | `KPowerUnitInstance`           | 总热流（可交换） |
| `power / heatFluxDensity` | `KAreaUnitInstance`            | 热流所铺展的面积 |

## 分解方式

两种分解方式都产生相同的类型化、值相等的实例。

| 分解方式        | 形式                               | 结果                                    |
|-----------------|------------------------------------|-----------------------------------------|
| `power / area`  | 类型化操作符                       | 直接得到 `KHeatFluxDensityUnitInstance` |
| `mass · time⁻³` | 原生表达式 + `toHeatFluxDensity()` | `KHeatFluxDensityUnitInstance`          |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val typed  = (1 of watts) / ((1 of meters) * (1 of meters))
val native = ((1000 of grams).toUnit() / ((1 of seconds).toUnit() pow 3)).toHeatFluxDensity()

typed == native // true —— 两者都是 1.0 W/m²
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val total = (1 of kilo.wattsPerSquareMeter) + (500 of wattsPerSquareMeter)  // 1500 W/m²
(1 of kilo.wattsPerSquareMeter) > (500 of wattsPerSquareMeter)              // true
(1 of kilo.wattsPerSquareMeter) == (1000 of wattsPerSquareMeter)            // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

(1361 of wattsPerSquareMeter).toString()                                 // "1361.0 W/m²"
"${(1361 of wattsPerSquareMeter) into btusPerHourSquareFoot} Btu/(h·ft²)" // "431.4..."
```

## 记法

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·`
表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示    | Kotlin                                  | 含义                           |
|-------------|-----------------------------------------|--------------------------------|
| `W/m²`      | `wattsPerSquareMeter`                   | 热流密度，基本单位 —— 命名令牌 |
| `kg·s⁻³`    | `grams / (seconds pow 3)`               | 相同的量以基础维度表示         |
| `kW/m²`     | `kilo.wattsPerSquareMeter`              | 千瓦每平方米                   |
| `E_0`       | `SOLAR_CONSTANT of wattsPerSquareMeter` | 太阳常数，1361 W/m²            |
| `q̇ = P / A` | `(1000 of watts) / roof`                | 由功率 ÷ 面积得到热流密度      |
| `P = q̇ · A` | `irradiance * roof`                     | 由热流密度 × 面积得到功率      |
| `A = P / q̇` | `(50 of kilo.watts) / irradiance`       | 由功率 ÷ 热流密度得到面积      |
