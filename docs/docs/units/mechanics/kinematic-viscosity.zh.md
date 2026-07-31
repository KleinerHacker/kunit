# 运动粘度

包: `org.pcsoft.framework.kunit.common.diffusivity`
基本单位: **平方米每秒**(`KDiffusivityUnit.BASE == KDiffusivityUnit.SQUARE_METER_PER_SECOND`)

类型： **构造单位**

运动粘度 `ν = η / ρ` 是[动力粘度](viscosity.md)除以[密度](density.md)——它是支配流体中动量扩散方式的量。 其维度是
`length² · time⁻¹`(`m²/s`)。

这恰好是 **扩散率**组的维度和物理量，该组与热力学中的[热扩散率](../thermodynamics/thermal-diffusivity.md)共享。 因此 KUnit
并 **不**为其引入第二个组：运动粘度是 `KDiffusivityUnitInstance` 的一种 **读法**， 这也是为什么该组位于 `common`
中。本页描述其力学读法。

!!! note "一个组，两个学科领域"
`KDiffusivityUnit` 同时携带两套词汇：两个领域共享的公制读法 (m²/s、mm²/s)，以及传统的 运动粘度写法斯托克斯和厘斯托克斯。

## 命名单位

| 单位           | 符号    |                         令牌 | 1 单位等于多少 m²/s |
|----------------|---------|-----------------------------:|--------------------:|
| 平方米每秒     | `m²/s`  |      `squareMetersPerSecond` |                 1.0 |
| 平方毫米每秒   | `mm²/s` | `squareMillimetersPerSecond` |                1e-6 |
| 斯托克斯       | `St`    |                     `stokes` |                1e-4 |
| 厘斯托克斯     | `cSt`   |                `centistokes` |                1e-6 |
| 平方英尺每小时 | `ft²/h` |          `squareFeetPerHour` |        ≈ 2.58064e-5 |

`1 cSt = 1 mm²/s` 精确成立——20 °C 的水约为 1 cSt。所有单位都支持完整的 SI 前缀范围， 因此 `centi.stokes` 是厘斯托克斯的另一种写法。

## 分解方式

| 形式            | Kotlin                                                      | 结果类型                   |
|-----------------|-------------------------------------------------------------|----------------------------|
| 动力粘度 / 密度 | `viscosity / density`                                       | `KDiffusivityUnitInstance` |
| 原生表达式      | `((length.toUnit() pow 2) / time.toUnit()).toDiffusivity()` | `KDiffusivityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.viscosity.pascalSeconds

val water = (1000 of kilo.grams) / (1 of (meters pow 3))
val typed = (1 of milli.pascalSeconds) / water
val native = (((1 of milli.meters).toUnit() pow 2) / (1 of seconds).toUnit()).toDiffusivity()

typed == native          // true - 两者都是 1e-6 m²/s
typed into centistokes   // 1.0
```

## 使用核心单位计算

| 表达式                                           | 结果类型                   | 含义        |
|--------------------------------------------------|----------------------------|-------------|
| `viscosity / density`                            | `KDiffusivityUnitInstance` | `ν = η / ρ` |
| `diffusivity * density`、`density * diffusivity` | `KViscosityUnitInstance`   | `η = ν · ρ` |
| `viscosity / diffusivity`                        | `KDensityUnitInstance`     | `ρ = η / ν` |

## 现实示例：液压油的选择

一种液压油规格为 ISO VG 46，即 40 °C 下 46 cSt，密度为 870 kg/m³。这对应多大的动力粘度？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.viscosity.pascalSeconds

val nu = 46 of centistokes
nu into squareMillimetersPerSecond // 46.0

val rho = (870 of kilo.grams) / (1 of (meters pow 3))
val eta = nu * rho                 // KViscosityUnitInstance
eta into pascalSeconds             // ≈ 0.04002
eta into centi.poises              // ≈ 40.02
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.diffusivity.*

val sum = (10 of centistokes) + (4 of centistokes) // 14 cSt
(1 of stokes) > (10 of centistokes)                // true
(1 of centistokes) == (1 of squareMillimetersPerSecond) // true(值相同)
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.diffusivity.*

(46 of centistokes).toString()                  // "4.6E-5 m²/s"(基本单位)
"${(46 of centistokes) into centistokes} cSt"   // "46.0 cSt"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/`
表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学        | Kotlin                     | 含义                  |
|-------------|----------------------------|-----------------------|
| `m²/s`      | `squareMetersPerSecond`    | 运动粘度，基本单位    |
| `m²·s⁻¹`    | `(meters pow 2) / seconds` | 同一量以基础维度表示  |
| `cSt`       | `centistokes`              | 厘斯托克斯(= 1 mm²/s) |
| `ν = η / ρ` | `viscosity / density`      | 类型化分解            |
| `η = ν · ρ` | `diffusivity * density`    | 求解动力粘度          |
| `ρ = η / ν` | `viscosity / diffusivity`  | 求解密度              |
