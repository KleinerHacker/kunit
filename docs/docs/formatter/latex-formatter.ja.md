# LaTeX フォーマッター

`KLatexUnitFormatter` は値を **LaTeX** の数式として描画し、MathJax・KaTeX・LaTeX 文書でそのまま利用できます。
既定設定では `3 of meters / seconds` を `km/h` に読み替えると `1.5\,\frac{\mathrm{km}}{\mathrm{h}}` になります。

`org.pcsoft.framework.kunit.formatter` パッケージにあり、不変でスレッドセーフな `class` です。

## 生成される内容

レイアウトは共通ルールに従います。`FRACTION` スタイルでは、分子があり分母がちょうど 1 つの整った形を
`\frac{…}{…}` として組みます。それ以外の形（および `INLINE` スタイル全体）は、乗算記号で連結した平坦な積を
符号付き指数で表します。無次元の値は数値のみを描画します。

## 設定

`KLatexFormatConfig` は値型です。プリセットを選ぶか独自に構築します。

| オプション        | 値                                        | 既定       |
|------------------|------------------------------------------|-----------|
| `fractionStyle`  | `FRACTION`, `INLINE`                     | `FRACTION`|
| `unitWrapper`    | `MATHRM`, `TEXT`, `NONE`                 | `MATHRM`  |
| `multiplication` | `CDOT` (`\cdot`), `TIMES` (`\times`), `THIN_SPACE` (`\,`) | `CDOT` |
| `delimiter`      | `DOLLAR` (`$…$`), `PARENTHESES` (`\(…\)`), `NONE` | `NONE` |
| `spacing`        | `THIN` (`\,`), `NORMAL` (空白)            | `THIN`    |

プリセット: `DEFAULT`, `INLINE`（インライン積）, `PLAIN`（ラッパーなし・通常の空白）。

## 実例

距離と時間から速さ（`v = s / t`）:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KLatexUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

v.format(kilo.meters / hours, "%.1f", Locale.US, KLatexUnitFormatter())
// 90.0\,\frac{\mathrm{km}}{\mathrm{h}}
```

加速度（`a = m/s²`）は分数内で累乗した分母を示します:

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KLatexUnitFormatter())
// 9.81\,\frac{\mathrm{m}}{\mathrm{s}^{2}}
```

まったく別の記法を出力するには [カスタムフォーマッター](custom-formatters.md) を参照してください。
