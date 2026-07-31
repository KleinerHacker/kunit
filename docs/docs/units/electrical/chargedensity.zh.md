# 电荷密度

包: `org.pcsoft.framework.kunit.electric.chargedensity`
基本单位: **库仑每立方米**(`KChargeDensityUnit.BASE == KChargeDensityUnit.COULOMB_PER_CUBIC_METER`)

类型: **构成单位**

(体)电荷密度是一个 **构成**单位:组合 `current¹ · time¹ · length⁻³`(`A·s·m⁻³` = `C/m³`)。
`KChargeDensityUnitInstance` 包装了一个含三个项的 `KMixedUnitInstance` —— 指数 `+1` 的
`KElectricCurrentUnit.BASE`(安培)、指数 `+1` 的 `KTimeUnit.BASE`(秒)以及指数 `-3` 的
`KDistanceUnit.BASE`(米)。由于所有分量都以各自组的基本单位存储,所存值即为 C/m³ 的读数。

## 构建电荷密度

电荷密度 **没有裸令牌,也没有前缀构建器** —— 每种写法 (C/m³、mC/cm³ 等)都是比值。请以表达式或类型化的
`charge / volume` 运算符构建,并用 `into` 针对这样的表达式读回。前缀来自分量令牌 (`milli.coulombs`、
`centi.meters`):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

val rho = (6 of coulombs) / (2 of liters)  // KChargeDensityUnitInstance,3 C/L = 3000 C/m³
rho into (coulombs / (meters pow 3))       // 3000.0
rho into (coulombs / (centi.meters pow 3)) // 0.003(= 3 mC/cm³)
rho into (milli.coulombs / (meters pow 3)) // 3000000.0
```

## 多种分解

电荷密度可通过若干 **等价分解**得到,它们都产生数值相等的电荷密度:

| 表达式                 | 结果类型                     | 含义                          |
|------------------------|------------------------------|-------------------------------|
| `charge / volume`      | `KChargeDensityUnitInstance` | 定义 `ρ = Q / V`              |
| `current·time/length³` | 经由 `.toChargeDensity()`    | 原生规范形式 `A·s·m⁻³` 表达式 |

类型化运算符形式直接返回电荷密度。完全原生的表达式保持为通用的 `KMixedUnitInstance`,并通过
`toChargeDensity()` 收窄 (它只识别规范范式,否则抛出 `IllegalStateException`)。两条路径数值相等。

逆运算符将电荷、体积与电荷密度联系起来:

| 表达式                   | 结果类型              | 含义                |
|--------------------------|-----------------------|---------------------|
| `chargeDensity * volume` | `KChargeUnitInstance` | `Q = ρ · V`         |
| `volume * chargeDensity` | `KChargeUnitInstance` | `Q = V · ρ`(可交换) |
| `charge / chargeDensity` | `KVolumeUnitInstance` | `V = Q / ρ`         |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

// 实际示例 —— 电解液中的空间电荷:4 升电解液中溶解 12 mC 净电荷,电荷密度为 3 C/m³。
val rho = (0.012 of coulombs) / (4 of liters)   // KChargeDensityUnitInstance,3 C/m³

// 同一电荷密度的原生 A·s·m⁻³ 表达式:
val raw = (0.012 of coulombs).toUnit() / (0.004 of (meters pow 3))
raw.toChargeDensity() == rho                    // true

// 反算 4 升中所含的电荷,以及容纳 12 mC 的体积:
val q = rho * (4 of liters)                     // KChargeUnitInstance
q into coulombs                                 // 0.012
val v = (0.012 of coulombs) / rho               // KVolumeUnitInstance
v into liters                                   // 4.0
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

val a = (3 of coulombs) / (1 of liters)     // 3000 C/m³
val b = (1 of coulombs) / (1 of liters)     // 1000 C/m³
(a + b) into (coulombs / (meters pow 3))    // 4000.0
(a - b) into (coulombs / (meters pow 3))    // 2000.0
a > b                                       // true
a * b                                       // KMixedUnitInstance(离开该单位组)
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

((1 of coulombs) / (1 of liters)).toString() // "1000.0 C/m³"(基本单位)
```

## 记法

下表展示了该单位及其组成部分在数学写法与 KUnit 的 Kotlin 写法中的对应。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·` 表示乘法，
`/` 表示分数。对于既可写成分数也可写成含负指数乘积的量，两种等价的 Kotlin 形式都会列出。

| 数学          | Kotlin                                   | 含义                                          |
|---------------|------------------------------------------|-----------------------------------------------|
| `C/m³`        | `coulombs / (meters pow 3)`              | 电荷密度，基本单位（每立方米库仑）—— 分数形式 |
| `C·m⁻³`       | `coulombs * (meters pow -3)`             | 同一电荷密度写成含负指数的乘积                |
| `A·s/m³`      | `amperes * seconds / (meters pow 3)`     | 原生规范形式（电流·时间 / 长度³）             |
| `mC/cm³`      | `milli.coulombs / (centi.meters pow 3)`  | 每立方厘米毫库仑                              |
| `12 mC / 4 L` | `(12 of milli.coulombs) / (4 of liters)` | 由电荷 ÷ 体积构建                             |
