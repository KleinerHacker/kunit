# 比容

包: `org.pcsoft.framework.kunit.mechanic.specificvolume`
基本单位: **立方米每千克**
(`KSpecificVolumeUnit.BASE == KSpecificVolumeUnit.CUBIC_METERS_PER_KILOGRAM`)

类型： **构造单位**

比容 `v` 是单位质量所占据的体积——[密度](density.md)的 **倒数**。它是一个 **构造**单位—— 组合 `length³ · mass⁻¹`(`m³/kg`)。

`KSpecificVolumeUnitInstance` 包装了一个恰好由两项组成的 `KMixedUnitInstance`，处于规范正规形式： 指数为 `+3` 的
`KDistanceUnit.BASE`(米)和指数为 `-1` 的 `KMassUnit.BASE`(克)。由于本库的质量分量 归一化为克，存储值为原始的以克为基准的分量值，以
m³/kg 读取时通过固定因子桥接。

## 命名单位

| 单位         | 符号      |                      令牌 | 1 单位换算为 m³/kg |
|--------------|-----------|--------------------------:|-------------------:|
| 立方米每千克 | `m^3/kg`  |  `cubicMetersPerKilogram` |                1.0 |
| 升每千克     | `l/kg`    |       `litersPerKilogram` |               1e-3 |
| 立方厘米每克 | `cm^3/g`  | `cubicCentimetersPerGram` |               1e-3 |
| 立方英尺每磅 | `ft^3/lb` |       `cubicFeetPerPound` |        ≈ 0.0624280 |

所有单位均支持完整的 SI 前缀范围 (`milli.cubicMetersPerKilogram`)。

## 使用核心单位进行计算

| 表达式                                           | 结果类型                      | 含义        |
|--------------------------------------------------|-------------------------------|-------------|
| `volume / mass`                                  | `KSpecificVolumeUnitInstance` | `v = V / m` |
| `specificvolume * mass`、`mass * specificvolume` | `KVolumeUnitInstance`         | `V = v · m` |
| `volume / specificvolume`                        | `KMassUnitInstance`           | `m = V / v` |
| `1 / density`                                    | `KSpecificVolumeUnitInstance` | `v = 1 / ρ` |
| `1 / specificvolume`                             | `KDensityUnitInstance`        | `ρ = 1 / v` |

倒数运算符是类型化的：`1 / density` 保留真实的单位类型，而不会退化为通用的混合单位。 原生形式通过 `toSpecificVolume()` 转换。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mechanic.specificvolume.*

val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

val viaQuotient = (2 of liters) / (1 of kilo.grams)
val viaReciprocal = 1 / water

viaQuotient into litersPerKilogram   // 2.0
viaReciprocal into litersPerKilogram // 1.0
(1 / viaReciprocal).value == water.value // true —— 精确往返
```

## 现实示例:蒸汽表查询

1 bar 下的饱和蒸汽比容约为 1.694 m³/kg。2 kg 该蒸汽占据的体积是多少？它的密度是多少？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.specificvolume.*
import org.pcsoft.framework.kunit.pow

val v = 1.694 of cubicMetersPerKilogram
val volume = v * (2 of kilo.grams)   // KVolumeUnitInstance
volume into liters                   // 3388.0

val rho = 1 / v                      // KDensityUnitInstance
rho into (kilo.grams / (meters pow 3)) // ≈ 0.5903
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.specificvolume.*

val sum = (10 of litersPerKilogram) + (4 of litersPerKilogram) // 14 l/kg
(1 of cubicMetersPerKilogram) > (1 of litersPerKilogram)       // true
(1 of litersPerKilogram) == (1 of cubicCentimetersPerGram)     // true(相同的值)
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.specificvolume.*

(2 of cubicMetersPerKilogram).toString()                      // "2.0 m^3/kg"(基本单位)
"${(2 of cubicMetersPerKilogram) into litersPerKilogram} l/kg" // "2000.0 l/kg"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/`
表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学        | Kotlin                                 | 含义                     |
|-------------|----------------------------------------|--------------------------|
| `m³/kg`     | `cubicMetersPerKilogram`               | 比容，基本单位(命名令牌) |
| `m³·kg⁻¹`   | `(meters pow 3) * (kilo.grams pow -1)` | 同一量写成纯乘积         |
| `l/kg`      | `litersPerKilogram`                    | 升每千克读法             |
| `v = V / m` | `volume / mass`                        | 类型化分解方式           |
| `v = 1 / ρ` | `1 / density`                          | 密度的倒数               |
| `ρ = 1 / v` | `1 / specificvolume`                   | 返回密度                 |
