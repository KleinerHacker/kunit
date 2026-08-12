# 加加速度(急动度)

包: `org.pcsoft.framework.kunit.kinematic.jerk`
基本单位: **米每秒三次方**(`KJerkUnit.BASE == KJerkUnit.METER_PER_SECOND_CUBED`)

类型: **构造单位**

加加速度(急动度) `j` 是**加速度**的变化率: `j = Δa / t`。这正是乘坐舒适度标准实际限制的量 —
电梯或列车可以剧烈加速,但加速度不能骤然变化,否则乘客会猛地一颠。舒适度限值大约在 0.5 m/s³ 左右。

其规范的基础量纲标准形式为 `length · time⁻³`。

## 命名单位

| 单位             | 符号     |                          令牌 | 1 单位对应的 m/s³ |
|------------------|----------|-------------------------------:|-------------------:|
| 米每秒三次方     | `m/s^3`  |       `metersPerSecondCubed`   |                1.0 |
| 标准重力每秒     | `g/s`    | `standardGravitiesPerSecond`   |            9.80665 |
| 英尺每秒三次方   | `ft/s^3` |          `feetPerSecondCubed`  |             0.3048 |

所有令牌都支持所有 SI 前缀(`milli.metersPerSecondCubed` 等)。

## 分解方式

该分组只有一种分解方式,其两种形式都会产生相同的、类型化且值相等的实例:

| 形式                | 表达式                                                             |
|---------------------|----------------------------------------------------------------------|
| 类型化运算符        | `acceleration / time`                                             |
| 原生形式(`toX()`) | `(acceleration.toUnit() / (2 of seconds).toUnit()).toJerk()`      |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.acceleration.gals
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.jerk.*

val a = 120 of gals                    // 1.2 m/s²(1 Gal = 0.01 m/s²)

val typed = a / (2 of seconds)
val native = (a.toUnit() / (2 of seconds).toUnit()).toJerk()

typed == native                        // true
typed into metersPerSecondCubed        // 0.6
```

## 与该分组的计算

| 表达式                 | 结果类型                        | 含义                         |
|------------------------|-----------------------------------|-------------------------------|
| `acceleration / time`  | `KJerkUnitInstance`               | `j = Δa / t`                  |
| `jerk * time`          | `KAccelerationUnitInstance`       | 累积起来的加速度               |
| `acceleration / jerk`  | `KTimeUnitInstance`               | 变化坡道所需的时间             |

## 现实示例 — 舒适限值内的电梯加速坡道

一部电梯要在不超过 **0.5 m/s³** 加加速度的情况下达到 **1 m/s²** 的加速度。坡道需要多长时间?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.acceleration.gals
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.jerk.*

val target = 100 of gals                        // 1 m/s²
val comfort = 0.5 of metersPerSecondCubed

val ramp = target / comfort                     // KTimeUnitInstance
ramp into seconds                                // 2.0 s

// 反过来:1 秒的坡道会带来多大的加加速度?
val harsh = target / (1 of seconds)
harsh into metersPerSecondCubed                  // 1.0 — 是舒适限值的两倍
```

## 值语义

`equals`/`hashCode` 比较**归一化后的 m/s³ 值**,因此
`(1 of metersPerSecondCubed) == (1000 of milli.metersPerSecondCubed)`。`toString()` 以基本单位渲染值:
`"0.6 m/s^3"`。

## 参见

* [加速度](acceleration.zh.md) — 本单位所描述的是该量的变化率。
* [速度](speed.zh.md) 和 [距离](distance.zh.md) — 运动链的其余部分。
* [运动学概述](overview.zh.md)
