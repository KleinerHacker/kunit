# 弹性系数(电弹性)

包: `org.pcsoft.framework.kunit.electric.elastance`
基本单位: **逆法拉**(`KElastanceUnit.BASE == KElastanceUnit.RECIPROCAL_FARAD`)

类型: **构成单位**

弹性系数 `S = U / Q = 1 / C` 是 [电容](capacitance.zh.md) 的精确倒数。当电容器**串联**时它是一种方便的形式:
串联的弹性系数只需简单相加,正如串联电阻一样。它的单位逆法拉,经典上称为 **daraf**——"farad" 反写而来。

其规范的基本量纲标准形式为 `mass · length² · time⁻⁴ · current⁻²`。

## 具名单位

| 单位              | 符号    |              标记 | 1单位折合 F⁻¹ |
|-------------------|---------|-------------------:|--------------:|
| 逆法拉            | `1/F`   | `reciprocalFarads` |           1.0 |
| daraf             | `daraf` |            `darafs` |           1.0 |

`darafs` 是基本单位的另一种拼写,不是独立单位。所有标记都支持任意 SI 词头(`mega.reciprocalFarads` 等)。

## 分解

该组只有一种分解,两种形式都产生数值相等的同类型实例。原生形式由 **单位模板**组装而成,因为该组带有质量项。

| 形式             | 表达式                                                    |
|------------------|----------------------------------------------------------------|
| 带类型运算符     | `voltage / charge`                                            |
| 原生 (`toX()`)   | `(1 of kilo.grams · m² / s⁴ / A²).toElastance()`              |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.elastance.*

val typed = (10 of volts) / (10 of milli.coulombs)
val native = (1000 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 4) / (amperes.toUnit() pow 2))
    .toElastance()

typed == native              // true
typed into reciprocalFarads  // 1000.0
```

## 与该组一起计算

| 表达式                  | 结果类型                     | 含义                    |
|------------------------|---------------------------------|----------------------------|
| `voltage / charge`     | `KElastanceUnitInstance`        | `S = U / Q`                |
| `elastance * charge`   | `KVoltageUnitInstance`          | `U = S · Q`                |
| `voltage / elastance`  | `KChargeUnitInstance`           | 存储的电荷              |
| `1 / capacitance`      | `KElastanceUnitInstance`        | `S = 1 / C`                |
| `1 / elastance`        | `KCapacitanceUnitInstance`      | `C = 1 / S`                |
| `elastance + …`        | `KElastanceUnitInstance`        | 电容器串联              |

## 实际例子 — 两个串联电容器

两个 1 mF 的电容器串联后表现得像一个 0.5 mF 的电容器。用弹性系数表示这只是简单的加法:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.capacitance.farads
import org.pcsoft.framework.kunit.electric.elastance.*

val total = (1 / (1 of milli.farads)) + (1 / (1 of milli.farads))
total into reciprocalFarads       // 2000.0

(1 / total) into milli.farads     // 0.5 — 等效电容
```

## 值语义

`equals`/`hashCode` 比较**归一化的 F⁻¹ 值**,所以 `(1 of reciprocalFarads) == (1 of darafs)`。
`toString()` 以基本单位显示数值: `"1000.0 1/F"`。

## 另请参阅

* [电容](capacitance.zh.md) — 互为倒数的量。
* [电压](voltage.zh.md) 与 [电荷](charge.zh.md) — 分解的两个操作数。
* [电气工程概述](overview.zh.md)
