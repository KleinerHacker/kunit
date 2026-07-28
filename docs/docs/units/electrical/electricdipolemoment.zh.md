# 电偶极矩

包：`org.pcsoft.framework.kunit.electricdipolemoment`
基本单位：**库仑米**
（`KElectricDipoleMomentUnit.BASE == KElectricDipoleMomentUnit.COULOMB_METER`）

类型：**构造单位**

电偶极矩是一个**构造**单位：其组成为 `电流 · 时间 · 长度`
（`A·s·m` = `C·m`）。`KElectricDipoleMomentUnitInstance` 包装了一个由三项组成的 `KMixedUnitInstance` ——
`KElectricCurrentUnit.BASE`（安培）指数为 `+1`，`KTimeUnit.BASE`（秒）指数为 `+1`，以及 `KDistanceUnit.BASE`
（米）指数为 `+1`。该组不包含质量维度，因此不需要克/千克的桥接；存储值始终归一化为库仑米。

电偶极矩 `p = Q · d` 度量了一个正[电荷](charge.md)和一个负电荷之间的分离程度。它是将分子与
[电场强度](electricfieldstrength.md)耦合起来的物理量。

## 构建电偶极矩

可以用一个命名令牌构建电偶极矩，也可以通过分解构建（见下文）。命名单位以值为 1 的
令牌形式存在（配合 `of`/`into` 使用）：

| 偶极矩 | 符号 | 令牌 | 1 单位相当于多少 C·m |
|---|---|---:|---:|
| 库仑米 | `C·m` | `coulombMeters` | 1.0 |
| 德拜（CGS） | `D` | `debyes` | 3.335640952e-30 |

德拜在分子物理学和化学中占主导地位。命名单位通过 `KPrefixBuilder` 支持 SI 前缀
（`pico.coulombMeters`、`milli.debyes` 等）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electricdipolemoment.*

val p = 1.85 of debyes        // 水分子
p into debyes                 // 1.85
p into coulombMeters          // 6.1709357612e-30
```

## 多种分解方式

电偶极矩可以通过多种**等价的分解方式**得到，所有方式产生的结果在值上都相等：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `charge * length` | `KElectricDipoleMomentUnitInstance` | `p = Q · d`，电荷乘以其分离距离（可交换） |
| `current·time·length` | 通过 `.toElectricDipoleMoment()` | 原生规范形式的 `A·s·m` 表达式 |

带类型的操作符形式直接返回偶极矩。完全原生的表达式仍是一个通用的
`KMixedUnitInstance`，需通过 `toElectricDipoleMoment()` 收窄为具体类型（该方法仅识别规范正规形式，
否则会抛出 `IllegalStateException`）。两条路径在值上都相等。

反向操作符将电荷、分离距离与偶极矩联系在一起：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `electricDipoleMoment / charge` | `KLengthUnitInstance` | `d = p / Q` |
| `electricDipoleMoment / length` | `KChargeUnitInstance` | `Q = p / d` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.electricdipolemoment.*

// 现实示例 - 1 pC 的电荷分离 1 nm 得到 1e-21 C·m，约合 3.0e8 德拜。
val p = (1 of pico.coulombs) * (1 of nano.meters)   // KElectricDipoleMomentUnitInstance
p into debyes                                       // 2.997924579983392e8

// 反推分离距离：
val d = (6 of coulombMeters) / (2 of coulombs)      // KLengthUnitInstance，3 m

// 以原生的 A·s·m 表达式表示的相同偶极矩：
val raw = 6 of ((amperes pow 1) * (seconds pow 1) * (meters pow 1))
raw.toElectricDipoleMoment() == (6 of coulombMeters) // true
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricdipolemoment.*

val s = (2 of coulombMeters) + (3 of coulombMeters)  // 5 C·m
(1 of coulombMeters) > (1 of debyes)                 // true
(2 of coulombMeters) * (3 of coulombMeters)          // KMixedUnitInstance（脱离该组）
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricdipolemoment.*

(2 of coulombMeters).toString()   // "2.0 C·m"（基本单位）
```

## 符号表示

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标，`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `C·m` | `coulombMeters` | 电偶极矩，基本单位（命名令牌，库仑米） |
| `D` | `debyes` | CGS 单位德拜，3.335 640 952e-30 C·m |
| `Q · d` | `(1 of pico.coulombs) * (1 of nano.meters)` | 由电荷和其分离距离得出的偶极矩 |
| `p / Q` | `(6 of coulombMeters) / (2 of coulombs)` | 由偶极矩反推出的分离距离 |
| `A·s·m` | `(amperes pow 1) * (seconds pow 1) * (meters pow 1)` | 偶极矩作为电流·时间·长度（纯乘积形式） |
| `pC·m` | `pico.coulombMeters` | 带前缀的偶极矩（皮库仑米） |
