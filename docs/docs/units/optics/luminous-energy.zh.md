# 发光能量

包：`org.pcsoft.framework.kunit.optic.luminousenergy`
基本单位：**流明秒**（`KLuminousEnergyUnit.BASE == KLuminousEnergyUnit.LUMEN_SECOND`）

类型：**构造单位**

发光能量`Q`是**随时间累积**的光通量：`Q = Φ · t`。光通量说明灯*此刻*有多亮，
而发光能量说明它总共已经发出了多少光——这是灯具寿命额定值和摄影闪光灯能量背后的量。
流明秒又称为**塔尔博特**。

其规范的基础量纲标准形式是`luminousIntensity¹ · solidAngle¹ · time¹`。

## 单位

| 单位         | 枚举值                          | 符号 |          令牌 | 1单位对应lm·s |
|--------------|-------------------------------------|--------|---------------:|---------------:|
| 流明秒 | `KLuminousEnergyUnit.LUMEN_SECOND`  | `lm*s` | `lumenSeconds` |            1.0 |
| 塔尔博特       | `KLuminousEnergyUnit.LUMEN_SECOND`  | `lm*s` |      `talbots` |            1.0 |
| 流明时   | `KLuminousEnergyUnit.LUMEN_HOUR`    | `lm*h` |    `lumenHours` |           3600 |

`talbots`是基本单位的另一种写法，不是独立的单位。所有令牌都支持所有SI词头
（如`kilo.lumenHours`、`milli.lumenSeconds`等）。

## 分解

该组有一种分解方式，其两种形式都会产生相同的、类型化且值相等的实例：

| 形式             | 表达式                                                                  |
|------------------|-------------------------------------------------------------------------|
| 类型化运算符   | `luminousFlux * time`                                                       |
| 原生形式（`toX()`） | `((800 of lumens).toUnit() * (5 of seconds).toUnit()).toLuminousEnergy()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.luminousenergy.*

val typed = (800 of lumens) * (5 of seconds)
val native = ((800 of lumens).toUnit() * (5 of seconds).toUnit()).toLuminousEnergy()

typed == native            // true
typed into lumenSeconds    // 4000.0
```

## 使用该组进行计算

| 表达式                       | 结果类型                   | 含义                       |
|-----------------------------------|--------------------------------|--------------------------------|
| `luminousFlux * time`            | `KLuminousEnergyUnitInstance` | `Q = Φ · t`                   |
| `luminousEnergy / time`          | `KLuminousFluxUnitInstance`   | 平均光通量              |
| `luminousEnergy / luminousFlux`  | `KTimeUnitInstance`           | 光通量发出的持续时间 |

## 实例——灯泡整个寿命期间发出的光量

一个800 lm的LED灯泡额定寿命为**25000小时**。它一生能发出的总光量为：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.luminousenergy.*

val q = (800 of lumens) * (25_000 of hours)
q into lumenHours          // 20_000_000.0
q into mega.lumenHours     // 20.0

// 如果每天使用3小时，能用多少天？
val perDay = (800 of lumens) * (3 of hours)
q into lumenHours / (perDay into lumenHours)   // ≈ 8333天
```

## 值语义

`equals`/`hashCode`比较**归一化后的lm·s值**，因此`(1 of lumenHours) == (3600 of lumenSeconds)`。
`toString()`以基本单位渲染该值：`"3600.0 lm*s"`。

## 另请参阅

* [光通量](luminous-flux.zh.md) —— 该量累积所依据的速率。
* [光照曝光量](luminous-exposure.zh.md) —— 针对照度而非光通量的相同思路。
* [光学概述](overview.zh.md)
</content>
