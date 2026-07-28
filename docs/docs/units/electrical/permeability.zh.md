# 磁导率

包：`org.pcsoft.framework.kunit.electric.permeability`
基本单位：**亨利每米**（`KPermeabilityUnit.BASE == KPermeabilityUnit.HENRY_PER_METER`）

类型：**构造单位**

磁导率是一个**构造**单位：其组成为 `质量 · 长度 · 时间⁻² · 电流⁻²`
（`kg·m·s⁻²·A⁻²` = `H/m`）。`KPermeabilityUnitInstance` 包装了一个由四项组成的 `KMixedUnitInstance` ——
`KMassUnit.BASE`（克）指数为 `+1`，`KDistanceUnit.BASE`（米）指数为 `+1`，`KTimeUnit.BASE`（秒）指数为 `-2`，
以及 `KElectricCurrentUnit.BASE`（安培）指数为 `-2`。由于库中的质量分量以**克**（而非千克）为归一化基准，
规范乘积需除以 1000 才能得到亨利每米；存储值始终以亨利每米归一化。

磁导率 `μ` 是材料的磁常数：它将[磁通密度](magneticfluxdensity.md)与
[磁场强度](magneticfieldstrength.md)相关联（`μ = B / H`），也将[电感](inductance.md)与
线圈几何形状相关联。它的电学对应量是[介电常数](permittivity.md)。

## 构建磁导率

可以用一个命名令牌构建磁导率，也可以通过分解构建（见下文）。命名单位以值为 1 的
令牌形式存在（配合 `of`/`into` 使用）：

| 磁导率 | 符号 | 令牌 | 1 单位相当于多少 H/m |
|---|---|---:|---:|
| 亨利每米 | `H/m` | `henriesPerMeter` | 1.0 |
| 亨利每厘米 | `H/cm` | `henriesPerCentimeter` | 100.0 |
| 真空磁导率 `μ₀` | `H/m` | `vacuumPermeability` | 1.25663706127e-6 |

命名单位通过 `KPrefixBuilder` 支持 SI 前缀（`micro.henriesPerMeter`、`milli.henriesPerMeter`
等）。该常数也可以通过 `KPermeabilityUnit.VACUUM_PERMEABILITY` 获取。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.permeability.*

val mu = 1 of vacuumPermeability      // μ₀
mu into henriesPerMeter               // 1.25663706127e-6
mu into micro.henriesPerMeter         // 1.25663706127
(1 of henriesPerCentimeter) into henriesPerMeter // 100.0
```

## 多种分解方式

磁导率可以通过多种**等价的分解方式**得到，所有方式产生的结果在值上都相等：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `inductance / length` | `KPermeabilityUnitInstance` | `μ = L · l / (N² · A)`，几何因子是长度 |
| `magneticFluxDensity / magneticFieldStrength` | `KPermeabilityUnitInstance` | `μ = B / H` |
| `mass·length/(time²·current²)` | 通过 `.toPermeability()` | 原生规范形式的 `kg·m·s⁻²·A⁻²` 表达式 |

带类型的操作符形式直接返回磁导率。完全原生的表达式仍是一个通用的
`KMixedUnitInstance`，需通过 `toPermeability()` 收窄为具体类型（该方法仅识别规范正规形式，
否则会抛出 `IllegalStateException`）。所有路径在值上都相等。

反向操作符将电感、长度以及两种磁场量联系在一起：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `permeability * length` | `KInductanceUnitInstance` | `L = μ · N² · A / l`（可交换） |
| `inductance / permeability` | `KLengthUnitInstance` | 几何因子 `N² · A / l = L / μ` |
| `permeability * magneticFieldStrength` | `KMagneticFluxDensityUnitInstance` | `B = μ · H`（可交换） |
| `magneticFluxDensity / permeability` | `KMagneticFieldStrengthUnitInstance` | `H = B / μ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.inductance.henries
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.amperesPerMeter
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.teslas
import org.pcsoft.framework.kunit.electric.permeability.*

// 现实示例 - 在真空中，1000 A/m 的场产生 1.257 mT 的通量密度。
val b = (1 of vacuumPermeability) * (1000 of amperesPerMeter)  // 1.25663706127e-3 T

// 用定义式求解磁导率：
val mu = (6 of teslas) / (3 of amperesPerMeter)                // 2 H/m
val fromInductance = (10 of henries) / (5 of meters)           // 2 H/m

// 以原生的 kg·m·s⁻²·A⁻² 表达式表示的相同磁导率：
val raw = 2 of (kilo.grams * (meters pow 1)) / ((seconds pow 2) * (amperes pow 2))
raw.toPermeability() == (2 of henriesPerMeter)                 // true
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.permeability.*

val s = (1 of henriesPerMeter) + (1 of henriesPerCentimeter)  // 101 H/m
(1 of henriesPerCentimeter) > (1 of henriesPerMeter)          // true
(2 of henriesPerMeter) * (3 of henriesPerMeter)               // KMixedUnitInstance（脱离该组）
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.permeability.*

(1 of henriesPerCentimeter).toString()   // "100.0 H/m"（基本单位）
```

## 符号表示

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`⁻²`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `H/m` | `henriesPerMeter` | 磁导率，基本单位（命名令牌，亨利每米） |
| `μ₀` | `vacuumPermeability` | 真空磁导率常数，1.257 µH/m |
| `B / H` | `(6 of teslas) / (3 of amperesPerMeter)` | 由通量密度和场强得出的磁导率 |
| `L · l / (N²·A)` | `(10 of henries) / (5 of meters)` | 由电感和线圈几何形状得出的磁导率 |
| `kg·m/(s²·A²)` | `(kilo.grams * (meters pow 1)) / ((seconds pow 2) * (amperes pow 2))` | 磁导率作为质量·长度 / (时间²·电流²)（分数形式） |
| `kg·m·s⁻²·A⁻²` | `kilo.grams * (meters pow 1) * (seconds pow -2) * (amperes pow -2)` | 相同磁导率作为纯乘积形式 |
| `µH/m` | `micro.henriesPerMeter` | 带前缀的磁导率（微亨每米） |
