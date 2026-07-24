# コンソールフォーマッター

`KConsoleUnitFormatter` は **ANSI 対応ターミナル** 向けに値を描画します。出力の記法は
[デフォルトフォーマッター](default-formatter.md) とまったく同じ（`"10.8 km/h"`、`"m^2"`、
`"m*s^-3*A^-2"`）ですが、各視覚要素——数値、単位記号、演算子、指数——を ANSI カラーシーケンスで
囲むため、コンソール上で各部分が際立ちます。

`org.pcsoft.framework.kunit.formatter` パッケージにあります。デフォルトフォーマッターと異なり、
カラーパレットを保持するため通常の（不変・スレッドセーフな）`class` です。

## 生成される内容

レイアウトは [デフォルトフォーマッター](default-formatter.md) と **同一** です。`"<数値> <単位>"`
形式で、単一分数形式 `a/b` は分子がちょうど 1 つ・分母がちょうど 1 つのときのみ、それ以外は符号付き
指数のフラットな積、無次元値は数値のみです。唯一の違いは、各部分が ANSI SGR カラーで囲まれ、リセット
シーケンス `ESC[0m` で閉じられる点です。

### 色分けされる要素

4 つの視覚要素は [`KConsoleColorPalette`](#パレット) を通じて独立に色付けされます。

| 要素     | パレットのフィールド | 例                |
|----------|----------------------|-------------------|
| 数値     | `numberColor`        | `10.8`            |
| 単位記号 | `symbolColor`        | `km`, `h`, `m`    |
| 演算子   | `operatorColor`      | `*`, `/`          |
| 指数     | `exponentColor`      | `^2`, `^-3`       |

色が **空文字列** の要素は、エスケープシーケンスなしで出力されます（その部分は色付けされません）。
`MONOCHROME` が指数を色付けしないのはこの仕組みによります。

## パレット

色は値型 `KConsoleColorPalette` です。3 つのパレットが定義済みです。

| パレット     | 数値                          | 記号                  | 演算子          | 指数                    |
|--------------|-------------------------------|-----------------------|-----------------|-------------------------|
| `CLASSIC`    | シアン `ESC[36m`              | 黄 `ESC[33m`          | 灰 `ESC[90m`    | マゼンタ `ESC[35m`      |
| `VIVID`      | 明るい緑・太字 `ESC[92;1m`    | 明るい青 `ESC[94m`    | 白 `ESC[97m`    | 明るいマゼンタ `ESC[95m`|
| `MONOCHROME` | 太字 `ESC[1m`                 | 淡色 `ESC[2m`         | 淡色 `ESC[2m`   | 色なし（空）            |

- `CLASSIC` は暗いターミナルで落ち着いて読みやすく、**デフォルト** です。
- `VIVID` は高コントラストで目を引きます。
- `MONOCHROME` は色を使わず明度のみで、色数の少ないターミナル向けです。

## 使い方

引数なしで生成するとデフォルトの `CLASSIC` パレットを使います。定義済みまたは独自のパレットを渡す
こともできます。あとは他のフォーマッターと同様に [`format`](formatting.md) 動詞（またはパラメータ付き
`toString`）へ渡します。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KConsoleColorPalette
import org.pcsoft.framework.kunit.formatter.KConsoleUnitFormatter
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 3 of meters / seconds

// デフォルトの CLASSIC パレット
v.format(kilo.meters / hours, "%.1f", Locale.US, KConsoleUnitFormatter())

// 定義済みパレット
v.format(kilo.meters / hours, "%.1f", Locale.US, KConsoleUnitFormatter(KConsoleColorPalette.VIVID))

// ターゲットなしで基準単位を描画
(5 of meters).toString(pattern = "%.1f", formatter = KConsoleUnitFormatter(KConsoleColorPalette.MONOCHROME))
```

## 独自パレットの定義

`KConsoleColorPalette` は素朴なデータクラスなので、独自のカラーシーケンスを指定できます。各フィールド
には ANSI の **導入シーケンス**（例: 赤の `ESC[31m`。`ESC` はコード 27 のエスケープ文字）を保持し、
共有の `reset`（既定は `ESC[0m`）が各色付き部分の後に付加されます。

```kotlin
import org.pcsoft.framework.kunit.formatter.KConsoleColorPalette
import org.pcsoft.framework.kunit.formatter.KConsoleUnitFormatter

val esc = 27.toChar()   // ANSI エスケープ文字 (ESC)
val myPalette = KConsoleColorPalette(
    numberColor = "$esc[31m",   // 赤
    symbolColor = "$esc[32m",   // 緑
    operatorColor = "$esc[34m", // 青
    exponentColor = "$esc[35m", // マゼンタ
)
val formatter = KConsoleUnitFormatter(myPalette)
```

色だけでなくまったく異なる記法を出力するには、独自の
[カスタムフォーマッター](custom-formatters.md) を実装してください。

## 設定（指数・記号・関数記号）

色とは独立に、2 番目の引数 `KConsoleFormatConfig` が記法を制御します（[デフォルトフォーマッター](default-formatter.md)
と同じ）: `exponentStyle`（`CARET` = `m^2` / `SUPERSCRIPT` = `m²`）、`multiplication`（`*` / `·` / `×`）、
`division`（`/` / `÷`）、および `functionSymbols`（`UNICODE` / `ASCII`）。両引数とも既定値があるため
`KConsoleUnitFormatter()` は従来どおりです。`KConsoleUnitFormatter(palette, config)` のように渡します。

本物の分数線を持つ複数行の 2 次元分数には [グラフィカルフォーマッター](graphical-formatter.md) を参照してください。
