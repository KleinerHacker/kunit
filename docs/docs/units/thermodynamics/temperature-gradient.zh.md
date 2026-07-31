# 温度梯度

包：`org.pcsoft.framework.kunit.thermo.temperaturegradient`
基本单位： **开尔文每米**（`KTemperatureGradientUnit.BASE == KTemperatureGradientUnit.KELVIN_PER_METER`）

类型： **构造单位**

温度梯度是每单位长度的温度变化：`temperatureDifference / length`（`K/m`）。 它是传导现象的驱动量 ——
乘以[热导率](thermal-conductivity.md)即得到
[热流密度](heat-flux-density.md)。

`KTemperatureGradientUnitInstance` 包装了一个恰好由两项组成的
`KMixedUnitInstance`，处于规范正规形式 `temperature¹ · distance⁻¹`（`K·m⁻¹`）， 始终以 K/m 归一化。

!!! note "梯度是每单位长度的 *变化*"
温度维度是 **差**组（`KTemperatureDifferenceUnit`）。带偏移量的绝对标度 （°C、°F）在梯度中没有意义 —— 只有区间才有意义。这也是为什么
`°F/ft` 使用 华氏度的 *区间*换算系数 5/9 而非 −32 的偏移量进行转换。

## 命名单位

| 单位         | 符号    |                 令牌 | 1 单位相当于多少 K/m |
|--------------|---------|---------------------:|---------------------:|
| 开尔文每米   | `K/m`   |     `kelvinPerMeter` |                  1.0 |
| 开尔文每千米 | `K/km`  | `kelvinPerKilometer` |                0.001 |
| 华氏度每英尺 | `°F/ft` |  `fahrenheitPerFoot` |           ≈ 1.822689 |

以上单位均支持完整的 SI 前缀范围（`milli.kelvinPerMeter` 等）。

## 现实示例：地热梯度

地壳每千米深度大约升温 25 K。一口钻井深达 3.5 km。井底比地面热多少， 要达到 100 K 的温升需要钻多深？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val geothermal = 25 of kelvinPerKilometer
val borehole = 3.5 of kilo.meters

val rise = geothermal * borehole            // KTemperatureDifferenceUnitInstance
rise into KTemperatureDifference.ofKelvin(1) // 井底热了 87.5 K

val depthFor100K = KTemperatureDifference.ofKelvin(100) / geothermal // KLengthUnitInstance
depthFor100K into kilo.meters               // 4.0 km
depthFor100K into meters                    // 4000.0 m
```

## 用核心单位（温度差与长度）计算

| 表达式                                        | 结果类型                             | 含义           |
|-----------------------------------------------|--------------------------------------|----------------|
| `temperatureDifference / length`              | `KTemperatureGradientUnitInstance`   | 梯度           |
| `temperatureGradient * length`                | `KTemperatureDifferenceUnitInstance` | 该长度上的温升 |
| `length * temperatureGradient`                | `KTemperatureDifferenceUnitInstance` | 温升（可交换） |
| `temperatureDifference / temperatureGradient` | `KLengthUnitInstance`                | 跨越的长度     |

## 分解方式

两种分解方式都产生相同的类型化、值相等的实例。

| 分解方式                         | 形式                                   | 结果                               |
|----------------------------------|----------------------------------------|------------------------------------|
| `temperatureDifference / length` | 类型化操作符                           | `KTemperatureGradientUnitInstance` |
| `temperature · distance⁻¹`       | 原生表达式 + `toTemperatureGradient()` | `KTemperatureGradientUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val typed  = KTemperatureDifference.ofKelvin(1) / (1 of meters)
val native = (KTemperatureDifference.ofKelvin(1).toUnit() / (1 of meters).toUnit()).toTemperatureGradient()

typed == native // true —— 两者都是 1.0 K/m
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*

val total = (1 of kelvinPerMeter) + (500 of kelvinPerKilometer)  // 1.5 K/m
(1 of kelvinPerMeter) > (500 of kelvinPerKilometer)              // true
(1 of kelvinPerMeter) == (1000 of kelvinPerKilometer)            // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*

(25 of kelvinPerKilometer).toString()                        // "0.025 K/m"
"${(25 of kelvinPerKilometer) into kelvinPerKilometer} K/km" // "25.0 K/km"
```

## 记法

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·`
表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示      | Kotlin                                                     | 含义                     |
|---------------|------------------------------------------------------------|--------------------------|
| `K/m`         | `kelvinPerMeter`                                           | 温度梯度，基本单位       |
| `K·m⁻¹`       | `ΔK / meters`                                              | 相同的量以基础维度表示   |
| `K/km`        | `kelvinPerKilometer`                                       | 开尔文每千米（地热梯度） |
| `°F/ft`       | `fahrenheitPerFoot`                                        | 华氏度每英尺             |
| `∇T = ΔT / L` | `KTemperatureDifference.ofKelvin(25) / (1 of kilo.meters)` | 由温升 ÷ 长度得到梯度    |
| `ΔT = ∇T · L` | `geothermal * borehole`                                    | 由梯度 × 长度得到温升    |
| `L = ΔT / ∇T` | `KTemperatureDifference.ofKelvin(100) / geothermal`        | 由温升 ÷ 梯度得到长度    |
