# 存储密度

包: `org.pcsoft.framework.kunit.it.storagedensity`
基本单位: **字节每平方米**(`KStorageDensityUnit.BASE == KStorageDensityUnit.BYTES_PER_SQUARE_METER`)

类型: **构成单位**

存储密度是一个**构成**单位: 它不是单一的“真实”量,而是一个组合,`storage · distance⁻²`(`B/m²`)。因此
`KStorageDensityUnitInstance` 包装了一个恰好有两个项的 `KMixedUnitInstance` — 一个指数为 `+1` 的
`KStorageUnit.BASE`(字节)和一个指数为 `-2` 的 `KDistanceUnit.BASE`(米)。无论它由哪个单位或存储/面积组合
创建,值始终归一化存储为字节每平方米。

## 构建存储密度

存储密度以**存储每面积表达式**构建,例如 `100 of bytes / area`、`5 of mega.bytes / area`。面积是任意的
`KAreaUnitInstance`(例如 `(1 of meters) * (1 of meters)`),因此所有 SI/二进制前缀和长度单位都能自由组合。
用任意存储每面积模板读回(`d into (bits / area)`)。这里有意**没有**拼写出来的复合标记。

基本单位: 与存储组一致的*字节*每平方米。“比特每平方米”是 `0.125 B/m²`。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)  // 1 m²
val d = 100 of bytes / area
d.value               // 100.0(归一化为 B/m²)
d into (bits / area)  // 800.0(以 bit/m² 读回)
```

## 真实示例: SSD 晶粒的面密度

一个闪存晶粒在 **100 mm²** 的表面上存储 **256 GB**。它的面存储密度是数据量除以面积:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val data = 256 of giga.bytes                       // 256 GB
val side = 10 of milli.meters                      // 10 mm × 10 mm 的晶粒 = 100 mm²
val area = side * side
val density = data / area                          // KStorageDensityUnitInstance
density.value                                       // 2.56e15(B/m²)
density into (giga.bytes / (side * side))           // 256.0(每 100 mm² 的 GB)
```

## 使用核心单位(存储与面积)计算

存储密度*就是*存储量除以面积。用普通的 `*` 和 `/` 在三个量 — 存储、面积和存储密度 — 之间转换;每个结果都是
**强类型**的。

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `storage / area` | `KStorageDensityUnitInstance` | 密度 = 量 / 面积 |
| `storage density * area` | `KStorageUnitInstance` | 量 = 密度 × 面积 |
| `area * storage density` | `KStorageUnitInstance` | 量(交换律) |
| `storage / storage density` | `KAreaUnitInstance` | 面积 = 量 / 密度 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)   // 1 m²

// --- 核心单位 -> 存储密度 --------------------------------------
val d = (100 of bytes) / area   // KStorageDensityUnitInstance(无需 .toStorageDensity()!)
d.value               // 100.0(B/m²)

// --- 存储密度 -> 存储(乘以面积)-------------------
val amount = d * area           // KStorageUnitInstance
amount into bytes     // 100.0
area * d              // 相同结果(交换律)

// --- 存储密度 -> 面积(用存储量去除)------------------
val a = (600 of bytes) / d      // KAreaUnitInstance (6 m²)
```

!!! warning "只有*纯粹的* 存储 / 面积 形状才是存储密度"
    `KMixedUnitInstance.toStorageDensity()` 要求恰好一个指数为 `+1` 的存储项和一个指数为 `-2` 的距离项。
    `B²·m⁻²`、`B·m⁻¹` 或 `B·m²` 形状不是存储密度 — 转换会抛出 `IllegalStateException`。同样,
    `storage + storage density`(不同维度)是编译错误。

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)

// + / - : 同组,字节与比特密度之间自动转换
val a = (1 of bytes / area) + (8 of bits / area)   // KStorageDensityUnitInstance, 2 B/m²
val b = (2 of bytes / area) - (8 of bits / area)   // 1 B/m²

// 比较(按归一化的 B/m² 值)
(1 of bytes / area) > (4 of bits / area)           // true
(1 of bytes / area) == (8 of bits / area)          // true

// 两个存储密度之间的 * / / 会退化为 KMixedUnitInstance(不再是纯密度)
val squared = (10 of bytes / area) * (2 of bytes / area) // KMixedUnitInstance, [B^2, m^-4]
```

## SI 和二进制(IEC)前缀

存储密度组沿用 [存储](storage.md) 组的前缀策略(其分子是存储量): 分子使用**增大**的 SI 构建器
(`kilo`、`mega`、…)或**二进制**构建器(`kibi`、`mebi`、…);分母(面积)使用任意长度单位和前缀。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val mm2 = (1 of milli.meters) * (1 of milli.meters)  // 1 mm²
val d = 1 of kilo.bytes / mm2                         // 1 kB/mm²
d into (kilo.bytes / mm2)  // 1.0
```

## toString 格式化

只存在基本单位的 `toString()`;通过 `into` 或 `format` 格式化特定单位:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.it.storage.bytes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)
((1000 of bytes) / area).toString()  // "1000.0 B/m²"(基本单位)
((1000 of bytes) / area) format (kilo.bytes.toUnit() / area.toUnit()) // "1.0 kB/m^2"
```

## 记法

下表展示该单位及其组成部分在数学上与在使用 KUnit 的 Kotlin 中如何书写。指数使用 Unicode 上标(`²`、`³`、`⁻¹`),`·` 表示乘法,`/` 表示分数。当一个量既可写成分数又可写成带负指数的乘积时,两种等价的 Kotlin 形式都会列出。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `B/m²` | `bytes / area` | 存储密度,基本单位(字节每平方米) — 分数形式 |
| `B·m⁻²` | `bytes * (meters pow -2)` | 作为带负指数乘积的相同密度 |
| `bit/m²` | `bits / area` | 比特每平方米 |
| `kB/mm²` | `kilo.bytes / mm2` | 千字节每平方毫米 |
| `256 GB / 100 mm²` | `(256 of giga.bytes) / (side * side)` | 由 存储 ÷ 面积 构建 |
