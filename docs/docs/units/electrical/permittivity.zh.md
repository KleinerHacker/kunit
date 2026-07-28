# 介电常数

包：`org.pcsoft.framework.kunit.electric.permittivity`
基本单位：**法拉每米**（`KPermittivityUnit.BASE == KPermittivityUnit.FARAD_PER_METER`）

类型：**构造单位**

介电常数是一个**构造**单位：其组成为 `质量⁻¹ · 长度⁻³ · 时间⁴ · 电流²`
（`kg⁻¹·m⁻³·s⁴·A²` = `F/m`）。`KPermittivityUnitInstance` 包装了一个由四项组成的 `KMixedUnitInstance` ——
`KMassUnit.BASE`（克）指数为 `-1`，`KDistanceUnit.BASE`（米）指数为 `-3`，`KTimeUnit.BASE`（秒）指数为 `+4`，
以及 `KElectricCurrentUnit.BASE`（安培）指数为 `+2`。由于库中的质量分量以**克**（而非千克）为归一化基准，且
质量指数为负，规范乘积需乘以 1000 才能得到法拉每米；存储值始终以法拉每米归一化。

介电常数 `ε` 是材料的电常数：它将[电通量密度](electricfluxdensity.md)与
[电场强度](electricfieldstrength.md)相关联（`ε = D / E`），也将[电容](capacitance.md)与
极板几何形状相关联。它的磁学对应量是[磁导率](permeability.md)。

## 构建介电常数

可以用一个命名令牌构建介电常数，也可以通过分解构建（见下文）。命名单位以值为 1 的
令牌形式存在（配合 `of`/`into` 使用）：

| 介电常数 | 符号 | 令牌 | 1 单位相当于多少 F/m |
|---|---|---:|---:|
| 法拉每米 | `F/m` | `faradsPerMeter` | 1.0 |
| 法拉每厘米 | `F/cm` | `faradsPerCentimeter` | 100.0 |
| 真空介电常数 `ε₀` | `F/m` | `vacuumPermittivity` | 8.8541878188e-12 |

命名单位通过 `KPrefixBuilder` 支持 SI 前缀（`pico.faradsPerMeter`、`nano.faradsPerMeter` 等）。
该常数也可以通过 `KPermittivityUnit.VACUUM_PERMITTIVITY` 获取。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.electric.permittivity.*

val eps = 1 of vacuumPermittivity     // ε₀
eps into faradsPerMeter               // 8.8541878188e-12
eps into pico.faradsPerMeter          // 8.8541878188
(1 of faradsPerCentimeter) into faradsPerMeter // 100.0
```

## 多种分解方式

介电常数可以通过多种**等价的分解方式**得到，所有方式产生的结果在值上都相等：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `capacitance / length` | `KPermittivityUnitInstance` | `ε = C · d / A`，几何因子 `d / A` 是长度 |
| `electricFluxDensity / electricFieldStrength` | `KPermittivityUnitInstance` | `ε = D / E` |
| `(time⁴·current²)/(mass·length³)` | 通过 `.toPermittivity()` | 原生规范形式的 `kg⁻¹·m⁻³·s⁴·A²` 表达式 |

带类型的操作符形式直接返回介电常数。完全原生的表达式仍是一个通用的
`KMixedUnitInstance`，需通过 `toPermittivity()` 收窄为具体类型（该方法仅识别规范正规形式，
否则会抛出 `IllegalStateException`）。所有路径在值上都相等。

反向操作符将电容、长度以及两种场量联系在一起：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `permittivity * length` | `KCapacitanceUnitInstance` | `C = ε · A / d`（可交换） |
| `capacitance / permittivity` | `KLengthUnitInstance` | 几何因子 `A / d = C / ε` |
| `permittivity * electricFieldStrength` | `KElectricFluxDensityUnitInstance` | `D = ε · E`（可交换） |
| `electricFluxDensity / permittivity` | `KElectricFieldStrengthUnitInstance` | `E = D / ε` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.capacitance.farads
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.electric.fluxdensity.coulombsPerSquareMeter
import org.pcsoft.framework.kunit.electric.permittivity.*

// 现实示例 - 在真空中，1 MV/m 的场产生 8.854 µC/m² 的通量密度。
val d = (1 of vacuumPermittivity) * (1_000_000 of voltsPerMeter)  // 8.8541878188e-6 C/m²

// 用定义式求解介电常数：
val eps = (6 of coulombsPerSquareMeter) / (3 of voltsPerMeter)    // 2 F/m
val fromCapacitance = (10 of farads) / (5 of meters)              // 2 F/m

// 以原生的 kg⁻¹·m⁻³·s⁴·A² 表达式表示的相同介电常数：
val raw = 2 of ((seconds pow 4) * (amperes pow 2)) / (kilo.grams * (meters pow 3))
raw.toPermittivity() == (2 of faradsPerMeter)                     // true
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.permittivity.*

val s = (1 of faradsPerMeter) + (1 of faradsPerCentimeter)  // 101 F/m
(1 of faradsPerCentimeter) > (1 of faradsPerMeter)          // true
(2 of faradsPerMeter) * (3 of faradsPerMeter)               // KMixedUnitInstance（脱离该组）
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.permittivity.*

(1 of faradsPerCentimeter).toString()   // "100.0 F/m"（基本单位）
```

## 符号表示

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`⁴`、`⁻³`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `F/m` | `faradsPerMeter` | 介电常数，基本单位（命名令牌，法拉每米） |
| `ε₀` | `vacuumPermittivity` | 真空介电常数，8.854 pF/m |
| `D / E` | `(6 of coulombsPerSquareMeter) / (3 of voltsPerMeter)` | 由通量密度和场强得出的介电常数 |
| `C · (d/A)` | `(10 of farads) / (5 of meters)` | 由电容和几何因子得出的介电常数 |
| `(s⁴·A²)/(kg·m³)` | `((seconds pow 4) * (amperes pow 2)) / (kilo.grams * (meters pow 3))` | 介电常数作为 (时间⁴·电流²) / (质量·长度³)（分数形式） |
| `kg⁻¹·m⁻³·s⁴·A²` | `(kilo.grams pow -1) * (meters pow -3) * (seconds pow 4) * (amperes pow 2)` | 相同介电常数作为纯乘积形式 |
| `pF/m` | `pico.faradsPerMeter` | 带前缀的介电常数（皮法每米） |
