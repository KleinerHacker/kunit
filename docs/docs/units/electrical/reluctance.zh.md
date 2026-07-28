# 磁阻

包：`org.pcsoft.framework.kunit.reluctance`
基本单位：**安培每韦伯**（`KReluctanceUnit.BASE == KReluctanceUnit.AMPERE_PER_WEBER`）

类型：**构造单位**

磁阻是一个**构造**单位：其组成为 `质量⁻¹ · 长度⁻² · 时间² · 电流²`
（`kg⁻¹·m⁻²·s²·A²` = `A/Wb` = `H⁻¹`）。`KReluctanceUnitInstance` 包装了一个由四项组成的 `KMixedUnitInstance` ——
`KMassUnit.BASE`（克）指数为 `-1`，`KDistanceUnit.BASE`（米）指数为 `-2`，`KTimeUnit.BASE`（秒）指数为 `+2`，
以及 `KElectricCurrentUnit.BASE`（安培）指数为 `+2`。由于库中的质量分量以**克**（而非千克）为归一化基准，
且质量指数为负，规范乘积需乘以 1000 才能得到安培每韦伯；存储值始终以安培每韦伯归一化。

磁阻 `Rm` 是磁路中对应于电学[电阻](resistance.md)的量：它通过霍普金森定律 `Θ = Rm · Φ`，将磁动势
`Θ`（以安匝为单位，参见[电流](ec.md)）与产生的[磁通量](magneticflux.md)联系起来。它的倒数是
**磁导** `Λ`，以亨利为单位，因此由[电感](inductance.md)组承载。

## 构建磁阻

可以用一个命名令牌构建磁阻，也可以通过分解构建（见下文）。命名单位以值为 1 的
令牌形式存在（配合 `of`/`into` 使用）：

| 磁阻 | 符号 | 令牌 | 1 单位相当于多少 A/Wb |
|---|---|---:|---:|
| 安培每韦伯 | `A/Wb` | `amperesPerWeber` | 1.0 |
| 逆亨利 | `H⁻¹` | `inverseHenries` | 1.0 |
| 安匝每韦伯 | `At/Wb` | `ampereTurnsPerWeber` | 1.0 |

这三种写法描述的是同一个量——线圈匝数只是一个纯计数——因此它们在值上相等；不同的符号只是记录了看待
方式的不同。命名单位通过 `KPrefixBuilder` 支持 SI 前缀（`mega.amperesPerWeber`、`kilo.inverseHenries`
等）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.reluctance.*

val rm = 2 of mega.amperesPerWeber    // 带气隙的铁芯
rm into mega.amperesPerWeber          // 2.0
rm into amperesPerWeber               // 2.0e6
(1 of amperesPerWeber) == (1 of inverseHenries) // true
```

## 多种分解方式

磁阻可以通过多种**等价的分解方式**得到，所有方式产生的结果在值上都相等：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `current / magneticFlux` | `KReluctanceUnitInstance` | 霍普金森定律 `Rm = Θ / Φ` |
| `1 / inductance` | `KReluctanceUnitInstance` | 磁导的倒数，`Rm = 1 / Λ` |
| `(time²·current²)/(mass·length²)` | 通过 `.toReluctance()` | 原生规范形式的 `kg⁻¹·m⁻²·s²·A²` 表达式 |

带类型的操作符形式直接返回磁阻。完全原生的表达式仍是一个通用的
`KMixedUnitInstance`，需通过 `toReluctance()` 收窄为具体类型（该方法仅识别规范正规形式，
否则会抛出 `IllegalStateException`）。所有路径在值上都相等。

反向操作符将磁动势、磁通量、磁导与磁阻联系在一起：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `reluctance * magneticFlux` | `KElectricCurrentUnitInstance` | `Θ = Rm · Φ`（可交换） |
| `current / reluctance` | `KMagneticFluxUnitInstance` | `Φ = Θ / Rm` |
| `1 / reluctance` | `KInductanceUnitInstance` | 磁导 `Λ = 1 / Rm`（以亨利为单位） |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.ec.ampereTurns
import org.pcsoft.framework.kunit.magneticflux.webers
import org.pcsoft.framework.kunit.inductance.henries
import org.pcsoft.framework.kunit.reluctance.*

// 现实示例 - 2 kAt 的磁动势通过 2 MA/Wb 的铁芯得到 1 mWb 的磁通量。
val rm = 2_000_000 of amperesPerWeber
val flux = (2000 of ampereTurns) / rm       // KMagneticFluxUnitInstance
flux into milli.webers                      // 1.0

// 用定义式求解磁阻：
val fromHopkinson = (6 of amperes) / (3 of webers)   // 2 A/Wb
val fromPermeance = 1 / (0.5 of henries)             // 2 A/Wb

// 以原生的 kg⁻¹·m⁻²·s²·A² 表达式表示的相同磁阻：
val raw = 2 of ((seconds pow 2) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
raw.toReluctance() == (2 of amperesPerWeber)         // true
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.reluctance.*

val series = (1 of amperesPerWeber) + (1 of inverseHenries)  // 2 A/Wb（串联磁路）
(3 of amperesPerWeber) > (2 of amperesPerWeber)              // true
(2 of amperesPerWeber) * (3 of amperesPerWeber)              // KMixedUnitInstance（脱离该组）
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.reluctance.*

(2 of inverseHenries).toString()   // "2.0 A/Wb"（基本单位）
```

## 符号表示

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`⁻²`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `A/Wb` | `amperesPerWeber` | 磁阻，基本单位（命名令牌，安培每韦伯） |
| `H⁻¹` | `inverseHenries` | 同一量的逆电感写法 |
| `Θ / Φ` | `(6 of amperes) / (3 of webers)` | 由霍普金森定律得出的磁阻 |
| `1 / Λ` | `1 / (0.5 of henries)` | 磁阻作为磁导的倒数 |
| `(s²·A²)/(kg·m²)` | `((seconds pow 2) * (amperes pow 2)) / (kilo.grams * (meters pow 2))` | 磁阻作为 (时间²·电流²) / (质量·长度²)（分数形式） |
| `kg⁻¹·m⁻²·s²·A²` | `(kilo.grams pow -1) * (meters pow -2) * (seconds pow 2) * (amperes pow 2)` | 相同磁阻作为纯乘积形式 |
| `MA/Wb` | `mega.amperesPerWeber` | 带前缀的磁阻（兆安培每韦伯） |
