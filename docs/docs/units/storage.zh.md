# 存储

包: `org.pcsoft.framework.kunit.storage`
基本单位: **字节**(`KStorageUnit.BASE == KStorageUnit.BYTE`)

存储组建模数字数据量。它是一个**简单的一维**组(没有像距离组那样的指数特化子类型,也没有像时间组那样的
`Duration` 支撑): `KStorageUnitInstance` 封装单个 `KStorageUnit.BASE`(字节)项,始终归一化为字节后存储。

有两点让这个组与众不同:

* **无缩小前缀。** 比特的分数不是有意义的数据量,因此缩小 SI 前缀(`deci`、`centi`、`milli` 等 —— 系数 `< 1`)
  对 `bytes`/`bits` **不可用**。写 `milli.bytes` 是**编译错误**而非运行时失败: `bytes`/`bits` 属性只挂在增大
  SI 构建器(`KAugmentingPrefixBuilder`)和二进制构建器上,从不挂在缩小构建器上。
* **二进制(IEC)前缀。** 除了十进制 SI 构建器(`kilo` = 1000)之外,还有第二套二进制构建器体系(`kibi` = 1024、
  `mebi` = 1024² 等),使得值可以区分十进制步长 1000 和二进制步长 1024。

## 单位

| 单位 | Enum 值 | 符号 | 令牌 | 1 单位对应字节 |
|---|---|---|---:|---:|
| 字节 | `KStorageUnit.BYTE` | `B` | `bytes` | 1.0 |
| 比特 | `KStorageUnit.BIT` | `bit` | `bits` | 0.125 |

一字节等于八比特。每个 `令牌` 都是值为 1 的 `KStorageUnitInstance`,用于 `of`(构建)和 `into`(读取)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*

val size = 5 of bytes
size.value          // 5.0(归一化为字节)
size into bits      // 40.0(以比特读回)
(1 of bytes) into bits   // 8.0
(8 of bits) into bytes   // 1.0
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds

// + / - : 同组,比特与字节间自动转换
val a = (1 of bytes) + (8 of bits)   // KStorageUnitInstance: 2.0 B
val b = (4 of bytes) - (16 of bits)  // KStorageUnitInstance: 2.0 B

// 比较
(1 of bytes) == (8 of bits)          // true(归一化后的量相同)
(2 of bytes) > (1 of bytes)          // true

// storage / time 是带类型的数据传输率(见数据传输率页面)
val rate = (1000 of bytes) / (2 of seconds)  // KDataRateUnitInstance: 500 B/s
```

### 比较与相等性

`==`、`!=`、`<`、`<=`、`>`、`>=` 比较两个 `KStorageUnitInstance` 值的归一化 `value`(字节)。`equals` 按归一化
后的量比较,因此 `(1 of bytes) == (8 of bits)`。

## 用 `pow` 求幂

用中缀 `pow` 运算符将值提升到整数次幂(Kotlin 没有可重载的 `^`)。对于存储组,`pow` 返回通用的
`KMixedUnitInstance`(存储没有带维度的幂类型):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.storage.*

val squared = (2 of bytes) pow 2     // KMixedUnitInstance: 4.0 B²
```

## 十进制 SI 前缀

任何存储单位都可以通过属性访问与**增大**(大于 1)SI 前缀构建器(`deca`、`hecto`、`kilo`、`mega`、`giga`、
`tera`、`peta`、`exa`、`zetta`、`yotta`、`ronna`、`quetta`)组合。缩小构建器(`deci` 及以下)**没有**
`bytes`/`bits` 属性,因此 `milli.bytes` 无法编译。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.*

val fiveKb = 5 of kilo.bytes         // KStorageUnitInstance(== 5000 B)
fiveKb.value                         // 5000.0

(3 of bytes) into kilo.bytes         // 0.003(kB)

// 5 of milli.bytes                  // 无法编译: 缩小构建器上没有 `bytes`
```

## 二进制(IEC)前缀

二进制前缀构建器是 1024 的幂,让值可以区分 1000(`kilo`)和 1024(`kibi`): `kibi`、`mebi`、`gibi`、`tebi`、
`pebi`、`exbi`、`zebi`、`yobi`。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.*

(1 of kilo.bytes).value   // 1000.0     (十进制)
(1 of kibi.bytes).value   // 1024.0     (二进制)
(1 of mega.bytes).value   // 1_000_000.0
(1 of mebi.bytes).value   // 1_048_576.0

val file = 4 of mebi.bytes
file into kibi.bytes      // 4096.0(KiB)
```

| 二进制构建器 | 符号 | 1 单位(字节) |
|---|---|---:|
| `kibi` | `Ki` | 1024 |
| `mebi` | `Mi` | 1024² |
| `gibi` | `Gi` | 1024³ |
| `tebi` | `Ti` | 1024⁴ |
| `pebi` | `Pi` | 1024⁵ |
| `exbi` | `Ei` | 1024⁶ |
| `zebi` | `Zi` | 1024⁷ |
| `yobi` | `Yi` | 1024⁸ |

## 与其他单位混合

存储值与时间结合形成数据传输率(`byte·second⁻¹`),并可以再分解回来:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds

val rate = (1000 of bytes) / (1 of seconds)  // 1000 B/s(带类型的 KDataRateUnitInstance)
val amount = rate * (60 of seconds)          // 60000 B(带类型的 KStorageUnitInstance)
amount into kibi.bytes                        // ≈ 58.59(KiB)
```

## toString 格式化

只存在基本单位的 `toString()`;通过 `into` 格式化特定单位:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*

(1024 of bytes).toString()               // "1024.0 B"(基本单位表示)
"${(5 of bits) into bits} bit"           // "5.0 bit"
"${(2048 of bytes) into kibi.bytes} KiB" // "2.0 KiB"
```
