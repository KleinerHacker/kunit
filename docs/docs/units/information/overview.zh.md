# 信息技术 — 概述

包：`org.pcsoft.framework.kunit.storage`、`…datarate`

信息技术处理**数字数据量**及其移动的速度。KUnit 将存储的量建模为**原生**基本量(存储,以字节计),
将吞吐量建模为由它**构造**的量(数据速率 = 存储 ÷ 时间),从而把"这个下载要多久?"这类日常问题变成
类型化的表达式。

## 本主题的单位

| 单位 | 类型 | 基准单位 | 页面 |
|---|---|---|---|
| 存储 | 原生 | 字节(`B`) | [存储](storage.md) |
| 数据速率 | 构造 | 字节每秒(`B/s`) | [数据速率](datarate.md) |

两组共享相同的前缀策略:**无递减前缀**(比特的小数部分没有意义),并且除十进制 SI 前缀
(`kilo` = 1000)外,还有第二套**二进制(IEC)**系列(`kibi` = 1024)。

## 各量之间的关系

| 表达式 | 结果 | 公式 |
|---|---|---|
| `storage / time` | 数据速率 | `r = 量 / t` |
| `data rate * time` | 存储 | `量 = r · t` |
| `time * data rate` | 存储 | `量 = r · t`(可交换) |
| `storage / data rate` | 时间 | `t = 量 / r` |

## 实例 —— 下载时间

通过 **10 MB/s** 的链路下载一个 **500 MB** 的文件。时间为 `t = 量 / 速率`,再将速率乘以该时间即可重现
数据量 `量 = r · t`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.datarate.*

val amount = 500 of mega.bytes
val rate   = 10 of mega.bytes / seconds        // KDataRateUnitInstance,10 MB/s

val time = amount / rate                        // KTimeUnitInstance
time into seconds                               // 50.0(s)

val transferred = rate * (50 of seconds)        // KStorageUnitInstance
transferred into mega.bytes                     // 500.0(MB)
```

## 实例 —— 十进制 vs 二进制

同一数值的量在十进制(`kB`)与二进制(`KiB`)模板下读数不同 —— 1000 对 1024:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.*

val size = 4096 of bytes
size into kilo.bytes    // 4.096(kB,十进制 1000)
size into kibi.bytes    // 4.0  (KiB,二进制 1024)
```

## 输出值(`toString`)

`toString()` 以该组的**基准单位**(值 + 符号)输出值;对于其他单位,在字符串模板中用 `into` 读取并自行
附加符号:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

val r = (10 of bytes) / (1 of seconds)   // KDataRateUnitInstance
r.toString()                             // "10.0 B/s"(基准单位)
"${(4096 of bytes / seconds) into (kibi.bytes / seconds)} KiB/s" // "4.0 KiB/s"
```

## 记法

下表以数学表记与 KUnit 的 Kotlin 表记对照本领域的核心关系。指数使用 Unicode 上标(`⁻¹`),`·` 表示乘法,
`/` 表示分数。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `r = 量 / t` | `(500 of mega.bytes) / (50 of seconds)` | 量÷时间得数据速率 |
| `量 = r · t` | `rate * (50 of seconds)` | 速率×时间得数据量 |
| `t = 量 / r` | `amount / rate` | 量÷速率得时间 |
| `1 kB = 1000 B` | `kilo.bytes` | 十进制前缀字节 |
| `1 KiB = 1024 B` | `kibi.bytes` | 二进制前缀字节 |

## 后续阅读

* [存储](storage.md) —— 原生字节组,十进制与二进制前缀。
* [数据速率](datarate.md) —— 存储 ÷ 时间,以及存储↔时间↔速率运算符。
