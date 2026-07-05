# カスタム単位の追加

kunit は現在1つの単位グループ([距離](units/distance.md))のみを提供していますが、エンジン全体
(`KUnit`、`KMixedUnitInstance`、接頭辞、派生単位)は汎用的でグループに依存しません。新しい物理量を追加する
とは、`length` パッケージがすでに確立しているパターンに従うことを意味します。このページでは、例として
**質量**(Mass)グループ(`org.pcsoft.framework.kunit.mass`)をゼロから追加する手順を説明します。

## 1. サブパッケージと `KUnit` enum を作成する

すべての単位グループは `org.pcsoft.framework.kunit` の下に独自のサブパッケージを持ち、その単位は
`KUnit` を実装する `enum class` として宣言されます。`baseValue` はグループの基本単位への変換係数です -
基本単位自体は `baseValue == 1.0` を持ちます。

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KUnit

/**
 * 質量の具体的な単位を列挙します。[baseValue] はグループの基本単位([BASE]、キログラム)への
 * 変換係数です: `1 単位 = baseValue * キログラム`。
 */
enum class KMassUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** キログラム、質量の SI 基本単位; 定義により [baseValue] = 1.0。 */
    KILOGRAM("kg", 1.0),

    /** グラム、1 g = 0.001 kg。 */
    GRAM("g", 0.001),

    /** 国際常用ポンド、1 lb = 0.45359237 kg。 */
    POUND("lb", 0.45359237),

    /** 国際常用オンス、1 oz = 0.028349523125 kg。 */
    OUNCE("oz", 0.028349523125);

    companion object {
        /** 質量グループの基本単位; [KMassUnitInstance] のすべての内部値はこの単位に正規化されます。 */
        val BASE: KMassUnit = KILOGRAM
    }
}
```

## 2. ラッパークラスを作成する

ラッパークラス(`KMassUnitInstance`)は**委譲**(継承ではなく)によって `KMixedUnitInstance` をカプセル化し、
常にその値をグループの基本単位に正規化します。`KLengthUnitInstance` の形をそのまま複製してください -
これは指数について汎用的であるため、必要になれば同じラッパーが質量の派生量にも対応できます。

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

/** 純粋な質量の [KMixedUnitInstance] を [KMassUnit.BASE] に正規化して [KMassUnitInstance] に変換します。 */
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

## 3. 生成用拡張関数を追加する

`K...UnitExtensions.kt` のパターンに従って、単位ごとに bare な `val` エイリアスと `Number` 拡張関数を
追加します。これにより、呼び出し元は `5.kilograms` や `1 kilo grams` と書くことができ、`kilograms`
を純粋な `valueAs` ターゲットとして渡すこともできます。

```kotlin
package org.pcsoft.framework.kunit.mass

/** [KMassUnit.KILOGRAM] への bare 参照。[valueAs][KMassUnitInstance.valueAs] や接頭辞 infix 関数で使用。 */
val kilograms: KMassUnit = KMassUnit.KILOGRAM

/** [KMassUnit.GRAM] への bare 参照。 */
val grams: KMassUnit = KMassUnit.GRAM

/** [KMassUnit.POUND] への bare 参照。 */
val pounds: KMassUnit = KMassUnit.POUND

/** [KMassUnit.OUNCE] への bare 参照。 */
val ounces: KMassUnit = KMassUnit.OUNCE

private fun of(value: Number, unit: KMassUnit): KMassUnitInstance = massUnitInstanceOf(value.toDouble() * unit.baseValue)

/** 任意の [Number] 型からキログラム単位の純粋な質量値を作成します。 */
val Number.kilograms: KMassUnitInstance get() = of(this, KMassUnit.KILOGRAM)

/** グラム単位の純粋な質量値を作成します。 */
val Number.grams: KMassUnitInstance get() = of(this, KMassUnit.GRAM)

/** ポンド単位の純粋な質量値を作成します。 */
val Number.pounds: KMassUnitInstance get() = of(this, KMassUnit.POUND)

/** オンス単位の純粋な質量値を作成します。 */
val Number.ounces: KMassUnitInstance get() = of(this, KMassUnit.OUNCE)
```

これで完了です - すべてのロジックは汎用のルートパッケージにあり、`KMassUnit : KUnit` だけで動作するため、
すでに完全な `+`、`-`、`*`、`/`、比較演算、SI 接頭辞(`5 kilo grams`)、そして
`toUnit()`/`toKMassUnit()` の相互変換を無料で手に入れています。

```kotlin
import org.pcsoft.framework.kunit.mass.*

val a = 500.grams
val b = 2.pounds
val total = a + b            // KMassUnitInstance、キログラムに正規化
println(total.valueAs(kilograms))
println(total.valueAs(grams))

val heavier = b > a          // true
```

## 4.(任意)特殊・派生単位の追加

グループに特定の指数に紐づく、よく使われる名前付き単位(面積のヘクタールのような)がある場合は、
`KDistanceDerivedUnit` に類似した `KDerivedUnit` オブジェクトを追加してください。

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KDerivedUnit

object KMassDerivedUnit {
    /** トン、1 t = 1000 kg(指数1、基本単位の代替の「名前付き」スケーリング)。 */
    val TONNE: KDerivedUnit<KMassUnit> = KDerivedUnit(symbol = "t", exponent = 1, baseValue = 1000.0, referenceUnit = KMassUnit.BASE)
}
```

```kotlin
val truckLoad = 3.pounds.toUnit().toKMassUnit() // 説明のみを目的とした例
println(2500.grams.valueAs(KMassDerivedUnit.TONNE)) // 0.0025
```

## 5. 他のグループとの組み合わせ

すべてが最終的に汎用の `KMixedUnitInstance` エンジンを通るため、新しいグループは `*`/`/` を介して他の
任意のグループ(例: 長さ)とすぐに組み合わせることができます - 完全なルールは
[混合単位](mixed-units.md) を参照してください。

```kotlin
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.mass.*

// 密度 = 質量 / 体積
val density = 5.kilograms.toUnit() / 2.liters.toUnit()
```

## 6. 命名とテストのチェックリスト

- すべての公開型は `K` で始まります(`KMassUnit`、`KMassUnitInstance`、`KMassDerivedUnit` など)。
  生成用拡張関数と bare な `val` エイリアス(`kilograms()`、`grams` など)はこの規則の対象外で、
  言語的に自然な名前のままです。
- すべての公開メンバーを英語で、Markdown 形式で、有用な箇所(特に演算子)には例とともに文書化して
  ください。
- `length` の下の構造を反映して、グループごとに完全なテストスイートを作成してください:
    - `KUnit` enum 値自体のための専用テストクラス、
    - すべての演算子(`+`、`-`、`*`、`/`)とすべての比較演算子
      (`==`、`!=`、`<`、`<=`、`>`、`>=`)を、成功ケースと(該当する場合)`IllegalStateException`
      失敗ケースの両方でカバーするラッパークラス専用のテストクラス、
    - 完全な接頭辞 × 単位のテストマトリクス(すべての単位/派生単位をすべての SI 接頭辞と組み合わせる)、
      さらに接頭辞ごとに独立したテストを1つ、
    - 新しいグループを少なくとも1つの他のグループと組み合わせる混合単位のテスト。
