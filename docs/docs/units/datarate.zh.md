# 数据速率

包: `org.pcsoft.framework.kunit.datarate`
基本单位: **字节每秒** (`KDataRateUnit.BASE == KDataRateUnit.BYTES_PER_SECOND`)

数据速率是（继[速度](speed.md)之后的）第二个**构造(constructed)**单位。它不是单一的“真实”物理量，而是
`存储 · 时间⁻¹` (`B/s`) 的组合。因此 `KDataRateUnitInstance` 封装了一个恰好两项的
`KMixedUnitInstance` - 一个指数 `+1` 的 `KStorageUnit.BASE`（字节）和一个指数 `-1` 的
`KTimeUnit.BASE`（秒）。无论使用哪个单位、前缀或存储/时间组合创建，其值始终以字节每秒归一化存储。

## 单位

| 单位 | Enum 值 | 符号 | 创建器 | 1 单位 (B/s) |
|---|---|---|---:|---:|
| 字节每秒 | `KDataRateUnit.BYTES_PER_SECOND` | `B/s` | `Number.bytesPerSecond` | 1.0 |
| 比特每秒 | `KDataRateUnit.BITS_PER_SECOND` | `bit/s` | `Number.bitsPerSecond` | 0.125 |

两个单位都有对应的 bare `val` 别名，可用作 `valueAs`/`toString` 目标或前缀 `infix` 函数的 `unit`
参数: `bytesPerSecond`, `bitsPerSecond`。

> **以字节为基准。** 基本单位是字节每秒，与存储组（其基准为字节）保持一致。网络中常见的比特每秒
> (`bps`) 为 `0.125 B/s`。所有更大的单位（kB/s、MB/s、Mbit/s、KiB/s、…）都来自下面的前缀 DSL，而不是
> 专门的枚举值。

```kotlin
import org.pcsoft.framework.kunit.datarate.*

val r = 100.bytesPerSecond
r.value                                     // 100.0 (归一化为 B/s)
r.valueAs(KDataRateUnit.BITS_PER_SECOND)    // 800.0 (以 bit/s 读回)
r.valueAs(bitsPerSecond)                     // 800.0 (通过 bare 别名)
```

## 使用核心单位（存储 & 时间）计算

这正是构造单位的意义所在。数据速率*就是*存储量除以时间。KUnit 让你用普通的 `*` 和 `/` 在存储、时间和
数据速率三个量之间转换，每个结果都是**强类型**的。你无需自己构建或拆解原始的 `KMixedUnitInstance`。

四种合法组合及其结果类型:

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `storage / time` | `KDataRateUnitInstance` | 速率 = 量 / 时长 |
| `data rate * time` | `KStorageUnitInstance` | 量 = 速率 × 时长 |
| `time * data rate` | `KStorageUnitInstance` | 量（可交换） |
| `storage / data rate` | `KTimeUnitInstance` | 时长 = 量 / 速率 |

```kotlin
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.datarate.*

// --- 核心单位 -> 数据速率 -----------------------------------------------
val r = 100.bytes / 10.seconds            // KDataRateUnitInstance（无需 .toDataRate()！）
r.value                                     // 10.0 (B/s)
r.valueAs(KDataRateUnit.BITS_PER_SECOND)    // 80.0

// 赋值目标类型不会转换任何东西 - 运算符已经返回 KDataRateUnitInstance。
val explicit: KDataRateUnitInstance = 100.bytes / 10.seconds

// --- 数据速率 -> 存储（乘以时间） --------------------------------------
val amount = r * 60.seconds               // KStorageUnitInstance
amount.value                                // 600.0 (B)
amount.valueAs(bytes)                        // 600.0
amount.valueAs(bits)                         // 4800.0（以任意存储单位读回）
60.seconds * r                            // 相同结果（可交换）

// --- 数据速率 -> 时间（用存储量去除） ----------------------------------
val time = 600.bytes / r                  // KTimeUnitInstance
time.value                                  // 60.0 (s)
time.valueAs(KTimeUnit.MINUTE)              // 1.0
```

!!! warning "只有*纯粹的*存储 / 时间形态才是数据速率"
    `KMixedUnitInstance.toDataRate()` 要求恰好一个指数 `+1` 的存储项和一个指数 `-1` 的时间项。`B²`
    （存储平方）、`B·s⁻²` 或 `B·s` 形态都不是数据速率 - 转换会抛出 `IllegalStateException` 而不是悄悄
    返回错误值。同样，`storage + data rate`（维度不同）是编译错误。

## 运算符

```kotlin
import org.pcsoft.framework.kunit.datarate.*

// + / - ：同组，不同数据速率单位间自动转换
val a = 1.bytesPerSecond + 8.bitsPerSecond   // KDataRateUnitInstance, 2 B/s
val b = 2.bytesPerSecond - 8.bitsPerSecond   // 1 B/s

// 比较（按归一化的 B/s 值）
1.bytesPerSecond > 4.bitsPerSecond           // true  (1 B/s > 0.5 B/s)
1.bytesPerSecond == 8.bitsPerSecond          // true  (归一化值相同)

// 两个数据速率间的 * / / 会退回到 KMixedUnitInstance（不再是纯速率）
val squared = 10.bytesPerSecond * 2.bytesPerSecond // KMixedUnitInstance, units=[B^2, s^-2]
```

## 比较与相等

`==`、`!=`、`<`、`<=`、`>`、`>=` 比较两个 `KDataRateUnitInstance` 的归一化 `value`（字节每秒）。由于
数据速率始终具有相同的维度，因此无需指数检查。

## SI 前缀与二进制 (IEC) 前缀

数据速率组沿用[存储](storage.md)组的前缀策略（其分子是存储量）:

* 仅提供**非缩小的**十进制 SI 前缀（`deca` 及以上，系数 >= 1）。缩小的前缀（`deci`、`centi`、`milli`、
  …）**不存在** - `5 milli bytesPerSecond` 是**编译错误**，而非运行时失败。
* 除十进制 SI 前缀外，还提供**二进制 IEC 前缀**（`kibi`、`mebi`、`gibi`、…，1024 的幂，复用自
  `KStorageBinaryPrefix`），因此速率可以区分 1000 (`kilo`) 与 1024 (`kibi`)。

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.storage.KStorageBinaryPrefix
import org.pcsoft.framework.kunit.storage.with
import org.pcsoft.framework.kunit.datarate.*

// 构造: "5 mega bytesPerSecond" -> KDataRateUnitInstance（直接，== 5_000_000.bytesPerSecond）
val download = 5 mega bytesPerSecond
download.value // 5000000.0

// 十进制 vs 二进制: 1000 (kilo) != 1024 (kibi)
(1 kilo bytesPerSecond).value // 1000.0
(1 kibi bytesPerSecond).value // 1024.0

// 使用缩放后的整体速率目标读回值
val r = 4096.bytesPerSecond
r.valueAs(KUnitPrefix.KILO with bytesPerSecond)              // 4.096  (kB/s)
r.valueAs(KStorageBinaryPrefix.KIBI with bytesPerSecond)     // 4.0    (KiB/s)
```

你也可以将数据速率读回为显式的**存储-每-时间对**（两个目标）:

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.storage.KStorageUnit
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.datarate.*

val r = 5000.bytesPerSecond
r.valueAs(KUnitPrefix.KILO with KStorageUnit.BYTE, KTimeUnit.SECOND)   // 5.0 (每秒 kB)
r.toString(KUnitPrefix.KILO with KStorageUnit.BYTE, KTimeUnit.SECOND)  // "5.0 kB*s^-1"
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.storage.KStorageBinaryPrefix
import org.pcsoft.framework.kunit.storage.with
import org.pcsoft.framework.kunit.datarate.*

10.bytesPerSecond.toString()                                    // "10.0 B/s"（基本单位）
(100.bytes / 10.seconds).toString(KDataRateUnit.BITS_PER_SECOND) // "80.0 bit/s"
4096.bytesPerSecond.toString(KStorageBinaryPrefix.KIBI with bytesPerSecond) // "4.0 KiB/s"
```
