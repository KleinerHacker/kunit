# 时间

包: `org.pcsoft.framework.kunit.time`
基本单位: **秒** (`KTimeUnit.BASE == KTimeUnit.SECOND`)

`KTimeUnitInstance` 是对 [`java.time.Duration`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/Duration.html)
的 100% 封装：`Duration` 是唯一的数据来源（纳秒级精确），并转发完整的 `Duration` API。在此之上，它提供
与其他所有"纯"单位包装器相同的接口（`value`/`valueAs`/`+`/`-`/`*`/`/`/`toString`/`toKMixedUnitInstance`），
因此时间值可直接接入通用的混合单位引擎（例如 `length / time` = 速度）。值始终以秒为单位归一化存储。

由于 `Duration` 只表示一段普通的时长，时间值始终为指数 1 —— 没有 time² 或 1/time 包装器（乘法/除法会像
长度那样"逃逸"为原始的 `KMixedUnitInstance`）。因此 `KMixedUnitInstance.toKTimeUnit()` 只接受**指数为 1 的**单个
`KTimeUnit` 项。

## 单位

| 单位 | 枚举值 | 符号 | 构造器 | 1 单位 (秒) |
|---|---|---|---:|---:|
| 秒 | `KTimeUnit.SECOND` | `s` | `Number.seconds` | 1.0 |
| 分 | `KTimeUnit.MINUTE` | `min` | `Number.minutes` | 60.0 |
| 小时 | `KTimeUnit.HOUR` | `h` | `Number.hours` | 3600.0 |
| 天 | `KTimeUnit.DAY` | `d` | `Number.days` | 86 400.0 |

仅建模物理时间刻度；基于日历的单位（周、年）被有意省略，因为它们由日历定义，而非固定的物理量。

上述每个单位都有对应的裸 `val` 别名，可用作 `valueAs`/`toString` 的目标，或作为前缀 infix 函数的 `unit`
参数：`seconds`、`minutes`、`hours`、`days`。

毫秒、微秒、纳秒等亚秒级刻度**不是**专门的单位，而是通过对 `second` 施加 SI 前缀来通用地表达（见下方
[SI 前缀](#si)）。

```kotlin
import org.pcsoft.framework.kunit.time.*

val t = 2.hours
t.value                      // 7200.0 (归一化为秒)
t.valueAs(KTimeUnit.HOUR)    // 2.0 (以小时读回)
t.valueAs(minutes)           // 120.0
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.time.*

// + / - : 同一组，不同时间单位之间自动转换（精确的 Duration 运算）
val a = 1.hours + 30.minutes   // KTimeUnitInstance，归一化为秒 (5400.0)
val b = 2.hours - 30.minutes

// 比较
2.hours > 90.minutes            // true
1.hours == 60.minutes           // true (归一化值相同)

// * / / : 始终允许，生成具有新指数的 KMixedUnitInstance
val secondsSquared = 3.seconds * 4.seconds   // KMixedUnitInstance: value=12.0, units=[SECOND^2]
val ratio = 10.seconds / 2.seconds           // KMixedUnitInstance: value=5.0, 无量纲
```

## 比较与相等

`==`、`!=`、`<`、`<=`、`>`、`>=` 通过底层 `Duration`（纳秒级精确）比较两个 `KTimeUnitInstance`。由于时间
值始终为指数 1，不会像长度的面积/体积那样出现指数不匹配错误。

## `java.time.Duration` 包装器

`KTimeUnitInstance` 是 `Duration` 的即插即用外观：可以取出被包装的 `Duration`、包装一个已有的 `Duration`，
并直接使用转发的 `Duration` 方法（返回 `Duration` 的方法返回 `KTimeUnitInstance`，查询方法则原样透传）。

```kotlin
import java.time.Duration
import org.pcsoft.framework.kunit.time.*

val t = 90.minutes
t.toDuration()                       // PT1H30M
Duration.ofMinutes(90).toKTimeUnit() // KTimeUnitInstance, valueAs(HOUR) == 1.5

// 转发的修改方法返回 KTimeUnitInstance
t.plusHours(1).valueAs(KTimeUnit.HOUR) // 2.5
t.negated().isNegative()               // true

// 转发的查询方法原样透传
t.toHours()      // 1
t.toMinutesPart() // 30
t.dividedBy(30.minutes) // 3
```

## SI 前缀

任何 `KTimeUnit` 都可以与 24 个 SI 前缀（`KUnitPrefix`，根包，从 Quetta/Q 到 Quecto/q）中的任意一个结合，
使用每组各自的 infix 构造函数（直接返回具体单位）和 `with`（用于 valueAs/toString 目标）。亚秒（以及超天）刻度即由此表达。

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.time.*

// 构造: "5 milli seconds" -> KTimeUnitInstance (direct)
val fiveMillis = 5 milli seconds
fiveMillis.value // 0.005 (秒)

// 使用带前缀的目标读回值
val t = 2.hours
t.valueAs(KUnitPrefix.MILLI with KTimeUnit.SECOND)  // 7 200 000.0 (ms)
t.toString(KUnitPrefix.MILLI with KTimeUnit.SECOND) // "7200000.0 ms"
```

!!! note "Duration 范围"
    由于值以 `java.time.Duration`（整秒以 `Long` 存储，纳秒分辨率）为后盾，`KTimeUnitInstance` 只能精确
    表示大约 `[1 ns, Long.MAX 秒]`（≈ 2920 亿年）范围内的量级。对天施加 `quetta` 等极端前缀会超出该范围，
    而亚纳秒的值会舍入为零。通用的 `KMixedUnitInstance`/前缀层本身基于 `Double`，不受影响 —— 只有转换为
    Duration 后盾的包装器时才受范围限制。

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.time.*

2.hours.toString()               // "7200.0 s" (基本单位表示)
2.hours.toString(KTimeUnit.HOUR) // "2.0 h"
2.hours.toString(minutes)        // "120.0 min"
```

## 与其他单位混合

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.length.*
import org.pcsoft.framework.kunit.time.*

val speed = 10.meters / 1.seconds.toKMixedUnitInstance()          // KMixedUnitInstance, units=[METER^1, SECOND^-1]
speed.toString(KUnitPrefix.KILO with KLengthUnit.METER, KTimeUnit.HOUR) // "36.0 km*h^-1"

// 将速度再乘以时间可恢复为纯长度
val distance = speed * 2.seconds.toKMixedUnitInstance()
distance.toKLengthUnit().value // 20.0
```
