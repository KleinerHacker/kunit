# カスタム単位の追加

kunit は現在いくつかの単位グループを提供しています([距離](units/kinematics/distance.md)、[時間](units/kinematics/time.md)、
[ストレージ](units/information/storage.md)、[速度](units/kinematics/speed.md)、[データ転送率](units/information/datarate.md))が、エンジン全体
(`KUnit`、`KMixedUnitInstance`、`of`/`into` の動詞、接頭辞ビルダー)は汎用的でグループに依存しません。新しい
物理量を追加するとは、同じパターンに従うことを意味します。このページでは、デモ用の**質量**グループ
(`org.pcsoft.framework.kunit.mass`) — ストレージグループをモデルにした素朴な1次元グループ — の追加を順を追って
説明します。

## 1. サブパッケージと `KUnit` enum を作成する

各単位グループは `org.pcsoft.framework.kunit` の下に独自のサブパッケージを持ち、その単位は `KUnit` を実装する
`enum class` として宣言されます。`baseValue` はグループの基本単位への変換係数です — 基本単位自体は
`baseValue == 1.0` を持ちます。

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KUnit

enum class KMassUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** キログラム、質量の SI 基本単位。定義により [baseValue] = 1.0。 */
    KILOGRAM("kg", 1.0),

    /** グラム、1 g = 0.001 kg。 */
    GRAM("g", 0.001),

    /** 国際常用ポンド、1 lb = 0.45359237 kg。 */
    POUND("lb", 0.45359237),

    /** 国際常用オンス、1 oz = 0.028349523125 kg。 */
    OUNCE("oz", 0.028349523125);

    companion object {
        /** 質量グループの基本単位。[KMassUnitInstance] のすべての内部値はこの単位に正規化される。 */
        val BASE: KMassUnit = KILOGRAM
    }
}
```

## 2. ラッパークラスを作成する

ラッパー(`KMassUnitInstance`)は `KMixedUnitInstance` を**委譲**(`KUnitMeasurable by instance`)で
カプセル化し、`KUnitInstance<KMassUnitInstance>` を実装します。`KUnitInstance` のみのメンバー
(`plus`/`minus`/`compareTo`)と、`of` を支える `scaledBy` のオーバーライド、および
`equals`/`hashCode`/`toString` だけを手書きします。`valueAs`/`toString(target)` は**ありません** — 読み取りは
グループ非依存の `into` 動詞です。`KStorageUnitInstance` の形をコピーしてください。

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm

class KMassUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitMeasurable by instance, KUnitInstance<KMassUnitInstance> {

    /** `of` を支える: 値(キログラム)をスケールし、同じ型を返す。 */
    override fun scaledBy(factor: Double): KMassUnitInstance = massUnitInstanceOf(value * factor)

    override operator fun plus(other: KMassUnitInstance): KMassUnitInstance = massUnitInstanceOf(value + other.value)
    override operator fun minus(other: KMassUnitInstance): KMassUnitInstance = massUnitInstanceOf(value - other.value)
    override operator fun compareTo(other: KMassUnitInstance): Int = value.compareTo(other.value)

    override fun equals(other: Any?): Boolean = other is KMassUnitInstance && value == other.value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = instance.toString()
}

/** すでにキログラム([KMassUnit.BASE])で表された値から [KMassUnitInstance] を構築する。 */
internal fun massUnitInstanceOf(value: Double): KMassUnitInstance =
    KMassUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KMassUnit.BASE, 1))))

/** 純粋な質量の [KMixedUnitInstance] を [KMassUnitInstance] に変換し戻し、[KMassUnit.BASE] に正規化する。 */
fun KMixedUnitInstance.toMass(): KMassUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KMassUnit) {
        "KMixedUnitInstance $this does not represent a pure mass value (expected exactly one term of a KMassUnit)"
    }
    return massUnitInstanceOf(value * unit.baseValue)
}
```

## 3. 値1の bare トークンと接頭辞ビルダープロパティを追加する

プロジェクトの慣例に従い、DSL の語彙を2つのファイルに分けます: 値1の bare トークンは `K...UnitBareValues.kt` に、
接頭辞ビルダーのプロパティ拡張は `K...UnitExtensions.kt` に入れます。合わせることで、呼び出し側は
`5 of kilograms` や `5 of kilo.grams` と書き、`into` で読み戻せます。

`KMassUnitBareValues.kt`:

```kotlin
package org.pcsoft.framework.kunit.mass

/** 1 キログラム([KMassUnit.KILOGRAM])。 */
val kilograms: KMassUnitInstance = massUnitInstanceOf(KMassUnit.KILOGRAM.baseValue)

/** 1 グラム([KMassUnit.GRAM])。 */
val grams: KMassUnitInstance = massUnitInstanceOf(KMassUnit.GRAM.baseValue)

/** 1 ポンド([KMassUnit.POUND])。 */
val pounds: KMassUnitInstance = massUnitInstanceOf(KMassUnit.POUND.baseValue)

/** 1 オンス([KMassUnit.OUNCE])。 */
val ounces: KMassUnitInstance = massUnitInstanceOf(KMassUnit.OUNCE.baseValue)
```

`KMassUnitExtensions.kt`(質量は任意の大きさを受け付けるため、プロパティは共通の基底 `KPrefixBuilder` に付きます):

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KPrefixBuilder

private fun prefixedMass(builder: KPrefixBuilder, unit: KMassUnit): KMassUnitInstance =
    massUnitInstanceOf(builder.prefix.factor * unit.baseValue)

/** 接頭辞付きキログラム、例: `kilo.kilograms`。 */
val KPrefixBuilder.kilograms: KMassUnitInstance get() = prefixedMass(this, KMassUnit.KILOGRAM)

/** 接頭辞付きグラム、例: `milli.grams` = 1 mg。 */
val KPrefixBuilder.grams: KMassUnitInstance get() = prefixedMass(this, KMassUnit.GRAM)

/** 接頭辞付きポンド。 */
val KPrefixBuilder.pounds: KMassUnitInstance get() = prefixedMass(this, KMassUnit.POUND)

/** 接頭辞付きオンス。 */
val KPrefixBuilder.ounces: KMassUnitInstance get() = prefixedMass(this, KMassUnit.OUNCE)
```

これで完了です — これだけで完全な `+`、`-`、`*`、`/`、比較、SI 接頭辞ビルダー(`5 of milli.grams`)、
`toUnit()`/`toMass()` の往復変換がすべて無料で手に入ります。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mass.*

val a = 500 of grams
val b = 2 of pounds
val total = a + b            // KMassUnitInstance、キログラムに正規化
println(total into kilograms)
println(total into grams)

val heavier = b > a          // true
```

## 4.(任意)特殊/派生単位を追加する

特定のスケーリングに結び付いた、よく使われる名前付き単位(面積のヘクタールのような)がグループにある場合は、
名前付きの値1インスタンスとして追加します — 別のターゲット型は不要です:

```kotlin
package org.pcsoft.framework.kunit.mass

/** 1 メートルトン(1000 kg)。 */
val tonnes: KMassUnitInstance = massUnitInstanceOf(1000.0)
```

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mass.*

println((2500 of grams) into tonnes) // 0.0025
```

## 5. 他のグループと組み合わせる

すべてが最終的に汎用の `KMixedUnitInstance` エンジンに集約されるため、新しいグループは `*`/`/` を介して他の任意の
グループとすぐに組み合わせられます — ルールについては [混合単位](mixed-units.md) を参照してください。強く型付け
されたグループ間の結果(`mass / volume = density` のような)については、`KSpeedUnitOperators.kt` を反映して
`K...UnitOperators.kt` に型付き演算子拡張を追加します。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.mass.*

// 密度 = 質量 / 体積(汎用 KMixedUnitInstance: [KILOGRAM^1, METER^-3])
val density = (5 of kilograms) / (2 of liters)
```

## 6. 命名とテストのチェックリスト

- すべての public 型は `K` で始まります(`KMassUnit`、`KMassUnitInstance` など)。値1の bare トークンと接頭辞
  ビルダーのプロパティ拡張(`kilograms`、`grams` など)は例外で、言語的に自然なままにします。
- パラメータ化されたクロスマトリクステスト手順でグループをカバーします。`of`/`into` を通して構築し(生の enum は
  決して使わない): 単位 → 単位の変換、単位ペアごとに演算子ごと・比較ごとに1メソッド、接頭辞ビルダーマトリクス、
  `of` の型保存、`into` のエラーケース — `../../.claude/CLAUDE.md` の「パラメータ化クロスマトリクステスト手順」の節を参照して
  ください。
- すべての public メンバーを英語で、Markdown で、有用な場合は例を添えてドキュメント化します — 特に演算子。
- グループが大きさ制限付き(縮小接頭辞を拒否するストレージのような)である場合は、その単位プロパティを基底
  `KPrefixBuilder` ではなく `KAugmentingPrefixBuilder`/`KDiminishingPrefixBuilder` に付け、許可されない接頭辞を
  **コンパイルエラー**にします。
