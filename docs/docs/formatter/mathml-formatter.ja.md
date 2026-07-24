# MathML フォーマッター

`KMathMlUnitFormatter` は値を **表示用 MathML** として描画し、ブラウザや MathJax でそのまま表示できます。
既定設定では `3 of meters / seconds` を `km/h` に読み替えると、`<mfrac>`（`<mi>km</mi>` を `<mi>h</mi>` で割る）
を含むインライン `<math>` になります。

`org.pcsoft.framework.kunit.formatter` パッケージにあり、不変でスレッドセーフな `class` です。

## 生成される内容

`MFRAC` スタイルでは整った単一分母の形を `<mfrac>` で組みます。それ以外の形（および `EXPONENT` スタイル全体）は
乗算 `<mo>` で連結した平坦な積を符号付き `<msup>` 指数で表します。無次元の値は `<mn>` のみになります。

## 設定

`KMathMlFormatConfig` は値型です。プリセットを選ぶか独自に構築します。

| オプション        | 値                                        | 既定           |
|------------------|------------------------------------------|----------------|
| `fractionStyle`  | `MFRAC`, `EXPONENT`                      | `MFRAC`        |
| `unitTag`        | `MI`, `MTEXT`                            | `MI`           |
| `multiplication` | `MIDDLE_DOT` (`·`), `TIMES` (`×`), `INVISIBLE_TIMES` | `INVISIBLE_TIMES` |
| `wrapper`        | `MATH_INLINE`, `MATH_BLOCK`, `FRAGMENT`  | `MATH_INLINE`  |

プリセット: `DEFAULT`, `INLINE`（インライン `<msup>` 指数）, `FRAGMENT`（`<math>` ルートなし）。

## 実例

距離と時間から速さ（`v = s / t`）:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KMathMlUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

v.format(kilo.meters / hours, "%.1f", Locale.US, KMathMlUnitFormatter())
// <math display="inline"><mn>90.0</mn><mo>⁢</mo>
//   <mfrac><mrow><mi>km</mi></mrow><mrow><mi>h</mi></mrow></mfrac></math>
```

加速度（`a = m/s²`）は指数を分数内の `<msup>` にします:

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KMathMlUnitFormatter())
// <math display="inline"><mn>9.81</mn><mo>⁢</mo>
//   <mfrac><mrow><mi>m</mi></mrow><mrow><msup><mi>s</mi><mn>2</mn></msup></mrow></mfrac></math>
```

まったく別の記法を出力するには [カスタムフォーマッター](custom-formatters.md) を参照してください。
