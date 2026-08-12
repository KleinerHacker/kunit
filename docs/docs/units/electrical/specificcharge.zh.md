# 比荷

包: `org.pcsoft.framework.kunit.electric.specificcharge`
基本单位: **库仑每千克**
(`KSpecificChargeUnit.BASE == KSpecificChargeUnit.COULOMB_PER_KILOGRAM`)

类型: **构成单位**

比荷 `q/m` 是物体每单位质量所携带的电荷。这是 J. J. 汤姆逊用来鉴别电子的量,也是质谱法据以分离粒子的量。

其规范的基本量纲标准形式为 `current · time · mass⁻¹`。

!!! note "一个组,两种解读"
    相同的量纲还表示辐射防护中的**电离剂量**(照射量),历史上以伦琴为单位测量——参见
    [照射量](../thermodynamics/exposure.zh.md)。由于一个标准形式对应一个类型,这两种解读共享此组;伦琴是其具名单位之一。
    通过为你的值命名来区分它们。

## 具名单位

| 单位                 | 符号 |                 标记 | 1单位折合 C/kg |
|----------------------|--------|----------------------:|---------------:|
| 库仑每千克           | `C/kg` | `coulombsPerKilogram` |            1.0 |
| 伦琴                 | `R`    |            `roentgens` |        2.58e-4 |

所有标记都支持任意 SI 词头(`milli.roentgens` 等)。

## 常量

| 常量                         | 值                  | 含义                                     |
|-----------------------------|---------------------|------------------------------------------|
| `ELECTRON_SPECIFIC_CHARGE`  | `1.75882001076e11 C/kg` | 电子的荷质比                     |

符号被省略: 电子的电荷为负,但该比值以数值大小给出。

## 分解

该组只有一种分解,两种形式都产生数值相等的同类型实例。原生形式由 **单位模板**组装而成,因为该组带有质量项。

| 形式             | 表达式                                               |
|------------------|----------------------------------------------------------|
| 带类型运算符     | `charge / mass`                                         |
| 原生 (`toX()`)   | `(2 of A · s / kilo.grams).toSpecificCharge()`          |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

val typed = (4 of coulombs) / (2 of kilo.grams)
val native = (2 of amperes.toUnit() * (seconds pow 1) / kilo.grams.toUnit()).toSpecificCharge()

typed == native                   // true
typed into coulombsPerKilogram    // 2.0
```

## 与该组一起计算

| 表达式                       | 结果类型                     | 含义              |
|-----------------------------|----------------------------------|----------------------|
| `charge / mass`             | `KSpecificChargeUnitInstance`   | `q/m`                |
| `specificCharge * mass`     | `KChargeUnitInstance`           | 总电荷     |
| `charge / specificCharge`   | `KMassUnitInstance`             | 携带电荷的质量    |

## 实际例子 — 电子,以及一次照射量读数

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

// 汤姆逊的比值
val electron = ELECTRON_SPECIFIC_CHARGE of coulombsPerKilogram
electron into coulombsPerKilogram          // ≈ 1.7588e11

// 巡测仪的照射量读数,以及在 1 kg 空气中释放的电荷
val exposure = 1 of roentgens
exposure into coulombsPerKilogram          // 2.58e-4
(exposure * (1 of kilo.grams)) into coulombs   // 2.58e-4
```

## 值语义

`equals`/`hashCode` 比较**归一化的 C/kg 值**,所以
`(1 of roentgens) == (2.58e-4 of coulombsPerKilogram)`。`toString()` 以基本单位显示数值:
`"1.0 C/kg"`。

## 另请参阅

* [电荷](charge.zh.md) 与 [质量](../mechanics/mass.zh.md) — 两个操作数。
* [照射量](../thermodynamics/exposure.zh.md) — 作为电离剂量读取的同一类型。
* [电气工程概述](overview.zh.md)
