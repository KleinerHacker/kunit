# 线电荷密度

包：`org.pcsoft.framework.kunit.electric.linearchargedensity`
基本单位： **库仑每米**
（`KLinearChargeDensityUnit.BASE == KLinearChargeDensityUnit.COULOMB_PER_METER`）

类型： **构造单位**

线电荷密度是一个 **构造**单位：其组成为 `电流 · 时间 · 长度⁻¹`
（`A·s·m⁻¹` = `C/m`）。`KLinearChargeDensityUnitInstance` 包装了一个由三项组成的 `KMixedUnitInstance` ——
`KElectricCurrentUnit.BASE`（安培）指数为 `+1`，`KTimeUnit.BASE`（秒）指数为 `+1`，以及 `KDistanceUnit.BASE`
（米）指数为 `-1`。该组不包含质量维度，因此不需要克/千克的桥接；存储值始终归一化为库仑每米。

线电荷密度 `λ` 是单位长度上承载的电荷，例如沿一根导线或一条带电细丝分布的电荷。它 **没有自己的命名单位**：
每种写法都是一个比值（C/m、µC/cm），因此该组没有裸令牌，也没有前缀构建器 —— 值须通过表达式或带类型的
操作符构建。二维对应量是[电通量密度](electricfluxdensity.md)（C/m²），三维对应量是
[电荷密度](chargedensity.md)（C/m³）。

## 构建线电荷密度

没有命名令牌。请通过电荷除以长度构建一个值：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

val lambda = (5 of micro.coulombs) / (2 of meters)  // 2.5e-6 C/m
lambda.value                                        // 2.5e-6（归一化为 C/m）
```

## 多种分解方式

线电荷密度可以通过多种 **等价的分解方式**得到，所有方式产生的结果在值上都相等：

| 表达式                | 结果类型                           | 含义                            |
|-----------------------|------------------------------------|---------------------------------|
| `charge / length`     | `KLinearChargeDensityUnitInstance` | `λ = Q / l`，电荷沿一条长度分布 |
| `current·time/length` | 通过 `.toLinearChargeDensity()`    | 原生规范形式的 `A·s·m⁻¹` 表达式 |

带类型的操作符形式直接返回线电荷密度。完全原生的表达式仍是一个通用的
`KMixedUnitInstance`，需通过 `toLinearChargeDensity()` 收窄为具体类型（该方法仅识别规范正规形式， 否则会抛出
`IllegalStateException`）。两条路径在值上都相等。

反向操作符将电荷、长度与密度联系在一起：

| 表达式                         | 结果类型              | 含义                  |
|--------------------------------|-----------------------|-----------------------|
| `linearChargeDensity * length` | `KChargeUnitInstance` | `Q = λ · l`（可交换） |
| `charge / linearChargeDensity` | `KLengthUnitInstance` | `l = Q / λ`           |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

// 现实示例 - 一条细丝在 2 m 内承载 5 µC 的电荷，线电荷密度为 2.5 µC/m。
val lambda = (5 of micro.coulombs) / (2 of meters)   // 2.5e-6 C/m

// 反推电荷：
val q = lambda * (2 of meters)                       // KChargeUnitInstance，5 µC
q into micro.coulombs                                // 5.0

// 以原生的 A·s·m⁻¹ 表达式表示的相同密度：
val raw = 2.5e-6 of ((amperes pow 1) * (seconds pow 1)) / (meters pow 1)
raw.toLinearChargeDensity() == lambda                // true
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

val a = (2 of coulombs) / (1 of meters)
val b = (3 of coulombs) / (1 of meters)
(a + b).value    // 5.0 C/m
b > a            // true
(a * b)          // KMixedUnitInstance（脱离该组）
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

((2 of coulombs) / (1 of meters)).toString()   // "2.0 C/m"（基本单位）
```

## 符号表示

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`⁻¹`），`·` 表示乘法，
`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示  | Kotlin                                                 | 含义                                 |
|-----------|--------------------------------------------------------|--------------------------------------|
| `C/m`     | `(1 of coulombs) / (1 of meters)`                      | 线电荷密度，基本单位（无命名令牌）   |
| `Q / l`   | `(5 of micro.coulombs) / (2 of meters)`                | 由电荷沿一条长度分布得出的密度       |
| `λ · l`   | `lambda * (2 of meters)`                               | 一条长度上承载的电荷                 |
| `A·s/m`   | `((amperes pow 1) * (seconds pow 1)) / (meters pow 1)` | 密度作为电流·时间 / 长度（分数形式） |
| `A·s·m⁻¹` | `(amperes pow 1) * (seconds pow 1) * (meters pow -1)`  | 相同密度作为纯乘积形式               |
