# Typst フォーマッター

`KTypstUnitFormatter` は値を **Typst** の数式として描画します。既定設定では `3 of meters / seconds` を `km/h` に
読み替えると `$1.5 upright("km")/upright("h")$` になります。

`org.pcsoft.framework.kunit.formatter` パッケージにあり、不変でスレッドセーフな `class` です。

## 生成される内容

`FRACTION` スタイルでは整った単一分母の形を `a/b` の分数形にします（必要に応じて分子や累乗した分母を括弧で
まとめます）。それ以外の形（および `EXPONENT` スタイル全体）は乗算記号で連結した平坦な積を符号付き指数で
表します。無次元の値は数値のみになります。

## 設定

`KTypstFormatConfig` は値型です。プリセットを選ぶか独自に構築します。

| オプション        | 値                                        | 既定       |
|------------------|------------------------------------------|-----------|
| `fractionStyle`  | `FRACTION`, `EXPONENT`                   | `FRACTION`|
| `unitStyle`      | `UPRIGHT` (`upright("km")`), `TEXT` (`"km"`) | `UPRIGHT` |
| `multiplication` | `SPACE` (空白), `DOT` (`dot`), `TIMES` (`times`) | `SPACE` |
| `delimiter`      | `MATH` (`$…$`), `FRAGMENT`               | `MATH`    |

プリセット: `DEFAULT`, `FRAGMENT`（`$…$` 区切りなし）。

## 実例

距離と時間から速さ（`v = s / t`）:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KTypstUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

v.format(kilo.meters / hours, "%.1f", Locale.US, KTypstUnitFormatter())
// $90.0 upright("km")/upright("h")$
```

加速度（`a = m/s²`）は累乗した分母を括弧でまとめます:

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KTypstUnitFormatter())
// $9.81 upright("m")/(upright("s")^2)$
```

まったく別の記法を出力するには [カスタムフォーマッター](custom-formatters.md) を参照してください。
