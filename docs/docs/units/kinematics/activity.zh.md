# 放射性活度(贝可)

包: `org.pcsoft.framework.kunit.kinematic.frequency`
基本单位: **赫兹**(`KFrequencyUnit.BASE == KFrequencyUnit.HERTZ`)

类型: **原生单位**

放射性样品的活度 `A` 是每秒发生的核衰变数。其单位是**贝可**,`1 Bq = 1 s⁻¹` — 与[频率](frequency.zh.md)
**在量纲上完全相同**。

## 为什么贝可没有自己的类型

KUnit 有意使用 `KFrequencyUnitInstance` 来建模活度,而不是单独的 `KActivityUnitInstance`。原因在于本库的
形式识别约定:

* 每个标准化的分组只有**一个**规范的基础量纲标准形式,并且
* `toX()` 只识别该形式。

活度和频率共享标准形式 `time⁻¹`。一个标准形式对应两个类型会使原生表达变得含糊 —
`toFrequency()` 和一个假设的 `toActivity()` 会匹配同一个混合单位,而两者都不比对方更正确。单一类型才能保证
往返转换的确定性。

区别只在于*你如何命名变量*: 频率计的是周期性循环,活度计的是随机衰变,但两者都是"每秒事件数"。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.frequency.*
import org.pcsoft.framework.kunit.kinematic.time.seconds

val activity = 37 of giga.hertz     // 读作 37 GBq — 一克镭的活度
activity into mega.hertz             // 37 000.0

// 一分钟内的衰变数
val decays = activity * (60 of seconds)   // 无量纲计数
decays                                     // 2.22e12
```

!!! note "居里"
    历史单位是居里,1 Ci = 3.7 × 10¹⁰ Bq。它没有自己的令牌;可写作
    `37 of giga.hertz`,或引入你自己的常量。

## 现实示例 — 烟雾探测器的放射源

家用烟雾探测器中约含 **30 kBq** 的镅-241:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.frequency.*
import org.pcsoft.framework.kunit.kinematic.time.hours

val source = 30 of kilo.hertz             // 30 kBq
source into hertz                          // 30 000.0

// 一天内的衰变数
val perDay = source * (24 of hours)
perDay                                      // ≈ 2.59e9
```

## 参见

* [频率](frequency.zh.md) — 同一类型,读作周期速率。
* [剂量率](../thermodynamics/dose-rate.zh.md) — 放射源每单位时间释放的剂量。
* [吸收剂量](../thermodynamics/absorbed-dose.zh.md) — 基于能量的剂量。
