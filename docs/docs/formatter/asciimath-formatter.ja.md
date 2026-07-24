# AsciiMath フォーマッター

`KAsciiMathUnitFormatter` は値を MathJax の簡潔な入力構文である **AsciiMath** として描画します。既定設定では
`3 of meters / seconds` を `km/h` に読み替えると `10.8 "km"/"h"` になります。

`org.pcsoft.framework.kunit.formatter` パッケージにあり、不変でスレッドセーフな `class` です。

## 生成される内容

`FRACTION` スタイルでは整った単一分母の形を `a/b` の分数形にします（必要に応じて分子や累乗した分母を括弧で
まとめます）。それ以外の形（および `EXPONENT` スタイル全体）は乗算記号で連結した平坦な積を符号付き指数で
表します。無次元の値は数値のみになります。

## 設定

`KAsciiMathFormatConfig` は値型です。プリセットを選ぶか独自に構築します。

| オプション        | 値                                        | 既定       |
|------------------|------------------------------------------|-----------|
| `fractionStyle`  | `FRACTION`, `EXPONENT`                   | `FRACTION`|
| `quoting`        | `QUOTED` (`"km"`), `BARE` (`km`)         | `QUOTED`  |
| `multiplication` | `ASTERISK` (`*`), `TIMES` (`xx`), `SPACE` (空白) | `SPACE` |

プリセット: `DEFAULT`, `PLAIN`（`*` で連結した裸の記号）。

## 実例

距離と時間から速さ（`v = s / t`）:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KAsciiMathUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

v.format(kilo.meters / hours, "%.1f", Locale.US, KAsciiMathUnitFormatter())
// 90.0 "km"/"h"
```

加速度（`a = m/s²`）は累乗した分母を括弧でまとめます:

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KAsciiMathUnitFormatter())
// 9.81 "m"/("s"^2)
```

まったく別の記法を出力するには [カスタムフォーマッター](custom-formatters.md) を参照してください。
