# 时间

包: `org.pcsoft.framework.kunit.time`
基本单位: **秒**(`KTimeUnit.BASE == KTimeUnit.SECOND`)

`KTimeUnitInstance` 是对 [`java.time.Duration`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/Duration.html)
的 100 % 封装: `Duration` 是唯一的真实来源(纳秒精确),并且完整的 `Duration` API 都被转发。在此之上,它
提供与其他所有"纯"单位包装器相同的表面(`value`/`+`/`-`/`*`/`/`/`toString`/`toUnit` 以及 `of`/`into` 动词),
因此时间值可以接入通用的混合单位引擎(例如 `length / time` = 速度)。值始终归一化为秒后存储。

由于 `Duration` 始终只表示纯粹的持续时间,时间值的指数永远为 1 —— 没有 时间² 或 1/时间 的包装器(乘法/除法
会像长度一样"逃逸"为原始的 `KMixedUnitInstance`)。因此 `KMixedUnitInstance.toTime()` 只接受**指数为 1 的**
单个 `KTimeUnit` 项。

## 单位

| 单位 | Enum 值 | 符号 | 令牌 | 1 单位对应秒数 |
|---|---|---|---:|---:|
| 秒 | `KTimeUnit.SECOND` | `s` | `seconds` | 1.0 |
| 分 | `KTimeUnit.MINUTE` | `min` | `minutes` | 60.0 |
| 时 | `KTimeUnit.HOUR` | `h` | `hours` | 3600.0 |
| 日 | `KTimeUnit.DAY` | `d` | `days` | 86 400.0 |

仅建模物理时间尺度;基于日历的单位(周、年)被有意省略,因为它们由日历而非固定的物理量定义。每个 `令牌` 都是
值为 1 的 `KTimeUnitInstance`,用于 `of`(构建)和 `into`(读取)。

亚秒尺度(毫秒、微秒、纳秒等)**不是**专用单位 —— 它们通过 `seconds` 上的 SI 前缀构建器通用地表达(见下方
[SI 前缀](#si))。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.time.*

val t = 2 of hours
t.value          // 7200.0(归一化为秒)
t into hours     // 2.0(以时读回)
t into minutes   // 120.0
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.*

// + / - : 同组,不同时间单位间自动转换(精确的 Duration 运算)
val a = (1 of hours) + (30 of minutes)   // KTimeUnitInstance, 归一化为秒(5400.0)
val b = (2 of hours) - (30 of minutes)

// 比较
(2 of hours) > (90 of minutes)           // true
(1 of hours) == (60 of minutes)          // true(归一化后的值相同)

// * / / : 始终允许,生成带有新指数的 KMixedUnitInstance
val secondsSquared = (3 of seconds) * (4 of seconds)   // KMixedUnitInstance: value=12.0, units=[SECOND^2]
val ratio = (10 of seconds) / (2 of seconds)           // KMixedUnitInstance: value=5.0, 无量纲
```

## 比较与相等性

`==`、`!=`、`<`、`<=`、`>`、`>=` 通过底层的 `Duration`(纳秒精确)比较两个 `KTimeUnitInstance`。由于时间值
的指数始终为 1,不会出现像长度的面积/体积那样的指数不匹配错误。

## `java.time.Duration` 包装器

`KTimeUnitInstance` 是对 `Duration` 的即插即用外观: 可以获取被包装的 `Duration`、包装一个已有的 `Duration`,
或直接使用被转发的 `Duration` 方法(返回 `Duration` 的方法返回 `KTimeUnitInstance`;查询方法直接透传)。

```kotlin
import java.time.Duration
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.time.*

val t = 90 of minutes
t.toDuration()                  // PT1H30M
Duration.ofMinutes(90).toTime() into hours // 1.5

// 转发的修改方法返回 KTimeUnitInstance
t.plusHours(1) into hours       // 2.5
t.negated().isNegative()        // true

// 转发的查询方法直接透传
t.toHours()             // 1
t.toMinutesPart()       // 30
t.dividedBy(30 of minutes) // 3
```

## <a name="si"></a>SI 前缀

任何时间单位都可以通过属性访问与 24 个 SI 前缀**构建器**(`kilo`、`milli`、`micro` 等,根包)中的任意一个
组合,生成用于 `of`/`into` 的值为 1 的模板。这就是表达亚秒尺度的方式。请注意 `Duration` 支撑限制了可表示的
范围(见下方注记),因此对多秒基底应用极端前缀是无法表示的:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.time.*

// 构建: "5 of milli.seconds" -> KTimeUnitInstance
val fiveMillis = 5 of milli.seconds
fiveMillis.value // 0.005(秒)

// 用带前缀的单位读回值
val t = 2 of hours
t into milli.seconds  // 7 200 000.0(ms)
```

!!! note "Duration 范围"
    由于值由 `java.time.Duration`(整数秒存储为 `Long`,纳秒分辨率)支撑,`KTimeUnitInstance` 只能忠实地
    表示大约 `[1 ns, Long.MAX 秒]`(≈ 2920 亿年)范围内的量级。像对日应用 `quetta` 这样的极端前缀会超出此
    范围,而亚纳秒值会舍入为零。通用的 `KMixedUnitInstance`/前缀层本身基于 `Double`,不受影响 —— 只有转换为
    Duration 支撑的包装器时才受范围限制。

## toString 格式化

只存在基本单位的 `toString()`;通过 `into` 格式化特定单位:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.time.*

(2 of hours).toString()          // "7200.0 s"(基本单位表示)
"${(2 of hours) into hours} h"   // "2.0 h"
"${(2 of hours) into minutes} min" // "120.0 min"
```

## 与其他单位混合

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

val speed = (10 of meters) / (1 of seconds)  // KSpeedUnitInstance
speed into (kilo.meters / hours)             // 36.0(km/h)

// 速度再乘以时间可还原出纯长度
val distance = speed * (2 of seconds)
distance into meters // 20.0
```

**没有**专用跨组运算符的组的两个纯单位(例如 `(2 of hours) * (5 of bytes)`)直接结合为
`KMixedUnitInstance`,无需 `.toUnit()`。
