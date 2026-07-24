# デフォルトフォーマッター

`KDefaultUnitFormatter` は kunit が標準で使用するフォーマッターです。[`format`](formatting.md) や
パラメータ付き `toString` を独自フォーマッターを渡さずに呼び出すと、常にこのフォーマッターが結果を
生成します。出力は `"10.8 km/h"` のような、人が読めるプレーンテキストです。このページでは、**何を**
**どのように**描画するのかを、出力例とともに正確に説明し、明示的な使い方も示します。

これは不変でスレッドセーフな `class` であり、`org.pcsoft.framework.kunit.formatter` パッケージにあります。
既定の動作には引数なしで構築し、描画方法を変えるには `KDefaultFormatConfig` を渡します。

## 生成される内容

描画された文字列は **数値** 部と **単位** 部の 2 つからなり、1 つの空白で区切られます
（`"<数値> <単位>"`）。値が無次元（単位なし）の場合は数値のみが描画されます。

### 数値

- パターンなしの場合、生の `Double` が `Double.toString()` で出力されます。
- `java.util.Formatter` パターン（および任意の `Locale`）を指定した場合、数値は
  `String.format(locale, pattern, value)` で描画されます。パターンは **数値のみ** に影響し、単位部には
  影響しません。

| 呼び出し                                          | 描画される数値 |
|--------------------------------------------------|-----------------|
| `format(kilo.meters / hours)`                    | `10.799999999999999` |
| `format(kilo.meters / hours, "%.1f")`            | `10.8` |
| `format(kilo.meters / hours, "%.1f", Locale.GERMAN)` | `10,8` |

### 単位部

各単位項は、それ自身の **書き下された記号**（接頭辞や代替単位の表示メタデータを尊重）で描画されます。
そのため `km`、`h`、`mi`、`KiB` はグループ基準記号ではなく、それ自身として描画されます。全体の形は
項の構成によって決まります。

| 項                                       | 描画結果               |
|------------------------------------------|-----------------------|
| 単一単位、指数 1                          | `km`                  |
| 指数 ≠ 1                                | `m^2`                 |
| 分子 1 つ + 分母がちょうど 1 つ           | `km/h`, `m/s^2`       |
| それ以外                                  | `m*s^-3*A^-2`, `s^-1` |
| 単位なし（無次元）                        | 数値のみ               |

単一分数形式（`a/b`）は、分子項が **ちょうど 1 つ**、分母項が **ちょうど 1 つ** のときにのみ使われます。
それ以外はすべて、明示的な（負の場合もある）指数を持つフラットな積として描画されます。

## 出力例

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

(1500 of meters).toString()                          // "1500.0 m"
(3 of meters / seconds).format(kilo.meters / hours)  // "10.799999999999999 km/h"
(3 of meters / seconds).format(meters / seconds, "%.2f") // "3.33 m/s"
(9.81 of meters / (seconds pow 2)).format(meters / (seconds pow 2), "%.2f") // "9.81 m/s^2"
```

## 明示的な使い方

デフォルトフォーマッターは自動的に適用されるため、名前を明示することはほとんどありません。とはいえ、
カスタムフォーマッターとの対称性のため、または呼び出し箇所で選択を明確にするために、明示的に渡すことも
できます。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KDefaultUnitFormatter
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 3 of meters / seconds

// 明示的なフォーマッター、デフォルト呼び出しと同じ結果
v.format(kilo.meters / hours, "%.1f", Locale.US, KDefaultUnitFormatter()) // "10.8 km/h"

// ターゲットなしで基準単位をデフォルトフォーマッターで描画
(5 of meters).toString(pattern = null, formatter = KDefaultUnitFormatter()) // "5.0 m"
```

まったく異なる記法を出力するには、[カスタムフォーマッター](custom-formatters.md) を参照してください。
