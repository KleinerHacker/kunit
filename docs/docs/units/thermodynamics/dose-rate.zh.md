# 剂量率

包: `org.pcsoft.framework.kunit.thermo.doserate`
基本单位: **戈瑞每秒** (`KDoseRateUnit.BASE == KDoseRateUnit.GRAY_PER_SECOND`)

类型: **构造单位（constructed unit）**

剂量率是**每单位时间**吸收的辐射剂量: `Ḋ = D / t`。这就是巡检仪读数所显示的内容 —— 几乎总是以
微希沃特每小时为单位 —— 而累积剂量则是对暴露时间的积分。

其规范基本量纲正规形式为 `length² · time⁻³`。戈瑞的 `J/kg` 中的千克与焦耳中的千克相互抵消，
这就是为什么不再保留质量项。

## 命名单位

| 单位             | 符号    | 令牌                   | 1单位对应的Gy/s |
|------------------|---------|------------------------|----------------:|
| 戈瑞每秒          | `Gy/s`  | `graysPerSecond`       |            1.0 |
| 戈瑞每小时         | `Gy/h`  | `graysPerHour`         |         1/3600 |
| 希沃特每秒         | `Sv/s`  | `sievertsPerSecond`    |            1.0 |
| 希沃特每小时       | `Sv/h`  | `sievertsPerHour`      |         1/3600 |

戈瑞（吸收剂量）和希沃特（当量剂量）共享同一量纲，因此 KUnit 为两者建模了同一个分组 —— 希沃特
的拼写形式是为了让辐射防护读数可以直接书写。所有令牌都接受任何SI前缀；
`micro.sievertsPerHour` 是日常使用的形式。

!!! note "一个分组，两种读法"
    戈瑞和希沃特的差异在于无量纲的辐射加权因子，而不在于量纲。单一正规形式必须映射到单一类型
    （同样的论证参见[熵](entropy.zh.md)），因此这一区别只在于您如何命名您的值。

## 分解

该分组有一种分解方式，其两种形式都会生成相同的、值相等的类型化实例:

| 形式               | 表达式                                                                       |
|-------------------|---------------------------------------------------------------------------------|
| 类型化运算符          | `specificEnergy / time`                                                        |
| 原生形式（`toX()`）   | `((6 of joulesPerKilogram).toUnit() / (2 of seconds).toUnit()).toDoseRate()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import org.pcsoft.framework.kunit.thermo.doserate.*

val typed = (6 of joulesPerKilogram) / (2 of seconds)
val native = ((6 of joulesPerKilogram).toUnit() / (2 of seconds).toUnit()).toDoseRate()

typed == native            // true
typed into graysPerSecond  // 3.0
```

## 使用该分组进行计算

| 表达式                          | 结果类型                          | 含义                  |
|-----------------------------------|------------------------------------|-----------------------|
| `specificEnergy / time`          | `KDoseRateUnitInstance`            | `Ḋ = D / t`           |
| `doseRate * time`                | `KSpecificEnergyUnitInstance`      | 累积的剂量              |
| `specificEnergy / doseRate`      | `KTimeUnitInstance`                | 暴露时间                |

吸收剂量本身属于[比能](specific-energy.zh.md)分组 —— 1 Gy = 1 J/kg。

## 实际示例 — 年度本底辐射

天然本底辐射大约为 **0.274 µSv/h**。一年内（8766小时）这累积到熟悉的 2.4 mSv:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import org.pcsoft.framework.kunit.thermo.doserate.*

val background = 0.274 of micro.sievertsPerHour
val year = 8766 of hours

val dose = background * year                       // KSpecificEnergyUnitInstance
dose into milli.joulesPerKilogram                  // ≈ 2.4 (mSv)

// How long until a 1 mSv limit is reached?
val t = (1 of milli.joulesPerKilogram) / background
t into hours                                        // ≈ 3650 h
```

## 值语义

`equals`/`hashCode` 比较**归一化的Gy/s值**，因此
`(1 of graysPerHour) == (1 of sievertsPerHour)`。`toString()` 以基本单位渲染该值:
`"1.0 Gy/s"`。

## 另请参阅

* [比能](specific-energy.zh.md) —— 吸收剂量本身（`Gy` = `J/kg`）。
* [热力学概览](overview.zh.md)
