# 出力の書式設定

このページは **フォーマッター** グループの概要です。すべての書式設定の入口である `format` 動詞を説明します。
さらに 2 つの専用ページがあります。

- [デフォルトフォーマッター](default-formatter.md) — 付属の `KDefaultUnitFormatter` が単位部をどう描画するか
  （標準の表記）を出力例とともに説明します。
- [カスタムフォーマッター](custom-formatters.md) — 独自の描画（LaTeX、MathML、HTML など）を差し込む方法。

すべての値は `toString()` によって**基本単位**で自身を表示でき、[`into`](../mixed-units.md) によって特定の単位へ
**読み取る**こともできます。しかし `into` は単位記号のない素の `Double` を返すだけです。`format` 動詞はその隙間を
埋めます。`into` の表示版であり、値**と**単位記号を `String` として返します。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds

val v = 3 of meters / seconds

v format kilo.meters / hours       // "10.799999999999999 km/h"
```

`into` と同様に、`format` はまず値を対象の単位へ読み取り（同じ次元チェックとアフィン変換を行い）、その後に対象の単位
記号を付加します。対象は書かれたとおりの単位を保持しているため、接頭辞付きや別名の単位はグループの基本記号（`m`、
`s`）ではなく、**それ自身**の記号（`km`、`h`、`mi`）で描画されます。

## 数値の書式：パターンとロケール

中置形式は生の `Double` を描画します。**数値部分**を丸めたりローカライズするには、
[`java.util.Formatter`](https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html) のパターンと
任意の `Locale` を取る `format` オーバーロードを使います。

```kotlin
import java.util.Locale

v.format(kilo.meters / hours, "%.1f")                // "10.8 km/h"
v.format(kilo.meters / hours, "%.1f", Locale.GERMAN) // "10,8 km/h"
```

パターンは**数値のみ**に影響し、単位部分は変わりません。無効なパターンは `java.util.IllegalFormatException` を、
互換性のない対象次元は（`into` と同様に）`IllegalStateException` をスローします。

## 分数表記と積表記

組み込みフォーマッターは単位部分を次のように描画します。

| 項                              | 表示           |
|---------------------------------|----------------|
| 単一単位、指数 1                | `km`           |
| 指数 ≠ 1                       | `m^2`          |
| 分子 + ちょうど 1 個の分母      | `km/h`、`m/s^2` |
| それ以外                        | `m*s^-3*A^-2`、`s^-1` |
| 単位なし（無次元）              | 数値のみ       |

## パターン付き `toString`

引数なしの `toString()` は変わりません（基本単位での描画）。追加のオーバーロードは同じ数値パターン／ロケールを
基本単位出力に適用します。これは対象なしの `format` 動詞です。

```kotlin
(3 of meters / seconds).toString("%.2f", Locale.US) // "3.00 m/s"
(1500 of meters).toString("%.1f", Locale.US)        // "1500.0 m"
```

## 実世界の例

ランニングのペースを変換してきれいに表示します。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*
import java.util.Locale

val distance = 10 of kilo.meters
val time = 50 of minutes
val speed = distance / time                    // KSpeedUnitInstance

println(speed.format(kilo.meters / hours, "%.1f", Locale.US)) // "12.0 km/h"
println(speed.format(meters / seconds, "%.2f", Locale.US))    // "3.33 m/s"
```

## カスタム描画

単位部分は差し替え可能な [`KUnitFormatter`](custom-formatters.md) が生成します。付属の `KDefaultUnitFormatter`
は上記のプレーンテキストを生成します。その正確な規則と出力例は [デフォルトフォーマッター](default-formatter.md)
を参照してください。まったく異なる表記（グラフィカルな数式レンダラー向けの LaTeX や MathML、
HTML など）を出力するには、独自のフォーマッターを実装して明示的に渡します。[カスタムフォーマッター](custom-formatters.md)
を参照してください。
