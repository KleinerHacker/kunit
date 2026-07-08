# 数据传输率

包: `org.pcsoft.framework.kunit.datarate`
基本单位: **字节每秒**(`KDataRateUnit.BASE == KDataRateUnit.BYTES_PER_SECOND`)

数据传输率是**构造**单位(继[速度](speed.md)之后的第二个): 它不是单一的"真实"量,而是一个合成量
`storage · time⁻¹`(`B/s`)。因此 `KDataRateUnitInstance` 封装了一个恰好包含两个项的 `KMixedUnitInstance` ——
指数为 `+1` 的 `KStorageUnit.BASE`(字节)和指数为 `-1` 的 `KTimeUnit.BASE`(秒)。无论从哪种单位或存储/时间
组合创建,值始终归一化为字节每秒后存储。

## 构建数据传输率

数据传输率以**存储每时间表达式**构建,例如 `100 of bytes / seconds`、`5 of mega.bytes / seconds` 或
`10 of kibi.bytes / seconds` —— 每个都生成 `KDataRateUnitInstance`。用任意存储每时间模板读回
(`r into (bits / seconds)`)。刻意**没有**像 `bytesPerSecond` 这样拼写出来的复合令牌(它们正是
`bytes / seconds`)。

基本单位: 与存储组一致,是每秒*字节*。网络原生的 bit/s(`bps`)为 `0.125 B/s`;"兆比特每秒"是
`1 of mega.bits / seconds`。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

val r = 100 of bytes / seconds
r.value                  // 100.0(归一化为 B/s)
r into (bits / seconds)  // 800.0(以 bit/s 读回)
```

## 用核心单位(存储和时间)计算

数据传输率*就是*存储量除以时间。用普通的 `*` 和 `/` 在三个量 —— 存储、时间和数据传输率 —— 之间移动;每个
结果都是**强类型**的。

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `storage / time` | `KDataRateUnitInstance` | 传输率 = 量 / 时间 |
| `data rate * time` | `KStorageUnitInstance` | 量 = 传输率 × 时间 |
| `time * data rate` | `KStorageUnitInstance` | 量(可交换) |
| `storage / data rate` | `KTimeUnitInstance` | 时间 = 量 / 传输率 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.datarate.*

// --- 核心单位 -> 数据传输率 --------------------------------------------
val r = (100 of bytes) / (10 of seconds)   // KDataRateUnitInstance(无需 .toDataRate()!)
r.value                  // 10.0(B/s)
r into (bits / seconds)  // 80.0

// 带前缀的分子,无括号:
val download = 5 of mega.bytes / seconds   // KDataRateUnitInstance(5 MB/s)

// --- 数据传输率 -> 存储(乘以时间) --------------------------
val amount = r * (60 of seconds)   // KStorageUnitInstance
amount into bytes     // 600.0
amount into bits      // 4800.0
(60 of seconds) * r   // 相同结果(可交换)

// --- 数据传输率 -> 时间(用存储量除以它) ------------------
val time = (600 of bytes) / r      // KTimeUnitInstance
time into minutes     // 1.0
```

!!! warning "只有*纯*存储 / 时间形态才是数据传输率"
    `KMixedUnitInstance.toDataRate()` 要求恰好一个指数为 `+1` 的存储项和一个指数为 `-1` 的时间项。`B²`(存储
    平方)、`B·s⁻²` 或 `B·s` 形态不是数据传输率 —— 转换会抛出 `IllegalStateException`。同样,`storage + data rate`
    (不同维度)是编译错误。

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

// + / - : 同组,基于字节和基于比特的传输率间自动转换
val a = (1 of bytes / seconds) + (8 of bits / seconds)   // KDataRateUnitInstance, 2 B/s
val b = (2 of bytes / seconds) - (8 of bits / seconds)   // 1 B/s

// 比较(按归一化的 B/s 值)
(1 of bytes / seconds) > (4 of bits / seconds)           // true
(1 of bytes / seconds) == (8 of bits / seconds)          // true

// 两个数据传输率之间的 * / / 会逃逸为 KMixedUnitInstance(不再是纯传输率)
val squared = (10 of bytes / seconds) * (2 of bytes / seconds) // KMixedUnitInstance, [B^2, s^-2]
```

## SI 和二进制(IEC)前缀

数据传输率组沿用[存储](storage.md)组的前缀策略(其分子是存储量): 分子使用**增大** SI 构建器(`kilo`、`mega`
等)或**二进制**构建器(`kibi`、`mebi` 等);缩小构建器没有 `bytes`/`bits` 属性,因此 `milli.bytes / seconds`
无法编译。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

// 十进制 vs 二进制: 1000(kilo)!= 1024(kibi)
(1 of kilo.bytes / seconds).value // 1000.0
(1 of kibi.bytes / seconds).value // 1024.0

// 用存储每时间模板读回值
val r = 4096 of bytes / seconds
r into (kilo.bytes / seconds)  // 4.096(kB/s)
r into (kibi.bytes / seconds)  // 4.0  (KiB/s)
```

## toString 格式化

只存在基本单位的 `toString()`;通过 `into` 格式化特定单位:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.storage.kibi
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

(10 of bytes / seconds).toString()  // "10.0 B/s"(基本单位)
"${(4096 of bytes / seconds) into (kibi.bytes / seconds)} KiB/s" // "4.0 KiB/s"
```
