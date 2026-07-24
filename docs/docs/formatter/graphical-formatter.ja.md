# グラフィカルフォーマッター

`KGraphicalConsoleUnitFormatter` は ANSI 対応端末向けに値を**複数行でグラフィカルに**描画します。分数は本物の
2 次元スタック（分子・横線・分母）として描かれ、値は横線（中央）の行に置かれます。指数は常に本物の Unicode
上付き数字で表され、各視覚的役割は `KGraphicalConsoleColorPalette` で着色されます。

`org.pcsoft.framework.kunit.formatter` パッケージにあり、不変でスレッドセーフな `class` です。

## 生成される内容

整った単一分母の形は 3 行で組まれ、それ以外は乗算記号で連結した上付き指数付きの 1 行の積になります。無次元の
値は着色された数値のみです。分子と分母は**表示上の**幅（ANSI 色シーケンスは幅に数えません）で横線の上に中央
揃えされます。加速度 `9.81 m/s²` は（無着色で）次のように描かれます:

```
     m
9.81 ──
     s²
```

## 設定

`KGraphicalConsoleFormatConfig` は値型です。`DEFAULT` プリセットを選ぶか独自に構築します。

| オプション         | 値 / 型                                          | 既定       |
|-------------------|-------------------------------------------------|------------|
| `palette`         | `KGraphicalConsoleColorPalette` — `CLASSIC`, `VIVID`, `MONOCHROME` | `CLASSIC` |
| `fractionBar`     | `LINE` (`─`), `HEAVY` (`━`), `ASCII` (`-`)      | `LINE`     |
| `multiplication`  | `ASTERISK` (`*`), `MIDDLE_DOT` (`·`), `CROSS` (`×`) | `MIDDLE_DOT` |
| `functionSymbols` | `KGraphicalFunctionSymbols` — `UNICODE`, `ASCII` | `UNICODE`  |

パレットは 5 つの役割（数値・記号・演算子・指数・横線）を着色します。色が空文字列の役割は無着色になります
（`MONOCHROME` が指数を無着色にする仕組み）。

## 実例

端末での速さと加速度:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KGraphicalConsoleUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

// 既定の CLASSIC パレット（ここでは色は省略）。配置:
//      km
// 90.0 ──
//      h
println(v.format(kilo.meters / hours, "%.1f", Locale.US, KGraphicalConsoleUnitFormatter()))

// 太い横線
val config = KGraphicalConsoleFormatConfig(fractionBar = KGraphicalFractionBar.HEAVY)
println(v.format(kilo.meters / hours, "%.1f", Locale.US, KGraphicalConsoleUnitFormatter(config)))
```

まったく別の記法を出力するには [カスタムフォーマッター](custom-formatters.md) を参照してください。
