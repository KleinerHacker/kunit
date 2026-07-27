# 磁通量

包：`org.pcsoft.framework.kunit.magneticflux`
基本单位：**韦伯**（`KMagneticFluxUnit.BASE == KMagneticFluxUnit.WEBER`）

类型：**构造单位**

磁通量是一个**构造**单位：其组成为 `质量 · 长度² · 时间⁻² · 电流⁻¹`
（`kg·m²·s⁻²·A⁻¹`）。`KMagneticFluxUnitInstance` 包装了一个由四项组成的 `KMixedUnitInstance` —— `KMassUnit.BASE`
（克）指数为 `+1`，`KDistanceUnit.BASE`（米）指数为 `+2`，`KTimeUnit.BASE`（秒）指数为 `-2`，以及
`KElectricCurrentUnit.BASE`（安培）指数为 `-1`。由于库中的质量分量以**克**（而非千克）为归一化基准，
规范乘积需除以 1000 才能得到韦伯；存储值始终以韦伯归一化。

## 构建磁通量

可以用一个命名令牌构建磁通量，也可以通过分解构建（见下文）。命名单位以值为 1 的令牌形式存在
（配合 `of`/`into` 使用）：

| 磁通量 | 符号 | 令牌 | 1 单位相当于多少 Wb |
|---|---|---:|---:|
| 韦伯 | `Wb` | `webers` | 1.0 |
| 麦克斯韦（CGS-EMU） | `Mx` | `maxwells` | 1.0e-8 |
| 单位磁极 | `pole` | `unitPoles` | 1.2566370614359173e-7 |

命名单位通过 `KPrefixBuilder` 支持 SI 前缀（`milli.webers`、`micro.webers`、`kilo.maxwells` 等）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.magneticflux.*

val phi = 20 of milli.webers
phi into milli.webers          // 20.0
phi into webers                // 0.02
(1 of webers) into maxwells    // 1.0e8
```

## 多种分解方式

磁通量可以通过多种**等价的分解方式**得到，所有方式产生的结果在值上都相等：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `voltage * time` | `KMagneticFluxUnitInstance` | 法拉第感应定律 `Φ = U · t`（可交换） |
| `voltage / frequency` | `KMagneticFluxUnitInstance` | 时间倒数形式（`V/Hz = V·s`） |
| `inductance * current` | `KMagneticFluxUnitInstance` | `Φ = L · I`（参见[电感](inductance.md)） |
| `fluxDensity * area` | `KMagneticFluxUnitInstance` | `Φ = B · A`（参见[磁通密度](magneticfluxdensity.md)） |
| `mass·length²/(time²·current)` | 通过 `.toMagneticFlux()` | 原生规范形式的 `kg·m²·s⁻²·A⁻¹` 表达式 |

带类型的操作符形式直接返回磁通量。完全原生的表达式仍是一个通用的
`KMixedUnitInstance`，需通过 `toMagneticFlux()` 收窄为具体类型（该方法仅识别规范正规形式，
否则会抛出 `IllegalStateException`）。所有路径在值上都相等。

反向操作符将电压、时间与磁通量联系在一起：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `flux / time` | `KVoltageUnitInstance` | 感应电压 `U = Φ / t` |
| `flux * frequency` | `KVoltageUnitInstance` | 对应的时间倒数形式 |
| `flux / voltage` | `KTimeUnitInstance` | `t = Φ / U` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.frequency.hertz
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.magneticflux.*

// 现实示例 - 点火线圈：20 mWb 的铁芯磁通量在 4 ms 内衰减，感应出 5 V 电压。
val u = (20 of milli.webers) / (4 of milli.seconds)   // KVoltageUnitInstance，5 V

// 用感应定律求解磁通量：
val phi = (10 of volts) * (0.2 of seconds)            // KMagneticFluxUnitInstance，2 Wb

// 由频率得出的相同磁通量，以及原生的 kg·m²·s⁻²·A⁻¹ 表达式：
val fromFrequency = (10 of volts) / (5 of hertz)      // 2 Wb
val raw = 2 of (kilo.grams * (meters pow 2)) / ((seconds pow 2) * (amperes pow 1))
raw.toMagneticFlux() == (2 of webers)                 // true
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.magneticflux.*

val s = (100 of webers) + (40 of webers)  // 140 Wb
(100 of webers) > (40 of webers)          // true
(100 of webers) * (40 of webers)          // KMixedUnitInstance（脱离该组）
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.magneticflux.*

(20 of webers).toString()     // "20.0 Wb"（基本单位）
```

## 符号表示

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`⁻¹`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `Wb` | `webers` | 磁通量，基本单位（命名令牌，韦伯） |
| `V·s` | `(10 of volts) * (0.2 of seconds)` | 磁通量作为电压·时间（感应定律） |
| `kg·m²/(s²·A)` | `(kilo.grams * (meters pow 2)) / ((seconds pow 2) * (amperes pow 1))` | 磁通量作为质量·长度² / (时间²·电流)（分数形式） |
| `kg·m²·s⁻²·A⁻¹` | `kilo.grams * (meters pow 2) * (seconds pow -2) * (amperes pow -1)` | 相同磁通量作为纯乘积形式 |
| `mWb` | `milli.webers` | 带前缀的磁通量（毫韦伯） |
