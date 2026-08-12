# 光照曝光量

包：`org.pcsoft.framework.kunit.optic.luminousexposure`
基本单位：**勒克斯秒**（`KLuminousExposureUnit.BASE == KLuminousExposureUnit.LUX_SECOND`）

类型：**构造单位**

光照曝光量`H`是**随时间累积**的照度：`H = E · t`。这是表面所接收到的*光剂量*——
博物馆保护人员用每年的勒克斯小时数来预算，以限制颜料褪色；这也是相机曝光值背后的量。

其规范的基础量纲标准形式是`luminousIntensity¹ · solidAngle¹ · distance⁻² · time¹`。

## 单位

| 单位       | 枚举值                          | 符号 |        令牌 | 1单位对应lx·s |
|------------|-------------------------------------|--------|-------------:|---------------:|
| 勒克斯秒 | `KLuminousExposureUnit.LUX_SECOND`  | `lx*s` | `luxSeconds` |            1.0 |
| 勒克斯时   | `KLuminousExposureUnit.LUX_HOUR`    | `lx*h` |   `luxHours` |           3600 |

所有令牌都支持所有SI词头（`kilo.luxHours`是年度光照剂量预算的常用单位）。

## 分解

该组有一种分解方式，其两种形式都会产生相同的、类型化且值相等的实例：

| 形式             | 表达式                                                                   |
|------------------|--------------------------------------------------------------------------|
| 类型化运算符   | `illuminance * time`                                                         |
| 原生形式（`toX()`） | `((50 of lux).toUnit() * (10 of seconds).toUnit()).toLuminousExposure()`     |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousexposure.*

val typed = (50 of lux) * (10 of seconds)
val native = ((50 of lux).toUnit() * (10 of seconds).toUnit()).toLuminousExposure()

typed == native          // true
typed into luxSeconds    // 500.0
```

## 使用该组进行计算

| 表达式                        | 结果类型                     | 含义                    |
|-----------------------------------|---------------------------------|-----------------------------|
| `illuminance * time`              | `KLuminousExposureUnitInstance` | `H = E · t`                |
| `luminousExposure / time`         | `KIlluminanceUnitInstance`      | 平均照度        |
| `luminousExposure / illuminance`  | `KTimeUnitInstance`             | 曝光时间          |

## 实例——博物馆的光照预算

敏感的水彩画每年的曝光限制约为**50000 lx·h**。在展示照度为50 lx、每天开馆8小时的条件下，
能展出多少天？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousexposure.*

val perDay = (50 of lux) * (8 of hours)     // KLuminousExposureUnitInstance
perDay into luxHours                         // 400.0

val budget = 50_000 of luxHours
(budget into luxHours) / (perDay into luxHours)   // 每年125个开馆日

// 反过来：在200 lx的照度下能展示多长时间？
val t = budget / (200 of lux)                // KTimeUnitInstance
t into hours                                  // 250.0小时
```

## 值语义

`equals`/`hashCode`比较**归一化后的lx·s值**，因此`(1 of luxHours) == (3600 of luxSeconds)`。
`toString()`以基本单位渲染该值：`"3600.0 lx*s"`。

## 另请参阅

* [照度](illuminance.zh.md) —— 该量累积所依据的速率。
* [发光能量](luminous-energy.zh.md) —— 针对光通量而非照度的相同思路。
* [光学概述](overview.zh.md)
</content>
