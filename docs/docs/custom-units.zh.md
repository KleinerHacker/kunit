# 添加自定义单位

kunit 目前只提供一个单位组([距离](units/distance.md))，但整个引擎（`KUnit`、`KMixedUnitInstance`、前缀、
派生单位）都是通用的、与组无关的。添加一个新的物理量意味着遵循 `length` 包已经建立的模式。本页从零开始
演示如何添加一个示范性的**质量**（Mass）组（`org.pcsoft.framework.kunit.mass`）。

## 1. 创建子包和 `KUnit` 枚举

每个单位组都在 `org.pcsoft.framework.kunit` 下拥有自己的子包，其单位声明为实现 `KUnit` 的
`enum class`。`baseValue` 是转换为该组基础单位的系数——基础单位本身的 `baseValue == 1.0`。

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KUnit

/**
 * 枚举具体的质量单位。[baseValue] 是转换为组基础单位([BASE]，千克)的系数：
 * `1 单位 = baseValue * 千克`。
 */
enum class KMassUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** 千克，质量的 SI 基础单位；根据定义 [baseValue] = 1.0。 */
    KILOGRAM("kg", 1.0),

    /** 克，1 g = 0.001 kg。 */
    GRAM("g", 0.001),

    /** 国际常衡磅，1 lb = 0.45359237 kg。 */
    POUND("lb", 0.45359237),

    /** 国际常衡盎司，1 oz = 0.028349523125 kg。 */
    OUNCE("oz", 0.028349523125);

    companion object {
        /** 质量组的基础单位；[KMassUnitInstance] 的所有内部值都会归一化到该单位。 */
        val BASE: KMassUnit = KILOGRAM
    }
}
```

## 2. 创建包装类

包装类（`KMassUnitInstance`）通过**委托**（而非继承）封装一个 `KMixedUnitInstance`，并始终将其值归一化到该组
的基础单位。直接复制 `KLengthUnitInstance` 的结构即可——它在指数上是通用的，因此同一个包装类以后如果需要
也可以服务于质量的派生量。

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitTarget
import org.pcsoft.framework.kunit.KUnitTerm

class KMassUnitInstance internal constructor(internal val instance: KMixedUnitInstance) {

    private val exponent: Int get() = instance.units.single().exponent

    val value: Double get() = instance.value

    fun valueAs(target: KUnitTarget): Double = instance.valueAs(target)

    operator fun plus(other: KMassUnitInstance): KMassUnitInstance = KMassUnitInstance(instance + other.instance)
    operator fun minus(other: KMassUnitInstance): KMassUnitInstance = KMassUnitInstance(instance - other.instance)

    operator fun times(other: KMassUnitInstance): KMixedUnitInstance = instance * other.instance
    operator fun div(other: KMassUnitInstance): KMixedUnitInstance = instance / other.instance
    operator fun times(other: KMixedUnitInstance): KMixedUnitInstance = instance * other
    operator fun div(other: KMixedUnitInstance): KMixedUnitInstance = instance / other

    operator fun compareTo(other: KMassUnitInstance): Int {
        check(exponent == other.exponent) { "Cannot compare KMassUnitInstance with different exponents: $exponent vs ${other.exponent}" }
        return value.compareTo(other.value)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is KMassUnitInstance) return false
        check(exponent == other.exponent) { "Cannot compare KMassUnitInstance with different exponents: $exponent vs ${other.exponent}" }
        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = instance.toString()
    fun toString(target: KUnitTarget): String = instance.toString(target)

    fun toUnit(): KMixedUnitInstance = instance
}

/** 将纯质量的 [KMixedUnitInstance] 转换回 [KMassUnitInstance]，并归一化到 [KMassUnit.BASE]。 */
fun KMixedUnitInstance.toKMassUnit(): KMassUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KMassUnit) {
        "KMixedUnitInstance $this does not represent a pure mass-based value (expected exactly one term of a KMassUnit)"
    }
    val normalizedValue = value * Math.pow(unit.baseValue, term.exponent.toDouble())
    return KMassUnitInstance(KMixedUnitInstance(normalizedValue, listOf(KUnitTerm(KMassUnit.BASE, term.exponent))))
}

internal fun massUnitInstanceOf(value: Double): KMassUnitInstance =
    KMassUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KMassUnit.BASE, 1))))
```

## 3. 添加创建者扩展函数

遵循 `K...UnitExtensions.kt` 的模式，为每个单位添加一个裸 `val` 别名以及一个 `Number` 扩展函数，这样
调用方就可以写 `5.kilograms` 或 `1 kilo grams`，也可以把 `kilograms` 作为纯粹的 `valueAs` 目标传入：

```kotlin
package org.pcsoft.framework.kunit.mass

/** [KMassUnit.KILOGRAM] 的裸引用，用于 [valueAs][KMassUnitInstance.valueAs] 或前缀 infix 函数。 */
val kilograms: KMassUnit = KMassUnit.KILOGRAM

/** [KMassUnit.GRAM] 的裸引用。 */
val grams: KMassUnit = KMassUnit.GRAM

/** [KMassUnit.POUND] 的裸引用。 */
val pounds: KMassUnit = KMassUnit.POUND

/** [KMassUnit.OUNCE] 的裸引用。 */
val ounces: KMassUnit = KMassUnit.OUNCE

private fun of(value: Number, unit: KMassUnit): KMassUnitInstance = massUnitInstanceOf(value.toDouble() * unit.baseValue)

/** 从任意 [Number] 类型创建以千克为单位的纯质量值。 */
val Number.kilograms: KMassUnitInstance get() = of(this, KMassUnit.KILOGRAM)

/** 创建以克为单位的纯质量值。 */
val Number.grams: KMassUnitInstance get() = of(this, KMassUnit.GRAM)

/** 创建以磅为单位的纯质量值。 */
val Number.pounds: KMassUnitInstance get() = of(this, KMassUnit.POUND)

/** 创建以盎司为单位的纯质量值。 */
val Number.ounces: KMassUnitInstance get() = of(this, KMassUnit.OUNCE)
```

到这里就完成了——由于所有逻辑都位于通用的根包中，只需要 `KMassUnit : KUnit` 即可工作，你已经免费获得了
完整的 `+`、`-`、`*`、`/`、比较运算、SI 前缀（`5 kilo grams`），以及
`toUnit()`/`toKMassUnit()` 之间的往返转换。

```kotlin
import org.pcsoft.framework.kunit.mass.*

val a = 500.grams
val b = 2.pounds
val total = a + b            // KMassUnitInstance，归一化为千克
println(total.valueAs(kilograms))
println(total.valueAs(grams))

val heavier = b > a          // true
```

## 4.（可选）添加特殊/派生单位

如果你的组有常用的、绑定到特定指数的命名单位（如面积中的公顷），可以添加一个类似
`KDistanceDerivedUnit` 的 `KDerivedUnit` 对象：

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KDerivedUnit

object KMassDerivedUnit {
    /** 公吨，1 t = 1000 kg（指数 1，是基础单位的另一种"命名"缩放）。 */
    val TONNE: KDerivedUnit<KMassUnit> = KDerivedUnit(symbol = "t", exponent = 1, baseValue = 1000.0, referenceUnit = KMassUnit.BASE)
}
```

```kotlin
val truckLoad = 3.pounds.toUnit().toKMassUnit() // 仅作说明用
println(2500.grams.valueAs(KMassDerivedUnit.TONNE)) // 0.0025
```

## 5. 与其他组结合

由于所有内容最终都会流经通用的 `KMixedUnitInstance` 引擎，你的新组可以立即通过 `*`/`/` 与任何其他组
（例如长度）组合——完整规则请参见[混合单位](mixed-units.md)：

```kotlin
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.mass.*

// 密度 = 质量 / 体积
val density = 5.kilograms.toUnit() / 2.liters.toUnit()
```

## 6. 命名与测试检查清单

- 所有公开类型都以 `K` 开头（`KMassUnit`、`KMassUnitInstance`、`KMassDerivedUnit` 等）；创建者扩展函数
  和裸 `val` 别名（`kilograms()`、`grams` 等）不受此规则约束，保持语言自然的命名。
- 用英语、Markdown 格式记录每个公开成员，并在有用的地方（尤其是运算符）附上示例。
- 按照 `length` 下的结构，为每个组编写完整的测试套件：
    - 针对 `KUnit` 枚举值本身的专用测试类，
    - 针对包装类的专用测试类，覆盖每个运算符（`+`、`-`、`*`、`/`）和每个比较运算符
      （`==`、`!=`、`<`、`<=`、`>`、`>=`），既包含成功用例，也（在适用的情况下）包含
      `IllegalStateException` 失败用例，
    - 完整的前缀 × 单位测试矩阵（每个单位/派生单位与每个 SI 前缀组合），外加每个前缀一个独立测试，
    - 将新组与至少一个其他组组合的混合单位测试。
