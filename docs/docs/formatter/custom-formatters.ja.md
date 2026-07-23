# カスタムフォーマッター

[`format`](formatting.md) 動詞とパラメータ付き `toString` は、差し替え可能な `KUnitFormatter` を通じてテキストを
描画します。付属の `KDefaultUnitFormatter` は `"10.8 km/h"` のようなプレーンテキストを生成しますが、まったく
独自の描画を差し込めます。たとえばグラフィカルな数式レンダラー向けの **LaTeX** や **MathML**、HTML、任意の
ドメイン固有表記などです。これにより kunit を、文字列を組版された数式へ変換するサードパーティのフレームワークへ
容易に拡張できます。

## 契約

フォーマッターは必要なものすべてを 1 つの `KUnitFormatContext` で受け取り、完成した文字列を返します。

```kotlin
interface KUnitFormatter {
    fun format(context: KUnitFormatContext): String
}

data class KUnitFormatContext(
    val value: Double,            // 対象単位へ変換済みの数値
    val units: List<KUnitTerm>,   // 対象次元の項（接頭辞・指数の表示メタデータ付き）
    val pattern: String? = null,  // 数値用の任意の java.util.Formatter パターン
    val locale: Locale = Locale.getDefault(),
)
```

すべては **1 つ**のコンテキストオブジェクトで渡されるため、インターフェースは実装を壊さずに加法的に拡張（新しい
フィールドはデフォルト値を持つ）できます。よく使う構成要素として 2 つのヘルパーがあります。

- `KUnitFormatContext.renderValue()` — 数値を描画します。`pattern` が `null` なら `Double.toString()`、
  そうでなければ `String.format(locale, pattern, value)`。
- `KUnitTerm.displaySymbol` — 項の書かれたとおりの記号（`"km"`、`"h"`）。表示メタデータを尊重し、無ければ
  グループの基本記号にフォールバックします。

項の `exponent` は冪を示します（正 = 分子、負 = 分母）。指数の描画方法はフォーマッターが決めます。

## 手順：LaTeX フォーマッター

以下のフォーマッターは分子・分母の項から `\frac{...}{...}` を描画し、各単位記号に `\mathrm{...}` を使います。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.*

object LatexFormatter : KUnitFormatter {
    override fun format(context: KUnitFormatContext): String {
        // 1. 分子（指数 > 0）と分母（指数 < 0）に分割する
        val (numerator, denominator) = context.units.partition { it.exponent > 0 }

        // 2. 1 項を描画する。例: \mathrm{km} や \mathrm{s}^{2}
        fun render(terms: List<KUnitTerm>) = terms.joinToString(" ") { term ->
            val magnitude = kotlin.math.abs(term.exponent)
            val base = "\\mathrm{${term.displaySymbol}}"      // 表示メタデータを使用
            if (magnitude == 1) base else "$base^{$magnitude}"
        }

        // 3. ヘルパーで数値を描画（パターン + ロケールを尊重）
        val value = context.renderValue()

        // 4. 組み立て
        if (denominator.isEmpty()) return "$value\\,${render(numerator)}".trim()
        return "$value\\,\\frac{${render(numerator)}}{${render(denominator)}}"
    }
}
```

## 使用

フォーマッターは明示的に渡します。指定しない限りデフォルトの動作は変わりません。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 3 of meters / seconds

// カスタムフォーマッターで対象単位へ整形
v.format(kilo.meters / hours, "%.1f", Locale.US, LatexFormatter)
// "10.8\,\frac{\mathrm{km}}{\mathrm{h}}"

// または対象なしで基本単位を描画
(5 of meters).toString(pattern = null, formatter = LatexFormatter)
// "5.0\,\mathrm{m}"
```

## 注意

- フォーマッターは**状態を持たない**（したがってスレッドセーフ）に保ちます。付属の `KDefaultUnitFormatter` は
  素の `object` で、上記の `LatexFormatter` も同様です。
- `KUnitFormatContext` は対象単位へ**変換済み**の値を受け取るため、フォーマッターは単位変換を行いません。描画
  のみです。
- `units` の項は表示メタデータ（`KUnitTerm.display`）を保持します。接頭辞付きや別名の単位（`km`、`mi`、`KiB`）が
  正しく描画されるよう、記号は必ず `displaySymbol` で読み取ってください。
