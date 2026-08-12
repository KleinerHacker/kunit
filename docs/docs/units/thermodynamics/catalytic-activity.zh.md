# 催化活性

包: `org.pcsoft.framework.kunit.thermo.catalyticactivity`
基本单位: **卡塔尔** (`KCatalyticActivityUnit.BASE == KCatalyticActivityUnit.KATAL`)

类型: **构造单位（constructed unit）**

酶制剂的催化活性 `z` 表示它**每单位时间**转化多少底物: `z = n / t`。其SI单位是**卡塔尔**
（1 kat = 1 mol/s）—— 这是一个非常大的单位，因此实践中通常使用微卡塔尔，或传统的
**酶单位** `U`（每分钟一微摩尔）。

其规范基本量纲正规形式为 `substance¹ · time⁻¹`。

## 命名单位

| 单位   | 符号   |          令牌 |         1单位对应的kat数 |
|-------|--------|--------------:|-----------------------:|
| 卡塔尔  | `kat`  |      `katals` |                    1.0 |
| 酶单位  | `U`    | `enzymeUnits` | 1/60 × 10⁻⁶ ≈ 1.667e-8 |

1 U = 1 µmol/min，因此 1 kat = 60,000,000 U，1 U ≈ 16.67 nkat。所有令牌都接受任何SI前缀
（`micro.katals`、`nano.katals` 等）。

## 分解

该分组有一种分解方式，其两种形式都会生成相同的、值相等的类型化实例:

| 形式               | 表达式                                                                     |
|-------------------|------------------------------------------------------------------------------|
| 类型化运算符          | `amountOfSubstance / time`                                                   |
| 原生形式（`toX()`）   | `((2 of moles).toUnit() / (4 of seconds).toUnit()).toCatalyticActivity()`    |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.catalyticactivity.*

val typed = (2 of moles) / (4 of seconds)
val native = ((2 of moles).toUnit() / (4 of seconds).toUnit()).toCatalyticActivity()

typed == native      // true
typed into katals    // 0.5
```

## 使用该分组进行计算

| 表达式                                    | 结果类型                          | 含义                  |
|--------------------------------------------|-----------------------------------|-----------------------|
| `amountOfSubstance / time`                 | `KCatalyticActivityUnitInstance`  | `z = n / t`           |
| `catalyticActivity * time`                 | `KAmountOfSubstanceUnitInstance`  | `n = z · t`           |
| `amountOfSubstance / catalyticActivity`    | `KTimeUnitInstance`               | 所需的时间              |

## 实际示例 — 酶分析

某项分析在**10秒**内转化了**0.5毫摩尔**底物。以两种方式表示，并计算较小批量所需的时间:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.catalyticactivity.*

val z = (0.5 of milli.moles) / (10 of seconds)
z into micro.katals        // 50.0
z into enzymeUnits         // ≈ 3000.0 U

// The enzyme unit by definition: one micromole per minute
val one = (1 of micro.moles) / (1 of minutes)
one into enzymeUnits       // 1.0

// How long for 2 mmol at that activity?
val t = (2 of milli.moles) / z
t into seconds             // 40.0
```

## 值语义

`equals`/`hashCode` 比较**归一化的kat值**，因此 `(1 of katals) == (1000 of milli.katals)`。
`toString()` 以基本单位渲染该值: `"5.0E-5 kat"`。

## 另请参阅

* [物质的量](amount-of-substance.zh.md) —— 分子部分。
* [物质的量浓度](concentration.zh.md) —— 分析中通常测量的量。
* [热力学概览](overview.zh.md)
