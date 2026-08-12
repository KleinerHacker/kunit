# 出力の書式設定

このページは **フォーマッター** グループの概要です。すべての書式設定の入口である `format` 動詞を説明します。 さらに 2
つの専用ページがあります。

- [デフォルトフォーマッター](default-formatter.md) — 付属の `KDefaultUnitFormatter` が単位部をどう描画するか
  （標準の表記）を出力例とともに説明します。
- [カスタムフォーマッター](custom-formatters.md) — 独自の描画（LaTeX、MathML、HTML など）を差し込む方法。

すべての値は `toString()` によって **基本単位**で自身を表示でき、[`into`](../mixed-units.md) によって特定の単位へ
**読み取る**こともできます。しかし `into` は単位記号のない素の `Double` を返すだけです。`format` 動詞はその隙間を 埋めます。
`into` の表示版であり、値 **と**単位記号を `String` として返します。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds

val v = 3 of meters / seconds

v format kilo.meters / hours       // "10.799999999999999 km/h"
```

`into` と同様に、`format` はまず値を対象の単位へ読み取り（同じ次元チェックとアフィン変換を行い）、その後に対象の単位
記号を付加します。対象は書かれたとおりの単位を保持しているため、接頭辞付きや別名の単位はグループの基本記号（`m`、
`s`）ではなく、 **それ自身**の記号（`km`、`h`、`mi`）で描画されます。

## 数値の書式：パターンとロケール

中置形式は生の `Double` を描画します。 **数値部分**を丸めたりローカライズするには、数値パターンと任意の
`KLocale` を取る `format` オーバーロードを使います。

```kotlin
import org.pcsoft.framework.kunit.formatter.KLocale

v.format(kilo.meters / hours, "%.1f")                 // "10.8 km/h"
v.format(kilo.meters / hours, "%.1f", KLocale.DE_DE)  // "10,8 km/h"
```

パターンは **数値のみ**に影響し、単位部分は変わりません。無効なパターンは `IllegalArgumentException` を、
互換性のない対象次元は（`into` と同様に）`IllegalStateException` をスローします。

### `KLocale`

Kotlin には共通のロケール API がありません — `java.util.Locale` は JVM 上にのみ存在します — そのため kunit は数値の
書き方（小数点の区切り文字、桁区切り文字、桁区切りの単位）に関する独自の最小限の記述を持っています。この規約が
値とともに運ばれるため、**同じパターンはどのターゲット上でも同じ文字列を描画します**。

`KLocale.ROOT`（ドット小数点、カンマ区切り）が既定値です。事前定義された定数は一般的なケースをカバーします：
`EN_US`、`EN_GB`、`DE_DE`、`FR_FR`、`ES_ES`、`IT_IT`、`PT_BR`、`NL_NL`、`RU_RU`、`JA_JP`、`ZH_CN`、`KO_KR`、
`AR_SA`、`HI_IN`（インドの 3-then-2 桁区切りをモデル化しています）。それ以外の規約は `KLocale` を直接構築することで
表現できます。

JVM 上では `java.util.Locale` も引き続き使用できます。これを受け取るオーバーロードは JVM ソースセットで利用可能で、
`toKLocale()` を介して変換されます。

```kotlin
import java.util.Locale

v.format(kilo.meters / hours, "%.1f", Locale.GERMANY) // "10,8 km/h"（JVM のみ）
```

### サポートされるパターン

パターンは単一の数値に適用される printf のサブセットです。

```
%[flags][width][.precision]conversion
```

| 部分       | 意味                                                                             |
|------------|----------------------------------------------------------------------------------|
| flags      | `-` 左揃え・`+` 常に符号・空白で正の値を表現・`0` ゼロ埋め・`,` 桁区切り          |
| width      | 文字数の最小合計                                                                  |
| precision  | 小数部の桁数（変換指定 `f`、`e`、`E`）                                            |
| conversion | `f` 固定小数点・`e`/`E` 指数表記・`d` 整数・`s` そのまま出力                      |

`%%` はリテラルのパーセント記号を出力し、変換指定の前後にあるリテラルテキストはそのままコピーされます。

```kotlin
(1500 of meters).toString("%,.2f", KLocale.EN_US) // "1,500.00 m"
(1500 of meters).toString("%,.2f", KLocale.DE_DE) // "1.500,00 m"
(1500 of meters).toString("%.2e", KLocale.EN_US)  // "1.50e+03 m"
```

## 分数表記と積表記

組み込みフォーマッターは単位部分を次のように描画します。

| 項                         | 表示                  |
|----------------------------|-----------------------|
| 単一単位、指数 1           | `km`                  |
| 指数 ≠ 1                   | `m^2`                 |
| 分子 + ちょうど 1 個の分母 | `km/h`、`m/s^2`       |
| それ以外                   | `m*s^-3*A^-2`、`s^-1` |
| 単位なし（無次元）         | 数値のみ              |

## パターン付き `toString`

引数なしの `toString()` は変わりません（基本単位での描画）。追加のオーバーロードは同じ数値パターン／ロケールを
基本単位出力に適用します。これは対象なしの `format` 動詞です。

```kotlin
(3 of meters / seconds).toString("%.2f", KLocale.EN_US) // "3.00 m/s"
(1500 of meters).toString("%.1f", KLocale.EN_US)        // "1500.0 m"
```

## 実世界の例

ランニングのペースを変換してきれいに表示します。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*
import org.pcsoft.framework.kunit.kinematic.time.*
import org.pcsoft.framework.kunit.formatter.KLocale

val distance = 10 of kilo.meters
val time = 50 of minutes
val speed = distance / time                    // KSpeedUnitInstance

println(speed.format(kilo.meters / hours, "%.1f", KLocale.EN_US)) // "12.0 km/h"
println(speed.format(meters / seconds, "%.2f", KLocale.EN_US))    // "3.33 m/s"
```

## カスタム描画

単位部分は差し替え可能な [`KUnitFormatter`](custom-formatters.md) が生成します。付属の `KDefaultUnitFormatter`
は上記のプレーンテキストを生成します。その正確な規則と出力例は [デフォルトフォーマッター](default-formatter.md)
を参照してください。まったく異なる表記（グラフィカルな数式レンダラー向けの LaTeX や MathML、 HTML
など）を出力するには、独自のフォーマッターを実装して明示的に渡します。[カスタムフォーマッター](custom-formatters.md)
を参照してください。
