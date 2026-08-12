# デフォルトフォーマッター

`KDefaultUnitFormatter` は kunit が標準で使用するフォーマッターです。[`format`](formatting.md) や パラメータ付き
`toString` を独自フォーマッターを渡さずに呼び出すと、常にこのフォーマッターが結果を 生成します。出力は `"10.8 km/h"`
のような、人が読めるプレーンテキストです。このページでは、 **何を**
**どのように**描画するのかを、出力例とともに正確に説明し、明示的な使い方も示します。

これは不変でスレッドセーフな `class` であり、`org.pcsoft.framework.kunit.formatter` パッケージにあります。
既定の動作には引数なしで構築し、描画方法を変えるには `KDefaultFormatConfig` を渡します。

## 生成される内容

描画された文字列は **数値** 部と **単位** 部の 2 つからなり、1 つの空白で区切られます （`"<数値> <単位>"`
）。値が無次元（単位なし）の場合は数値のみが描画されます。

### 数値

- パターンなしの場合、生の `Double` は kunit 独自の可搬なプレーン形式（`10.8`、`5.0`、`1.0E7`）で出力されます —
  これはプラットフォーム自身の `Double.toString()` とは異なり、どのターゲットでも同一です。
- 数値パターン（および任意の `KLocale`）を指定した場合、パターンは数値のみに適用され、単位部には決して 影響しません。
  対応するパターンについては [出力の書式設定](formatting.md) を参照してください。

| 呼び出し                                             | 描画される数値       |
|------------------------------------------------------|----------------------|
| `format(kilo.meters / hours)`                        | `10.799999999999999` |
| `format(kilo.meters / hours, "%.1f")`                | `10.8`               |
| `format(kilo.meters / hours, "%.1f", KLocale.DE_DE)` | `10,8`               |

### 単位部

各単位項は、それ自身の **書き下された記号**（接頭辞や代替単位の表示メタデータを尊重）で描画されます。 そのため `km`、`h`、
`mi`、`KiB` はグループ基準記号ではなく、それ自身として描画されます。全体の形は 項の構成によって決まります。

| 項                              | 描画結果              |
|---------------------------------|-----------------------|
| 単一単位、指数 1                | `km`                  |
| 指数 ≠ 1                        | `m^2`                 |
| 分子 1 つ + 分母がちょうど 1 つ | `km/h`, `m/s^2`       |
| それ以外                        | `m*s^-3*A^-2`, `s^-1` |
| 単位なし（無次元）              | 数値のみ              |

単一分数形式（`a/b`）は、分子項が **ちょうど 1 つ**、分母項が **ちょうど 1 つ** のときにのみ使われます。
それ以外はすべて、明示的な（負の場合もある）指数を持つフラットな積として描画されます。

## 出力例

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*
import org.pcsoft.framework.kunit.kinematic.time.*

(1500 of meters).toString()                          // "1500.0 m"
(3 of meters / seconds).format(kilo.meters / hours)  // "10.799999999999999 km/h"
(3 of meters / seconds).format(meters / seconds, "%.2f") // "3.33 m/s"
(9.81 of meters / (seconds pow 2)).format(meters / (seconds pow 2), "%.2f") // "9.81 m/s^2"
```

## 設定

`KDefaultFormatConfig`（値型）は、レイアウトのルールに影響を与えることなく描画方法を変更します。

| オプション        | 値                                                             | 既定値     |
|-------------------|----------------------------------------------------------------|------------|
| `exponentStyle`   | `CARET` (`m^2`), `SUPERSCRIPT` (`m²`)                          | `CARET`    |
| `multiplication`  | `ASTERISK` (`*`), `MIDDLE_DOT` (`·`), `CROSS` (`×`)            | `ASTERISK` |
| `division`        | `SLASH` (`/`), `OBELUS` (`÷`)                                  | `SLASH`    |
| `functionSymbols` | `KDefaultFunctionSymbols` — `UNICODE`, `ASCII` (`√`/`sqrt`, …) | `UNICODE`  |

`functionSymbols` テーブル（根号 `√`/`∛`/`∜`、`±`、`∞`、`°`）は、関数表記が適用される箇所のために
用意された設定であり、単純な整数指数では使用されません。プリセット: `DEFAULT`（従来の出力）、
`SUPERSCRIPT`（実際の上付き文字の指数）。

```kotlin
import org.pcsoft.framework.kunit.formatter.KDefaultFormatConfig
import org.pcsoft.framework.kunit.formatter.KDefaultUnitFormatter

(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", KLocale.EN_US, KDefaultUnitFormatter(KDefaultFormatConfig.SUPERSCRIPT))
// "9.81 m/s²"
```

## 明示的な使い方

デフォルトフォーマッターは自動的に適用されるため、名前を明示することはほとんどありません。とはいえ、
カスタムフォーマッターとの対称性のため、または呼び出し箇所で選択を明確にするために、明示的に渡すことも できます。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KDefaultUnitFormatter
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.formatter.KLocale

val v = 3 of meters / seconds

// 明示的なフォーマッター、デフォルト呼び出しと同じ結果
v.format(kilo.meters / hours, "%.1f", KLocale.EN_US, KDefaultUnitFormatter()) // "10.8 km/h"

// ターゲットなしで基準単位をデフォルトフォーマッターで描画
(5 of meters).toString(pattern = null, formatter = KDefaultUnitFormatter()) // "5.0 m"
```

まったく異なる記法を出力するには、[カスタムフォーマッター](custom-formatters.md) を参照してください。
