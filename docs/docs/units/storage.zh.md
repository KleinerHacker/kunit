# 存储

包: `org.pcsoft.framework.kunit.storage`
基准单位: **字节** (`KStorageUnit.BASE == KStorageUnit.BYTE`)

存储组用于建模数字数据量。它是一个**简单的一维**组（不像距离组那样按指数划分子类型，也不像时间组那样以
`Duration` 为底层）。`KStorageUnitInstance` 封装单个 `KStorageUnit.BASE`（字节）项，始终以字节归一化存储。

该组有两个特殊之处:

* **没有缩小值的前缀。** 比特的分数不是有意义的数据量，因此缩小值的 SI 前缀（`deci`、`centi`、`milli` 等
  — 系数 `< 1`）**不予提供**。编写 `5 milli bytes` 会导致**编译错误**，而非运行时错误。只有不缩小值的
  SI 前缀（`deca` 及以上）存在。
* **二进制（IEC）前缀。** 除十进制 SI 前缀（`kilo` = 1000）外，还有第二套二进制前缀体系
  （`KStorageBinaryPrefix`: `kibi` = 1024、`mebi` = 1024²、…），使数值能够区分十进制步长 1000 与二进制
  步长 1024。

## 单位

| 单位 | 枚举值 | 符号 | 创建器 | 换算为字节 |
|---|---|---|---:|---:|
| 字节 | `KStorageUnit.BYTE` | `B` | `Number.bytes` | 1.0 |
| 比特 | `KStorageUnit.BIT` | `bit` | `Number.bits` | 0.125 |

一个字节等于八比特。两个单位都有对应的 bare `val` 别名（`bytes`、`bits`），可用作 `valueAs`/`toString`
的目标，或用作前缀 `infix` 函数的 `unit` 参数。

```kotlin
import org.pcsoft.framework.kunit.storage.*

val size = 5.bytes
size.value                     // 5.0 (normalized to bytes)
size.valueAs(bits)             // 40.0 (read back in bits)
1.bytes.valueAs(bits)          // 8.0
8.bits.valueAs(bytes)          // 1.0
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.storage.*

// + / - : 同组，比特与字节之间自动转换
val a = 1.bytes + 8.bits        // KStorageUnitInstance: 2.0 B
val b = 4.bytes - 16.bits       // KStorageUnitInstance: 2.0 B

// 比较
1.bytes == 8.bits               // true (same normalized amount)
2.bytes > 1.bytes               // true

// * / / 委托给混合引擎（针对 KMixedUnitInstance）
val rate = 1000.bytes.toUnit() / 2.seconds.toUnit() // KMixedUnitInstance: 500 B·s⁻¹
```

### 比较与相等

`==`、`!=`、`<`、`<=`、`>`、`>=` 比较两个 `KStorageUnitInstance` 值的归一化 `value`（字节）。`equals`
基于归一化的数量，因此 `1.bytes == 8.bits`。

## 使用 `pow` 求幂

使用中缀 `pow` 运算符将值求整数次幂（Kotlin 没有可重载的 `^`）。对于存储组，`pow` 返回通用的
`KMixedUnitInstance`（存储没有带维度的幂类型）:

```kotlin
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.storage.*

val squared = 2.bytes pow 2     // KMixedUnitInstance: 4.0 B²
```

`pow` 是具名中缀函数，因此其结合力**弱于** `* / + -`；在混合表达式中请加括号（`(a * b) pow 2`）。

## 十进制 SI 前缀

任何 `KStorageUnit` 都可以与**不缩小值的** SI 前缀（`deca`、`hecto`、`kilo`、`mega`、`giga`、`tera`、
`peta`、`exa`、`zetta`、`yotta`、`ronna`、`quetta`）结合，使用存储组的 `infix` 构造函数（直接返回
`KStorageUnitInstance`）和 `with`（用于 `valueAs`/`toString` 目标）。缩小值的前缀（`deci` 及以下）在
存储组中**不存在**。

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.storage.*

val fiveKb = 5 kilo bytes                 // KStorageUnitInstance (== 5000.bytes)
fiveKb.value                              // 5000.0

val big = 3.bytes
big.valueAs(KUnitPrefix.KILO with bytes)  // 0.003 (kB)

// 5 milli bytes                          // 无法编译：不提供缩小值的前缀
```

## 二进制（IEC）前缀

二进制前缀是 1024 的幂，使数值能够区分 1000（`kilo`）与 1024（`kibi`）。它们以 `infix` 构造函数
（`kibi`、`mebi`、`gibi`、`tebi`、`pebi`、`exbi`、`zebi`、`yobi`）以及通过 `KStorageBinaryPrefix` +
`with` 作为 `valueAs`/`toString` 目标的形式提供。

```kotlin
import org.pcsoft.framework.kunit.storage.*

(1 kilo bytes).value                                  // 1000.0  (decimal)
(1 kibi bytes).value                                  // 1024.0  (binary)
(1 mega bytes).value                                  // 1_000_000.0
(1 mebi bytes).value                                  // 1_048_576.0

val file = 4 mebi bytes
file.valueAs(KStorageBinaryPrefix.KIBI with bytes)    // 4096.0 (KiB)
file.toString(KStorageBinaryPrefix.MEBI with bytes)   // "4.0 MiB"
```

| 二进制前缀 | 枚举值 | 符号 | `infix` | 系数 |
|---|---|---|---:|---:|
| Kibi | `KStorageBinaryPrefix.KIBI` | `Ki` | `kibi` | 1024 |
| Mebi | `KStorageBinaryPrefix.MEBI` | `Mi` | `mebi` | 1024² |
| Gibi | `KStorageBinaryPrefix.GIBI` | `Gi` | `gibi` | 1024³ |
| Tebi | `KStorageBinaryPrefix.TEBI` | `Ti` | `tebi` | 1024⁴ |
| Pebi | `KStorageBinaryPrefix.PEBI` | `Pi` | `pebi` | 1024⁵ |
| Exbi | `KStorageBinaryPrefix.EXBI` | `Ei` | `exbi` | 1024⁶ |
| Zebi | `KStorageBinaryPrefix.ZEBI` | `Zi` | `zebi` | 1024⁷ |
| Yobi | `KStorageBinaryPrefix.YOBI` | `Yi` | `yobi` | 1024⁸ |

## 与其他单位混合

存储值与时间结合，通过混合引擎形成数据速率（`byte·second⁻¹`），并可再次分解:

```kotlin
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds

val rate = 1000.bytes.toUnit() / 1.seconds.toUnit()   // 1000 B/s
val amount = (rate * 60.seconds.toUnit()).toStorage() // 60000 B
amount.valueAs(KStorageBinaryPrefix.KIBI with bytes)  // ≈ 58.59 (KiB)
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.storage.*

1024.bytes.toString()                                   // "1024.0 B" (base unit representation)
5.bits.toString(bits)                                   // "5.0 bit"
2048.bytes.toString(KStorageBinaryPrefix.KIBI with bytes) // "2.0 KiB"
```
