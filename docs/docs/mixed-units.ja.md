# 混合単位

**混合単位**(ドイツ語: *Mischeinheit*)は、複数の `KUnit` がそれぞれ独自の指数とともに組み合わされた値です。
例: 速度の `m^1 * s^-1`、力の `m^1 * kg^1 * s^-2`。kunit ではこれは汎用の `KMixedUnitInstance` クラスで
表現されます。

グループごとのラッパークラス(`KLengthUnitInstance` など、[定義済み単位](units/length.md) を参照)は
単一の物理次元を扱う際に便利ですが、**異なる**グループの単位を組み合わせる必要がある場合や、ラッパークラスが
提供する同一グループ内の自動変換を望まない場合には、`KMixedUnitInstance` を直接使用することになります。

## 構造

```kotlin
data class KUnitTerm(val unit: KUnit, val exponent: Int)

class KMixedUnitInstance(value: Number, val units: List<KUnitTerm>)
```

- `value` は正規化された `Double` の大きさで、常に `units` に列挙された単位と指数に対して相対的です -
  グループラッパーとは異なり、`KMixedUnitInstance` はグループの基本単位への正規化を**行いません**。
- `units` は物理次元を記述する `(KUnit, 指数)` のペアのリストです。

すべての「純粋な」単位は、この汎用表現に変換するための `toKMixedUnitInstance()` 拡張関数を提供します。

```kotlin
import org.pcsoft.framework.kunit.length.*

val d = 5.meters()
val mixed = d.toKMixedUnitInstance() // KMixedUnitInstance: value=5.0, units=[METER^1]
```

!!! note
    以下の例で `seconds()`/`TimeUnit` を参照している部分は、「時間」単位グループが長さと組み合わされた
    場合の姿を示すためのものです - kunit は現在 `length` グループのみを提供しています
    ([定義済み単位](units/length.md) を参照)。自分で追加するには
    [カスタム単位の追加](custom-units.md) に従ってください。

## 乗算と除算

2つの `KMixedUnitInstance` 間の `*` と `/` は**常に**許可されています - 単位の乗算・除算は常に物理的に意味が
あるため、次元の制約はありません。

- `*` は一致する単位の指数を加算し、一方にしか存在しない単位はそのまま結果に含まれます。
- `/` は一致する単位について右側オペランドの指数を減算します(右側にのみ存在する単位は指数の符号が
  反転されます)。
- 結果の指数が `0` になった場合、その単位は結果から完全に削除されます。

```kotlin
import org.pcsoft.framework.kunit.length.*

val distance = 10.meters().toKMixedUnitInstance()   // units=[METER^1]
val width = 4.meters().toKMixedUnitInstance()       // units=[METER^1]

val area = distance * width                     // value=40.0, units=[METER^2]
val backToLength = area / width                 // value=10.0, units=[METER^1]
```

2つの異なる単位グループ(例: 長さと、将来追加されるかもしれない時間)を混合する場合も全く同様に動作し、
真の混合単位が生成されます。

```kotlin
// 「カスタム単位の追加」のパターンに従って「時間」単位グループが存在すると仮定:
val distance = 100.meters().toKMixedUnitInstance()
val time = 10.seconds().toKMixedUnitInstance()

val speed = distance / time // value=10.0, units=[METER^1, SECOND^-1]
```

## 加算と減算

`*`/`/` とは異なり、`+` と `-` は2つの `KMixedUnitInstance` が**同じ物理次元**を表す場合にのみ許可されます -
片方の各項に対し、もう片方に同じ単位グループ(例えば `KLengthUnit` の値すべて)かつ同じ指数を持つ項が
ちょうど1つ存在する必要があります(順序は問いません)。`KUnit` 自体が完全に一致している必要は**ありません** -
一致した項は自動的に正規化変換されます。これはグループごとのラッパークラス(`KLengthUnitInstance` など)が
「pure」単位に対して行っているのと同じ仕組みです。結果は左辺オペランドの `units` で表現されます。

```kotlin
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.length.KLengthUnit

val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
(a + b).value // 8.0

val c = KMixedUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.MILE, 1)))
(a + c).value // 4832.032 (3マイルをメートルに変換してから加算), units=[METER^1]
```

単位グループや指数が一致しない場合は失敗します:

```kotlin
val time = KMixedUnitInstance(3.0, listOf(KUnitTerm(TimeUnit.SECOND, 1)))
a + time // IllegalStateException が発生: TimeUnit.SECOND に対応する単位グループがない

val area = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 2)))
a + area // IllegalStateException が発生: 指数の不一致 (1 vs 2)
```

`hasSameUnits` を使用すると、(グループではなく)**完全一致**であることを事前に確認できます。

```kotlin
val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KLengthUnit.METER, 0)))
a.hasSameUnits(b) // (単位 -> 指数) シグネチャを順序に関係なく比較
```

## 値の読み取りとフォーマット

`valueAs` は値を任意のターゲット単位の集合に変換します - 各ターゲットは単位グループごとに(派生単位の場合は
指数も含めて)ちょうど1つの項と一致する必要があります。`toString` オーバーロードは同様に動作しますが、
記号もレンダリングします。

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.length.*

val speed = 10.meters().toKMixedUnitInstance() / 1.seconds().toKMixedUnitInstance()

speed.valueAs(KUnitPrefix.KILO with KLengthUnit.METER, TimeUnit.HOUR) // 36.0 (km/h)
speed.toString(KUnitPrefix.KILO with KLengthUnit.METER, TimeUnit.HOUR) // "36.0 km*h^-1"

val area = 200.meters().toKMixedUnitInstance() * 50.meters().toKMixedUnitInstance()
area.valueAs(KLengthDerivedUnit.HECTARE) // 1.0
```

引数なしのデフォルトの `toString()` は常に各項自身の `KUnit.symbol` を使用し、`*` で連結します。例:
`"5.0 m*s^-1"`。

## 純粋な単位と混合単位の混在

すべての純粋な単位ラッパークラスは `KMixedUnitInstance` に対して直接 `*`/`/` をサポートしているため、これらの
演算子のために `toKMixedUnitInstance()` を明示的に呼び出す必要はほとんどありません。

```kotlin
import org.pcsoft.framework.kunit.length.*

val distance = 100.meters()                 // KLengthUnitInstance
val mixed = distance.toKMixedUnitInstance()       // KMixedUnitInstance

val combined = distance * mixed              // KMixedUnitInstance: METER^2
```

## 純粋な単位への変換

`KMixedUnitInstance` が再び単一の単位グループの1つの項だけで構成されるようになった場合、グループ固有の
`toXxxUnit()` 拡張関数(例: `toKLengthUnit()`)を介して、そのグループのラッパークラスに戻すことができます。

```kotlin
import org.pcsoft.framework.kunit.length.*

val speed = 10.meters() / 2.seconds()          // KMixedUnitInstance(時間グループが存在すると仮定)
val distanceAgain = speed.toKMixedUnitInstance() * 2.seconds() // units=[METER^1]
distanceAgain.toKLengthUnit().value             // 10.0

val area = 200.meters() * 50.meters()           // units=[METER^2]
area.toKLengthUnit().value                        // 10000.0(面積、指数2)
```

`KMixedUnitInstance` がそのグループの1つの項だけで構成されて**いない**場合(例: まだ長さ/時間が混在した値の場合)、
変換は `IllegalStateException` を投げます。
