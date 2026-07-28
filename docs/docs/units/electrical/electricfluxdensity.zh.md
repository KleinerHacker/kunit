# 电通量密度

包：`org.pcsoft.framework.kunit.electricfluxdensity`
基本单位：**库仑每平方米**
（`KElectricFluxDensityUnit.BASE == KElectricFluxDensityUnit.COULOMB_PER_SQUARE_METER`）

类型：**构造单位**

电通量密度是一个**构造**单位：其组成为 `电流 · 时间 · 长度⁻²`
（`A·s·m⁻²` = `C/m²`）。`KElectricFluxDensityUnitInstance` 包装了一个由三项组成的 `KMixedUnitInstance` ——
`KElectricCurrentUnit.BASE`（安培）指数为 `+1`，`KTimeUnit.BASE`（秒）指数为 `+1`，以及 `KDistanceUnit.BASE`
（米）指数为 `-2`。该组不包含质量维度，因此不需要克/千克的桥接；存储值始终归一化为库仑每平方米。

通量密度 `D`（也称电位移）是单位面积上的电荷。**表面电荷密度** `σ` 在维度上与该量相同，因此由该组表示，
而不是由单独的组表示。`D` 通过[介电常数](permittivity.md)与[电场强度](electricfieldstrength.md)
相关联（`D = ε · E`）。一维对应量是[线电荷密度](linearchargedensity.md)，三维对应量是
[电荷密度](chargedensity.md)。

## 构建电通量密度

可以用一个命名令牌构建电通量密度，也可以通过分解构建（见下文）。命名单位以值为 1 的
令牌形式存在（配合 `of`/`into` 使用）：

| 通量密度 | 符号 | 令牌 | 1 单位相当于多少 C/m² |
|---|---|---:|---:|
| 库仑每平方米 | `C/m²` | `coulombsPerSquareMeter` | 1.0 |
| 库仑每平方厘米 | `C/cm²` | `coulombsPerSquareCentimeter` | 1.0e4 |

命名单位通过 `KPrefixBuilder` 支持 SI 前缀（`micro.coulombsPerSquareMeter`、
`milli.coulombsPerSquareMeter` 等）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electricfluxdensity.*

val d = 5 of micro.coulombsPerSquareMeter   // 带电的电容器极板
d into micro.coulombsPerSquareMeter         // 5.0
d into coulombsPerSquareMeter               // 5.0e-6
(1 of coulombsPerSquareCentimeter) into coulombsPerSquareMeter // 10000.0
```

## 多种分解方式

电通量密度可以通过多种**等价的分解方式**得到，所有方式产生的结果在值上都相等：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `charge / area` | `KElectricFluxDensityUnitInstance` | `D = Q / A`，电荷分布在一个面积上 |
| `permittivity * electricFieldStrength` | `KElectricFluxDensityUnitInstance` | `D = ε · E`（可交换，见[介电常数](permittivity.md)） |
| `current·time/length²` | 通过 `.toElectricFluxDensity()` | 原生规范形式的 `A·s·m⁻²` 表达式 |

带类型的操作符形式直接返回通量密度。完全原生的表达式仍是一个通用的
`KMixedUnitInstance`，需通过 `toElectricFluxDensity()` 收窄为具体类型（该方法仅识别规范正规形式，
否则会抛出 `IllegalStateException`）。所有路径在值上都相等。

反向操作符将电荷、面积与通量密度联系在一起：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `electricFluxDensity * area` | `KChargeUnitInstance` | `Q = D · A`（可交换） |
| `charge / electricFluxDensity` | `KAreaUnitInstance` | `A = Q / D` |
| `electricFluxDensity / electricFieldStrength` | `KPermittivityUnitInstance` | `ε = D / E` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.distance.ares
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.electricfluxdensity.*

// 现实示例 - 20 µC 分布在 4 m² 的电容器极板上，得到 5 µC/m²。
val plate: KAreaUnitInstance = 0.04 of ares            // 4 m²
val d = (20 of micro.coulombs) / plate                 // 5e-6 C/m²

// 以原生的 A·s·m⁻² 表达式表示的相同通量密度：
val raw = 5e-6 of ((amperes pow 1) * (seconds pow 1)) / (meters pow 2)
raw.toElectricFluxDensity() == d                       // true
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricfluxdensity.*

val s = (1 of coulombsPerSquareMeter) + (1 of coulombsPerSquareCentimeter)  // 10001 C/m²
(1 of coulombsPerSquareCentimeter) > (1 of coulombsPerSquareMeter)          // true
(2 of coulombsPerSquareMeter) * (3 of coulombsPerSquareMeter)               // KMixedUnitInstance
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricfluxdensity.*

(1 of coulombsPerSquareCentimeter).toString()   // "10000.0 C/m²"（基本单位）
```

## 符号表示

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`⁻²`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `C/m²` | `coulombsPerSquareMeter` | 电通量密度，基本单位（命名令牌） |
| `Q / A` | `(20 of micro.coulombs) / plate` | 由电荷分布在面积上得出的通量密度 |
| `ε · E` | `(1 of vacuumPermittivity) * (1 of voltsPerMeter)` | 由介电常数和场强得出的通量密度 |
| `A·s/m²` | `((amperes pow 1) * (seconds pow 1)) / (meters pow 2)` | 通量密度作为电流·时间 / 长度²（分数形式） |
| `A·s·m⁻²` | `(amperes pow 1) * (seconds pow 1) * (meters pow -2)` | 相同通量密度作为纯乘积形式 |
| `µC/m²` | `micro.coulombsPerSquareMeter` | 带前缀的通量密度（微库仑每平方米） |
