# 添加自定义单位

kunit 今天提供了多个单位组([距离](units/kinematics/distance.md)、[时间](units/kinematics/time.md)、[存储](units/information/storage.md)、
[速度](units/kinematics/speed.md)、[数据传输率](units/information/datarate.md)),但整个引擎(`KUnit`、`KMixedUnitInstance`、`of`/`into`
动词、前缀构建器)是通用且与组无关的。添加一个新的物理量意味着遵循相同的模式。本页逐步介绍如何添加一个演示性的
**质量**组(`org.pcsoft.framework.kunit.mass`)—— 一个仿照存储组的简单一维组。

## 1. 创建子包和 `KUnit` 枚举

每个单位组在 `org.pcsoft.framework.kunit` 下拥有自己的子包,其单位声明为实现 `KUnit` 的 `enum class`。
`baseValue` 是到该组基本单位的转换系数 —— 基本单位本身的 `baseValue == 1.0`。

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KUnit

enum class KMassUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** 千克,质量的 SI 基本单位;根据定义 [baseValue] = 1.0。 */
    KILOGRAM("kg", 1.0),

    /** 克,1 g = 0.001 kg。 */
    GRAM("g", 0.001),

    /** 国际常衡磅,1 lb = 0.45359237 kg。 */
    POUND("lb", 0.45359237),

    /** 国际常衡盎司,1 oz = 0.028349523125 kg。 */
    OUNCE("oz", 0.028349523125);

    companion object {
        /** 质量组的基本单位;[KMassUnitInstance] 的所有内部值都归一化为此单位。 */
        val BASE: KMassUnit = KILOGRAM
    }
}
```

## 2. 创建包装器类

包装器(`KMassUnitInstance`)通过**委托**(`KUnitMeasurable by instance`)封装一个 `KMixedUnitInstance`,并实现
`KUnitInstance<KMassUnitInstance>`。它只手写 `KUnitInstance` 专属的成员(`plus`/`minus`/`compareTo`)、支撑 `of`
的 `scaledBy` 重写,以及 `equals`/`hashCode`/`toString`。**没有** `valueAs`/`toString(target)` —— 读取是与组无关的
`into` 动词。复制 `KStorageUnitInstance` 的形态。

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm

class KMassUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitMeasurable by instance, KUnitInstance<KMassUnitInstance> {

    /** 支撑 `of`: 缩放值(千克),返回相同类型。 */
    override fun scaledBy(factor: Double): KMassUnitInstance = massUnitInstanceOf(value * factor)

    override operator fun plus(other: KMassUnitInstance): KMassUnitInstance = massUnitInstanceOf(value + other.value)
    override operator fun minus(other: KMassUnitInstance): KMassUnitInstance = massUnitInstanceOf(value - other.value)
    override operator fun compareTo(other: KMassUnitInstance): Int = value.compareTo(other.value)

    override fun equals(other: Any?): Boolean = other is KMassUnitInstance && value == other.value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = instance.toString()
}

/** 从已经以千克([KMassUnit.BASE])表示的值构建一个 [KMassUnitInstance]。 */
internal fun massUnitInstanceOf(value: Double): KMassUnitInstance =
    KMassUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KMassUnit.BASE, 1))))

/** 将纯质量 [KMixedUnitInstance] 转换回 [KMassUnitInstance],归一化为 [KMassUnit.BASE]。 */
fun KMixedUnitInstance.toMass(): KMassUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KMassUnit) {
        "KMixedUnitInstance $this does not represent a pure mass value (expected exactly one term of a KMassUnit)"
    }
    return massUnitInstanceOf(value * unit.baseValue)
}
```

## 3. 添加值为 1 的裸令牌和前缀构建器属性

按照项目约定,将 DSL 词汇拆分到两个文件: 值为 1 的裸令牌放入 `K...UnitBareValues.kt`,前缀构建器属性扩展放入
`K...UnitExtensions.kt`。二者结合让调用者可以写 `5 of kilograms` 或 `5 of kilo.grams`,并用 `into` 读回。

`KMassUnitBareValues.kt`:

```kotlin
package org.pcsoft.framework.kunit.mass

/** 1 千克([KMassUnit.KILOGRAM])。 */
val kilograms: KMassUnitInstance = massUnitInstanceOf(KMassUnit.KILOGRAM.baseValue)

/** 1 克([KMassUnit.GRAM])。 */
val grams: KMassUnitInstance = massUnitInstanceOf(KMassUnit.GRAM.baseValue)

/** 1 磅([KMassUnit.POUND])。 */
val pounds: KMassUnitInstance = massUnitInstanceOf(KMassUnit.POUND.baseValue)

/** 1 盎司([KMassUnit.OUNCE])。 */
val ounces: KMassUnitInstance = massUnitInstanceOf(KMassUnit.OUNCE.baseValue)
```

`KMassUnitExtensions.kt`(质量接受任何量级,因此属性挂在公共基类 `KPrefixBuilder` 上):

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KPrefixBuilder

private fun prefixedMass(builder: KPrefixBuilder, unit: KMassUnit): KMassUnitInstance =
    massUnitInstanceOf(builder.prefix.factor * unit.baseValue)

/** 带前缀的千克,例如 `kilo.kilograms`。 */
val KPrefixBuilder.kilograms: KMassUnitInstance get() = prefixedMass(this, KMassUnit.KILOGRAM)

/** 带前缀的克,例如 `milli.grams` = 1 mg。 */
val KPrefixBuilder.grams: KMassUnitInstance get() = prefixedMass(this, KMassUnit.GRAM)

/** 带前缀的磅。 */
val KPrefixBuilder.pounds: KMassUnitInstance get() = prefixedMass(this, KMassUnit.POUND)

/** 带前缀的盎司。 */
val KPrefixBuilder.ounces: KMassUnitInstance get() = prefixedMass(this, KMassUnit.OUNCE)
```

就是这样 —— 这已经免费给了你完整的 `+`、`-`、`*`、`/`、比较、SI 前缀构建器(`5 of milli.grams`),以及
`toUnit()`/`toMass()` 往返转换。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mass.*

val a = 500 of grams
val b = 2 of pounds
val total = a + b            // KMassUnitInstance, 归一化为千克
println(total into kilograms)
println(total into grams)

val heavier = b > a          // true
```

## 4.(可选)添加特殊/派生单位

如果你的组有绑定到特定缩放的常用命名单位(如面积的公顷),将它们作为命名的值 1 实例添加 —— 不需要单独的目标类型:

```kotlin
package org.pcsoft.framework.kunit.mass

/** 1 公吨(1000 kg)。 */
val tonnes: KMassUnitInstance = massUnitInstanceOf(1000.0)
```

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mass.*

println((2500 of grams) into tonnes) // 0.0025
```

## 5. 与其他组组合

由于一切最终都汇入通用的 `KMixedUnitInstance` 引擎,你的新组立即可以通过 `*`/`/` 与任何其他组组合 —— 规则见
[混合单位](mixed-units.md)。对于强类型的跨组结果(如 `mass / volume = density`),在 `K...UnitOperators.kt` 中
添加带类型的运算符扩展,仿照 `KSpeedUnitOperators.kt`。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.mass.*

// 密度 = 质量 / 体积(通用 KMixedUnitInstance: [KILOGRAM^1, METER^-3])
val density = (5 of kilograms) / (2 of liters)
```

## 6. 命名与测试清单

- 所有 public 类型以 `K` 开头(`KMassUnit`、`KMassUnitInstance` 等);值为 1 的裸令牌和前缀构建器属性扩展
  (`kilograms`、`grams` 等)是例外,保持语言自然。
- 用参数化的交叉矩阵测试流程覆盖该组,通过 `of`/`into` 构建(绝不使用原始枚举): 单位 → 单位转换,每个运算符
  和每个比较对每个单位对各一个方法,前缀构建器矩阵,`of` 类型保持,以及 `into` 错误用例 —— 参见 `../../.claude/CLAUDE.md`
  中的"参数化交叉矩阵测试流程"一节。
- 用英文、以 Markdown 记录每个 public 成员,在有用处附上示例 —— 尤其是运算符。
- 如果该组是量级受限的(如拒绝缩小前缀的存储),将其单位属性挂在 `KAugmentingPrefixBuilder`/
  `KDiminishingPrefixBuilder` 而非基类 `KPrefixBuilder` 上,使不允许的前缀成为**编译错误**。
