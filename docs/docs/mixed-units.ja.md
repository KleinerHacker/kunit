# 混合単位

**混合単位**は、それぞれが独自の指数を持つ複数の `KUnit` から構成される値です。
例: 速度なら `m^1 * s^-1`、力なら `m^1 * kg^1 * s^-2`。kunit ではこれは汎用の `KMixedUnitInstance` クラスで
表現されます。

グループ固有のラッパークラス(`KLengthUnitInstance` など、[事前定義された単位](units/kinematics/distance.md) を参照)は
単一の物理次元を扱うのに便利ですが、**異なる**グループの単位を組み合わせる必要がある場合や、ラッパークラスが
提供する同一グループの自動変換を望まない場合には `KMixedUnitInstance` を使います。

## 構造

```kotlin
data class KUnitTerm(val unit: KUnit, val exponent: Int)

class KMixedUnitInstance(value: Number, val units: List<KUnitTerm>)
```

- `value` は正規化された `Double` の大きさで、常に `units` に列挙されたまさにその単位と指数に対する相対値です —
  グループラッパーとは異なり、`KMixedUnitInstance` はグループの基本単位への正規化を**行いません**。
- `units` は物理次元を記述する `(KUnit, exponent)` のペアのリストです。

すべての「純粋な」単位は、この汎用表現に変換するための `toUnit()` 拡張を公開します:

```kotlin
import org.pcsoft.framework.kunit.distance.*

val d = 5 of meters
val mixed = d.toUnit() // KMixedUnitInstance: value=5.0, units=[METER^1]
```

## 乗算と除算

`*` と `/` は2つの `KMixedUnitInstance` の間で**常に**許可されます — 単位の乗算/除算は常に物理的に意味を持つため、
次元の制限はありません。

- `*` は一致する単位の指数を足し合わせ、片側にのみ存在する単位はそのまま引き継ぎます。
- `/` は一致する単位から右辺の指数を引きます(右辺にのみ存在する単位については指数を反転します)。
- 結果の指数が `0` になると、その単位は結果から完全に取り除かれます。

```kotlin
import org.pcsoft.framework.kunit.distance.*

val distance = (10 of meters).toUnit()   // units=[METER^1]
val width = (4 of meters).toUnit()       // units=[METER^1]

val area = distance * width                     // value=40.0, units=[METER^2]
val backToLength = area / width                 // value=10.0, units=[METER^1]
```

2つの異なる単位グループ(例: 長さと時間)を混ぜても、まったく同じように動作し、真の混合単位を生成します:

```kotlin
import org.pcsoft.framework.kunit.time.seconds

val distance = (100 of meters).toUnit()
val time = (10 of seconds).toUnit()

val speed = distance / time // value=10.0, units=[METER^1, SECOND^-1]
```

## 数値によるスケーリング

任意の単位値は、単なる `Number` でスケーリングできます。これは**大きさのみ**の演算です。値は変わりますが、単位の項と指数はそのまま保たれるため、結果は型と次元を維持します。

- `unit * n`、`n * unit`、`unit / n` はすべて**同じ型の単位**を返します（長さは長さのまま、面積は面積のまま）。
- `n / unit` は次元を**反転**し（すべての指数を符号反転）、汎用の `KMixedUnitInstance` を返します。周期から周波数を作るような逆数を構築する慣用的な方法です。
- スカラーの `+`/`-` は意図的に**ありません**。無次元の数を次元付きの値に加えることは無意味だからです。

実世界の例 — 円の面積 `A = π · r²` を単位システムだけで計算します。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.times
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.distance.*

val r = 12 of centi.meters       // KLengthUnitInstance、0.12 m
val area = Math.PI * (r * r)     // KAreaUnitInstance: π·r² ≈ 0.04524 m²
area into (meters * meters)      // ≈ 0.04524（平方メートル）
```

長さのスケーリングや、経路を等分する場合も同様です。

```kotlin
val tripled = (12 of meters) * 3 // KLengthUnitInstance、36 m
val leg = (10 of kilo.meters) / 4 // KLengthUnitInstance、2.5 km（経路の4分の1）
```

数を単位で**割る**と次元が反転します（例: 周期からの周波数）。

```kotlin
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.time.seconds

val frequency = 1 / (2 of seconds) // KMixedUnitInstance: value=0.5、units=[SECOND^-1]（0.5 Hz）
```

アフィンな**絶対温度**グループだけは例外です。絶対温度を数値でスケーリングすることは物理的に無意味なため（ケルビン値が −273.15 のオフセットを含むため）、`(20 of celsius) * 2` は**コンパイルエラー**になります。代わりに線形の**温度差**をスケーリングしてください（[温度差](units/thermodynamics/temperature-difference.md)を参照）。

## 加算と減算

`*`/`/` とは異なり、`+` と `-` は**同じ物理次元**を記述する2つの `KMixedUnitInstance` の間でのみ許可されます:
片側のすべての項について、同じ単位グループに属し(例: すべて `KDistanceUnit` の値)同じ指数を持つ項が、もう
片側にちょうど1つ存在しなければなりません(順序は問いません)。`KUnit` 自体が同一である必要は**ありません** —
一致する項は、グループ固有のラッパークラス(`KLengthUnitInstance` など)が「純粋な」単位に対して行うのと同じ
ように、正規化によって自動的に変換されます。結果は左辺のオペランドの `units` で表現されます。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.distance.miles

val a = (5 of meters).toUnit()
val b = (3 of meters).toUnit()
(a + b).value // 8.0

val c = (3 of miles).toUnit()
(a + c).value // 4832.032(3マイルをメートルに変換してから加算)、units=[METER^1]
```

一致しない単位グループや一致しない指数は依然として失敗します:

```kotlin
import org.pcsoft.framework.kunit.time.seconds

a + (3 of seconds).toUnit()       // IllegalStateException をスロー: 時間の項に一致する単位グループがない
a + ((2 of meters) pow 2).toUnit() // IllegalStateException をスロー: 指数の不一致(1 対 2)
```

事前に**厳密な**一致(同じグループだけでなく同じ `KUnit`)を確認するには `hasSameUnits` を使います:

```kotlin
val x = (5 of meters).toUnit()
val y = (3 of meters).toUnit()
x.hasSameUnits(y) // (unit -> exponent) シグネチャを順序に依存せず比較する
```

## 値の読み取り

`into` はターゲットの単位テンプレート(bare トークン、接頭辞付きビルダーテンプレート、または特殊な値1
インスタンス)で値を読み取り、素の `Double` を返します。両辺は同じ物理次元を記述していなければなりません。
`valueAs` もカスタム単位の `toString` もありません。特定の単位は `"${v into kilo.meters} km"` のように
フォーマットします。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds

val speed = (10 of meters) / (1 of seconds)

speed into (kilo.meters / hours)   // 36.0(km/h)

val area = (200 of meters) * (50 of meters)
area into hectares                 // 1.0
```

デフォルト(引数なし)の `toString()` は常に各項自身の `KUnit.symbol` を `*` で連結して使用します。例:
`"5.0 m*s^-1"`。

## 純粋な単位と混合単位の混在

すべての純粋な単位ラッパークラスは `KMixedUnitInstance` に対して直接 `*`/`/` をサポートするため、これらの演算子の
ために `toUnit()` を明示的に呼ぶ必要はほとんどありません:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*

val distance = 100 of meters        // KLengthUnitInstance
val mixed = distance.toUnit()       // KMixedUnitInstance

val combined = distance * mixed              // KMixedUnitInstance: METER^2
```

## 純粋な単位への変換し戻し

`KMixedUnitInstance` が再び単一の単位グループのちょうど1つの項を表すようになったら、グループ固有の `toXxxUnit()`
拡張(例: `toDistance()`)を介してそのグループのラッパークラスに変換し戻すことができます:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.seconds

val speed = (10 of meters) / (2 of seconds)    // KSpeedUnitInstance
val distanceAgain = speed.toUnit() * (2 of seconds).toUnit() // units=[METER^1]
distanceAgain.toDistance().value               // 10.0

val area = (200 of meters) * (50 of meters)    // KAreaUnitInstance
area.toUnit().toDistance().value               // 10000.0(面積、指数2)
```

`KMixedUnitInstance` がそのグループのちょうど1つの項で構成されて**いない**場合(例: まだ混合された長さ/時間の値
である場合)、変換は `IllegalStateException` をスローします。

同じ絞り込みは**距離の値に対して直接**(`KMixedUnitInstance` だけでなく)利用できます: 一般の
`KDistanceUnitInstance` — または任意のリーフ — は `toLength()`、`toArea()`、`toVolume()` で特定の次元に
絞り込めます。これらは指数がチェックされ、不一致の場合は `IllegalStateException` をスローします:

```kotlin
val area = (200 of meters) * (50 of meters)  // KAreaUnitInstance(指数2)
area.toArea().value                          // 10000.0
area.toDistance().toArea().value             // 10000.0(広げてから絞り込み戻す)
area.toLength()                              // IllegalStateException(指数2であり1ではない)
```
