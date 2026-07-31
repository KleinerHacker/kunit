# 机械应力与弹性模量

包: `org.pcsoft.framework.kunit.mechanic.pressure`
基本单位: **帕斯卡**(`KPressureUnit.BASE == KPressureUnit.PASCAL`)

类型： **构造单位**

机械应力 `σ = F / A` 和弹性 (杨氏)模量 `E = σ / ε` 恰好具有[压强](pressure.md)的维度：
`mass · length⁻¹ · time⁻²`。因此 KUnit 并 **不**为它们引入单独的单位组——两者都是压强组的 **读法**，
通过其前缀别名表达。本页描述这些读法；该组本身在[压强](pressure.md)页面中描述。

!!! note "MPa、N/mm² 和 GPa 是前缀别名"
这些静力学单位 **不是**专门的令牌，因为它们可以精确地转换得到： **MPa = N/mm² = `mega.pascals`**， **GPa =
`giga.pascals`**。`(1 of newtons) / ((1 of milli.meters) *
    (1 of milli.meters))` 与 `1 of mega.pascals` 得到完全相同的值。

## 读法表

| 读法             | 符号   | Kotlin         | 1 单位等于多少 Pa |
|------------------|--------|----------------|------------------:|
| 帕斯卡           | `Pa`   | `pascals`      |               1.0 |
| 千帕斯卡         | `kPa`  | `kilo.pascals` |               1e3 |
| 兆帕斯卡 = N/mm² | `MPa`  | `mega.pascals` |               1e6 |
| 吉帕斯卡(模量)   | `GPa`  | `giga.pascals` |               1e9 |
| 力每面积         | `N/m²` | `force / area` |               1.0 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.pressure.*

val fromExpression = (1 of newtons) / ((1 of milli.meters) * (1 of milli.meters))
fromExpression into mega.pascals // 1.0(N/mm² 即兆帕斯卡)
```

## 胡克定律

结合[应变](strain.md)组，压强组承载了胡克定律的两个方面：

| 表达式                                   | 结果类型                | 含义                 |
|------------------------------------------|-------------------------|----------------------|
| `force / area`                           | `KPressureUnitInstance` | 应力 `σ = F / A`     |
| `stress / strain`                        | `KPressureUnitInstance` | 弹性模量 `E = σ / ε` |
| `pressure * strain`、`strain * pressure` | `KPressureUnitInstance` | 应力 `σ = E · ε`     |
| `pressure * area`                        | `KForceUnitInstance`    | 作用力 `F = σ · A`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.mechanic.pressure.*
import org.pcsoft.framework.kunit.mechanic.strain.perMille
import org.pcsoft.framework.kunit.mechanic.strain.div
import org.pcsoft.framework.kunit.mechanic.strain.times

val modulus = (210 of mega.pascals) / (1 of perMille) // E = σ / ε
modulus into giga.pascals                              // 210.0(钢)

val stress = (210 of giga.pascals) * (2 of perMille)   // σ = E · ε
stress into mega.pascals                                // 420.0
```

## 现实示例：受载拉杆

一根直径 20 mm 的钢制拉杆 (A ≈ 314 mm²)承受 60 kN 的载荷。应力是多少？是否低于 S235 钢 235 MPa 的屈服强度？一根 3 m 的杆会伸长多少
(E = 210 GPa)？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.pressure.*
import org.pcsoft.framework.kunit.mechanic.strain.ratio
import org.pcsoft.framework.kunit.times

val area = (10 of milli.meters) * (10 of milli.meters) * Math.PI // ≈ 314 mm²
val stress = (60 of kilo.newtons) / area
stress into mega.pascals                     // ≈ 191.0
stress < (235 of mega.pascals)                // true - 在屈服强度以内

val strainRatio = (stress into giga.pascals) / 210.0 // ε = σ / E 作为纯比值
val elongation = (3 of meters) * strainRatio
elongation into milli.meters                          // ≈ 2.73
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.mechanic.pressure.*

val sum = (100 of mega.pascals) + (50 of mega.pascals) // 150 MPa
(1 of giga.pascals) > (999 of mega.pascals)            // true
(1000 of mega.pascals) == (1 of giga.pascals)          // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.mechanic.pressure.*

(210 of mega.pascals).toString()                    // "2.1E8 Pa"(组的基本单位)
"${(210 of mega.pascals) into mega.pascals} MPa"    // "210.0 MPa"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/`
表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学         | Kotlin                                            | 含义                 |
|--------------|---------------------------------------------------|----------------------|
| `MPa`        | `mega.pascals`                                    | 应力读法(= N/mm²)    |
| `N/mm²`      | `newtons / (milli.meters pow 2)`                  | 与力每面积相同的读法 |
| `GPa`        | `giga.pascals`                                    | 弹性模量读法         |
| `kg·m⁻¹·s⁻²` | `kilo.grams * (meters pow -1) * (seconds pow -2)` | 同一量以基础维度表示 |
| `σ = F / A`  | `force / area`                                    | 由力和面积求得的应力 |
| `E = σ / ε`  | `stress / strain`                                 | 胡克定律，求解模量   |
| `σ = E · ε`  | `pressure * strain`                               | 胡克定律，求解应力   |
